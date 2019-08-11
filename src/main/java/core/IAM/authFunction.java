package core.IAM;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.page.Page;
import com.vaadin.flow.server.VaadinResponse;
import com.vaadin.flow.server.VaadinService;
import core.Cache.cache;
import core.Persisit.sqlCommand;
import core.Utility;
import front.MainPage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.servlet.http.Cookie;
import javax.swing.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class authFunction {

    private static Logger logger = LogManager.getLogger(MainPage.class);

    public static void validateAuthKey() throws Exception {
        Cookie[] authKeyValue = VaadinService.getCurrentRequest().getCookies();
        for (Cookie cookie : authKeyValue) {
            if (cookie.getName().equals("authKey")) {
                String readSessionValue = cookie.getValue();
                logger.info("verifying session value.");
                if (cache.sessions.asMap().containsValue(readSessionValue)) {
                    logger.info("session validation successful.");
                } else {
                    logger.error("Session is not valid or expired.");
                }
            } else {
                logger.error("Error reading cookie values");
                //set guest session and redirect to  login page.
                String defaultAuthKey = "0000";
                Cookie cookies = new Cookie("authKey", defaultAuthKey);
                VaadinResponse.getCurrent().addCookie(cookies);
                Page page = UI.getCurrent().getPage();
                page.executeJavaScript("redirectLocation('login')");
            }
        }
    }


    // validate smscode
    public static boolean validateSMS(String userPhone, String SMS_Code) throws Exception {
        AtomicBoolean authorzie = new AtomicBoolean(false);
        try {
            cache.sendCode.asMap().forEach((k, v) -> {
                if (k.equals(userPhone) && v.equals(SMS_Code)) {
                    authorzie.set(true);
                } else {
                    authorzie.set(false);
                }
            });
            if (!authorzie.get() == true) {
                return false;
            } else {
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    ///
    public static boolean phoneRegisterStatus(String phoneNo) throws Exception {
        Transaction transaction = null;
        try (Session session = sqlCommand.getSessionFactory().openSession()) {

            transaction = session.beginTransaction();
            String hql = "FROM userInfo  where phoneNumber= :phoneNumber";
            Query query = session.createQuery(hql);
            query.setParameter("phoneNumber", phoneNo);
            List result = query.list();
            transaction.commit();
            session.close();
            if (!result.isEmpty()) {
                return true;
            } else {
                return false;
            }
            // commit transaction
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }

        return false;
    }

}
