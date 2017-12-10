package com.example.android.eyebody.management.main

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.example.android.eyebody.R
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

val TAG = "mydbg_MainContent.kt"

/**
 * date format must : "yyyyMMddHHmmss"
 */
class DateData(val date: String, val weight: Double?, val imageUri: List<String>?)

/**
 * date format must : "yyyyMMddHHmmss"
 * https://github.com/google/gson
 * "json data~~" = Gson().toJson(contents)
 * Gson().fromJson("json data~~", 클래스::class.java)
 */
class MainManagementContent(var isInProgress: Boolean,
                            val startDate: String, val endDate: String,
                            val startWeight: Double, val endWeight: Double,
                            val DataList: List<DateData>) {
    companion object {

        @SuppressLint("ApplySharedPref")
        fun putMainManagementContent(context: Context, content: MainManagementContent) {


            val jsonContent = Gson().toJson(content)
            val initPref = context.getSharedPreferences(context.getString(R.string.getSharedPreference_initSetting), AppCompatActivity.MODE_PRIVATE)
            val fillInfo_json = initPref.getString(context.getString(R.string.sharedPreference_MainManagementContent_Fill_Information_Json_ArrayListOfString), null)
            if (fillInfo_json == null) {
                initPref.edit()
                        .putString(context.getString(R.string.sharedPreference_MainManagementContent_Fill_Information_Json_ArrayListOfString), Gson().toJson(arrayListOf("0")))
                        .putString(context.getString(R.string.sharedPreference_MainManagementContent) + "0", jsonContent)
                        .commit()
            } else {
                val type = object : TypeToken<java.util.ArrayList<String>>() {}.type
                val fillInfo: java.util.ArrayList<String> = Gson().fromJson(fillInfo_json, type)

                var position = 0
                while (true) {
                    if (fillInfo.contains("$position")) {
                        Log.d(TAG, "$position 자리는 차있습니다.")
                        position += 1
                    } else
                        break
                }
                fillInfo.add("$position")
                val changedFillInfo_json = Gson().toJson(fillInfo)
                initPref.edit()
                        .putString(context.getString(R.string.sharedPreference_MainManagementContent_Fill_Information_Json_ArrayListOfString), changedFillInfo_json)
                        .putString(context.getString(R.string.sharedPreference_MainManagementContent) + "$position", jsonContent)
                        .commit()
            }

        }

        fun getMainManagementContents(context: Context): ArrayList<MainManagementContent> {
            val contents = arrayListOf<MainManagementContent>()

            val initSharedPref = context.getSharedPreferences(context.getString(R.string.getSharedPreference_initSetting), Context.MODE_PRIVATE)
            val fillInfo_json = initSharedPref.getString(context.getString(R.string.sharedPreference_MainManagementContent_Fill_Information_Json_ArrayListOfString), null)
            if (fillInfo_json == null) {
                // 아무 데이터도 없다. 냅둠. 위에 비어있는거 선언해서.
            } else {
                val type = object : TypeToken<ArrayList<String>>() {}.type
                val fillInfo: ArrayList<String> = Gson().fromJson(fillInfo_json, type)
                for (item in fillInfo) {
                    val content_json = initSharedPref.getString(context.getString(R.string.sharedPreference_MainManagementContent) + item, null)
                    if (content_json == null) {
                        // 아무것도 없다? 냅둠.
                    } else {
                        val content = Gson().fromJson(content_json, MainManagementContent::class.java)
                        contents.add(content)
                    }
                }
            }

            // TODO 이미지 uri 검색해서 반환할 수 있어야 함.

            /*
            val filePath = activity.getExternalFilesDir(null).toString() + "/gallery_body/"
            val availableStartFileName = listOf("front_", "side_")

            val dbDate = listOf("20171201", "20171205", "20171208", "20171209", "20171211")
            val dbWeight = listOf(81.0, null, 78.0, null, 77.0)
            if (dbDate.size != dbWeight.size)
                Log.e(TAG, "dbDate와 dbWeight 사이즈가 맞지 않으니 다시 볼 것.")

            val dateDataList = ArrayList<DateData>(0)
            val date2imageMap = mutableMapOf<String, ArrayList<String>>()

            activity.getExternalFilesDir(null).listFiles { folder ->
                if (folder.isDirectory && folder.endsWith("gallery_body")) {
                    folder.listFiles { file ->
                        (0 until dbDate.size).forEach {
                            for (startFileName in availableStartFileName) {
                                if (file.isFile && file.name.startsWith(startFileName + dbDate[it])) {
                                    if (date2imageMap[dbDate[it]] == null)
                                        date2imageMap[dbDate[it]] = arrayListOf(file.toString())
                                    else
                                        date2imageMap[dbDate[it]]?.add(file.toString())
                                    Log.d(TAG, file.toString())
                                }
                            }
                        }
                        true
                    }
                }
                true
            }
            (0 until dbDate.size).mapTo(dateDataList) {
                DateData(dbDate[it], dbWeight[it], date2imageMap[dbDate[it]]?.toList())
            }.forEach {
                Log.d(TAG, "${it.date} : ${it.weight} :  ${it.imageUri}")
            }

            val contents = arrayListOf(
                    MainManagementContent(false,
                            "20171201", "20171230",
                            80.0, 70.0,
                            dateDataList
                    ),
                    MainManagementContent(false,
                            "20171201", "20171230",
                            80.0, 70.0,
                            listOf(DateData("20171220", null, List(0, { "" })))
                    ))

            return contents
            */
            return contents
        }
    }
}