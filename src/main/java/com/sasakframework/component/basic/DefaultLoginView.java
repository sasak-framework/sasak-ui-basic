/*
 * Copyright (c) 2021. Sasak UI. All rights reserved.
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
 */

package com.sasakframework.component.basic;

import com.helger.commons.collection.map.MapEntry;
import com.sasakframework.utilities.Dialogs;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Tag("login-view")
@JsModule("./styles/login-form-style.js")
public class DefaultLoginView extends MainWindow {

    private NullableTextField userId = new NullableTextField();
    private PasswordField password = new PasswordField();
    private Button btnLogin = new Button("Login");
    private Consumer<Map.Entry<String, String>> loginHandler = null;

    public DefaultLoginView() {



    }

    @Override
    public void initLayout() {

        super.initLayout();
        initLoginBox();

    }

    private void initLoginBox() {

        Div loginBox = new Div();
        loginBox.addClassName("login-container");

        IonIcon icon = new IonIcon("ion-ios-locked-outline");

        Div logo = new Div(icon);
        logo.addClassName("login-logo");

        loginBox.add(logo);

        btnLogin.addClassName("login-button");
        userId.setWidth("100%");
        userId.setPlaceholder("User ID");
        userId.addClassName("user-id");
        userId.setMaxLength(100);
        userId.addKeyPressListener(Key.ENTER, listener -> password.focus());

        password.setWidth("100%");
        password.setPlaceholder("Password");
        password.setAutofocus(false);
        password.addClassName("password");
        password.setMaxLength(100);
        password.addKeyPressListener(Key.ENTER, listener -> doLogin(null));

        btnLogin.setWidth("100%");

        btnLogin.addClickListener(this::doLogin);

        VerticalLayout vlForm = new VerticalLayout();
        vlForm.add(userId, password, btnLogin);
        vlForm.setWidth("100%");

        loginBox.add(vlForm);

        getContentContainer().add(loginBox);
        userId.focus();
    }

    private void doLogin(ClickEvent<Button> event) {

        if ((userId.getValue() == null) || (userId.getValue().isEmpty())) {
            Dialogs.notifyWarning("Invalid User ID.");
            userId.focus();
            return;
        }

        Map.Entry<String, String> userPassword = new MapEntry<>(userId.getValue(), password.getValue());

        if (loginHandler != null) loginHandler.accept(userPassword);
    }

    public Consumer<Map.Entry<String, String>> getLoginHandler() {
        return loginHandler;
    }

    public void setLoginHandler(Consumer<Map.Entry<String, String>> loginHandler) {
        this.loginHandler = loginHandler;
    }
}

