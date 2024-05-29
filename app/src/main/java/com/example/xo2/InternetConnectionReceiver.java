package com.example.xo2;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class InternetConnectionReceiver extends BroadcastReceiver {

    // Manager for handling queries about the state of network connectivity
    ConnectivityManager connectivityManager;

    // Information about the current network connection
    NetworkInfo networkInfo;

    // Variable to store network status
    String status;

    // Constructor for the BroadcastReceiver
    public InternetConnectionReceiver() {
    }

    // Method to check the wifi status and apply dialog
    @Override
    public void onReceive(Context context, Intent intent) {
        // Get the connectivity manager to check network status
        connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getActiveNetworkInfo();

        // Check if there is an active network connection
        if (networkInfo != null && networkInfo.isConnected()) {
            // Check the type of network (Mobile or other)
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                status = "WiFi Network is enabled";
            } else {
                status = "No WiFi connection, play offline or fix it";
            }
        } else {
            // No active network connection
            status = "WiFi is not activated, play offline or fix it";
        }

        // Show a Toast message with the network status
        Toast.makeText(context, status, Toast.LENGTH_SHORT).show();

        // Create a dialog to display network status information
        final Dialog dialog = new Dialog(context);
        // Choose the layout based on the network status
        if ("No WiFi connection, play offline or fix it".equals(status) || "WiFi is not activated, play offline or fix it".equals(status)) {
            dialog.setContentView(R.layout.activity_alert_dialog);
        } else {
            dialog.setContentView(R.layout.activity_alert_dialog_wifi_on);
        }

        // Set dialog properties
        dialog.setCancelable(false);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().getAttributes().windowAnimations = android.R.style.Animation_Dialog;

        // Inflate and set up the dialog layout
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView;
        if ("No WiFi connection, play offline or fix it".equals(status) || "WiFi is not activated, play offline or fix it".equals(status)) {
            dialogView = inflater.inflate(R.layout.activity_alert_dialog, null);
        } else {
            dialogView = inflater.inflate(R.layout.activity_alert_dialog_wifi_on, null);
        }

        // Set the network status text in the dialog
        TextView tvStatus = dialogView.findViewById(R.id.tvStatus);
        tvStatus.setText(status);

        // Set up the "OK" button to dismiss the dialog
        Button bOk = dialogView.findViewById(R.id.bOk);
        if (bOk != null) {
            bOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        } else {
            Toast.makeText(context, "Button bOk not found", Toast.LENGTH_SHORT).show();
        }

        // Set up the "Play Offline" button to allow the player to play offline
        Button bOff = dialogView.findViewById(R.id.bOffline);
        if (bOff != null) {
            bOff.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    // Navigate to GameMenu activity
                    Intent intent = new Intent(context, GameMenu.class);
                    context.startActivity(intent);
                }
            });
        } else {
            Toast.makeText(context, "Button bOff not found", Toast.LENGTH_SHORT).show();
        }

        // Set the dialog view
        dialog.setContentView(dialogView);

        // Show the dialog
        dialog.show();
    }
}
