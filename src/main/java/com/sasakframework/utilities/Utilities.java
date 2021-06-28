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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wontlost.ckeditor.Config;
import com.wontlost.ckeditor.Constants;
import com.wontlost.ckeditor.VaadinCKEditor;
import com.wontlost.ckeditor.VaadinCKEditorBuilder;
import org.apache.commons.io.FilenameUtils;
import org.joda.time.Days;
import org.joda.time.LocalDate;

import java.lang.reflect.Field;
import java.net.Inet4Address;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

@SuppressWarnings("Duplicates")
public class Utilities {

    public static int toInt(Object obj) {
        try {
            if (obj == null) {
                return 0;
            } else {
                return Integer.parseInt(String.valueOf(obj));
            }
        } catch (Exception e) {
            return 0;
        }
    }

    public static String formatDate(Date date, String format) {

        if (date == null) return null;

        try {
            DateFormat df = new SimpleDateFormat(format, Locale.getDefault());
            return df.format(date);
        } catch (Exception ex) {
            return null;
        }

    }

    public static String getFileExtension(final String filename) {
        if (filename == null) return null;
        /*final String afterLastSlash = filename.substring(filename.lastIndexOf('/') + 1);
        final int afterLastBackslash = afterLastSlash.lastIndexOf('\\') + 1;
        final int dotIndex = afterLastSlash.indexOf('.', afterLastBackslash);
        return (dotIndex == -1) ? "" : afterLastSlash.substring(dotIndex + 1);*/

        return FilenameUtils.getExtension(filename);
    }

    public static String formatNumber(int value) {

        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
        DecimalFormat formatter = new DecimalFormat("#,###,###,##0.##", symbols);
        return formatter.format(value);

    }

    public static String formatNumber(long value) {

        DecimalFormat formatter = new DecimalFormat("###,##0.##",
                new DecimalFormatSymbols(Locale.getDefault()));
        return formatter.format(value);

    }

    public static String formatNumber(double value) {

        DecimalFormat formatter = new DecimalFormat("###,##0.##",
                new DecimalFormatSymbols(Locale.getDefault()));
        return formatter.format(value);

    }

    public static int getCurrentYear() {

        return Calendar.getInstance().get(Calendar.YEAR);

    }

    public static int compareDate(Date date1, Date date2) {

        Days d = Days.daysBetween(new LocalDate(date1), new LocalDate(date2));
        return d.getDays();

    }

    public static int getCurrentMonth() {

        return Calendar.getInstance().get(Calendar.MONTH);

    }
    
    public static int getYear(Date date) {
        
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        
        return calendar.get(Calendar.YEAR);
        
    }
    
    public static int getMonth(Date date) {
        
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        
        return calendar.get(Calendar.MONTH);
        
    }
    
    public static String getMd5(String text) {

        if (text == null) return "";

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] source = text.getBytes("UTF-8");
            byte[] dest = md.digest(source);

            if (dest == null) return "";
            return dest.toString();

        } catch (Exception ex) {
            Dialogs.notifyError("Failed to get md5 digest:\n" + ex);
            return "";
        }
    }

    public static String getServerIp() {

        try {
            return Inet4Address.getLocalHost().getHostAddress();
        } catch (Exception ex) {
            return "";
        }
    }

    public static void copyVar(Object originalObj, Object newObj) {

        if (originalObj.getClass() == newObj.getClass()) {
            for (Field field1 : originalObj.getClass().getDeclaredFields()) {
                field1.setAccessible(true);

                for (Field field2 : newObj.getClass().getDeclaredFields()) {
                    if (field1.getName() == field2.getName()) {
                        field2.setAccessible(true);
                        try {
                            switch (field2.getType().getSimpleName()) {
                                case "Boolean":
                                case "boolean":
                                    field2.set(newObj, Boolean.parseBoolean(getFieldValue(field1, originalObj).toString()));
                                    break;
                                case "long":
                                    field2.set(newObj, Long.parseLong(getFieldValue(field1, originalObj).toString()));
                                    break;
                                case "int":
                                    field2.set(newObj, Integer.parseInt(getFieldValue(field1, originalObj).toString()));
                                    break;
                                case "Date":
                                    field2.set(newObj,field1.get(originalObj));
                                    break;
                                default:
                                    field2.set(newObj, getFieldValue(field1, originalObj));
                            }
                        } catch (IllegalArgumentException e) {
                        } catch (IllegalAccessException e) {
                        }
                    }
                }
            }
        }
    }

    public static Object cloneVar(Object originalObj) {

        try {
            ObjectMapper mapper = new ObjectMapper();
            String jsonString = mapper.writeValueAsString(originalObj);

            Object newObj = mapper.readValue(jsonString, originalObj.getClass());
            return newObj;
        } catch (Exception ex) {
            Dialogs.notifyError("Utility", 0, "Failed to copy variable: \n" + ex);
            return null;
        }
    }

    static Object getFieldValue(Field field, Object obj) {

        field.setAccessible(true);
        Object tmp = null;

        try {
            tmp = field.get(obj).toString();
        } catch (Exception e) {
        }

        return tmp;

    }

    public static LocalTime getLocalTime(Date date) {

        if (date == null) {
            return null;
        } else {
            return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().toLocalTime();
        }
    }

    public static VaadinCKEditor getDefaultCKEditor() {
        
        return new VaadinCKEditorBuilder().with(builder -> {
            builder.editorType = Constants.EditorType.CLASSIC;
            builder.theme = Constants.ThemeType.LIGHT;
            builder.width = "calc(100% - 5px)";
            builder.margin = "-20px 0 0 0";
            builder.config = getCKEditorDefaultConfig();
        }).createVaadinCKEditor();
        
    }
    
    public static Config getCKEditorDefaultConfig() {

        Constants.Toolbar[] toolbar = new Constants.Toolbar[] {Constants.Toolbar.undo,
                Constants.Toolbar.fontFamily, Constants.Toolbar.fontSize,
                Constants.Toolbar.fontColor, Constants.Toolbar.bold, Constants.Toolbar.underline, Constants.Toolbar.italic,
                Constants.Toolbar.alignment, Constants.Toolbar.numberedList, Constants.Toolbar.bulletedList,
                Constants.Toolbar.indent
        };
        Config config = new Config();
        config.setEditorToolBar(toolbar);

        return config;

    }
}
