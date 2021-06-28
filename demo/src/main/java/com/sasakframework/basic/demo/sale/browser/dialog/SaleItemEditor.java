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

package com.sasakframework.basic.demo.sale.browser.dialog;

import com.sasakframework.basic.demo.master.MasterRecipe;
import com.sasakframework.basic.demo.sale.SaleItem;
import com.sasakframework.component.basic.FullWidthSpacer;
import com.sasakframework.component.basic.IntegerField;
import com.sasakframework.component.basic.NullableTextField;
import com.sasakframework.component.basic.Window;
import com.sasakframework.utilities.CheckField;
import com.sasakframework.utilities.Dialogs;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.NumberField;

import java.util.function.Consumer;


@SuppressWarnings("Duplicates")
public class SaleItemEditor extends Window {

    private static String moduleName = "SaleItem";
    private boolean editMode;
    private long id;
    private MasterRecipe masterRecipe = new MasterRecipe();
    private SaleItem saleItem = new SaleItem();

    private Consumer<SaleItem> onUpdate;
    private Consumer<SaleItem> onDelete;

    private Button btnSelectProduct = new Button("Select Product...");
    private NullableTextField recipeName = new NullableTextField();
    private NullableTextField unit = new NullableTextField();
    private NumberField price = new NumberField();
    private IntegerField qty = new IntegerField();

    private Button btnSave = new Button("Save");
    private Button btnDelete = new Button("Delete");
    private Button btnClose = new Button("Cancel");

    private Div vlMain;
    private FormLayout formLayout = new FormLayout();

    private Div buttons;

    {
        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1));
        formLayout.getStyle().set("padding", "10px");

    }

    public SaleItemEditor(boolean editMode, SaleItem saleItem,
                          Consumer<SaleItem> onUpdate,
                          Consumer<SaleItem> onDelete) {

        this.editMode = editMode;
        this.saleItem = saleItem == null ? new SaleItem():
                saleItem;
        this.onUpdate = onUpdate;
        this.onDelete = onDelete;

    }

    public boolean start() {

        initLayout();

        if (editMode) {
            if (!loadDB()) return false;
        }

        if (!editMode) {
            super.setTitle("Add Ingredient");

            MasterRecipeSelector selector = new MasterRecipeSelector(i -> {
                if ((i == null) || (i.getId() == saleItem.getRecipeId())) return;

                masterRecipe = i;
                recipeName.setReadOnly(false);
                unit.setReadOnly(false);
                price.setReadOnly(false);

                recipeName.setValue(masterRecipe.getName());
                unit.setValue(masterRecipe.getUnit());
                price.setValue(masterRecipe.getSalePrice());

                recipeName.setReadOnly(true);
                unit.setReadOnly(true);
                price.setReadOnly(true);
                qty.focus();
            });

            if (selector.start()) add(selector);

        } else {
            super.setTitle("Product - " + saleItem.getRecipeName());
        }

        return true;
    }

    private void initLayout() {

        initFormLayout();
        initButtons();

        vlMain = getContent();
        vlMain.add(formLayout);
        vlMain.setWidth("510px");

        recipeName.focus();

    }

    private void initFormLayout() {

        btnSelectProduct.addClickListener(l -> {
           MasterRecipeSelector selector = new MasterRecipeSelector(i -> {
               if ((i == null) || (i.getId() == saleItem.getRecipeId())) return;

               masterRecipe = i;

               recipeName.setValue(masterRecipe.getName());
               unit.setValue(masterRecipe.getUnit());
               price.setValue(masterRecipe.getSalePrice());

               qty.focus();
           });
           if (selector.start()) add(selector);

        });

        recipeName.setReadOnly(true);
        unit.setReadOnly(true);
        price.setReadOnly(true);

        recipeName.setWidthFull();
        qty.addKeyPressListener(Key.ENTER, listener -> {
            btnSave.focus();
        });

        formLayout.addFormItem(btnSelectProduct, "" );
        formLayout.addFormItem(recipeName, "Product Name");
        formLayout.addFormItem(unit, "Unit");
        formLayout.addFormItem(price, "Price");
        formLayout.addFormItem(qty, "Qty");

        formLayout.setWidth("calc(100% - 20px)");

    }

    private void initButtons() {

        btnDelete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        btnDelete.addClickListener(clickEvent -> delete());
        btnDelete.setEnabled(editMode);

        btnSave.getElement().setAttribute("theme", "primary");
        btnSave.addClickListener(clickEvent -> doSave());

        btnClose.getElement().setAttribute("theme", "secondary");
        btnClose.addClickListener(clickEvent -> {

            getElement().removeFromParent();

        });

        HorizontalLayout leftButtons = new HorizontalLayout(btnDelete);
        leftButtons.setSpacing(true);
        leftButtons.setSizeUndefined();

        HorizontalLayout rightButtons = new HorizontalLayout(btnSave, btnClose);
        rightButtons.setSpacing(true);
        rightButtons.setSizeUndefined();

        buttons = getFooter();
        buttons.add(leftButtons, new FullWidthSpacer(), rightButtons);

    }

    private void delete() {

        if (id < 1) return;
        if (saleItem == null) return;

        Dialogs.ask("Ingredient", "Are you sure want to delete this item?", () -> {
            try {
                if (onDelete != null) {
                    onDelete.accept(saleItem);
                }
                getElement().removeFromParent();
                return;
            } catch (Exception ex) {
                Dialogs.notifyError(moduleName, id, "Failed to delete Ingredient: \n" + ex);
                return;
            }
        }, () -> {});

    }

    private boolean doSave() {

        if (!CheckField.checkTextField(recipeName, "Ingredient Name")) return false;
        if (!CheckField.checkTextField(unit, "Unit")) return false;
        if (!CheckField.checkNumberField(price, "Cost Per Unit")) return false;
        if (!CheckField.checkNumberField(qty, "Volume")) return false;

        saleItem.setRecipeId(masterRecipe.getId());
        saleItem.setRecipeName(recipeName.getValue());
        saleItem.setUnit(unit.getValue());
        saleItem.setPrice(price.getValue());
        saleItem.setQty(qty.getIntValue());

        if (onUpdate != null) onUpdate.accept(saleItem);
        getElement().removeFromParent();

        return true;
    }

    private boolean loadDB() {

        recipeName.setValue(saleItem.getRecipeName());
        unit.setValue(saleItem.getUnit());
        price.setValue(saleItem.getPrice());
        qty.setValue(saleItem.getQty());

        return true;

    }

}
