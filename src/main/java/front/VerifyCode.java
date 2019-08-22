package front;


import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Page;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinResponse;
import com.vaadin.flow.server.VaadinService;
import core.Cache.cache;
import core.DataModel.responseType;
import core.Persisit.sqlCommand;
import core.DataModel.userInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.servlet.http.Cookie;
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
        try {
            Cookie[] authKeyValue = VaadinService.getCurrentRequest().getCookies();
            for (Cookie cookie : authKeyValue) {
                if (cookie.getName().equals("authKey")) {
                    String tmpSessionVal = cookie.getValue();
                    logger.info("verifying tmp session {} value.", tmpSessionVal);
                    if (cache.tmpSessions.asMap().containsValue(tmpSessionVal)) {
                        logger.info("tmp session {} validation successful.", tmpSessionVal);
                        /* main body*/

                        FormLayout verifyForm = new FormLayout();
                        Div header = new Div();
                        header.addClassName("main-layout__header");
                        setSizeFull();
                        verifyForm.addFormItem(div, "");
                        verifyForm.addFormItem(sms_Code_Text, "");
                        sms_Code_Text.setWidth("75%");
                        verifyForm.addFormItem(verify_button, "");
                        add(header, verifyForm);

                        verify_button.addClickListener(buttonClickEvent -> {
                            String code_text = sms_Code_Text.getValue();
                            //lookup phone from authKey
                            AtomicReference<String> user_Phone = new AtomicReference<>("");
                            // extract phone form session
                            cache.tmpSessions.asMap().forEach((k, v) -> {
                                if (v.equals(tmpSessionVal)) {
                                    logger.info("tmp session {} for phone {} matched.", tmpSessionVal, k);
                                    user_Phone.set(k);
                                }
                            });
                            try {
                                if (core.IAM.authFunction.validateSMS(user_Phone.get(), code_text)) {
                                    // invalidate SMS code, tmpSession
                                    logger.info("invalidate sendCode for {}", user_Phone.get());
                                    cache.sendCode.invalidate(user_Phone.get());
                                    logger.info("invalidate tmp Session {}", tmpSessionVal);
                                    cache.tmpSessions.invalidate(user_Phone.get());

                                    // Get Session and set cookie

                                    String authKey = core.Utility.getauthKeyID(responseType.SESSION_SIZE);
                                    logger.info("permanent session resptype size {}", responseType.SESSION_SIZE);
                                    Cookie cookies = new Cookie("authKey", authKey);
                                    VaadinResponse.getCurrent().addCookie(cookies);

                                    cache.sessions.put(user_Phone.get(), authKey);
                                    logger.info("set permanent autkey {} for phone {}", authKey, user_Phone.get());


                                    // if user already registered
                                    logger.info("check registering status for phone {}", user_Phone.get());
                                    Transaction transaction = null;
                                    try (Session session = sqlCommand.getSessionFactory().openSession()) {
                                        transaction = session.beginTransaction();
                                        String hql = "FROM userInfo E WHERE E.phoneNumber = :userPhone";
                                        Query query = session.createQuery(hql);
                                        query.setParameter("userPhone", user_Phone.get());
                                        List result = query.list();
                                        transaction.commit();
                                        if (!result.isEmpty()) {
                                            logger.info("phone {} already registered.forward main page",
                                                    user_Phone.get());
                                            Page page = UI.getCurrent().getPage();
                                            page.executeJavaScript("redirectLocation('')");

                                        } else {
                                            logger.info("phone {} not registered. forward register page",
                                                    user_Phone.get());
                                            Page page = UI.getCurrent().getPage();
                                            page.executeJavaScript("redirectLocation('register')");
                                        }
                                        session.close();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        if (transaction != null) {
                                            transaction.rollback();
                                        }
                                        e.printStackTrace();
                                    }
                                } else {
                                    logger.error("Error in verifying SMS  {} code.", code_text);
                                    Notification.show("Error SMS code.");
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });

                        /* main body */
                    } else {
                        logger.error("tmp session is not valid or expired.");
                        Page page = UI.getCurrent().getPage();
                        page.executeJavaScript("redirectLocation('login')");
                    }
                }

            }
        } catch (Exception e) {
            logger.error("AutKey (tmp session) null pointer exception");
            Page page = UI.getCurrent().getPage();
            page.executeJavaScript("redirectLocation('login')");
        }
    }
}


