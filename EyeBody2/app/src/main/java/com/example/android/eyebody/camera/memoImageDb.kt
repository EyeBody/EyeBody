package com.example.android.eyebody.camera

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * Created by ytw11 on 2017-12-25.
 * db 이름 : memoImage
 * 구성 :
 */
class memoImageDb(var context: Context, var name:String, private var factory: SQLiteDatabase.CursorFactory?, var version:Int) : SQLiteOpenHelper(context,name,factory,version )
{
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE MEMOIMAGE (_id INTEGER PRIMARY KEY AUTOINCREMENT,frontImage STRING,sideImage STRING,memo STRING,create_at TEXT);)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

    fun insert(create_at: String, frontImage: String, sideImage:String,memo:String) {
        var db: SQLiteDatabase = writableDatabase
        db.execSQL("INSERT INTO MEMOIMAGE VALUES(null, '$frontImage', '$sideImage','$memo', '$create_at');")
        db.close()
    }

    fun getResult(count:Int): ArrayList<String>
    {
        // 읽기가 가능하게 DB 열기
        val db: SQLiteDatabase = readableDatabase
        var result= ArrayList<String>()
        // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 인덱스가 주어질 때 테이블에 있는 데이터 출력
        var cursor: Cursor = db . rawQuery ("SELECT * FROM MEMOIMAGE", null)
        for(x in 1..count){
            cursor.moveToNext()
        }
        var frontImage=cursor.getString(0)
        var sideImage=cursor.getString(1)
        var memo=cursor.getString(2)
        cursor.getInt(2)
        result.add(frontImage)//frontImage
        result.add(sideImage)//sideImage
        result.add(memo)//memo
        return result
    }//TODO : 인자로 인덱스를 넘기면 db에서 그 인덱스번째에 있는 칼럼들을 가져오는 걸로 구현

    fun getCount():Int
    {
        var count=0
        var db: SQLiteDatabase =readableDatabase
        var cursor: Cursor = db . rawQuery ("SELECT * FROM MEMOIMAGE", null)
        while (cursor.moveToNext()) {
            count++
        }
        return count
    }
    fun getMemo(imageName:String):String{
        val db: SQLiteDatabase = readableDatabase
        var memo=""
        var cursor: Cursor = db . rawQuery ("SELECT * FROM MEMOIMAGE", null)
        while(cursor.getString(0)==imageName || cursor.getString(1)==imageName){
            cursor.moveToNext()
        }
        memo=cursor.getString(2)
        return memo
    }
    fun getDate(imageName:String):String{
        val db: SQLiteDatabase = readableDatabase
        var date=""
        var cursor: Cursor = db . rawQuery ("SELECT * FROM MEMOIMAGE", null)
        while(cursor.getString(0)==imageName || cursor.getString(1)==imageName){
            cursor.moveToNext()
        }
        date=cursor.getString(3)
        return date
    }
}