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

import com.vaadin.flow.component.html.Label;

public class Spacer extends Label {

    public Spacer() {

        setText(" ");
        getStyle().set("margin-left", "5px");

    }

    public Spacer(String height) {

        setText(" ");
        setWidth("5px");
        getStyle().set("margin-top", height);
    }

    public Spacer(String height, String width) {

        setText(" ");
        setWidth("5px");
        getStyle().set("margin-top", height);
        getStyle().set("margin-left", "15px");

    }
}
