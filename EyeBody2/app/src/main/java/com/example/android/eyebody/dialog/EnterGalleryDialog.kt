package com.example.android.eyebody.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.android.eyebody.GalleryActivity
import com.example.android.eyebody.R
import java.security.MessageDigest

/**
 * Created by YOON on 2017-09-24.
 */

class EnterGalleryDialog : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        Log.d("mydbg_enterGallery", "다이얼로그 시작")



        val layoutInflater = activity.layoutInflater
        val dialogbuilder = AlertDialog.Builder(activity)
        val view = layoutInflater.inflate(R.layout.dialog_main_enter_gallery_input_pw, null)
        val bt_pwSubmit = view.findViewById<Button>(R.id.Button_enter_gallery_pw_submit)
        val tv_pwInput = view.findViewById<EditText>(R.id.EditText_enter_gallery_pw_input)


        // 키보드-확인 눌렀을 때 반응
        tv_pwInput.setOnEditorActionListener { textView, i, keyEvent ->
            true
            Log.d("mydbg_enterGallery", "is ok ????")
            bt_pwSubmit.callOnClick()
        }


        dialogbuilder.setView(view)
                .setTitle("Input Password")
                .setMessage("Please type your private password")


        // button Listener
        bt_pwSubmit.setOnClickListener {
            // EditText 친 부분 MD5 암호화
            val md5 = MessageDigest.getInstance("MD5")
            val str_inputPW: String = tv_pwInput.text.toString()
            val pwByte: ByteArray = str_inputPW.toByteArray(charset("unicode"))
            md5.update(pwByte)
            val hashedPW = md5.digest().toString(charset("unicode"))

            // 공유변수와 일치여부 검증
            val sharedPref: SharedPreferences = activity.getSharedPreferences("hash-md5", Context.MODE_PRIVATE)
            val preHashedPW = sharedPref.getString("hashedPW", "none")
            Log.d("mydbg_enterGallery", "prePW : $preHashedPW / PW : $hashedPW")
            if (hashedPW == preHashedPW.toString()) {
                val galleryPage = Intent(activity, GalleryActivity::class.java)
                startActivity(galleryPage)
                dismiss()
            } else {
                Toast.makeText(activity, "관계자 외 출입금지~", Toast.LENGTH_LONG).show()
            }

        }

        return dialogbuilder.create()
    }

    override fun onStop() {
        super.onStop()
        Log.d("mydbg_enterGallery", "다이얼로그가 가려짐 또는 종료됨")
    }

}