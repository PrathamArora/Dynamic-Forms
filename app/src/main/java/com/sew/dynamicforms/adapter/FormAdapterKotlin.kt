package com.sew.dynamicforms.adapter

import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sew.dynamicforms.activities.BaseActivity
import com.sew.dynamicforms.model.FormItemKotlin

class FormAdapterKotlin{

}

//class FormAdapterKotlin constructor(private var context: Context?, private var formItemArrayList: ArrayList<FormItemKotlin>?)
//    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
//
//    companion object {
//        private var viewTypeMap: HashMap<String, Int>? = null
//        fun getCurrentItemViewType(fieldType: String): Int {
//            return ((if (viewTypeMap != null && viewTypeMap!!.containsKey(fieldType)) viewTypeMap!![fieldType] else BaseActivity.TEXT_TYPE) as Int)
//        }
//    }
//
//    init {
//        viewTypeMap!![BaseActivity.TEXT] = BaseActivity.TEXT_TYPE
//        viewTypeMap!![BaseActivity.EMAIL] = BaseActivity.EMAIL_TYPE
//        viewTypeMap!![BaseActivity.NUMBER] = BaseActivity.NUMBER_TYPE
//        viewTypeMap!![BaseActivity.PHONE] = BaseActivity.PHONE_TYPE
//        viewTypeMap!![BaseActivity.RADIOBUTTON] = BaseActivity.RADIO_BUTTON_TYPE
//        viewTypeMap!![BaseActivity.CHECKBOX] = BaseActivity.CHECKBOX_TYPE
//        viewTypeMap!![BaseActivity.CALENDER] = BaseActivity.CALENDAR_TYPE
//    }
//
//    override fun getItemViewType(position: Int): Int {
//        return (if (viewTypeMap != null && viewTypeMap!!.containsKey(formItemArrayList!![position].fieldType))
//            viewTypeMap!![formItemArrayList!![position].fieldType]
//        else BaseActivity.TEXT_TYPE)!!
//    }
//
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//        var view: View
//
//        when (viewType) {
//            BaseActivity.EMAIL_TYPE -> {
//
//            }
//            BaseActivity.NUMBER_TYPE -> {
//
//            }
//            BaseActivity.PHONE_TYPE -> {
//
//            }
//            BaseActivity.RADIO_BUTTON_TYPE -> {
//
//            }
//            BaseActivity.CHECKBOX_TYPE -> {
//
//            }
//            BaseActivity.CALENDAR_TYPE -> {
//
//            }
//            else -> {
////                BaseActivity.TEXT_TYPE
//
//            }
//
//        }
//    }
//
//    override fun getItemCount(): Int {
//        TODO("Not yet implemented")
//    }
//
//    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//        TODO("Not yet implemented")
//    }
//}