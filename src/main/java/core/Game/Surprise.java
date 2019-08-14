package core.Game;

import com.vaadin.flow.server.VaadinService;
import core.Cache.cache;
import core.Response;
import core.Utility;
import core.responseType;
import front.MainPage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.Cookie;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class Surprise {
    private static Logger logger = LogManager.getLogger(MainPage.class);

    public Response getSurprise() {

        /*
        Read authKey form cookie and make a quezzID based on sha1(authKeyID,randomString(25),Date,Time)
         */
        Cookie[] authKeyValue = VaadinService.getCurrentRequest().getCookies();
        String sessionAuthKeyValue = null;
        String queezId=null;
        for (Cookie cookie : authKeyValue) {
            if (cookie.getName().equals("authKey")) {
                sessionAuthKeyValue = cookie.getValue();
            } else {
                logger.error("something is wrong, authKey error during bulding surprize!!");
                //todo kill session and redirect to login page
            }
        }
        //get datetime
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String dateTime=dtf.format(now);

        try {
            queezId= Utility.sha1(sessionAuthKeyValue+dateTime+ Utility.getauthKeyID(30));
            logger.info("sessionID {} generated successfully.", queezId);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }



        /* Select random gift, then send to the user.*/
        Random rnd = new Random();
        int selectResp =1;//rnd.nextInt(5);
        Response response = new Response();
        switch (selectResp) {
            case 0:
                response.setRespType(responseType.RESPONSE_TEXT);
                response.setRespText(Utility.getRandomText());
                response.setRespCharacterName(responseType.RESPONSE_CHARACTER_FERI);
                response.setRespMediaLink("NULL");
                break;
            case 1:
                QueezMaker queezMaker=new QueezMaker();
                Quezz qu=queezMaker.queezBuidler();
                qu.setQuezzId(queezId);
                response.setRespType(responseType.RESPONSE_QUIZ);
                response.setRespText(qu.getQuezzSubject());
                response.setRespCharacterName(responseType.RESPONSE_CHARACTER_FERI);
                response.setRespMessage(qu.getQuezzOptions());
                response.setRespTime(qu.getQuezzTime());
                response.setQuezzRes(qu.getQuezzResult());
                response.setRespId(qu.getQuezzId());
                response.setRespMediaLink("NULL");
                // set queezId to current user.
                cache.quizSession.put(sessionAuthKeyValue,queezId);
                break;
            case 2:
                response.setRespType(responseType.RESPONSE_VIDEO);
                response.setRespText("VIDEO");
                response.setRespCharacterName(responseType.RESPONSE_CHARACTER_FERI);
                response.setRespMediaLink(Utility.getRandomVideo());
                break;
            case 3:
                response.setRespType(responseType.RESPONSE_AUDIO);
                response.setRespText("AUDIO");
                response.setRespCharacterName(responseType.RESPONSE_CHARACTER_FERI);
                response.setRespMediaLink(Utility.getRandomAudio());
                break;
            case 4:
                response.setRespType(responseType.RESPONSE_IMAGE);
                response.setRespText("IMAGE");
                response.setRespCharacterName(responseType.RESPONSE_CHARACTER_FERI);
                response.setRespMediaLink(Utility.getRandomImage());
                break;
        }

        return response;
    }
}
