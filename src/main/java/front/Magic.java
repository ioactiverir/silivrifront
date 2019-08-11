package front;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.page.Page;
import com.vaadin.flow.component.page.Viewport;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import core.Game.Surprise;
import core.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Route(value = "magic", layout = MainLayout.class)
@PageTitle("Profile Information")
@HtmlImport("frontend://styles/shared-styles.html")
@JavaScript("frontend://src/javascripts/pageUtils.js")
@Tag("magic")

public class Magic extends Div {
    private static Logger logger = LogManager.getLogger(MainPage.class);

    public Magic() {


        //core.IAM.authFunction.validateAuthKey();
        H1 title = new H1("");
        title.addClassName("main-layout__title");

        RouterLink magic = new RouterLink(null,MainPage.class);
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
        switch (res.getRespType()) {
            case "TEXT":
                UI_TYPE_LABLE.setText(res.getRespType());
                UI_SUBJECT_LABLE.setText(res.getRespText());
                add(UI_TYPE_LABLE, UI_SUBJECT_LABLE);
                break;

            case "QUIZ":
                UI_TYPE_LABLE.setText(res.getRespType());
                UI_SUBJECT_LABLE.setText(res.getRespText());
                add(UI_TYPE_LABLE, UI_SUBJECT_LABLE);
                // fixme define full repsonse that contain everting
                // e.g coverage quiz, video, audio, etc. full
                break;


            case "VIDEO":
                String mediaLink= res.getRespMediaLink();
                Html videoPlay=new Html("<video id='vv' width='320' height='240' autoplay><source src='"+mediaLink+"' type='video/mp4'></video>");
                add(videoPlay);
                Page page= UI.getCurrent().getPage();
                page.executeJavaScript("var vid = document.getElementById('vv');  vid.play();");
                break;


            case "AUDIO":
                logger.info("Starting audio");
                String audioLink=res.getRespMediaLink();
                Html musicImage=new Html("<img src='frontend\\src\\img\\music.jpeg' alt='Music'>");
                Html audioPlay=new Html("<audio id='myAudio'><source src='"+audioLink+"' type='audio/mpeg'></audio>");
                add(musicImage, audioPlay);
                Page page2= UI.getCurrent().getPage();
                page2.executeJavaScript("var x = document.getElementById('myAudio'); x.play();");
                break;

            case "IMAGE":
                Html image=new Html("<img src='"+res.getRespMediaLink()+"' alt='Bingooo'>");
                add(image);
                break;


        }


    }
}
