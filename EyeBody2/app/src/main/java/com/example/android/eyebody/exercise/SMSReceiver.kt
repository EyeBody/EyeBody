package com.example.android.eyebody.exercise

import android.app.Activity
import android.content.*
import android.os.Build
import android.provider.Telephony
import android.telephony.SmsMessage
import android.util.Log
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by ytw11 on 2017-11-06.
 */
class SMSReceiver : BroadcastReceiver() {
    private var spentMoney = String()
    override fun onReceive(context: Context, intent: Intent) {
        val dbHelper= DbHelper(context,"bill.db",null,1)
        var now=System.currentTimeMillis()
        var date= Date(now)
        val simpleDateFormat=SimpleDateFormat("yyyy년 MM월 dd일")
        if (intent.action == ACTION) {
            //Bundle 널 체크
            val bundle = intent.extras ?: return

            //pdu 객체 널 체크
            val pdusObj = bundle.get("pdus") as Array<Any> ?: return

            //message 처리
            val smsMessages = arrayOfNulls<SmsMessage>(pdusObj.size)
            for (i in pdusObj.indices) {
                if (Build.VERSION.SDK_INT >= 19) {
                    var sms: Array<SmsMessage> = Telephony.Sms.Intents.getMessagesFromIntent(intent)
                    smsMessages[i] = sms[0]
                } else smsMessages[i] = SmsMessage.createFromPdu(pdusObj[i] as ByteArray)

                if (!checkBank(smsMessages[i]?.originatingAddress)) {//카드사라는 것을 확인
                   //if (wonFind(smsMessages[i]?.displayMessageBody) != "") {//얼마 썻는지 확인
                       spentMoney = wonFind(smsMessages[i]?.displayMessageBody)
                       var menu="후식"//TODO : 노티피케이션 받아서 저장하는 걸로 하자.
                       //var price=spentMoney as Int
                       var price=1000
                        val activity= NotiActivity()
                        activity.showBasicNotification()
                       dbHelper.insert("what the",menu,price)
                        //TODO:단순 사용 저장 보다는 노티를 날리자.
                    //}
                }
            }
        }
    }

     private fun wonFind(price: String?): String {
                    var array: List<String>? = price?.split(" ")
        for (items in array as List<String>) {
            Log.e(logTag, "haha : " + items) //문자 내용
            if (items[items.length - 1] == '원') {
                return items
            }
        }
        return ""
    }

    private fun checkBank(number: String?): Boolean {
        val numbers = arrayOf("15447200", "15881688", "15661000", "15776200", "15886700", "15888900", "15991155", "15888300", "15889955", "15884515", "15881600")
        //신한카드 , 국민카드, 시티카드, 현대카드, 외환카드, 삼성카드, 하나sk카드, 롯데카드, 우리카드, bc카드,농협카드
        return numbers.contains(number)
    }//문자가 오면 은행들 번호랑 비교해 가면서 은행에서 온 문자라는 것을 판별, 확인 완료

    companion object {
        internal val logTag = "SmsReceiver"
        internal val ACTION = "android.provider.Telephony.SMS_RECEIVED"
    }
}
