package kennedy.com.Pigeonper;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Menu;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import kennedy.com.Pigeonper.Entidades.HistorialPerformance;
import kennedy.com.Pigeonper.Entidades.Usuario;
import kennedy.com.Pigeonper.ui.gallery.GalleryFragment;
import kennedy.com.Pigeonper.ui.encanastar.AddCanastas;
import kennedy.com.Pigeonper.ui.slideshow.SlideshowFragment;

public class MainAdminActivity extends AppCompatActivity {

    private RequestQueue rq;
    ArrayList<HistorialPerformance> historialPerformanceArrayList =new ArrayList<>();

    private AppBarConfiguration mAppBarConfiguration;
    private DrawerLayout drawer;
    private NavigationView navigationView;

    private FragmentManager fm;
    private FragmentTransaction ft;
    Usuario us = new Usuario();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //obtenerDetalleCanasta();

        setContentView(R.layout.activity_main_admin);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        navigationView = findViewById(R.id.nav_view);
        drawer = findViewById(R.id.drawer_layout);

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_pacientes, R.id.nav_perfil, R.id.nav_turnos)
                .setDrawerLayout(drawer)
                .build();
        final NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);

        NavigationUI.setupWithNavController(navigationView, navController);
        navigationView.bringToFront();

        NavigationUI.setupActionBarWithNavController(this, navController, drawer);
        NavigationUI.setupWithNavController(navigationView, navController);


/////////////////////////////////////////////////////////////////////////////////////////////
        final Intent intent = getIntent();
        us = (Usuario) intent.getExtras().getSerializable("usuario");

       //Toast.makeText(this, us.getDireccion(), Toast.LENGTH_SHORT).show();


////////////////////////////////////////////////////////////////////////////////////////////

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int id=item.getItemId();

                switch (id) {

                    case R.id.nav_encanastar:
                        getSupportActionBar().setTitle(R.string.menu_encanastar);// ACUTIALIZA EL TITULO DE LA BARRA
                        fm=getSupportFragmentManager();
                        ft=fm.beginTransaction();

                        Fragment HomeFragment = new AddCanastas();
                        //Bundle b = new Bundle();
                        //b.putSerializable("dc",detalleCanastaArrayList);
                        //HomeFragment.setArguments(b);
                        ft.replace(R.id.nav_host_fragment, HomeFragment);
                        ft.commit();
                        break;


                    case R.id.nav_perfil:
                        getSupportActionBar().setTitle(R.string.menu_perfil); // ACUTIALIZA EL TITULO DE LA BARRA
                        fm=getSupportFragmentManager();
                        ft=fm.beginTransaction();
                        ft.replace(R.id.nav_host_fragment, new GalleryFragment());
                        ft.commit();
                        break;

                    case R.id.nav_palomas:
                        getSupportActionBar().setTitle(R.string.menu_palomas);// ACUTIALIZA EL TITULO DE LA BARRA
                        fm=getSupportFragmentManager();
                        ft=fm.beginTransaction();
                        ft.replace(R.id.nav_host_fragment, new SlideshowFragment());
                        ft.commit();
                        break;
                }

                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });


    }//FIN ON CREATE
/////////////////////////////////////////////////////////////////////////////////////////////

    private void obtenerDetalleCanasta() {

        String URL = "http://" + LocalHost.LOCALHOST + ":3333/pigeonper/consultar_detalle_canasta.php";
        JsonArrayRequest jsonRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                historialPerformanceArrayList.clear();
                JSONObject jo=null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jo = response.getJSONObject(i);

                        //int idHistorialPerformance, int idPaloma, int idLocalidad, int hora, String fecha, float velocidad, float distancia
                        historialPerformanceArrayList.add(new HistorialPerformance(
                                jo.getInt("idHistorial_Performance"),
                                jo.getInt("idPaloma"),
                                jo.getInt("idLocalidad"),
                                jo.getInt("hora"),
                                jo.getString("fecha"),
                                jo.getDouble("velocidad"),
                                jo.getDouble("distancia")
                        ));

                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        rq = Volley.newRequestQueue(MainAdminActivity.this);
        rq.add(jsonRequest);

    }

//////////////////////////////////////////////////////////////////////////////////////////////////

    public Usuario getusu(){
        return us;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_admin, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }



}

