/*
 * Copyright (c) 2021. Sasak UI
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
 * Code written by Husein Musawa @ 2021
 */

package com.sasakframework.basic.demo.sale;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sasakframework.basic.demo.master.MasterRecipeIngredient;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Sale {
    private long id;
    private Date date;
    private String customerName;
    @Getter(AccessLevel.NONE)
    private Double total;
    private List<SaleItem> itemList = new ArrayList<>();

    @JsonIgnore
    public String getItemListString() {
        if ((itemList == null) || (itemList.isEmpty())) return null;

        StringBuilder s = new StringBuilder();
        for (SaleItem item : itemList) {
            s = s.length() < 1 ? s.append(item.getRecipeName()) : s.append(", ").append(item.getRecipeName());
        }

        return s.toString();
    }

    @JsonIgnore
    public Double getTotal() {

        double tc = 0D;

        if ((getItemList() != null) && (!getItemList().isEmpty())) {
            for (SaleItem item : getItemList()) {
                tc = Double.sum(tc, item.getCostPerPortion() * item.getQty());
            }
        }

        return tc;

    }
}
