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

import com.sasakframework.component.basic.IntegerField;
import com.sasakframework.component.basic.LocalDatePicker;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.wontlost.ckeditor.VaadinCKEditor;

@SuppressWarnings("Duplicates")
public class CheckField {
	
	public static boolean checkTextField(TextField textField, String fieldName) {

		if (textField.getValue() == null) {
			Dialogs.notifyWarning(fieldName + " is empty or invalid.");
			return false;
		}

		if  (!textField.getValue().isEmpty()) {
			return true;
		}
		else {
			Dialogs.notifyWarning(fieldName + " is empty or invalid.");
			return false;
		}		
	}

	public static boolean checkTextField(VaadinCKEditor textField, String fieldName) {

		if (textField.getValue() == null) {
			Dialogs.notifyWarning(fieldName + " is empty or invalid.");
			return false;
		}

		if  (!textField.getValue().isEmpty()) {
			return true;
		}
		else {
			Dialogs.notifyWarning(fieldName + " is empty or invalid.");
			return false;
		}
	}

	public static boolean checkNumberField(NumberField numberField, String fieldName) {

		if (numberField.getValue() == null) {
			Dialogs.notifyWarning(fieldName + " is empty or invalid.");
			return false;
		}

		return true;

	}

	public static boolean checkNumberField(TextField textField, String fieldName) {

		if (textField.getValue() == null) {
			Dialogs.notifyWarning(fieldName + " is empty or invalid.");
			return false;
		}

		if  (!textField.getValue().isEmpty()) {
			Double dbl = Double.parseDouble(textField.getValue());
			if (dbl < 1) {
				Dialogs.notifyWarning(fieldName + " is empty or invalid.");
				return  false;
			}

			return true;
		}else {
			Dialogs.notifyWarning(fieldName + " is empty or invalid.");
			return false;
		}
	}

	public static boolean checkIntegerField(IntegerField integerField) {

		return checkNumberField(integerField, integerField.getTitle());

	}

	public static boolean checkIntegerField(IntegerField integerField, String fieldName) {

		if (fieldName == null) fieldName = "";

		if (integerField.getIntValue() < 1) {
			Dialogs.notifyWarning(fieldName + " is empty or invalid.");
			return false;
		}

		return true;

	}

	public static boolean checkTextField(TextField textField) {

		return checkTextField(textField, textField.getTitle());

	}


	public static boolean checkTextField(TextArea textArea) {

		return checkTextField(textArea, textArea.getLabel());

	}

	public static boolean checkTextField(TextArea textArea, String fieldName) {

		if (textArea.getValue() == null) {
			Dialogs.notifyWarning(fieldName + " is empty or invalid.");
			return false;
		}

		if  (!textArea.getValue().isEmpty()) {
			return true;
		}
		else {
			Dialogs.notifyWarning(fieldName + " is empty or invalid.");
			return false;
		}		
	}

	public static boolean checkTextField(PasswordField passwordField, String fieldName) {

		if (passwordField.getValue() == null) {
			Dialogs.notifyWarning(fieldName + " is empty or invalid.");
			return false;
		}

		if  (!passwordField.getValue().isEmpty()) {
			return true;
		}
		else {
			Dialogs.notifyWarning(fieldName + " is empty or invalid.");
			return false;
		}		
	}

	public static boolean checkComboField(ComboBox comboBox) {

		return checkComboField(comboBox, comboBox.getLabel());

	}

	public static boolean checkComboField(ComboBox comboField, String fieldName) {
		
		if (comboField.getValue() == null) {
			Dialogs.notifyWarning(fieldName + " is empty or invalid.");
			return false;
		}
		else if  (!comboField.getValue().toString().isEmpty()) {
			if ((comboField.getValue().toString().trim() == "")) {
				Dialogs.notifyWarning(fieldName + " is empty or invalid.");
				return false;
			}
			return true;
		}
		else {
			Dialogs.notifyWarning(fieldName + " is empty or invalid.");
			return false;
		}		
	}

	public static boolean checkSelectField(Select selectField) {

		return checkSelectField(selectField, selectField.getLabel());

	}

	public static boolean checkSelectField(Select selectField, String fieldName) {

		if (selectField.getValue() == null) {
			Dialogs.notifyWarning(fieldName + " is empty or invalid.");
			return false;
		}
		else if  (!selectField.getValue().toString().isEmpty()) {
			if ((selectField.getValue().toString().trim() == "")) {
				Dialogs.notifyWarning(fieldName + " is empty or invalid.");
				return false;
			}
			return true;
		}
		else {
			Dialogs.notifyWarning(fieldName + " is empty or invalid.");
			return false;
		}
	}

	public static boolean checkDateField(LocalDatePicker popupDateField, String fieldName) {
		
		if  (!popupDateField.isEmpty()) {
			return true;
		}
		else {
			Dialogs.notifyWarning(fieldName + " is empty or invalid.");
			return false;
		}		
	}

	public static boolean checkDateField(LocalDatePicker dateField) {

		return checkDateField(dateField, dateField.getLabel());

	}

	public static boolean checkPasswordField(PasswordField password, PasswordField retypePassword) {
		
		if  (password.getValue().isEmpty()) {
			Dialogs.notifyWarning("Password is empty");
			return false;
		}else {
			if (password.getValue().equals(retypePassword.getValue())) {
				return true;
			}
			else {
				Dialogs.notifyWarning("Retyped password is incorrect.");
				return false;
			}
		}		
	}

	public static boolean checkField(Component component, String title) {

		if (component instanceof TextField) {
			return checkTextField((TextField) component, title);
		} else if (component instanceof ComboBox) {
			return checkComboField((ComboBox) component, title);
		} else if (component instanceof LocalDatePicker) {
			return checkDateField((LocalDatePicker) component, title);
		} else {
			Dialogs.notifyWarning("Could not check unknown field type: " + title);
			return false;
		}

	}
}
