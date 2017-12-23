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
class DateData(val date: String, var weight: Double?) : Parcelable {

    val imageUri = arrayListOf<String>()

    constructor(date: String, weight: Double?, imageUri: ArrayList<String>)
            : this(date, weight) {
        this.imageUri.addAll(imageUri)
    }

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readValue(Double::class.java.classLoader) as? Double){
        parcel.readStringArray(imageUri.toTypedArray())
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(date)
        parcel.writeValue(weight)
        parcel.writeStringArray(imageUri.toTypedArray())
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<DateData> {
        override fun createFromParcel(parcel: Parcel): DateData = DateData(parcel)

        override fun newArray(size: Int): Array<DateData?> = arrayOfNulls(size)
    }
}

/**
 * date format must : "yyyyMMddHHmmss"
 * json 포맷 : https://github.com/google/gson
 */
@SuppressLint("ApplySharedPref")
class MainManagementContent(var isInProgress: Boolean,
                            val startDate: String, val desireDate: String,
                            var startWeight: Double, var desireWeight: Double) : Parcelable {

    val dateDataList = arrayListOf<DateData>()

    constructor(isInProgress: Boolean,
                startDate: String, desireDate: String,
                startWeight: Double, desireWeight: Double,
                dateDataList: ArrayList<DateData>) : this(isInProgress, startDate, desireDate, startWeight, desireWeight) {
        this.dateDataList.addAll(dateDataList)
    }

    constructor(parcel: Parcel) : this(
            parcel.readByte() != 0.toByte(),
            parcel.readString(),
            parcel.readString(),
            parcel.readDouble(),
            parcel.readDouble()) {
        val list = listOf(DateData)
        parcel.readList(list, DateData::class.java.classLoader)
        // TODO 어렵다..
    }


    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeByte(if (isInProgress) 1 else 0)
        parcel.writeString(startDate)
        parcel.writeString(desireDate)
        parcel.writeDouble(startWeight)
        parcel.writeDouble(desireWeight)
        parcel.writeList(dateDataList.toList())
        //parcel.writeTypedArray<DateData>(dateDataList.toTypedArray(), Parcelable.PARCELABLE_WRITE_RETURN_VALUE)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<MainManagementContent> {
        override fun createFromParcel(parcel: Parcel): MainManagementContent =
                MainManagementContent(parcel)

        override fun newArray(size: Int): Array<MainManagementContent?> = arrayOfNulls(size)


        private fun initPref(context: Context)
                = context.getSharedPreferences(context.getString(R.string.getSharedPreference_initSetting), AppCompatActivity.MODE_PRIVATE)

        private fun loadMainManagementContents(context: Context): ArrayList<MainManagementContent> {
            val type = object : TypeToken<ArrayList<MainManagementContent>>() {}.type
            val jsonContents = initPref(context).getString(context.getString(R.string.sharedPreference_MainManagementContents), null)
            val contents =
                    if (jsonContents == null)
                        arrayListOf<MainManagementContent>()
                    else
                        Gson().fromJson(jsonContents, type)
            return contents
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
                        content.dateDataList.add(DateData(date, null, arrayListOf()))
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
         * delete content by position with sorting.
         */
        fun deleteMainManagementContent(context: Context, position: Int) {
            val contents = loadMainManagementContents(context)
            contents.removeAt(position)
            val sortedContents = sortedMainManagementContent(contents)
            storeMainManagementContents(context, sortedContents)
        }

        /**
         * insert content with sorting.
         * 진행중인 목표는 오직 하나만 존재할 수 있으므로 추가되는 목표가 isInProgress 속성을 가진다면 다른 목표가 종료됩니다.
         */
        fun insertMainManagementContent(context: Context, content: MainManagementContent) {
            val contents = loadMainManagementContents(context)
            if (content.isInProgress) {
                val progress = progressPosition(contents)
                if (progress != null)
                    contents[progress].isInProgress = false
            }
            contents.add(content)
            val sortedContents = sortedMainManagementContent(contents)
            storeMainManagementContents(context, sortedContents)
        }

        /**
         * 진행중인 목표에 날짜데이터를 추가합니다.
         * 이 작업은 추후 (해당 날짜에 image 가 존재한다면) contents 데이터에 uri 를 추가하게 합니다.
         * @see getMainManagementContentArrayListForAdapterFromJson
         */
        fun addDateDataToProgressContent(context: Context, date: String) {
            val contents = loadMainManagementContents(context)
            val progress = progressPosition(contents)
            if (progress != null) {
                val start = contents[progress].startDate.toIntOrNull() ?: Int.MAX_VALUE
                val end = contents[progress].desireDate.toIntOrNull() ?: Int.MIN_VALUE
                val dateValue = date.toIntOrNull() ?: 0
                if (dateValue in start..end) {
                    if (contents[progress].dateDataList.firstOrNull { it.date == date } == null)
                        contents[progress].dateDataList.add(DateData(date, null, arrayListOf()))
                    storeMainManagementContents(context, contents)
                }
            }
        }

        /**
         * 진행중인 목표에 추가된 날짜데이터에 weight 속성을 부여합니다.
         */
        fun setWeightToProgressContent(context: Context, date: String, weight: Double) {
            val contents = loadMainManagementContents(context)
            val progress = progressPosition(contents)
            if (progress != null) {
                val start = contents[progress].startDate.toIntOrNull() ?: Int.MAX_VALUE
                val end = contents[progress].desireDate.toIntOrNull() ?: Int.MIN_VALUE
                val dateValue = date.toIntOrNull() ?: 0
                if (dateValue in start..end) {
                    if (dateValue == start)
                        contents[progress].startWeight = weight
                    findDateDataFromString(contents[progress], date).weight = weight
                    storeMainManagementContents(context, contents)
                }
            }
        }

        /**
         * Deprecated.
         * @see addDateDataToProgressContent
         *
         * 이미지 uri 를 진행중인 목표의 해당 날짜에 직접 삽입합니다.
         */
        fun addUriToProgressContent(context: Context, date: String, uri: String) {
            val contents = loadMainManagementContents(context)
            val progress = progressPosition(contents)
            if (progress != null) {
                val preImageUri = findDateDataFromString(contents[progress], date).imageUri.add(uri)
            }
            storeMainManagementContents(context, contents)
        }
    }
}