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
import com.sasakframework.basic.demo.master.MasterUtensil;
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

import java.util.function.Consumer;


@SuppressWarnings("Duplicates")
public class MasterUtensilEditor extends Window {

    private static String moduleName = "MasterUtensil";
    private boolean editMode;
    private long id;
    private MasterUtensil masterUtensil = new MasterUtensil();

    private Consumer<MasterUtensil> onUpdate;
    private Consumer<MasterUtensil> onDelete;

    private NullableTextField name = new NullableTextField();

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

    public MasterUtensilEditor(boolean editMode, long id, Consumer<MasterUtensil> onUpdate,
                               Consumer<MasterUtensil> onDelete) {

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
            super.setTitle("Add Utensil");
        } else {
            super.setTitle("Utensil - " + masterUtensil.getName());
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
        name.addKeyPressListener(Key.ENTER, l -> btnSave.focus());

        formLayout.addFormItem(name, "Name");
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
        if (masterUtensil == null) return;

        Dialogs.ask("Utensil", "Are you sure want to delete this item?", () -> {
            try {
                if (onDelete != null) {
                    Session.getCookBiz().getUtensilList().removeIf(i -> i.getId() == id);
                    onDelete.accept(masterUtensil);
                    Dialogs.notifyInfo("Data deleted.");
                }
                getElement().removeFromParent();
                return;
            } catch (Exception ex) {
                Dialogs.notifyError(moduleName, id, "Failed to delete Utensil: \n" + ex);
                return;
            }
        }, () -> {});

    }

    private boolean doSave() {

        if (!CheckField.checkTextField(name, "Utensil Name")) return false;

        masterUtensil.setName(name.getValue());

        if (!editMode) {
            if (Session.getCookBiz() == null) {
                CookBiz cookBiz = new CookBiz();
                Session.setCookBiz(cookBiz);
            }

            id = 1;

            for (MasterUtensil utensil : Session.getCookBiz().getUtensilList()) {
                if (utensil.getId() > id) {
                    id++;
                }
            }

            masterUtensil.setId(id);
            Session.getCookBiz().getUtensilList().add(masterUtensil);
            if (onUpdate != null) {
                onUpdate.accept(masterUtensil);
            }
        } else {
            for (MasterUtensil utensil : Session.getCookBiz().getUtensilList()) {
                if (utensil.getId() == masterUtensil.getId()) {
                    utensil.setName(masterUtensil.getName());
                }
            }
        }

        Dialogs.notifyDataSaved();
        if (onUpdate != null) onUpdate.accept(masterUtensil);
        getElement().removeFromParent();
        return true;
    }

    private boolean loadDB() {

        for (MasterUtensil i : Session.getCookBiz().getUtensilList()) {
            if (i.getId() == id) {
                masterUtensil = i;
                break;
            }
        }

        name.setValue(masterUtensil.getName());

        return true;

    }

}
