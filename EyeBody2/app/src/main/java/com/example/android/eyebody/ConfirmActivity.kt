package com.example.android.eyebody

import android.content.DialogInterface
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_confirm.*

class ConfirmActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm)
        saveButtonClicked()
    }
    private fun saveButtonClicked(){
        val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(baseContext)
        alertDialogBuilder.setTitle("저장")
        val alertDialog = AlertDialog.Builder(baseContext)
        button_save.setOnClickListener {
            alertDialog.setMessage("저장하시겠습니까?").setCancelable(false).setPositiveButton("확인"
            ) { dialog, which ->
                // 'YES'
                Toast.makeText(baseContext, "저장되었습니다", Toast.LENGTH_SHORT)
            }.setNegativeButton("취소",
                    DialogInterface.OnClickListener { dialog, which ->
                        Toast.makeText(baseContext, "취소되었습니다", Toast.LENGTH_SHORT)
                    })
            val alert = alertDialog.create()
            alert.show()
        }
    }
}

