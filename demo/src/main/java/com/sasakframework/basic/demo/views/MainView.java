
/*
 * Copyright (c) 2021. Sasak UI
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Code written by Husein Musawa @ 2021
 */

package com.sasakframework.basic.demo.views;

import com.sasakframework.basic.demo.CookBiz;
import com.sasakframework.basic.demo.session.Session;
import com.sasakframework.component.basic.*;
import com.sasakframework.utilities.Dialogs;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.page.BodySize;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.shared.ui.Transport;
import com.vaadin.flow.spring.annotation.UIScope;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;

import java.util.ArrayList;
import java.util.List;

@Route("")
@PageTitle("COOK BIZ - Sasak UI Basic Demo")
@Theme(Lumo.class)
//@Push(transport = Transport.WEBSOCKET_XHR)
@PWA(name = "COOK BIZ - Sasak UI Basic Demo", shortName = "COOK BIZ")
@BodySize(height = "100vh")
@PreserveOnRefresh
@SuppressWarnings("Duplicates")
public class MainView extends DefaultMainView implements BeforeEnterObserver {

    private Tabs tabs = new Tabs();
    private ModuleTabAdd newTabButton = new ModuleTabAdd();
    private List<ModuleTab> tabList = new ArrayList<>();

    private Div contentContainer = new Div();

    private List<ModuleBrowser> moduleBrowsers = new ArrayList<>();

    public MainView() {

        if (!Session.isLoggedIn()) {
            return;
        }

        setAppTitle("COOK BIZ");
        setAppFooter("© 2021 Sasak UI");
        setAppBackground("./images/pexels-flora-westbrook-1924815-small.jpg");
        setAppIcon("./icons/icon.png");

        if (Session.getCookBiz() == null) {
            CookBiz cookBiz = new CookBiz();
            Session.setCookBiz(cookBiz);
        }

        setMenuList(Session.getCookBiz().getMenuList());


        TopMenuItem menuUser = new TopMenuItem(VaadinIcon.USER.create(),
                Session.getCurrentUser().getFullName());
        menuUser.addClickListener(listener -> {
            Dialogs.ask("Log Out", "Are you sure want to log out?",
                    () -> {Session.logOut(); UI.getCurrent().navigate(LoginView.class);});
        });

        TopMenuItem menuAbout = new TopMenuItem(VaadinIcon.QUESTION_CIRCLE_O.create(), null);
        menuAbout.addClickListener(l -> {
            Dialogs.notifyInfoEx("Vaadin Framework © Vaadin Ltd.\n" +
                    "SO Chart © Syam Pillai\n" +
                    "Photo by Flora Westbrook from Pexels\n" +
                    "\n" +
                    "Sasak UI Basic © 2021 Sasak UI\n\n" +
                    "Demo written by Husein Musawa 2021");
        });

        List<TopMenuItem> topMenuItemList = new ArrayList<>();
        topMenuItemList.add(menuUser);
        topMenuItemList.add(menuAbout);

        setTopMenuItemList(topMenuItemList);
        initLayout();

    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {

        if (!Session.isLoggedIn()) beforeEnterEvent.rerouteTo("login");

    }
}
