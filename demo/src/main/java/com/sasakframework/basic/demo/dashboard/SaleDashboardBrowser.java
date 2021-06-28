package com.sasakframework.basic.demo.dashboard;

import com.sasakframework.Vars;
import com.sasakframework.basic.demo.master.*;
import com.sasakframework.basic.demo.sale.Sale;
import com.sasakframework.basic.demo.sale.SaleItem;
import com.sasakframework.basic.demo.session.Session;
import com.sasakframework.component.basic.FullWidthSpacer;
import com.sasakframework.component.basic.ModuleBrowser;
import com.sasakframework.component.basic.ModuleInnerContainer;
import com.sasakframework.component.basic.ModuleToolbar;
import com.sasakframework.utilities.Dialogs;
import com.sasakframework.utilities.Utilities;
import com.storedobject.chart.*;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import lombok.Data;

import java.util.*;

import static java.util.stream.Collectors.toMap;

@JsModule("./styles/dashboard-styles.js")
public class SaleDashboardBrowser extends ModuleBrowser {

    private final String moduleName = "DashboardSurveillance";

    private ModuleToolbar moduleToolbar;
    private FlexLayout backgroundToolbar;
    private FlexLayout chartLayout = new FlexLayout();

    private List<String> colorList = Arrays.asList("none", "white", "lightgray", "aliceblue", "blanchedalmond",
            "floralwhite", "paleturquoise", "lightyellow");

    private ComboBox<String> backgroundSelector = new ComboBox<>();

    @Data
    private class SurveillanceFindingStatus {String montName; int totalPendingApproval; int totalOpen; int totalClosed;}

    @Data
    class SurveillanceFindingArea {String areaCode; String areaName; int count;}

    public SaleDashboardBrowser() {

        initLayout();
    }

    private void initLayout() {

        moduleToolbar = new ModuleToolbar(moduleName, this);
        moduleToolbar.addGoBackListener(() -> {

            for (ModuleToolbar.GoBackListener listener : super.getGoBackListeners()) {
                listener.goBack();
            }

        });
        moduleToolbar.addRefreshListener(this::loadDB);
        moduleToolbar.setYear(null);
        moduleToolbar.setSearchTextFieldVisible(false);
        moduleToolbar.setFilterButtonVisible(false);

        initBackgroundSelector();
        initChartLayout();
        loadDB();

        ModuleInnerContainer innerContainer = new ModuleInnerContainer(chartLayout, backgroundToolbar);
        add(moduleToolbar, innerContainer);

    }

    private void initBackgroundSelector() {

        chartLayout.getStyle().set("background-color", "white");

        backgroundSelector.setClassName("color-selector");
        backgroundSelector.setItems(colorList);
        backgroundSelector.setValue("white");
        backgroundSelector.setAllowCustomValue(true);
        backgroundSelector.addCustomValueSetListener(l -> {
            if (l.getDetail() == null) return;
            if (l.getDetail().trim().length() < 1) {
                return;
            }

            colorList.add(l.getDetail());
            backgroundSelector.setItems(colorList);
        });

        backgroundSelector.addValueChangeListener(l -> {
            if ((l.getValue() == null) || (l.getValue().trim().isEmpty()) ||
                    (l.getValue().trim().length() < 3) ||
                    (l.getValue().trim().equals("none"))) {
                chartLayout.getStyle().remove("background-color");
            } else {
                chartLayout.getStyle().set("background-color", l.getValue());
            }
        });

        FormLayout formLayout = new FormLayout();
        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0px", 1,
                FormLayout.ResponsiveStep.LabelsPosition.ASIDE));
        Label backgroundLabel = new Label("Background:");
        backgroundLabel.getStyle().set("color", "white");
        formLayout.addFormItem(backgroundSelector, backgroundLabel);

        backgroundToolbar = new FlexLayout(new FullWidthSpacer(), formLayout);
        backgroundToolbar.setWidthFull();
        backgroundToolbar.getStyle().set("margin-right", "10px");

    }

    private void initChartLayout() {

        chartLayout.setFlexDirection(FlexDirection.ROW);
        chartLayout.setFlexWrap(FlexWrap.WRAP);
        chartLayout.setWidth("calc(100% - 40px)");
        chartLayout.setHeight("calc(100% - 90px)");
        chartLayout.getStyle().set("padding", "20px");
        chartLayout.getStyle().set("overflow", "auto");
    }

    private boolean loadDB() {

        chartLayout.removeAll();
        initSalesPerformanceChart();
        initSaleByProductChart();
        initIngredientOccurenceChart();
        initUtensilUsageChart();
        return true;

    }

    private void initSalesPerformanceChart() {

        SOChart soChart = new SOChart();
        soChart.setSize("100%", "400px");

        // To hold multiple charts
        List<Chart> charts = new ArrayList<>();
        com.storedobject.chart.Data data = new com.storedobject.chart.Data();

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

        Toolbox toolbox = new Toolbox();
        toolbox.addButton(new Toolbox.Download());

        soChart.add(title, toolbox);
        soChart.disableDefaultLegend();
        Div container = new Div(soChart);
        container.addClassName("chart-container");
        container.setWidthFull();
        chartLayout.add(container);
    }

    private void initSaleByProductChart() {

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

                for (Sale sale : Session.getCookBiz().getSaleList()) {
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

        com.storedobject.chart.Data data = new com.storedobject.chart.Data();
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

        Title title = new Title("Product Sale (Top 5)");
        p = new Position();
        p.justifyCenter();
        title.setPosition(p);

        Toolbox toolbox = new Toolbox();
        toolbox.addButton(new Toolbox.Download());
        chart.add(pieChart, title, toolbox);
        chart.disableDefaultLegend();
        Div container = new Div(chart);
        container.addClassName("chart-container");
        chartLayout.add(container);

    }

    private void initIngredientOccurenceChart() {

        SOChart chart = new SOChart();
        chart.setSize("500px", "300px");

        NightingaleRoseChart pieChart = new NightingaleRoseChart();
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

        com.storedobject.chart.Data data = new com.storedobject.chart.Data();
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

        Title title = new Title("Ingredient Usage");
        p = new Position();
        p.justifyCenter();
        title.setPosition(p);

        Toolbox toolbox = new Toolbox();
        toolbox.addButton(new Toolbox.Download());
        chart.add(pieChart, title, toolbox);
        chart.disableDefaultLegend();
        Div container = new Div(chart);
        container.addClassName("chart-container");
        chartLayout.add(container);

    }

    private void initUtensilUsageChart() {

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

        com.storedobject.chart.Data data = new com.storedobject.chart.Data();
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

        Title title = new Title("Utensil Usage");
        p = new Position();
        p.justifyCenter();
        title.setPosition(p);

        Toolbox toolbox = new Toolbox();
        toolbox.addButton(new Toolbox.Download());
        chart.add(pieChart, title, toolbox);
        chart.disableDefaultLegend();
        Div container = new Div(chart);
        container.addClassName("chart-container");
        chartLayout.add(container);

    }

}
