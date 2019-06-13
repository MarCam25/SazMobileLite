package com.secuencia.saz.sazmobilelite;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.secuencia.saz.sazmobilelite.Modelo.ModeloEmpresa;
import com.secuencia.saz.sazmobilelite.Modelo.ModeloNumeroOrden;
import com.secuencia.saz.sazmobilelite.Modelo.Zonas;
import com.secuencia.saz.sazmobilelite.conexion.ConexionBDCliente;
import com.secuencia.saz.sazmobilelite.conexion.ConexionSQLiteHelper;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class Area extends AppCompatActivity {

    ModeloEmpresa me=new ModeloEmpresa();
    ConexionBDCliente bdc=new ConexionBDCliente();

    Spinner Areas;


    public static Button guardar,actualizar;
    public static EditText zona;
    public static ArrayList listaArea=new ArrayList();

    ArrayList<Zonas> listaZonas;
    RecyclerView recyclerView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_area);


        guardar=(Button)findViewById(R.id.idGuardar);
        zona=(EditText) findViewById(R.id.idZonaEd);
        Areas=(Spinner)findViewById(R.id.ListaArea);
        actualizar=(Button)findViewById(R.id.idActualizarZona);
        actualizar.setEnabled(false);
        guardar.setEnabled(true);
        listaZonas=new ArrayList();

        recyclerView=(RecyclerView)findViewById(R.id.recyZonas);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        getZonas();







        Areas.setAdapter(null);
        listaArea.clear();
        llenarListaAreas();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.spinner_item, listaArea);
        Areas.setAdapter(adapter);
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(zona.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"El campo esta vacio",Toast.LENGTH_LONG).show();

                }else{
                    veificar();
                    zona.setText(null);

                }






            }
        });

        actualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(zona.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"Elcapo esta vacio",Toast.LENGTH_LONG).show();

                }else{
                    veificarUpdate();
                    zona.setText(null);

                }
            }
        });
    }


    public void update(){
        ModeloNumeroOrden mno=new ModeloNumeroOrden();
        String tienda=getTienda();
        String idArea =getNumeroArea(Areas.getSelectedItem().toString());
        try {
            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            String sql="UPDATE ZonasDeSurtido SET nombre='"+zona.getText()+"', idArea="+idArea+ " WHERE idTienda="+tienda+ " and idZona="+mno.getIdZona() ;
            st.executeUpdate(sql);


        } catch (Exception e) {

            Toast.makeText(this, "Error al Registrar Zona", Toast.LENGTH_SHORT).show();
        }
    }


    public void RegistrarZona(){

        String tienda=getTienda();
        String idArea =getNumeroArea(Areas.getSelectedItem().toString());
        try {
            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            String sql="INSERT INTO ZonasDeSurtido  (idTienda,nombre,idArea) VALUES ('"+tienda+"','"+zona.getText()+"',"+idArea+");";
            st.executeUpdate(sql);


        } catch (Exception e) {

            Toast.makeText(this, "Error al Registrar Zona", Toast.LENGTH_SHORT).show();
        }
    }

    public String getNumeroArea(String nombreArea){
        String numeroArea=null;
        try {
            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            String sql="select idArea from AreasDeControl WHERE nombre='"+nombreArea+"'";
            ResultSet rs=st.executeQuery(sql);

            while(rs.next()){
                numeroArea=rs.getString(1);
            }

        } catch (Exception e) {

            Toast.makeText(this, "Error al obtener id del area", Toast.LENGTH_SHORT).show();
        }

        return numeroArea;
    }
    public void llenarListaAreas(){
        String tienda=getTienda();
        try {
            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            String sql="SELECT nombre FROM areasdecontrol WHERE idtienda="+tienda;
            ResultSet rs=st.executeQuery(sql);

            while(rs.next()){

                listaArea.add(rs.getString(1));

            }


        } catch (Exception e) {

            Toast.makeText(this, "Error al llenar la lista de area", Toast.LENGTH_SHORT).show();
        }
    }


    public void veificar(){
        String zonas=null;
        try {
            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            String sql="select nombre from Zonasdesurtido where nombre='"+zona.getText()+"' ";
            ResultSet rs=st.executeQuery(sql);
            while(rs.next()){


                zonas=rs.getString(1);

            }


        } catch (Exception e) {

            Toast.makeText(this, "Error al verificar", Toast.LENGTH_SHORT).show();
        }
        if(zonas!=null){
            Toast.makeText(this, "No se puede guardar el registro porque ya existe una ZONA con el mismo nombre. ", Toast.LENGTH_SHORT).show();
        }else{
            RegistrarZona();
            Toast.makeText(this, "Se a creado un nuevo registro", Toast.LENGTH_SHORT).show();
            actualizarPagina();

        }
    }
    public void veificarUpdate() {
        String zonas = null;
        int idZona = 0;

        String tienda = getTienda();

        try {
            Statement st = bdc.conexionBD(me.getServer(), me.getBase(), me.getUsuario(), me.getPass()).createStatement();
            String sql = "select nombre,idZona from Zonasdesurtido where nombre='" + zona.getText() + "' and idtienda=" + tienda;
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {


                zonas = rs.getString(1);
                idZona = rs.getInt(2);


            }


        } catch (Exception e) {

            Toast.makeText(this, "Error al verificar", Toast.LENGTH_SHORT).show();
        }
        ModeloNumeroOrden mno=new ModeloNumeroOrden();

        if(Integer.parseInt(mno.getIdZona())==idZona){
            update();
            Toast.makeText(this, "Zona actualizada", Toast.LENGTH_SHORT).show();

            actualizarPagina();
        }else{
            if (zonas != null) {
                Toast.makeText(this, "No se puede guardar el registro porque ya existe una ZONA con el mismo nombre. ", Toast.LENGTH_SHORT).show();
            } else {

                update();
                Toast.makeText(this, "Zona actualizada", Toast.LENGTH_SHORT).show();

                actualizarPagina();

            }
        }
    }


    public String getTienda(){
        String tienda =null;
        ConexionSQLiteHelper conn = new ConexionSQLiteHelper(this, "db tienda", null, 1);

        SQLiteDatabase db = conn.getReadableDatabase();




        String sql = "SELECT nombreT from tienda where id=1";
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            tienda=cursor.getString(0);


        }

        return tienda;
    }



    public void getZonas(){
        String tienda=getTienda();
        Zonas zonas=null;
        try {
            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            String sql="  select idZona, ZonasDeSurtido.idTienda, ZonasDeSurtido.nombre, ZonasDeSurtido.idArea,AreasDeControl.nombre from ZonasDeSurtido inner join AreasDeControl on ZonasDeSurtido.idArea=AreasDeControl.idArea where ZonasDeSurtido.idTienda= "+tienda;
            ResultSet rs = st.executeQuery(sql);


            while(rs.next()){
                zonas=new Zonas();
                zonas.setIdZona(rs.getString(1));
                zonas.setIdTienda(rs.getString(2));
                zonas.setNombre(rs.getString(3));
                zonas.setIdArea(rs.getString(4));
                zonas.setNombreArea(rs.getString(5));


                listaZonas.add(zonas);
            }


        } catch (Exception e) {

            Toast.makeText(this, "Error al Mostar Zonas", Toast.LENGTH_SHORT).show();
        }
    }
    public void actualizarPagina(){
        Intent orden = new Intent(this, Area.class);
        startActivity(orden);
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Principal.location=3;
            Intent intent = new Intent(getApplicationContext(), menu.class);



            startActivity(intent);
        }
        return true;
    }

}
