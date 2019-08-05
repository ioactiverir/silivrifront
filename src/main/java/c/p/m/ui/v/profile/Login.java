package c.p.m.ui.v.profile;

import com.packagename.myapp.ui.MainLayout;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Page;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;


@Route(value = "login", layout = MainLayout.class)
@PageTitle("login Information")
@Tag("login")
//@HtmlImport("frontend://src/views/reviewslist/callSendCode.html")
@JavaScript("frontend://src/views/reviewslist/callSendCode.js")
public class Login extends VerticalLayout {
    final Button sendCode = new Button("Send");


    private void ok(ClickEvent event) {
        //event.getButton() .setCaption ("OK!");
        sendCode.setText("YOU");
        Page page = UI.getCurrent().getPage();

        page.executeJavaScript("getCode()");

    }

    private TextField phonenumber = new TextField("Login Code");

    public Login() {
        //UI.getCurrent().getPage().addJavaScript("frontend://src/views/reviewslist/callSendCode.js");
        add(phonenumber, sendCode);
//        phonenumber.addValueChangeListener(textFieldStringComponentValueChangeEvent -> {
//           textFieldStringComponentValueChangeEvent.getValue();
//        });
        sendCode.addClickListener(buttonClickEvent -> {
            //sendCode.setText("CLicked");
            Page page = UI.getCurrent().getPage();
            page.executeJavaScript("getCode('"+phonenumber.getValue()+"')");

        });

    }
}


