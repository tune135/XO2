package com.example.xo2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

public class OnlineCode extends AppCompatActivity {

    TextView heatTV;
    EditText codeEdt;
    Button createCodeButton;
    Button joinCodeButton;
    ProgressBar loadingPB;
    Boolean isCodeMaker;
    int code;
    Boolean codeFound;
    Boolean checkTemp;
    String keyValue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_code);

        heatTV = findViewById(R.id.idTVHead);
        codeEdt = findViewById(R.id.idEdtCode);
        createCodeButton = findViewById(R.id.idBtnCreate);
        joinCodeButton = findViewById(R.id.idBtnJoin);
        loadingPB = findViewById(R.id.idPbLoading);

        isCodeMaker = true;
        codeFound = false;
        checkTemp = true;
        keyValue = "null";


    }

    public void Join(View view) {

    }

    public void Create(View view) {

    }
}