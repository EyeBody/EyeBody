package com.example.android.eyebody.utility

import android.util.Log
import java.io.ByteArrayInputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.security.AccessController
import java.security.PrivilegedAction
import java.security.Provider
import java.security.Security
import java.util.*
import javax.activation.DataHandler
import javax.activation.DataSource
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage


/**
 * Created by YOON on 2017-11-26
 *
 * earn knowledge at
 * https://stackoverflow.com/questions/2020088/sending-email-in-android-using-javamail-api-without-using-the-default-built-in-a
 * <Java mail api using g-mail authentication>
 */
class SendGmailManager private constructor() : javax.mail.Authenticator() {
    val TAG = "mydbg_SendGmailManager"

    private val mailHost: String = "smtp.gmail.com"
    private var senderId: String? = null
    private var senderPw: String? = null
    private var senderVisibleId: String? = null
    private var receiverId: String? = null
    private var ccReceiverId: String? = null
    private var bccReceiverId: String? = null
    private var subject: String? = null
    private var body: String? = null
    private var bodyType: String? = null
    private var session: Session? = null


    private constructor(senderId: String?, senderPw: String?, senderVisibleId: String?, receiverId: String?, ccReceiverId: String?, bccReceiver: String?, subject: String?, body: String?, bodyType: String?) : this() {
        this.senderId = senderId
        this.senderPw = senderPw
        this.senderVisibleId = senderVisibleId
        this.receiverId = receiverId
        this.ccReceiverId = ccReceiverId
        this.bccReceiverId = bccReceiverId
        this.subject = subject
        this.body = body
        this.bodyType = bodyType
    }


    override fun getPasswordAuthentication(): PasswordAuthentication =
            PasswordAuthentication(senderId, senderPw)

