package com.sew.dynamicforms.textwatchers;

import android.text.Editable;
import android.text.TextWatcher;

import com.google.android.material.textfield.TextInputEditText;
import com.sew.dynamicforms.helper.Validation;
import com.sew.dynamicforms.model.FormItem;

import java.util.Objects;

public class TextInputEditTextWatcher implements TextWatcher {
    private TextInputEditText textInputEditText;
    private FormItem formItem;

    public TextInputEditTextWatcher(final TextInputEditText textInputEditText, final FormItem formItem) {
        this.textInputEditText = textInputEditText;
        this.formItem = formItem;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        formItem.setCurrentValue(s.toString());
        String errorMessage = formItem.getErrorText();
//        String errorMessage = Validation.validateSingleInputData(Objects.requireNonNull(textInputEditText.getText()).toString().trim(), formItem);

        textInputEditText.setError(errorMessage);

    }
}
