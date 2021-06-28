/*
 * Copyright (c) 2021. Sasak UI. All rights reserved.
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
 */

package com.sasakframework.component.basic;

import com.sasakframework.utilities.Utilities;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexLayout;

import java.util.ArrayList;
import java.util.List;

@CssImport("./styles/module-styles.css")
public class ModuleToolbar extends FlexLayout {

    private String year = String.valueOf(Utilities.getCurrentYear());
    private boolean filterButtonVisible = true;
    private boolean searchTextFieldVisible = true;
    private boolean refreshButtonVisible = true;

    private Div menuBack = new Div();
    private ModuleSearchTextField searchTextField = new ModuleSearchTextField();
    private Div menuRefresh = new Div();
    private Div menuFilter = new Div();

    private String moduleName;
    private ModuleBrowser parentLayout;

    private List<GoBackListener> goBackListeners = new ArrayList<>();
    private List<FilterClickListener> filterClickListeners = new ArrayList<>();
    private List<SearchTextChangedListener> searchTextChangedListeners = new ArrayList<>();
    private List<RefreshListener> refreshListeners = new ArrayList<>();

    private Label lblYear;
    private Icon filterIcon;

    public interface GoBackListener {
        void goBack();
    }

    public interface SearchTextChangedListener {
        void searchTextChanged(String searchText);
    }

    public interface FilterClickListener {
        void filterButtonClicked();
    }

    public interface RefreshListener {
        void refresh();
    }

    public ModuleToolbar(String moduleName, ModuleBrowser parentLayout) {

        this.moduleName = moduleName;
        this.parentLayout = parentLayout;

        setWidth("calc(100% - 10px)");
        setHeight("45px");
        setAlignItems(Alignment.CENTER);
        setFlexGrow(1);
        getStyle().set("font-size", "small");
        getStyle().set("margin-left", "5px");
        getStyle().set("margin-right", "5px");

        Icon backIcon = VaadinIcon.BACKSPACE.create();
        backIcon.setSize("15px");
        backIcon.setColor("inherit");

        Label lblBack = new Label("Back");
        lblBack.getStyle().set("color", "inherit");
        lblBack.getStyle().set("margin-left", "10px");

        menuBack.addClassName("module-button");
        menuBack.add(backIcon, lblBack);
        menuBack.getStyle().set("align-items", "center");
        menuBack.addClickListener(listener -> {

            if (goBackListeners != null) {
                for (GoBackListener goBackListener : goBackListeners) {
                    goBackListener.goBack();
                }
            }

        });

        searchTextField.getStyle().set("flex-grow", "1");
        searchTextField.getStyle().set("margin", "0px 5px");
        searchTextField.getStyle().set("align-items", "center");
        searchTextField.addValueChangeListener(listener -> {

            if (searchTextChangedListeners != null) {
                for (SearchTextChangedListener searchTextChangedListener : searchTextChangedListeners) {
                    searchTextChangedListener.searchTextChanged(searchTextField.getValue());
                }
            }

        });

        Icon refreshIcon = VaadinIcon.REFRESH.create();
        refreshIcon.setSize("15px");
        refreshIcon.setColor("inherit");

        menuRefresh.addClassName("module-button");
        menuRefresh.add(refreshIcon);
        menuRefresh.getStyle().set("align-items", "center");
        menuRefresh.setWidth("16px");
        menuRefresh.getStyle().set("margin-right", "5px");

        menuRefresh.addClickListener(listener -> {

            if (refreshListeners != null) {
                for (RefreshListener refreshListener : refreshListeners) {
                    refreshListener.refresh();
                }
            }

        });

        lblYear = new Label(year);

        filterIcon = VaadinIcon.FILTER.create();
        filterIcon.setSize("15px");
        filterIcon.setColor("inherit");
        filterIcon.getStyle().set("margin-left", "20px");

        menuFilter.addClassName("module-button");

        menuFilter.add(filterIcon);
        menuFilter.addClickListener(listener -> {
            if (filterClickListeners != null) {
                for (FilterClickListener filterClickListener : filterClickListeners) {
                    filterClickListener.filterButtonClicked();
                }
            }
        });

        refreshButtons();

    }

    private void refreshButtons() {

        removeAll();
        add(menuBack);
        if (searchTextFieldVisible) {
            add(searchTextField);
        } else {
            add(new FullWidthSpacer());
        }

        if (refreshButtonVisible) add(menuRefresh);
        menuFilter.removeAll();
        if (year != null) {
            menuFilter.add(lblYear);
            menuFilter.setWidth("65px");
        } else {
            menuFilter.getStyle().remove("width");
        }
        menuFilter.add(filterIcon);
        if (filterButtonVisible) add(menuFilter);

    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
        refreshButtons();
    }

    public boolean isFilterButtonVisible() {
        return filterButtonVisible;
    }

    public void setFilterButtonVisible(boolean filterButtonVisible) {
        this.filterButtonVisible = filterButtonVisible;
        refreshButtons();
    }

    public boolean isSearchTextFieldVisible() {
        return searchTextFieldVisible;
    }

    public void setSearchTextFieldVisible(boolean searchTextFieldVisible) {
        this.searchTextFieldVisible = searchTextFieldVisible;
        refreshButtons();
    }

    public boolean isRefreshButtonVisible() {
        return refreshButtonVisible;
    }

    public void setRefreshButtonVisible(boolean refreshButtonVisible) {
        this.refreshButtonVisible = refreshButtonVisible;
        refreshButtons();
    }

    public void addGoBackListener(GoBackListener listener) {

        goBackListeners.add(listener);

    }

    public void addSearchTextChangedListener(SearchTextChangedListener listener) {

        searchTextChangedListeners.add(listener);

    }

    public void addFilterClickedListener(FilterClickListener listener) {

        filterClickListeners.add(listener);

    }

    public void addRefreshListener(RefreshListener listener) {

        refreshListeners.add(listener);

    }

    public String getCurrentYear() {return lblYear.getText();}

    public String getSearchText() {

        return searchTextField.getValue();

    }
}
