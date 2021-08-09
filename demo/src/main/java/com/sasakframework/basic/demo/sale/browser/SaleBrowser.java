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

package com.sasakframework.basic.demo.sale.browser;

import com.sasakframework.Vars;
import com.sasakframework.basic.demo.master.MasterRecipe;
import com.sasakframework.basic.demo.sale.Sale;
import com.sasakframework.basic.demo.sale.SaleItem;
import com.sasakframework.basic.demo.sale.dialog.SaleEditor;
import com.sasakframework.basic.demo.session.Session;
import com.sasakframework.component.basic.*;
import com.sasakframework.utilities.Dialogs;
import com.sasakframework.utilities.Utilities;
import com.storedobject.chart.*;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.data.renderer.TemplateRenderer;

import java.text.SimpleDateFormat;
import java.util.*;

import static java.util.stream.Collectors.toMap;

@SuppressWarnings("Duplicates")
public class SaleBrowser extends ModuleBrowser {

    private ModuleToolbar moduleToolbar;
    private Grid<Sale> grid = new Grid<>();
    private ModuleBottomBar moduleBottomBar = new ModuleBottomBar(true,
            true);

    private ModuleGridLayout gridLayout = new ModuleGridLayout();
    private Div chartLayout = new Div();

    private String moduleName = "Sale";

    private List<Sale> saleList;

