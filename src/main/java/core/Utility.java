package core;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.page.Page;
import core.Cache.cache;
import front.MainPage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Random;

public class Utility {
    private static Logger logger = LogManager.getLogger(Utility.class);

    public static void importImageLocation() {
        File folder = new File("src/main/webapp/frontend/src/img");
        File[] listOfFiles = folder.listFiles();
        Long ID = responseType.IMAGE_ID_START_PINT;
        assert listOfFiles != null;
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                logger.info("found" + listOfFiles[i].getName());
                cache.mediaLocation.put(ID, listOfFiles[i].getName());
                ID--;
            }
        }
    }


    //generate random sms code
    public static String getSMSCode() {
        Random rnd = new Random();
        String smscode = String.valueOf(rnd.nextInt(1000000));
        return smscode;
    }

    // function to generate a random session ID of length n
    public static String getauthKeyID(int n) {

        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvxyz";
        StringBuilder sb = new StringBuilder(n);
        for (int i = 0; i < n; i++) {
            int index = (int) (AlphaNumericString.length()
                    * Math.random());
            sb.append(AlphaNumericString
                    .charAt(index));
        }
        return sb.toString();
    }

    public static String getRandomImage() {
        Random ran = new Random();
        int ranImgId = ran.nextInt(4);
        return "frontend/src/img/" + ranImgId + ".jpg";
    }

    public static String getRandomVideo() {
        return "frontend\\src\\video\\b.mp4";
    }

    public static String getRandomAudio() {
        return "frontend\\src\\audio\\1.mp3";
    }

    public static String getRandomText() {
        //todo read data from DB and load in cache has been initialized during service startup
        Random rnd = new Random();
        int getRnadomContent = rnd.nextInt(2);
        List textList = new ArrayList();
        textList.add("2KjYp9mE2KfYqtixINin2LIg2LPYsdi52Kog2YbZiNixINmF24zYr9mI2YbbjCDahtuM2Ycg2J8K2LPYsdi52Kog2KzZhdi5INmIINis2YjYsSDZg9ix2K/ZhiDYrtmI2YbZhyDYr9ixINmF2YjYp9is2YfZhyDYqNinINmF2YfZhdin2YYg2LPYsdiy2K/ZhyA6KSk=");
        textList.add("2KjZhyDYr9mI2LPYqtmFINmF24wg2q/ZhSDaqduM2YEg2b7ZiNmE2Kog2obZgtiv2LEg2YLYtNmG2q/ZhwrZhduMINqv2Ycg2obYsdmFINmF2LTZh9ivINi52YXZiNmFINin2LIg2KLZhNmF2KfZhiDYqNix2KfZhSDYotmI2LHYr9mHIQ==");
        textList.add("2KfYsiDZhdin2K/Ysdio2LLYsdqv2YUg2YXbjNm+2LHYs9mFINi02YXYpyDYqNiy2LHar9iq2LHbjNmGINuM2Kcg2K7Yp9mE2Ycg2KjYstix2q8K2YXbjNqv2Ycg2KfYsiDZhti42LEg2LPZhiDZhduM2q/bjCDYnwrZviDZhiDZviDYp9iyINmG2LjYsSDZgtivINmIINmI2LLZhiDZiCDZhduM2LLYp9mGINqp2YTYs9iq2LHZiNmEINiu2YjZhiDZhduM2q/ZhQ==");
        textList.add("2LfYqNmCINiq2K3ZgtuM2YLYp9iqINmF2YYg2YXZh9mF2KrYsduM2YYg2LnYp9mF2YQg2LfZhNin2YIg2KfYstiv2YjYp9is2YchCg==");

        String decodedString;
        byte[] decodedBytes = Base64.getDecoder().decode((String) textList.get(getRnadomContent));
        decodedString = new String(decodedBytes);
        return decodedString;
    }

    public static int getUserCreditValue(String userId) throws NullPointerException {
        int value = Integer.parseInt(cache.userGifts.asMap().get(userId));
        return value;
    }

    public static String[] generteQuezz(int quezzComplexity) {
        String result[] = new String[3];
        Random digit = new Random();
        Random r = new Random();
        int n1 = 0;
        int n2 = 0;
        int n3 = 0;
        int nOption1 = 0;
        int nOption2 = 0;
        int realResp = 0;

        switch (quezzComplexity) {
            case 1:
                n1 = digit.nextInt(5);
                n2 = digit.nextInt(5);
                n3 = digit.nextInt(5);
                nOption1 = digit.nextInt(20);
                nOption2 = digit.nextInt(15);
                break;
            case 2:
                n1 = digit.nextInt(10);
                n2 = digit.nextInt(10);
                n3 = digit.nextInt(10);
                nOption1 = digit.nextInt(30);
                nOption2 = digit.nextInt(40);
                break;
            case 3:
                n1 = digit.nextInt(15);
                n2 = digit.nextInt(15);
                n3 = digit.nextInt(15);
                nOption1 = digit.nextInt(50);
                nOption2 = digit.nextInt(60);
                break;
        }
        realResp = n1 + n2 + n3;
        String subject = n1 + "+" + n2 + "+" + n3 + "=?";
        String responseOption1 = realResp + "," + nOption1 + "," + nOption2;
        String responseOption2 = nOption1 + "," + realResp + "," + nOption2;
        String responseOption3 = nOption2 + "," + nOption1 + "," + realResp;
        String[] list = {responseOption1, responseOption2, responseOption3};

        result[0] = subject;
        result[1] = list[r.nextInt(list.length)];
        result[2] = String.valueOf(realResp);
        return result;

    }
}
