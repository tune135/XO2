package com.example.xo2;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.io.ByteArrayOutputStream;


public class activity_capture_photo extends AppCompatActivity {

    // Request code for capturing an image
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    // Firebase authentication instance
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture_photo);

        // Initialize capture button and set its click listener
        Button btnCapture = findViewById(R.id.btnCapture);
        btnCapture.setOnClickListener(v -> dispatchTakePictureIntent());

        // Initialize Firebase authentication
        auth = FirebaseAuth.getInstance();

        // Check if camera permission is granted
        if (checkCameraPermission()) {
            // Initialize the camera or any other setup
        } else {
            // Request camera permission if not granted
            requestCameraPermission();
        }
    }

    // Check if camera permission is granted
    private boolean checkCameraPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    // Request camera permission
    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
    }

    // Start the camera intent to capture a photo
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    // Override onRequestPermissionsResult to handle the result of the permission request
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Check if the permission request is for camera and if the permission is granted
        if (requestCode == 1 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Start the camera intent after the permission is granted
            dispatchTakePictureIntent();
        } else {
            // Display a message or take appropriate action if the user denies the permission
            Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check if the captured image is obtained successfully
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK && data != null) {
            Bundle extras = data.getExtras();
            if (extras != null) {
                // Get the captured image as a Bitmap
                Bitmap imageBitmap = (Bitmap) extras.get("data");

                // Convert the Bitmap to a byte array
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                if (imageBitmap != null) {
                    imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                    byte[] imageData = byteArrayOutputStream.toByteArray();

                    // Save the image data to Firebase or perform any other action
                    savePicture(imageData);

                    // Display a toast message indicating the success or failure of the operation
                    Toast.makeText(this, "Photo saved successfully", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(this, ChooseGame.class);
                    startActivity(intent);
                }
            }
        }
    }

    // Save the captured image data to Firebase or perform other actions
    private void savePicture(byte[] imageData) {
        FirebaseHandler firebaseHandler = new FirebaseHandler(auth, getApplicationContext());
        firebaseHandler.updateUserPhoto(imageData);
    }
}
