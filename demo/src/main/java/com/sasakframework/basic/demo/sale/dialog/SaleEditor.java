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

package com.sasakframework.basic.demo.sale.dialog;

import com.sasakframework.basic.demo.CookBiz;
import com.sasakframework.basic.demo.sale.Sale;
import com.sasakframework.basic.demo.sale.SaleItem;
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
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.data.renderer.TemplateRenderer;
import com.vaadin.flow.function.ValueProvider;

import java.util.Date;
import java.util.Iterator;
import java.util.function.Consumer;


@SuppressWarnings("Duplicates")
public class SaleEditor extends Window {

    private static String moduleName = "Sale";
    private boolean editMode;
    private long id;
    private Sale sale = new Sale();

    private Consumer<Sale> onUpdate;
    private Consumer<Sale> onDelete;

    private Div itemLayout = new Div();
    private Button btnAddItem = new Button("Add");
    private Grid<SaleItem> grdItems = new Grid<>();
    private Label lblStatusItems = new Label("0 item.");
    
    private Button btnSave = new Button("Save");
    private Button btnDelete = new Button("Delete");
    private Button btnClose = new Button("Cancel");

    private Div vlMain;
    private FormLayout formLayout = new FormLayout();
    private NullableTextField customerName = new NullableTextField();
    private NumberField totalPrice = new NumberField();

    private Div buttons;

