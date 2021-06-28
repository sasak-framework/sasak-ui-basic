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
import com.vaadin.flow.component.icon.VaadinIcon;

import java.util.ArrayList;
import java.util.List;

public class MenuView extends ModuleBrowser {

    private Div mainMenu = new Div();
    private Div topMenu = new Div();
    private Div menuItemContainer = new Div();
    private long parentId = 0;
    private List<CaptionChangedListener> captionChangedListeners = new ArrayList<>();
    private List<ModuleSelectedListener> moduleSelectedListeners = new ArrayList<>();
    private List<Menu> menuList;
    private List<TopMenuItem> topMenuList;
    public interface CaptionChangedListener {
        void captionChanged(String newCaption);
    }

    public interface ModuleSelectedListener {
        boolean moduleSelected(Class browser, String menuName, long menuParentId,
                               MenuView currentMenu);
    }

    public MenuView(List<Menu> menuList, long parentId) {

        this.menuList = menuList;
        this.parentId = parentId;
        initLayout();

    }

    public MenuView(List<Menu> menuList, long parentId, List<TopMenuItem> topMenuList) {

        this.menuList = menuList;
        this.parentId = parentId;
        this.topMenuList = topMenuList;
        initLayout();

    }

    private void initLayout() {

        mainMenu.addClassName("main-menu");
        topMenu.addClassName("top-menu");
        menuItemContainer.addClassName("menu-item-container");

        initTopMenu();
        loadMenu(this.parentId);

        mainMenu.add(menuItemContainer);
        setSizeFull();
        getStyle().set("display", "flex");
        add(mainMenu);
        setAlignItems(Alignment.CENTER);

    }

    private void initTopMenu() {

        Div topMenuSpacer = new Div();
        topMenuSpacer.addClassName("top-menu-spacer");

        topMenu.add(topMenuSpacer);

        if (topMenuList != null) {
            for (TopMenuItem item : topMenuList) {
                topMenu.add(item);
            }
        }

        mainMenu.add(topMenu);

    }

    private void loadMenu(long parentId) {

        List<Menu> menus = new ArrayList<>();

        for (Menu m : menuList) {
            if (m.getParentId() == parentId) {
                menus.add(m);
            }
        }

        if ((menus == null) || (menus.size() < 1)) {

            Menu menu = null;

            for (Menu m : menuList) {
                if (m.getId()== parentId) {
                    menu = m;
                    break;
                }
            }

            if (menu == null) return;

            for (ModuleSelectedListener listener : moduleSelectedListeners) {
                listener.moduleSelected(menu.getBrowser(), menu.getMenuName(),
                        menu.getParentId(), this);
            }

            return;
        }


        if (parentId > 0) {
            Menu menu = null;

            for (Menu m : menuList) {
                if (m.getId()== parentId) {
                    menu = m;
                    break;
                }
            }

            if (menu == null) menu = new Menu();
            Menu parentMenu = menu;

            menuItemContainer.removeAll();
            Menu menuBack = new Menu(-1, 0, "Back", VaadinIcon.BACKWARDS.name(),
                    null, null);
            MenuItem menuItem = new MenuItem(menuBack);

            menuItem.addClickListener(listener -> loadMenu(parentMenu.getParentId()));
            menuItemContainer.add(menuItem);

            changeCaption(menu.getMenuName());
        } else {
            menuItemContainer.removeAll();
            changeCaption("New Tab");
        }

        for (Menu menu : menus) {
            MenuItem menuItem = new MenuItem(menu);

            menuItem.addClickListener(listener -> {

                loadMenu(menu.getId());

            });

            menuItemContainer.add(menuItem);
        }
    }

    public void addCaptionChangedListener(CaptionChangedListener listener) {

        captionChangedListeners.add(listener);

    }

    private void changeCaption(String caption) {

        for (CaptionChangedListener listener : captionChangedListeners) {
            listener.captionChanged(caption);
        }
    }

    public void addModuleSelectedListener(ModuleSelectedListener listener) {

        moduleSelectedListeners.add(listener);

    }
}
