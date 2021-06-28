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

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;

public class GroupBox extends Div {

    private String title;

    private Div titleBar = new Div();
    private Label titleLabel = new Label();
    private Div contentLayout = new Div();

    public GroupBox(String title) {

        this.title = title;
        initLayout();
    }

    private void initLayout() {

        titleBar.setWidth("100%");
        titleBar.setHeight("30px");
        titleBar.getStyle().set("background-color", "lightslategray");
        titleBar.getStyle().set("display", "flex");

        titleLabel.setText(title);
        titleLabel.getStyle().set("color", "white");
        titleLabel.getStyle().set("padding-top", "5px");
        titleLabel.getStyle().set("padding-left", "5px");
        titleBar.add(titleLabel);

        contentLayout.setWidth("calc(100% - 20px)");
        contentLayout.setHeight("calc(100% - 30px)");
        contentLayout.getStyle().set("padding", "10px");
        getStyle().set("border", "1px solid lightgray");
        getStyle().set("border-radius", "3px");

        add(titleBar);
        add(contentLayout);
    }

    public Div getTitleBar() {
        return titleBar;
    }

    public void setTitleBar(Div titleBar) {
        this.titleBar = titleBar;
    }

    public Div getContentLayout() {
        return contentLayout;
    }

    public void setContentLayout(Div contentLayout) {
        this.contentLayout = contentLayout;
    }
}
