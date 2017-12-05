package com.example.android.eyebody.management.food

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * Created by ytw11 on 2017-11-15.
 */
class DbHelper(var context: Context, var name:String, private var factory: SQLiteDatabase.CursorFactory?, var version:Int) : SQLiteOpenHelper(context,name,factory,version ) {
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
        db.execSQL("UPDATE BILL SET price=$price WHERE menu='$menu';")
        db.close()
    }
    fun getResult(count:Int): ArrayList<String>
    {
        // 읽기가 가능하게 DB 열기
        // 읽기가 가능하게 DB 열기
        val db:SQLiteDatabase = readableDatabase
        var result= ArrayList<String>()
        // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 인덱스가 주어질 때 테이블에 있는 데이터 출력
        var cursor:Cursor = db . rawQuery ("SELECT * FROM BILL", null)
        for(x in 1..count){
            cursor.moveToNext()
        }
        var date=cursor.getString(0)
        var menu=cursor.getString(1)
        var price=cursor.getInt(2).toString()
        cursor.getInt(2)
        result.add(date)//date
        result.add(menu)//menu
        result.add(price)//price
        return result
    }//TODO : 인자로 인덱스를 넘기면 db에서 그 인덱스번째에 있는 칼럼들을 가져오는 걸로 구현

    fun getCount():Int
    {
        var count=0
        var db:SQLiteDatabase=readableDatabase
        var cursor:Cursor = db . rawQuery ("SELECT * FROM BILL", null)
        while (cursor.moveToNext()) {
            count++
        }
        return count
    }
}