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
import com.vaadin.flow.component.orderedlayout.FlexLayout;

import java.util.ArrayList;
import java.util.List;

@CssImport("./styles/module-styles.css")
public class ModuleBrowser extends FlexLayout {

    private List<ModuleToolbar.GoBackListener> goBackListeners = new ArrayList<>();
    private String year;

    public ModuleBrowser() {

        addClassName("module-container");

    }

    public void addGoBackListener(ModuleToolbar.GoBackListener listener) {

        goBackListeners.add(listener);

    }

    public List<ModuleToolbar.GoBackListener> getGoBackListeners() {
        return goBackListeners;
    }

    public void hideContent() {

    }

    public void refreshContent() {

    }

}
