package front;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.page.Page;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinRequest;
import com.vaadin.flow.server.VaadinResponse;
import com.vaadin.flow.server.VaadinService;
import core.Persisit.sqlCommand;
import core.userInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.servlet.http.Cookie;
import java.util.Iterator;
import java.util.List;

@Route(value = "profile", layout = MainLayout.class)
@PageTitle("Profile Information")
@JavaScript("frontend://src/javascripts/pageUtils.js")
@Tag("profile")

public class Profile extends Div {
    private static Logger logger = LogManager.getLogger(MainPage.class);

    public Profile() throws Exception {
        userInfo employee = new userInfo();
        String userPhone;
        Cookie[] authKeyValue;
        try { //
            userPhone = VaadinRequest.getCurrent().getParameter("userPhone");
            authKeyValue = VaadinService.getCurrentRequest().getCookies();
            for (Cookie cookie : authKeyValue) {
                if (cookie.getName().equals("authKey")) {
                    String getAuthKey=cookie.getValue();
                    logger.info("get AutKey value {}", getAuthKey);
                    if (core.IAM.sessionManager.validateSession(userPhone, getAuthKey)) {
                        logger.info("authKey {} valid pair phone {}", getAuthKey, userPhone);
                        // show profile here
                        Transaction transaction = null;
                        try (Session session = sqlCommand.getSessionFactory().openSession()) {
                            transaction = session.beginTransaction();

                            String hql = "FROM userInfo E WHERE E.phoneNumber = :userPhone";
                            Query query = session.createQuery(hql);
                            query.setParameter("userPhone", userPhone);
                            List qq = query.list();
                            for (Iterator iterator1 = qq.iterator(); iterator1.hasNext(); ) {
                                employee = (userInfo) iterator1.next();
                                logger.info(" Lookup phone {} sucecess.", employee.getPhoneNumber());
                            }
                            // commit transaction
                            transaction.commit();
                            session.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                            if (transaction != null) {
                                transaction.rollback();
                            }
                            e.printStackTrace();
                        }
                        logger.info("find user name {}", employee.getUserFirstName());
                        setText(employee.getUserFirstName());
                        //fixme usr see full prfoile in table. etc
                    } else {// the authKey not matched with userphone
                        logger.info("authKey {} is NOT match userPhone {}", authKeyValue, userPhone);
                        setText("authKey is NOT match userPhone ");
//
//                        Page page = UI.getCurrent().getPage();
//                        page.executeJavaScript("redirectLocation('login')");
                    }
                } else { // the authKey is not set in cookie
                    logger.info("authKey not set in coockie for  userPhone {}", userPhone);
                    setText("authKey is NOT set in coockie ");
//                    Page page = UI.getCurrent().getPage();
//                    page.executeJavaScript("redirectLocation('login')");
                }
            }

        } catch (Exception e) { //
            logger.error("missing autkKey or userPhone.");
            setText(String.format("Invalid parameters."));
        }
    }

}