    {
        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1));
        formLayout.getStyle().set("padding", "10px");

    }

    public SaleEditor(boolean editMode, long id, Consumer<Sale> onUpdate,
                      Consumer<Sale> onDelete) {

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
            super.setTitle("New Sale");
        } else {
            super.setTitle("Sale - " + sale.getCustomerName());
        }

        return true;
    }

    private void initLayout() {

        vlMain = getContent();

        initFormLayout();
        initItemLayout();
        initButtons();

        vlMain.setWidth("810px");

        customerName.focus();

    }

    private void initFormLayout() {

        customerName.setWidthFull();
        totalPrice.setWidthFull();
        totalPrice.setReadOnly(true);

        formLayout.addFormItem(customerName, "Name");
        formLayout.addFormItem(totalPrice, "Total Cost");
        formLayout.setWidth("calc(100% - 20px)");

        vlMain.add(formLayout);
    }

    private void initItemLayout() {

        itemLayout.setWidthFull();

        btnAddItem.setIcon(VaadinIcon.PLUS.create());
        btnAddItem.addClickListener(l -> {
            SaleItemEditor editor = new SaleItemEditor(false, null,
                    newItem -> {
                        long newId = 1;
                        if (sale.getItemList() != null) {
                            for (SaleItem item : sale.getItemList()) {
                                if (newId <= item.getId()) newId++;
                                //if (item.getRecipeId() == newItem.getRecipeId()) return;
                            }
                        }
                        newItem.setId(newId);
                        sale.getItemList().add(newItem);
                        grdItems.setItems(sale.getItemList());

                        totalPrice.setValue(sale.getTotal());

                        btnAddItem.focus();
                    }, null);

            if (editor.start()) add(editor);

        });

        FlexLayout toolbar = new FlexLayout();
        toolbar.getStyle().set("padding", "5px");
        toolbar.setWidth("calc(100%- 10px)");
        toolbar.add(btnAddItem);

        itemLayout.add(toolbar);

        grdItems.setWidth("100%");
        grdItems.getStyle().set("min-height", "400px");
        grdItems.getStyle().set("flex-grow", "1");
        grdItems.setHeight("100%");
        grdItems.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);

        grdItems.addColumn(TemplateRenderer.<SaleItem>of(
                "<Label style=\"padding-left: 5px;\"><small>[[item.name]]</small></Label>")
                .withProperty("name", SaleItem::getRecipeName))
                .setHeader(new BoldLabel("Item", "5px"))
                .setResizable(true)
                .setWidth("250px");
        grdItems.addColumn(TemplateRenderer.<SaleItem>of(
                "<Label><small>[[item.qty]]</small></Label>")
                .withProperty("qty", SaleItem::getQty))
                .setHeader(new BoldLabel("Qty", "5px"))
                .setResizable(true)
                .setWidth("100px");
        grdItems.addColumn(TemplateRenderer.<SaleItem>of(
                "<Label><small>[[item.qty]]</small></Label>")
                .withProperty("unit", SaleItem::getUnit))
                .setHeader(new BoldLabel("Unit", "5px"))
                .setResizable(true)
                .setWidth("100px");
        grdItems.addColumn(TemplateRenderer.<SaleItem>of(
                "<Label><small>[[item.price]]</small></Label>")
                .withProperty("price", i -> "$" + i.getPrice()))
                .setHeader(new BoldLabel("Price", "5px"))
                .setResizable(true)
                .setWidth("120px");
        grdItems.addColumn(TemplateRenderer.<SaleItem>of(
                "<Label><small>[[item.total]]</small></Label>")
                .withProperty("total", i -> "$" + Utilities.formatNumber(i.getPrice() * i.getQty())))
                .setHeader(new BoldLabel("Total", "5px"))
                .setResizable(true)
                .setWidth("120px");
        grdItems.addComponentColumn((ValueProvider<SaleItem, Component>) saleItem -> {

            Button btnDelete = new Button();
            btnDelete.setIcon(VaadinIcon.TRASH.create());
            btnDelete.addThemeVariants(ButtonVariant.LUMO_ICON);
            btnDelete.addThemeVariants(ButtonVariant.LUMO_SMALL);
            btnDelete.addClickListener(l -> Dialogs.ask("Item", "Are you sure want to delete this item?",
                    () -> {
                        Iterator<SaleItem> itr = sale.getItemList().iterator();
                        while (itr.hasNext()) {
                            if (itr.next().getId() == saleItem.getId()) {
                                itr.remove();
                                grdItems.setItems(sale.getItemList());
                                totalPrice.setValue(sale.getTotal());
                                Dialogs.notifyInfo("Data deleted.");
                                return;
                            }
                        }
                    }));

            return btnDelete;
        });
        grdItems.addItemClickListener(i -> {
            if (i.getItem() == null) return;

            SaleItemEditor editor = new SaleItemEditor(true, i.getItem(),
                    newItem -> {
                        long newId = 1;
                        if (sale.getItemList() != null) {
                            for (SaleItem item : sale.getItemList()) {
                                if (newId <= item.getId()) newId++;
                                //if (item.getRecipeId() == newItem.getRecipeId()) return;
                            }
                        }
                        newItem.setId(newId);
                        sale.getItemList().add(newItem);
                        totalPrice.setValue(sale.getTotal());
                    }, null);

            if (editor.start()) add(editor);

        });

        itemLayout.add(grdItems);

        lblStatusItems.setWidthFull();
        lblStatusItems.getStyle().set("font-size", "95%");
        lblStatusItems.getStyle().set("margin-top", " 5px");
        itemLayout.add(lblStatusItems);

        vlMain.add(itemLayout);
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
        if (sale == null) return;

        Dialogs.ask("Sale", "Are you sure want to delete this item?", () -> {
            try {
                if (onDelete != null) {
                    Session.getCookBiz().getSaleList().removeIf(i -> i.getId() == id);
                    onDelete.accept(sale);
                    Dialogs.notifyInfo("Data deleted.");
                }
                super.close();
            } catch (Exception ex) {
                Dialogs.notifyError(moduleName, id, "Failed to delete Sale: \n" + ex);
            }
        }, () -> {});

    }

    private boolean doSave() {

        if (!CheckField.checkTextField(customerName, "Sale Name")) return false;

        sale.setCustomerName(customerName.getValue());
        sale.setTotal(totalPrice.getValue());

        if (!editMode) {
            if (Session.getCookBiz() == null) {
                CookBiz cookBiz = new CookBiz();
                Session.setCookBiz(cookBiz);
            }

            id = 1;

            for (Sale Sale : Session.getCookBiz().getSaleList()) {
                if (Sale.getId() > id) {
                    id++;
                }
            }

            sale.setId(id);
            sale.setDate(new Date());

            Session.getCookBiz().getSaleList().add(sale);
            if (onUpdate != null) {
                onUpdate.accept(sale);
            }
        } else {
            for (Sale sale : Session.getCookBiz().getSaleList()) {
                if (sale.getId() == this.sale.getId()) {
                    sale.setCustomerName(this.sale.getCustomerName());
                    sale.setDate(this.sale.getDate());
                    sale.setItemList(this.sale.getItemList());
                    sale.setTotal(this.sale.getTotal());
                }
            }
        }

        Dialogs.notifyDataSaved();
        if (onUpdate != null) onUpdate.accept(sale);
        super.close();
        return true;
    }

    private boolean loadDB() {

        for (Sale i : Session.getCookBiz().getSaleList()) {
            if (i.getId() == id) {
                sale = i;
                break;
            }
        }

        customerName.setValue(sale.getCustomerName());
        totalPrice.setValue(sale.getTotal());
        grdItems.setItems(sale.getItemList());

        return true;

    }


}
