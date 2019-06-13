package com.secuencia.saz.sazmobilelite;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.secuencia.saz.sazmobilelite.Modelo.ModeloDatos;
import com.secuencia.saz.sazmobilelite.Modelo.ModeloEmpresa;
import com.secuencia.saz.sazmobilelite.Modelo.ModeloUsuario;
import com.secuencia.saz.sazmobilelite.conexion.ConexionBDCliente;
import com.secuencia.saz.sazmobilelite.conexion.ConexionSQLiteHelper;
import com.secuencia.saz.sazmobilelite.conexion.ConexionSqlServer;
import com.secuencia.saz.sazmobilelite.utilidades.ModeloTienda;
import com.secuencia.saz.sazmobilelite.utilidades.Utilidades;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ActPrincipal extends AppCompatActivity {
    String usuario, nombreUsuario, numeroUsuario;

    String empresa;
    ConexionSqlServer conex=new ConexionSqlServer();
    ModeloEmpresa me=new ModeloEmpresa();
    ConexionBDCliente bdc=new ConexionBDCliente();
    Button acep;
    ModeloDatos md=new ModeloDatos();
    CheckBox check;
    ArrayList listaTiendas=new ArrayList();
    ArrayList listaZonas=new ArrayList();

    public String numeroTienda;
    private Spinner spTiendas, spZona;
    ConexionSQLiteHelper conn=new ConexionSQLiteHelper(this,"db tienda",null,1);
    String repetido;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_principal);

        getSupportActionBar().setTitle("SazMobile Lite App ");

        spTiendas=(Spinner)findViewById(R.id.spTiendas);
        spZona=(Spinner)findViewById(R.id.spZona);
        check=(CheckBox)findViewById(R.id.check);
        crearComanderoLog();
        crearComanderoDet();
        crearUbicacionTabla();
        crearAreaAsignadaTabla();
        crearZonaTabla();
        crearComanderoTabla();
        crearAreasTabla();
        dropSP();
        crearSp();


        obtenerTiendas();





        ArrayAdapter<String> adapterTiendas = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, listaTiendas);
        spTiendas.setAdapter(adapterTiendas);
        acep=(Button)findViewById(R.id.prueba);


        spTiendas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                ArrayAdapter<String> adapterZonas=new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, listaZonas);
                adapterZonas.clear();
                obtenerNumeroTienda();
                consultarZona();


                spZona.setAdapter(adapterZonas);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        acep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Toast toast = Toast.makeText(getApplicationContext(), "Cargando...", Toast.LENGTH_LONG);
                TextView x = (TextView) toast.getView().findViewById(android.R.id.message);
                x.setTextColor(Color.YELLOW);
                toast.show();

                Toast toaste = Toast.makeText(getApplicationContext(), "Cargando...", Toast.LENGTH_LONG);
                TextView xx = (TextView) toaste.getView().findViewById(android.R.id.message);
                xx.setTextColor(Color.YELLOW);

                toaste.show();



                    obtenerNumero();





            }
        });


    }


    public void obtenerNumero(){
        try {
            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            ResultSet rs = st.executeQuery("select numero from tiendas where nombre='"+spTiendas.getSelectedItem()+"'");

            ModeloTienda mt=new ModeloTienda();
            mt.setNumeroTienda(spTiendas.getSelectedItem().toString());
            while (rs.next()) {


                numeroTienda=rs.getString(1);

                usuario= getIntent().getStringExtra("Usuario");
                empresa=getIntent().getStringExtra("Empresa");
                getNameUser();
                Toast toast = Toast.makeText(getApplicationContext(), "Cargando...", Toast.LENGTH_LONG);
                TextView x = (TextView) toast.getView().findViewById(android.R.id.message);
                x.setTextColor(Color.YELLOW);
                toast.show();
                insertarEntrada();

                Intent inte= new Intent(getApplicationContext(), menu.class);
                inte.putExtra("listado",numeroTienda);
                inte.putExtra("Usuario",usuario);
                inte.putExtra("Empresa",empresa);


                SQLiteDatabase db=conn.getWritableDatabase();
                ContentValues values=new ContentValues();

                values.put(Utilidades.CAMPOS_NOMBRE,numeroTienda);
                values.put(Utilidades.CAMPOS_DATOS,"aa"+"-"+"aa"+"-"+"aa"+"-"+"aa"+"-"+"aa"+"-"+"aa"+"-"+"aa");
                values.put(Utilidades.CAMPOS_STATTUS,"3");
                Long idResultante=db.insert(Utilidades.TABLA_TIENDA,Utilidades.CAMPOS_NOMBRE,values);
                db.close();



                actualizar();

                insertarAreaAsignada();
                //aqui va
                startActivity(inte);

            }


        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error en sp", Toast.LENGTH_SHORT).show();
        }
    }


    public void entrarSinArea(){
        try {
            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            ResultSet rs = st.executeQuery("select numero from tiendas where nombre='"+spTiendas.getSelectedItem()+"'");


            while (rs.next()) {


                numeroTienda=rs.getString(1);

                usuario= getIntent().getStringExtra("Usuario");
                empresa=getIntent().getStringExtra("Empresa");
                getNameUser();
                insertarEntrada();

                Intent inte= new Intent(getApplicationContext(), PrimerRegistroArea.class);
                inte.putExtra("listado",numeroTienda);
                inte.putExtra("Usuario",usuario);
                inte.putExtra("Empresa",empresa);


                SQLiteDatabase db=conn.getWritableDatabase();
                ContentValues values=new ContentValues();

                values.put(Utilidades.CAMPOS_NOMBRE,numeroTienda);
                values.put(Utilidades.CAMPOS_DATOS,"aa"+"-"+"aa"+"-"+"aa"+"-"+"aa"+"-"+"aa"+"-"+"aa"+"-"+"aa");
                values.put(Utilidades.CAMPOS_STATTUS,"3");
                Long idResultante=db.insert(Utilidades.TABLA_TIENDA,Utilidades.CAMPOS_NOMBRE,values);
                db.close();



                actualizar();

                insertarAreaAsignada();
                startActivity(inte);

            }


        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error en sp", Toast.LENGTH_SHORT).show();
        }

    }

    public void obtenerNumeroTienda(){
        try {
            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            ResultSet rs = st.executeQuery("select numero from tiendas where nombre='"+spTiendas.getSelectedItem()+"'");


            while (rs.next()) {


                numeroTienda=rs.getString(1);

            }


        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error en sp", Toast.LENGTH_SHORT).show();
        }

    }


    public void actualizarZona(String zona){



        ConexionSQLiteHelper conn = new ConexionSQLiteHelper(this, "db tienda", null, 1);
        SQLiteDatabase db=conn.getWritableDatabase();




        db.execSQL("UPDATE zona SET zona="+zona+"");



    }

    public void actualizar(){
        SQLiteDatabase db=conn.getReadableDatabase();
        String[] parametros={"1"};
        ContentValues values=new ContentValues();
        try {
            values.put(Utilidades.CAMPOS_NOMBRE,numeroTienda);
            db.update(Utilidades.TABLA_TIENDA,values,Utilidades.CAMPO_ID+"=?",parametros);
            db.close();
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"no pos no" , Toast.LENGTH_LONG).show();
        }

    }
    public void ultimaVez(){
        ConexionSQLiteHelper conn = new ConexionSQLiteHelper(this, "db tienda", null, 1);
        SQLiteDatabase db = conn.getReadableDatabase();

        String sql="SELECT nombreT FROM tienda where id=1";
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            String numeroT=(cursor.getString(0));
            getTienda(numeroT);

        }
    }
    public void getTienda(String numeroT){

        try {
            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            ResultSet rs = st.executeQuery("select nombre from tiendas where numero="+numeroT+";");


            while (rs.next()) {


                listaTiendas.add(rs.getString(1));



            }


        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error al obtener datos del usuario", Toast.LENGTH_SHORT).show();
        }
    }


    public void consultarZona(){
        try {
            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            ResultSet rs = st.executeQuery("SELECT nombre FROM areasdecontrol where idTienda="+numeroTienda+";");


            while (rs.next()) {


                listaZonas.add(rs.getString(1));



            }


        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error al obtener datos del usuario", Toast.LENGTH_SHORT).show();
        }
    }



    private void obtenerTiendas() {
        ultimaVez();
        try {
            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            ResultSet rs = st.executeQuery("select  nombre from tiendas ");


            while (rs.next())
            {

                listaTiendas.add(rs.getString(1));
            }

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error en traer tiendas", Toast.LENGTH_SHORT).show();
        }
    }


    private void insertarEntrada() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd-hhmmss", Locale.getDefault());
        Date date = new Date();

        String fecha = dateFormat.format(date);

        String[] FechaHora;
        FechaHora=fecha.split("-");

        try {
            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            st.executeUpdate("insert into logdia (nombre, fecha, tienda, hora,origen, tipo, idEmpleado, caja, id, llave, autoriza) values ('"+nombreUsuario+"', getDate(),1,'"+FechaHora[1]+"', 1, 'ENTRADA',"+numeroUsuario+",1 ,92911, newId(), 0 );");



        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error al insertar en asistencias", Toast.LENGTH_SHORT).show();
        }
    }



    public void getNameUser(){
        ModeloUsuario mu=new ModeloUsuario();
        try {
            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            ResultSet rs = st.executeQuery("select nombre, numero from empleado where [user]='"+mu.getCorreo()+"';");


            while (rs.next()) {

                nombreUsuario=rs.getString(1);
                numeroUsuario=rs.getString(2);

            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error al obtener datos del usuario", Toast.LENGTH_SHORT).show();
        }
    }
    public void InsertarZona(String zona){


        ConexionSQLiteHelper conn = new ConexionSQLiteHelper(this, "db tienda", null, 1);
        SQLiteDatabase db=conn.getWritableDatabase();





        db.execSQL("INSERT INTO  zona  (zona) VALUES('"+zona+"')");

    }

    public void insertarAreaAsignada(){
        String id=getArea();
        try {

            if(check.isChecked()==true){
                id="-1";
            }
            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            String sql="insert into AreasAsignadas (idTienda,idArea,idEmpleado,Fecha,Hora) values ("+numeroTienda+",'"+id+"',"+numeroUsuario+",getDate(),getDate());";
            st.executeQuery(sql);


        } catch (Exception e) {

        }
        Toast toast = Toast.makeText(getApplicationContext(), "Cargando...", Toast.LENGTH_LONG);
        TextView x = (TextView) toast.getView().findViewById(android.R.id.message);
        x.setTextColor(Color.YELLOW);
        toast.show();
    }

    public String getArea(){
        String id=null;
        try {
            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            String sql="select idArea from areasdecontrol where nombre='"+spZona.getSelectedItem().toString()+"'";
            ResultSet rs=st.executeQuery(sql);

            while(rs.next()){
                id=rs.getString(1);
            }

        } catch (Exception e) {

        }
        return id;
    }


    public void crearComanderoTabla(){

        try{
            Statement st=bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            String sql="Create table comandero(numero varchar(50), tienda numeric(5,0), cliente nvarchar(50), fecha date, total decimal(18,2), status nchar(1), pares numeric(4,0), empleado varchar(50), impreso bit, llave uniqueidentifier);";
            st.executeUpdate(sql);
        }catch (SQLException e){
            e.getMessage();
            Toast.makeText(getApplicationContext(),"No se puede crear la tabla Comandero",Toast.LENGTH_LONG);
        }

    }

    public void crearAreasTabla(){

        try{
            Statement st=bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            String sql="Create table AreasDeControl(idArea numeric(18,0) IDENTITY, idTienda numeric(18,0), nombre nchar(50));";
            st.executeUpdate(sql);
        }catch (SQLException e){
            e.getMessage();
            Toast.makeText(getApplicationContext(),"No se puede crear la tabla AreasDeControl ",Toast.LENGTH_LONG);
        }

    }

    public void crearZonaTabla(){

        try{
            Statement st=bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            String sql="Create table AreasDeControl (idZona numeric(18,0) IDENTITY, idTienda nvarchar(50), nombre nchar(50), idArea numeric(18, 0))";
            st.executeUpdate(sql);
        }catch (SQLException e){
            e.getMessage();
            Toast.makeText(getApplicationContext(),"No se puede crear la tabla AreasDeControl ",Toast.LENGTH_LONG);
        }

    }


    public void crearAreaAsignadaTabla(){
        try{
            Statement st=bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            String sql="Create table AreasAsignadas (idTienda numeric(18,0), idArea nvarchar(50), idEmpleado numeric(18,0), fecha date, hora time(2))";
            st.executeUpdate(sql);
        }catch (SQLException e){
            e.getMessage();
            Toast.makeText(getApplicationContext(),"No se puede crear la tabla AreasDeControl ",Toast.LENGTH_LONG);
        }
    }

    public void crearUbicacionTabla(){
        try{
            Statement st=bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            String sql="Create table UbicacionesProductos (idTienda numeric(18,0), idZona numeric(18,0), barcode nvarchar(50), id numeric(18,0) IDENTITY, hora time(2))";
            st.executeUpdate(sql);
        }catch (Exception e){
            e.getMessage();
        }
    }


    public void crearComanderoDet(){
        try{
            Statement st=bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            String sql="Create table comanderoDet (numero varchar(50), tienda numeric(5,0), barcode nvarchar(50), estilo nvarchar(50),color nvarchar(50), marca nvarchar(50), acabado nvarchar(50), talla decimal(4,1), status char(1), ubicacion nvarchar(50), llave uniqueidentifier)";
            st.executeUpdate(sql);
        }catch (Exception e ){
            e.getMessage();
        }
    }



    public void crearComanderoLog(){
        try{
            Statement st=bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            String sql="Create table comanderoLog (numero varchar(50), fecha date, hora time(7), llave uniqueidentifier)";
            st.executeUpdate(sql);
        }catch (Exception e ){
            e.getMessage();
        }
    }




    public void dropSP(){
        try{
            Statement st=bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            String sql="drop procedure lupita";
            st.executeUpdate(sql);
        }catch (Exception e ){
            e.getMessage();
        }
    }


    public void crearSp(){
        try{
            Statement st=bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            String sql="\n" +
                    "\n" +
                    "CREATE PROCEDURE [dbo].[Lupita] @Barcode AS VARCHAR(8000) = NULL\n" +
                    "\t,@TallaBusca AS VARCHAR(5)\n" +
                    "\t,@TiendaBusca AS DECIMAL\n" +
                    "AS\n" +
                    "SET NOCOUNT ON\n" +
                    "\n" +
                    "DECLARE @SQL AS NVARCHAR(MAX)\n" +
                    "\t,@Tienda AS NVARCHAR(200)\n" +
                    "\t,@Talla AS NVARCHAR(5)\n" +
                    "\t,@Cantidad AS NVARCHAR(10)\n" +
                    "\t,@Tallas AS NVARCHAR(MAX)\n" +
                    "\t,@TallasSort AS NVARCHAR(MAX)\n" +
                    "\t,@TallasUpdate AS NVARCHAR(MAX)\n" +
                    "\n" +
                    "IF object_id('tempdb..##TempTallas') IS NOT NULL\n" +
                    "BEGIN\n" +
                    "\tDROP TABLE ##TempTallas\n" +
                    "END\n" +
                    "\n" +
                    "IF object_id('tempdb..##TempTienda') IS NOT NULL\n" +
                    "BEGIN\n" +
                    "\tDROP TABLE ##TempTienda\n" +
                    "END\n" +
                    "\n" +
                    "IF object_id('tempdb..##Temp') IS NOT NULL\n" +
                    "BEGIN\n" +
                    "\tDROP TABLE ##Temp\n" +
                    "END\n" +
                    "\n" +
                    "CREATE TABLE ##TempTienda (Tienda INT NOT NULL)\n" +
                    "\n" +
                    "IF @TiendaBusca <= 0\n" +
                    "BEGIN\n" +
                    "\tIF CAST(@TallaBusca AS decimal) > 0\n" +
                    "\tBEGIN\n" +
                    "\t\tSET @SQL = 'Insert Into ##TempTienda (Tienda) Select Distinct Tienda From Existen WHERE Barcode = ''' + @Barcode + ''' AND Talla = ' + CAST(@TallaBusca AS NVARCHAR(10)) + ' AND Cantidad > 0'\n" +
                    "\tEND\n" +
                    "\tElSE\n" +
                    "\tBEGIN\n" +
                    "\t\tSET @SQL = 'Insert Into ##TempTienda (Tienda) Select Distinct Tienda From Existen WHERE Barcode = ''' + @Barcode + ''' AND Cantidad > 0'\n" +
                    "\tEND\n" +
                    "END\n" +
                    "ELSE\n" +
                    "BEGIN\n" +
                    "\tIF CAST(@TallaBusca AS decimal) > 0\n" +
                    "\tBEGIN\n" +
                    "\t\tSET @SQL = 'Insert Into ##TempTienda (Tienda) Select Distinct Tienda From Existen WHERE Tienda = ' + CAST(@TiendaBusca AS NVARCHAR(10)) + ' AND Barcode = ''' + @Barcode + ''' AND Talla = ' + CAST(@TallaBusca AS NVARCHAR(10)) + ' AND Cantidad > 0'\n" +
                    "\tEND\n" +
                    "\tELSE\n" +
                    "\tBEGIN\n" +
                    "\t\tSET @SQL = 'Insert Into ##TempTienda (Tienda) Select Distinct Tienda From Existen WHERE Tienda = ' + CAST(@TiendaBusca AS NVARCHAR(10)) + ' AND Barcode = ''' + @Barcode + ''' AND Cantidad > 0'\n" +
                    "\tEND\n" +
                    "END\n" +
                    "EXECUTE (@SQL)\n" +
                    "\n" +
                    "CREATE TABLE ##Temp (\n" +
                    "\tBarcode VARCHAR(100) collate database_default NOT NULL\n" +
                    "\t,Tienda VARCHAR(100) collate database_default NOT NULL\n" +
                    "\t,Estilo VARCHAR(100) collate database_default NOT NULL\n" +
                    "\t,Color VARCHAR(100) collate database_default NOT NULL\n" +
                    "\t,Acabado VARCHAR(100) collate database_default NOT NULL\n" +
                    "\t,Marca VARCHAR(100) collate database_default NOT NULL\n" +
                    "\t,SubLinea VARCHAR(100) collate database_default NOT NULL\n" +
                    "\t,Linea VARCHAR(100) collate database_default NOT NULL\n" +
                    "\t,Corrida VARCHAR(100) collate database_default NOT NULL\n" +
                    "\t,Temporada VARCHAR(100) collate database_default NOT NULL\n" +
                    "\t,Basico BIT NOT NULL\n" +
                    "\t)\n" +
                    "\n" +
                    "DECLARE @CompTalla AS NVARCHAR(MAX)\n" +
                    "DECLARE @CompTiend AS NVARCHAR(MAX)\n" +
                    "DECLARE @SiHay AS INT\n" +
                    "\n" +
                    "SET @SiHay = 0\n" +
                    "\n" +
                    "IF CAST(@TallaBusca AS DECIMAL) <= 0\n" +
                    "BEGIN\n" +
                    "\tSET @CompTalla = ''\n" +
                    "END\n" +
                    "ELSE\n" +
                    "BEGIN\n" +
                    "\tSET @CompTalla = 'AND existen.Talla = ' + CAST(@TallaBusca AS NVARCHAR(10))\n" +
                    "END\n" +
                    "\n" +
                    "IF @TiendaBusca <= 0\n" +
                    "BEGIN\n" +
                    "\tSET @CompTiend = ''\n" +
                    "END\n" +
                    "ELSE\n" +
                    "BEGIN\n" +
                    "\tSET @CompTiend = ' AND existen.TIENDA = ' + CAST(@TiendaBusca AS NVARCHAR(10))\n" +
                    "END\n" +
                    "\n" +
                    "SET @SQL = 'SELECT existen.BARCODE, tiendas.NOMBRE AS Tienda, existen.TALLA, existen.CANTIDAD Into ##TempTallas FROM existen INNER JOIN tiendas ON existen.TIENDA = tiendas.NUMERO INNER JOIN  articulo ON existen.BARCODE = articulo.BARCODE INNER JOIN colores ON articulo.COLOR = colores.numero INNER JOIN acabados ON articulo.ACABADO = acabados.numero INNER JOIN marcas ON articulo.MARCA = marcas.numero INNER JOIN sublinea ON articulo.SUBLINEA = sublinea.numero INNER JOIN lineas ON articulo.LINEA = lineas.NUMERO inner join corridas on corridas.id=articulo.corrida inner join temporad on temporad.numero=articulo.temporad Where (Existen.Tienda In (Select Tienda From ##TempTienda)) And (Existen.Cantidad <> 0) ' + @CompTalla + @CompTiend + ' and articulo.barcode in(''' + @Barcode + ''')'\n" +
                    "EXECUTE (@SQL)\n" +
                    "\n" +
                    "SET @sql = 'DECLARE temp_cursor CURSOR FOR SELECT Distinct existen.TALLA FROM existen INNER JOIN tiendas ON existen.TIENDA = tiendas.NUMERO INNER JOIN articulo ON existen.BARCODE = articulo.BARCODE INNER JOIN colores ON articulo.COLOR = colores.numero INNER JOIN acabados ON articulo.ACABADO = acabados.numero INNER JOIN marcas ON articulo.MARCA = marcas.numero INNER JOIN sublinea ON articulo.SUBLINEA = sublinea.numero INNER JOIN lineas ON articulo.LINEA = lineas.NUMERO inner join corridas on corridas.id=articulo.corrida Where (Existen.Tienda In (Select Tienda From ##TempTienda)) And (Existen.Cantidad <> 0) and articulo.barcode in( ''' + @Barcode + ''') ' + @CompTalla + @CompTiend + ' Order by Talla'\n" +
                    "EXECUTE (@SQL)\n" +
                    "\n" +
                    "OPEN temp_cursor\n" +
                    "\n" +
                    "FETCH NEXT\n" +
                    "FROM temp_cursor\n" +
                    "INTO @Talla\n" +
                    "\n" +
                    "SET @Tallas = ''\n" +
                    "SET @TallasSort = ''\n" +
                    "SET @TallasUpdate = ''\n" +
                    "\n" +
                    "WHILE @@FETCH_STATUS = 0\n" +
                    "BEGIN\n" +
                    "\tSET @SiHay = 1\n" +
                    "\tSET @Tallas = @Tallas + 'Isnull([' + @Talla + '], 0) + '\n" +
                    "\tSET @TallasSort = @TallasSort + 'Isnull([' + @Talla + '],0) as [' + @Talla + '],'\n" +
                    "\tSET @TallasUpdate = @TallasUpdate + '[' + @Talla + '] = (Select top 1 Cantidad From ##TempTallas Where (Barcode = ##Temp.Barcode) And (Tienda = ##Temp.Tienda) And (Talla = ' + @Talla + ')),'\n" +
                    "\tSET @SQL = 'ALTER TABLE ##Temp ADD [' + @Talla + '] int NULL '\n" +
                    "\tEXECUTE (@SQL)\n" +
                    "\n" +
                    "\tFETCH NEXT\n" +
                    "\tFROM temp_cursor\n" +
                    "\tINTO @Talla\n" +
                    "END\n" +
                    "\n" +
                    "CLOSE temp_cursor\n" +
                    "\n" +
                    "DEALLOCATE temp_cursor\n" +
                    "\n" +
                    "IF @SiHay = 1\n" +
                    "BEGIN\n" +
                    "\tBEGIN TRY\n" +
                    "\t\tSET @Tallas = Left(@Tallas, Len(@Tallas) - 2)\n" +
                    "\t\tSET @TallasUpdate = Left(@TallasUpdate, Len(@TallasUpdate) - 1)\n" +
                    "\n" +
                    "\t\tALTER TABLE ##Temp ADD Total INT NULL\n" +
                    "\n" +
                    "\t\tSET @SQL = 'Insert Into ##Temp (Barcode, Tienda, Estilo, Color, Acabado, Marca, SubLinea, Linea, Corrida, Temporada, Basico) SELECT distinct existen.BARCODE, tiendas.NOMBRE AS Tienda, articulo.ESTILO, colores.COLOR, acabados.ACABADO, marcas.MARCA, sublinea.SUBLINEA, Lineas.Linea, Corridas.Nombre, Temporad.Temporad, isnull(Articulo.basico,0) AS Basico FROM existen INNER JOIN tiendas ON existen.TIENDA = tiendas.NUMERO INNER JOIN articulo ON existen.BARCODE = articulo.BARCODE INNER JOIN colores ON articulo.COLOR = colores.numero INNER JOIN acabados ON articulo.ACABADO = acabados.numero INNER JOIN marcas ON articulo.MARCA = marcas.numero INNER JOIN sublinea ON articulo.SUBLINEA = sublinea.numero INNER JOIN lineas ON articulo.LINEA = lineas.NUMERO inner join corridas on corridas.id=articulo.corrida inner join temporad on temporad.numero=articulo.temporad Where (Existen.Tienda In (Select Tienda From ##TempTienda)) And (Existen.Cantidad <> 0) and articulo.barcode in (''' + @Barcode + \n" +
                    "\t\t\t''') Order by articulo.ESTILO, colores.COLOR, acabados.ACABADO, tiendas.NOMBRE'\n" +
                    "\t\tEXECUTE (@SQL)\n" +
                    "\n" +
                    "\t\t--SELECT * INTO dbo.datos FROM ##Temp \n" +
                    "\t\t--DECLARE temp_cursor CURSOR FOR \n" +
                    "\t\tSET @SQL = 'Update ##Temp Set ' + @TallasUpdate\n" +
                    "\t\tEXECUTE (@SQL)\n" +
                    "\n" +
                    "\t\tSET @SQL = 'Update ##Temp Set Total = ' + @Tallas\n" +
                    "\t\tEXECUTE (@SQL)\n" +
                    "\n" +
                    "\t\t--Set @SQL = 'Select Tienda, Estilo, Color, Acabado, Marca, SubLinea, Linea,Corrida,Temporada,Basico,' + @TallasSort + ' Total From ##Temp  '\n" +
                    "\t\tSET @SQL = 'Select Tienda, ' + @TallasSort + ' Total From ##Temp'\n" +
                    "\t\tEXECUTE (@SQL)\n" +
                    "\tEND TRY\n" +
                    "\tBEGIN CATCH\n" +
                    "\t\tSELECT ERROR_NUMBER() AS ErrorNumber\n" +
                    "\t\t\t,ERROR_SEVERITY() AS ErrorSeverity\n" +
                    "\t\t\t,ERROR_STATE() AS ErrorState\n" +
                    "\t\t\t,ERROR_PROCEDURE() AS ErrorProcedure\n" +
                    "\t\t\t,ERROR_LINE() AS ErrorLine\n" +
                    "\t\t\t,ERROR_MESSAGE() AS ErrorMessage;\n" +
                    "\tEND CATCH\n" +
                    "END\n" +
                    "\n" +
                    "IF object_id('tempdb..##TempTallas') IS NOT NULL\n" +
                    "BEGIN\n" +
                    "\tDROP TABLE ##TempTallas\n" +
                    "END\n" +
                    "\n" +
                    "IF object_id('tempdb..##TempTienda') IS NOT NULL\n" +
                    "BEGIN\n" +
                    "\tDROP TABLE ##TempTienda\n" +
                    "END\n" +
                    "\n" +
                    "IF object_id('tempdb..##Temp') IS NOT NULL\n" +
                    "BEGIN\n" +
                    "\tDROP TABLE ##Temp\n" +
                    "END";
            st.executeUpdate(sql);
        }catch (Exception e ){
            e.getMessage();
        }
    }

}
