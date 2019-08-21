/*
 * Copyright 2000-2017 Vaadin Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package front;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Page;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinService;
import core.Cache.cache;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.Cookie;

/**
 * Displays the list of available categories, with a search filter as well as
 * buttons to add a new category or edit existing ones.
 */
@Route(value = "categories", layout = MainLayout.class)
@JavaScript("frontend://src/javascripts/pageUtils.js")
@PageTitle("Categories List")
public class CategoriesList extends VerticalLayout {
    private static Logger logger = LogManager.getLogger(CategoriesList.class);

    public CategoriesList() {

        // first check if there is a session or not
        try {
            Cookie[] authKeyValue = VaadinService.getCurrentRequest().getCookies();
            for (Cookie cookie : authKeyValue) {
                if (cookie.getName().equals("authKey")) {
                    String readSessionValue = cookie.getValue();
                    logger.info("verifying session {} value.", readSessionValue);
                    if (cache.sessions.asMap().containsValue(readSessionValue)) {
                        logger.info("session validation successful.");


                        setSizeFull();
                        Div header = new Div();
                        header.addClassName("main-layout__header");

                        FormLayout formLayout = new FormLayout();

                        Div div = new Div();
                        formLayout.addFormItem(div, "");

                        Label banner = new Label();
                        banner.setText("Transfer money to my bank account.");
                        Button transferButton = new Button();
                        transferButton.setText("Transfer now!");
                        Button backButton = new Button();
                        backButton.setText("Back");

                        formLayout.addFormItem(banner, "");
                        formLayout.addFormItem(div, "");
                        formLayout.addFormItem(transferButton, "");
                        formLayout.addFormItem(backButton, "");
                        add(header, formLayout);
                        backButton.addClickListener(buttonClickEvent -> {
                            logger.info("forward to main page");

                            Page page = UI.getCurrent().getPage();
                            page.executeJavaScript("redirectLocation('')");

                        });


                    } else {
                        logger.error("Session is not valid or expired.");
                        Page page = UI.getCurrent().getPage();
                        page.executeJavaScript("redirectLocation('login')");
                    }
                }

            }
        } catch (Exception e) {
            logger.error("AutKey Null Pointer exception...");
            Page page = UI.getCurrent().getPage();
            page.executeJavaScript("redirectLocation('login')");
        }


    }

}
