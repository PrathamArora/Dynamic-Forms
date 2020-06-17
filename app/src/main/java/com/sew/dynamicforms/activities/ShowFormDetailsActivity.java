package com.sew.dynamicforms.activities;


import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sew.dynamicforms.R;
import com.sew.dynamicforms.adapter.FormAdapter;
import com.sew.dynamicforms.model.FormItem;
import com.sew.dynamicforms.model.ItemValue;

import java.util.ArrayList;
import java.util.List;

public class ShowFormDetailsActivity extends BaseActivity {

    private List<FormItem> formItemArrayList;
    private LinearLayout llParentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_form_details);

        initUI();

        Bundle bundle = getIntent().getExtras();
        formItemArrayList = (ArrayList<FormItem>) bundle.getSerializable(FORMITEMSKEY);
        if (formItemArrayList != null && formItemArrayList.size() != 0)
            renderUI();

    }

    @SuppressLint("SetTextI18n")
    private void renderUI() {

        LinearLayout.LayoutParams textViewParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        textViewParams.setMargins(0, 10, 0, 10);

        for (FormItem formItem : formItemArrayList) {

            TextView textView = new TextView(this);
            textView.setTextSize(20.0f);
            textView.setTextColor(Color.BLACK);
            textView.setText(prepareText(formItem));
            textView.setLayoutParams(textViewParams);
            llParentView.addView(textView);
        }
    }

    private String prepareText(FormItem formItem) {
        StringBuilder stringBuilder = new StringBuilder(formItem.getFieldName()).append(" : ");
        String currentValue = formItem.getCurrentValue();

        if (FormAdapter.getCurrentItemViewType(formItem.getFieldType()) == RADIO_BUTTON_TYPE) {
            ArrayList<ItemValue> itemValueArrayList = formItem.getItemValuesArrayList();

            for (ItemValue itemValue : itemValueArrayList) {
                if (currentValue.equals(String.valueOf(itemValue.get_id()))) {
                    stringBuilder.append(itemValue.getLabel());
                    break;
                }
            }
        } else if (FormAdapter.getCurrentItemViewType(formItem.getFieldType()) == CHECKBOX_TYPE) {
            ArrayList<Integer> selectedLabels = formItem.getSelectedLabels();
            ArrayList<ItemValue> itemValueArrayList = formItem.getItemValuesArrayList();

            int counter = 1;

            for (ItemValue itemValue : itemValueArrayList) {
                if (selectedLabels.contains(itemValue.get_id())) {
                    stringBuilder.append("\n\t\t").append(counter).append(". ").append(itemValue.getLabel());
                    counter++;
                }
            }
        } else {
            stringBuilder.append(currentValue);
        }

        return stringBuilder.toString();
    }


    private void initUI() {
        llParentView = findViewById(R.id.llParentView);
        (findViewById(R.id.imgBackButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


}
