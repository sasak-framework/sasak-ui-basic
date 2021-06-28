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
import com.vaadin.flow.component.orderedlayout.FlexLayout;

import java.util.Date;

/**
 * Created by husein on 4/26/17.
 */
public class DateRange extends FlexLayout {

    private LocalDatePicker date1 = new LocalDatePicker();
    private LocalDatePicker date2 = new LocalDatePicker();

    public DateRange(Date date1Value, Date date2Value, String caption) {

        init(date1Value, date2Value, "dd-MM-yyyy", caption);

    }

    public DateRange(Date date1Value, Date date2Value, String dateFormat, String caption) {

        init(date1Value, date2Value, dateFormat, caption);

    }

    private void init(Date date1Value, Date date2Value, String dateFormat, String caption) {

        date1.setDateValue(date1Value);
        date2.setDateValue(date2Value);

        Label lblDash = new Label(" - ");
        lblDash.getStyle().set("margin", "5px");
        getStyle().set("flex-wrap", "wrap");
        add(date1, lblDash, date2);
        setAlignItems(Alignment.CENTER);

    }

    public Date getDate1Value() { return date1.getDateValue(); }

    public Date getDate2Value() { return date2.getDateValue(); }

    public void setDate1Value(Date date) {date1.setDateValue(date); }

    public void setDate2Value(Date date) {date2.setDateValue(date); }
}
