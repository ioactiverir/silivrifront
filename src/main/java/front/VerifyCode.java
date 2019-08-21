package front;


import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Page;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinResponse;
import com.vaadin.flow.server.VaadinService;
import core.Cache.cache;
import core.Persisit.sqlCommand;
import core.Utility;
import core.responseType;
import core.userInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.servlet.http.Cookie;
import java.util.Iterator;
import java.util.List;
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
    private Div div = new Div();

    public VerifyCode() {

        // first check if there is a session or not
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
        } catch (Exception e) {
            logger.error("AutKey Null Pointer exception...");
            Page page = UI.getCurrent().getPage();
            page.executeJavaScript("redirectLocation('login')");
        }


        setSizeFull();
        Div header = new Div();
        header.addClassName("main-layout__header");

        FormLayout verifyForm = new FormLayout();
        verifyForm.addFormItem(div, "");
        verifyForm.addFormItem(sms_Code_Text, "");
        sms_Code_Text.setWidth("75%");
        verifyForm.addFormItem(verify_button, "");
        add(header, verifyForm);
        verify_button.addClickListener(buttonClickEvent -> {
            //lookup phone from authKey
            AtomicReference<String> user_Phone = new AtomicReference<>("");
            String sessionValue = "";
            Cookie[] authKeyValue = VaadinService.getCurrentRequest().getCookies();
            //first get authKey
            for (Cookie cookie : authKeyValue) {
                if (cookie.getName().equals("authKey")) {
                    logger.info("fetch authKey {} in coockie.", cookie.getValue());
                    sessionValue = cookie.getValue();
                }
            }
            // extract phone form session
            String finalSessionValue = sessionValue;
            cache.sessions.asMap().forEach((k, v) -> {
                if (v.equals(finalSessionValue)) {
                    logger.info("session {} for phone {} matched.", finalSessionValue, k);
                    user_Phone.set(k);
                }
            });
            String code_text = sms_Code_Text.getValue();

            try {
                if (core.IAM.authFunction.validateSMS(user_Phone.get(), code_text)) {
                    logger.info("invalidate sendCode for {}", user_Phone.get());
                    cache.sendCode.invalidate(user_Phone.get());

                    // if user already registered
                    logger.info("check registering status for phone {}",user_Phone.get());
                    userInfo employee=new userInfo();
                    Transaction transaction = null;
                    try (Session session = sqlCommand.getSessionFactory().openSession()) {
                        transaction = session.beginTransaction();

                        String hql = "FROM userInfo E WHERE E.phoneNumber = :userPhone";
                        Query query = session.createQuery(hql);
                        query.setParameter("userPhone", user_Phone.get());
                        List qq = query.list();
                        List result = query.list();
                        transaction.commit();
                        if (!result.isEmpty()) {
                            logger.info("forward main page");
                            Page page = UI.getCurrent().getPage();
                            page.executeJavaScript("redirectLocation('')");

                        } else {
                            logger.info("forward register page");
                            Page page = UI.getCurrent().getPage();
                            page.executeJavaScript("redirectLocation('register')");
                        }

                        // commit transaction
                        session.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                        if (transaction != null) {
                            transaction.rollback();
                        }
                        e.printStackTrace();
                    }










                    // else forward to register form

                } else {
                    logger.error("Error in verifying SMS  {} code.",code_text);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


        });

    }
}


