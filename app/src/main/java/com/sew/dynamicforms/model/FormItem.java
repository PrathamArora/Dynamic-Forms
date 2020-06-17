package com.sew.dynamicforms.model;

import com.sew.dynamicforms.activities.BaseActivity;
import com.sew.dynamicforms.helper.Validation;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

import static com.sew.dynamicforms.activities.BaseActivity.DEFAULT_DATE_FORMAT;
import static com.sew.dynamicforms.activities.BaseActivity.MAX_DATE;
import static com.sew.dynamicforms.activities.BaseActivity.MIN_DATE;
import static com.sew.dynamicforms.activities.BaseActivity.SEPARATOR;

public class FormItem implements Serializable {

    private String fieldName, fieldType, defaultValue, currentValue;
    private String format;
    private String minDate, maxDate;

    private boolean isRequired;
    private int minLength, maxLength;
    private ArrayList<ItemValue> itemValuesArrayList;

    private FormItem(String fieldName, String fieldType, String defaultValue,
                     boolean isRequired, int minLength, int maxLength,
                     ArrayList<ItemValue> itemValuesArrayList, String format,
                     String minDate, String maxDate) {
        setFieldName(fieldName);
        setFieldType(fieldType);
        setDefaultValue(defaultValue);
        setRequired(isRequired);
        setMinLength(isRequired ? minLength : 0);
        setMaxLength(maxLength);
        setCurrentValue(defaultValue);
        setItemValuesArrayList(itemValuesArrayList);
        setFormat(format);
        setMin_date(minDate);
        setMax_date(maxDate);
    }

    public ArrayList<Integer> getSelectedLabels() {
        if (currentValue.length() == 0)
            return new ArrayList<>(0);

        String[] selectLabelsString = currentValue.split(BaseActivity.SEPARATOR);
        ArrayList<Integer> selectedLabelsList = new ArrayList<>();

        for (String singleLabel : selectLabelsString) {
            if (singleLabel.trim().length() != 0) {
                selectedLabelsList.add(Integer.parseInt(singleLabel.trim()));
            }
        }
        return selectedLabelsList;
    }

    public void addCheckBoxID(int selectedID) {
        setCurrentValue(currentValue + SEPARATOR + selectedID);
    }

    public void removeCheckBoxID(int selectedID) {
        ArrayList<Integer> selectedLabels = getSelectedLabels();
        selectedLabels.remove((Integer) selectedID);
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < selectedLabels.size(); i++) {
            stringBuilder.append(selectedLabels.get(i)).append(SEPARATOR);
        }
        setCurrentValue(stringBuilder.toString());
    }


    public String getHintTextSelect() {
        StringBuilder stringBuilder = new StringBuilder("Select ");

        stringBuilder.append(getFieldName()).append(" ");

        if (isRequired()) {
            stringBuilder.append("*");
        }
        return stringBuilder.toString();
    }

    public String getHintText() {
        StringBuilder stringBuilder = new StringBuilder("Enter ");

        stringBuilder.append(getFieldName()).append(" ");

        if (isRequired()) {
            stringBuilder.append("*");
        }
        return stringBuilder.toString();
    }


    public static String getGeneralizedErrorText() {
        return "Please make sure that all the below parameters are matched :" +
                "\n1. All the required fields are filled." +
                "\n2. Input values are well within the limits." +
                "\n3. Format of each input is valid.";
    }

    public String getErrorText() {
        return Validation.validateSingleInputData(this.currentValue.trim(), this);
    }

    public static ArrayList<Integer> getAllFaultyPositions(ArrayList<FormItem> formItemArrayList) {
        return Validation.getAllFaultyPositions(formItemArrayList);
    }

    public static FormItem getItemFromJSON(JSONObject singleItem) {
        String fieldName = singleItem.optString("field_name");
        String fieldType = singleItem.optString("field_type");
        String defaultValue = singleItem.optString("default");
        boolean isRequired = singleItem.optString("required").equals("true");

        String format = singleItem.optString("format");
        if (format.trim().length() == 0) {
            format = DEFAULT_DATE_FORMAT;
        }

        int minLength = 0;
        int maxLength = 1000;
        String min_date = MIN_DATE, max_date = MAX_DATE;

        JSONObject validations = singleItem.optJSONObject("validations");
        if (validations != null) {
            if (validations.has("min_length")) {
                minLength = Integer.parseInt(validations.optString("min_length"));
            }

            if (validations.has("max_length")) {
                maxLength = Integer.parseInt(validations.optString("max_length"));
            }

            if (validations.has("min_date")) {
                min_date = validations.optString("min_date");
            }

            if (validations.has("max_date")) {
                max_date = validations.optString("max_date");
            }
        }

        JSONArray values = singleItem.optJSONArray("values");
        ArrayList<ItemValue> itemValuesArrayList;
        if (values != null) {
            itemValuesArrayList = new ArrayList<>(values.length());

            for (int i = 0; i < values.length(); i++) {
                itemValuesArrayList.add(ItemValue.getItemValueFromJSON(values.optJSONObject(i)));
            }

        } else {
            itemValuesArrayList = new ArrayList<>(0);
        }

        return new FormItem(fieldName, fieldType, defaultValue, isRequired, minLength, maxLength, itemValuesArrayList, format, min_date, max_date);
    }

    public String getMinDate() {
        return minDate;
    }

    private void setMin_date(String min_date) {
        this.minDate = min_date;
    }

    public String getMaxDate() {
        return maxDate;
    }

    private void setMax_date(String max_date) {
        this.maxDate = max_date;
    }

    public ArrayList<ItemValue> getItemValuesArrayList() {
        return itemValuesArrayList;
    }

    private void setItemValuesArrayList(ArrayList<ItemValue> itemValuesArrayList) {
        this.itemValuesArrayList = itemValuesArrayList;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getFieldName() {
        return fieldName;
    }

    private void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldType() {
        return fieldType;
    }

    private void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    private void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(String currentValue) {
        this.currentValue = currentValue;
    }

    public boolean isRequired() {
        return isRequired;
    }

    private void setRequired(boolean required) {
        isRequired = required;
    }

    public int getMinLength() {
        return minLength;
    }

    private void setMinLength(int minLength) {
        this.minLength = minLength;
    }

    public int getMaxLength() {
        return maxLength;
    }

    private void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }
}