    public SaleBrowser() {

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

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm");

        grid.addColumn(TemplateRenderer.<Sale>of(
                "<Label style=\"padding-left: 5px;\"><small>[[item.date]]</small></Label>")
                .withProperty("date", s -> df.format(s.getDate())))
                .setHeader(new BoldLabel("Date", "5px"))
                .setResizable(true)
                .setWidth("120px");
        grid.addColumn(TemplateRenderer.<Sale>of(
                "<Label style=\"padding-left: 5px;\"><small>[[item.name]]</small></Label>")
                .withProperty("name", Sale::getCustomerName))
                .setHeader(new BoldLabel("Customer Name", "5px"))
                .setResizable(true)
                .setWidth("300px");
        grid.addColumn(TemplateRenderer.<Sale>of(
                "<div style=\"overflow-wrap: break-word; height: auto; " +
                        "width: calc(100% - 5px); white-space: normal;\">" +
                        "<small>[[item.orders]]</small></div>")
                .withProperty("orders", Sale::getItemListString))
                .setHeader(new BoldLabel("Items", "5px"))
                .setResizable(true)
                .setWidth("300px");
        grid.addColumn(TemplateRenderer.<Sale>of(
                "<Label style=\"padding-left: 5px;\"><small>[[item.total]]</small></Label>")
                .withProperty("total", s -> "$" + s.getTotal()))
                .setHeader(new BoldLabel("Total", "5px"))
                .setResizable(true)
                .setWidth("300px");


        grid.addItemClickListener(listener -> {

            Sale sale = listener.getItem();
            
            SaleEditor editor = new SaleEditor(true, sale.getId(),
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
        
        saleList = Session.getCookBiz().getSaleList();
        List<Sale> finalList = new ArrayList<>();

        String s = moduleToolbar.getSearchText();

        if ((s != null) && (!s.trim().isEmpty())) {
            saleList.forEach(i ->
            {if (i.getCustomerName().toLowerCase().contains(s)) finalList.add(i);});
        } else {
            finalList.addAll(saleList);
        }

        finalList.sort(Comparator.comparingLong(sale -> sale.getDate().getTime()));
        grid.setItems(finalList);
        moduleBottomBar.updateStatus(Utilities.formatNumber(saleList.size()) + " sale" +
                (saleList.size() > 1 ? "s" : ""));
        reloadCharts();

        for (Menu m : Session.getCookBiz().getMenuList()) {
            if (m.getMenuName().equals("Sale")) {
                m.setBadge(String.valueOf(saleList.size()));
            }
        }
    }

    private void initChart1() {

        SOChart soChart = new SOChart();
        soChart.setSize("100%", "300px");

        // To hold multiple charts
        List<Chart> charts = new ArrayList<>();
        Data data = new Data();

        // Define a data matrix to hold production data.
        DataMatrix dataMatrix = new DataMatrix();
        // Columns contain products
        dataMatrix.setColumnNames("Total Sale");
        dataMatrix.setColumnDataName("Sales");
        // Rows contain years of production
        dataMatrix.setRowNames(Vars.shortMonths);
        dataMatrix.setRowDataName("Month");
        // Add row values
        List<Sale> sales = Session.getCookBiz().getSaleList();
        if (sales == null) sales = new ArrayList<>();

        LinkedHashMap<String, Integer> monthlySales = new LinkedHashMap<>();

        for (int i = 0; i < 12; i++) {
            int totalSales = 0;
            for (Sale sale : sales) {
                if (Utilities.getMonth(sale.getDate()) == i) {
                    totalSales++;
                }
            }

            dataMatrix.addRow(totalSales);
        }

        // Define axes
        XAxis xAxisProduct = new XAxis(DataType.CATEGORY);
        xAxisProduct.setName(dataMatrix.getColumnDataName());
        XAxis xAxisYear = new XAxis(DataType.CATEGORY);
        xAxisYear.setName(dataMatrix.getRowDataName());
        YAxis yAxis = new YAxis(DataType.NUMBER);
        yAxis.setName(dataMatrix.getName());

        // Second rectangular coordinate
        RectangularCoordinate rc2 = new RectangularCoordinate();
        rc2.addAxis(xAxisYear, yAxis); // Same Y-axis is re-used here
        rc2.getPosition(true).setTop(Size.pixels(35)); // Position it leaving 55% space at the top

        // Bar chart variable
        BarChart bc;
        // Crate a bar chart for each data column
        for (int i = 0; i < dataMatrix.getColumnCount(); i++) {
            bc = new BarChart(dataMatrix.getRowNames(), dataMatrix.getColumn(i));
            bc.setName(dataMatrix.getColumnName(i));
            bc.plotOn(rc2);
            charts.add(bc);
            soChart.add(bc);
        }

        String year = "All Year";

        if (moduleToolbar.getCurrentYear() != null) {
            year = String.valueOf(moduleToolbar.getCurrentYear());
        }

        Title title = new Title("Sale Performance ( Year" + year + ")");
        Position p = new Position();
        p.justifyCenter();
        title.setPosition(p);

        TextStyle textStyle = new TextStyle();
        textStyle.setColor(new Color("white"));
        title.setTextStyle(textStyle);

        Toolbox toolbox = new Toolbox();
        toolbox.addButton(new Toolbox.Download());

        soChart.add(title, toolbox);
        soChart.disableDefaultLegend();
        chartLayout.add(soChart);

    }

    private void initChart2() {

        SOChart chart = new SOChart();
        chart.setSize("500px", "300px");

        PieChart pieChart = new PieChart();
        pieChart.setName("Total Sale");
        Position p = new Position();
        p.setTop(Size.pixels(20));
        pieChart.setPosition(p);

        DataMatrix dataMatrix = new DataMatrix();

        List<MasterRecipe> recipeList = Session.getCookBiz().getRecipeList();

        if (recipeList == null) recipeList = new ArrayList<>();

        Map<String, Integer> volumeSubject = new HashMap<>();

        try {
            for (MasterRecipe recipe : recipeList) {
                int totalVolume = 0;

                for (Sale sale : saleList) {
                    for (SaleItem item : sale.getItemList()) {
                        if (item.getRecipeId() == recipe.getId()) {
                            totalVolume++;
                        }
                    }
                }
                if (totalVolume > 0) volumeSubject.put(recipe.getName(), totalVolume);
            }
        } catch (Exception ex) {
            Dialogs.notifyError(moduleName, 0,
                    "Failed to load sale chart data: \n" + ex);
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

        Title title = new Title("Sales Volume");
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

    @Override
    public void hideContent() {

        grid.setVisible(false);

    }

    @Override
    public void refreshContent() {

        grid.setVisible(true);

    }

    private void addNew() {

        SaleEditor editor = new SaleEditor(false, 0,
                i -> {
                    loadDB();
                    moduleBottomBar.getMenuAdd().focus();
                }, null);

        if (editor.start()) add(editor);

    }

    private void showReport() {}
}
