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

import com.vaadin.flow.component.HtmlComponent;
import com.vaadin.flow.component.PropertyDescriptor;
import com.vaadin.flow.component.PropertyDescriptors;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.server.AbstractStreamResource;

@Tag("iframe")
public class IFrame extends HtmlComponent {

    private static final PropertyDescriptor<String, String> srcDescriptor = PropertyDescriptors.attributeWithDefault("src", "");

    public IFrame() {

        getStyle().set("border", "1px solid lightgray");

    }

    public IFrame(String src) {

        getStyle().set("border", "1px solid lightgray");
        this.set(srcDescriptor, src);
        
    }

    public String getSrc() {
        return (String)this.get(srcDescriptor);
    }

    public void setSrc(String src) {
        this.set(srcDescriptor, src);
    }

    public void setSrc(AbstractStreamResource src) {
        this.getElement().setAttribute("src", src);
    }
}
