package front;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import static com.ibm.icu.util.LocalePriorityList.add;

@Route(value = "register", layout = MainLayout.class)
@PageTitle("registering form")
@Tag("register")
@JavaScript("frontend://src/javascripts/pageUtils.js")
public class Register extends Div {
    private static Logger logger = LogManager.getLogger(MainPage.class);
    private TextField firstName=new TextField("نام");
    private TextField lastName=new TextField("نام خانوادگی");
    private TextField bankAccount=new TextField("شماره کارت بانکی");
    private Button button=new Button("تایید");

    public Register() {
        H1 title=new H1("فرم ثبت نام");
        title.addClassName("main-layout__title");
        Label subject=new Label("ورود اطلاعات");

        UI.getCurrent().add(subject,firstName,lastName,bankAccount,button);
        button.addClickListener(buttonClickEvent -> {
        logger.info("name:{} family:{} bankNo:{}",firstName.getValue(),lastName.getValue(),bankAccount.getValue());
        });
    }
}
