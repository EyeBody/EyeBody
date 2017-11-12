import android.content.Context
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class DbOpenHelper() {
    private val DATABASE_NAME:String="addressbook.db"
    private var mDBHelper: DatabaseHelper? = null
    private var mCtx:Context?=null
    private val DATABASE_VERSION=1
    var mDB:SQLiteDatabase?=null

    private inner class DatabaseHelper
    (context: Context, name: String,
     factory: SQLiteDatabase.CursorFactory?, version: Int) : SQLiteOpenHelper(context, name, factory, version) {

        // 최초 DB를 만들때 한번만 호출된다.
        override fun onCreate(db: SQLiteDatabase) {
            db.execSQL(DataBases.CreateDB._CREATE)
        }

        // 버전이 업데이트 되었을 경우 DB를 다시 만들어 준다.
        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            db.execSQL("DROP TABLE IF EXISTS " + DataBases.CreateDB._TABLENAME)
            onCreate(db)
        }
    }

    @Throws(SQLException::class)
    fun open(): DbOpenHelper {
        mDBHelper = DatabaseHelper(mCtx as Context, DATABASE_NAME, null, DATABASE_VERSION)
        mDB = mDBHelper!!.writableDatabase
        return this
    }

    fun close() {
        mDB?.close()
    }

    companion object {
        private val DATABASE_NAME = "addressbook.db"
        private val DATABASE_VERSION = 1
        lateinit var mDB: SQLiteDatabase
    }
}
