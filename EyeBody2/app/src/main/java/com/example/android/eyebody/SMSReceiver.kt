package com.example.android.eyebody
import android.content.*
import android.os.Build
import android.provider.Telephony
import android.widget.Toast

/**
 * Created by ytw11 on 2017-11-06.
 */
import android.telephony.SmsMessage
import android.util.Log
import android.widget.Toast.LENGTH_LONG

class SMSReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == ACTION) {
            //Bundle 널 체크
            val bundle = intent.extras ?: return

            //pdu 객체 널 체크
            val pdusObj = bundle.get("pdus") as Array<Any> ?: return

            //message 처리
            val smsMessages = arrayOfNulls<SmsMessage>(pdusObj.size)
            for (i in pdusObj.indices) {
                if(Build.VERSION.SDK_INT>=19){
                    var msgs:Array<SmsMessage> =Telephony.Sms.Intents.getMessagesFromIntent(intent)
                    smsMessages[i]=msgs[0]
                }
                else{
                    smsMessages[i] = SmsMessage.createFromPdu(pdusObj[i] as ByteArray)
                }
                Log.e(logTag, "NEW SMS " + i + "th")
                Log.e(logTag, "DisplayOriginatingAddress : " + smsMessages[i]?.displayOriginatingAddress) //문자 발신 번호
                Log.e(logTag, "DisplayMessageBody : " + smsMessages[i]?.displayMessageBody) //문자 내용
                Log.e(logTag, "OriginatingAddress : " + smsMessages[i]?.originatingAddress)//문자 발신 번호
                Log.e(logTag, "MessageBody : " + smsMessages[i]?.messageBody)


                Toast.makeText(context, smsMessages[i]?.messageBody, LENGTH_LONG).show()
            }
        }
    }

    companion object {
        internal val logTag = "SmsReceiver"
        internal val ACTION = "android.provider.Telephony.SMS_RECEIVED"
    }
}