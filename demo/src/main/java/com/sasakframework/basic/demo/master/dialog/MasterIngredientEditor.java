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

package com.sasakframework.basic.demo.master.dialog;

import com.sasakframework.basic.demo.CookBiz;
import com.sasakframework.basic.demo.master.MasterIngredient;
import com.sasakframework.basic.demo.session.Session;
import com.sasakframework.component.basic.FullWidthSpacer;
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
public class MasterIngredientEditor extends Window {

    private static String moduleName = "MasterIngredient";
    private boolean editMode;
    private long id;
    private MasterIngredient masterIngredient = new MasterIngredient();

    private Consumer<MasterIngredient> onUpdate;
    private Consumer<MasterIngredient> onDelete;

    private NullableTextField name = new NullableTextField();
    private NullableTextField unit = new NullableTextField();
    private NumberField costPerUnit = new NumberField();

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

    public MasterIngredientEditor(boolean editMode, long id, Consumer<MasterIngredient> onUpdate,
                                  Consumer<MasterIngredient> onDelete) {

        this.editMode = editMode;
        this.id = id;
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
        } else {
            super.setTitle("Ingredient - " + masterIngredient.getName());
        }

        return true;
    }

    private void initLayout() {

        initFormLayout();
        initButtons();

        vlMain = getContent();
        vlMain.add(formLayout);
        vlMain.setWidth("510px");

        name.focus();

    }

    private void initFormLayout() {

        name.setWidthFull();
        costPerUnit.addKeyPressListener(Key.ENTER, listener -> {
                btnSave.focus();
        });

        formLayout.addFormItem(name, "Name");
        formLayout.addFormItem(unit, "Unit");
        formLayout.addFormItem(costPerUnit, "Cost Per Unit");

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
        if (masterIngredient == null) return;

        Dialogs.ask("Ingredient", "Are you sure want to delete this item?", () -> {
            try {
                if (onDelete != null) {
                    Session.getCookBiz().getIngredientList().removeIf(i -> i.getId() == id);
                    onDelete.accept(masterIngredient);
                    Dialogs.notifyInfo("Data deleted.");
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

        if (!CheckField.checkTextField(name, "Ingredient Name")) return false;
        if (!CheckField.checkTextField(unit, "Unit")) return false;
        if (!CheckField.checkNumberField(costPerUnit, "Cost Per Unit")) return false;

        masterIngredient.setName(name.getValue());
        masterIngredient.setUnit(unit.getValue());
        masterIngredient.setCostPerUnit(costPerUnit.getValue());

        if (!editMode) {
            if (Session.getCookBiz() == null) {
                CookBiz cookBiz = new CookBiz();
                Session.setCookBiz(cookBiz);
            }

            id = 1;

            for (MasterIngredient ingredient : Session.getCookBiz().getIngredientList()) {
                if (ingredient.getId() > id) {
                    id++;
                }
            }

            masterIngredient.setId(id);
            Session.getCookBiz().getIngredientList().add(masterIngredient);
            if (onUpdate != null) {
                onUpdate.accept(masterIngredient);
            }
        } else {
            for (MasterIngredient ingredient : Session.getCookBiz().getIngredientList()) {
                if (ingredient.getId() == masterIngredient.getId()) {
                    ingredient.setName(masterIngredient.getName());
                    ingredient.setUnit(masterIngredient.getUnit());
                    ingredient.setCostPerUnit(masterIngredient.getCostPerUnit());
                }
            }
        }

        Dialogs.notifyDataSaved();
        if (onUpdate != null) onUpdate.accept(masterIngredient);
        getElement().removeFromParent();
        return true;
    }

    private boolean loadDB() {

        for (MasterIngredient i : Session.getCookBiz().getIngredientList()) {
            if (i.getId() == id) {
                masterIngredient = i;
                break;
            }
        }

        name.setValue(masterIngredient.getName());
        unit.setValue(masterIngredient.getUnit());
        costPerUnit.setValue(masterIngredient.getCostPerUnit());

        return true;

    }

}
