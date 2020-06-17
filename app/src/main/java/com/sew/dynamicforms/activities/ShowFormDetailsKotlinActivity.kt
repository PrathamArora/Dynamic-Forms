package com.sew.dynamicforms.activities

import android.graphics.Color
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.sew.dynamicforms.R
import com.sew.dynamicforms.adapter.FormAdapter
import com.sew.dynamicforms.adapter.FormAdapterKotlin
import com.sew.dynamicforms.model.FormItem
import java.lang.StringBuilder
import java.util.*

class ShowFormDetailsKotlinActivity : BaseActivity() {

    private var formItemArrayList: ArrayList<FormItem>? = null
    private var llParentView: LinearLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_form_details)

        initUI()


        val bundle = intent.extras

        formItemArrayList = bundle!!.getSerializable(FORMITEMSKEY) as ArrayList<FormItem>?
        if (formItemArrayList != null && formItemArrayList!!.size != 0) {
            renderUI()
        }
    }

    private fun renderUI() {
        val textViewParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT)
        textViewParams.setMargins(0, 10, 0, 10)

        formItemArrayList?.forEach {
            val textView = TextView(this)
            textView.textSize = 20.0f
            textView.setTextColor(Color.BLACK)
            textView.text = prepareText(it)
            textView.layoutParams = textViewParams
            llParentView?.addView(textView)
        }
    }

    private fun prepareText(formItem : FormItem) : String {
        val stringBuilder = StringBuilder(formItem.fieldName).append(" : ")
        val currentValue = formItem.currentValue

        when(FormAdapter.getCurrentItemViewType(formItem.fieldType)){
            RADIO_BUTTON_TYPE -> {
                val itemValueArrayList = formItem.itemValuesArrayList

                itemValueArrayList.forEach {
                    if(currentValue == it._id.toString()){
                        stringBuilder.append(it.label)
                    }
                }
            }
            CHECKBOX_TYPE -> {
                val selectedLabels = formItem.selectedLabels
                val itemValueArrayList = formItem.itemValuesArrayList
                var counter = 1

                itemValueArrayList.forEach {
                    if(selectedLabels.contains(it._id)){
                        stringBuilder.append("\n\t\t").append(". ").append(it.label)
                        counter++
                    }
                }
            }
            else -> {
                stringBuilder.append(currentValue)
            }
        }

        return stringBuilder.toString()
    }

    private fun initUI() {
        llParentView = findViewById(R.id.llParentView)
        findViewById<ImageView>(R.id.imgBackButton).setOnClickListener { finish() }
    }
}
