package id.uripyogantara.smsretriver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status

class SmsBroadcastReceiver : BroadcastReceiver() {

    private var smsBroadcastReceiverListener: SmsBroadcastReceiverListener? = null

    override fun onReceive(context: Context?, intent: Intent?) {

        if (intent?.action == SmsRetriever.SMS_RETRIEVED_ACTION) {

            val extras = intent.extras
            val smsRetrieverStatus = extras?.get(SmsRetriever.EXTRA_STATUS) as Status

            when (smsRetrieverStatus.statusCode) {
                CommonStatusCodes.SUCCESS -> {
                    val message : String = extras.get(SmsRetriever.EXTRA_SMS_MESSAGE) as String
                    val otp = fetchVerificationCode(message)
                    smsBroadcastReceiverListener?.onSuccess(otp)
                }

                CommonStatusCodes.TIMEOUT -> {
                    smsBroadcastReceiverListener?.onFailure()
                }
            }
        }
    }

    fun setListener(listener: SmsBroadcastReceiverListener){
        this.smsBroadcastReceiverListener=listener
    }

    private fun fetchVerificationCode(message: String): String {
        return Regex("(\\d{6})").find(message)?.value ?: ""
    }

    interface SmsBroadcastReceiverListener {
        fun onSuccess(otp:String)
        fun onFailure()
    }
}