package com.sew.dynamicforms.adapter;

import android.app.DatePickerDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.sew.dynamicforms.R;
import com.sew.dynamicforms.activities.BaseActivity;
import com.sew.dynamicforms.model.FormItem;
import com.sew.dynamicforms.model.ItemValue;
import com.sew.dynamicforms.textwatchers.TextInputEditTextWatcher;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

import static com.sew.dynamicforms.activities.BaseActivity.CALENDAR_TYPE;
import static com.sew.dynamicforms.activities.BaseActivity.CALENDER;
import static com.sew.dynamicforms.activities.BaseActivity.CHECKBOX;
import static com.sew.dynamicforms.activities.BaseActivity.CHECKBOX_TYPE;
import static com.sew.dynamicforms.activities.BaseActivity.DEFAULT_DATE_FORMAT;
import static com.sew.dynamicforms.activities.BaseActivity.EMAIL;
import static com.sew.dynamicforms.activities.BaseActivity.EMAIL_TYPE;
import static com.sew.dynamicforms.activities.BaseActivity.NUMBER;
import static com.sew.dynamicforms.activities.BaseActivity.NUMBER_TYPE;
import static com.sew.dynamicforms.activities.BaseActivity.PHONE;
import static com.sew.dynamicforms.activities.BaseActivity.PHONE_TYPE;
import static com.sew.dynamicforms.activities.BaseActivity.RADIOBUTTON;
import static com.sew.dynamicforms.activities.BaseActivity.RADIO_BUTTON_TYPE;
import static com.sew.dynamicforms.activities.BaseActivity.TEXT;
import static com.sew.dynamicforms.activities.BaseActivity.TEXT_TYPE;

