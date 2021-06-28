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

package com.sasakframework.utilities;

import com.sasakframework.component.basic.*;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

@SuppressWarnings("Duplicates")
public class Dialogs {

    public static void ask(String question, Runnable onYes, Runnable onNo) {
        ask("", question, onYes, onNo);
    }

    public static void ask(String title, String question, Runnable onYes) {
        ask(title, question, onYes, () -> {});
    }

    public static void ask(String title, String question, Runnable onYes, Runnable onNo) {

        com.vaadin.flow.component.dialog.Dialog window = new com.vaadin.flow.component.dialog.Dialog();
        window.setCloseOnOutsideClick(false);
        window.setCloseOnEsc(false);

        H3 titleLabel = new H3(title);
        //titleLabel.getStyle().set("font-weight", "bold");

        Div titleLayout = new Div(titleLabel);
        titleLayout.setWidth("100%");
        titleLayout.setHeight("30px");
        titleLayout.getStyle().set("margin-bottom", "20px");

        Icon icon = VaadinIcon.QUESTION_CIRCLE.create();
        icon.setSize("40px");
        icon.setColor("orange");

        HorizontalLayout hlIcon = new HorizontalLayout(icon);
        hlIcon.setVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        hlIcon.setHeight("100%");

        Label label = new Label(question);
        HorizontalLayout hlLabel = new HorizontalLayout(hlIcon, label);
        hlLabel.setAlignItems(FlexComponent.Alignment.CENTER);
        hlLabel.getElement().getStyle().set("padding-left", "20px");

        HorizontalLayout hlMessage = new HorizontalLayout(hlLabel);
        hlMessage.setAlignItems(FlexComponent.Alignment.CENTER);
        hlMessage.setMargin(false);
        hlMessage.setSpacing(true);

        Button btnYes = new Button("Yes");
        btnYes.getElement().setAttribute("theme", "primary");
        btnYes.addClickListener(listener -> {
            window.close();
            onYes.run();
        });

        Button btnNo = new Button("No");
        btnNo.getElement().setAttribute("theme", "tertiary");
        btnNo.addClickListener(listener -> {window.close(); onNo.run();});

        HorizontalLayout hlInnerButton = new HorizontalLayout(btnNo, btnYes);

        HorizontalLayout hlButtons = new HorizontalLayout(hlInnerButton);
        hlButtons.setWidth("100%");
        hlButtons.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        hlButtons.setVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        hlButtons.getElement().getStyle().set("margin-top", "20px");
        hlButtons.setPadding(false);
        hlButtons.setMargin(false);

        VerticalLayout vlMain = new VerticalLayout(hlMessage, hlButtons);
        vlMain.setPadding(false);
        vlMain.setMargin(false);
        vlMain.setSpacing(false);
        vlMain.getStyle().set("min-width", "350px");

        Spacer spacer = new Spacer();
        spacer.setWidth("100%");
        spacer.setHeight("30px");

        window.add(spacer, vlMain);
        window.open();
        btnYes.focus();
    }

    public static void notifyError(String errMessage) {

        notifyError("Unknown", 0, errMessage);

    }

    public static void notifyError(String moduleName, long refId, Exception exception) {

        notifyError(moduleName, refId, exception.toString());

    }

    public static void notifyError(String moduleName, long refId, String errMessage) {

        Window window = new Window("Error");

        window.setTitle("Error");

        if (errMessage == null) errMessage = "Unknown error.";
        String[] msg = errMessage.split("\n");

        if (msg == null) msg = new String[]{""};

        String title = msg[0];

        Icon icon = VaadinIcon.EXCLAMATION_CIRCLE.create();
        icon.setSize("40px");
        icon.setColor("red");

        HorizontalLayout hlIcon = new HorizontalLayout(icon);
        hlIcon.setAlignItems(FlexComponent.Alignment.CENTER);
        hlIcon.setHeight("100%");

        VerticalLayout vlMessage = new VerticalLayout();
        vlMessage.setPadding(false);
        vlMessage.getStyle().set("padding-left", "20px");
        vlMessage.getStyle().set("overflow", "auto");
        vlMessage.getStyle().set("max-height", "300px");

        if ((title != null) && (!title.isEmpty())) {
            if ((msg != null) && (msg.length > 1)) {
                vlMessage.add(new BoldLabel(title));
            } else {
                vlMessage.add(new Label(title));
            }
        }

        for (String s : msg) {
            if (!s.equals(title)) vlMessage.add(new Label(s));
        }

        HorizontalLayout hlMessage = new HorizontalLayout(hlIcon, vlMessage);
        hlMessage.setVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        hlMessage.setAlignItems(FlexComponent.Alignment.CENTER);
        hlMessage.setPadding(false);
        hlMessage.setSpacing(true);
        hlMessage.getStyle().set("padding", "10px");
        hlMessage.getStyle().set("max-height", "300px");

        Button btnOk = new Button("Ok");
        btnOk.getElement().setAttribute("theme", "primary");
        btnOk.addClickListener(listener -> {
            window.close();
        });


        Div vlMain = window.getContent();
        vlMain.getStyle().remove("width");
        vlMain.add(hlMessage);

        window.getFooter().add(new FullWidthSpacer(), btnOk);
        UI.getCurrent().add(window);
        btnOk.focus();

        try {
             System.out.println("ERROR: " + errMessage);
        }
        catch (Exception ex) {
            System.out.println("Dialogs.notifyError failed: \n");
            ex.printStackTrace();
        }
    }

