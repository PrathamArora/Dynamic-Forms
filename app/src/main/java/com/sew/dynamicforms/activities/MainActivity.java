package com.sew.dynamicforms.activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sew.dynamicforms.R;
import com.sew.dynamicforms.adapter.FormAdapter;
import com.sew.dynamicforms.model.FormItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends BaseActivity {

    private ArrayList<FormItem> formItemArrayList;
    private ProgressDialog progressDialog;
    private RecyclerView rvFormItems;
    private FormAdapter formAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preProcessJSONAsset();

    }

    @SuppressLint("StaticFieldLeak")
    private void preProcessJSONAsset() {
        new AsyncTask<Void, Void, ArrayList<FormItem>>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setTitle(getString(R.string.loading_form_items));
                progressDialog.setMessage(getString(R.string.please_wait));
                progressDialog.setCancelable(false);
                progressDialog.show();
            }

            @Override
            protected ArrayList<FormItem> doInBackground(Void... voids) {
                String json;
                ArrayList<FormItem> formItemArrayList;
                try {
                    InputStream is = getAssets().open(ITEMS_ASSET_DIR);
                    int size = is.available();
                    byte[] buffer = new byte[size];
                    is.read(buffer);
                    is.close();
                    json = new String(buffer, StandardCharsets.UTF_8);
                    JSONObject allItemsJSON = new JSONObject(json);
                    JSONArray allItems = allItemsJSON.getJSONArray("inputs");
                    formItemArrayList = new ArrayList<>(allItems.length());

                    for (int i = 0; i < allItems.length(); i++) {
                        JSONObject singleItem = allItems.getJSONObject(i);
                        formItemArrayList.add(FormItem.getItemFromJSON(singleItem));
                    }

                } catch (Exception e) {
                    formItemArrayList = new ArrayList<>();
                }

                return formItemArrayList;
            }

            @Override
            protected void onPostExecute(ArrayList<FormItem> formItemArrayListProcessed) {
                super.onPostExecute(formItemArrayListProcessed);

                formItemArrayList = new ArrayList<>(formItemArrayListProcessed);
                progressDialog.dismiss();
                renderUI();
            }
        }.execute();

    }

    public void initUI() {
        rvFormItems = findViewById(R.id.rvFormItems);
        findViewById(R.id.btnNext).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAllFields();
            }
        });
    }

    private void checkAllFields() {
        ArrayList<Integer> faultyPositions = FormItem.getAllFaultyPositions(formItemArrayList);
        String errorText;
        String errorHeading;

        if (faultyPositions.size() == 0) {
            Intent intent = new Intent(MainActivity.this, ShowFormDetailsActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(FORMITEMSKEY, formItemArrayList);
            intent.putExtras(bundle);
            startActivity(intent);
            return;
        } else if (faultyPositions.size() == 1) {
            errorHeading = "Error Occurred in : " + formItemArrayList.get(faultyPositions.get(0)).getFieldName();
            errorText = formItemArrayList.get(faultyPositions.get(0)).getErrorText();
            int position = faultyPositions.get(0) - 2 >= 0 ? faultyPositions.get(0) - 2 : faultyPositions.get(0);
            Objects.requireNonNull(rvFormItems.getLayoutManager()).scrollToPosition(position);
        } else {
            errorHeading = "Error Occurred";
            errorText = FormItem.getGeneralizedErrorText();
        }

        showDialogBox(errorHeading, errorText);
    }

    public void renderUI() {
        initUI();

        formAdapter = new FormAdapter(this, formItemArrayList);
        LinearLayoutManager recyclerLayoutManager = new LinearLayoutManager(this);
        rvFormItems.setLayoutManager(recyclerLayoutManager);
        rvFormItems.setAdapter(formAdapter);
    }

}


