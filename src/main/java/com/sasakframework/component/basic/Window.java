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

import com.sasakframework.utilities.Dialogs;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CssImport("./styles/window-styles.css")
public class Window extends Div {

    private String moduleName = "";
    private long id = 0;

    private Div windowBox = new Div();
    private Div windowTitleContainer = new Div();
    private Span windowTitle = new Span();
    private Div closeButton = new Div();
    private Span closeButtonText = new Span("X");
    private IonIcon closeButtonIcon = new IonIcon("ion-close-round");
    private Div contentContainer = new Div();
    private Div windowFooter = new Div();

    private List<Component> components = new ArrayList<>();

    private String title;

    public Window() {

        initLayout();

    }

    public Window(String title) {

        this.title = title;
        initLayout();

    }

    private void initLayout() {

        windowTitle.setText(title);
        windowTitle.addClassName("window-title");

        closeButtonText.addClassName("window-close-button-text");

        closeButton.addClassName("window-close-button");
        closeButtonIcon.addClassName("window-close-button-icon");
        closeButton.add(closeButtonIcon);
        closeButton.addClickListener(listener -> close());

        windowTitleContainer.addClassName("window-title-container");
        windowTitleContainer.add(windowTitle, closeButton);

        windowBox.addClassName("window");
        windowBox.add(windowTitleContainer);

        contentContainer.addClassName("window-content");
        windowBox.add(contentContainer);

        windowFooter.addClassName("window-footer");
        windowBox.add(windowFooter);

        if (!hasClassName("layout-modal")) addClassName("layout-modal");
        if (hasClassName("modal-hidden")) removeClassName("modal-hidden");

        add(windowBox);

    }

    public Div getContent() {
        return contentContainer;
    }

    public Div getFooter() {
        return windowFooter;
    }

    public void close() {getElement().removeFromParent();}

    @Override
    public Optional<String> getTitle() {
        return this.getTitle();
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
        windowTitle.setText(title);
    }

    @Override
    public void setWidth(String width) {

        windowBox.setWidth(width);

    }

    @Override
    public String getWidth() {
        return windowBox.getWidth();
    }

    @Override
    public void setHeight(String height) {

        windowBox.setHeight(height);

    }

    @Override
    public String getHeight() { return windowBox.getHeight();}

    @Override
    public void setSizeFull() {

        setWidth("calc(100% - 20px)");
        setHeight("calc(100vh - 110px)");

    }

    public boolean contains(Component component) {

        if (super.getChildren() == null) return false;
        return super.getChildren().anyMatch(c -> c.equals(component));
    }

    public void notifyError(String error) {

        Dialogs.notifyError(moduleName, id, error);

    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public long getItemId() {
        return id;
    }

    public void setItemId(long id) {
        this.id = id;
    }
}
