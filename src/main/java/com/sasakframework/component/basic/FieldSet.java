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

import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.HtmlComponent;
import com.vaadin.flow.component.Tag;

@Tag("fieldset")
public class FieldSet extends HtmlComponent implements HasComponents {

    private Legend _legend = new Legend();
    private String legend = "";

    {
        _legend.getStyle().set("font-family", "\"Roboto Medium\", Helvetica, Arial, sans-serif");
        _legend.getStyle().set("font-size", "14px");
        _legend.getStyle().set("color", "rgba(27, 43, 65, 0.72)");
        _legend.getStyle().set("text-align", "center");
    }

    public FieldSet(){

        add(_legend);

    }

    public FieldSet(String legend) {

        this.legend = legend;
        _legend.setText(legend);
        add(_legend);

    }

    public void setLegend(String legend) {

        this.legend = legend;
        _legend.setText(legend);

    }

    public String getLegend() {

        return legend;

    }
}
