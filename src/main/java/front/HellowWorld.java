package front;

import com.vaadin.flow.templatemodel.TemplateModel;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;

/**
 * A Designer generated component for the hellow-world template.
 *
 * Designer will add and remove fields with @Id mappings but
 * does not overwrite or otherwise change this file.
 */
@Tag("hellow-world")
@HtmlImport("src/html/view/hellow-world.html")
public class HellowWorld extends PolymerTemplate<HellowWorld.HellowWorldModel> {

    /**
     * Creates a new HellowWorld.
     */
    public HellowWorld() {
        // You can initialise any data required for the connected UI components here.
    }

    /**
     * This model binds properties between HellowWorld and hellow-world
     */
    public interface HellowWorldModel extends TemplateModel {
        // Add setters and getters for template properties here.
    }
}
