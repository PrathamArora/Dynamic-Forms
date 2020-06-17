package com.sew.dynamicforms.model

import com.sew.dynamicforms.activities.BaseActivity
import com.sew.dynamicforms.helper.Validation
import org.json.JSONObject
import java.io.Serializable

class FormItemKotlin : Serializable {
    var fieldName: String
    var fieldType: String
    var defaultValue: String
    var currentValue: String
    private var format: String
    private var minDate: String
    var maxDate: String

    var isRequired: Boolean = false
    var minLength: Int = 0
    var maxLength: Int = 1000

    var itemValuesArrayList: ArrayList<ItemValue>

    constructor(fieldName: String, fieldType: String, defaultValue: String,
                format: String, minDate: String, maxDate: String, isRequired: Boolean,
                minLength: Int, maxLength: Int, itemValuesArrayList: ArrayList<ItemValue>) {

        this.fieldName = fieldName
        this.fieldType = fieldType
        this.defaultValue = defaultValue
        this.currentValue = defaultValue
        this.format = format
        this.minDate = minDate
        this.maxDate = maxDate
        this.isRequired = isRequired
        this.minLength = minLength
        this.maxLength = maxLength
        this.itemValuesArrayList = itemValuesArrayList

    }

    fun getSelectedLabels(): ArrayList<Int> {
        if (currentValue.isEmpty()) {
            return ArrayList(0)
        }

        val selectLabelsString = currentValue.split(BaseActivity.SEPARATOR).toTypedArray()
        val selectedLabelsList = ArrayList<Int>()

        for (singleLabel in selectLabelsString) {
            if (singleLabel.trim().isNotEmpty()) {
                selectedLabelsList.add(singleLabel.trim().toInt())
            }
        }
        return selectedLabelsList
    }

    fun addCheckBoxID(selectedID: Int) {
        currentValue = currentValue + BaseActivity.SEPARATOR + selectedID
    }

    fun removeCheckBoxID(selectedID: Int) {
        val selectedLabels: ArrayList<Int> = getSelectedLabels()
        selectedLabels.remove(selectedID)

        val stringBuilder = StringBuilder()

        for (i in 0 until selectedLabels.size) {
            stringBuilder.append(selectedLabels[i])
        }
        currentValue = stringBuilder.toString()
    }

    fun getHintTextSelect(): String? {
        val stringBuilder = StringBuilder("Select ")
        stringBuilder.append(fieldName).append(" ")
        if (isRequired) {
            stringBuilder.append("*")
        }
        return stringBuilder.toString()
    }

    fun getErrorText(): String {
        return Validation.validateSingleInputData(currentValue.trim(), this@FormItemKotlin)
    }

    companion object {
        fun getAllFaultyPositions(formItemArrayList: ArrayList<FormItem>): ArrayList<Int> {
            return Validation.getAllFaultyPositions(formItemArrayList)
        }

        fun getGeneralizedErrorText(): String {
            return "Please make sure that all the below parameters are matched :" +
                    "\n1. All the required fields are filled." +
                    "\n2. Input values are well within the limits." +
                    "\n3. Format of each input is valid.";
        }

        fun getItemFromJSON(singleItem: JSONObject): FormItemKotlin {
            val fieldName = singleItem.optString("field_name")
            val fieldType = singleItem.optString("field_type")
            val defaultValue = singleItem.optString("default")
            val isRequired = singleItem.optString("required") == "true"
            var format = singleItem.optString("format")

            if (format.trim().isEmpty())
                format = BaseActivity.DEFAULT_DATE_FORMAT

            var minLength = 0
            var maxLength = 0
            var min_date = BaseActivity.MIN_DATE
            var max_date = BaseActivity.MAX_DATE

            val validations = singleItem.optJSONObject("validations")

            if (validations != null) {
                if (validations.has("min_length")) {
                    minLength = validations.optString("min_length").toInt()
                }

                if (validations.has("max_length")) {
                    maxLength = validations.optString("min_length").toInt()
                }

                if (validations.has("min_date")) {
                    min_date = validations.optString("min_date")
                }
                if (validations.has("max_date")) {
                    max_date = validations.optString("max_date")
                }
            }

            val values = singleItem.optJSONArray("values")
            val itemValuesArrayList: ArrayList<ItemValue>
            if (values != null) {
                itemValuesArrayList = ArrayList(values.length())
                for (i in 0 until values.length()) {
                    itemValuesArrayList.add(ItemValue.getItemValueFromJSON(values.getJSONObject(i)))
                }
            } else {
                itemValuesArrayList = ArrayList(0);
            }

            return FormItemKotlin(fieldName, fieldType, defaultValue, format, min_date, max_date,
                    isRequired, minLength, maxLength, itemValuesArrayList)
        }
    }

}