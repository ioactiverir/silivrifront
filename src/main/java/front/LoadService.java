package front;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "start", layout = MainLayout.class)
@PageTitle("Initialization...")
@Tag("start")
public class LoadService {

    public LoadService() {
        core.Utility.importImageLocation();
    }
}
