package com.example.xo2;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ChangeNicknameActivity extends Activity {
    private EditText nicknameEditText;
    private Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_nickname);

        nicknameEditText = findViewById(R.id.nicknameEditText);
        submitButton = findViewById(R.id.submitButton);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newNickname = nicknameEditText.getText().toString();
                if (!newNickname.isEmpty()) {
                    // Logic to update the nickname in your database or preference
                    Toast.makeText(ChangeNicknameActivity.this, "Nickname updated!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ChangeNicknameActivity.this, "Nickname cannot be empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}