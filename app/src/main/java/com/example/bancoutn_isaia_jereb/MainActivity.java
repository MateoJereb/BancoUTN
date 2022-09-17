package com.example.bancoutn_isaia_jereb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Constituir Plazo Fijo");
    }

    public void onSimular(View vie){
        Intent intent = new Intent(this,ActivityConstitucion.class);
        startActivity(intent);
    }
}