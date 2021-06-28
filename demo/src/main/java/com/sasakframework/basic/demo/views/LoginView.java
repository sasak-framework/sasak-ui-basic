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
import com.sasakframework.basic.demo.master.MasterUser;
import com.sasakframework.basic.demo.session.Session;
import com.sasakframework.component.basic.DefaultLoginView;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.router.*;
import com.vaadin.flow.spring.annotation.UIScope;


@Tag("login-view")
@Route("login")
@PageTitle("COOK BIZ - Sasak UI Basic Demo")
public class LoginView extends DefaultLoginView implements BeforeEnterObserver {

    public LoginView() {

        setAppTitle("COOK BIZ");
        setAppFooter("© 2021 Sasak UI");
        setAppBackground("./images/pexels-flora-westbrook-1924815-small.jpg");
        setAppIcon("./icons/icon.png");
        initLayout();
        setLoginHandler(u -> {

            try {
                Thread.sleep(500);
            } catch (Exception ex) {

            }

            MasterUser user = new MasterUser(0, u.getKey(), u.getKey(), u.getValue());
            Session.login(user);
            UI.getCurrent().navigate(MainView.class);

        });

    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {

        if (Session.isLoggedIn()) {
            beforeEnterEvent.rerouteTo(MainView.class);
        }

    }
}

