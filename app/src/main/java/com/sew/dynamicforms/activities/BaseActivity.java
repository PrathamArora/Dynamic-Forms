package com.sew.dynamicforms.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class BaseActivity extends AppCompatActivity {
    public static String ITEMS_ASSET_DIR = "dynamic_forms.json";
    public static final String FORMITEMSKEY = "FormItems";
    public static String textRegex = "^[a-zA-Z ]*$";
    public static String numberRegex = "[0-9]+";

    public static String DEFAULT_DATE_FORMAT = "dd/MM/yyyy";

    public static String MIN_DATE = "1/1/1970";
    public static String MAX_DATE = "1/1/2050";

    public static String SEPARATOR = ",";

    public static String TEXT = "text", EMAIL = "email", NUMBER = "number", PHONE = "phone",
            RADIOBUTTON = "radiobutton", CHECKBOX = "checkbox", CALENDER = "date";

    public final static int TEXT_TYPE = 1;
    public final static int EMAIL_TYPE = 2;
    public final static int NUMBER_TYPE = 3;
    public final static int PHONE_TYPE = 4;
    public final static int RADIO_BUTTON_TYPE = 5;
    public final static int CHECKBOX_TYPE = 6;
    public final static int CALENDAR_TYPE = 7;

    public static Date getDate(String strDate, String format) {
        Date prevDate = new Date();
        if (strDate.trim().length() != 0) {
            try {
                prevDate = new SimpleDateFormat(format, Locale.US).parse(strDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return prevDate;
    }


    public void showDialogBox(String errorHeading, String errorText) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(errorHeading)
                .setMessage(errorText)
                .setCancelable(false)
                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });


        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}
