package com.heniktechnology.callremider.reciver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import com.heniktechnology.callremider.constants.Constants;
import com.heniktechnology.hncore.utility.HNLoger;

/**
 * Created by NikhilNagpure on 31-05-2017.
 */

public class Receiver extends BroadcastReceiver {
    String TAG = BroadcastReceiver.class.getSimpleName();


    @Override
    public void onReceive(Context context, Intent intent) {
        try {

            //SMS Reading
            if (intent.getAction().equals(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)) {
                Bundle intentExtras = intent.getExtras();
                if (intentExtras != null) {
                    Object[] sms = (Object[]) intentExtras.get(Constants.SMS_FORMAT);
                    if (sms != null) {
                        for (int i = 0; i < sms.length; ++i)
                        {
                            SmsMessage smsMessage = getIncomingMessage(sms[i],intentExtras);
                            String address = smsMessage.getOriginatingAddress();

                            HNLoger.debug(TAG,"address : "+address + HNLoger.Constants.NEW_LINE_SEPERATOR
                                    +"smsMessage.getDisplayMessageBody() :"+smsMessage.getDisplayMessageBody()+HNLoger.Constants.NEW_LINE_SEPERATOR
                                    +"smsMessage.getMessageBody() :"+smsMessage.getMessageBody()+HNLoger.Constants.NEW_LINE_SEPERATOR
                                    +"smsMessage.IndexOnIcc() :"+smsMessage.getIndexOnIcc()+HNLoger.Constants.NEW_LINE_SEPERATOR
                                    +"smsMessage.getTimestampMillis() :"+smsMessage.getTimestampMillis()+HNLoger.Constants.NEW_LINE_SEPERATOR
                                    +"smsMessage.getDisplayOriginatingAddress() :"+smsMessage.getDisplayOriginatingAddress()+HNLoger.Constants.NEW_LINE_SEPERATOR

                            );

                        }
                    }
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    private SmsMessage getIncomingMessage(Object object, Bundle bundle) {
        SmsMessage currentSMS;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String format = bundle.getString("format");
            currentSMS = SmsMessage.createFromPdu((byte[]) object, format);
        } else {
            currentSMS = SmsMessage.createFromPdu((byte[]) object);
        }
        return currentSMS;
    }
}
