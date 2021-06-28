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

package com.sasakframework.basic.demo;

import com.sasakframework.basic.demo.dashboard.SaleDashboardBrowser;
import com.sasakframework.basic.demo.master.*;
import com.sasakframework.basic.demo.master.browser.MasterIngredientBrowser;
import com.sasakframework.basic.demo.master.browser.MasterRecipeBrowser;
import com.sasakframework.basic.demo.master.browser.MasterUtensilBrowser;
import com.sasakframework.basic.demo.sale.Sale;
import com.sasakframework.basic.demo.sale.SaleItem;
import com.sasakframework.basic.demo.sale.browser.SaleBrowser;
import com.sasakframework.basic.demo.session.Session;
import com.sasakframework.component.basic.Menu;
import com.sasakframework.utilities.Dialogs;
import com.sasakframework.utilities.Utilities;
import com.vaadin.flow.component.icon.VaadinIcon;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class CookBiz {

    private List<Menu> menuList = new ArrayList<>();
    private List<MasterIngredient> ingredientList = new ArrayList<>();
    private List<MasterUtensil> utensilList = new ArrayList<>();
    private List<MasterRecipe> recipeList = new ArrayList<>();
    private List<Sale> saleList = new ArrayList<>();
    private List<SaleItem> saleItemList = new ArrayList<>();

    private boolean initialized = false;

    public CookBiz() {

        if (initialized) return;

        initMenuList();

        initUtensilList();
        initIngredientList();
        initRecipeList();
        initSaleList();

        initialized = true;
    }

    private void initMenuList() {

        menuList.add(new Menu(1, 0, "Master", VaadinIcon.BUILDING.name(), null, null));
        menuList.add(new Menu(2, 1, "Utensil", VaadinIcon.TABLE.name(),
                MasterUtensilBrowser.class, null));
        menuList.add(new Menu(3, 1, "Ingredient", VaadinIcon.TABLE.name(),
                MasterIngredientBrowser.class, String.valueOf(ingredientList.size())));
        menuList.add(new Menu(4, 1, "Recipe", VaadinIcon.LIST.name(), MasterRecipeBrowser.class,
                null));
        menuList.add(new Menu(5, 0, "Sale", VaadinIcon.CASH.name(), SaleBrowser.class, null));
        menuList.add(new Menu(6, 0, "Dashboard", VaadinIcon.CHART.name(),
                SaleDashboardBrowser.class, null));

    }

    private void initUtensilList() {

        utensilList.clear();
        utensilList.add(new MasterUtensil(1, "Spatula"));
        utensilList.add(new MasterUtensil(2, "Spoon"));
        utensilList.add(new MasterUtensil(3, "Tea Spoon"));
        utensilList.add(new MasterUtensil(4, "Stainless Steel Bowl"));
        utensilList.add(new MasterUtensil(5, "Plastic Bowl"));
        utensilList.add(new MasterUtensil(6, "Fork"));
        utensilList.add(new MasterUtensil(7, "Frying Pan"));
        utensilList.add(new MasterUtensil(8, "Rice Cooker"));
        utensilList.add(new MasterUtensil(9, "Wok"));


    }

    private void initIngredientList() {

        ingredientList.clear();
        ingredientList.add(new MasterIngredient(1, "Water", "cc", 0D));
        ingredientList.add(new MasterIngredient(2,"Salt", "gr", 0.00716D));
        ingredientList.add(new MasterIngredient(3,"Jalapeno Pepper", "oz", 0.49D));
        ingredientList.add(new MasterIngredient(4,"Tomato", "lb", 4.78D));
        ingredientList.add(new MasterIngredient(5,"Onion", "lb", 7.48D));
        ingredientList.add(new MasterIngredient(6,"Garlic", "lb", 3.99D));
        ingredientList.add(new MasterIngredient(7,"White Pepper", "oz", 4.49D));
        ingredientList.add(new MasterIngredient(8,"Candlenut", "oz", 1.1D));
        ingredientList.add(new MasterIngredient(9,"Coriander", "oz", 1.12D));
        ingredientList.add(new MasterIngredient(10,"Rice", "oz", 0.15D));
        ingredientList.add(new MasterIngredient(11, "Cane Sugar", "oz", 0.05D));
        ingredientList.add(new MasterIngredient(12, "Leek", "pcs", 1.62D));
        ingredientList.add(new MasterIngredient(13, "Cabbage", "pcs", 1.87D));
        ingredientList.add(new MasterIngredient(14, "Ginger", "lb", 3.49D));
        ingredientList.add(new MasterIngredient(15, "Bay Leaf", "oz", 4.98D));
        ingredientList.add(new MasterIngredient(16, "Steak Beef", "oz", 1D));
        ingredientList.add(new MasterIngredient(17, "Chicken Thigh", "lb", 3.23D));
        ingredientList.add(new MasterIngredient(18, "Shrimp Paste", "oz", 1.08D));
        ingredientList.add(new MasterIngredient(19, "Broccoli", "oz", 0.19D));
        ingredientList.add(new MasterIngredient(20, "Egg", "pcs", 0.27D));
        ingredientList.add(new MasterIngredient(21, "Soy Sauce", "fl oz", 0.30D));
        ingredientList.add(new MasterIngredient(22, "White Flour", "oz", 0.04D));
        ingredientList.add(new MasterIngredient(23, "Yeast", "oz", 2.5D));
        ingredientList.add(new MasterIngredient(24, "Butter", "oz", 0.19D));
        ingredientList.add(new MasterIngredient(25, "Cocoa Powder", "oz", 0.75D));
        ingredientList.add(new MasterIngredient(26, "Tea Bag", "pcs", 0.25D));
        ingredientList.add(new MasterIngredient(27, "Cofee Ground", "oz", 0.6D));
    }

    private void initRecipeList() {

        MasterRecipe recipe = new MasterRecipe(1, "Nasi Goreng Jawa (Javanese Fried Rice)", null, "Plate", 0D, 7D, new ArrayList<>(), new ArrayList<>());
            recipe.getIngredientList().add(new MasterRecipeIngredient(1, 1, 10, "Rice", "oz", 0.15D, 0.5D));
            recipe.getIngredientList().add(new MasterRecipeIngredient(2, 1, 6, "Garlic", "lb", 0.15D, 0.001D));
            recipe.getIngredientList().add(new MasterRecipeIngredient(3, 1, 2, "Salt", "gr", 0.15D, 0.1D));
            recipe.getIngredientList().add(new MasterRecipeIngredient(4, 1, 20, "Egg", "pcs", 0.27D, 1D));
            recipe.getIngredientList().add(new MasterRecipeIngredient(5, 1, 5, "Onion", "lb", 7.48D, 0.1D));
            recipe.getIngredientList().add(new MasterRecipeIngredient(6, 1, 13, "Cabbage", "pcs", 1.87D, 0.1D));
            recipe.getIngredientList().add(new MasterRecipeIngredient(7, 1, 8, "Candlenut", "oz", 1.1D, 0.1D));
            recipe.getIngredientList().add(new MasterRecipeIngredient(8, 1, 21, "Soy Sauce", "oz", 0.30D, 0.01D));

            recipe.getUtensilList().add(new MasterRecipeUtensil(1, 1, 9, "Wok"));
            recipe.getUtensilList().add(new MasterRecipeUtensil(2, 1, 1, "Spatula"));
        recipeList.add(recipe);

        recipe = new MasterRecipe(2, "White Bread", null, "Loaf", 0D, 3D, new ArrayList<>(), new ArrayList<>());
            recipe.getIngredientList().add(new MasterRecipeIngredient(9, 2, 22, "White Flour", "oz", 0.04D, 10D));
            recipe.getIngredientList().add(new MasterRecipeIngredient(10, 2, 1, "Water", "cc", 0D, 100D));
            recipe.getIngredientList().add(new MasterRecipeIngredient(11, 2, 2, "Salt", "gr", 0.00716D, 1D));
            recipe.getIngredientList().add(new MasterRecipeIngredient(12, 2, 11, "Cane Sugar", "oz", 0.05D, 0.2D));
            recipe.getIngredientList().add(new MasterRecipeIngredient(13, 2, 23, "Yeast", "oz", 2.5D, 0.02D));
            recipe.getIngredientList().add(new MasterRecipeIngredient(14, 2, 24, "Butter", "oz", 0.19D, 0.2D));
            recipe.getIngredientList().add(new MasterRecipeIngredient(15, 2, 20, "Egg", "pcs", 0.27D, 2D));
        recipeList.add(recipe);

        recipe = new MasterRecipe(3, "Choco Bread", null, "Loaf", 0D, 3.5D, new ArrayList<>(), new ArrayList<>());
        recipe.getIngredientList().add(new MasterRecipeIngredient(16, 2, 22, "White Flour", "oz", 0.04D, 10D));
        recipe.getIngredientList().add(new MasterRecipeIngredient(17, 2, 1, "Water", "cc", 0D, 100D));
        recipe.getIngredientList().add(new MasterRecipeIngredient(18, 2, 2, "Salt", "gr", 0.00716D, 1D));
        recipe.getIngredientList().add(new MasterRecipeIngredient(19, 2, 11, "Cane Sugar", "oz", 0.05D, 0.4D));
        recipe.getIngredientList().add(new MasterRecipeIngredient(20, 2, 23, "Yeast", "oz", 2.5D, 0.02D));
        recipe.getIngredientList().add(new MasterRecipeIngredient(21, 2, 24, "Butter", "oz", 0.19D, 0.2D));
        recipe.getIngredientList().add(new MasterRecipeIngredient(22, 2, 20, "Egg", "pcs", 0.27D, 2D));
        recipe.getIngredientList().add(new MasterRecipeIngredient(23, 2, 25, "Cocoa Powder", "oz", 0.75D, 0.2D));
        recipeList.add(recipe);

        recipe = new MasterRecipe(4, "Hot Tea", null, "Cup", 0D, 2D, new ArrayList<>(), new ArrayList<>());
        recipe.getIngredientList().add(new MasterRecipeIngredient(24, 2, 1, "Water", "cc", 0D, 100D));
        recipe.getIngredientList().add(new MasterRecipeIngredient(25, 2, 26, "Tea bag", "pcs", 0.25D, 1D));
        recipe.getIngredientList().add(new MasterRecipeIngredient(26, 2, 11, "Cane Sugar", "oz", 0.05D, 0.2D));
        recipeList.add(recipe);

        recipe = new MasterRecipe(4, "Hot Coffee", null, "Cup", 0D, 2.5D, new ArrayList<>(), new ArrayList<>());
        recipe.getIngredientList().add(new MasterRecipeIngredient(24, 2, 1, "Water", "cc", 0D, 100D));
        recipe.getIngredientList().add(new MasterRecipeIngredient(25, 2, 26, "Coffee Ground", "oz", 0.6D, 0.02D));
        recipe.getIngredientList().add(new MasterRecipeIngredient(26, 2, 11, "Cane Sugar", "oz", 0.05D, 0.2D));
        recipeList.add(recipe);

    }

    private void initSaleList() {

        List<String> firstNames = Arrays.asList("Mr. Mahama", "Mrs. Dija", "Ms. Tima", "Mr. Baka", "Mr. Sama'i", "Mr. Salema",
                "Mr. Barahi", "Ms. Jabeda");

        List<String> lastNames = Arrays.asList("Abdullah", "Khoiruddin", "Najmuddin", "Abidin", "Badaruddin", "Syarifuddin");

        long id = 0;
        long itemId = 0;

        Random rndFirstName = new Random();
        Random rndLastName = new Random();
        Random rndRecipe = new Random();
        do {
                id++;
                String name = firstNames.get(rndFirstName.nextInt(firstNames.size())) + " " +
                        lastNames.get(rndLastName.nextInt(lastNames.size()));
                Date date = getRandomDate(getDateFromString("01-01-" + Utilities.getCurrentYear()),
                        new Date());

                Sale sale = new Sale(id, date, name, 0D, new ArrayList<>());

                Random rnd = new Random();
                for (int i = 1; i <= (rnd.nextInt(3) + 1); i++) {
                    itemId++;
                    Random rndQty = new Random();
                    MasterRecipe recipe = recipeList.get(rndRecipe.nextInt(recipeList.size()));
                    SaleItem item = new SaleItem(itemId, id, recipe.getId(), recipe.getName(), (rndQty.nextInt(3) + 1),
                            recipe.getUnit(), recipe.getCost(), recipe.getSalePrice());

                    if (sale.getItemList() != null) {
                        boolean itemExists = false;
                        for (SaleItem item1 : sale.getItemList()) {
                            if (item1.getRecipeId() == recipe.getId()) {
                                itemExists = true;
                                break;
                            }
                        }
                        if (!itemExists) sale.getItemList().add(item);
                    }
                }

                saleList.add(sale);
        } while (id < 200);

        for (Menu m : menuList) {
            if (m.getMenuName().equals("Sale")) {
                m.setBadge(String.valueOf(saleList.size()));
            }
        }

    }

    private Date getDateFromString(String strDate) {

        try {
            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
            return df.parse(strDate);
        } catch (Exception ex) {
            Dialogs.notifyError("Error parsing date '" + strDate + "': \n" + ex);
            return null;
        }

    }

    private  Date getRandomDate(Date startInclusive, Date endExclusive) {
        long startMillis = startInclusive.getTime();
        long endMillis = endExclusive.getTime();
        long randomMillisSinceEpoch = ThreadLocalRandom
                .current()
                .nextLong(startMillis, endMillis);

        return new Date(randomMillisSinceEpoch);
    }

    public List<Menu> getMenuList() {
        return menuList;
    }

    public void setMenuList(List<Menu> menuList) {
        this.menuList = menuList;
    }

    public List<MasterIngredient> getIngredientList() {
        return ingredientList;
    }

    public void setIngredientList(List<MasterIngredient> ingredientList) {
        this.ingredientList = ingredientList;
    }

    public List<MasterUtensil> getUtensilList() {
        return utensilList;
    }

    public void setUtensilList(List<MasterUtensil> utensilList) {
        this.utensilList = utensilList;
    }

    public List<MasterRecipe> getRecipeList() {
        return recipeList;
    }

    public void setRecipeList(List<MasterRecipe> recipeList) {
        this.recipeList = recipeList;
    }

    public List<Sale> getSaleList() {
        return saleList;
    }

    public void setSaleList(List<Sale> saleList) {
        this.saleList = saleList;
    }

    public List<SaleItem> getSaleItemList() {
        return saleItemList;
    }

    public void setSaleItemList(List<SaleItem> saleItemList) {
        this.saleItemList = saleItemList;
    }
}