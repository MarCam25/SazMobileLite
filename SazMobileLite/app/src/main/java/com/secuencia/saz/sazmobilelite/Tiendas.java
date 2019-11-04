package com.secuencia.saz.sazmobilelite;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.secuencia.saz.sazmobilelite.Modelo.DatosLupita;
import com.secuencia.saz.sazmobilelite.Modelo.Lupita;
import com.secuencia.saz.sazmobilelite.Modelo.ModeloEmpresa;
import com.secuencia.saz.sazmobilelite.conexion.ConexionBDCliente;
import com.secuencia.saz.sazmobilelite.utilidades.AdaptadorLupita;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Tiendas extends AppCompatActivity {

    public static ModeloEmpresa me=new ModeloEmpresa();
    public static ConexionBDCliente bdc=new ConexionBDCliente();
    DatosLupita dl=new DatosLupita();
    ArrayList<Lupita> listaLupita;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tiendas);
        try {

            listaLupita = new ArrayList<>();
            recyclerView = (RecyclerView) findViewById(R.id.recyId);
            getSupportActionBar().setTitle("SazMobile Lite App  -Todas las tiendas-");



            //++++++++++++++++++++++++++++ algoritmo para llenar el recyclerView +++++++++++++++++++++++++++++++++++++
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            llenarRecycler();
            AdaptadorLupita adapterLupita = new AdaptadorLupita(listaLupita);
            recyclerView.setAdapter(adapterLupita);
            //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++




        }catch (Exception e){
            Toast.makeText(this, "Error al intentar consulatar en otras tiendas "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void llenarRecycler(){
        String puntoSp=dl.getPunto();
        String barcode=dl.getBarcode();
        Lupita lupita=null;
        try {

            Statement st = bdc.conexionBD(me.getServer(), me.getBase(), me.getUsuario(), me.getPass()).createStatement();

            String sql = "lupitaApartados'"+barcode+"',"+puntoSp+",0,''";
            ResultSet rs=st.executeQuery(sql);

            while(rs.next()){
                lupita=new Lupita();
                int validar = rs.getInt(2);
                if(validar>0) {
                    String tienda = rs.getString(1);
                    String punto = rs.getString(2);
                    lupita.setTienda(tienda);
                    lupita.setPunto(punto);
                    listaLupita.add(lupita);
                }


            }


            st.close();

        } catch (SQLException e){
            e.getMessage();
            Toast.makeText(this, "No hay este producto en otras tiendas", Toast.LENGTH_LONG).show();
        }
    }
}
