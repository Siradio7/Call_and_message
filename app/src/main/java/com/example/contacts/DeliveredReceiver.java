package com.example.contacts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class DeliveredReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        switch (getResultCode()) {
            case ActivitySendMessage.RESULT_OK:
                Toast.makeText(context, "Message livré avec succès", Toast.LENGTH_SHORT).show();
                break;
            case ActivitySendMessage.RESULT_CANCELED:
                Toast.makeText(context, "Echec de la livraison du code", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
