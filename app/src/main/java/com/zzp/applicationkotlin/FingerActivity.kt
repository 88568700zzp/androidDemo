package com.zzp.applicationkotlin

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_WEAK
import androidx.biometric.BiometricManager.BIOMETRIC_SUCCESS
import androidx.biometric.BiometricPrompt
import androidx.core.hardware.fingerprint.FingerprintManagerCompat
import kotlinx.android.synthetic.main.activity_finger.*

class FingerActivity : AppCompatActivity(), View.OnClickListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_finger)




        finger.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v) {
            finger -> {
                if (Build.VERSION.SDK_INT <= 29) {
                    var fingerprintManager = FingerprintManagerCompat.from(this)
                    Log.d(
                        "zzp123",
                        "isHardwareDetected:${fingerprintManager.isHardwareDetected} hasEnrolledFingerprints:${fingerprintManager.hasEnrolledFingerprints()}"
                    )
                } else {
                    var biometricManager = BiometricManager.from(this)
                    if (BIOMETRIC_SUCCESS == biometricManager.canAuthenticate(BIOMETRIC_WEAK)) {
                        Log.d("zzp123", "biometricManager:BIOMETRIC_SUCCESS")
                        var biometricPrompt = BiometricPrompt(this,
                            object : BiometricPrompt.AuthenticationCallback() {
                                override fun onAuthenticationError(
                                    errorCode: Int,
                                    errString: CharSequence
                                ) {
                                    super.onAuthenticationError(errorCode, errString)
                                    Log.d("zzp123", "onAuthenticationError")
                                }

                                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                                    super.onAuthenticationSucceeded(result)
                                    Log.d("zzp123", "onAuthenticationSucceeded:${result.authenticationType} ${result.cryptoObject}")
                                }

                                override fun onAuthenticationFailed() {
                                    super.onAuthenticationFailed()
                                    Log.d("zzp123", "onAuthenticationFailed")
                                }
                            })
                        var promptInfo = BiometricPrompt.PromptInfo.Builder().setDescription("生物监测")
                            .setTitle("指纹识别").setNegativeButtonText("取消识别").build()
                        biometricPrompt.authenticate(promptInfo)
                    }
                }
            }
        }
    }
}