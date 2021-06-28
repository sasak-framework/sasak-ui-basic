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

import com.sasakframework.basic.demo.master.MasterIngredient;
import com.sasakframework.basic.demo.master.MasterRecipeIngredient;
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
public class MasterRecipeIngredientEditor extends Window {

    private static String moduleName = "MasterRecipeIngredient";
    private boolean editMode;
    private long id;
    private MasterIngredient masterIngredient = new MasterIngredient();
    private MasterRecipeIngredient masterRecipeIngredient = new MasterRecipeIngredient();

    private Consumer<MasterRecipeIngredient> onUpdate;
    private Consumer<MasterRecipeIngredient> onDelete;

    private Button btnSelectIngredient = new Button("Select Ingredient...");
    private NullableTextField name = new NullableTextField();
    private NullableTextField unit = new NullableTextField();
    private NumberField costPerUnit = new NumberField();
    private NumberField volume = new NumberField();

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

    public MasterRecipeIngredientEditor(boolean editMode, MasterRecipeIngredient masterRecipeIngredient,
                                        Consumer<MasterRecipeIngredient> onUpdate,
                                        Consumer<MasterRecipeIngredient> onDelete) {

        this.editMode = editMode;
        this.masterRecipeIngredient = masterRecipeIngredient == null ? new MasterRecipeIngredient():
                masterRecipeIngredient;
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

            MasterIngredientSelector selector = new MasterIngredientSelector(i -> {
                if ((i == null) || (i.getId() == masterRecipeIngredient.getIngredientId())) return;

                masterIngredient = i;
                name.setReadOnly(false);
                unit.setReadOnly(false);
                costPerUnit.setReadOnly(false);

                name.setValue(masterIngredient.getName());
                unit.setValue(masterIngredient.getUnit());
                costPerUnit.setValue(masterIngredient.getCostPerUnit());

                name.setReadOnly(true);
                unit.setReadOnly(true);
                costPerUnit.setReadOnly(true);
                volume.focus();
            });

            if (selector.start()) add(selector);

        } else {
            super.setTitle("Ingredient - " + masterRecipeIngredient.getName());
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

        btnSelectIngredient.addClickListener(l -> {
           MasterIngredientSelector selector = new MasterIngredientSelector(i -> {
               if ((i == null) || (i.getId() == masterRecipeIngredient.getIngredientId())) return;

               masterIngredient = i;

               name.setValue(masterIngredient.getName());
               unit.setValue(masterIngredient.getUnit());
               costPerUnit.setValue(masterIngredient.getCostPerUnit());

               volume.focus();
           });
           if (selector.start()) add(selector);

        });

        name.setReadOnly(true);
        unit.setReadOnly(true);
        costPerUnit.setReadOnly(true);

        name.setWidthFull();
        volume.addKeyPressListener(Key.ENTER, listener -> {
            btnSave.focus();
        });

        formLayout.addFormItem(btnSelectIngredient, "" );
        formLayout.addFormItem(name, "Name");
        formLayout.addFormItem(unit, "Unit");
        formLayout.addFormItem(costPerUnit, "Cost Per Unit");
        formLayout.addFormItem(volume, "Volume");

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
        if (masterRecipeIngredient == null) return;

        Dialogs.ask("Ingredient", "Are you sure want to delete this item?", () -> {
            try {
                if (onDelete != null) {
                    onDelete.accept(masterRecipeIngredient);
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
        if (!CheckField.checkNumberField(volume, "Volume")) return false;

        masterRecipeIngredient.setIngredientId(masterIngredient.getId());
        masterRecipeIngredient.setName(name.getValue());
        masterRecipeIngredient.setUnit(unit.getValue());
        masterRecipeIngredient.setCostPerUnit(costPerUnit.getValue());
        masterRecipeIngredient.setVolume(volume.getValue());

        if (onUpdate != null) onUpdate.accept(masterRecipeIngredient);
        getElement().removeFromParent();

        return true;
    }

    private boolean loadDB() {

        name.setValue(masterRecipeIngredient.getName());
        unit.setValue(masterRecipeIngredient.getUnit());
        costPerUnit.setValue(masterRecipeIngredient.getCostPerUnit());
        volume.setValue(masterRecipeIngredient.getVolume());

        return true;

    }

}
