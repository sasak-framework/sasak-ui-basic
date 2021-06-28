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

import com.sasakframework.utilities.Utilities;
import com.vaadin.flow.component.textfield.TextFieldVariant;

public class IntegerField extends NullableTextField {

    private static final long serialVersionUID = 1L;
    Long value = 0L;
    boolean changingValue = false;

    public IntegerField() {

        init();

    }

    public IntegerField(String label) {

        setLabel(label);
        init();
    }

    private void init() {

        addThemeVariants(TextFieldVariant.LUMO_ALIGN_RIGHT);
        setPattern("([0-9]|\\.)+");
        setPreventInvalidInput(true);
        setAutofocus(true);
        setAutoselect(true);
        super.addValueChangeListener(listener -> {
            if (changingValue) return;

            try {
                value = Long.valueOf(super.getValue());
                changingValue = true;
                setValue(Utilities.formatNumber(value));
                changingValue = false;
            } catch (Exception ex) {
                setValue("0");
            }

        });
    }

    public void setValue(long value) {

        this.value = value;
        super.setValue(String.valueOf(value));

    }

    public long getLongValue() {

        return value;

    }

    public int getIntValue() {

        return value.intValue();

    }
}
