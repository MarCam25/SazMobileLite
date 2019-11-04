package com.secuencia.saz.sazmobilelite;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.secuencia.saz.sazmobilelite.Modelo.ModeloDatos;
import com.secuencia.saz.sazmobilelite.Modelo.ModeloEmpresa;
import com.secuencia.saz.sazmobilelite.Modelo.ModeloNumeroOrden;
import com.secuencia.saz.sazmobilelite.Modelo.ModeloUsuario;
import com.secuencia.saz.sazmobilelite.conexion.ConexionBDCliente;
import com.secuencia.saz.sazmobilelite.conexion.ConexionSQLiteHelper;
import com.secuencia.saz.sazmobilelite.conexion.ConexionSqlServer;
import com.secuencia.saz.sazmobilelite.utilidades.ModeloTienda;

import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class AltaDeProducto extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alta_de_producto);


    }



}
