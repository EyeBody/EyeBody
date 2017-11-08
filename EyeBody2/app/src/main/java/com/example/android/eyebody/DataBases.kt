import android.provider.BaseColumns

import android.provider.BaseColumns._ID

class DataBases {

    class CreateDB : BaseColumns {
        companion object {
            val NAME = "name"
            val CONTACT = "contact"
            val EMAIL = "email"
            val _TABLENAME = "address"
            val _CREATE = (
                    "create table " + _TABLENAME + "("
                            + _ID + " integer primary key autoincrement, "
                            + NAME + " text not null , "
                            + CONTACT + " text not null , "
                            + EMAIL + " text not null );")
        }
    }
}
