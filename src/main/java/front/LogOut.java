package front;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Page;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinService;
import core.Cache.cache;
import core.Game.Surprise;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.Cookie;
import java.util.concurrent.ExecutionException;

@Route(value = "logout", layout = MainLayout.class)
@PageTitle("logout")
@Tag("logout")
@HtmlImport("frontend://styles/shared-styles.html")
@JavaScript("frontend://src/javascripts/pageUtils.js")
public class LogOut extends VerticalLayout {
    private static Logger logger = LogManager.getLogger(LogOut.class);

    public LogOut() {
        Cookie[] authKeyValue = VaadinService.getCurrentRequest().getCookies();
        for (Cookie cookie : authKeyValue) {
            if (cookie.getName().equals("authKey")) {
                String authKey = cookie.getValue();
                logger.info("revoke authkey {}.", authKey);
                String finalAuthKey = authKey;
                cache.sessions.asMap().forEach((k, v) -> {
                    if (v.equals(finalAuthKey)) {
                        //clean up everything.
                        cache.sessions.invalidate(k);
                        try {
                            logger.info("revoke quizSession {}",cache.quizSession.get(authKey));
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                        cache.quizSession.invalidate(authKey);
                    }
                });


            }
        }


        logger.info("redirect to main page.");
        Page page = UI.getCurrent().getPage();
        page.executeJavaScript("redirectLocation('')");
        //todo service should return metrics
    }
}
