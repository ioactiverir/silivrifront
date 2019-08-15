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
import com.vaadin.flow.server.VaadinService;
import core.Cache.cache;
import core.Utility;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.Cookie;
import java.util.concurrent.atomic.AtomicReference;


@Route(value = "verify", layout = MainLayout.class)
@PageTitle("verify Information")
@Tag("verify")
@JavaScript("frontend://src/javascripts/pageUtils.js")
public class VerifyCode extends VerticalLayout {
    //todo change class per class logger
    private static Logger logger = LogManager.getLogger(VerifyCode.class);

    final Button verify_button = new Button("Verify Code");
    private TextField sms_Code_Text = new TextField("Enter verification code:");


    public VerifyCode() {
        add(sms_Code_Text, verify_button);
        verify_button.addClickListener(buttonClickEvent -> {
            //lookup phone from authKey
            AtomicReference<String> user_Phone = new AtomicReference<>("");
            String sessionValue = "";
            Cookie[] authKeyValue = VaadinService.getCurrentRequest().getCookies();
            //first get authKey
            for (Cookie cookie : authKeyValue) {
                if (cookie.getName().equals("authKey")) {
                    logger.info("fetch authKey {} in coockie.",cookie.getValue());
                    sessionValue = cookie.getValue();
                }
            }
            // extract phone form session
            String finalSessionValue = sessionValue;
            cache.sessions.asMap().forEach((k, v) -> {
                if (v.equals(finalSessionValue)) {
                    logger.info("session {} for phone {} matched.",finalSessionValue,k);
                    user_Phone.set(k);
                }
            });
            String code_text = sms_Code_Text.getValue();

            try {
                if (core.IAM.authFunction.validateSMS(user_Phone.get(), code_text)) {
                    logger.info("invalidate sendCode for {}",user_Phone.get());
                    cache.sendCode.invalidate(user_Phone.get());
                    logger.info("forward main page");
                    Page page = UI.getCurrent().getPage();
                    page.executeJavaScript("redirectLocation('')");

                } else {
                    logger.error("Error in verifying SMS code.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


        });

    }
}


