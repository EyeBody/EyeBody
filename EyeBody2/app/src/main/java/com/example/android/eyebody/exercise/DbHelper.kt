package com.example.android.eyebody.exercise

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * Created by ytw11 on 2017-11-15.
 */
class DbHelper(var context: Context,var name:String,var factory: SQLiteDatabase.CursorFactory?,var version:Int) : SQLiteOpenHelper(context,name,factory,version ) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE BILL (_id INTEGER PRIMARY KEY AUTOINCREMENT,menu STRING,price INTEGER,create_at TEXT);)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

    fun insert(create_at: String, menu: String, price: Int) {
        var db: SQLiteDatabase = writableDatabase
        db.execSQL("INSERT INTO BILL VALUES(null, '$menu', $price, '$create_at');")
        db.close()
    }

    fun update(menu: String, price: Int) {
        var db: SQLiteDatabase = writableDatabase
        // 입력한 항목과 일치하는 행의 가격 정보 수정
        db.execSQL("UPDATE BILL SET price=$price WHERE menu='$menu';");
        db.close()
    }
    fun getResult(): String
    {
        // 읽기가 가능하게 DB 열기
        var db:SQLiteDatabase = readableDatabase
        var result:String = ""

        // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
         var cursor:Cursor = db . rawQuery ("SELECT * FROM BILL", null)
            while (cursor.moveToNext()) {
            result += cursor.getString(0)+" : "+cursor.getString(1)+" | "+cursor.getInt(2)+"원 "+cursor.getString(3)+"\n"
        }
        return result
    }
}