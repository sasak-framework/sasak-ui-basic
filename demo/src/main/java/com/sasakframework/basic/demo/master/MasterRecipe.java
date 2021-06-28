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

package com.sasakframework.basic.demo.master;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MasterRecipe {
    private long id;
    private String name;
    private String picture;
    private String unit;
    @Getter(AccessLevel.NONE)
    private Double cost;
    private Double salePrice;
    private List<MasterRecipeIngredient> ingredientList = new ArrayList<>();
    private List<MasterRecipeUtensil> utensilList = new ArrayList<>();

    @JsonIgnore
    public String getIngredientString() {

        if ((ingredientList == null) || (ingredientList.isEmpty())) return null;

        StringBuilder ingredients = new StringBuilder();
        for (MasterRecipeIngredient ingredient : ingredientList) {
            ingredients = ingredients.length() > 0 ? ingredients.append(", ").append(ingredient.getName()) :
                    ingredients.append(ingredient.getName());
        }

        return ingredients.toString();

    }

    @JsonIgnore
    public String getUtensilString() {

        if ((utensilList == null) || (utensilList.isEmpty())) return null;

        StringBuilder utensils = new StringBuilder();
        for (MasterRecipeUtensil utensil : utensilList) {
            utensils = utensils.length() > 0 ? utensils.append(", ").append(utensil.getName()) :
                    utensils.append(utensil.getName());
        }

        return utensils.toString();

    }

    public Double getCost() {

        double tc = 0D;

        if ((getIngredientList() != null) && (!getIngredientList().isEmpty())) {
            for (MasterRecipeIngredient ingredient : getIngredientList()) {
                tc = Double.sum(tc, ingredient.getCostPerUnit() * ingredient.getVolume());
            }
        }

        return tc;

    }
}
