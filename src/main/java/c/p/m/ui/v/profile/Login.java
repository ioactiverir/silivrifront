package c.p.m.ui.v.profile;

import com.packagename.myapp.ui.MainLayout;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;


@Route(value = "login", layout = MainLayout.class)
@PageTitle("login Information")
@Tag("login")
public class Login extends VerticalLayout {
    private TextField name = new TextField("Name");
    Button sendCode = new Button("Send");

    public Login() {

//        Binder<Contact> binder = new BeanValidationBinder<>(Contact.class);
//        binder.bindInstanceFields(this);
//        binder.setBean(company);
//
//        Button save = new Button("Save");
//        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
//        save.addClickListener(event -> {
//            if (binder.validate().isOk()) {
//                // Save here
//            }
//        });

        add(name,sendCode);
    }
}


