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
import com.sasakframework.basic.demo.master.MasterRecipe;
import com.sasakframework.basic.demo.master.MasterRecipeIngredient;
import com.sasakframework.basic.demo.master.MasterRecipeUtensil;
import com.sasakframework.basic.demo.session.Session;
import com.sasakframework.component.basic.BoldLabel;
import com.sasakframework.component.basic.FullWidthSpacer;
import com.sasakframework.component.basic.NullableTextField;
import com.sasakframework.component.basic.Window;
import com.sasakframework.utilities.CheckField;
import com.sasakframework.utilities.Dialogs;
import com.sasakframework.utilities.Utilities;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.data.renderer.TemplateRenderer;
import com.vaadin.flow.function.ValueProvider;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.function.Consumer;


@SuppressWarnings("Duplicates")
public class MasterRecipeEditor extends Window {

    private static String moduleName = "MasterRecipe";
    private boolean editMode;
    private long id;
    private MasterRecipe masterRecipe = new MasterRecipe();

    private Consumer<MasterRecipe> onUpdate;
    private Consumer<MasterRecipe> onDelete;

    private Tabs tabs = new Tabs();
    private Div contentContainer = new Div();
    private Div ingredientLayout = new Div();
    private Div utensilLayout = new Div();
    private Button btnAddIngredient = new Button("Add");
    private Button btnAddUtensil = new Button("Add");
    private Grid<MasterRecipeIngredient> grdIngredients = new Grid<>();
    private Grid<MasterRecipeUtensil> grdUtensils = new Grid<>();
    private Label lblStatusIngredients = new Label("0 item.");
    private Label lblStatusUtensils = new Label("0 item.");

    private Button btnSave = new Button("Save");
    private Button btnDelete = new Button("Delete");
    private Button btnClose = new Button("Cancel");

    private Div vlMain;
    private FormLayout formLayout = new FormLayout();
    private NullableTextField name = new NullableTextField();
    private NumberField totalCost = new NumberField();

    private Div buttons;

