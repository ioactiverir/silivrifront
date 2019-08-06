package c.p.m.ui.v.profile;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.page.Page;
import com.vaadin.flow.component.page.Viewport;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinRequest;

@Route("")
@Viewport("width=device-width")
@JavaScript("frontend://src/views/reviewslist/pageUtils.js")
public class MyApp extends Div {
    public MyApp() {
        String userPhone=VaadinRequest.getCurrent().getParameter("userPhone");
        String mockPhone="123";// assume the ur has been registered, loged and have a valid session
        if (userPhone == null) {
            setText(String.format("Invalid parameters."));
        } else {
            if (userPhone.equals(mockPhone)) {
                setText(String.format("Welcome %s.", userPhone));
            } else {
//                setText(String.format("Register | Login "));
//                Button button = new Button("GO", event -> UI.getCurrent().navigate("login"));
//                add(button);
                Page page = UI.getCurrent().getPage();
                page.executeJavaScript("redirectLocation('login')");


            }
        }
    }
}