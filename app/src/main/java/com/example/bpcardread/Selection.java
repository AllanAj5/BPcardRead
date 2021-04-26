package com.example.bpcardread;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class Selection extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);
    }

    public void newScan(View view) {
        Intent i = new Intent(getApplicationContext(),ScanActivity3.class);
        this.startActivity(i);

    }

    public void openDbview(View view) {
        Intent i = new Intent(getApplicationContext(),GetData.class);
        this.startActivity(i);
    }
}