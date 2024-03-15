package com.example.contacts;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Button;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private SentReceiver sentReceiver;
    private DeliveredReceiver deliveredReceiver;

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_SMS, Manifest.permission.READ_CONTACTS}, 0);
        } else {
            new Thread(this::readSMS).start();
        }

        sentReceiver = new SentReceiver();
        deliveredReceiver = new DeliveredReceiver();

        registerReceiver(sentReceiver, new IntentFilter("SMS_SENT"), Context.RECEIVER_NOT_EXPORTED);
        registerReceiver(deliveredReceiver, new IntentFilter("SMS_DELIVERED"), Context.RECEIVER_NOT_EXPORTED);

        Button button_call = findViewById(R.id.button_call);
        Button button_send_sms = findViewById(R.id.button_sms);

        button_call.setOnClickListener(v -> {
            Intent intent = new Intent(this, ActivityCall.class);
            startActivity(intent);
        });

        button_send_sms.setOnClickListener(v -> {
            Intent intent = new Intent(this, ActivitySendMessage.class);
            startActivity(intent);
        });
    }

    @SuppressLint("Range")
    private void readSMS() {
        Uri uri = Uri.parse("content://sms/inbox");
        Cursor cursor = getContentResolver().query(uri, null, null, null, "date DESC");

        if (cursor != null && cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String address = cursor.getString(cursor.getColumnIndex("address"));
                String body = cursor.getString(cursor.getColumnIndex("body"));
                long date = cursor.getLong(cursor.getColumnIndex("date"));

                String contactName = getContactNameFromNumber(address);

                Log.d("SMS", "Name: " + contactName + ", Body: " + body + ", Date: " + new Date(date));
            } while (cursor.moveToNext());
            cursor.close();
        }
    }

    @SuppressLint("Range")
    private String getContactNameFromNumber(String phoneNumber) {
        String contactName = null;
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
        Cursor cursor = getContentResolver().query(uri, new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
            cursor.close();
        }

        return contactName;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver(sentReceiver);
        unregisterReceiver(deliveredReceiver);
    }
}