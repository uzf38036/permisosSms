package com.example.sms_permisos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;
    private static final int REQUEST_SEND_SMS = 0;
    EditText etPhoneNumber, etMessage;
    Button btnSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initReferences();
        setListenersToButtons();
    }

    private void initReferences(){
        etMessage = findViewById(R.id.etMessage);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        btnSend = findViewById(R.id.btnSend);
    }

    private void setListenersToButtons(){
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               checkSmsPermission();
            }
        });
    }

    //Method that sends the SMS checking permissions
    private void checkSmsPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, REQUEST_SEND_SMS);
                requestPermission(Manifest.permission.SEND_SMS, String.valueOf(R.string.explanation),
                        REQUEST_SEND_SMS, this);
        } else {
            sendSms();
        }
    }

//Method for requesting user permission
    private void requestPermission(final String permission, String explanation, final int requestCode, final Activity activity){
        if(ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)){
            new AlertDialog.Builder(activity)
                    .setTitle(R.string.requestPermTitle)
                    .setMessage(explanation)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(activity, new String[]{permission}, requestCode);
                        }
                    })
                    .show();
        } else {
            //request permission
            ActivityCompat.requestPermissions(activity, new String[]{permission}, requestCode);
        }
    }

    //Method which sends the SMS
    private void sendSms() {
        String phoneNumber= etPhoneNumber.getText().toString();
        String message = etMessage.getText().toString();
        if (!TextUtils.isEmpty(phoneNumber) && !TextUtils.isEmpty(message)) {
            // Use the SmsManager to send the SMS
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);

            // Display a toast to confirm that the SMS was sent
            Toast.makeText(this, R.string.sent_confirmation, Toast.LENGTH_SHORT).show();
        } else {
            // Display an error message if the phone number or message is empty
            Toast.makeText(this, R.string.empty_error_messg, Toast.LENGTH_SHORT).show();
        }
    }



    // This method is called when the user responds to the permission request & collects the permit

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_SEND_SMS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission has been granted, send the SMS
                sendSms();
            } else {
                // Permission was denied, display a message to the user
                Toast.makeText(this, R.string.alert_permission_denied, Toast.LENGTH_SHORT).show();
            }
        }
    }
}