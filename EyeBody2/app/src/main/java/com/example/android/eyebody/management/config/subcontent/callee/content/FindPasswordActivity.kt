package com.example.android.eyebody.management.config.subcontent.callee.content

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DialogFragment
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.android.eyebody.utility.StringHashManager
import com.example.android.eyebody.R
import com.example.android.eyebody.utility.SendGmailManager
import kotlinx.android.synthetic.main.activity_find_password.*
import javax.mail.AuthenticationFailedException

/**
 * Created by Yoon on 2017-11-24
 */
class FindPasswordActivity : AppCompatActivity() {

    val TAG = "mydbg_FindPwActivity"

    @SuppressLint("ApplySharedPref")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find_password)

        val email = pre_registered_mail
        val emailButton = send_temporary_key
        val inputKey = input_temporary_key
        val inputKeyButton = validation_temporary_key

        val initPref = baseContext.getSharedPreferences(getString(R.string.getSharedPreference_initSetting), Context.MODE_PRIVATE)
        val pref_email = initPref.getString(getString(R.string.sharedPreference_email), "None")
        val pref_email_auth = initPref.getString(getString(R.string.sharedPreference_email_auth_status), "None")

        var passwordCount = 0

        initPref.edit()
                .putString(getString(R.string.email_auth_key), "${(Math.random() * 1_000_000).toInt()}")
                .apply()

        email.text = pref_email
        emailButton.isClickable = true//pref_email_auth == getString(R.string.sharedPreference_email_auth_ok)
        inputKeyButton.isClickable = emailButton.isClickable

        if (emailButton.isClickable) {
            emailButton.setOnClickListener {

                passwordCount = 0

                //TODO random text generater 를 만들거나 5회이상 입력시 5분 뒤 시도하게 만들기 또는 firebase 로 전향
                val key: Int? = (Math.random() * 1_000_000).toInt()

                val mailSender = SendGmailManager
                        .Builder(senderId = "temporary.mail.for.programming@gmail.com",
                                senderPw = "temporary.mail",
                                subject = "<Eyebody> :: 비밀번호 수정을 위한 임시 키를 발급해드렸습니다.",
                                body = "<a href='www.google.com'>비밀번호</a> 수정을 위한 임시 키는 $key 입니다.\n" +
                                        "감사합니다.",
                                bodyType = "text/html")
                        .setSenderVisibleId("admin@eyebody.noreply.com") // 지메일에서 지원을 안한다 ㅠㅠ : https://stackoverflow.com/a/4189363/7354469
                        .addReceiverId(/*TODO pref_email*/ "tiger940404@naver.com") // 현재 none으로 되어있어서 막아 놓음.
                        //.addCcReceiver("myenggeun44@naver.com") //참조
                        //.addBccReceiver("tiger940404@naver.com") //숨은 참조
                        .build()
                Log.d(TAG, "보내기 시도")

                object : Thread() {
                    override fun run() {
                        try {
                            mailSender?.send()
                            runOnUiThread{
                                Toast.makeText(baseContext,"Mail sent.",Toast.LENGTH_SHORT).show()
                            }
                        } catch(e : Throwable){
                            runOnUiThread{
                                Toast.makeText(baseContext,"Mail send failed : ${e.localizedMessage}",Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }.start()

                val encryptKey = StringHashManager.encryptString(key.toString())

                initPref.edit()
                        .putString(getString(R.string.email_auth_key), encryptKey)
                        .commit()
            }
            inputKeyButton.setOnClickListener {
                passwordCount++
                if(passwordCount % 5 == 0){
                    Toast.makeText(baseContext,"5회 잘못 입력하여 reset 됩니다.",Toast.LENGTH_SHORT).show()
                    initPref.edit()
                            .putString(getString(R.string.email_auth_key), "${(Math.random() * 1_000_000).toInt()}")
                            .apply()
                    return@setOnClickListener
                }
                val encryptInput = StringHashManager.encryptString(inputKey.text.toString())

                //TODO 0000을 지워야 함. release 할 때.
                if (encryptInput == initPref.getString(getString(R.string.email_auth_key), "None") || inputKey.text.toString() == "0000") {

                    Toast.makeText(this, "비밀번호를 변경해주세요.", Toast.LENGTH_LONG).show()

                    class PasswordChangeDialog : DialogFragment() {
                        override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
                            val mView = inflater?.inflate(R.layout.fragment_init2, container)
                            if (mView != null) {
                                mView.setBackgroundColor(0x7f000000)

                                val pw = mView.findViewById<EditText>(R.id.editText_input_password)
                                val pwC = mView.findViewById<EditText>(R.id.editText_input_password_confirm)
                                val bt = mView.findViewById<Button>(R.id.button_submit_password)

                                val pwStr = pw.text
                                val pwConStr = pwC.text

                                pw.setOnEditorActionListener { a, b, c ->
                                    pwC.requestFocus()
                                }
                                pwC.setOnEditorActionListener { a, b, c ->
                                    bt.callOnClick()
                                    val imm: InputMethodManager = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
                                    true
                                }
                                bt.setOnClickListener {
                                    if (pwStr.isNotEmpty() && pwStr.toString() == pwConStr.toString()) {
                                        val hashedPW = StringHashManager.encryptString(pwStr.toString())
                                        initPref.edit()
                                                .putString(getString(R.string.sharedPreference_hashedPW), hashedPW)
                                                .commit()
                                        Toast.makeText(activity, "비밀번호 변경이 완료되었습니다.", Toast.LENGTH_SHORT).show()
                                        dismiss()
                                    } else {
                                        Toast.makeText(activity, "password를 입력해주세요 (1자 이상)", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                            return mView!!
                        }
                    }

                    PasswordChangeDialog().show(fragmentManager, "changePassword")

                } else {
                    Toast.makeText(this, "틀렸습니다!", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}