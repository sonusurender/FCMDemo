package com.androhub.fcmdemo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private TextView tvToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvToken = findViewById(R.id.printToken);
        handleNotificationData();
    }

    /**
     * method to get the token manually
     *
     * @param view
     */
    public void getToken(View view) {
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {

                if (!task.isSuccessful()) {
                    Log.e(TAG, "Failed to get the token.");
                    return;
                }

                //get the token from task
                String token = task.getResult();

                Log.d(TAG, "Token : " + token);
                tvToken.setText(token);


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "Failed to get the token : " + e.getLocalizedMessage());
            }
        });
    }

    /**
     * method to handle the data content on clicking of notification if both notification and data payload are sent
     */
    private void handleNotificationData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey("data1")) {
                Log.d(TAG, "Data1 : " + bundle.getString("data1"));
            }
            if (bundle.containsKey("data2")) {
                Log.d(TAG, "Data2 : " + bundle.getString("data2"));
            }

        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d(TAG, "On New Intent called");
    }

    public void subsSports(View view) {
        subscribeToTopic("sports");
    }

    public void unsubsSports(View view) {
        unsubscribeToTopic("sports");
    }

    public void subsEnt(View view) {
        subscribeToTopic("entertainment");
    }

    public void unsubsEnt(View view) {
        unsubscribeToTopic("entertainment");
    }

    /**
     * method to subscribe to topic
     *
     * @param topic to which subscribe
     */
    private void subscribeToTopic(String topic) {
        FirebaseMessaging.getInstance().subscribeToTopic(topic).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(MainActivity.this, "Subscribed to " + topic, Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, "Failed to subscribe", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * method to unsubscribe to topic
     *
     * @param topic to which unsubscribe
     */
    private void unsubscribeToTopic(String topic) {
        FirebaseMessaging.getInstance().unsubscribeFromTopic(topic).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(MainActivity.this, "UnSubscribed to " + topic, Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, "Failed to unsubscribe", Toast.LENGTH_SHORT).show();
            }
        });
    }
}