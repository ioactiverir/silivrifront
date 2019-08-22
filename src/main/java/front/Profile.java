package front;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.page.Page;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinService;
import core.Cache.cache;
import core.Persisit.sqlCommand;
import core.DataModel.userInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.servlet.http.Cookie;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Route(value = "profile", layout = MainLayout.class)
@PageTitle("Profile")
@JavaScript("frontend://src/javascripts/pageUtils.js")
@Tag("profile")

public class Profile extends Div {
    private static Logger logger = LogManager.getLogger(Profile.class);
    private String phoneNumber;

    public Profile() {

        userInfo employee = new userInfo();
        try {
            Cookie[] authKeyValue = VaadinService.getCurrentRequest().getCookies();
            for (Cookie cookie : authKeyValue) {
                if (cookie.getName().equals("authKey")) {
                    String getSessionValue = cookie.getValue();
                    logger.info("verifying session {} value.", getSessionValue);
                    if (cache.sessions.asMap().containsValue(getSessionValue)) {
                        logger.info("session {} validation successful.", getSessionValue);
                        // extract phoneNumber
                        cache.sessions.asMap().forEach((k, v) -> {
                            if (v.equals(getSessionValue)) {
                                logger.info("lookup phone number {} for session {}", k, getSessionValue);
                                phoneNumber = k;
                            }
                        });
                        /* main body */
                        Transaction transaction = null;
                        try (Session session = sqlCommand.getSessionFactory().openSession()) {
                            transaction = session.beginTransaction();

                            String hql = "FROM userInfo E WHERE E.phoneNumber = :userPhone";
                            Query query = session.createQuery(hql);
                            query.setParameter("userPhone", phoneNumber);
                            List qq = query.list();
                            for (Object o : qq) {
                                employee = (userInfo) o;
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
                        logger.info("find  user phone {}", employee.getPhoneNumber());
                        FormLayout resualtForm = new FormLayout();
                        Label firstName = new Label(employee.getUserFirstName());
                        Label lastName = new Label(employee.getUserLastName());
                        Label bankNo = new Label(employee.getBankNo());
                        Label creditValue = new Label(String.valueOf(employee.getUserCreditValue()));
                        resualtForm.addFormItem(firstName, "Name");
                        resualtForm.addFormItem(lastName, "Fanily");
                        resualtForm.addFormItem(bankNo, "Bank Card");
                        resualtForm.addFormItem(creditValue, "Balance");
                        Button backButton = new Button();
                        backButton.setText("Back");
                        resualtForm.addFormItem(backButton, "");
                        add(resualtForm);
                        backButton.addClickListener(buttonClickEvent -> {
                            logger.info("back to main page");
                            Page page = UI.getCurrent().getPage();
                            page.executeJavaScript("redirectLocation('')");

                        });
                        /* main body*/
                    } else {
                        logger.error("Session {} is not valid or expired.", getSessionValue);
                        Page page = UI.getCurrent().getPage();
                        logger.info("redirect to login page");
                        page.executeJavaScript("redirectLocation('login')");
                    }
                }
            }
        } catch (Exception e) {
            logger.error("AutKey null pointer exception.");
            Page page = UI.getCurrent().getPage();
            logger.info("redirect to login page");
            page.executeJavaScript("redirectLocation('login')");
        }

    }
}