package front;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Page;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.VaadinService;
import core.Cache.cache;
import core.DataModel.userInfo;
import core.Persisit.sqlCommand;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.servlet.http.Cookie;

@Route(value = "register", layout = MainLayout.class)
@PageTitle("Register")
@Tag("register")
@HtmlImport("frontend://styles/shared-styles.html")
@JavaScript("frontend://src/javascripts/pageUtils.js")
public class Register extends VerticalLayout {

    private static Logger logger = LogManager.getLogger(Register.class);
    private Button button = new Button("Register Now!");
    private String phoneNumber;

    public Register() {


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

                        H1 title = new H1("");
                        title.addClassName("main-layout__title");

                        RouterLink magic = new RouterLink(null, MainPage.class);
                        magic.add(new Icon(VaadinIcon.MAGIC), new Text("Try!"));
                        magic.addClassName("main-layout__nav-item");

                        magic.setHighlightCondition(HighlightConditions.sameLocation());

                        RouterLink categories = new RouterLink(null, CategoriesList.class);
                        categories.add(new Icon(VaadinIcon.CASH), new Text("Money"));
                        categories.addClassName("main-layout__nav-item");


                        RouterLink profile = new RouterLink(null, Profile.class);
                        profile.add(new Icon(VaadinIcon.USER), new Text("Profile"));
                        profile.addClassName("main-layout__nav-item");


                        RouterLink register = new RouterLink(null, Register.class);
                        register.add(new Icon(VaadinIcon.USER), new Text("Register"));
                        register.addClassName("main-layout__nav-item");


                        RouterLink logout = new RouterLink(null, LogOut.class);
                        logout.add(new Icon(VaadinIcon.SIGN_OUT), new Text("Logout"));
                        logout.addClassName("main-layout__nav-item");


                        Div navigation = new Div(magic, categories, profile, logout);
                        navigation.addClassName("main-layout__nav");

                        Div header = new Div(title, navigation);
                        header.addClassName("main-layout__header");
                        add(header);
                        addClassName("main-layout");


                        setSizeFull();


                        FormLayout formLayout = new FormLayout();

                        TextField firstName = new TextField();
                        firstName.setWidth("75%");
                        formLayout.addFormItem(firstName, "First Name");

                        TextField lastName = new TextField();
                        lastName.setWidth("75%");
                        formLayout.addFormItem(lastName, "Last Name");

                        TextField formPhoneNumber = new TextField();
                        formPhoneNumber.setWidth("75%");
                        formPhoneNumber.setValue(phoneNumber);
                        formPhoneNumber.setEnabled(false);
                        formLayout.addFormItem(formPhoneNumber, "Phone Number");

                        TextField userEmail = new TextField();
                        userEmail.setWidth("75%");
                        formLayout.addFormItem(userEmail, "Email");


                        TextField bankNo = new TextField();
                        bankNo.setWidth("75%");
                        formLayout.addFormItem(bankNo, "Bank Account");
                        formLayout.addFormItem(button, "");

                        add(formLayout);


                        button.addClickListener(buttonClickEvent -> {
                            //todo store form info in base64.
                            logger.info("User info:{} {} {} {} {}", firstName.getValue()
                                    , userEmail.getValue()
                                    , formPhoneNumber
                                    , lastName.getValue()
                                    , bankNo.getValue());
                            userInfo newUser = new userInfo();
                            newUser.setPhoneNumber(formPhoneNumber.getValue());
                            newUser.setUserFirstName(firstName.getValue());
                            newUser.setUserLastName(lastName.getValue());
                            newUser.setBankNo(bankNo.getValue());
                            newUser.setUserMail(userEmail.getValue());
                            newUser.setUserCreditValue(0);
                            newUser.setUserGiftValue(0);
                            Transaction transaction;
                            transaction = null;
                            try (Session session = sqlCommand.getSessionFactory().openSession()) {
                                logger.info("starting transaction, recording new user...");
                                transaction = session.beginTransaction();
                                session.save(newUser);
                                transaction.commit();
                                session.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                                if (transaction != null) {
                                    transaction.rollback();
                                }
                                e.printStackTrace();
                            }
                            //ok, done. now redirect  to main page.
                            Page page = UI.getCurrent().getPage();
                            page.executeJavaScript("redirectLocation('magic')");

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