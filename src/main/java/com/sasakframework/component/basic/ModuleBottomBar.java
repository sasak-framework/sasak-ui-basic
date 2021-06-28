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

import com.vaadin.flow.component.Focusable;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;

import java.util.ArrayList;
import java.util.List;

@CssImport("./styles/module-styles.css")
public class ModuleBottomBar extends Div {

    private List<AddButtonClickListener> addButtonClickListeners = new ArrayList<>();
    private List<ReportButtonClickListener> reportButtonClickListeners = new ArrayList<>();

    private boolean menuAddVisible = true;
    private boolean menuReportVisible = true;

    private Button menuAdd = new Button(VaadinIcon.PLUS_CIRCLE_O.create(), "Add");
    private Button menuReport = new Button(VaadinIcon.PRINT.create(), "Report");

    private Label lblStatus = new Label();

    private String statusText = "0 item";

    public interface AddButtonClickListener {
        void click();
    }

    public interface ReportButtonClickListener {
        void click();
    }

    public ModuleBottomBar() {

        initLayout();

    }

    public ModuleBottomBar(boolean menuAddVisible, boolean menuReportVisible) {

        this.menuAddVisible = menuAddVisible;
        this.menuReportVisible = menuReportVisible;
        initLayout();

    }

    private void initLayout() {

        getStyle().set("display", "flex");
        setWidth("100%");
        getStyle().set("align-items", "center");

        lblStatus.setText(statusText);
        lblStatus.addClassName("module-status");
        menuAdd.addClassName("module-button");
        menuAdd.addClickListener(listener -> {
            for (AddButtonClickListener listener1 : addButtonClickListeners) {
                listener1.click();
            }
        });

        menuReport.addClassName("module-button");
        menuReport.addClickListener(listener -> {
            for (ReportButtonClickListener listener1 : reportButtonClickListeners) {
                listener1.click();
            }
        });

        refreshButtons();

    }

    private void refreshButtons() {

        removeAll();
        add(lblStatus, new FullWidthSpacer());
        if (menuAddVisible) add(menuAdd);
        if (menuReportVisible) add(menuReport);

    }

    public boolean isMenuAddVisible() {
        return menuAddVisible;
    }

    public void setMenuAddVisible(boolean menuAddVisible) {
        this.menuAddVisible = menuAddVisible;
        refreshButtons();
    }

    public boolean isMenuReportVisible() {
        return menuReportVisible;
    }

    public void setMenuReportVisible(boolean menuReportVisible) {
        this.menuReportVisible = menuReportVisible;
        refreshButtons();
    }

    public class Button extends Div implements Focusable {

        public Button(Icon icon, String caption) {

            icon.setSize("15px");
            icon.setColor("inherit");

            Label label = new Label(caption);
            label.getStyle().set("font-size", "small");
            label.getStyle().set("color", "inherit");
            label.getStyle().set("margin-left", "10px");

            getStyle().set("display", "inline-flex");
            getStyle().set("margin", "5px 10px");
            getStyle().set("align-items", "center");

            add(icon, label);

        }
    }

    public void setStatusVisible(boolean visible) {

        lblStatus.setVisible(visible);

    }

    public boolean isStatusVisible(boolean menuAddVisible) {

        return lblStatus.isVisible();

    }

    public void updateStatus(String statusText) {

        this.statusText = statusText;
        lblStatus.setText(statusText);

    }

    public void addAddButtonClickListener(AddButtonClickListener listener) {
        addButtonClickListeners.add(listener);
    }

    public void addReportButtonClickListener(ReportButtonClickListener listener) {
        reportButtonClickListeners.add(listener);
    }

    public Button getMenuAdd() {
        return menuAdd;
    }

    public Button getMenuReport() {
        return menuReport;
    }

    public Label getStatusLabel() {
        return lblStatus;
    }
}
