package front;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Page;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.VaadinService;
import core.Cache.cache;
import core.DataModel.Response;
import core.DataModel.responseType;
import core.Game.Surprise;
import core.Utility;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.vaadin.erik.TimerBar;

import javax.servlet.http.Cookie;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

@Route(value = "magic", layout = MainLayout.class)
@PageTitle("Magic")
@HtmlImport("frontend://styles/shared-styles.html")
@JavaScript("frontend://src/javascripts/pageUtils.js")
@Tag("magic")

public class Magic extends VerticalLayout {
    private static Logger logger = LogManager.getLogger(Magic.class);
    private String phoneNumber;

    public Magic() {
        try {
            Cookie[] authKeyValue = VaadinService.getCurrentRequest().getCookies();
            for (Cookie cookie : authKeyValue) {
                if (cookie.getName().equals("authKey")) {
                    String getSessionValue = cookie.getValue();
                    logger.info("verifying session {} value.", getSessionValue);
                    if (cache.sessions.asMap().containsValue(getSessionValue)) {
                        logger.info("session {} validation successful.", getSessionValue);
                        /* main body */

                        // extract phoneNumber
                        cache.sessions.asMap().forEach((k, v) -> {
                            if (v.equals(getSessionValue)) {
                                logger.info("lookup phone number {} for session {}", k, getSessionValue);
                                phoneNumber = k;
                            }
                        });

                        RouterLink magic = new RouterLink(null, MainPage.class);
                        magic.add(new Icon(VaadinIcon.MAGIC), new Text("New"));
                        magic.addClassName("main-layout__nav-item");
                        // Only show as active for the exact URL, but not for sub paths
                        magic.setHighlightCondition(HighlightConditions.sameLocation());


                        RouterLink categories = new RouterLink(null, CategoriesList.class);
                        categories.add(new Icon(VaadinIcon.CASH), new Text("Money"));
                        categories.addClassName("main-layout__nav-item");


                        RouterLink profile = new RouterLink(null, Profile.class);
                        profile.add(new Icon(VaadinIcon.USER), new Text("Profile"));
                        profile.addClassName("main-layout__nav-item");


                        RouterLink register = new RouterLink(null, Register.class);
                        register.add(new Icon(VaadinIcon.USER), new Text("Register"));
                        register.addClassName("main-layout__nav-item");


                        RouterLink logout = new RouterLink(null, LogOut.class);
                        logout.add(new Icon(VaadinIcon.SIGN_OUT), new Text("Logout"));
                        logout.addClassName("main-layout__nav-item");


                        Div navigation = new Div(magic, categories, profile, logout);
                        navigation.addClassName("main-layout__nav");

                        Div header = new Div(navigation);
                        header.addClassName("main-layout__header");
                        add(header);
                        addClassName("main-layout");

                        Surprise wizard = new Surprise();
                        Response res = wizard.getSurprise();
                        switch (res.getRespType()) {

                            case "QUIZ":
                                logger.info("response QUIZ");
                                Page page = UI.getCurrent().getPage();


                                FormLayout quizForm = new FormLayout();
                                String optionQu[] = res.getRespMessage().split(",");

                                String qres = res.getQuezzRes();

                                logger.info("Quiz valid answer is a {}", qres);
                                String finalSessionAuthKeyValue = getSessionValue;
                                logger.info("Queez info session {}: quzzSession {}"
                                        , finalSessionAuthKeyValue, res.getRespId());

                                Button answer0 = new Button(optionQu[0]);
                                Button answer1 = new Button(optionQu[1]);
                                Button answer2 = new Button(optionQu[2]);
                                //fixme leave page or revoke quiz
                                TimerBar timerBar = new TimerBar(res.getRespTime() * 1000);

                                answer0.addClickListener(buttonClickEvent -> {
                                    if (answer0.getText().equals(qres)) {
                                        if (cache.quizSession.asMap().containsValue(res.getRespId())) {
                                            long getCurrentTime = System.currentTimeMillis();
                                            logger.info("curent time {}", Utility.epochConvert(getCurrentTime));
                                            logger.info("quizRandomId:{}", res.getRespId());
                                            AtomicLong getQuizTime = new AtomicLong();
                                            cache.quizTTL.asMap().forEach((k, v) -> {
                                                //  logger.info("QuizTTL k:{} v:{} ",k,Utility.epochConvert(v));
                                                if (k.equals(res.getRespId())) {
                                                    getQuizTime.set(v);
                                                }
                                            });
                                            logger.info("lookup quizTimeStamp {}", Utility.epochConvert(getQuizTime.get()));
                                            long duration = ((getCurrentTime - getQuizTime.get()) / 1000);
                                            logger.info("duration: {} secend.", duration);
                                            logger.info("time to answer: {} second.", res.getRespTime());
                                            if (duration < res.getRespTime()) {
                                                logger.info("invalidate quizTTL {}", res.getRespId());
                                                cache.quizTTL.invalidate(res.getRespId());
                                                cache.quizSession.invalidate(finalSessionAuthKeyValue);
                                                logger.info("answer is success");
                                                Notification.show(responseType.PUZZLE_DONE_SUCCESS);
                                                //Disable other buttons
                                                answer1.setEnabled(false);
                                                answer2.setEnabled(false);
                                                logger.info("app credit {} to phone {}", 100, phoneNumber);
                                                // todo read and set creditValue per quiz, we assumed fixed value
                                                core.Game.Credit.appendUserCredit(phoneNumber, 100);
                                                timerBar.stop();
                                            } else {
                                                Notification.show("Time expired!");
                                            }
                                        } else {
                                            cache.quizSession.invalidate(finalSessionAuthKeyValue);
                                            logger.warn("queez time is over.");
                                            Notification.show("Game Over!");
                                            answer0.setEnabled(false);
                                            answer1.setEnabled(false);
                                            answer2.setEnabled(false);

                                        }
                                    } else {
                                        cache.quizSession.invalidate(finalSessionAuthKeyValue);
                                        logger.info("answer is not valid.");
                                        Notification.show(responseType.PUZZLE_DONE_FAILED);
                                        answer0.setEnabled(false);
                                        answer1.setEnabled(false);
                                        answer2.setEnabled(false);

                                    }
                                });

                                answer1.addClickListener(buttonClickEvent -> {
                                    if (answer1.getText().equals(qres)) {
                                        if (cache.quizSession.asMap().containsValue(res.getRespId())) {
                                            long getCurrentTime = System.currentTimeMillis();
                                            logger.info("curent time {}", Utility.epochConvert(getCurrentTime));
                                            logger.info("quizRandomId:{}", res.getRespId());
                                            AtomicLong getQuizTime = new AtomicLong();
                                            cache.quizTTL.asMap().forEach((k, v) -> {
                                                //  logger.info("QuizTTL k:{} v:{} ",k,Utility.epochConvert(v));
                                                if (k.equals(res.getRespId())) {
                                                    getQuizTime.set(v);
                                                }
                                            });
                                            logger.info("lookup quizTimeStamp {}", Utility.epochConvert(getQuizTime.get()));
                                            long duration = ((getCurrentTime - getQuizTime.get()) / 1000);
                                            logger.info("duration: {} secend.", duration);
                                            logger.info("time to answer: {} second.", res.getRespTime());
                                            if (duration < res.getRespTime()) {
                                                logger.info("invalidate quizTTL {}", res.getRespId());
                                                cache.quizTTL.invalidate(res.getRespId());
                                                cache.quizSession.invalidate(finalSessionAuthKeyValue);
                                                logger.info("answer is success");
                                                Notification.show(responseType.PUZZLE_DONE_SUCCESS);
                                                //Disable other buttons
                                                answer0.setEnabled(false);
                                                answer2.setEnabled(false);
                                                logger.info("app credit {} to phone {}", 100, phoneNumber);
                                                core.Game.Credit.appendUserCredit(phoneNumber, 100);
                                                timerBar.stop();
                                            } else {
                                                Notification.show("Time expired!");
                                            }
                                        } else {
                                            cache.quizSession.invalidate(finalSessionAuthKeyValue);
                                            logger.warn("queez time is over.");
                                            Notification.show("Game Over!");
                                            answer0.setEnabled(false);
                                            answer1.setEnabled(false);
                                            answer2.setEnabled(false);

                                        }
                                    } else {
                                        cache.quizSession.invalidate(finalSessionAuthKeyValue);
                                        logger.info("answer is not valid.");
                                        Notification.show(responseType.PUZZLE_DONE_FAILED);
                                        answer0.setEnabled(false);
                                        answer1.setEnabled(false);
                                        answer2.setEnabled(false);

                                    }
                                });

                                answer2.addClickListener(buttonClickEvent -> {
                                    if (answer2.getText().equals(qres)) {
                                        if (cache.quizSession.asMap().containsValue(res.getRespId())) {
                                            long getCurrentTime = System.currentTimeMillis();
                                            logger.info("curent time {}", Utility.epochConvert(getCurrentTime));
                                            logger.info("quizRandomId:{}", res.getRespId());
                                            AtomicLong getQuizTime = new AtomicLong();
                                            cache.quizTTL.asMap().forEach((k, v) -> {
                                                //  logger.info("QuizTTL k:{} v:{} ",k,Utility.epochConvert(v));
                                                if (k.equals(res.getRespId())) {
                                                    getQuizTime.set(v);
                                                }
                                            });
                                            logger.info("lookup quizTimeStamp {}", Utility.epochConvert(getQuizTime.get()));
                                            long duration = ((getCurrentTime - getQuizTime.get()) / 1000);
                                            logger.info("duration: {} secend.", duration);
                                            logger.info("time to answer: {} second.", res.getRespTime());
                                            if (duration < res.getRespTime()) {
                                                logger.info("invalidate quizTTL {}", res.getRespId());
                                                cache.quizTTL.invalidate(res.getRespId());
                                                cache.quizSession.invalidate(finalSessionAuthKeyValue);
                                                logger.info("answer is success");
                                                Notification.show(responseType.PUZZLE_DONE_SUCCESS);
                                                //Disable other buttons
                                                answer0.setEnabled(false);
                                                answer1.setEnabled(false);
                                                logger.info("app credit {} to phone {}", 100, phoneNumber);
                                                core.Game.Credit.appendUserCredit(phoneNumber, 100);
                                                timerBar.stop();
                                            } else {
                                                Notification.show("Time expired!");
                                            }
                                        } else {
                                            cache.quizSession.invalidate(finalSessionAuthKeyValue);
                                            logger.warn("queez time is over.");
                                            Notification.show("Game Over!");
                                            answer0.setEnabled(false);
                                            answer1.setEnabled(false);
                                            answer2.setEnabled(false);

                                        }
                                    } else {
                                        cache.quizSession.invalidate(finalSessionAuthKeyValue);
                                        logger.info("answer is not valid.");
                                        Notification.show(responseType.PUZZLE_DONE_FAILED);
                                        answer0.setEnabled(false);
                                        answer1.setEnabled(false);
                                        answer2.setEnabled(false);

                                    }
                                });

                                Html quizCharImg = new Html("<img  width=75%  height=auto src='frontend\\src\\img\\magic5.gif' alt='Quiz'>");
                                quizForm.addFormItem(quizCharImg, "");
                                Label question = new Label(res.getRespText());
                                question.addClassName("font-label");
                                quizForm.addFormItem(question, "");
                                quizForm.addFormItem(answer0, "");
                                quizForm.addFormItem(answer1, "");
                                quizForm.addFormItem(answer2, "");
                                //  quizForm.addFormItem(timerBar, "Answer Time");

                                add(quizForm, timerBar);
                                timerBar.start();
                                break;


                            case "TEXT":
                                logger.info("response TEXT");
                                Html noteBody = new Html("<h2 dir='rtl' style='text-align: center;'><br />\n" +
                                        "<br />" + res.getRespText() + "</h2>");
                                add(noteBody);
                                break;

                            case "VIDEO":
                                logger.info("response VIDEO");

                                final Div div1 = new Div();
                                div1.setText("");
                                String mediaLink = res.getRespMediaLink();
                                Html videoPlay = new Html("<div><video class='center' id='vv' width=100% height=auto autoplay><source src='" + mediaLink + "' type='video/mp4'></video></div>");
                                add(div1, videoPlay);
                                page = UI.getCurrent().getPage();
                                page.executeJavaScript("var vid = document.getElementById('vv');  vid.play();");
                                break;


                            case "AUDIO":
                                logger.info("response AUDIO");
                                String audioLink = res.getRespMediaLink();
                                Html musicImage = new Html("<div><img width=100%  height=auto src='frontend\\src\\img\\player.gif' alt='Music'></div>");
                                Html audioPlay = new Html("<audio id='myAudio'><source src='" + audioLink + "' type='audio/mpeg'></audio>");
                                add(musicImage, audioPlay);
                                Page page2 = UI.getCurrent().getPage();
                                page2.executeJavaScript("var x = document.getElementById('myAudio'); x.play();");
                                break;

                            case "IMAGE":
                                logger.info("response IAMGE");


                                final Div div2 = new Div();
                                Html image = new Html("<div><img width=100%  height=auto src='" + res.getRespMediaLink() + "' alt='Bingooo'></div>");
                                Random random = new Random();

                                int viewCount = random.nextInt(100000); // todo get viewCount form K,V store then set to image info
                                Html viewImage = new Html("<div><img  align=center width=10%  height=auto src='frontend\\src\\img\\view.png' alt='View'>" + viewCount + "</div>");
                                add(div2, image, viewImage);
                                break;


                        }

                        /*main body*/
                    } else {
                        logger.error("Session {} is not valid or expired.", getSessionValue);
                        Page page = UI.getCurrent().getPage();
                        logger.info("redirect to login page");
                        page.executeJavaScript("redirectLocation('login')");
                    }
                }
            }
        } catch (Exception e) {
            logger.error("AutKey null pointer exception.");
            Page page = UI.getCurrent().getPage();
            logger.info("redirect to login page");
            page.executeJavaScript("redirectLocation('login')");
        }


    }
}
