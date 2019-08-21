package front;

import com.google.common.collect.Lists;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.page.Page;
import com.vaadin.flow.component.page.Viewport;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.VaadinService;
import core.Cache.cache;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.Cookie;
import java.util.ArrayList;
import java.util.Random;

@Tag("main-page")
@Route("")
@HtmlImport("frontend://styles/shared-styles.html")
@Viewport("width=device-width, minimum-scale=1.0, initial-scale=1.0, user-scalable=yes")
@JavaScript("frontend://src/javascripts/pageUtils.js")

public class MainPage extends Div {
    private static Logger logger = LogManager.getLogger(MainPage.class);

    public MainPage() {

//        try {
//            core.IAM.authFunction.validateAuthKey();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//

        try {

            Cookie[] authKeyValue = VaadinService.getCurrentRequest().getCookies();
            for (Cookie cookie : authKeyValue) {
                if (cookie.getName().equals("authKey")) {
                    String readSessionValue = cookie.getValue();
                    logger.info("verifying session {} value.", readSessionValue);
                    if (cache.sessions.asMap().containsValue(readSessionValue)) {
                        logger.info("session validation successful.");
                    } else {
                        logger.error("Session is not valid or expired.");
                        Page page = UI.getCurrent().getPage();
                        page.executeJavaScript("redirectLocation('login')");
                    }
                }

            }
        }catch (Exception e){

            logger.error("AutKey Null pointer exception...");
            Page page = UI.getCurrent().getPage();
            page.executeJavaScript("redirectLocation('login')");
            //e.printStackTrace();
        }











        H1 title = new H1("");
        title.addClassName("main-layout__title");

        RouterLink magic = new RouterLink(null,Magic.class);
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


        RouterLink logout = new RouterLink(null, LogOut.class);
        logout.add(new Icon(VaadinIcon.SIGN_OUT), new Text("Logout"));
        logout.addClassName("main-layout__nav-item");


        Div navigation = new Div(magic, categories, profile, logout);
        navigation.addClassName("main-layout__nav");

        Div header = new Div(title, navigation);
        header.addClassName("main-layout__header");

// add magic box here
        ArrayList<String> wizardList= Lists.newArrayList("magic1.gif",
                "magic2.gif",
                "magic3.gif",
                "magic4.gif",
                "magic5.gif");

        Random rnd=new Random();
        int selectItem=rnd.nextInt(4);
        Html magicBox = new Html("<div><br><a href='/magic'><img  align='center' width=100%  height=auto src='frontend\\src\\img\\"+ wizardList.get(selectItem) +"' alt='Music'></a></div>");
        add(header,magicBox);

        addClassName("main-layout");

//        Page page = UI.getCurrent().getPage();
//        page.executeJavaScript("redirectLocation('magic')");

    }
}




