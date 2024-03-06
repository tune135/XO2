package com.example.xo2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;


public class OnlineCode extends AppCompatActivity {

    TextView headTV;
    EditText codeEdt;
    Button createCodeButton;
    Button joinCodeButton;
    ProgressBar loadingPB;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_code);

        headTV = findViewById(R.id.idTVHead);
        codeEdt = findViewById(R.id.idEdtCode);
        createCodeButton = findViewById(R.id.idBtnCreate);
        joinCodeButton = findViewById(R.id.idBtnJoin);
        loadingPB = findViewById(R.id.idPbLoading);

    }

    public void Join(View view) {
        Constants.code = codeEdt.getText().toString().trim();
        if (!Constants.code.isEmpty()) {
            createCodeButton.setVisibility(View.GONE);
            joinCodeButton.setVisibility(View.GONE);
            codeEdt.setVisibility(View.GONE);
            headTV.setVisibility(View.GONE);
            loadingPB.setVisibility(View.VISIBLE);
            Constants.isCodeMaker = false;
            FirebaseDatabase.getInstance().getReference().child("Codes").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if (isValueAvailable(snapshot, Constants.code)) {
                        showViewsAfterLoading();
                    } else {
                        FirebaseDatabase.getInstance().getReference().child("Codes").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot snapshot) {
                                boolean data = isValueAvailable(snapshot, Constants.code);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (data) {
                                            Constants.codeFound = true;
                                            Accepted();
                                        } else {
                                            showViewsAfterLoading();
                                            Toast.makeText(OnlineCode.this, "Invalid Code", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }, 0);
                            }

                            @Override
                            public void onCancelled(DatabaseError error) {
                                Toast.makeText(OnlineCode.this, "Database error occurred", Toast.LENGTH_SHORT).show();
                                showViewsAfterLoading();
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    Toast.makeText(OnlineCode.this, "Database error occurred", Toast.LENGTH_SHORT).show();
                    showViewsAfterLoading();
                }
            });
        } else {
            Toast.makeText(this, "Please enter a valid code", Toast.LENGTH_SHORT).show();
        }
    }

    public void Create(View view) {
        String code = codeEdt.getText().toString().trim();
        if (!code.isEmpty()) {
            createCodeButton.setVisibility(View.GONE);
            joinCodeButton.setVisibility(View.GONE);
            headTV.setVisibility(View.GONE);
            codeEdt.setVisibility(View.GONE);
            loadingPB.setVisibility(View.VISIBLE);

            FirebaseDatabase.getInstance().getReference().child("Codes").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if (isValueAvailable(snapshot, code)) {
                        showViewsAfterLoading();
                    } else {
                        FirebaseDatabase.getInstance().getReference().child("Codes").push().setValue(code)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            showViewsAfterLoading();
                                            Accepted();
                                            Toast.makeText(OnlineCode.this, "Please don't go back", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(OnlineCode.this, "Failed to create code. Please try again.", Toast.LENGTH_SHORT).show();
                                            showViewsAfterLoading();
                                        }
                                    }
                                });
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    Toast.makeText(OnlineCode.this, "Database error occurred", Toast.LENGTH_SHORT).show();
                    showViewsAfterLoading();
                }
            });
        } else {
            Toast.makeText(this, "Please enter a valid code", Toast.LENGTH_SHORT).show();
        }
    }

    private void showViewsAfterLoading() {
        createCodeButton.setVisibility(View.VISIBLE);
        joinCodeButton.setVisibility(View.VISIBLE);
        codeEdt.setVisibility(View.VISIBLE);
        headTV.setVisibility(View.VISIBLE);
        loadingPB.setVisibility(View.GONE);
    }

    public void Accepted() {
        intent = new Intent(this, GameAPlayer.class);
        startActivity(intent);
        showViewsAfterLoading();
    }

    public boolean isValueAvailable(DataSnapshot snapshot, String code) {
        Iterable<DataSnapshot> data = snapshot.getChildren();
        Iterator<DataSnapshot> iterator = data.iterator();
        while (iterator.hasNext()) {
            DataSnapshot dataSnapshot = iterator.next();
            String value = dataSnapshot.getValue().toString();
            if (value.equals(code)) {
                Constants.keyValue = dataSnapshot.getKey().toString();
                return true;
            }
        }
        return false;
    }
}



