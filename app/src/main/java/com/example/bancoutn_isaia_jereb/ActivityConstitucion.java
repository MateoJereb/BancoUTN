package com.example.bancoutn_isaia_jereb;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;

import com.example.bancoutn_isaia_jereb.databinding.ActivityConstitucionBinding;

import org.w3c.dom.Text;

import java.util.regex.Pattern;

public class ActivityConstitucion extends AppCompatActivity {

    private ActivityConstitucionBinding binding;
    private String signoMoneda = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityConstitucionBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        setTitle("Simular Plazo Fijo");

        int moneda = (int) getIntent().getExtras().getLong("moneda");
        switch (moneda){
            case 0:
                signoMoneda = "$";
                binding.simuladorTextView.setText("Simulador Plazo Fijo en Pesos");
                break;
            case 1:
                signoMoneda = "US$";
                binding.simuladorTextView.setText("Simulador Plazo Fijo en Dólares");
                break;
            case 2:
                signoMoneda = "\u20AC";
                binding.simuladorTextView.setText("Simulador Plazo Fijo en Euros");
                break;
        }

        binding.diasSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                int progress = seekBar.getProgress();
                binding.diasTextView.setText(progress*30+" días");
                calcular();
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });

        binding.renovacionCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                calcular();
            }
        });

        TextWatcher watcherCalculos = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                calcular();
            }
            @Override
            public void afterTextChanged(Editable editable) {}
        };

        binding.tasaNomEditText.addTextChangedListener(watcherCalculos);
        binding.tasaEfecEditText.addTextChangedListener(watcherCalculos);
        binding.capitalEditText.addTextChangedListener(watcherCalculos);

    }

    private void calcular(){
        if(camposCompletos()){
            try{
                double  tasaNom = Double.parseDouble(binding.tasaNomEditText.getText().toString()),
                        tasaEfec = Double.parseDouble(binding.tasaEfecEditText.getText().toString()),
                        capital = Double.parseDouble(binding.capitalEditText.getText().toString()),
                        tasa;
                int plazo = binding.diasSeekBar.getProgress();

                boolean renovacionAuto = binding.renovacionCheckBox.isChecked();
                if(renovacionAuto) tasa = tasaEfec;
                else tasa = tasaNom;

                double  intereses = capital*(tasa/100)*plazo/12,
                        montoTotal = intereses+capital,
                        montoAnual = capital*(1+(tasa/100));

                intereses = Math.round(intereses*100.0)/100.0;
                montoTotal = Math.round(montoTotal*100.0)/100.0;
                montoAnual = Math.round(montoAnual*100.0)/100.0;

                String  plazoStr = "Plazo: "+plazo*30+" días",
                        capitalStr = "Capital: "+signoMoneda+capital,
                        interesesStr = "Intereses ganados: "+signoMoneda+intereses,
                        montoTotalStr = "Monto total: "+signoMoneda+montoTotal,
                        montoAnualStr = "Monto anual: "+signoMoneda+montoAnual;

                binding.plazoTextView.setText(plazoStr);
                binding.capitalTextView.setText(capitalStr);
                binding.interesesTextView.setText(interesesStr);
                binding.montoTextView.setText(montoTotalStr);
                binding.montoAnualTextView.setText(montoAnualStr);
            }
            catch(Exception e){ e.printStackTrace(); }
        }
    }

    private boolean camposCompletos(){
        if (binding.tasaNomEditText.getText().length() == 0) return false;
        if (binding.tasaEfecEditText.getText().length() == 0) return false;
        if (binding.capitalEditText.getText().length() == 0) return false;

        return true;
    }

    public void onConfirmar(View view){
        if(camposCompletos()){
            if(binding.diasSeekBar.getProgress() != 0){
                double capital = Double.parseDouble(binding.capitalEditText.getText().toString());
                int plazo = binding.diasSeekBar.getProgress();

                Intent intent = new Intent();
                intent.putExtra("capital",capital);
                intent.putExtra("plazo",plazo);
                setResult(100,intent);
                finish();
            }
            else{ //No indicó duración
                AlertDialog.Builder builder = new AlertDialog.Builder(ActivityConstitucion.this);
                builder.setMessage("La duración no puede ser de 0 días.")
                        .setNeutralButton("Aceptar",null);

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }
        else{ //Campos incompletos
            AlertDialog.Builder builder = new AlertDialog.Builder(ActivityConstitucion.this);
            builder.setMessage("Debe completar todos los campos.")
                    .setNeutralButton("Aceptar", null);

            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }
}

