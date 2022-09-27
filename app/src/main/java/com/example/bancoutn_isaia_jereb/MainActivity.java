package com.example.bancoutn_isaia_jereb;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;

import com.example.bancoutn_isaia_jereb.databinding.ActivityConstitucionBinding;
import com.example.bancoutn_isaia_jereb.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private double capital;
    private int plazo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        setTitle("Constituir Plazo Fijo");


    }

    public void onSimular(View view){
        Intent intent = new Intent(this, ActivityConstitucion.class);
        startActivityForResult(intent,111);
    }

    public void onConstituir(View view){
        if(camposCompletos()){
            String nombre = binding.nombreEditText.getText().toString();
            String apellido = binding.apellidoEditText.getText().toString();
            String moneda = binding.monedasSpinner.getSelectedItem().toString().toLowerCase();

            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Felicitaciones "+nombre+" "+apellido+"!")
                    .setMessage("Tu plazo fijo de "+capital+" "+moneda+" por "+plazo*30+" días ha sido constituido!")
                    .setNeutralButton("Aceptar",null);

            AlertDialog dialog = builder.create();
            dialog.show();
        }
        else{ //Campos incompletos
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage("Indique nombre y apellido")
                    .setNeutralButton("Aceptar",null);

            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == 100){
            if(requestCode == 111){
                binding.constituirButton.setEnabled(true);
                capital = data.getExtras().getDouble("capital");
                plazo = data.getExtras().getInt("plazo");
            }
        }
    }

    private boolean camposCompletos(){
        if(binding.nombreEditText.getText().length() == 0) return false;
        if(binding.apellidoEditText.getText().length() == 0) return false;
        return true;
    }
}