package front;

import com.google.gson.Gson;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import core.Cache.cache;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.atomic.AtomicInteger;

@Route(value = "info", layout = MainLayout.class)
@PageTitle("Service")
@JavaScript("frontend://src/javascripts/pageUtils.js")
@Tag("service")
public class Service extends Div {
    private static Logger logger = LogManager.getLogger(Service.class);

    public Service(){
    Gson gson=new Gson();
    AtomicInteger count = new AtomicInteger();
    cache.sessions.asMap().forEach((k,v)->{
        count.getAndIncrement();
    });
        logger.info("online user {}",count.get());
    }
}
