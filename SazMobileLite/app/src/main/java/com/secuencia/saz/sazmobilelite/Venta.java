package com.secuencia.saz.sazmobilelite;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.secuencia.saz.sazmobilelite.Modelo.ModeloEmpresa;
import com.secuencia.saz.sazmobilelite.conexion.ConexionBDCliente;
import com.secuencia.saz.sazmobilelite.Modelo.Comandero;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;


public class Venta extends Fragment {
    public static ModeloEmpresa me=new ModeloEmpresa();
    public static ConexionBDCliente bdc=new ConexionBDCliente();

    ArrayList<Comandero> listaComandero;
    RecyclerView recyclerView;

    Context context;
    View root;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root=inflater.inflate(R.layout.fragment_venta, container, false);
        context=root.getContext();

        listaComandero=new ArrayList<>();
        recyclerView=(RecyclerView)root.findViewById(R.id.resicladorId);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        mostrarVentas();


        return root;
    }


    public void mostrarVentas(){

        try {
            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            ResultSet rs = st.executeQuery("select numero,cliente,total,pares,empleado from comandero where [status]=1;");

            Comandero comandero=null;
            while (rs.next()) {
                comandero=new Comandero();

                comandero.setNumero(rs.getString(1));
                comandero.setCliente(rs.getString(2));
                comandero.setTotal(rs.getString(3));
                comandero.setPares(rs.getString(4));
                comandero.setEmpleado(rs.getString(5));

                listaComandero.add(comandero);




            }

        } catch (Exception e) {

        }
    }

}
