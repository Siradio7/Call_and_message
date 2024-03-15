package com.example.contacts;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private SentReceiver sentReceiver;
    private DeliveredReceiver deliveredReceiver;
    private Button button_call, button_send_sms;

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sentReceiver = new SentReceiver();
        deliveredReceiver = new DeliveredReceiver();

        registerReceiver(sentReceiver, new IntentFilter("SMS_SENT"), Context.RECEIVER_NOT_EXPORTED);
        registerReceiver(deliveredReceiver, new IntentFilter("SMS_DELIVERED"), Context.RECEIVER_NOT_EXPORTED);

        button_call = findViewById(R.id.button_call);
        button_send_sms = findViewById(R.id.button_sms);

        button_call.setOnClickListener(v -> {
            Intent intent = new Intent(this, ActivityCall.class);
            startActivity(intent);
        });

        button_send_sms.setOnClickListener(v -> {
            Intent intent = new Intent(this, ActivitySendMessage.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver(sentReceiver);
        unregisterReceiver(deliveredReceiver);
    }
}