    public static void notifyWarning(String warning) {

        com.vaadin.flow.component.dialog.Dialog window = new com.vaadin.flow.component.dialog.Dialog();
        window.getElement().getStyle().set("padding", "0");
        window.setCloseOnOutsideClick(false);
        window.setCloseOnEsc(false);

        String[] msg = warning.split("\n");

        Icon icon = VaadinIcon.EXCLAMATION_CIRCLE.create();
        icon.setSize("40px");
        icon.setColor("orange");

        HorizontalLayout hlIcon = new HorizontalLayout(icon);
        hlIcon.setAlignItems(FlexComponent.Alignment.CENTER);
        hlIcon.setHeight("100%");

        VerticalLayout vlMessage = new VerticalLayout();
        vlMessage.getStyle().set("padding-left", "20px");
        for (String s : msg) {
            if (s.contains("<strong>") && s.contains("</strong>")) {
                s = s.replace("<strong>", "");
                s = s.replace("</strong>", "");

                Label label = new Label(s);
                label.getStyle().set("font-weight", "bold");

                vlMessage.add(label);
            } else {
                Label label = new Label(s);
                vlMessage.add(label);
            }
        }

        HorizontalLayout hlMessage = new HorizontalLayout(hlIcon, vlMessage);
        hlMessage.setVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        hlMessage.setAlignItems(FlexComponent.Alignment.CENTER);
        hlMessage.setMargin(false);
        hlMessage.setSpacing(true);

        Button btnOk = new Button("Ok");
        btnOk.getElement().setAttribute("theme", "primary");
        btnOk.addClickListener(listener -> {
            window.close();
        });

        HorizontalLayout hlInnerButton = new HorizontalLayout(btnOk);

        HorizontalLayout hlButtons = new HorizontalLayout(hlInnerButton);
        hlButtons.setWidth("100%");
        hlButtons.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        hlButtons.setVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        hlButtons.getElement().getStyle().set("margin-top", "20px");

        VerticalLayout vlMain = new VerticalLayout(hlMessage, hlButtons);
        vlMain.setPadding(false);
        vlMain.setMargin(false);

        window.add(vlMain);
        window.getElement().getStyle().set("padding", "0 !important");
        window.open();
        btnOk.focus();

    }

    public static void notifyInfo(String info) {

        Notification.show(info, 3000, Notification.Position.TOP_CENTER);

    }

    public static void notifyInfoEx(String info) {

        com.vaadin.flow.component.dialog.Dialog window = new com.vaadin.flow.component.dialog.Dialog();
        window.getElement().getStyle().set("padding", "0");
        window.setCloseOnOutsideClick(false);
        window.setCloseOnEsc(false);

        String[] msg = info.split("\n");

        Icon icon = VaadinIcon.INFO.create();
        icon.setSize("40px");
        icon.setColor("lightblue");

        HorizontalLayout hlIcon = new HorizontalLayout(icon);
        hlIcon.setAlignItems(FlexComponent.Alignment.CENTER);
        hlIcon.setHeight("100%");

        VerticalLayout vlMessage = new VerticalLayout();
        vlMessage.getStyle().set("padding-left", "20px");
        for (String s : msg) {
            if (s.contains("<strong>") && s.contains("</strong>")) {
                s = s.replace("<strong>", "");
                s = s.replace("</strong>", "");

                Label label = new Label(s);
                label.getStyle().set("font-weight", "bold");

                vlMessage.add(label);
            } else {
                Label label = new Label(s);
                vlMessage.add(label);
            }
        }

        HorizontalLayout hlMessage = new HorizontalLayout(hlIcon, vlMessage);
        hlMessage.setVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        hlMessage.setAlignItems(FlexComponent.Alignment.CENTER);
        hlMessage.setMargin(false);
        hlMessage.setSpacing(true);

        Button btnOk = new Button("Ok");
        btnOk.getElement().setAttribute("theme", "primary");
        btnOk.addClickListener(listener -> {
            window.close();
        });

        HorizontalLayout hlInnerButton = new HorizontalLayout(btnOk);

        HorizontalLayout hlButtons = new HorizontalLayout(hlInnerButton);
        hlButtons.setWidth("100%");
        hlButtons.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        hlButtons.setVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        hlButtons.getElement().getStyle().set("margin-top", "20px");

        VerticalLayout vlMain = new VerticalLayout(hlMessage, hlButtons);
        vlMain.setPadding(false);
        vlMain.setMargin(false);

        window.add(vlMain);
        window.getElement().getStyle().set("padding", "0 !important");
        window.open();
        btnOk.focus();

    }

    public static void notifyDataSaved() {

        notifyInfo("Data saved.");

    }

    public static void notifyFieldRequired() {

        notifyWarning("Please fill all required fields.");

    }

    public static void notifyNothingSelected() {

        notifyWarning("No item selected.");

    }

    public static void notifyDataNotFound(String moduleName, long id) {

        notifyError(moduleName, id, "Data not found. Probably deleted.");

    }

}
