package core.Game;

import core.Cache.cache;
import core.Utility;
import org.junit.Test;

import java.util.Random;

public class QueezMaker {
    public Quezz queezBuidler() {
        long quizBuildTime=0L;
        Random rndQuezz = new Random();
        int selectQuezz = rndQuezz.nextInt(2);
        String resp[];
        String quizRandomId=Utility.getRandomID(50);
        switch (selectQuezz) {
            case 0:

                resp = Utility.generteQuezz(1);
                Quezz simpleQuezz = new Quezz();
                simpleQuezz.setQuezzName("simple");
                simpleQuezz.setQuezzTime(5);
                simpleQuezz.setQuezzType("math");
//                        if (!cache.userCredit.asMap().containsKey(userPhone)) {
//                            logger.info("user {} playing free", userPhone);
//                            simpleQuezz.setQuezzMessage("Charge Account and get more gifts!");
//                        }
                simpleQuezz.setQuezzSubject(resp[0]);
                simpleQuezz.setQuezzOptions(resp[1]);
                simpleQuezz.setQuezzResult(resp[2]);
                simpleQuezz.setQuezzCredit("1000");
                simpleQuezz.setQuezzId(quizRandomId);
                return simpleQuezz;
            case 1:
                resp = Utility.generteQuezz(2);
                Quezz mediumQuezz = new Quezz();
                mediumQuezz.setQuezzName("meduim");
                mediumQuezz.setQuezzTime(10);
                mediumQuezz.setQuezzType("math");

                mediumQuezz.setQuezzSubject(resp[0]);
                mediumQuezz.setQuezzOptions(resp[1]);
                mediumQuezz.setQuezzResult(resp[2]);
                mediumQuezz.setQuezzCredit("2000");
                mediumQuezz.setQuezzId(quizRandomId);

                return mediumQuezz;
            case 2:
                resp = Utility.generteQuezz(3);
                Quezz complexQuezz = new Quezz();
                complexQuezz.setQuezzName("complex");
                complexQuezz.setQuezzTime(15);
                complexQuezz.setQuezzType("math");

                complexQuezz.setQuezzSubject(resp[0]);
                complexQuezz.setQuezzOptions(resp[1]);
                complexQuezz.setQuezzResult(resp[2]);
                complexQuezz.setQuezzCredit("3000");
                complexQuezz.setQuezzId(quizRandomId);
                return complexQuezz;
        }
        //fuck!!!
        Quezz nullQueez = new Quezz();
        return nullQueez;
    }
}
