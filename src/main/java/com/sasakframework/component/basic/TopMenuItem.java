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
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;

public class TopMenuItem extends Div {

    private Icon icon;
    private Span menuTitle = new Span();
    private String title;

    public TopMenuItem(Icon icon, String title) {

        this.icon = icon;
        this.title = title;

        initLayout();

    }

    private void initLayout() {

        addClassName("top-menu-item");
        icon.addClassName("top-menu-icon");

        menuTitle.setText(this.title);
        menuTitle.addClassName("top-menu-title");

        add(icon, menuTitle);

    }

}