public class FormAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private ArrayList<FormItem> formItemArrayList;
    private static HashMap<String, Integer> viewTypeMap;


    public FormAdapter(Context context, ArrayList<FormItem> formItemArrayList) {
        this.context = context;
        this.formItemArrayList = formItemArrayList;
        viewTypeMap = new HashMap<>();
        viewTypeMap.put(TEXT, TEXT_TYPE);
        viewTypeMap.put(EMAIL, EMAIL_TYPE);
        viewTypeMap.put(NUMBER, NUMBER_TYPE);
        viewTypeMap.put(PHONE, PHONE_TYPE);
        viewTypeMap.put(RADIOBUTTON, RADIO_BUTTON_TYPE);
        viewTypeMap.put(CHECKBOX, CHECKBOX_TYPE);
        viewTypeMap.put(CALENDER, CALENDAR_TYPE);
    }

    public static int getCurrentItemViewType(String fieldType) {
        return viewTypeMap != null && viewTypeMap.containsKey(fieldType) ? viewTypeMap.get(fieldType) : TEXT_TYPE;
    }

    @Override
    public int getItemViewType(int position) {
        return viewTypeMap != null && viewTypeMap.containsKey(formItemArrayList.get(position).getFieldType()) ?
                viewTypeMap.get(formItemArrayList.get(position).getFieldType()) : TEXT_TYPE;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        switch (viewType) {

            case TEXT_TYPE:
            default:
                view = LayoutInflater.from(context).inflate(R.layout.text_type_layout, parent, false);
                return new InputTypeHolder(view);

            case EMAIL_TYPE:
                view = LayoutInflater.from(context).inflate(R.layout.email_type_layout, parent, false);
                return new InputTypeHolder(view);

            case NUMBER_TYPE:
                view = LayoutInflater.from(context).inflate(R.layout.number_type_layout, parent, false);
                return new InputTypeHolder(view);

            case PHONE_TYPE:
                view = LayoutInflater.from(context).inflate(R.layout.phone_type_layout, parent, false);
                return new InputTypeHolder(view);

            case RADIO_BUTTON_TYPE:
                view = LayoutInflater.from(context).inflate(R.layout.radiobutton_type_layout, parent, false);
                return new RadioButtonHolder(view, context);

            case CHECKBOX_TYPE:
                view = LayoutInflater.from(context).inflate(R.layout.checkbox_type_layout, parent, false);
                return new CheckBoxHolder(view, context);

            case CALENDAR_TYPE:
                view = LayoutInflater.from(context).inflate(R.layout.calendar_type_layout, parent, false);
                return new CalendarTypeHolder(view, context);
        }

    }


    //-------------------------Edit Text------------------------------------------------------------
    private void resetEditText(TextInputEditText tieInput) {
        TextInputEditTextWatcher prevTextWatcher = (TextInputEditTextWatcher) tieInput.getTag();
        if (prevTextWatcher != null) {
            tieInput.removeTextChangedListener(prevTextWatcher);
        }
        tieInput.setError(null);
    }

    private void initializeEditText(TextInputEditText tieInput, FormItem formItem, RecyclerView.ViewHolder holder) {
        TextInputEditTextWatcher textInputEditTextWatcher = new TextInputEditTextWatcher(tieInput, formItem);

        ((InputTypeHolder) holder).setHolderDetails(formItem);

        tieInput.addTextChangedListener(textInputEditTextWatcher);
        tieInput.setTag(textInputEditTextWatcher);
    }

    private void addWatcherToView(final TextInputEditText tieInput, final FormItem formItem, RecyclerView.ViewHolder holder) {
        resetEditText(tieInput);

        initializeEditText(tieInput, formItem, holder);

        if (formItem.getCurrentValue().length() != 0 || formItem.getDefaultValue().length() != 0) {
            String errorMessage = formItem.getErrorText();
            tieInput.setError(errorMessage);
        }
    }


    //-------------------------Radio Button---------------------------------------------------------
    private void prepareRadioButtons(final RadioButtonHolder holder, final FormItem formItem) {
        resetRadioButtons(holder);
        initializeRadioButtons(holder, formItem);
    }

    private void initializeRadioButtons(RadioButtonHolder holder, FormItem formItem) {
        holder.initializeViews(formItem);
    }

    private void resetRadioButtons(RadioButtonHolder holder) {
        holder.resetAllViews();
    }


    //-------------------------Check Boxes----------------------------------------------------------
    private void prepareCheckBoxes(final CheckBoxHolder holder, final FormItem formItem) {
        resetCheckBoxes(holder);

        initializeCheckBoxes(holder, formItem);
    }

    private void initializeCheckBoxes(CheckBoxHolder holder, FormItem formItem) {
        holder.initializeViews(formItem);
    }

    private void resetCheckBoxes(CheckBoxHolder holder) {
        holder.resetAllViews();
    }

    //-------------------------Calendar Edittext----------------------------------------------------
    private void prepareCalendarEdittext(final CalendarTypeHolder holder, final FormItem formItem) {
        resetCalendarEdittext(holder);

        initializeCalendarEdittext(holder, formItem);
    }

    private void resetCalendarEdittext(CalendarTypeHolder holder) {
        holder.resetView();
    }

    private void initializeCalendarEdittext(CalendarTypeHolder holder, FormItem formItem) {
        holder.initializeViews(formItem);
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int itemViewType = getItemViewType(position);
        if (itemViewType == RADIO_BUTTON_TYPE) {
            prepareRadioButtons((RadioButtonHolder) holder, formItemArrayList.get(position));
        } else if (itemViewType == CHECKBOX_TYPE) {
            prepareCheckBoxes((CheckBoxHolder) holder, formItemArrayList.get(position));
        } else if (itemViewType == CALENDAR_TYPE) {
            prepareCalendarEdittext((CalendarTypeHolder) holder, formItemArrayList.get(position));
        } else {
            addWatcherToView((TextInputEditText) holder.itemView.findViewById(R.id.tieInput),
                    formItemArrayList.get(position), holder);
        }
    }

    @Override
    public int getItemCount() {
        return formItemArrayList.size();
    }

    static class CalendarTypeHolder extends RecyclerView.ViewHolder {
        private TextInputLayout tilLabel;
        private TextInputEditText tieInput;
        private Context context;
        private Calendar calendar;

        void initializeViews(final FormItem formItem) {
            tilLabel.setHint(formItem.getHintTextSelect());
            tieInput.setText(formItem.getCurrentValue());

            final DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                    SimpleDateFormat dateFormat = new SimpleDateFormat(formItem.getFormat(), Locale.US);
                    formItem.setCurrentValue(dateFormat.format(calendar.getTime()));
                    tieInput.setText(formItem.getCurrentValue());
                }
            };

            tieInput.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String format = formItem.getFormat();
                    if (format.trim().length() == 0) {
                        format = DEFAULT_DATE_FORMAT;
                        formItem.setFormat(format);
                    }
                    String prevDateStr = formItem.getCurrentValue();

                    Date prevDate = BaseActivity.getDate(prevDateStr, format);

                    calendar.setTime(Objects.requireNonNull(prevDate));

                    DatePickerDialog datePickerDialog = new DatePickerDialog(context, datePickerListener,
                            calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH));

                    datePickerDialog.getDatePicker().setMinDate(BaseActivity.getDate(formItem.getMinDate(), format).getTime());
                    datePickerDialog.getDatePicker().setMaxDate(BaseActivity.getDate(formItem.getMaxDate(), format).getTime());

                    datePickerDialog.show();
                }
            });
        }

        void resetView() {
            tieInput.setText("");
        }

        CalendarTypeHolder(@NonNull View itemView, Context context) {
            super(itemView);
            tilLabel = itemView.findViewById(R.id.tilLabel);
            tieInput = itemView.findViewById(R.id.tieInput);
            this.context = context;
            this.calendar = Calendar.getInstance();
        }

    }

    static class CheckBoxHolder extends RecyclerView.ViewHolder {
        private LinearLayout llParentViewCheckBoxes;
        private Context context;
        private TextView tvGroupLabel;

        void resetAllViews() {
            llParentViewCheckBoxes.removeAllViews();
        }

        void initializeViews(final FormItem formItem) {
            tvGroupLabel.setText(formItem.getHintTextSelect());
            final ArrayList<Integer> selectedLabels = formItem.getSelectedLabels();

            ArrayList<ItemValue> itemValueArrayList = formItem.getItemValuesArrayList();

            for (int i = 0; i < itemValueArrayList.size(); i++) {
                ItemValue currentItemValue = itemValueArrayList.get(i);

                CheckBox checkBox = new CheckBox(context);
                checkBox.setText(currentItemValue.getLabel());
                checkBox.setId(currentItemValue.get_id());

                llParentViewCheckBoxes.addView(checkBox);
                if (selectedLabels.contains(currentItemValue.get_id())) {
                    checkBox.setChecked(true);
                }

                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            formItem.addCheckBoxID(buttonView.getId());
                        } else {
                            formItem.removeCheckBoxID(buttonView.getId());
                        }
                    }
                });
            }
        }

        CheckBoxHolder(@NonNull View itemView, Context context) {
            super(itemView);
            llParentViewCheckBoxes = itemView.findViewById(R.id.llParentViewCheckBoxes);
            tvGroupLabel = itemView.findViewById(R.id.tvGroupLabel);
            this.context = context;
        }
    }

    static class RadioButtonHolder extends RecyclerView.ViewHolder {

        private RadioGroup radioGroup;
        private Context context;
        private TextView tvGroupLabel;

        void initializeViews(final FormItem formItem) {
            tvGroupLabel.setText(formItem.getHintTextSelect());

            ArrayList<ItemValue> itemValueArrayList = formItem.getItemValuesArrayList();

            for (int i = 0; i < itemValueArrayList.size(); i++) {

                ItemValue currentItemValue = itemValueArrayList.get(i);

                RadioButton radioButton = new RadioButton(context);
                radioButton.setText(currentItemValue.getLabel());
                radioButton.setId(currentItemValue.get_id());

                if (formItem.getCurrentValue().trim().length() > 0
                        && Integer.parseInt(formItem.getCurrentValue()) == currentItemValue.get_id()) {
                    radioButton.setChecked(true);
                }
                radioGroup.addView(radioButton);
            }

            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    formItem.setCurrentValue(String.valueOf(checkedId));
                }
            });
        }

        void resetAllViews() {
            radioGroup.setOnCheckedChangeListener(null);
            radioGroup.removeAllViews();
        }


        RadioButtonHolder(@NonNull View itemView, Context context) {
            super(itemView);
            radioGroup = itemView.findViewById(R.id.radioGroup);
            tvGroupLabel = itemView.findViewById(R.id.tvGroupLabel);
            this.context = context;
        }
    }

    static class InputTypeHolder extends RecyclerView.ViewHolder {
        private TextInputLayout tilLabel;
        private TextInputEditText tieInput;

        InputTypeHolder(@NonNull View itemView) {
            super(itemView);
            tilLabel = itemView.findViewById(R.id.tilLabel);
            tieInput = itemView.findViewById(R.id.tieInput);
        }

        void setHolderDetails(FormItem formItem) {
            tilLabel.setHint(formItem.getHintText());
            if (formItem.getCurrentValue().length() != 0) {
                tieInput.setText(formItem.getCurrentValue());
            } else {
                tieInput.setText(formItem.getDefaultValue());
            }
        }
    }

}
