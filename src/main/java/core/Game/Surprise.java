package core.Game;

import com.google.gson.Gson;
import core.Cache.cache;
import core.Response;
import core.Utility;
import core.responseType;
import front.MainPage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;

public class Surprise {
    private static Logger logger = LogManager.getLogger(MainPage.class);

    public Response getSurprise() {
        /* Select random gift, then send to the user.*/
        Random rnd = new Random();
        int selectResp =4;// rnd.nextInt(5);
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
                response.setRespType(responseType.RESPONSE_QUIZ);
                response.setRespText(qu.getQuezzSubject());
                response.setRespCharacterName(responseType.RESPONSE_CHARACTER_FERI);
                response.setRespMessage(qu.getQuezzOptions());
                response.setRespTime(qu.getQuezzTime());
                response.setRespMediaLink("NULL");
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
