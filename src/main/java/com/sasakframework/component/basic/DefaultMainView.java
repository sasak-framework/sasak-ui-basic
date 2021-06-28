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
public class DefaultMainView extends MainWindow {

    private Tabs tabs = new Tabs();
    private ModuleTabAdd newTabButton = new ModuleTabAdd();
    private List<ModuleTab> tabList = new ArrayList<>();
    private List<ModuleBrowser> moduleBrowsers = new ArrayList<>();

    public DefaultMainView() {

        addClassName("main-window");

    }

    @Override
    public void initLayout() {

        super.initLayout();
        initTabs();

    }

 private void initTabs() {

        ModuleTab newTab = new ModuleTab("New Tab", true);
        newTab.addCloseButtonClickListener(() -> closeTab(newTab));
        tabList.add(newTab);
        tabs.add(newTab);
        tabs.add(newTabButton);

        MenuView menuView = new MenuView(getMenuList(), 0, getTopMenuItemList());
        menuView.setSizeFull();
        menuView.addModuleSelectedListener((className, menuName, menuParentId, currentMenuView) ->
                displayModule(className, menuName, menuParentId, menuView, newTab));
        moduleBrowsers.add(menuView);

        getContentContainer().getChildren().forEach(e -> e.setVisible(false));
        getContentContainer().add(menuView);

        tabs.addClassName("tab-container");

        tabs.addSelectedChangeListener(listener -> {

            if (moduleBrowsers.size() < 1) return;
            if (listener.getSource().getSelectedIndex() < 0) return;
            if (listener.getSource().getSelectedIndex() >= moduleBrowsers.size()) return;

            ModuleBrowser moduleBrowser =
                    moduleBrowsers.get(listener.getSource().getSelectedIndex());

            getContentContainer().getChildren().forEach(e -> e.setVisible(false));

            if (moduleBrowsers.contains(moduleBrowser)) {
                moduleBrowser.setVisible(true);

            } else {
                moduleBrowser.add(moduleBrowser);
                getContentContainer().add(moduleBrowser);
            }

        });

        newTabButton.addClickListener(listener -> addNewTab());
        getHeader().add(tabs);
    }

    private void closeTab(ModuleTab tab) {

        ModuleBrowser browser = moduleBrowsers.get(tabs.indexOf(tab));
        getContentContainer().remove(browser);
        moduleBrowsers.remove(browser);

        tabs.remove(newTabButton);
        tabs.remove(tab);
        tabList.remove(tab);

        if (tabList.size() < 1) {
            addNewTab();
        } else {
            tabs.setSelectedTab(tabList.get(tabList.size() - 1));

            ModuleBrowser moduleBrowser =
                    moduleBrowsers.get(tabList.size() - 1);

            getContentContainer().getChildren().forEach(e -> e.setVisible(false));

            if (moduleBrowsers.contains(moduleBrowser)) {
                moduleBrowser.setVisible(true);

            } else {
                moduleBrowser.add(moduleBrowser);
                getContentContainer().add(moduleBrowser);
            }
        }

        tabs.add(newTabButton);
    }

    private void addNewTab() {

        tabs.remove(newTabButton);

        for (ModuleTab tab : tabList) {
            tab.setLastTab(false);
        }

        ModuleTab lastTab = new ModuleTab("New Tab", true);
        lastTab.addCloseButtonClickListener(() -> closeTab(lastTab));
        tabList.add(lastTab);
        tabs.add(lastTab);
        tabs.add(newTabButton);
        tabs.setSelectedTab(lastTab);

        MenuView menuView = new MenuView(getMenuList(), 0, getTopMenuItemList());
        menuView.setSizeFull();
        menuView.addModuleSelectedListener((className, menuName, menuParentId, currentMenuView) ->
                displayModule(className, menuName, menuParentId, menuView, lastTab));
        menuView.addCaptionChangedListener(listener -> lastTab.setCaption(listener));
        moduleBrowsers.add(menuView);

        getContentContainer().getChildren().forEach(e -> e.setVisible(false));
        getContentContainer().add(menuView);

    }

    private boolean displayModule(Class<ModuleBrowser> browser, String menuName, long menuParentId,
                                  MenuView currentMenu, ModuleTab currentTab) {

        try {
            if (browser == null) {
                Dialogs.notifyError("Module not yet available.");
                return false;
            }

            ModuleBrowser moduleBrowser = browser.getDeclaredConstructor().newInstance();

            moduleBrowser.addGoBackListener(() -> {
                getContentContainer().remove(moduleBrowser);
                MenuView menuView = new MenuView(getMenuList(), menuParentId, getTopMenuItemList());
                menuView.addModuleSelectedListener((className1, menuName1, menuParentId1, currentMenuView1) ->
                        displayModule(className1, menuName1, menuParentId1, currentMenuView1, currentTab));
                menuView.addCaptionChangedListener(listener -> currentTab.setCaption(listener));
                getContentContainer().getChildren().forEach(e -> e.setVisible(false));
                moduleBrowsers.set(moduleBrowsers.indexOf(moduleBrowser), menuView);
                getContentContainer().add(menuView);

                Menu parentMenu = null;

                for (Menu menu : getMenuList()) {
                    if (menu.getId() == menuParentId) {
                        parentMenu = menu;
                    }
                }

                if (parentMenu != null) currentTab.setCaption(parentMenu.getMenuName());
            });

            getContentContainer().remove(currentMenu);
            getContentContainer().getChildren().forEach(e -> e.setVisible(false));
            moduleBrowsers.set(moduleBrowsers.indexOf(currentMenu), moduleBrowser);
            getContentContainer().add(moduleBrowser);
            currentTab.setCaption(menuName);

            return true;
        } catch (Exception ex) {
            String error = ex.toString();
            if (error.contains("java.lang.ClassNotFoundException")) {
                error = "Module not yet available.";
                Dialogs.notifyWarning(error);
            } else {
                error = "Failed to load module " + menuName + ": \n" + ex;
                Dialogs.notifyError("MenuView", 0, error);
            }

            return false;
        }
    }
}
