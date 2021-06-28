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

import com.vaadin.flow.component.timepicker.TimePicker;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;

public class LocalTimePicker extends TimePicker {

    private String label = null;

    public LocalTimePicker() {

        initControls();

    }

    public LocalTimePicker(String label) {

        this.label = label;
        initControls();

    }

    public void setDateValue(Date date) {

        if (date == null) {
            setValue(null);
        } else {
            setValue(date.toInstant().atZone(ZoneId.systemDefault()).toLocalTime());
        }

    }

    public Date getDateValue() {

        if (super.getValue() == null) return null;

        Instant instant = super.getValue().atDate(LocalDate.of(1900, 1, 1)).
                atZone(ZoneId.systemDefault()).toInstant();
        Date time = Date.from(instant);

        return time;

    }

    private void initControls() {

        setLocale(new Locale("ID", "id"));
        setLabel(label);
        setPlaceholder("Select Time");
        setValue(null);

    }
}
