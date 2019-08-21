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
import core.userInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.servlet.http.Cookie;
import java.awt.*;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Route(value = "profile", layout = MainLayout.class)
@PageTitle("Profile")
@JavaScript("frontend://src/javascripts/pageUtils.js")
@Tag("profile")

public class Profile extends Div {
    private static Logger logger = LogManager.getLogger(Profile.class);

    public Profile() throws Exception {
        try {
            core.IAM.authFunction.validateAuthKey();
        } catch (Exception e) {
            e.printStackTrace();
        }

        userInfo employee = new userInfo();
        try { //
            Cookie[] authKeyValue = VaadinService.getCurrentRequest().getCookies();
            for (Cookie cookie : authKeyValue) {
                if (cookie.getName().equals("authKey")) {
                    String getAuthKey = cookie.getValue();
                    logger.info("get AutKey value {}", getAuthKey);
                    // show profile here
                    Transaction transaction = null;
                    try (Session session = sqlCommand.getSessionFactory().openSession()) {
                        transaction = session.beginTransaction();

                        String hql = "FROM userInfo E WHERE E.phoneNumber = :userPhone";
                        Query query = session.createQuery(hql);
                        // extarct user phone form session
                        AtomicReference<String> getUserPhone = new AtomicReference<>("");
                        cache.sessions.asMap().forEach((k, v) -> {
                            if (v.equals(cookie.getValue())) {
                                logger.info("session {} for phone {} matched.", cookie.getValue(), k);
                                getUserPhone.set(k);
                            }
                        });
                        query.setParameter("userPhone", getUserPhone.get());
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
                }

            }

        } catch (Exception e) { //
            e.printStackTrace();
            setText(String.format("Error 500"));
        }
    }

}