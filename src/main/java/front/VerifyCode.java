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
import com.vaadin.flow.server.VaadinRequest;
import com.vaadin.flow.server.VaadinResponse;
import core.Cache.cache;
import core.Utility;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.Cookie;


@Route(value = "verify", layout = MainLayout.class)
@PageTitle("verify Information")
@Tag("verify")
@JavaScript("frontend://src/javascripts/pageUtils.js")
public class VerifyCode extends VerticalLayout {
    private static Logger logger = LogManager.getLogger(MainPage.class);

    final Button verify_button = new Button("Verify Code");
    private TextField sms_Code_Text = new TextField("Enter verification code:");


    public VerifyCode() {
        add(sms_Code_Text, verify_button);
        verify_button.addClickListener(buttonClickEvent -> {
            String user_Phone ="09100022217"; //VaadinRequest.getCurrent().getParameter("userPhone");
            String code_text=sms_Code_Text.getValue();
            try {
                if (core.IAM.authFunction.validateSMS(user_Phone,code_text)) {
                    String authKey = Utility.getauthKeyID(15);
                    Cookie cookies = new Cookie("authKey", authKey);
                    VaadinResponse.getCurrent().addCookie(cookies);
                    cache.sessions.put(user_Phone, authKey);
                    logger.info("set autkey {} for phone {}", authKey, user_Phone);
                    cache.sendCode.invalidate(user_Phone);
                    logger.info("forward to profile page");
                    Page page = UI.getCurrent().getPage();
                    page.executeJavaScript("redirectLocation('profile')");

                } else {
                   logger.error("Error in verifying SMS code.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


        });

    }
}


