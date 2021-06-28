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

import com.vaadin.flow.component.datepicker.DatePicker;

import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;

public class LocalDatePicker extends DatePicker {

    private String label = null;

    public LocalDatePicker() {

        initControl();

    }
    public LocalDatePicker(String label) {

        this.label = label;
        initControl();

    }

    public void setDateValue(Date date) {

        if (date == null) {
            setValue(null);
        } else {
            setValue(date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        }
    }

    private void initControl() {

        super.setLocale(Locale.getDefault());
        setLocale(super.getLocale());
        setLabel(label);
        setPlaceholder("Select Date");
        setValue(null);

    }

    public Date getDateValue() {

        if (super.getValue() == null) return null;
        return Date.from(super.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());

    }
}
