package front;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Page;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinResponse;
import core.Cache.cache;
import core.Utility;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.Cookie;


@Route(value = "login", layout = MainLayout.class)
@PageTitle("login Information")
@Tag("login")
@JavaScript("frontend://src/javascripts/pageUtils.js")
public class Login extends VerticalLayout {
    private static Logger logger = LogManager.getLogger(MainPage.class);

    final Button sendCode = new Button("Send Code");
    private TextField phonenumber = new TextField("Enter Phone");

    public Login() {

        add(phonenumber, sendCode);
        sendCode.addClickListener(buttonClickEvent -> {

            // sample
            String authKey = core.Utility.getauthKeyID(15);
            String phone=phonenumber.getValue();
            Cookie cookies = new Cookie("authKey", authKey);
            VaadinResponse.getCurrent().addCookie(cookies);

            cache.sessions.put(phone, authKey);
            logger.info("set autkey {} for phone {}", authKey, phone);
            Page page = UI.getCurrent().getPage();
            page.executeJavaScript("getCode('" + phone + "')");
            //  send sms code for usr phone
            String code= Utility.getSMSCode();
            logger.info("set sms code {} for phone {}",code,phone);
            cache.sendCode.put(phone,code);
            // todo call sms gateway  and code.
            logger.info("forward to verify sms code page");
           // page = UI.getCurrent().getPage();
           // page.executeJavaScript("redirectLocation('verify')");
         });

    }
}

