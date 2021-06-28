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
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.tabs.Tabs;

import java.util.ArrayList;
import java.util.List;

@CssImport("./styles/main-ui-styles.css")
@CssImport("./styles/window-tab-styles.css")
@CssImport("./styles/menu-styles.css")
@SuppressWarnings("Duplicates")
public class MainWindow extends Div {

    private Div header = new Div();
    private Div contentContainer = new Div();
    private FlexLayout footer = new FlexLayout();
    private String appTitle;
    private String appFooter;
    private String appIcon;
    private String appBackground;

    private Tabs tabs = new Tabs();
    private ModuleTabAdd newTabButton = new ModuleTabAdd();
    private List<ModuleTab> tabList = new ArrayList<>();
    private List<ModuleBrowser> moduleBrowsers = new ArrayList<>();
    private List<Menu> menuList;
    private List<TopMenuItem> topMenuItemList;

    public MainWindow() {

        addClassName("main-window");

    }

    public void initLayout() {

        removeAll();
        initHeader();
        initContentContainer();
        initFooter();

    }

    private void initHeader() {

        Image icon = new Image();
        if (this.appIcon != null) {
            icon.setSrc(this.appIcon);
        }
        icon.addClassName("header-icon");

        Span appTitle = new Span(this.appTitle);
        appTitle.addClassName("header-text");

        header.removeAll();
        header.addClassName("header");
        header.add(icon, appTitle);

        add(header);
    }

    private void initContentContainer() {

        contentContainer.removeAll();
        contentContainer.addClassName("content-container");
        if (appBackground != null)
            contentContainer.getStyle().set("background-image", "url('" + appBackground + "')");
        add(contentContainer);

    }

    private void initFooter() {

        Span appFooter = new Span(this.appFooter);
        appFooter.addClassName("footer-text");

        footer.removeAll();
        footer.addClassName("footer");
        footer.add(appFooter);

        add(footer);
    }

    public Div getContentContainer() {
        return contentContainer;
    }

    public Div getHeader() {
        return header;
    }

    public String getAppTitle() {
        return appTitle;
    }

    public void setAppTitle(String appTitle) {
        this.appTitle = appTitle;
    }

    public String getAppFooter() {
        return appFooter;
    }

    public void setAppFooter(String appFooter) {
        this.appFooter = appFooter;
    }

    public String getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(String appIcon) {
        this.appIcon = appIcon;
    }

    public List<Menu> getMenuList() {
        return menuList;
    }

    public void setMenuList(List<Menu> menuList) {
        this.menuList = menuList;
    }

    public String getAppBackground() {
        return appBackground;
    }

    public void setAppBackground(String appBackground) {
        this.appBackground = appBackground;
    }

    public List<TopMenuItem> getTopMenuItemList() {
        return topMenuItemList;
    }

    public void setTopMenuItemList(List<TopMenuItem> topMenuItemList) {
        this.topMenuItemList = topMenuItemList;
    }
}
