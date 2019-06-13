package com.secuencia.saz.sazmobilelite.utilidades;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.google.zxing.Result;
import com.secuencia.saz.sazmobilelite.ConsultaF;
import com.secuencia.saz.sazmobilelite.R;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class Scann extends AppCompatActivity  implements ZXingScannerView.ResultHandler{

    TextView codigo;
    ZXingScannerView scannerView;
    Button scann;
    ZXingScannerView vistaescaner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scann);
        scannerView=new ZXingScannerView(this);
        setContentView(scannerView);
    }


    @Override
    public void handleResult(Result result) {
        ConsultaF.cargarDatos(result.getText());
        onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        scannerView.stopCamera();
    }

    @Override
    protected void onResume() {
        super.onResume();
        scannerView.setResultHandler(this);
        scannerView.startCamera();
    }

}