    {
        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1));
        formLayout.getStyle().set("padding", "10px");

    }

    public MasterRecipeEditor(boolean editMode, long id, Consumer<MasterRecipe> onUpdate,
                              Consumer<MasterRecipe> onDelete) {

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
            super.setTitle("Add Recipe");
        } else {
            super.setTitle("Recipe - " + masterRecipe.getName());
        }

        return true;
    }

    private void initLayout() {

        vlMain = getContent();

        initFormLayout();
        initIngredientLayout();
        initUtensilLayout();
        initTabs();
        initButtons();

        vlMain.setWidth("810px");

        name.focus();

    }

    private void initFormLayout() {

        name.setWidthFull();
        totalCost.setWidthFull();
        totalCost.setReadOnly(true);

        formLayout.addFormItem(name, "Name");
        formLayout.addFormItem(totalCost, "Total Cost");
        formLayout.setWidth("calc(100% - 20px)");

        vlMain.add(formLayout);
    }

    private void initTabs() {

        tabs.setWidthFull();
        vlMain.add(tabs);

        contentContainer.setWidth("calc(100% - 10px)");
        contentContainer.setMinHeight("500px");
        contentContainer.getStyle().set("overflow", "auto");
        contentContainer.add(ingredientLayout);
        vlMain.add(contentContainer);

        tabs.addSelectedChangeListener(l -> {
            switch (tabs.getSelectedIndex()) {
                case 0:
                    contentContainer.removeAll();
                    contentContainer.add(ingredientLayout);
                    break;
                case 1:
                    contentContainer.removeAll();
                    contentContainer.add(utensilLayout);
                    break;
            }
        });

    }

    private void initIngredientLayout() {

        ingredientLayout.setSizeFull();

        btnAddIngredient.setIcon(VaadinIcon.PLUS.create());
        btnAddIngredient.addClickListener(l -> {
            MasterRecipeIngredientEditor editor = new MasterRecipeIngredientEditor(false, null,
                    newIngredient -> {
                        long newId = 1;
                        if (masterRecipe.getIngredientList() != null) {
                            for (MasterRecipeIngredient ingredient : masterRecipe.getIngredientList()) {
                                if (newId <= ingredient.getId()) newId++;
                                if (ingredient.getIngredientId() == newIngredient.getIngredientId()) return;
                            }
                        }
                        newIngredient.setId(newId);
                        masterRecipe.getIngredientList().add(newIngredient);
                        grdIngredients.setItems(masterRecipe.getIngredientList());

                        totalCost.setValue(masterRecipe.getCost());

                        btnAddIngredient.focus();
                    }, null);

            if (editor.start()) add(editor);

        });

        FlexLayout toolbar = new FlexLayout();
        toolbar.getStyle().set("padding", "5px");
        toolbar.setWidth("calc(100%- 10px)");
        toolbar.add(btnAddIngredient);

        ingredientLayout.add(toolbar);

        grdIngredients.setWidth("100%");
        grdIngredients.getStyle().set("min-height", "400px");
        grdIngredients.getStyle().set("flex-grow", "1");
        grdIngredients.setHeight("100%");
        grdIngredients.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);

        grdIngredients.addColumn(TemplateRenderer.<MasterRecipeIngredient>of(
                "<Label style=\"padding-left: 5px;\"><small>[[item.name]]</small></Label>")
                .withProperty("name", MasterRecipeIngredient::getName))
                .setHeader(new BoldLabel("Role", "5px"))
                .setResizable(true)
                .setWidth("250px");
        grdIngredients.addColumn(TemplateRenderer.<MasterRecipeIngredient>of(
                "<Label><small>[[item.unit]]</small></Label>")
                .withProperty("unit", MasterRecipeIngredient::getUnit))
                .setHeader(new BoldLabel("Unit", "5px"))
                .setResizable(true)
                .setWidth("100px");
        grdIngredients.addColumn(TemplateRenderer.<MasterRecipeIngredient>of(
                "<Label><small>[[item.qty]]</small></Label>")
                .withProperty("qty", MasterRecipeIngredient::getVolume))
                .setHeader(new BoldLabel("Qty", "5px"))
                .setResizable(true)
                .setWidth("120px");
        grdIngredients.addColumn(TemplateRenderer.<MasterRecipeIngredient>of(
                "<Label><small>[[item.costPerUnit]]</small></Label>")
                .withProperty("costPerUnit", i -> "$" + i.getCostPerUnit()))
                .setHeader(new BoldLabel("Cost Per Unit", "5px"))
                .setResizable(true)
                .setWidth("120px");
        grdIngredients.addColumn(TemplateRenderer.<MasterRecipeIngredient>of(
                "<Label><small>[[item.totalCost]]</small></Label>")
                .withProperty("totalCost", i -> "$" + Utilities.formatNumber(i.getCostPerUnit() * i.getVolume())))
                .setHeader(new BoldLabel("Total", "5px"))
                .setResizable(true)
                .setWidth("120px");
        grdIngredients.addComponentColumn((ValueProvider<MasterRecipeIngredient, Component>) recipeIngredient -> {

            Button btnDelete = new Button();
            btnDelete.setIcon(VaadinIcon.TRASH.create());
            btnDelete.addThemeVariants(ButtonVariant.LUMO_ICON);
            btnDelete.addThemeVariants(ButtonVariant.LUMO_SMALL);
            btnDelete.addClickListener(l -> {
                Dialogs.ask("Ingredient", "Are you sure want to delete this item?",
                        () -> {
                            Iterator<MasterRecipeIngredient> itr = masterRecipe.getIngredientList().iterator();
                            while (itr.hasNext()) {
                                if (itr.next().getId() == recipeIngredient.getId()) {
                                    itr.remove();
                                    grdIngredients.setItems(masterRecipe.getIngredientList());
                                    totalCost.setValue(masterRecipe.getCost());
                                    Dialogs.notifyInfo("Data deleted.");
                                    return;
                                }
                            }
                        });

            });

            return btnDelete;
        });
        grdIngredients.addItemClickListener(i -> {
            if (i.getItem() == null) return;

            MasterRecipeIngredientEditor editor = new MasterRecipeIngredientEditor(true, i.getItem(),
                    newIngredient -> {
                        long newId = 1;
                        if (masterRecipe.getIngredientList() != null) {
                            for (MasterRecipeIngredient ingredient : masterRecipe.getIngredientList()) {
                                if (newId <= ingredient.getId()) newId++;
                                if (ingredient.getIngredientId() == newIngredient.getIngredientId()) return;
                            }
                        }
                        newIngredient.setId(newId);
                        masterRecipe.getIngredientList().add(newIngredient);
                        totalCost.setValue(masterRecipe.getCost());
                    }, null);

            if (editor.start()) add(editor);

        });

        ingredientLayout.add(grdIngredients);

        lblStatusIngredients.setWidthFull();
        lblStatusIngredients.getStyle().set("font-size", "95%");
        lblStatusIngredients.getStyle().set("margin-top", " 5px");
        ingredientLayout.add(lblStatusIngredients);

        Tab tab = new Tab(ingredientLayout);
        tab.setLabel("Ingredient");
        tabs.add(tab);
    }

    private void initUtensilLayout() {

        utensilLayout.setSizeFull();

        btnAddUtensil.setIcon(VaadinIcon.PLUS.create());
        btnAddUtensil.addClickListener(l -> {
            MasterUtensilSelector selector = new MasterUtensilSelector(u -> {
                if (masterRecipe.getUtensilList() == null) {
                    masterRecipe.setUtensilList(new ArrayList<>());
                    MasterRecipeUtensil newUtensil = new MasterRecipeUtensil(1, masterRecipe.getId(), u.getId(),
                            u.getName());
                    masterRecipe.getUtensilList().add(newUtensil);
                } else if (masterRecipe.getUtensilList().isEmpty()) {
                    MasterRecipeUtensil newUtensil = new MasterRecipeUtensil(1, masterRecipe.getId(), u.getId(),
                            u.getName());
                    masterRecipe.getUtensilList().add(newUtensil);
                } else {
                    long newId = 1;

                    for (MasterRecipeUtensil recipeUtensil : masterRecipe.getUtensilList()) {
                        if (recipeUtensil.getUtensilId() == u.getId()) return;
                        if (recipeUtensil.getId() >= newId) newId = recipeUtensil.getId() + 1;
                    }

                    MasterRecipeUtensil newUtensil = new MasterRecipeUtensil(newId, masterRecipe.getId(), u.getId(),
                            u.getName());
                    masterRecipe.getUtensilList().add(newUtensil);
                }

                grdUtensils.setItems(masterRecipe.getUtensilList());
            });

            if (selector.start()) add(selector);

        });
        
        FlexLayout toolbar = new FlexLayout();
        toolbar.getStyle().set("padding", "5px");
        toolbar.setWidth("calc(100%- 10px)");
        toolbar.add(btnAddUtensil);

        utensilLayout.add(toolbar);

        grdUtensils.setWidth("100%");
        grdUtensils.getStyle().set("min-height", "400px");
        grdUtensils.getStyle().set("flex-grow", "1");
        grdUtensils.setHeight("100%");
        grdUtensils.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);

        grdUtensils.addColumn(TemplateRenderer.<MasterRecipeUtensil>of(
                "<Label style=\"padding-left: 5px;\"><small>[[item.name]]</small></Label>")
                .withProperty("name", MasterRecipeUtensil::getName))
                .setHeader(new BoldLabel("Role", "5px"))
                .setResizable(true)
                .setWidth("60%");
        
        grdUtensils.addComponentColumn((ValueProvider<MasterRecipeUtensil, Component>) recipeUtensil -> {

            Button btnDelete = new Button();
            btnDelete.setIcon(VaadinIcon.TRASH.create());
            btnDelete.addThemeVariants(ButtonVariant.LUMO_ICON);
            btnDelete.addThemeVariants(ButtonVariant.LUMO_SMALL);
            btnDelete.addClickListener(l -> {
                Dialogs.ask("Ingredient", "Are you sure want to delete this item?",
                        () -> {
                            Iterator<MasterRecipeUtensil> itr = masterRecipe.getUtensilList().iterator();
                            while (itr.hasNext()) {
                                if (itr.next().getId() == recipeUtensil.getId()) {
                                    itr.remove();
                                    grdUtensils.setItems(masterRecipe.getUtensilList());
                                    Dialogs.notifyInfo("Data deleted.");
                                    return;
                                }
                            }
                        });

            });

            return btnDelete;

        });

        utensilLayout.add(grdUtensils);

        lblStatusUtensils.setWidthFull();
        lblStatusUtensils.getStyle().set("font-size", "95%");
        lblStatusUtensils.getStyle().set("margin-top", " 5px");
        utensilLayout.add(lblStatusUtensils);

        Tab tab = new Tab(utensilLayout);
        tab.setLabel("Utensil");
        tabs.add(tab);

    }

    private void initButtons() {

        btnDelete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        btnDelete.addClickListener(clickEvent -> delete());
        btnDelete.setEnabled(editMode);

        btnSave.getElement().setAttribute("theme", "primary");
        btnSave.addClickListener(clickEvent -> doSave());

        btnClose.getElement().setAttribute("theme", "secondary");
        btnClose.addClickListener(clickEvent -> {

            super.close();

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
        if (masterRecipe == null) return;

        Dialogs.ask("Recipe", "Are you sure want to delete this item?", () -> {
            try {
                if (onDelete != null) {
                    Session.getCookBiz().getRecipeList().removeIf(i -> i.getId() == id);
                    onDelete.accept(masterRecipe);
                    Dialogs.notifyInfo("Data deleted.");
                }
                super.close();
            } catch (Exception ex) {
                Dialogs.notifyError(moduleName, id, "Failed to delete Recipe: \n" + ex);
            }
        }, () -> {});

    }

    private boolean doSave() {

        if (!CheckField.checkTextField(name, "Recipe Name")) return false;

        masterRecipe.setName(name.getValue());

        if (!editMode) {
            if (Session.getCookBiz() == null) {
                CookBiz cookBiz = new CookBiz();
                Session.setCookBiz(cookBiz);
            }

            id = 1;

            for (MasterRecipe Recipe : Session.getCookBiz().getRecipeList()) {
                if (Recipe.getId() > id) {
                    id++;
                }
            }

            masterRecipe.setId(id);
            Session.getCookBiz().getRecipeList().add(masterRecipe);
            if (onUpdate != null) {
                onUpdate.accept(masterRecipe);
            }
        } else {
            for (MasterRecipe Recipe : Session.getCookBiz().getRecipeList()) {
                if (Recipe.getId() == masterRecipe.getId()) {
                    Recipe.setName(masterRecipe.getName());
                }
            }
        }

        Dialogs.notifyDataSaved();
        if (onUpdate != null) onUpdate.accept(masterRecipe);
        super.close();
        return true;
    }

    private boolean loadDB() {

        for (MasterRecipe i : Session.getCookBiz().getRecipeList()) {
            if (i.getId() == id) {
                masterRecipe = i;
                break;
            }
        }

        name.setValue(masterRecipe.getName());
        totalCost.setValue(masterRecipe.getCost());
        grdIngredients.setItems(masterRecipe.getIngredientList());
        grdUtensils.setItems(masterRecipe.getUtensilList());

        return true;

    }


}
