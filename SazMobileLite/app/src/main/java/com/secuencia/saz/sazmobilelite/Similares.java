package com.secuencia.saz.sazmobilelite;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.secuencia.saz.sazmobilelite.Modelo.Comandero;
import com.secuencia.saz.sazmobilelite.Modelo.DatosLupita;
import com.secuencia.saz.sazmobilelite.Modelo.ModeloEmpresa;
import com.secuencia.saz.sazmobilelite.Modelo.ModeloSimilar;
import com.secuencia.saz.sazmobilelite.Modelo.Similar;
import com.secuencia.saz.sazmobilelite.conexion.ConexionBDCliente;
import com.secuencia.saz.sazmobilelite.conexion.ConexionSQLiteHelper;
import com.secuencia.saz.sazmobilelite.utilidades.AdaptadorSimilares;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Similares extends AppCompatActivity {
    ModeloEmpresa me=new ModeloEmpresa();
    Similar similar=new Similar();
    DatosLupita dl=new DatosLupita();
    ConexionBDCliente bdc=new ConexionBDCliente();
    ArrayList<ModeloSimilar> listaSimilar;
    RecyclerView recyclerView;
    String tienda;
    String where=" where ";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_similares);

        listaSimilar=new ArrayList<>();
        recyclerView=(RecyclerView)findViewById(R.id.reciclador);
        ultimaVez();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        consultarSimilares();


        AdaptadorSimilares adapter=new AdaptadorSimilares(listaSimilar);
        recyclerView.setAdapter(adapter);

    }





    public void getSimilares(){
        try {
            String validar=null;
            ModeloSimilar similar=null;
            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
             String sql="select DISTINCT a.estilo,ac.acabado, c.COLOR, ma.MARCA, ex.TALLA, a.BARCODE,co.Nombre \n" +
                    " from articulo a inner join lineas l on a.LINEA=l.NUMERO inner join sublinea sl on a.SUBLINEA=sl.NUMERO inner join temporad t on a.TEMPORAD=t.NUMERO\n" +
                    "  inner join proveed p on a.PROVEED=p.numero\n" +
                    "  left join empleado e on a.comprador=e.numero inner join departamentos d on a.DEPARTAMENTO=d.NUMERO\n" +
                    "  inner join tacones ta on a.TACON=ta.NUMERO inner join plantillas pl on a.PLANTILLA=pl.NUMERO inner join forros f on a.FORRO=f.NUMERO \n" +
                    "  inner join corridas co on a.corrida=co.id inner join suelas su on a.SUELA=su.numero inner join colores c on a.color = c.numero\n" +
                    "  inner join acabados ac on a.ACABADO=ac.NUMERO inner join marcas ma on a.MARCA=ma.NUMERO left join imagenes im on a.id=im.id inner join clasific clas on a.CLASIFIC=clas.numero inner join existen ex on a.barcode = ex.barcode and ex.CANTIDAD>0"+where+ " and ex.Tienda="+tienda+" and talla="+Principal.punto;
            ResultSet rs = st.executeQuery(sql);


            while (rs.next()) {

                similar=new ModeloSimilar();
                similar.setEstilo(rs.getString(1));
                similar.setAcabado(rs.getString(2));
                similar.setColor(rs.getString(3));
                similar.setMarca(rs.getString(4));
                similar.setCorrida(rs.getString(5));
                similar.setBarcode(rs.getString(6));
                similar.setPunto(rs.getString(7));
                listaSimilar.add(similar);

                validar="hola";

            }
            if(validar!=null){

            }else{
                Toast toast = Toast.makeText(getApplication(), "Este producto no cuenta con similares", Toast.LENGTH_LONG);
                TextView x = (TextView) toast.getView().findViewById(android.R.id.message);
                x.setTextColor(Color.YELLOW); toast.show();

            }

        } catch (SQLException xe) {
            xe.getMessage();
            Toast.makeText(getApplicationContext(),"No tienes configurados los similares",Toast.LENGTH_LONG).show();

        }
    }



    public void ultimaVez(){
        ConexionSQLiteHelper conn = new ConexionSQLiteHelper(this, "db tienda", null, 1);
        SQLiteDatabase db = conn.getReadableDatabase();

        String sql="SELECT nombreT FROM tienda where id=1";
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            String numeroT=(cursor.getString(0));
            tienda=numeroT;

        }
    }


    public void consultarSimilares(){
        where=" WHERE ";
        int marca=0,  temporada=0, clasificacion=0,  sublinea=0, suela=0,  tacon=0,  color=0,  acabado=0,  corrida=0;
        ConexionSQLiteHelper conn = new ConexionSQLiteHelper(this, "db tienda", null, 1);
        SQLiteDatabase db = conn.getReadableDatabase();

        String sql="SELECT * FROM similar";
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {

            marca=cursor.getInt(1);
            temporada=cursor.getInt(2);
            clasificacion=cursor.getInt(3);
            sublinea=cursor.getInt(4);
            suela=cursor.getInt(5);
            tacon=cursor.getInt(6);
            color=cursor.getInt(7);
            acabado=cursor.getInt(8);
            corrida=cursor.getInt(9);
        }



            if(temporada==0 && clasificacion==0 && sublinea==0 && suela==0 && tacon==0 && color==0 && acabado==0 && corrida==0 && marca==0){
                Toast.makeText(getApplicationContext(),"No tienes configurados los similares",Toast.LENGTH_LONG).show();
            }


        if(marca==1){
            if(temporada==1 || clasificacion==1 || sublinea==1 || suela==1 || tacon==1 || color==1 || acabado==1 || corrida==1 ){
                where+=" a.marca="+consultarMarca()+" and ";
            }else{
                where+=" a.marca="+consultarMarca();
            }

        }


        if(temporada==1){

            if( clasificacion==1 || sublinea==1 || suela==1 || tacon==1 || color==1 || acabado==1 || corrida==1 ){
                where+=" a.temporad="+similar.getTemporada()+" and ";
            }else{
                where+=" a.temporad="+similar.getTemporada();
            }

        }

        if(clasificacion==1){
            if( sublinea==1 || suela==1 || tacon==1 || color==1 || acabado==1 || corrida==1 ){
                where+=" a.clasific="+similar.getClasificacion()+" and";
            }else{
                where+=" a.clasific="+similar.getClasificacion();
            }
        }

        if(sublinea==1){
            if(  suela==1 || tacon==1 || color==1 || acabado==1 || corrida==1 ){
                where+=" a.sublinea="+similar.getSubLinea()+" and";
            }else{
                where+=" a.sublinea="+similar.getSubLinea();
            }
        }

        if(suela==1){
            if(  tacon==1 || color==1 || acabado==1 || corrida==1 ){
                where+=" a.suela="+similar.getSuela()+" and";
            }else{
                where+=" a.suela="+similar.getSuela();
            }

        }

        if(tacon==1){
            if( color==1 || acabado==1 || corrida==1 ){
                where+=" a.tacon="+similar.getTacon()+" and";
            }else{
                where+=" a.tacon="+similar.getTacon();
            }
        }

        if(color==1){
            if( acabado==1 || corrida==1 ){
                where+=" a.color="+consultarColor()+" and";
            }else{
                where+=" a.color="+consultarColor();
            }

        }

        if(acabado==1){
            if( corrida==1 ){
                where+=" a.acabado="+consultarAcabado()+" and";
            }else{
                where+=" a.acabado="+consultarAcabado();
            }

        }

        if(corrida==1){
            where+=" a.corrida="+similar.getCorrida();
        }

        getSimilares();
    }


    public String  consultarMarca() {
        String marca=null;
        Similar simi=new Similar();
        try {
            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            String sql="select numero from marcas where marca='"+simi.getMarca()+"'";
            ResultSet rs = st.executeQuery(sql);


            while (rs.next()) {


                marca=rs.getString(1);


            }

            // Toast.makeText(Principal.this,"Inicio de sesion Exitosa...!!!: " + empresa, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error al consultar producto: Marca", Toast.LENGTH_SHORT).show();
        }

        return marca;
    }


    public String  consultarColor() {
        String color=null;
        Similar simi=new Similar();
        try {
            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            String sql="select numero from colores where color='"+simi.getColor()+"'";
            ResultSet rs = st.executeQuery(sql);


            while (rs.next()) {


                color=rs.getString(1);


            }

            // Toast.makeText(Principal.this,"Inicio de sesion Exitosa...!!!: " + empresa, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error al consultar producto: Color", Toast.LENGTH_SHORT).show();
        }

        return color;
    }


    public String  consultarAcabado() {
        String acabado=null;
        Similar simi=new Similar();
        try {
            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            String sql="select numero from acabados where acabado='"+simi.getAcabado()+"'";
            ResultSet rs = st.executeQuery(sql);


            while (rs.next()) {



                acabado=rs.getString(1);


            }

            // Toast.makeText(Principal.this,"Inicio de sesion Exitosa...!!!: " + empresa, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error al consultar producto: acabado", Toast.LENGTH_SHORT).show();
        }

        return acabado;
    }


}
