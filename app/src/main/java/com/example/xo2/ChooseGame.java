//package com.example.xo2;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.os.Bundle;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.widget.Button;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AlertDialog;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//
//public class ChooseGame extends AppCompatActivity {
//
//    private Button startGameButton;
//    private TextView winsTextView, lossesTextView, drawsTextView;
//    private FirebaseHandler firebaseHandler;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_choose_game);
//
//        // Initialize Firebase handler
//        firebaseHandler = new FirebaseHandler(FirebaseAuth.getInstance(), this);
//
//        // Get references to UI elements
//        startGameButton = findViewById(R.id.startGameButton);
//        winsTextView = findViewById(R.id.winsTextView);
//        lossesTextView = findViewById(R.id.lossesTextView);
//        drawsTextView = findViewById(R.id.drawsTextView);
//
//        // Set click listener for start game button
//        startGameButton.setOnClickListener(v -> {
//            // Get the selected game activity based on a preference or selection
//            String selectedGameActivity = getSelectedGameActivity(); // Implement logic to get the selected game activity
//            Intent intent;
//            if (selectedGameActivity != null && selectedGameActivity.equals("GameAPlayer")) {
//                intent = new Intent(ChooseGame.this, GameAPlayer.class);
//            } else {
//                intent = new Intent(ChooseGame.this, GameABot20.class);
//            }
//            startActivity(intent);
//        });
//
//        // Fetch player data from Firebase
//        fetchPlayerData();
//    }
//
//    // Fetch player data from Firebase and update UI
//    private void fetchPlayerData() {
//        firebaseHandler.getPlayerData(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists()) {
//                    Player player = dataSnapshot.getValue(Player.class);
//                    if (player != null) {
//                        // Assuming getGameStatistics() is properly implemented in the Player class
//                        //GameStatistics gameStatistics = player.getGameStatistics();
////                        if (gameStatistics != null) {
////                            winsTextView.setText(String.valueOf(gameStatistics.getWins()));
////                            lossesTextView.setText(String.valueOf(gameStatistics.getLosses()));
////                            drawsTextView.setText(String.valueOf(gameStatistics.getDraws()));
////                        } else {
//                            // Handle case where game statistics are not available
//                            winsTextView.setText("0");
//                            lossesTextView.setText("0");
//                            drawsTextView.setText("0");
//                        }
//                    }
//                }
//            }
//
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                // Handle errors in reading data
//                Toast.makeText(ChooseGame.this, "Failed to retrieve player data", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    // Implement a method to get the selected game activity based on your preference logic
//    private String getSelectedGameActivity() {
//        // Replace this with your logic to determine the selected game activity
//        // You can use Firebase or SharedPreferences, or any other suitable method
//        // Here's an example using SharedPreferences (assuming you have a preference named "selected_game")
//        SharedPreferences preferences = getSharedPreferences("game_preferences", MODE_PRIVATE);
//        return preferences.getString("selected_game", "GameAPlayer"); // Default value
//    }
//
//    // Implement menu functionality (optional)
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.total, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//
//        switch (id) {
//            case R.id.Rules:
//                GameRules();
//                return true;
//            case R.id.Difficulty:
//                showDifficultyDialog();
//                return true;
//            case R.id.Exit:
//                exitApp();
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }
//
//    private void GameRules() {
//        Intent intent = new Intent(this, GameRules.class);
//        startActivity(intent);
//    }
//
//    private void showDifficultyDialog() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("Select Difficulty");
//        String[] difficulties = {"Easy", "Medium", "Hard"};
//        builder.setItems(difficulties, (dialog, which) -> {
//            // Handle difficulty selection
//            Toast.makeText(this, "Selected: " + difficulties[which], Toast.LENGTH_SHORT).show();
//        });
//        builder.show();
//    }
//
//    private void exitApp() {
//        finishAffinity(); // Close all activities and exit app for Android 4.1 and higher
//    }
//
//}
