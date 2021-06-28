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

import com.sasakframework.basic.demo.master.MasterUtensil;
import com.sasakframework.basic.demo.master.MasterRecipe;
import com.sasakframework.basic.demo.master.MasterRecipeUtensil;
import com.sasakframework.basic.demo.master.dialog.MasterUtensilEditor;
import com.sasakframework.basic.demo.session.Session;
import com.sasakframework.component.basic.*;
import com.sasakframework.utilities.Dialogs;
import com.sasakframework.utilities.Utilities;
import com.storedobject.chart.*;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.data.renderer.TemplateRenderer;

import java.util.*;

import static java.util.stream.Collectors.toMap;

@SuppressWarnings("Duplicates")
public class MasterUtensilBrowser extends ModuleBrowser {

    private ModuleToolbar moduleToolbar;
    private Grid<MasterUtensil> grid = new Grid<>();
    private ModuleBottomBar moduleBottomBar = new ModuleBottomBar(true,
            true);

    private ModuleGridLayout gridLayout = new ModuleGridLayout();
    private Div chartLayout = new Div();

    private String moduleName = "MasterUtensil";

    private List<MasterUtensil> utensilList;

    public MasterUtensilBrowser() {

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

        grid.addColumn(TemplateRenderer.<MasterUtensil>of(
                "<Label style=\"padding-left: 5px;\"><small>[[item.name]]</small></Label>")
                .withProperty("name", MasterUtensil::getName))
                .setHeader(new BoldLabel("Role", "5px"))
                .setResizable(true)
                .setWidth("60%");

        grid.addItemClickListener(listener -> {

            MasterUtensil masterUtensil = listener.getItem();
            
            MasterUtensilEditor editor = new MasterUtensilEditor(true, masterUtensil.getId(),
                    i -> loadDB(), i -> loadDB());
            if (editor.start()) add(editor);

        });

        gridLayout.add(grid, moduleBottomBar);

    }

    private void initChartLayout() {

        chartLayout.getStyle().set("height", "-moz-fit-content");

    }

    private void reloadCharts() {

        chartLayout.removeAll();
        initChart1();

    }

    private void loadDB() {
        
        utensilList = Session.getCookBiz().getUtensilList();
        List<MasterUtensil> finalList = new ArrayList<>();

        String s = moduleToolbar.getSearchText();

        if ((s != null) && (!s.trim().isEmpty())) {
            utensilList.forEach(i ->
            {if (i.getName().toLowerCase().contains(s)) finalList.add(i);});
        } else {
            finalList.addAll(utensilList);
        }

        finalList.sort((i1, i2) -> i1.getName().toLowerCase().compareTo(i2.getName().toLowerCase()));
        grid.setItems(finalList);
        moduleBottomBar.updateStatus(Utilities.formatNumber(utensilList.size()) + " item" +
                (utensilList.size() > 1 ? "s" : ""));
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

    private void initChart1() {

        SOChart chart = new SOChart();
        chart.setSize("500px", "300px");

        PieChart pieChart = new PieChart();
        pieChart.setName("Total Usage");
        Position p = new Position();
        p.setTop(Size.pixels(20));
        pieChart.setPosition(p);

        DataMatrix dataMatrix = new DataMatrix();
        List<String> deptList = new ArrayList<>();

        List<MasterUtensil> utensilList = Session.getCookBiz().getUtensilList();
        List<MasterRecipe> recipeList = Session.getCookBiz().getRecipeList();
        
        Map<String, Integer> volumeSubject = new HashMap<>();

        try {
            for (MasterUtensil utensil : utensilList) {
                int totalUsage = 0;

                for (MasterRecipe recipe : recipeList) {
                    if (recipe.getUtensilList() != null) {
                        for (MasterRecipeUtensil i : recipe.getUtensilList()) {
                            if (i.getUtensilId() == utensil.getId()) {
                                totalUsage++;
                            }
                        }
                    }
                }

                if (totalUsage > 0) volumeSubject.put(utensil.getName(), totalUsage);
            }
        } catch (Exception ex) {
            Dialogs.notifyError(moduleName, 0,
                    "Failed to load Utensil chart data: \n" + ex);
            return;
        }

        Map<String, Integer> volumeSubjectCounted = volumeSubject
                .entrySet()
                .stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));

        Map<String, Integer> volumeSubjectSummarized = new HashMap<>();

        int cn = 0;
        int totalLain2 = 0;

        for (Map.Entry<String, Integer> entry : volumeSubjectCounted.entrySet()) {
            cn ++;

            if (cn <= 10) {
                volumeSubjectSummarized.put(entry.getKey(), entry.getValue());
            } else {
                totalLain2 += entry.getValue();
            }
        }

        Map<String, Integer> volumeSubjectFinal = volumeSubjectSummarized
                .entrySet()
                .stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        if (totalLain2 > 0) volumeSubjectFinal.put("OTHERS", totalLain2);

        String[] columns = new String[volumeSubjectFinal.size()];

        Data data = new Data();
        cn = 0;
        for (Map.Entry<String, Integer> entry : volumeSubjectFinal.entrySet()) {
            columns[cn] = entry.getKey();
            data.add(entry.getValue());
            cn++;
        }

        dataMatrix.setColumnNames(columns);
        dataMatrix.addRow(data);

        pieChart.setItemNames(dataMatrix.getColumnNames());
        pieChart.setData(dataMatrix.getRow(0));

        TextStyle whiteText = new TextStyle();
        whiteText.setColor(new Color("white"));

        Title title = new Title("Recipe Occurence");
        p = new Position();
        p.justifyCenter();
        title.setPosition(p);
        title.setTextStyle(whiteText);

        Toolbox toolbox = new Toolbox();
        toolbox.addButton(new Toolbox.Download());
        chart.add(pieChart, title, toolbox);
        chart.disableDefaultLegend();
        Div chartContainer = new Div(chart);
        chartContainer.getStyle().set("padding", "5px");
        chartContainer.getStyle().set("margin-bottom", "20px");
        chartLayout.add(chartContainer);

    }

    private void addNew() {

        MasterUtensilEditor editor = new MasterUtensilEditor(false, 0,
                i -> {
                    loadDB();
                    moduleBottomBar.getMenuAdd().focus();
                }, null);

        if (editor.start()) add(editor);

    }

    private void showReport() {}
}
