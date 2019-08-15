package front;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Page;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import core.Persisit.sqlCommand;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

@Route(value = "register", layout = MainLayout.class)
@PageTitle("registering form")
@Tag("register")
@HtmlImport("frontend://styles/shared-styles.html")
@JavaScript("frontend://src/javascripts/pageUtils.js")
public class Register extends VerticalLayout {

    private static Logger logger = LogManager.getLogger(Register.class);
    private Button button = new Button("Register Now!");


    public Register() {
        try {
            core.IAM.authFunction.validateAuthKey();
        } catch (Exception e) {
            e.printStackTrace();
        }



        H1 title = new H1("");
        title.addClassName("main-layout__title");

        RouterLink magic = new RouterLink(null, MainPage.class);
        magic.add(new Icon(VaadinIcon.MAGIC), new Text("Try!"));
        magic.addClassName("main-layout__nav-item");
        // Only show as active for the exact URL, but not for sub paths
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

        TextField phoneNumber = new TextField();
        phoneNumber.setWidth("75%");
        formLayout.addFormItem(phoneNumber, "Phone Number");

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
                    , phoneNumber
                    , lastName.getValue()
                    , bankNo.getValue());
            // persist in database

            // check user already register or not!
            core.userInfo newUser = new core.userInfo();
            newUser.setPhoneNumber(phoneNumber.getValue());
            newUser.setUserFirstName(firstName.getValue());
            newUser.setUserLastName(lastName.getValue());
            newUser.setBankNo(bankNo.getValue());
            newUser.setUserMail(userEmail.getValue());
            newUser.setUserCreditValue(0);
            newUser.setUserGiftValue(0);
            Transaction transaction;
            transaction = null;
            try (Session session = sqlCommand.getSessionFactory().openSession()) {
                // start a transaction
                logger.info("starting transcation, recording new user...");
                transaction = session.beginTransaction();
                // save the student objects
                session.save(newUser);
                // commit transaction
                transaction.commit();
                // revoke sms code
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

    }
}
