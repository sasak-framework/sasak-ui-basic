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

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;

import java.util.ArrayList;
import java.util.List;

@CssImport("./styles/window-tab-styles.css")
public class ModuleTab extends Tab {

    private boolean lastTab = false;
    private String caption;

    private Icon closeIcon = VaadinIcon.CLOSE_CIRCLE.create();
    private Div closeButton = new Div(closeIcon);

    private List<CloseButtonClickListener> closeButtonClickListeners = new ArrayList<>();
    private VerticalLayout content;
    Label lblCaption = new Label("New Tab");

    public interface CloseButtonClickListener {
        void click();
    }

    public interface NewModuleRequiredListener {
        void newModuleRequired();
    }

    public ModuleTab(String caption, boolean lastTab) {

        this.caption = caption;
        this.lastTab = lastTab;

        initLayout();

        closeButton.addClickListener(listener -> {

            for (CloseButtonClickListener listener1 : closeButtonClickListeners) {

                listener1.click();

            }
        });
    }

    private void initLayout() {

        lblCaption.setText(caption);
        lblCaption.getStyle().set("font-size", "small");
        lblCaption.getStyle().set("color", "lightgray");
        lblCaption.getStyle().set("text-shadow", "rgba(0, 0, 0, 0.65) 1px 2px 2px");
        lblCaption.getStyle().set("font-weight", "bold");

        closeIcon.addClassName("tab-close-icon");

        Div tabCaption = new Div(lblCaption, closeButton);
        tabCaption.getStyle().set("display", "inline-flex");
        tabCaption.getStyle().set("align-items", "center");
        removeAll();
        add(tabCaption);

        getStyle().set("border", "1px solid");
        getStyle().set("border-top-left-radius", "5px");
        getStyle().set("border-top-right-radius", "5px");
        //getStyle().set("background", "rgba(0, 0, 0, 0.2)");
        //getStyle().set("color", "rgb(10, 115, 121)");
        getStyle().set("height", "30px");
        getStyle().set("margin-left", "2px");

    }

    public boolean isLastTab() {
        return lastTab;
    }

    public void setLastTab(boolean lastTab) {
        this.lastTab = lastTab;
        initLayout();
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
        initLayout();
    }

    public void addCloseButtonClickListener(CloseButtonClickListener listener) {
        closeButtonClickListeners.add(listener);
    }

    public VerticalLayout getContent() {
        return content;
    }

    public void setContent(VerticalLayout content) {
        this.content = content;
    }
}
