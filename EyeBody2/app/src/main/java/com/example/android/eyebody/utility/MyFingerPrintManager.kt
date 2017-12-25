package com.example.android.eyebody.utility

import android.hardware.fingerprint.FingerprintManager
import android.os.Handler
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat
import android.support.v4.os.CancellationSignal
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import java.util.*

/**
 * Created by YOON on 2017-12-09
 */
open class MyFingerPrintManager(val fingerPrintManagerCompat: FingerprintManagerCompat, val appCompatActivity: AppCompatActivity,
                                val cryptoObject: FingerprintManagerCompat.CryptoObject?, val flags: Int, val cancelSignal: CancellationSignal, val handler: Handler?)
    : FingerprintManagerCompat.AuthenticationCallback() {

    val TAG = "mydbg_fingerprint"
    var isLockOut = false
    val context = appCompatActivity.baseContext!!

    fun startAuthenticate() {
        fingerPrintManagerCompat.authenticate(cryptoObject, flags, cancelSignal, this, handler)
    }

    open fun afterLockout() {}

    open fun afterLockOutRelease() {}

    open fun afterNoSpace() {}

    open fun afterUnAvailableError() {}

    override fun onAuthenticationSucceeded(result: FingerprintManagerCompat.AuthenticationResult?) {
        super.onAuthenticationSucceeded(result)
        //result는 crypto object를 위해 존재함.
        Log.d(TAG, "지문인식 성공")
    }

    override fun onAuthenticationFailed() {
        super.onAuthenticationFailed()
        Toast.makeText(context, "올바른 지문으로 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
        startAuthenticate()
    }

    override fun onAuthenticationError(errMsgId: Int, errString: CharSequence?) {
        super.onAuthenticationError(errMsgId, errString)

        when (errMsgId) {
            FingerprintManager.FINGERPRINT_ERROR_CANCELED -> {
                if (!cancelSignal.isCanceled) {
                    // TODO 만약에 처음에 알 수 없는 것에 대한 정보 제공을 동의하면 이메일로 나한테 보내지게 하거나 파이어베이스로 보내게 하기.
                    Log.d(TAG, "cancelSignal is not canceled but fingerprint canceled. : $errMsgId : $errString")
                } else {
                    // 정상적인 취소
                    Log.d(TAG, errString.toString())
                }
            }
            FingerprintManager.FINGERPRINT_ERROR_HW_UNAVAILABLE -> {
                Toast.makeText(context, "에러 : 지문인식 센서의 문제로 지문인식이 불가능합니다.", Toast.LENGTH_SHORT).show()
                Log.d(TAG, errString.toString())
            }
            FingerprintManager.FINGERPRINT_ERROR_LOCKOUT -> {
                // TODO 타이머 출력 30초
                if (!isLockOut) {
                    isLockOut = true
                    Toast.makeText(context, "안드로이드 내부 정책 상 지문인식 센서는 30초에 5번만 작동합니다. 30초 뒤에 활성화됩니다.", Toast.LENGTH_SHORT).show()
                    afterLockout()
                }
                Log.d(TAG, errString.toString())

                Handler().postDelayed({
                    if (!cancelSignal.isCanceled) {
                        isLockOut = false
                        appCompatActivity.runOnUiThread {
                            afterLockOutRelease()
                            Log.d(TAG, "30초 뒤 lock-out 풀렸음.")
                            startAuthenticate()
                        }
                    }
                },30001)

            }
            FingerprintManager.FINGERPRINT_ERROR_NO_SPACE -> {
                Toast.makeText(context, "지문센서를 위한 저장공간이 부족합니다.", Toast.LENGTH_SHORT).show()
                afterNoSpace()
                Log.d(TAG, errString.toString())
            }
            FingerprintManager.FINGERPRINT_ERROR_TIMEOUT -> {
                Log.d(TAG, errString.toString())
                startAuthenticate()
            }
            FingerprintManager.FINGERPRINT_ERROR_UNABLE_TO_PROCESS -> {
                Toast.makeText(context, "지문센서가 현재 지문을 식별하지 못합니다.", Toast.LENGTH_SHORT).show()
                Log.d(TAG, errString.toString())
            }
            else -> {
                // TODO 만약에 처음에 알 수 없는 것에 대한 정보 제공을 동의하면 이메일로 나한테 보내지게 하거나 파이어베이스로 보내게 하기.
                Log.d(TAG, "알 수 없는 이유로 지문인식이 종료되었습니다.")
                afterUnAvailableError()
                Toast.makeText(context, "해결할 수 없는 지문인식 오류가 발생하였습니다.", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onAuthenticationHelp(helpMsgId: Int, helpString: CharSequence?) {
        super.onAuthenticationHelp(helpMsgId, helpString)
        if (!cancelSignal.isCanceled) {
            when (helpMsgId) {
                FingerprintManager.FINGERPRINT_ACQUIRED_IMAGER_DIRTY -> {
                    Toast.makeText(context, "지문 센서 위 이물질을 제거한 뒤 다시 시도하세요.", Toast.LENGTH_SHORT).show()
                    Log.d(TAG, helpString.toString())
                }
                FingerprintManager.FINGERPRINT_ACQUIRED_INSUFFICIENT -> {
                    Toast.makeText(context, "지문 센서 위 이물질을 제거한 뒤 다시 시도하세요.", Toast.LENGTH_SHORT).show()
                    Log.d(TAG, helpString.toString())
                }
                FingerprintManager.FINGERPRINT_ACQUIRED_PARTIAL -> {
                    Toast.makeText(context, "지문 전체가 인식되도록 손가락을 지문 센서 위에 정확히 올리세요.", Toast.LENGTH_SHORT).show()
                    Log.d(TAG, helpString.toString())
                }
                FingerprintManager.FINGERPRINT_ACQUIRED_TOO_FAST -> {
                    Toast.makeText(context, "지문이 정확하게 인식될 수 있도록 조금 더 길게 올렸다가 떼세요.", Toast.LENGTH_SHORT).show()
                    Log.d(TAG, helpString.toString())
                }
                FingerprintManager.FINGERPRINT_ACQUIRED_TOO_SLOW -> {
                    Toast.makeText(context, helpString, Toast.LENGTH_SHORT).show()
                    Log.d(TAG, helpString.toString())
                }
            }
        } else {
            // TODO 만약에 처음에 알 수 없는 것에 대한 정보 제공을 동의하면 이메일로 나한테 보내지게 하거나 파이어베이스로 보내게 하기.
            Log.d(TAG, "지문이 취소되었는데 Help가 뜸 : $helpMsgId : $helpString")
        }
    }
}