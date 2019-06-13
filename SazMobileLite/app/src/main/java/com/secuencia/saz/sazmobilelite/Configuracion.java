package com.secuencia.saz.sazmobilelite;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.secuencia.saz.sazmobilelite.Modelo.Areas;
import com.secuencia.saz.sazmobilelite.Modelo.ModeloEmpresa;
import com.secuencia.saz.sazmobilelite.Modelo.ModeloNumeroOrden;
import com.secuencia.saz.sazmobilelite.conexion.ConexionBDCliente;
import com.secuencia.saz.sazmobilelite.conexion.ConexionSQLiteHelper;
import com.secuencia.saz.sazmobilelite.utilidades.Utilidades;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;


public class Configuracion extends Fragment {

    ModeloEmpresa me=new ModeloEmpresa();
    ConexionBDCliente bdc=new ConexionBDCliente();

    public static Button guardar,altaArea,altaProducto,actualizar;
    public static EditText zona;
    View root;
    ArrayList<Areas> listaAreas;
    RecyclerView recyclerView;
    public static Context context;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        root=inflater.inflate(R.layout.fragment_configuracion, container, false);
        context=root.getContext();
        zona=(EditText)root.findViewById(R.id.idZona);
        actualizar=(Button)root.findViewById(R.id.idActualizar);
        guardar=(Button)root.findViewById(R.id.idGuardar);
        altaArea=(Button)root.findViewById(R.id.idaltaArea);
        altaProducto=(Button)root.findViewById(R.id.altaProducto);
        actualizar.setEnabled(false);

        listaAreas=new ArrayList<>();
        recyclerView=(RecyclerView)root.findViewById(R.id.recyAreas);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        getAreas();


        actualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(zona.getText().toString().isEmpty()){
                    Toast.makeText(getActivity(),"El campo esta vacio",Toast.LENGTH_LONG).show();
                }else{

                    veificarActualizacion();
                    zona.setText(null);
                }

            }
        });


        altaArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent orden = new Intent(getActivity(), Area.class);
                startActivity(orden);
            }
        });

        altaProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent orden = new Intent(getActivity(), AltaDeProducto.class);
                startActivity(orden);
            }
        });

        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(zona.getText().toString().isEmpty()){
                    Toast.makeText(getActivity(),"El campo esta vacio",Toast.LENGTH_LONG).show();
                }else{

                    veificar();
                    zona.setText(null);
                    actualizarPagina();
                }




            }
        });




        return root;
    }


    public void veificarActualizacion(){
        String zonas=null;
        try {
            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            String sql="select nombre from AreasDeControl where nombre='"+zona.getText()+"' ";
            ResultSet rs=st.executeQuery(sql);
            while(rs.next()){


                zonas=rs.getString(1);

            }


        } catch (Exception e) {

            Toast.makeText(getActivity(), "Error al verificar", Toast.LENGTH_SHORT).show();
        }
        if(zonas!=null){
            Toast.makeText(getActivity(), "No se puede guardar el registro porque ya existe un área con el mismo nombre", Toast.LENGTH_LONG).show();
        }else{
            update();
            guardar.setEnabled(true);
            actualizar.setEnabled(false);
            Toast.makeText(getActivity(), "Área actualizada ", Toast.LENGTH_SHORT).show();
            actualizarPagina();

        }
    }
    public void veificar(){
        String zonas=null;
        try {
            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            String sql="select nombre from AreasDeControl where nombre='"+zona.getText()+"' ";
            ResultSet rs=st.executeQuery(sql);
            while(rs.next()){


                zonas=rs.getString(1);

            }


        } catch (Exception e) {

            Toast.makeText(getActivity(), "Error al verificar", Toast.LENGTH_SHORT).show();
        }
        if(zonas!=null){
            Toast.makeText(getActivity(), "No se puede guardar el registro porque ya existe un área con el mismo nombre", Toast.LENGTH_SHORT).show();
        }else{
            darAlta();
            Toast.makeText(getActivity(), "Se a creado un nuevo registro", Toast.LENGTH_SHORT).show();


        }
    }
    public void darAlta(){

        String tienda=getTienda();
        try {
            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            String sql="insert into AreasDeControl (idTienda,nombre)values('"+tienda+"','"+zona.getText()+"');";
            st.executeUpdate(sql);


        } catch (Exception e) {

            Toast.makeText(getActivity(), "Error al registrar areax", Toast.LENGTH_SHORT).show();
        }

    }
    public void update(){
        ModeloNumeroOrden mno=new ModeloNumeroOrden();
        String tienda=getTienda();
        try {
            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            String sql="UPDATE AreasDeControl SET nombre='"+zona.getText()+"'where idArea="+mno.getIdArea()+" and idTienda="+tienda;
            st.executeUpdate(sql);


        } catch (Exception e) {

            Toast.makeText(getActivity(), "Error al registrar areax", Toast.LENGTH_SHORT).show();
        }

    }



    public String  getTienda(){
        String tienda="";
        ConexionSQLiteHelper conn = new ConexionSQLiteHelper(getActivity(), "db tienda", null, 1);
        SQLiteDatabase db = conn.getReadableDatabase();


        Cursor cursor = db.rawQuery("SELECT nombreT FROM " + Utilidades.TABLA_TIENDA , null);
        while (cursor.moveToNext()) {
            tienda =(cursor.getString(0));



        }
        return tienda;
    }


    public void getAreas(){
        String tienda=getTienda();
        Areas areas=null;
        try {
            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            String sql="  select * from AreasDeControl where idTienda="+tienda;
            ResultSet rs = st.executeQuery(sql);


            while(rs.next()){
                areas=new Areas();
                areas.setIdArea(rs.getString(1));
                areas.setIdTienda(rs.getString(2));
                areas.setNombreArea(rs.getString(3));
                listaAreas.add(areas);
            }


        } catch (Exception e) {

            Toast.makeText(getActivity(), "Error al registrar areax", Toast.LENGTH_SHORT).show();
        }
    }

    public static  void actualizarPagina(){
        Principal.location=3;
        Intent intent = new Intent(context, menu.class);



        context.startActivity(intent);
    }
}
