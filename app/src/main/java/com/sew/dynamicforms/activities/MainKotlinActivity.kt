package com.sew.dynamicforms.activities

import android.app.ProgressDialog
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sew.dynamicforms.R
import com.sew.dynamicforms.adapter.FormAdapter
import com.sew.dynamicforms.model.FormItem
import org.json.JSONArray
import org.json.JSONObject
import java.io.InputStream

class MainKotlinActivity : BaseActivity() {

    private lateinit var formItemArrayList: ArrayList<FormItem>
    private lateinit var progressDialog: ProgressDialog
    private lateinit var rvFormItems: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        preProcessJSONAsset()
    }

    private fun preProcessJSONAsset() {
        FetchJSONAssetAsyncTask(this).execute()
    }

    private fun checkAllFields() {
        val faultyPositions: ArrayList<Int> = FormItem.getAllFaultyPositions(formItemArrayList)
        var errorText = ""
        var errorHeading = ""

        when (faultyPositions.size) {
            0 -> {
                val intent = Intent(this@MainKotlinActivity, ShowFormDetailsKotlinActivity::class.java)
                val bundle = Bundle()
                bundle.putSerializable(FORMITEMSKEY, formItemArrayList)
                intent.putExtras(bundle)
                startActivity(intent)
                return
            }
            1 -> {
                errorHeading = "Error Occurred in : " + formItemArrayList[faultyPositions[0]].fieldName
                errorText = formItemArrayList[faultyPositions[0]].errorText
                val position = if (faultyPositions[0] - 2 >= 0) faultyPositions[0] - 2 else 0
                rvFormItems.layoutManager?.scrollToPosition(position)
            }
            else -> {
                errorHeading = "Error Occurred"
                errorText = FormItem.getGeneralizedErrorText()
            }
        }

        showDialogBox(errorHeading, errorText)
    }

    private fun initUI() {
        rvFormItems = findViewById(R.id.rvFormItems)
        findViewById<Button>(R.id.btnNext).setOnClickListener {
            checkAllFields()
        }
    }

    fun renderUI() {
        initUI()
        rvFormItems.layoutManager = LinearLayoutManager(this)
        rvFormItems.adapter = FormAdapter(this, formItemArrayList)
    }

    class FetchJSONAssetAsyncTask(private var activity: MainKotlinActivity?) : AsyncTask<Void, Void, ArrayList<FormItem>>() {

        override fun onPreExecute() {
            super.onPreExecute()
            activity?.progressDialog = ProgressDialog(activity)
            activity?.progressDialog?.setTitle(R.string.loading_form_items)
            activity?.progressDialog?.setMessage(R.string.please_wait.toString())
            activity?.progressDialog?.setCancelable(false)
            activity?.progressDialog?.show()
        }

        override fun doInBackground(vararg params: Void?): ArrayList<FormItem> {
            val json: String
            var formItemArrayList: ArrayList<FormItem>

            try {
                val inputStream: InputStream? = activity?.assets?.open(ITEMS_ASSET_DIR)
                json = inputStream?.bufferedReader().use { it?.readText().toString() }
                val allItemsJSON = JSONObject(json)
                val allItems: JSONArray = allItemsJSON.getJSONArray("inputs")
                formItemArrayList = ArrayList(allItems.length())

                for (i in 0 until allItems.length()) {
                    val singleItem = allItems.getJSONObject(i)
                    formItemArrayList.add(FormItem.getItemFromJSON(singleItem))
                }
            } catch (e: Exception) {
                formItemArrayList = ArrayList(0)
            }

            return formItemArrayList
        }

        override fun onPostExecute(result: ArrayList<FormItem>?) {
            super.onPostExecute(result)
            activity?.progressDialog?.dismiss()
            if (result != null) {
                activity?.formItemArrayList = result
            } else {
                activity?.formItemArrayList = ArrayList(0)
            }

            activity?.renderUI()
        }

    }

}
