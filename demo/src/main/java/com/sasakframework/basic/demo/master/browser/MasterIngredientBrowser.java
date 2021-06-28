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

import com.sasakframework.basic.demo.master.MasterIngredient;
import com.sasakframework.basic.demo.master.MasterRecipe;
import com.sasakframework.basic.demo.master.MasterRecipeIngredient;
import com.sasakframework.basic.demo.master.dialog.MasterIngredientEditor;
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
public class MasterIngredientBrowser extends ModuleBrowser {

    private ModuleToolbar moduleToolbar;
    private Grid<MasterIngredient> grid = new Grid<>();
    private ModuleBottomBar moduleBottomBar = new ModuleBottomBar(true,
            true);

    private ModuleGridLayout gridLayout = new ModuleGridLayout();
    private Div chartLayout = new Div();

    private String moduleName = "MasterIngredient";

    private List<MasterIngredient> ingredientList;

    public MasterIngredientBrowser() {

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

        chartLayout.setId("chart-layout");
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

        grid.addColumn(TemplateRenderer.<MasterIngredient>of(
                "<Label style=\"padding-left: 5px;\"><small>[[item.name]]</small></Label>")
                .withProperty("name", MasterIngredient::getName))
                .setHeader(new BoldLabel("Role", "5px"))
                .setResizable(true)
                .setWidth("60%");
        grid.addColumn(TemplateRenderer.<MasterIngredient>of(
                "<Label><small>[[item.unit]]</small></Label>")
                .withProperty("unit", MasterIngredient::getUnit))
                .setHeader(new BoldLabel("Unit", "5px"))
                .setResizable(true)
                .setWidth("150px");
        grid.addColumn(TemplateRenderer.<MasterIngredient>of(
                "<Label><small>[[item.costPerUnit]]</small></Label>")
                .withProperty("costPerUnit", i -> "$" + Utilities.formatNumber(i.getCostPerUnit())))
                .setHeader(new BoldLabel("Cost Per Unit", "5px"))
                .setResizable(true)
                .setWidth("150px");


        grid.addItemClickListener(listener -> {

            MasterIngredient masterIngredient = listener.getItem();
            
            MasterIngredientEditor editor = new MasterIngredientEditor(true, masterIngredient.getId(),
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
        initChart2();

    }

    private void loadDB() {
        
        ingredientList = Session.getCookBiz().getIngredientList();
        List<MasterIngredient> finalList = new ArrayList<>();

        String s = moduleToolbar.getSearchText();

        if ((s != null) && (!s.trim().isEmpty())) {
            ingredientList.forEach(i ->
            {if (i.getName().toLowerCase().contains(s)) finalList.add(i);});
        } else {
            finalList.addAll(ingredientList);
        }

        finalList.sort(Comparator.comparing(i -> i.getName().toLowerCase()));
        grid.setItems(finalList);
        moduleBottomBar.updateStatus(Utilities.formatNumber(ingredientList.size()) + " item" +
                (ingredientList.size() > 1 ? "s" : ""));
        reloadCharts();

        for (Menu m : Session.getCookBiz().getMenuList()) {
            if (m.getMenuName().equals("Ingredient")) {
                m.setBadge(String.valueOf(ingredientList.size()));
            }
        }
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

        List<MasterIngredient> ingredientList = Session.getCookBiz().getIngredientList();
        List<MasterRecipe> recipeList = Session.getCookBiz().getRecipeList();
        
        Map<String, Integer> volumeSubject = new HashMap<>();

        try {
            for (MasterIngredient ingredient : ingredientList) {
                int totalUsage = 0;

                for (MasterRecipe recipe : recipeList) {
                    if (recipe.getIngredientList() != null) {
                        for (MasterRecipeIngredient i : recipe.getIngredientList()) {
                            if (i.getIngredientId() == ingredient.getId()) {
                                totalUsage++;
                            }
                        }
                    }
                }

                if (totalUsage > 0) volumeSubject.put(ingredient.getName(), totalUsage);
            }
        } catch (Exception ex) {
            Dialogs.notifyError(moduleName, 0,
                    "Failed to load Ingredient chart data: \n" + ex);
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

        Title title = new Title("Recipe Occurrence");
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

    private void initChart2() {

        SOChart chart = new SOChart();
        chart.setSize("500px", "300px");

        PieChart pieChart = new PieChart();
        pieChart.setName("Total Usage");
        Position p = new Position();
        p.setTop(Size.pixels(20));
        pieChart.setPosition(p);

        DataMatrix dataMatrix = new DataMatrix();
        List<String> deptList = new ArrayList<>();

        List<MasterIngredient> ingredientList = Session.getCookBiz().getIngredientList();
        List<MasterRecipe> recipeList = Session.getCookBiz().getRecipeList();

        Map<String, Double> volumeSubject = new HashMap<>();

        try {
            for (MasterIngredient ingredient : ingredientList) {
                double totalVolume = 0;

                for (MasterRecipe recipe : recipeList) {
                    if (recipe.getIngredientList() != null) {
                        for (MasterRecipeIngredient i : recipe.getIngredientList()) {
                            if (i.getIngredientId() == ingredient.getId()) {
                                totalVolume += i.getVolume();
                            }
                        }
                    }
                }

                if (totalVolume > 0) volumeSubject.put(ingredient.getName(), totalVolume);
            }
        } catch (Exception ex) {
            Dialogs.notifyError(moduleName, 0,
                    "Failed to load Ingredient chart data: \n" + ex);
            return;
        }

        Map<String, Double> volumeSubjectCounted = volumeSubject
                .entrySet()
                .stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));

        Map<String, Double> volumeSubjectSummarized = new HashMap<>();

        int cn = 0;
        double totalOthers = 0;

        for (Map.Entry<String, Double> entry : volumeSubjectCounted.entrySet()) {
            cn ++;

            if (cn <= 10) {
                volumeSubjectSummarized.put(entry.getKey(), entry.getValue());
            } else {
                totalOthers += entry.getValue();
            }
        }

        Map<String, Double> volumeSubjectFinal = volumeSubjectSummarized
                .entrySet()
                .stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        if (totalOthers > 0) volumeSubjectFinal.put("OTHERS", totalOthers);

        String[] columns = new String[volumeSubjectFinal.size()];

        Data data = new Data();
        cn = 0;
        for (Map.Entry<String, Double> entry : volumeSubjectFinal.entrySet()) {
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

        Title title = new Title("Recipe Usage By Volume");
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

        MasterIngredientEditor editor = new MasterIngredientEditor(false, 0,
                i -> {
                    loadDB();
                    moduleBottomBar.getMenuAdd().focus();
                }, null);

        if (editor.start()) add(editor);

    }

    private void showReport() {}
}