/*


{
  "inputs": [
    {
      "id": "1",
      "field_name": "Name",
      "field_type": "text",
      "required": "true",
      "default": "",
      "validations": {
        "min_length": "3",
        "max_length": "50"
      }
    },
    {
      "id": "2",
      "field_name": "Email",
      "field_type": "email",
      "required": "true",
      "default": "",
      "validations": {
      }
    },
    {
      "id": "3",
      "field_name": "Zip Code",
      "field_type": "number",
      "required": "false",
      "default": "60606",
      "validations": {
        "min_length": "6",
        "max_length": "6"
      }
    },
    {
      "id": "4",
      "field_name": "phone",
      "field_type": "phone",
      "required": "true",
      "default": "",
      "validations": {
        "min_length": "10",
        "max_length": "10"
      }
    },
    {
      "id": "1",
      "field_name": "Name",
      "field_type": "text",
      "required": "true",
      "default": "",
      "validations": {
        "min_length": "3",
        "max_length": "50"
      }
    },
    {
      "id": "2",
      "field_name": "Email",
      "field_type": "email",
      "required": "true",
      "default": "",
      "validations": {
      }
    },
    {
      "id": "3",
      "field_name": "Zip Code",
      "field_type": "number",
      "required": "false",
      "default": "60606",
      "validations": {
        "min_length": "6",
        "max_length": "6"
      }
    },
    {
      "id": "4",
      "field_name": "phone",
      "field_type": "phone",
      "required": "true",
      "default": "",
      "validations": {
        "min_length": "10",
        "max_length": "10"
      }
    },
    {
      "id": "1",
      "field_name": "Name",
      "field_type": "text",
      "required": "true",
      "default": "",
      "validations": {
        "min_length": "3",
        "max_length": "50"
      }
    },
    {
      "id": "2",
      "field_name": "Email",
      "field_type": "email",
      "required": "true",
      "default": "",
      "validations": {
      }
    },
    {
      "id": "3",
      "field_name": "Zip Code",
      "field_type": "number",
      "required": "false",
      "default": "60606",
      "validations": {
        "min_length": "6",
        "max_length": "6"
      }
    },
    {
      "id": "4",
      "field_name": "phone",
      "field_type": "phone",
      "required": "true",
      "default": "",
      "validations": {
        "min_length": "10",
        "max_length": "10"
      }
    },
    {
      "id": "1",
      "field_name": "Name",
      "field_type": "text",
      "required": "true",
      "default": "",
      "validations": {
        "min_length": "3",
        "max_length": "50"
      }
    },
    {
      "id": "2",
      "field_name": "Email",
      "field_type": "email",
      "required": "true",
      "default": "",
      "validations": {
      }
    },
    {
      "id": "3",
      "field_name": "Zip Code",
      "field_type": "number",
      "required": "false",
      "default": "60606",
      "validations": {
        "min_length": "6",
        "max_length": "6"
      }
    },
    {
      "id": "4",
      "field_name": "phone",
      "field_type": "phone",
      "required": "true",
      "default": "",
      "validations": {
        "min_length": "10",
        "max_length": "10"
      }
    },
    {
      "id": "1",
      "field_name": "Name",
      "field_type": "text",
      "required": "true",
      "default": "",
      "validations": {
        "min_length": "3",
        "max_length": "50"
      }
    },
    {
      "id": "2",
      "field_name": "Email",
      "field_type": "email",
      "required": "true",
      "default": "",
      "validations": {
      }
    },
    {
      "id": "3",
      "field_name": "Zip Code",
      "field_type": "number",
      "required": "false",
      "default": "60606",
      "validations": {
        "min_length": "6",
        "max_length": "6"
      }
    },
    {
      "id": "4",
      "field_name": "phone",
      "field_type": "phone",
      "required": "true",
      "default": "",
      "validations": {
        "min_length": "10",
        "max_length": "10"
      }
    },
    {
      "id": "1",
      "field_name": "Name",
      "field_type": "text",
      "required": "true",
      "default": "",
      "validations": {
        "min_length": "3",
        "max_length": "50"
      }
    },
    {
      "id": "2",
      "field_name": "Email",
      "field_type": "email",
      "required": "true",
      "default": "",
      "validations": {
      }
    },
    {
      "id": "3",
      "field_name": "Zip Code",
      "field_type": "number",
      "required": "false",
      "default": "60606",
      "validations": {
        "min_length": "6",
        "max_length": "6"
      }
    },
    {
      "id": "4",
      "field_name": "phone",
      "field_type": "phone",
      "required": "true",
      "default": "",
      "validations": {
        "min_length": "10",
        "max_length": "10"
      }
    },
    {
      "id": "1",
      "field_name": "Name",
      "field_type": "text",
      "required": "true",
      "default": "",
      "validations": {
        "min_length": "3",
        "max_length": "50"
      }
    },
    {
      "id": "2",
      "field_name": "Email",
      "field_type": "email",
      "required": "true",
      "default": "",
      "validations": {
      }
    },
    {
      "id": "3",
      "field_name": "Zip Code",
      "field_type": "number",
      "required": "false",
      "default": "60606",
      "validations": {
        "min_length": "6",
        "max_length": "6"
      }
    },
    {
      "id": "4",
      "field_name": "phone",
      "field_type": "phone",
      "required": "true",
      "default": "",
      "validations": {
        "min_length": "10",
        "max_length": "10"
      }
    },
    {
      "id": "1",
      "field_name": "Name",
      "field_type": "text",
      "required": "true",
      "default": "",
      "validations": {
        "min_length": "3",
        "max_length": "50"
      }
    },
    {
      "id": "2",
      "field_name": "Email",
      "field_type": "email",
      "required": "true",
      "default": "",
      "validations": {
      }
    },
    {
      "id": "3",
      "field_name": "Zip Code",
      "field_type": "number",
      "required": "false",
      "default": "60606",
      "validations": {
        "min_length": "6",
        "max_length": "6"
      }
    },
    {
      "id": "4",
      "field_name": "phone",
      "field_type": "phone",
      "required": "true",
      "default": "",
      "validations": {
        "min_length": "10",
        "max_length": "10"
      }
    },
    {
      "id": "1",
      "field_name": "Name",
      "field_type": "text",
      "required": "true",
      "default": "",
      "validations": {
        "min_length": "3",
        "max_length": "50"
      }
    },
    {
      "id": "2",
      "field_name": "Email",
      "field_type": "email",
      "required": "true",
      "default": "",
      "validations": {
      }
    },
    {
      "id": "3",
      "field_name": "Zip Code",
      "field_type": "number",
      "required": "false",
      "default": "60606",
      "validations": {
        "min_length": "6",
        "max_length": "6"
      }
    },
    {
      "id": "4",
      "field_name": "phone",
      "field_type": "phone",
      "required": "true",
      "default": "",
      "validations": {
        "min_length": "10",
        "max_length": "10"
      }
    },
    {
      "id": "1",
      "field_name": "Name",
      "field_type": "text",
      "required": "true",
      "default": "",
      "validations": {
        "min_length": "3",
        "max_length": "50"
      }
    },
    {
      "id": "2",
      "field_name": "Email",
      "field_type": "email",
      "required": "true",
      "default": "",
      "validations": {
      }
    },
    {
      "id": "3",
      "field_name": "Zip Code",
      "field_type": "number",
      "required": "false",
      "default": "60606",
      "validations": {
        "min_length": "6",
        "max_length": "6"
      }
    },
    {
      "id": "4",
      "field_name": "phone",
      "field_type": "phone",
      "required": "true",
      "default": "",
      "validations": {
        "min_length": "10",
        "max_length": "10"
      }
    },
    {
      "id": "1",
      "field_name": "Name",
      "field_type": "text",
      "required": "true",
      "default": "",
      "validations": {
        "min_length": "3",
        "max_length": "50"
      }
    },
    {
      "id": "2",
      "field_name": "Email",
      "field_type": "email",
      "required": "true",
      "default": "",
      "validations": {
      }
    },
    {
      "id": "3",
      "field_name": "Zip Code",
      "field_type": "number",
      "required": "false",
      "default": "60606",
      "validations": {
        "min_length": "6",
        "max_length": "6"
      }
    },
    {
      "id": "4",
      "field_name": "phone",
      "field_type": "phone",
      "required": "true",
      "default": "",
      "validations": {
        "min_length": "10",
        "max_length": "10"
      }
    }
  ]
}



 */