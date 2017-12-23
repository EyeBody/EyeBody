package com.example.android.eyebody

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat
import android.support.v4.os.CancellationSignal
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.example.android.eyebody.management.ManagementActivity
import com.example.android.eyebody.management.config.subcontent.callee.content.FindPasswordActivity
import kotlinx.android.synthetic.main.activity_setting.*
import java.security.*
import com.example.android.eyebody.utility.MyFingerPrintManager

// 임시 UI 만드는 곳
// 비번입력하고 SchedulerActivity로 넘어감.
class SettingActivity : AppCompatActivity() {

    val TAG = "mydbg_setting"
    val cancelSignal = CancellationSignal()

    //-------- 조금만 참아 ---------------------------------------
    val KEY_NAME = "my_key"
    var mKeyStore: KeyStore? = null
    var mKeyPairGenerator: KeyPairGenerator? = null
    var mSignature: Signature? = null
    //-------- 조금만 참아 ---------------------------------------

    override fun onStop() {
        super.onStop()
        if (!cancelSignal.isCanceled)
            cancelSignal.cancel()
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        Log.d(TAG, "setting 진입")

        val initSharedPref: SharedPreferences = getSharedPreferences(getString(R.string.getSharedPreference_initSetting), Context.MODE_PRIVATE)
        val sharedPref_hashedPW = initSharedPref.getString(getString(R.string.sharedPreference_hashedPW), getString(R.string.sharedPreference_default_hashedPW))

        val configSharedPref = getSharedPreferences(getString(R.string.getSharedPreference_configuration_Only_Int), Context.MODE_PRIVATE)
        val isUserSetCanFingerPrintAuth = configSharedPref.getInt(getString(R.string.sharedPreference_Security_Use_Fingerprint), 0) == 1

        // 키보드-확인 눌렀을 때 반응
        setting_input_pw.setOnEditorActionListener { textView, i, keyEvent ->
            Log.d(TAG, "키보드-확인 누름")

            // EditText 친 부분 MD5 암호화
            val md5 = MessageDigest.getInstance("MD5")
            val str_inputPW: String = setting_input_pw.text.toString()
            val pwByte: ByteArray = str_inputPW.toByteArray(charset("unicode"))
            md5.update(pwByte)
            val hashedPW = md5.digest().toString(charset("unicode"))

            // 공유변수와 일치여부 검증
            Log.d(TAG, "prePW : $sharedPref_hashedPW / PW : $hashedPW")
            if (hashedPW == sharedPref_hashedPW.toString()) {
                val schedulerPage = Intent(this, ManagementActivity::class.java)
                if (!cancelSignal.isCanceled)
                    cancelSignal.cancel()
                startActivity(schedulerPage)
                finish()
            } else {
                Toast.makeText(this, "관계자 외 출입금지~", Toast.LENGTH_LONG).show()
            }
            true
        }

        // password 찾기/수정 액티비티
        button_explore_password.setOnClickListener {
            Log.d(TAG, "explore password")
            val mIntent = Intent(this, FindPasswordActivity::class.java)
            startActivity(mIntent)
        }

        // 지문인식
        if (isUserSetCanFingerPrintAuth && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val osFpManager = FingerprintManagerCompat.from(this)
            if (osFpManager.isHardwareDetected) {
                setting_image_fingerprint.setImageDrawable(getDrawable(R.drawable.icons8fingerprint))
                setting_text_pw_guide.text = "지문 또는 비밀번호를 입력하세요."

                /*
                // --------------------------------------------------------------------------------
                // ------- signature 사용해보기 ---------------------------------------------------
                // --------------------------------------------------------------------------------
                                @RequiresApi(Build.VERSION_CODES.M)
                                fun createKeyPair() {
                                    try {
                                        // Set the alias of the entry in Android KeyStore where the key will appear
                                        // and the constrains (purposes) in the constructor of the Builder
                                        mKeyPairGenerator?.initialize(
                                                KeyGenParameterSpec.Builder(KEY_NAME,
                                                        KeyProperties.PURPOSE_SIGN)
                                                        .setDigests(KeyProperties.DIGEST_SHA256)
                                                        .setAlgorithmParameterSpec(ECGenParameterSpec("secp256r1"))
                                                        .setUserAuthenticationRequired(true)
                                                        .build())
                                        mKeyPairGenerator?.generateKeyPair()
                                    } catch (e: InvalidAlgorithmParameterException) {
                                        throw RuntimeException(e)
                                    }

                                }

                                @RequiresApi(Build.VERSION_CODES.M)
                                fun initSignature(): Boolean {
                                    try {
                                        mKeyStore?.load(null)
                                        val key = mKeyStore?.getKey(KEY_NAME, null) as PrivateKey
                                        mSignature?.initSign(key)
                                        return true
                                    } catch (e: KeyPermanentlyInvalidatedException) {
                                        return false
                                    } catch (e: Exception) {
                                        throw RuntimeException("Failed to init Cipher : ${e.localizedMessage}", e)
                                    }

                                }
                // --------------------------------------------------------------------------------
                // 비대칭키를 생성한다.
                val kpg = KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_EC, "AndroidKeyStore")
                kpg.initialize(
                        KeyGenParameterSpec.Builder("key1",
                                KeyProperties.PURPOSE_SIGN)
                                .setDigests(KeyProperties.DIGEST_SHA256)
                                .setAlgorithmParameterSpec(ECGenParameterSpec("secp256r1"))
                                .setUserAuthenticationRequired(true)
                                .build())
                kpg.generateKeyPair()

                // setUserAuthenticationRequired true 하면 등록한 상태에서만 하겠다는거고 이러면 대칭키를 만들어서 활용해야함?
                val keyStore1 = KeyStore.getInstance("AndroidKeyStore")
                keyStore1.load(null)
                val publicKey = keyStore1.getCertificate("Public Key").publicKey

                val keyStore2 = KeyStore.getInstance("AndroidKeyStore")
                keyStore2.load(null)
                val privateKey = keyStore2.getKey("Private Key", null) as PrivateKey

                val signature = Signature.getInstance("SHA256withECDSA")
                val keyStore = KeyStore.getInstance("AndroidKeyStore")
                keyStore.load(null)
                signature.initSign(privateKey)
                val cryptObject = FingerprintManagerCompat.CryptoObject(signature)
                // --------------------------------------------------------------------------------
                // --------------------------------------------------------------------------------


                // --------------------------------------------------------------------------------
                // ---------- cipher 사용해보기 ---------------------------------------------------
                // --------------------------------------------------------------------------------
                val KEY_NAME = "머라고써야돼는거야!!"
                val KEY_BYTE = KEY_NAME.toByteArray()

                // 임의의 키를 만든다. 앱별로 만들어짐. 해시처럼 비대칭키인것같다. 근데 rsa로 해버리기?
                val keyGenerator = KeyGenerator.getInstance("HmacSHA256")
                keyGenerator
                        .init(KeyGenParameterSpec.Builder(KEY_NAME, KeyProperties.PURPOSE_ENCRYPT.or(KeyProperties.PURPOSE_DECRYPT))
                                .setBlockModes(KeyProperties.BLOCK_MODE_ECB)
                                .setUserAuthenticationRequired(true)
                                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_OAEP)
                                .build())
                keyGenerator.generateKey()

                // 지문인식에 enc, dec를 해주는 보안 모듈?? 사이퍼를 위 키에 맞춰 만든다.
                val cipher = Cipher.getInstance("${KeyProperties.KEY_ALGORITHM_RSA}/${KeyProperties.BLOCK_MODE_ECB}/${KeyProperties.ENCRYPTION_PADDING_RSA_OAEP}")
                val keyStore = KeyStore.getInstance(KeyProperties.KEY_ALGORITHM_RSA)
                keyStore.load(null)
                cipher.init(Cipher.ENCRYPT_MODE, keyStore.getKey(KEY_NAME, null))
                val crypto: FingerprintManagerCompat.CryptoObject = FingerprintManagerCompat.CryptoObject(cipher)
                // --------------------------------------------------------------------------------
                // --------------------------------------------------------------------------------
                */

                //createKeyPair()
                if (true/*initSignature()*/) {
                    //val cryptObject = FingerprintManagerCompat.CryptoObject(mSignature)
                    val myFpManager = object : MyFingerPrintManager(osFpManager, this, null, 0, cancelSignal, null) {

                        override fun onAuthenticationSucceeded(result: FingerprintManagerCompat.AuthenticationResult?) {
                            super.onAuthenticationSucceeded(result)
                            val schedulerPage = Intent(baseContext, ManagementActivity::class.java)
                            startActivity(schedulerPage)
                            finish()
                        }

                        override fun afterLockout() {
                            runOnUiThread {
                                setting_image_fingerprint.setImageDrawable(getDrawable(R.drawable.icons8privacy))
                                setting_text_pw_guide.text = "비밀번호를 입력하세요. (지문인식은 30초 뒤 접근 가능합니다.)"
                            }
                        }

                        override fun afterLockOutRelease() {
                            runOnUiThread {
                                setting_image_fingerprint.setImageDrawable(getDrawable(R.drawable.icons8fingerprint))
                                setting_text_pw_guide.text = "지문 또는 비밀번호를 입력하세요."
                            }
                        }

                        override fun afterNoSpace() {
                            runOnUiThread {
                                setting_image_fingerprint.setImageDrawable(getDrawable(R.drawable.icons8privacy))
                                setting_text_pw_guide.text = "비밀번호를 입력하세요. (저장공간이 부족하여 지문인식이 불가능합니다.)"
                            }
                        }

                        override fun afterUnAvailableError() {
                            runOnUiThread {
                                setting_image_fingerprint.setImageDrawable(getDrawable(R.drawable.icons8privacy))
                                setting_text_pw_guide.text = "비밀번호를 입력하세요."
                            }
                        }
                    }

                    myFpManager.startAuthenticate()
                }

            } else {
                setting_image_fingerprint.setImageDrawable(getDrawable(R.drawable.icons8privacy))
                if (osFpManager.hasEnrolledFingerprints()) {
                    // 등록을 안함.
                    setting_text_pw_guide.text = "비밀번호를 입력하세요.\n(지문을 등록하지 않아 지문인식을 이용하실 수 없습니다.)"
                } else {
                    // 디바이스 미지원
                    setting_text_pw_guide.text = "비밀번호를 입력하세요."
                }
            }
        } else {
            setting_image_fingerprint.setImageDrawable(getDrawable(R.drawable.icons8privacy))
            setting_text_pw_guide.text = "비밀번호를 입력하세요."
        }

    }
}
