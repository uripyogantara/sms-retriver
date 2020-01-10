package id.uripyogantara.smsretriver

import android.app.Activity
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.gms.auth.api.phone.SmsRetriever
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), SmsBroadcastReceiver.SmsBroadcastReceiverListener {
    lateinit var smsBroadcastReceiver: SmsBroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initAppSignature()
        startSmsUserConsent()
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(smsBroadcastReceiver)
    }

    private fun initAppSignature(){
        val appSignatureHelper = AppSignatureHelper(this)
        Log.i("Signature", "HashKey: " + appSignatureHelper.getAppSignatures()[0]);

    }
    private fun startSmsUserConsent() {
        registerToSmsBroadcastReceiver()
        SmsRetriever.getClient(this).also {
            //We can add sender phone number or leave it blank
            it.startSmsRetriever()
                .addOnSuccessListener {
                    tv_message.text = "Listening Success"
                }
                .addOnFailureListener {
                    tv_message.text = "Listening failure"
                }
        }
    }

    private fun registerToSmsBroadcastReceiver() {
        smsBroadcastReceiver = SmsBroadcastReceiver()

        smsBroadcastReceiver.setListener(this)

        val intentFilter = IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION)
        registerReceiver(smsBroadcastReceiver, intentFilter)
    }

    override fun onSuccess(otp: String) {
        tv_message.text = otp
    }

    override fun onFailure() {
        tv_message.text = "Failure"
    }
}
