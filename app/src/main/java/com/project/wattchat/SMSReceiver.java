package com.project.wattchat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;

public class SMSReceiver extends BroadcastReceiver {
    /* SmsManager nesnesi oluşturulur.*/
    final SmsManager sms= SmsManager.getDefault();
    public void onReceive(Context context, Intent intent) {
        /* Cihaza bir mesaj geldiğinde bu metod tetiklenir.*/
        Bundle bundle=intent.getExtras();
        SmsMessage[] smsm=null;
        String sms_str="";

        if (bundle!=null) {
            /* SMS mesajı elde edilir.*/
            Object[] pdus=(Object[])bundle.get("pdus");
            smsm=new SmsMessage[pdus.length];
            for (int i=0; i<smsm.length; i++) {
                smsm[i]=SmsMessage.createFromPdu((byte[])pdus[i]);
                sms_str+=smsm[i].getMessageBody().toString();
            }
            try {
                /* Conversation sınıfındaki updateAdapter metoduna mesaj, "1" değeri (mesajın ekranda doğru tasarımla
                görüntülenmesi için) ve mesajı gönderen kişinin telefon numarası bilgisi gönderilir.
                 */
                Conversation.updateAdapter(sms_str, "1", smsm[0].getOriginatingAddress());
            } catch (Exception e){
            }
        }
    }
}
