import android.content.*
import android.support.v4.app.NotificationCompat.getExtras
import android.os.Bundle
import android.widget.Toast
import android.support.v4.app.NotificationCompat.getExtras
import android.telephony.SmsMessage
import android.util.Log
import android.widget.Toast.LENGTH_SHORT

class SMSReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == ACTION) {
            //Bundel 널 체크
            val bundle = intent.extras ?: return

            //pdu 객체 널 체크
            val pdusObj = bundle.get("pdus") as Array<Any> ?: return

            //message 처리
            val smsMessages = arrayOfNulls<SmsMessage>(pdusObj.size)
            for (i in pdusObj.indices) {
                smsMessages[i] = SmsMessage.createFromPdu(pdusObj[i] as ByteArray)
                Log.e(logTag, "NEW SMS " + i + "th")
                Log.e(logTag, "DisplayOriginatingAddress : " + smsMessages[i]?.getDisplayOriginatingAddress())
                Log.e(logTag, "DisplayMessageBody : " + smsMessages[i]?.getDisplayMessageBody())
                Log.e(logTag, "EmailBody : " + smsMessages[i]?.getEmailBody())
                Log.e(logTag, "EmailFrom : " + smsMessages[i]?.getEmailFrom())
                Log.e(logTag, "OriginatingAddress : " + smsMessages[i]?.getOriginatingAddress())
                Log.e(logTag, "MessageBody : " + smsMessages[i]?.getMessageBody())
                Log.e(logTag, "ServiceCenterAddress : " + smsMessages[i]?.getServiceCenterAddress())
                Log.e(logTag, "TimestampMillis : " + smsMessages[i]?.getTimestampMillis())
                Toast.makeText(context, smsMessages[i]?.getMessageBody(), LENGTH_SHORT).show()
            }
        }
    }
    companion object {
        internal val logTag = "SmsReceiver"
        internal val ACTION = "android.provider.Telephony.SMS_RECEIVED"
    }
}