package com.sew.dynamicforms.helper;

import android.util.Patterns;

import com.sew.dynamicforms.adapter.FormAdapter;
import com.sew.dynamicforms.model.FormItem;
import com.sew.dynamicforms.model.FormItemKotlin;

import java.util.ArrayList;
import java.util.regex.Pattern;

import static com.sew.dynamicforms.activities.BaseActivity.CHECKBOX_TYPE;
import static com.sew.dynamicforms.activities.BaseActivity.EMAIL_TYPE;
import static com.sew.dynamicforms.activities.BaseActivity.NUMBER_TYPE;
import static com.sew.dynamicforms.activities.BaseActivity.PHONE_TYPE;
import static com.sew.dynamicforms.activities.BaseActivity.TEXT_TYPE;
import static com.sew.dynamicforms.activities.BaseActivity.numberRegex;
import static com.sew.dynamicforms.activities.BaseActivity.textRegex;

public class Validation {

    public static String validateSingleInputData(final String inputValue, final FormItemKotlin formItem) {
        String errorMessage = "";

        if (inputValue.length() > 0 && FormAdapter.getCurrentItemViewType(formItem.getFieldType()) == TEXT_TYPE
                && !matchTextRegex(inputValue)) {

            errorMessage = "Numbers and special characters are not allowed";

        } else if (inputValue.length() > 0 && FormAdapter.getCurrentItemViewType(formItem.getFieldType()) == EMAIL_TYPE
                && !matchEmailRegex(inputValue)) {

            errorMessage = "Invalid E-Mail Format";

        } else if (inputValue.length() > 0 && FormAdapter.getCurrentItemViewType(formItem.getFieldType()) == NUMBER_TYPE
                && !matchNumberRegex(inputValue)) {

            errorMessage = "Only numbers are allowed";

        } else if (inputValue.length() > 0 && FormAdapter.getCurrentItemViewType(formItem.getFieldType()) == PHONE_TYPE
                && !matchPhoneRegex(inputValue)) {

            errorMessage = "Only numbers are allowed";

        } else if (formItem.isRequired() && FormAdapter.getCurrentItemViewType(formItem.getFieldType()) == CHECKBOX_TYPE
                && formItem.getSelectedLabels().size() == 0) {

            errorMessage = "Select at least one the option";

        } else if ((formItem.isRequired() && inputValue.length() == 0)
                || inputValue.length() < formItem.getMinLength()
                || inputValue.length() > formItem.getMaxLength()) {

            if (formItem.getMinLength() == formItem.getMaxLength()) {
                errorMessage = "Length should be " + formItem.getMinLength();
            } else {
                errorMessage = "Length should be between " + formItem.getMinLength() + " & " + formItem.getMaxLength();
            }
        }

        return errorMessage.trim().length() == 0 ? null : errorMessage.trim();
    }


    public static String validateSingleInputData(final String inputValue, final FormItem formItem) {
        String errorMessage = "";

        if (inputValue.length() > 0 && FormAdapter.getCurrentItemViewType(formItem.getFieldType()) == TEXT_TYPE
                && !matchTextRegex(inputValue)) {

            errorMessage = "Numbers and special characters are not allowed";

        } else if (inputValue.length() > 0 && FormAdapter.getCurrentItemViewType(formItem.getFieldType()) == EMAIL_TYPE
                && !matchEmailRegex(inputValue)) {

            errorMessage = "Invalid E-Mail Format";

        } else if (inputValue.length() > 0 && FormAdapter.getCurrentItemViewType(formItem.getFieldType()) == NUMBER_TYPE
                && !matchNumberRegex(inputValue)) {

            errorMessage = "Only numbers are allowed";

        } else if (inputValue.length() > 0 && FormAdapter.getCurrentItemViewType(formItem.getFieldType()) == PHONE_TYPE
                && !matchPhoneRegex(inputValue)) {

            errorMessage = "Only numbers are allowed";

        } else if (formItem.isRequired() && FormAdapter.getCurrentItemViewType(formItem.getFieldType()) == CHECKBOX_TYPE
                && formItem.getSelectedLabels().size() == 0) {

            errorMessage = "Select at least one the option";

        } else if ((formItem.isRequired() && inputValue.length() == 0)
                || inputValue.length() < formItem.getMinLength()
                || inputValue.length() > formItem.getMaxLength()) {

            if (formItem.getMinLength() == formItem.getMaxLength()) {
                errorMessage = "Length should be " + formItem.getMinLength();
            } else {
                errorMessage = "Length should be between " + formItem.getMinLength() + " & " + formItem.getMaxLength();
            }
        }

        return errorMessage.trim().length() == 0 ? null : errorMessage.trim();
    }

    private static boolean matchPhoneRegex(String input) {
        return matchNumberRegex(input);
//        return Patterns.PHONE.matcher(input).matches();
    }

    private static bolean matchNumberRegex(String input) {
        Pattern pattern = Pattern.compile(numberRegex);
        return pattern.matcher(input).matches();
    }

    private static boolean matchTextRegex(String input) {
        Pattern pattern = Pattern.compile(textRegex);
        return pattern.matcher(input).matches();
    }

    private static boolean matchEmailRegex(String input) {
        return Patterns.EMAIL_ADDRESS.matcher(input).matches();
    }

    public static ArrayList<Integer> getAllFaultyPositions(ArrayList<FormItem> formItemArrayList) {
        ArrayList<Integer> faultyPositions = new ArrayList<>(formItemArrayList.size());

        for (int i = 0; i < formItemArrayList.size(); i++) {

            if (faultyPositions.size() >= 2) {
                break;
            }

            FormItem formItem = formItemArrayList.get(i);
            String currentValue = formItem.getCurrentValue().trim();

            if (validateSingleInputData(currentValue, formItem) != null) {
                faultyPositions.add(i);
            }
        }

        return faultyPositions;
    }

}
