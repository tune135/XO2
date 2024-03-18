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
    Button createCodeBtn;
    Button joinCodeBtn;
    ProgressBar loadingPB;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_code);
        headTV = findViewById(R.id.idTVHead);
        codeEdt = findViewById(R.id.idEdtCode);
        createCodeBtn = findViewById(R.id.idBtnCreate);
        joinCodeBtn = findViewById(R.id.idBtnJoin);
        loadingPB = findViewById(R.id.idPBLoading);
        createCodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constants.code = "null";
                Constants.codeFound = false;
                Constants.checkTemp = true;
                Constants.keyValue = "null";
                Constants.code = codeEdt.getText().toString();
                createCodeBtn.setVisibility(View.GONE);
                joinCodeBtn.setVisibility(View.GONE);
                headTV.setVisibility(View.GONE);
                codeEdt.setVisibility(View.GONE);
                loadingPB.setVisibility(View.VISIBLE);

                if (Constants.code != "null" && Constants.code != "") {
                    Constants.isCodeMaker = true;
                    FirebaseDatabase.getInstance().getReference().child("codes").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            boolean check = isValueAvaliable(snapshot, Constants.code);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (check == true) {
                                        createCodeBtn.setVisibility(View.VISIBLE);
                                        joinCodeBtn.setVisibility(View.VISIBLE);
                                        codeEdt.setVisibility(View.VISIBLE);
                                        headTV.setVisibility(View.VISIBLE);
                                        loadingPB.setVisibility(View.GONE);
                                    } else {
                                        FirebaseDatabase.getInstance().getReference().child("codes").push().setValue(Constants.code);
                                        isValueAvaliable(snapshot, Constants.code);
                                        Constants.checkTemp = false;
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                Accepted();
                                                Toast.makeText(OnlineCode.this, "Please don't go back", Toast.LENGTH_SHORT).show();
                                            }
                                        }, 300);
                                    }
                                }
                            }, 2000);
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            // TODO: Implement onCancelled
                        }
                    });
                } else {
                    createCodeBtn.setVisibility(View.VISIBLE);
                    joinCodeBtn.setVisibility(View.VISIBLE);
                    codeEdt.setVisibility(View.VISIBLE);
                    headTV.setVisibility(View.VISIBLE);
                    loadingPB.setVisibility(View.GONE);
                    Toast.makeText(OnlineCode.this, "Please enter a valid code", Toast.LENGTH_SHORT).show();
                }
            }
        });


        joinCodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constants.code = "null";
                Constants.codeFound = false;
                Constants.checkTemp = true;
                Constants.keyValue = "null";
                Constants.code = codeEdt.getText().toString();
                if (Constants.code != "null" && Constants.code != "") {
                    createCodeBtn.setVisibility(View.GONE);
                    joinCodeBtn.setVisibility(View.GONE);
                    codeEdt.setVisibility(View.GONE);
                    headTV.setVisibility(View.GONE);
                    loadingPB.setVisibility(View.VISIBLE);
                    Constants.isCodeMaker = false;
                    FirebaseDatabase.getInstance().getReference().child("codes").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            boolean data = isValueAvaliable(snapshot, Constants.code);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (data == true) {
                                        Constants.codeFound = true;
                                        Accepted();
                                        createCodeBtn.setVisibility(View.VISIBLE);
                                        joinCodeBtn.setVisibility(View.VISIBLE);
                                        codeEdt.setVisibility(View.VISIBLE);
                                        headTV.setVisibility(View.VISIBLE);
                                        loadingPB.setVisibility(View.GONE);
                                    } else {
                                        createCodeBtn.setVisibility(View.VISIBLE);
                                        joinCodeBtn.setVisibility(View.VISIBLE);
                                        codeEdt.setVisibility(View.VISIBLE);
                                        headTV.setVisibility(View.VISIBLE);
                                        loadingPB.setVisibility(View.GONE);
                                        Toast.makeText(OnlineCode.this, "Invalid Code", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }, 2000);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                } else {
                    Toast.makeText(OnlineCode.this, "Please enter a valid code", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }


    private void showViewsAfterLoading() {
        createCodeBtn.setVisibility(View.VISIBLE);
        joinCodeBtn.setVisibility(View.VISIBLE);
        codeEdt.setVisibility(View.VISIBLE);
        headTV.setVisibility(View.VISIBLE);
        loadingPB.setVisibility(View.GONE);
    }

    public void Accepted() {
        intent = new Intent(this, GameAPlayer.class);
        startActivity(intent);
        showViewsAfterLoading();
    }

    public boolean isValueAvaliable(DataSnapshot snapshot, String code) {
        Iterable<DataSnapshot> data = snapshot.getChildren();
        for (DataSnapshot it : data) {
            String value = it.getValue().toString();
            if (value.equals(code)) {
                String keyValue = it.getKey().toString();
                return true;
            }
        }
        return false;
    }
}



