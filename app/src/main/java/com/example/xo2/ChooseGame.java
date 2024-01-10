package com.example.xo2;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;

public class ChooseGame extends AppCompatActivity {

    ActivityResultLauncher<Intent> arlSmall;
    private DatabaseReference mDatabase;
    FirebaseHandler firebaseHandler;
    TextView name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_game);

        ActivityCompat.requestPermissions(ChooseGame.this,
                new String[]{android.Manifest.permission.CAMERA,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        name = findViewById(R.id.player_name);

        // Initialize FirebaseHandler after finding the views
        firebaseHandler = new FirebaseHandler(FirebaseAuth.getInstance(), this);

        arlSmall = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK) {
                            Intent data = result.getData();
                            if (data != null) {
                                // Get the image data
                                Bundle extras = data.getExtras();
                                if (extras != null) {
                                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                                    if (imageBitmap != null) {
                                        // Resize the image
                                        Bitmap smallImageBitmap = Bitmap.createScaledBitmap(imageBitmap, 100, 100, true);

                                        // Convert the Bitmap to a byte array
                                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                        smallImageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                                        byte[] imageData = baos.toByteArray();

                                        // Update the FirebaseHandler with the new photo
                                        firebaseHandler.updateUserPhoto(imageData);
                                    }
                                }
                            }
                        }
                    }
                });

        // Moved onDataChange block inside onCreate
        if (currentUser != null) {
            DatabaseReference playerRef = mDatabase.child("players").child(currentUser.getUid());
            playerRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // This method is called when the data at the specified database reference changes
                    String playerName = dataSnapshot.child("nickname").getValue(String.class);

                    // Now you have the player's name, and you can use it as needed
                    // For example, update a TextView with the player's name
                    name.setText("Welcome " + playerName); // Update the TextView with the welcome message
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle errors in reading data
                    Toast.makeText(ChooseGame.this, "Failed to retrieve player data", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.total, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.Photo) {
            // Launch the CapturePhotoActivity
            Intent intent = new Intent(this, activity_capture_photo.class);
            startActivity(intent);
        } else if (id == R.id.Nickname) {
            // Handle Nickname option
        } else if (id == R.id.Difficulty) {
            startGameOptionsActivity(); // Call the new method to start GameOptionsActivity
        } else if (id == R.id.Exit) {
            firebaseHandler.signOut();
        }
        return super.onOptionsItemSelected(item);
    }

    public void NewGame(View view) {
        // Start the appropriate game activity based on the selected difficulty
        Intent intent;

        // Get the chosen difficulty from the FirebaseHandler
        String difficulty = firebaseHandler.getSelectedDifficulty();

        if ("Difficulty Hard - Play against a bot".equals(difficulty)) {
            intent = new Intent(this, GameABot.class);
        } else if ("Difficulty Changing - Play against a real player".equals(difficulty)) {
            intent = new Intent(this, GameAPlayer.class);
        } else {
            // Handle default case or unexpected difficulty value
            return;
        }

        // Set the button text dynamically based on the selected difficulty
        Button startGameButton = findViewById(R.id.StartGame);
        startGameButton.setText("Start a new game - " + difficulty);

        startActivity(intent);
    }


    // New method to start GameOptionsActivity
    private void startGameOptionsActivity() {
        Intent intent = new Intent(this, GameOptionsActivity.class);

        // Pass the selected difficulty to the GameOptionsActivity
        String selectedDifficulty = firebaseHandler.getSelectedDifficulty();

        // If no difficulty is selected, set "Difficulty Hard - Play against a bot" as default
        if (selectedDifficulty == null) {
            selectedDifficulty = "Difficulty Hard - Play against a bot";
        }

        intent.putExtra("SELECTED_DIFFICULTY", selectedDifficulty);

        startActivity(intent);
    }
}
