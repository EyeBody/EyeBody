import android.provider.BaseColumns

import android.provider.BaseColumns._ID

class DataBases {

    class CreateDB : BaseColumns {
        companion object {
            val NAME = "name"
            val PRICE = "price"
            val _TABLENAME = "address"
            val _CREATE = (
                    "create table " + _TABLENAME + "("
                            + _ID + " integer primary key autoincrement, "
                            + NAME + " text not null , "
                            + PRICE + " text not null);")
        }
    }
    //db테이블에 들어가는 원소 : id , name, price
}
