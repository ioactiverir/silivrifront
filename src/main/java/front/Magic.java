package front;

import com.helger.commons.lang.priviledged.IPrivilegedAction;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.charts.model.Responsive;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Page;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import core.Cache.cache;
import core.Game.Surprise;
import core.Response;
import core.responseType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.vaadin.erik.TimerBar;

import java.awt.*;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;

@Route(value = "magic", layout = MainLayout.class)
@PageTitle("Profile Information")
@HtmlImport("frontend://styles/shared-styles.html")
@JavaScript("frontend://src/javascripts/pageUtils.js")
@Tag("magic")

public class Magic extends VerticalLayout {
    private static Logger logger = LogManager.getLogger(MainPage.class);

    public Magic() {


        //core.IAM.authFunction.validateAuthKey();
//        H1 title = new H1("Surfriz!");
        H1 title = new H1("");
        title.addClassName("main-layout__title");

        RouterLink magic = new RouterLink(null, MainPage.class);
        magic.add(new Icon(VaadinIcon.MAGIC), new Text("Try!"));
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



        RouterLink login = new RouterLink(null, Login.class);
        login.add(new Icon(VaadinIcon.SIGN_OUT), new Text("Logout"));
        login.addClassName("main-layout__nav-item");


        Div navigation = new Div(magic, categories, profile, login);
        navigation.addClassName("main-layout__nav");

        Div header = new Div(title, navigation);
        header.addClassName("main-layout__header");
        add(header);
        addClassName("main-layout");


        Surprise wizard = new Surprise();
        Response res = wizard.getSurprise();
        String wType = res.getRespType();
        Label UI_TYPE_LABLE = new Label("");
        Label UI_SUBJECT_LABLE = new Label("");
        Label Quiz_Answr_Options = new Label("");
        switch (res.getRespType()) {

            case "QUIZ":
                logger.info("response QUIZ");


                UI_TYPE_LABLE.setText(res.getRespType());
                UI_SUBJECT_LABLE.setText(res.getRespText());
                Quiz_Answr_Options.setText(res.getRespMessage());
                String optionQu[] = res.getRespMessage().split(",");
                TimerBar timerBar = new TimerBar(res.getRespTime() * 1000);
                Html textBody = new Html("<h2 dir='ltr' style='text-align: center;'><br />\n" +
                        "<br />" + res.getRespText() + "</h2>");
                String qres = res.getQuezzRes();
                logger.info("Quiz valid answer is a {}", qres);
                timerBar.addTimerEndedListener(e -> {
                    logger.warn("your exame finishded.");
                }); //fixme leave page or revoke quiz
                Button answer0 = new Button(optionQu[0]);
                Page page = UI.getCurrent().getPage();
                answer0.addClickListener(buttonClickEvent -> {
                    logger.info("buttom getText {}", answer0.getText());
                    if (answer0.getText().equals(qres)) {
                        //todo add balance, show messgage and go back main page
                        logger.info("answer is success");
                        Notification.show(responseType.PUZZLE_DONE_SUCCESS);
                        page.executeJavaScript("redirectLocation('magic')");

                    } else {
                        logger.info("answer is not valid.");
                        Notification.show(responseType.PUZZLE_DONE_FAILED);
                        page.executeJavaScript("redirectLocation('magic')");

                    }
                });

                Button answer1 = new Button(optionQu[1]);
                answer1.addClickListener(buttonClickEvent -> {
                    logger.info("buttom getText {}", answer1.getText());
                    if (answer1.getText().equals(qres)) {
                        logger.info("answer is success");
                        Notification.show(responseType.PUZZLE_DONE_SUCCESS);
                        page.executeJavaScript("redirectLocation('magic')");

                    } else {
                        logger.info("answer is not valid.");
                        Notification.show(responseType.PUZZLE_DONE_FAILED);
                        page.executeJavaScript("redirectLocation('magic')");

                    }
                });

                Button answer2 = new Button(optionQu[2]);
                answer2.addClickListener(buttonClickEvent -> {
                    logger.info("buttom getText {}", answer2.getText());
                    if (answer2.getText().equals(qres)) {
                        logger.info("answer is success");
                        Notification.show(responseType.PUZZLE_DONE_SUCCESS);
                        page.executeJavaScript("redirectLocation('magic')");
                    } else {
                        logger.info("answer is not valid.");
                        Notification.show(responseType.PUZZLE_DONE_FAILED);
                        page.executeJavaScript("redirectLocation('magic')");

                    }
                });

                add(textBody, answer0, answer1, answer2, timerBar);
                timerBar.start();
                break;


            case "TEXT":
                logger.info("response TEXT");
                UI_TYPE_LABLE.setText(res.getRespType());
                UI_SUBJECT_LABLE.setText(res.getRespText());
                Html noteBody = new Html("<h2 dir='rtl' style='text-align: center;'><br />\n" +
                        "<br />" + res.getRespText() + "</h2>");
                add(noteBody);
                break;

            case "VIDEO":
                logger.info("response VIDEO");
                String mediaLink = res.getRespMediaLink();
                Html videoPlay = new Html("<div><video class='center' id='vv' width='320' height='240' autoplay><source src='" + mediaLink + "' type='video/mp4'></video></div>");
                add(videoPlay);
                page = UI.getCurrent().getPage();
                page.executeJavaScript("var vid = document.getElementById('vv');  vid.play();");
                break;


            case "AUDIO":
                logger.info("response AUDIO");
                String audioLink = res.getRespMediaLink();
                Html musicImage = new Html("<div><img width=100%  height=auto src='frontend\\src\\img\\music.jpeg' alt='Music'></div>");
                Html audioPlay = new Html("<audio id='myAudio'><source src='" + audioLink + "' type='audio/mpeg'></audio>");
                add(musicImage, audioPlay);
                Page page2 = UI.getCurrent().getPage();
                page2.executeJavaScript("var x = document.getElementById('myAudio'); x.play();");
                break;

            case "IMAGE":
                logger.info("response IAMGE");
                Html image = new Html("<div><img width=100%  height=auto src='" + res.getRespMediaLink() + "' alt='Bingooo'></div>");
                Random random=new Random();

                int viewCount = random.nextInt(100000); // todo get viewCount form K,V store then set to image info
                Html viewImage = new Html("<div><img   width=10%  height=auto src='frontend\\src\\img\\view.png' alt='Music'>" + viewCount + "</div>");
                add(image, viewImage);
                break;


        }


    }
}
