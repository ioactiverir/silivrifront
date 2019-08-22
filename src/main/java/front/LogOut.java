package front;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Page;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.PWA;
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
        logger.info("Starting logout process.");
        Cookie[] authKeyValue = VaadinService.getCurrentRequest().getCookies();
        for (Cookie cookie : authKeyValue) {
            if (cookie.getName().equals("authKey")) {
                logger.info("found authkey {}", cookie.getValue());
                String finalAuthKey = cookie.getValue();
                cache.sessions.asMap().forEach((k, v) -> {
                    if (v.equals(cookie.getValue())) {
                        //clean up everything.
                        logger.info("lookup phone {} success. ", k);
                        cache.sessions.invalidate(k);
                        if (cache.quizSession.equals(k)){
                            logger.info("revoke quizSession  for phone {}",k);
                            cache.quizSession.invalidate(k);
                        } else {
                            logger.warn("not found enrty for  phone {} in quizSession",k);
                        }

                    }
                });
            }
        }


        logger.info("redirect to login page.");
        Page page = UI.getCurrent().getPage();
        page.executeJavaScript("redirectLocation('login')");
        //todo service should return metrics
    }
}
