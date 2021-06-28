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
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;

public class MenuItem extends Div {

    private Menu menu;
    public MenuItem(Menu menu) {

        this.menu = menu;
        initLayout();

    }

    private void initLayout() {


        Div iconContainer = new Div();
        iconContainer.addClassName("menu-item-icon-container");

        if (menu.getIconName() != null){
            Icon icon = null;
            try {
                icon = VaadinIcon.valueOf(menu.getIconName()).create();
            } catch (Exception ex) {
                Dialogs.notifyError(this.getClassName(), menu.getId(), "Failed to create icon: \n" + ex);
            }

            if (icon != null) {
                icon.addClassName("menu-item-icon");
                iconContainer.add(icon);
            }
        }

        Span label = new Span(menu.getMenuName());
        label.addClassName("menu-item-title");
        addClassName("menu-item");
        add(iconContainer, label);

        if (menu.getBadge() != null) {
            Span badgeLabel = new Span(menu.getBadge());
            badgeLabel.addClassName("menu-item-badge");
            add(badgeLabel);
        }
    }

    public Class<ModuleBrowser> getBrowser() {
        return menu.getBrowser();
    }

}
