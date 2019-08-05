package c.p.m.ui.v.profile;

import com.packagename.myapp.ui.MainLayout;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "profile", layout = MainLayout.class)
@PageTitle("Profile Information")
@Tag("profile")
public class Profile extends Div {
    public Profile() {
        setText("Hello world");
    }
}