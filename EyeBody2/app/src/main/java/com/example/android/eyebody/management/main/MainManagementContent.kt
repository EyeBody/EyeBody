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
class DateData(val date: String, var weight: Double?, val imageUri: ArrayList<String>)

/**
 * date format must : "yyyyMMddHHmmss"
 * json 포맷 : https://github.com/google/gson
 */
@SuppressLint("ApplySharedPref")
class MainManagementContent(var isInProgress: Boolean,
                            val startDate: String, val endDate: String,
                            val startWeight: Double, val endWeight: Double,
                            val dateDataList: ArrayList<DateData>) {

    companion object {

        private fun initPref(context: Context)
                = context.getSharedPreferences(context.getString(R.string.getSharedPreference_initSetting), AppCompatActivity.MODE_PRIVATE)

        private fun getMainManagementContentArrayList(context: Context): ArrayList<MainManagementContent> {
            val type = object : TypeToken<ArrayList<MainManagementContent>>() {}.type
            val jsonContents = initPref(context).getString(context.getString(R.string.sharedPreference_MainManagementContents), null)
            val contents =
                    if (jsonContents == null)
                        arrayListOf<MainManagementContent>()
                    else
                        Gson().fromJson(jsonContents, type)
            return contents
        }

        private fun putMainManagementContentArrayList(context: Context, contents: ArrayList<MainManagementContent>) {
            val jsonContents = Gson().toJson(contents)
            initPref(context).edit()
                    .putString(context.getString(R.string.sharedPreference_MainManagementContents), jsonContents)
                    .commit()
        }

        private fun sortedMainManagementContent(contents: ArrayList<MainManagementContent>): ArrayList<MainManagementContent>
                = ArrayList(contents.sortedWith(compareBy<MainManagementContent>{it.startDate}.thenBy{it.endDate}))

        fun getMainManagementContentArrayListForAdapter(context: Context): ArrayList<MainManagementContent>
        = ArrayList(getMainManagementContentArrayList(context)
                .sortedWith(compareBy<MainManagementContent>{
                    !it.isInProgress
                }.thenByDescending{
                    it.startDate
                }.thenByDescending{
                    it.endDate
                }))

        fun deleteMainManagementContent(context: Context, position: Int) {
            val contents = getMainManagementContentArrayList(context)
            contents.removeAt(position)
            val sortedContents = sortedMainManagementContent(contents)
            putMainManagementContentArrayList(context, sortedContents)
        }

        fun putMainManagementContent(context: Context, content: MainManagementContent) {
            val contents = getMainManagementContentArrayList(context)
            if(content.isInProgress){
                val progress = progressPosition(contents)
                if(progress != null)
                    contents[progress].isInProgress = false
            }
            contents.add(content)
            val sortedContents = sortedMainManagementContent(contents)
            putMainManagementContentArrayList(context, sortedContents)
        }

        private fun progressPosition(contents: ArrayList<MainManagementContent>): Int?
                = contents
                .firstOrNull { it.isInProgress }
                ?.let { contents.indexOf(it) }

        fun addDateDataToProgressContent(context: Context, date: String) {
            val contents = getMainManagementContentArrayList(context)
            val progress = progressPosition(contents)
            if(progress != null)
                contents[progress].dateDataList.add(DateData(date,null,arrayListOf()))
            putMainManagementContentArrayList(context, contents)
        }

        private fun findDateDataFromString(content: MainManagementContent, date: String): DateData
                = content.dateDataList
                .firstOrNull { it.date == date }
                .let {
                    if(it == null){
                        content.dateDataList.add(DateData(date, null, arrayListOf()))
                        content.dateDataList[0]
                    }else{
                        it
                    }
                }

        fun setWeightToProgressContent(context: Context, date: String, weight: Double) {
            val contents = getMainManagementContentArrayList(context)
            val progress = progressPosition(contents)
            if(progress != null)
                findDateDataFromString(contents[progress], date).weight = weight
            putMainManagementContentArrayList(context, contents)
        }

        fun addUriToProgressContent(context: Context, date: String, uri: String) {
            val contents = getMainManagementContentArrayList(context)
            val progress = progressPosition(contents)
            if(progress != null)
                findDateDataFromString(contents[progress], date).imageUri.add(uri)
            putMainManagementContentArrayList(context, contents)
        }


        /*
         * deprecated.
         *
         * 목표를 생성하면 무조건 fragment로 오기때문에 adapter를 연결하는 과정에서 get을 호출할 수 밖에 없다.
         * 그러므로 이미지uri 검색은 현재 활성화된 것에 한해서만 검색하면 된다.
         * (활성화된 목표는 1개밖에 없고 끝난 목표들에 대해 사진을 표현해 줄 필요가 없기 때문) - 표현 해주면 안 됨
         *
        fun getMainManagementContentArrayListWithImageDataSearch(context: Context): ArrayList<MainManagementContent> {
            val contents = getMainManagementContentArrayList(context)

            val progress = progressPosition(contents)
            // 이미지 uri 검색해서 반환함.
            // get할때 uri를 왜 계속 검색해야 하냐면 데이터는 수시로 바뀌기 때문. (insert, delete 할 때 안 넣는 이유)


            contents[progress].dateDataList = listOf(DateData("", 3.0, listOf()))


            val filePath = context.getExternalFilesDir(null).toString() + "/gallery_body/"
            val availableStartFileName = listOf("front_", "side_")

            val dbDate = listOf("20171201", "20171205", "20171208", "20171209", "20171211")
            val dbWeight = listOf(81.0, null, 78.0, null, 77.0)
            if (dbDate.size != dbWeight.size)
                Log.e(TAG, "dbDate와 dbWeight 사이즈가 맞지 않으니 다시 볼 것.")

            val dateDataList = ArrayList<DateData>(0)
            val date2imageMap = mutableMapOf<String, ArrayList<String>>()

            context.getExternalFilesDir(null).listFiles { folder ->
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

            return contents
        }
        */
    }
}