    @Synchronized
    @Throws(Exception::class)
    fun send() {
        class JSSEProvider(name: String, version: Double, info: String) : Provider(name, version, info) {
            init {
                AccessController.doPrivileged(PrivilegedAction<Any> {
                    put("SSLContext.TLS",
                            "org.apache.harmony.xnet.provider.jsse.SSLContextImpl")
                    put("Alg.Alias.SSLContext.TLSv1",
                            "TLS")
                    put("KeyManagerFactory.X509",
                            "org.apache.harmony.xnet.provider.jsse.KeyManagerFactoryImpl")
                    put("TrustManagerFactory.X509",
                            "org.apache.harmony.xnet.provider.jsse.TrustManagerFactoryImpl")
                })
            }
        }

        Security.addProvider(JSSEProvider("HarmonyJSSE", 1.0, "Harmony JSSE Provider"))

        val props = Properties()
        props.setProperty("mail.transport.protocol", "smtp")
        props.setProperty("mail.host", mailHost)
        props.put("mail.smtp.auth", "true")
        props.put("mail.smtp.port", "465")
        props.put("mail.smtp.socketFactory.port", "465")
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory")
        props.put("mail.smtp.socketFactory.fallback", "false")
        props.setProperty("mail.smtp.quitwait", "false")
        session = Session.getDefaultInstance(props, this)

        val message = MimeMessage(session)
        try {

            class ByteArrayDataSource(private var data: ByteArray?, private var type: String?) : DataSource {
                override fun getContentType() = type ?: "application/octet-stream"

                //@Throws(IOException::class)
                override fun getInputStream(): InputStream = ByteArrayInputStream(data)

                //@Throws(IOException::class)
                override fun getOutputStream(): OutputStream {
                    throw IOException("Not Supported")
                }

                override fun getName() = "ByteArrayDataSource"
            }

            val handler = DataHandler(javax.mail.util.ByteArrayDataSource(body?.toByteArray(), bodyType))
            message.sender = InternetAddress(senderId)
            message.subject = subject
            message.dataHandler = handler
            if (receiverId != null) {
                if (receiverId?.indexOf(',')!! > 0)
                    message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(receiverId))
                else
                    message.setRecipient(Message.RecipientType.TO, InternetAddress(receiverId))
            }
            if (ccReceiverId != null) {
                if (ccReceiverId?.indexOf(',')!! > 0)
                    message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(ccReceiverId))
                else
                    message.setRecipient(Message.RecipientType.CC, InternetAddress(ccReceiverId))
            }
            if (bccReceiverId != null) {
                if (bccReceiverId?.indexOf(',')!! > 0)
                    message.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(bccReceiverId))
                else
                    message.setRecipient(Message.RecipientType.BCC, InternetAddress(bccReceiverId))
            }
            Log.d(TAG, "보낸다.")
            Transport.send(message)
        } catch (e: AuthenticationFailedException) {
            Log.d(TAG, "auth 실패했다 : ${e.localizedMessage}")
            e.printStackTrace()
            if (e.localizedMessage == null)
                Log.d(TAG, "e.localizedMessage가 null이라서 다 출력함 : \n" +
                        "sender : ${message.sender}\n" +
                        "messageID : ${message.messageID}\n" +
                        "subject : ${message.subject}\n" +
                        "content : ${message.content}\n" +
                        //"inputStream : ${message.rawInputStream}\n" + //여기서 에러 발생 : javax.mail.MessagingException: No content
                        "datahandler : ${message.dataHandler}\n" +
                        "all of recipients : ${message.allRecipients}\n" +
                        "encoding : ${message.encoding}\n" +
                        "replyTo : ${message.replyTo}\n" +
                        "size : ${message.size}\n" +
                        "description : ${message.description}\n" +
                        "from : ${message.from}")
            throw(e)
        } catch (e: Exception) {
            Log.d(TAG, "에러나온다 : ${e.localizedMessage}")
            throw(e)
        }
    }

    class Builder {
        private var senderId: String? = null
        private var senderPw: String? = null
        private var senderVisibleId: String? = null
        private var receiverId: String? = null
        private var ccReceiverId: String? = null
        private var bccReceiverId: String? = null
        private var subject: String? = null
        private var body: String? = null
        private var bodyType: String? = null

        fun build(): SendGmailManager? {
            if (senderVisibleId == null)
                senderVisibleId = senderId
            if (subject == null)
                subject = "제목 없음"
            if (body == null)
                body = "내용 없음"
            if (bodyType == null)
                bodyType = "text/plain"

            return SendGmailManager(
                    senderId = senderId,
                    senderPw = senderPw,
                    senderVisibleId = senderVisibleId,
                    receiverId = receiverId,
                    ccReceiverId = ccReceiverId,
                    bccReceiver = bccReceiverId,
                    subject = subject,
                    body = body,
                    bodyType = bodyType)
        }

        fun addSenderId(senderId: String): SendGmailManager.Builder {
            this.senderId = senderId
            return this
        }

        fun addSenderPw(senderPw: String): SendGmailManager.Builder {
            this.senderPw = senderPw
            return this
        }

        /**
         * 받는 입장에서 보이는 주소
         */
        fun addSenderVisibleId(senderVisibleId: String): SendGmailManager.Builder {
            this.senderVisibleId = senderVisibleId
            return this
        }

        /**
         * 받는 이
         * 콤마로 구분해서 받을 수 있음.
         */
        fun addReceiverId(receiverId: String): SendGmailManager.Builder {
            this.receiverId = receiverId
            return this
        }

        /**
         * 참조 받는 이
         * 콤마로 구분해서 받을 수 있음.
         */
        fun addCcReceiver(ccReceiverId: String): SendGmailManager.Builder {
            this.ccReceiverId = ccReceiverId
            return this
        }

        /**
         * 숨은 참조 받는 이
         * 콤마로 구분해서 받을 수 있음.
         */
        fun addBccReceiver(bccReceiverId: String): SendGmailManager.Builder {
            this.bccReceiverId = bccReceiverId
            return this
        }

        fun addSubject(subject: String): SendGmailManager.Builder {
            this.subject = subject
            return this
        }

        fun addBody(body: String): SendGmailManager.Builder {
            this.body = body
            return this
        }

        fun addBodyType(bodyType: String): SendGmailManager.Builder {
            this.bodyType = bodyType
            return this
        }
    }
}