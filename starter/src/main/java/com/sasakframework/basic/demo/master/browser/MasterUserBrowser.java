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

package com.sasakframework.basic.demo.master.browser;

import com.sasakframework.basic.demo.master.MasterUser;
import com.sasakframework.basic.demo.session.Session;
import com.sasakframework.component.basic.*;
import com.sasakframework.utilities.Dialogs;
import com.sasakframework.utilities.Utilities;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.data.renderer.TemplateRenderer;

import java.util.*;

import static java.util.stream.Collectors.toMap;

@SuppressWarnings("Duplicates")
public class MasterUserBrowser extends ModuleBrowser {

    private ModuleToolbar moduleToolbar;
    private Grid<MasterUser> grid = new Grid<>();
    private ModuleBottomBar moduleBottomBar = new ModuleBottomBar(true,
            true);

    private ModuleGridLayout gridLayout = new ModuleGridLayout();
    private Div chartLayout = new Div();

    private String moduleName = "MasterUser";

    private List<MasterUser> userList = new ArrayList<>();

    public MasterUserBrowser() {

        start();

    }

    public void start() {

        initLayout();
        loadDB();

    }

    private void initLayout() {

        initGridLayout();
        initChartLayout();

        moduleToolbar = new ModuleToolbar(moduleName, this);
        moduleToolbar.addGoBackListener(() -> {

            for (ModuleToolbar.GoBackListener listener : super.getGoBackListeners()) {
                listener.goBack();
            }

        });

        moduleToolbar.addRefreshListener(this::loadDB);
        moduleToolbar.addSearchTextChangedListener(s -> loadDB());
        moduleToolbar.addFilterClickedListener(() -> Dialogs.notifyWarning("Filter implementation goes here!"));
        moduleBottomBar.addAddButtonClickListener(this::addNew);
        moduleBottomBar.addReportButtonClickListener(() -> Dialogs.notifyWarning("Report implementation goes here!"));

        Div innerContainer = new Div(gridLayout, chartLayout);
        innerContainer.addClassName("module-inner-container");

        add(moduleToolbar, innerContainer);

    }

    private void initGridLayout() {

        grid.setWidth("100%");
        grid.getStyle().set("min-height", "400px");
        grid.getStyle().set("flex-grow", "1");
        grid.setHeight("100%");
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);

        grid.addColumn(TemplateRenderer.<MasterUser>of(
                "<Label style=\"padding-left: 5px;\"><small>[[item.name]]</small></Label>")
                .withProperty("name", MasterUser::getFullName))
                .setHeader(new BoldLabel("Full Name", "5px"))
                .setResizable(true)
                .setWidth("60%");

        grid.addItemClickListener(listener -> {

            Dialogs.notifyWarning("Edit dialog goes here.");

        });

        gridLayout.add(grid, moduleBottomBar);

    }

    private void initChartLayout() {

        chartLayout.getStyle().set("height", "-moz-fit-content");

    }

    private void reloadCharts() {

        chartLayout.removeAll();

    }

    private void loadDB() {

        userList.add(Session.getCurrentUser());
        grid.setItems(userList);
        moduleBottomBar.updateStatus(Utilities.formatNumber(userList.size()) + " item" +
                (userList.size() > 1 ? "s" : ""));
        reloadCharts();

    }

    @Override
    public void hideContent() {

        grid.setVisible(false);

    }

    @Override
    public void refreshContent() {

        grid.setVisible(true);

    }

    private void addNew() {

        Dialogs.notifyWarning("Add New goes here.");

    }

    private void showReport() {}
}
