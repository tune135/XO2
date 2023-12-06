package com.example.xo2;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class InternetConnectionReceiver extends BroadcastReceiver {

    // Constructor for the BroadcastReceiver
    public InternetConnectionReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // Get the connectivity manager to check network status
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        // Variable to store network status
        String status;

        // Check if there is an active network connection
        if (networkInfo != null && networkInfo.isConnected()) {
            // Check the type of network (Mobile or other)
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                status = "WiFi Network is enabled";
            } else {
                status = "No WiFi connection";
            }
        } else {
            // No active network connection
            status = "WiFi is not activated";
        }

        // Show a Toast message with the network status
        Toast.makeText(context, status, Toast.LENGTH_SHORT).show();

        // Create a dialog to display network status information
        final Dialog dialog;
        // Choose the layout based on the network status
        if ("No WiFi connection".equals(status) || "WiFi is not activated".equals(status)) {
            dialog = new Dialog(context);
            dialog.setContentView(R.layout.activity_alert_dialog);
        } else {
            dialog = new Dialog(context);
            dialog.setContentView(R.layout.activity_alert_dialog_wifi_on);
        }

        // Set dialog properties
        dialog.setCancelable(false);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().getAttributes().windowAnimations = android.R.style.Animation_Dialog;

        // Set the network status text in the dialog
        TextView tvStatus = dialog.findViewById(R.id.tvStatus);
        tvStatus.setText(status);

        // Set up the "OK" button to dismiss the dialog
        Button bOk = dialog.findViewById(R.id.bOk);
        bOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        // Show the dialog
        dialog.show();
    }
}
