package com.example.android.eyebody
import android.content.*
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
                smsMessages[i] = SmsMessage.createFromPdu(pdusObj[i] as ByteArray)
                Log.e(logTag, "NEW SMS " + i + "th")
                Log.e(logTag, "DisplayOriginatingAddress : " + smsMessages[i]?.displayOriginatingAddress)
                Log.e(logTag, "DisplayMessageBody : " + smsMessages[i]?.displayMessageBody)
                Log.e(logTag, "EmailBody : " + smsMessages[i]?.emailBody)
                Log.e(logTag, "EmailFrom : " + smsMessages[i]?.emailFrom)
                Log.e(logTag, "OriginatingAddress : " + smsMessages[i]?.originatingAddress)
                Log.e(logTag, "MessageBody : " + smsMessages[i]?.messageBody)
                Log.e(logTag, "ServiceCenterAddress : " + smsMessages[i]?.serviceCenterAddress)
                Log.e(logTag, "TimestampMillis : " + smsMessages[i]?.timestampMillis)

                Toast.makeText(context, smsMessages[i]?.messageBody, LENGTH_LONG).show()
            }
        }
    }

    companion object {
        internal val logTag = "SmsReceiver"
        internal val ACTION = "android.provider.Telephony.SMS_RECEIVED"
    }
}