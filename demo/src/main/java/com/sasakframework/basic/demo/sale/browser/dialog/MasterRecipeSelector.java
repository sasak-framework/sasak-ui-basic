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
import com.sasakframework.basic.demo.session.Session;
import com.sasakframework.component.basic.BoldLabel;
import com.sasakframework.component.basic.FullWidthSpacer;
import com.sasakframework.component.basic.Window;
import com.sasakframework.utilities.Utilities;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.data.renderer.TemplateRenderer;

import java.util.List;
import java.util.function.Consumer;

public class MasterRecipeSelector extends Window {

    private Consumer<MasterRecipe> onSelect;
    private List<MasterRecipe> recipeList = Session.getCookBiz().getRecipeList();
    private Grid<MasterRecipe> grid = new Grid<>();
    private Label lblStatus = new Label("0 item.");

    private Button btnClose = new Button("Close");

    private Div vlMain = getContent();

    public MasterRecipeSelector(Consumer<MasterRecipe> onSelect) {
        this.onSelect = onSelect;
    }

    public boolean start() {

        initLayout();
        grid.setItems(Session.getCookBiz().getRecipeList());

        return true;

    }

    private void initLayout() {

        vlMain.setWidth("810px");
        initGridLayout();
        initButtons();

        setTitle("Select Product");

    }

    private void initGridLayout() {

        grid.setWidth("100%");
        grid.getStyle().set("min-height", "400px");
        grid.getStyle().set("flex-grow", "1");
        grid.setHeight("100%");
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        grid.addColumn(TemplateRenderer.<MasterRecipe>of(
                "<Label style=\"padding-left: 5px;\"><small>[[item.name]]</small></Label>")
                .withProperty("name", MasterRecipe::getName))
                .setHeader(new BoldLabel("Name", "5px"))
                .setResizable(true)
                .setWidth("60%");
        grid.addColumn(TemplateRenderer.<MasterRecipe>of(
                "<Label><small>[[item.unit]]</small></Label>")
                .withProperty("unit", MasterRecipe::getUnit))
                .setHeader(new BoldLabel("Unit", "5px"))
                .setResizable(true)
                .setWidth("150px");
        grid.addColumn(TemplateRenderer.<MasterRecipe>of(
                "<Label><small>[[item.pricePerUnit]]</small></Label>")
                .withProperty("pricePerUnit", i -> Utilities.formatNumber(i.getSalePrice())))
                .setHeader(new BoldLabel("Price", "5px"))
                .setResizable(true)
                .setWidth("150px");

        grid.addItemClickListener(l -> {
            if (l.getItem() == null)  return;

            if (onSelect != null) onSelect.accept(l.getItem());
            super.close();

        });

        vlMain.add(grid);

        lblStatus.setWidthFull();
        lblStatus.getStyle().set("font-size", "95%");
        lblStatus.getStyle().set("margin-top", " 5px");
        vlMain.add(lblStatus);
    }

    private void initButtons() {

        btnClose.addClickListener(l -> super.close());

        FlexLayout buttons = new FlexLayout();
        buttons.setWidthFull();
        buttons.add(new FullWidthSpacer(), btnClose);

        getFooter().add(buttons);
    }

}

