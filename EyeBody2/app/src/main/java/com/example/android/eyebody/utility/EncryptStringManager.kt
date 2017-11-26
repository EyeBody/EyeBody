package com.example.android.eyebody.utility

import java.security.MessageDigest

/**
 * Created by Yoon on 2017-11-25
 */
class EncryptStringManager {
    companion object {
        fun encryptString(str: String?): String? {
            if (str != null) {
                val md5 = MessageDigest.getInstance("MD5")
                val strByte = str.toString().toByteArray(charset("unicode"))
                md5.update(strByte)
                val hashedStr = md5.digest().toString(charset("unicode"))
                return hashedStr
            }
            return str
        }
    }
}