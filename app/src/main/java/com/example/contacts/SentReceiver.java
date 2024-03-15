package com.example.contacts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import android.telephony.SmsManager;

public class SentReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        switch (getResultCode()) {
            case ActivitySendMessage.RESULT_OK:
                Toast.makeText(context, "Message envoyé avec succès", Toast.LENGTH_SHORT).show();
                break;
            case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                Toast.makeText(context, "Echec de l'envoi du message", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
