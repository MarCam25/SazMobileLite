package com.secuencia.saz.sazmobilelite;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
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

public class menu extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    String usuario, numeroUsuario, nombreUsuario;
    ModeloUsuario mu=new ModeloUsuario();
    String empresa,listalista;

    public static Boolean buscador=false;

    ConexionSQLiteHelper conn=new ConexionSQLiteHelper(this,"db tienda",null,1);

    ModeloTienda mt=new ModeloTienda();
    FragmentManager fm=getSupportFragmentManager();
    ConexionSqlServer conex=new ConexionSqlServer();
    ModeloEmpresa me=new ModeloEmpresa();
    ConexionBDCliente bdc=new ConexionBDCliente();
    TextView idTienda;
    ArrayList listaTiendas=new ArrayList();

    String sucursal;

    ModeloDatos md=new ModeloDatos();
    FragmentManager fmp=getSupportFragmentManager();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        obtenerLineaConexion();
        consultarT();
        ultimaVez();
        setSupportActionBar(toolbar);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        usuario= getIntent().getStringExtra("Usuario");
        getSupportActionBar().setTitle("SazMobile Lite -Existencias-");
        if(Principal.passConsulta==true) {
            fm.beginTransaction().replace(R.id.contenedorMenu, new ConsultaF()).commit();
        }
        //  Intent  intent= new Intent(getApplicationContext(), Hamburguesa.class);


        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.usu);
        TextView navSucursal=(TextView) headerView.findViewById(R.id.sucu);

        navUsername.setText("Usuario: "+mu.getNombre());
        navSucursal.setText("Sucursal: "+mt.getNumeroTienda());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        navigationView.setNavigationItemSelectedListener(this);


        getSupportActionBar().setTitle("SazMobile Lite App -Inicio-");

        if(Principal.location==1){
            getSupportActionBar().setTitle("Comandero");
            fm.beginTransaction().replace(R.id.contenedorMenu, new Coman()).commit();
            Principal.location=0;
        }else if(Principal.location==2){
            getSupportActionBar().setTitle("Venta");
            fm.beginTransaction().replace(R.id.contenedorMenu, new Venta()).commit();
            Principal.location=0;

        }else if(Principal.location==3){
            getSupportActionBar().setTitle("Configuración");
            fm.beginTransaction().replace(R.id.contenedorMenu, new Configuracion()).commit();
            Principal.location=0;

        }
    }



    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {

        }
        return true;
    }


    public void obtenerLineaConexion() {
        String empress = getIntent().getStringExtra("Empresa");

        me = new ModeloEmpresa();

        {
            try {
                Statement st = conex.conexionBD().createStatement();
                ResultSet rs = st.executeQuery("select  Server, Usuariosvr,PassSvr, basededatos, empresa from logins where idEmpresa= " + empress + " and status=1 and borrado=0 ");

                while (rs.next()) {

                    me.setServer(rs.getString("Server"));
                    me.setUsuario(rs.getString("Usuariosvr"));
                    me.setPass(rs.getString("PassSvr"));
                    me.setBase(rs.getString("basededatos"));
                    me.setEmpresa(rs.getString("empresa"));

                }


            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Error en la linea de conexion", Toast.LENGTH_SHORT).show();
            }
        }
    }






    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    private void insertarSalida() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd-hh:mm:ss", Locale.getDefault());
        Date date = new Date();

        String fecha = dateFormat.format(date);

        String[] FechaHora;
        FechaHora=fecha.split("-");

        try {
            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            String sql="insert into logdia (nombre, fecha, tienda, hora,origen, tipo, idEmpleado, caja, id, llave, autoriza) values ('"+nombreUsuario+"', getDate(),"+mt.getNombreTienda()+",'"+FechaHora[1]+"', 1, 'SALIDA',"+numeroUsuario+",1 ,92911, newId(), 0 );";
            st.executeUpdate(sql);



        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error al checar salida", Toast.LENGTH_SHORT).show();
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


                mt.setNumeroTienda(rs.getString(1));



            }


        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error al obtener datos del usuario", Toast.LENGTH_SHORT).show();
        }
    }

    //Obtenemos el nombre del ususario
    public void getNameUser(){
        String usu= getIntent().getStringExtra("Usuario");
        try {
            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            String sql="select nombre, numero from empleado where [user]='"+mu.getCorreo()+"';";
            ResultSet rs = st.executeQuery(sql);


            while (rs.next()) {


                nombreUsuario=rs.getString(1);
                numeroUsuario=rs.getString(2);


            }


        } catch (SQLException e) {
            e.getMessage();
            Toast.makeText(getApplicationContext(), "Error al obtener datos del usuario", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if (id == R.id.action_settings) {
            Intent intent=new Intent(getApplicationContext(), Configuraciones.class);
            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {

            fm.beginTransaction().replace(R.id.contenedorMenu, new ConsultaF()).commit();
           // startActivity(intent);
        } else if (id == R.id.generales) {



        } else if (id == R.id.nav_manage) {
          //  getSupportActionBar().setTitle("Comandero");
          //  fm.beginTransaction().replace(R.id.contenedorMenu, new Coman()).commit();
        } else if (id == R.id.nav_new) {
           // getSupportActionBar().setTitle("Nuevo Empleado");
           // fm.beginTransaction().replace(R.id.contenedorMenu, new AddUser()).commit();
        } else if (id == R.id.nav_vender) {
           // getSupportActionBar().setTitle("Vender");
           // fm.beginTransaction().replace(R.id.contenedorMenu, new Venta()).commit();
        }else if (id == R.id.nav_cerrar) {
            getNameUser();
            insertarSalida();
            desactivado();
            //cerramos secion en la app
            Intent  intent= new Intent(getApplicationContext(), Principal.class);
            //Cancelamos los hilos de notificaciones

            startActivity(intent);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void desactivado(){
        try{
            Statement st= conex.conexionBD().createStatement();
            String sql="update smAppAccesos set activo=0 where mail='"+mu.getCorreo()+"'";
            st.executeUpdate(sql);
        }catch (Exception e ){
            e.getMessage();
            Toast.makeText(getApplicationContext(), "No sé puede Desactivar", Toast.LENGTH_SHORT).show();
        }
    }
    private void consultarT() {
        SQLiteDatabase db=conn.getReadableDatabase();
        String[] parametros={"1"};
        String[] campos={Utilidades.CAMPOS_NOMBRE};
        try {
            Cursor cursor = db.query(Utilidades.TABLA_TIENDA, campos, Utilidades.CAMPO_ID+ "=?", parametros, null, null, null);
            cursor.moveToFirst();
            mt.setNombreTienda(cursor.getString(0));
            cursor.close();

        }catch (Exception e){
            Toast.makeText(this,"error No.45" , Toast.LENGTH_LONG).show();
        }

    }
}
