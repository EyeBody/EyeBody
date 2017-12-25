package com.example.android.eyebody.management.main

import android.annotation.SuppressLint
import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.example.android.eyebody.R
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

val TAG = "mydbg_MainContent.kt"

/**
 * date format must : "yyyyMMddHHmmss"
 */
class DateData(val date: String, var weight: Double?, var memo: String?,
               val imageUri: ArrayList<String> = arrayListOf()) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readValue(Double::class.java.classLoader) as? Double,
            parcel.readString(),
            arrayListOf<String>().apply {
                parcel.readStringList(this)
            })

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(date)
        parcel.writeValue(weight)
        parcel.writeString(memo)
        parcel.writeStringList(imageUri)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<DateData> {
        override fun createFromParcel(parcel: Parcel)
                = DateData(parcel)

        override fun newArray(size: Int): Array<DateData?>
                = arrayOfNulls(size)
    }
}

/**
 * date format must : "yyyyMMddHHmmss"
 * json 포맷 : https://github.com/google/gson
 */
@SuppressLint("ApplySharedPref")
class MainManagementContent(var isInProgress: Boolean,
                            val startDate: String, val desireDate: String,
                            var startWeight: Double, var desireWeight: Double,
                            val dateDataList: ArrayList<DateData>) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readByte() != 0.toByte(),
            parcel.readString(),
            parcel.readString(),
            parcel.readDouble(),
            parcel.readDouble(),
            arrayListOf<DateData>().apply {
                parcel.readList(this, DateData::class.java.classLoader)
            })


    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeByte(if (isInProgress) 1 else 0)
        parcel.writeString(startDate)
        parcel.writeString(desireDate)
        parcel.writeDouble(startWeight)
        parcel.writeDouble(desireWeight)
        parcel.writeList(dateDataList)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<MainManagementContent> {

        val MAIN_MANAGEMENT_SUCCESS = 1
        val MAIN_MANAGEMENT_SUCCESS_PROGRESS_CHANGED = 2

        val MAIN_MANAGEMENT_FAILED_NO_PROGRESS = -1
        val MAIN_MANAGEMENT_FAILED_ARRAY_BOUNDS_EXCEEDED = -2

        override fun createFromParcel(parcel: Parcel): MainManagementContent =
                MainManagementContent(parcel)

        override fun newArray(size: Int): Array<MainManagementContent?> = arrayOfNulls(size)


        private fun initPref(context: Context)
                = context.getSharedPreferences(context.getString(R.string.getSharedPreference_initSetting), AppCompatActivity.MODE_PRIVATE)

        private fun loadMainManagementContents(context: Context): ArrayList<MainManagementContent> {
            val type = object : TypeToken<ArrayList<MainManagementContent>>() {}.type
            val jsonContents = initPref(context).getString(context.getString(R.string.sharedPreference_MainManagementContents), null)
            return if (jsonContents == null)
                arrayListOf()
            else
                Gson().fromJson(jsonContents, type)
        }

        private fun storeMainManagementContents(context: Context, contents: ArrayList<MainManagementContent>) {
            val jsonContents = Gson().toJson(contents)
            initPref(context).edit()
                    .putString(context.getString(R.string.sharedPreference_MainManagementContents), jsonContents)
                    .commit()
        }

        private fun sortedMainManagementContent(contents: ArrayList<MainManagementContent>): ArrayList<MainManagementContent>
                = ArrayList(contents.sortedWith(compareBy<MainManagementContent> { it.startDate }.thenBy { it.desireDate }))

        private fun progressPosition(contents: ArrayList<MainManagementContent>): Int?
                = contents
                .firstOrNull { it.isInProgress }
                ?.let { contents.indexOf(it) }

        private fun findDateDataFromString(content: MainManagementContent, date: String): DateData
                = content.dateDataList
                .firstOrNull { it.date == date }
                .let {
                    if (it == null) {
                        content.dateDataList.add(DateData(date, null, null, arrayListOf()))
                        content.dateDataList[0]
                    } else {
                        it
                    }
                }

        // image uri search
        private fun imageUriRefresh(context: Context, content: MainManagementContent) {

            //val filePath = context.getExternalFilesDir(null).toString() + "/gallery_body/"
            val availableStartFileName = listOf("front_", "side_")

            val dateList = (0 until content.dateDataList.size).map { content.dateDataList[it].date }

            /*val dbWeight = listOf(81.0, null, 78.0, null, 77.0)
            if (dateList.size != dbWeight.size)
                Log.e(TAG, "dbDate와 dbWeight 사이즈가 맞지 않으니 다시 볼 것.")

            findDateDataFromString(content, dateList[0]).imageUri.add(uri)*/

            val dateDataList = ArrayList<DateData>(0)
            val date2imageMap = mutableMapOf<String, ArrayList<String>>()

            context.getExternalFilesDir(null).listFiles { folder ->
                if (folder.isDirectory && folder.endsWith("gallery_body")) {
                    folder.listFiles { file ->
                        (0 until dateList.size).forEach {
                            for (startFileName in availableStartFileName) {
                                if (file.isFile && file.name.startsWith(startFileName + dateList[it])) {
                                    if (date2imageMap[dateList[it]] == null)
                                        date2imageMap[dateList[it]] = arrayListOf(file.toString())
                                    else
                                        date2imageMap[dateList[it]]?.add(file.toString())
                                    Log.d(TAG, "$file added at ${dateList[it]}")
                                }
                            }
                        }
                        true
                    }
                }
                true
            }

            for (i in 0 until dateDataList.size) {
                val toList = date2imageMap[dateList[i]]?.toList()
                if (toList != null) {
                    content.dateDataList[i].imageUri.addAll(toList)
                }
            }

            /*
            (0 until dateList.size).mapTo(dateDataList) {
                DateData(dateList[it], dbWeight[it], date2imageMap[dateList[it]]?.toList())
            }.forEach {
                Log.d(TAG, "${it.date} : ${it.weight} :  ${it.imageUri}")
            }*/

        }

        /**
         * contents 를 호출하되,
         * 진행중인 목표에 대해서는 이미지 uri 를 탐색작업을 추가로 수행합니다.
         * @sample
         * listView.adapter = someAdapter(context, getMainManagementContentArrayListForAdapterFromJson(context))
         */
        fun getMainManagementContentArrayListForAdapterFromJson(context: Context): ArrayList<MainManagementContent> {
            val ret = ArrayList(loadMainManagementContents(context)
                    .sortedWith(compareBy<MainManagementContent> {
                        !it.isInProgress
                    }.thenByDescending {
                        it.startDate
                    }.thenByDescending {
                        it.desireDate
                    }))

            if (ret.firstOrNull()?.isInProgress == true)
                imageUriRefresh(context, ret[0])

            return ret
        }

        /**
         * 진행중인 목표를 종료시킵니다.
         *
         * @return 진행중인 목표가 있을 경우 삭제하고 MAIN_MANAGEMENT_SUCCESS 를 반환하며, 없을 경우 MAIN_MANAGEMENT_FAILED_NO_PROGRESS 를 반환합니다.
         */
        fun terminateProgress(context: Context, progressContent: MainManagementContent): Int {
            if(progressContent.isInProgress) {
                progressContent.isInProgress = false
                val contents = loadMainManagementContents(context)
                val pos = progressPosition(contents)
                if (pos != null) {
                    contents[pos].isInProgress = false
                    storeMainManagementContents(context, contents)
                    return MAIN_MANAGEMENT_SUCCESS
                }
            }
            return MAIN_MANAGEMENT_FAILED_NO_PROGRESS
        }

        /**
         * delete content by position with sorting.
         *
         * @return 성공시 : MAIN_MANAGEMENT_SUCCESS, 잘못된 index 접근 시 : MAIN_MANAGEMENT_FAILED_ARRAY_BOUNDS_EXCEEDED
         */
        fun deleteMainManagementContent(context: Context, position: Int): Int {
            val contents = loadMainManagementContents(context)
            if (position < 0 && contents.size <= position)
                return MAIN_MANAGEMENT_FAILED_ARRAY_BOUNDS_EXCEEDED
            contents.removeAt(position)
            val sortedContents = sortedMainManagementContent(contents)
            storeMainManagementContents(context, sortedContents)
            return MAIN_MANAGEMENT_SUCCESS
        }

        /**
         * insert content with sorting.
         * 진행중인 목표는 오직 하나만 존재할 수 있으므로 추가되는 목표가 isInProgress 속성을 가진다면 다른 목표가 종료됩니다.
         *
         * @return 성공시 : MAIN_MANAGEMENT_SUCCESS, 진행 중인 목표가 변경 될 경우 : MAIN_MANAGEMENT_SUCCESS_PROGRESS_CHANGED
         */
        fun insertMainManagementContent(context: Context, content: MainManagementContent): Int {
            var ret = MAIN_MANAGEMENT_SUCCESS

            val contents = loadMainManagementContents(context)
            if (content.isInProgress) {
                val progress = progressPosition(contents)
                if (progress != null) {
                    contents[progress].isInProgress = false
                    ret = MAIN_MANAGEMENT_SUCCESS_PROGRESS_CHANGED
                }
            }
            contents.add(content)
            val sortedContents = sortedMainManagementContent(contents)
            storeMainManagementContents(context, sortedContents)

            return ret
        }

        /**
         * 진행중인 목표에 날짜데이터를 추가합니다.
         * 이 작업은 추후 (해당 날짜에 image 가 존재한다면) contents 데이터에 uri 를 추가하게 합니다.
         * @see getMainManagementContentArrayListForAdapterFromJson
         *
         * @return 성공시 : MAIN_MANAGEMENT_SUCCESS, 진행 중인 목표가 없을 때 : MAIN_MANAGEMENT_FAILED_NO_PROGRESS
         */
        fun addDateDataToProgressContent(context: Context, date: String): Int {
            val contents = loadMainManagementContents(context)
            val progress = progressPosition(contents)
            return if (progress != null) {
                val start = contents[progress].startDate.toIntOrNull() ?: Int.MAX_VALUE
                val end = contents[progress].desireDate.toIntOrNull() ?: Int.MIN_VALUE
                val dateValue = date.toIntOrNull() ?: 0
                if (dateValue in start..end) {
                    if (contents[progress].dateDataList.firstOrNull { it.date == date } == null)
                        contents[progress].dateDataList.add(DateData(date, null, null, arrayListOf()))
                    storeMainManagementContents(context, contents)
                }
                MAIN_MANAGEMENT_SUCCESS
            } else {
                MAIN_MANAGEMENT_FAILED_NO_PROGRESS
            }
        }

        /**
         * 진행중인 목표의 지정 날짜에 몸무게 속성을 부여합니다.
         *
         * @return 성공시 : MAIN_MANAGEMENT_SUCCESS, 진행 중인 목표가 없을 때 : MAIN_MANAGEMENT_FAILED_NO_PROGRESS
         */
        fun setWeightToProgressContent(context: Context, date: String, weight: Double): Int {
            val contents = loadMainManagementContents(context)
            val progress = progressPosition(contents)
            return if (progress != null) {
                val start = contents[progress].startDate.toIntOrNull() ?: Int.MAX_VALUE
                val end = contents[progress].desireDate.toIntOrNull() ?: Int.MIN_VALUE
                val dateValue = date.toIntOrNull() ?: 0
                if (dateValue in start..end) {
                    if (dateValue == start)
                        contents[progress].startWeight = weight
                    findDateDataFromString(contents[progress], date).weight = weight
                    storeMainManagementContents(context, contents)
                }
                MAIN_MANAGEMENT_SUCCESS
            } else {
                MAIN_MANAGEMENT_FAILED_NO_PROGRESS
            }
        }

        /**
         * Deprecated.
         * @see addDateDataToProgressContent
         *
         * 이미지 uri 를 진행중인 목표의 해당 날짜에 직접 삽입합니다.
         *
         * @return 성공시  MAIN_MANAGEMENT_SUCCESS, 진행 중인 목표가 없을 때 : MAIN_MANAGEMENT_FAILED_NO_PROGRESS
         */
        fun addUriToProgressContent(context: Context, date: String, uri: String): Int {
            val contents = loadMainManagementContents(context)
            val progress = progressPosition(contents)
            return if (progress != null) {
                findDateDataFromString(contents[progress], date).imageUri.add(uri)
                storeMainManagementContents(context, contents)
                MAIN_MANAGEMENT_SUCCESS
            } else {
                MAIN_MANAGEMENT_FAILED_NO_PROGRESS
            }
        }

        /**
         * 진행중인 목표의 지정 날짜에 메모 속성을 부여합니다.
         *
         * @return 성공시 : MAIN_MANAGEMENT_SUCCESS, 진행 중인 목표가 없을 때 : MAIN_MANAGEMENT_FAILED_NO_PROGRESS
         */
        fun addMemoToProgressContent(context: Context, date: String, memo: String): Int {
            val contents = loadMainManagementContents(context)
            val progress = progressPosition(contents)
            return if (progress != null) {
                findDateDataFromString(contents[progress], date).memo = memo
                MAIN_MANAGEMENT_SUCCESS
            } else {
                MAIN_MANAGEMENT_FAILED_NO_PROGRESS
            }
        }
    }
}