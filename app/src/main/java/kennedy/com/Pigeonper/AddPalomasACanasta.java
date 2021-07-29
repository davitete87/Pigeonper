package kennedy.com.Pigeonper;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.ls.LSOutput;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import kennedy.com.Pigeonper.Entidades.Canasta;
import kennedy.com.Pigeonper.Entidades.HistorialPerformance;
import kennedy.com.Pigeonper.Entidades.Localidad;
import kennedy.com.Pigeonper.Entidades.Palomas;



public class AddPalomasACanasta extends AppCompatActivity {

    private int dia, mes, anio, hora, minutos;
    RequestQueue rq;
    Button botonEditarCanasta, btnFechaCanasta, btnHoraCanasta, btnGuardarCanastaEditada, btnCancelarAddPalomasACanasta, btnEjecutarAG;
    EditText txtLocalidad, txtNombreCanasta, txtFechaCanasta, txtHoraCanasta;
    Spinner spinnerLinea, spinnerLocalidad;
    public ProgressDialog progressDialog;

    public  Canasta canasta;
    public ArrayList<Palomas> listapalomas;
    public ArrayList<Palomas> listaGanadora;
    public ArrayList<HistorialPerformance> historialPerformances;
    public boolean cargaListaAG =false;
    public int cant_de_palomas_en_lista=0; // saber si sobreescribe datos o inserta directamente.
    public double distancia;
    AdapterPalomasEnCanasta adapterPalomasEnCanasta;
    RecyclerView recyclerViewPalomas;
    ArrayList<String> listaNombresLinea;
    ArrayList<Localidad> loc;
    Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_palomas_a_canasta);
        listapalomas = new ArrayList<>();
        listaGanadora = new ArrayList<>();
        historialPerformances =new ArrayList<>();
        listaNombresLinea=new ArrayList<>();
        loc = new ArrayList<>();

        progressDialog = new ProgressDialog(AddPalomasACanasta.this);

        obtenerDetalleCanasta();

        inicializar();

/////////TOMAR LOS DATOS
        Intent intent = getIntent();
        canasta = (Canasta) intent.getExtras().getSerializable("canasta");
        distancia=(double) intent.getExtras().getSerializable("distancia");
        listaNombresLinea=(ArrayList<String>) intent.getExtras().getSerializable("listaLinea");
        loc=(ArrayList<Localidad>) intent.getExtras().get("listaLoc");
        //Toast.makeText(getApplicationContext(),"La distancia es: "+distancia, Toast.LENGTH_SHORT).show();
//////////////////////////////
       // txtLocalidad.setText(String.valueOf(canasta.getIdLocalidad()));
        txtNombreCanasta.setText(canasta.getNombre());
        txtFechaCanasta.setText(canasta.getDia());
        txtHoraCanasta.setText(canasta.getHora());

///////////////////////////////////////////

        ArrayAdapter<CharSequence> adaptador = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item,listaNombresLinea);
        spinnerLinea.setAdapter(adaptador);
        spinnerLinea.setSelection(canasta.getIdHabitaculo());
        spinnerLinea.setEnabled(false);
        //guardo el item seleccionado del combo
        /*spinnerLinea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                nombredeLineaSeleccionada[0] = (String) spinnerLinea.getAdapter().getItem(i);

                if(nombredeLineaSeleccionada[0].equals("Todas")){

                    // Toast.makeText(getActivity(), "ERRORRRR", Toast.LENGTH_SHORT).show();
                }else {

                    //Toast.makeText(getActivity(), "Linea: "+idLinea.get(i-1), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });*/


        //-------------------------- SPONNER LOCALIDAD
        ArrayList<String> nombresLoc = new ArrayList<>();
        for(int i=0 ; i<loc.size();i++) nombresLoc.add(i,loc.get(i).getNombreLocalidad());

        ArrayAdapter<CharSequence> adaptadorLoc = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item,nombresLoc);
        spinnerLocalidad.setAdapter(adaptadorLoc);

        int indiceLoc=0;
        for (int i=0 ; i<loc.size();i++){
            if(i == canasta.getIdLocalidad()){
                indiceLoc=canasta.getIdLocalidad()-1;
            }
        }
        spinnerLocalidad.setSelection(indiceLoc);
        spinnerLocalidad.setEnabled(false);

        /*spinnerLocalidad.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                nombredeLineaSeleccionada[0] = (String) spinnerLocalidad.getAdapter().getItem(i);

                if(nombredeLineaSeleccionada[0].equals("Todas")){

                    // Toast.makeText(getActivity(), "ERRORRRR", Toast.LENGTH_SHORT).show();
                }else {

                    //Toast.makeText(getActivity(), "Linea: "+idLinea.get(i-1), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });*/

//////////////////////////////////////////////////////////////////////////////////////

        botonEditarCanasta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // txtLocalidad.setEnabled(true);
                txtNombreCanasta.setEnabled(true);
                txtFechaCanasta.setEnabled(true);
                txtHoraCanasta.setEnabled(true);
                btnFechaCanasta.setEnabled(true);
                btnHoraCanasta.setEnabled(true);
                btnGuardarCanastaEditada.setEnabled(true);
                spinnerLocalidad.setEnabled(true);
                spinnerLinea.setEnabled(true);
            }
        });

        btnFechaCanasta.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                dia = c.get(Calendar.DAY_OF_MONTH);
                mes = c.get(Calendar.MONTH);
                anio = c.get(Calendar.YEAR);

                DatePickerDialog datePickerDialog = new DatePickerDialog(AddPalomasACanasta.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        if (txtFechaCanasta.isEnabled())
                            txtFechaCanasta.setText(i2 + "/" + (i1 + 1) + "/" + i);
                    }
                }, dia, mes, anio);

                datePickerDialog.show();
            }
        });

        btnHoraCanasta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Calendar c = Calendar.getInstance();
                hora = c.get(Calendar.HOUR_OF_DAY);
                minutos = c.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(AddPalomasACanasta.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        if (txtHoraCanasta.isEnabled()) {
                            if (i1 >= 0 && i1 <= 9) {
                                txtHoraCanasta.setText(i + ":0" + i1);

                            } else txtHoraCanasta.setText(i + ":" + i1);
                        }
                    }
                }, hora, minutos, false);

                timePickerDialog.show();
            }
        });

        btnCancelarAddPalomasACanasta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

////////////////////////////////////////////////////////////////////////////////////////////////////


        btnEjecutarAG.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                cargarArrayDePalomasEnUnHabitaculo();
                cargaListaAG =true;
            }//Fin onClick
        });
//////////////////////////////

        if(cargaListaAG==false) {

            cargarPalomasEnCanasta();
            mostrarLista(listapalomas);
        }
    }//FIN ONCREATE

////////////////////////////////////INICIALIZACION ////////////////////////////////////////////////////

    private void inicializar() {

        recyclerViewPalomas = findViewById(R.id.idRecyclerPalomasEnCanasta);

        botonEditarCanasta = (Button) findViewById(R.id.btnEditarCanasta);
        btnFechaCanasta = (Button) findViewById(R.id.btnFechaCanasta);
        btnHoraCanasta = (Button) findViewById(R.id.btnHoraCanasta);
        btnGuardarCanastaEditada = (Button) findViewById(R.id.btnGuardarCanastaEditada);
        btnCancelarAddPalomasACanasta = (Button) findViewById(R.id.idbtnCancelarAddPalomasACanasta);
        btnEjecutarAG = (Button) findViewById(R.id.idbtnEjecutarAG);

        txtNombreCanasta = (EditText) findViewById(R.id.etNomCanastaEdit);
        //txtLocalidad = (EditText) findViewById(R.id.etCiudadEdit);
        spinnerLocalidad = (Spinner) findViewById(R.id.spinner);
        spinnerLinea = (Spinner) findViewById(R.id.spinnerLinea);
        txtFechaCanasta = (EditText) findViewById(R.id.etFechaCanasta);
        txtHoraCanasta = (EditText) findViewById(R.id.ettHoraCanasta);

    }

///////////////////////////////////////////////////////////////////////////////////////////////////////////7

    private void cargarPalomasEnCanasta() {

        String URL = "http://" + LocalHost.LOCALHOST + ":3333/pigeonper/consultar_palomasEnCanasta.php?idCanasta=" + canasta.getIdCanasta();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                JSONObject jo = null;
                listapalomas.clear();
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jo = response.getJSONObject(i);

                        listapalomas.add(new Palomas(
                                jo.getInt("edad"),
                                jo.getInt("numero"),
                                jo.getInt("idHabitaculo"),
                                jo.getString("raza"),
                                jo.getInt("idPaloma"),
                                jo.getInt("Disponible")
                        ));
                        adapterPalomasEnCanasta.notifyDataSetChanged();//IMPORTANTE!!!! SIN ESTO NO ACTUALIZA EL ADAPTER EN EL MENU O EN CUALQUIER OTRA SELECCION Y NO SE VE EL RECYCLER!!!

                    } catch (Exception e) {

                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                //System.out.println(listapalomas.size());
                //Toast.makeText(getApplicationContext(), "lista vacia perrito malvado", Toast.LENGTH_SHORT).show();
            }
        }
        );

        rq = Volley.newRequestQueue(AddPalomasACanasta.this);
        rq.add(jsonArrayRequest);

    }

/////////////////////////////////////////////////////////

    private void mostrarLista(ArrayList<Palomas> listaParaMostrar) {

        recyclerViewPalomas.setLayoutManager(new LinearLayoutManager(AddPalomasACanasta.this));
        adapterPalomasEnCanasta = new AdapterPalomasEnCanasta(AddPalomasACanasta.this, listaParaMostrar);
        recyclerViewPalomas.setAdapter(adapterPalomasEnCanasta);

    }

///////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void cargarArrayDePalomasEnUnHabitaculo() {

        String URL = "http://" + LocalHost.LOCALHOST + ":3333/pigeonper/consultar_palomasEnHabitaculoV2.php?idHabitaculo="+canasta.getIdHabitaculo();///////// CAMBIAR EL 1 POR LA VARIABLE DEL HABITACULO DE LA CANASTA

        cant_de_palomas_en_lista=listapalomas.size();

        //System.out.println("HHHABITACUUULOOOOOO: "+canasta.getIdHabitaculo());

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {

            public void onResponse(JSONArray response) {
                JSONObject jo = null;
                listapalomas.clear(); //PROBANDOOOOO
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jo = response.getJSONObject(i);

                        listapalomas.add(new Palomas(
                                jo.getInt("edad"),
                                jo.getInt("numero"),
                                jo.getInt("idHabitaculo"),
                                jo.getString("raza"),
                                jo.getInt("idPaloma"),
                                jo.getInt("Disponible")
                        ));
                        //adapterPalomasEnCanasta.notifyDataSetChanged();//IMPORTANTE!!!! SIN ESTO NO ACTUALIZA EL ADAPTER EN EL MENU O EN CUALQUIER OTRA SELECCION Y NO SE VE EL RECYCLER!!!

                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                //SI LAS PALOMAS DISPONIBLES SON MENOS QUE LAS PERMITIDAS PARA COMPETIR NO EJECUTA EL AG
                if(listapalomas.size() >= Ag.CANT_DE_INDIVIDUOS_A_SELECCIONAR_EN_CROMOSOMA){
                    deseaSobreescribirPalomas();

                }else{
                    Toast.makeText(AddPalomasACanasta.this, "LA CANT DE PALOMAS DISPONIBLES ES MENOR A LA CANTIDAD A ENVIAR A COMPETICIÓN - ("+Ag.CANT_DE_INDIVIDUOS_A_SELECCIONAR_EN_CROMOSOMA+")", Toast.LENGTH_SHORT).show();
                    finish();
                }
                //-----------------------------------------------------------------------------------

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error en la conexion", Toast.LENGTH_SHORT).show();

            }
        });
        rq = Volley.newRequestQueue(AddPalomasACanasta.this);
        rq.add(jsonArrayRequest);

    }

// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void deseaSobreescribirPalomas() {

        if (cant_de_palomas_en_lista!=0){
            AlertDialog.Builder alerta = new AlertDialog.Builder(AddPalomasACanasta.this);
            alerta.setMessage("La canasta seleccionada contiene palomas. \nDesea remplazarlas?")
                    .setCancelable(false)
                    .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            progressDialog.setTitle("Ejecutando Algoritmo Genetico");
                            progressDialog.setMessage("Aguarde a que termine de ejecutar el A.G.");
                            progressDialog.setCancelable(false);
                            progressDialog.show();

                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    ejecutarAG();
                                    ReemplazarPalomasBD();///////////////////////////////////////////////////////// SOBRESCRIBE PALOMA
                                    progressDialog.dismiss();
                                   //mostrarLista(listaGanadora);

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            mostrarLista(listaGanadora);
                                        }
                                    });
                                }
                            }).start();

                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    });

            AlertDialog titulo = alerta.create();
            titulo.setTitle("Canasta ocupada");
            titulo.show();

        }else{
            AlertDialog.Builder alerta = new AlertDialog.Builder(AddPalomasACanasta.this);
            alerta.setMessage("Desea ingresar las palomas?")
                    .setCancelable(false)
                    .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            progressDialog.setTitle("Ejecutando Algoritmo Genetico");
                            progressDialog.setMessage("Aguarde a que termine de ejecutar el A.G.");
                            progressDialog.setCancelable(false);
                            progressDialog.show();

                           new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    ejecutarAG();
                                    insertarDetalle_Palomas();/////////////////////////////////////////////////////// INSERTAR PALOMA
                                    progressDialog.dismiss();

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            mostrarLista(listaGanadora);
                                        }
                                    });
                                }
                            }).start();
                            ////////////////////////////
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    });

            AlertDialog titulo = alerta.create();
            titulo.setTitle("Ingreso de palomas");
            titulo.show();
        }

    }

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        private void insertarDetalle_Palomas() {

        String URL="http://"+ LocalHost.LOCALHOST+":3333/pigeonper/insertar_detalle_canasta.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AddPalomasACanasta.this, "ERROR DE CONEXION insertarDetalle_canastas.PHP", Toast.LENGTH_SHORT).show();
            }
        }

        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                JSONObject jsonObject= new JSONObject();
                JSONArray jsonArray = null;

                Map<String,String> param =  new HashMap<>();

                try {
                    for(int i=0;i<listaGanadora.size(); i++) {

                        jsonArray=new JSONArray();

                        jsonArray.put(listaGanadora.get(i).getRaza());
                        jsonArray.put(String.valueOf(listaGanadora.get(i).getEdad()));
                        jsonArray.put(String.valueOf(listaGanadora.get(i).getId_Paloma()));
                        jsonArray.put(String.valueOf(listaGanadora.get(i).getIdHabitaculo()));
                        jsonArray.put(String.valueOf(listaGanadora.get(i).getNumero()));

                        jsonObject.put("paloma"+i,jsonArray);
                        jsonObject.put("idCanasta",canasta.getIdCanasta());

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                param.put("listaGanadora",jsonObject.toString());
                param.put("idCanasta",String.valueOf(canasta.getIdCanasta()));
                return param;
            }
        };

        rq.add(stringRequest);

    }

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void ReemplazarPalomasBD() {


        String URL="http://"+ LocalHost.LOCALHOST+":3333/pigeonper/ReemplazarPalomas_detalle_canasta.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AddPalomasACanasta.this, "ERROR DE CONEXION ReemplazarPalomas_detalle_canasta.PHP", Toast.LENGTH_SHORT).show();
            }
        }

        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                JSONObject jsonObject= new JSONObject();
                JSONArray jsonArray = null;

                Map<String,String> param =  new HashMap<>();

                try {
                for(int i=0;i<listaGanadora.size(); i++) {

                    jsonArray=new JSONArray();

                    jsonArray.put(listaGanadora.get(i).getRaza());
                    jsonArray.put(String.valueOf(listaGanadora.get(i).getEdad()));
                    jsonArray.put(String.valueOf(listaGanadora.get(i).getId_Paloma()));
                    jsonArray.put(String.valueOf(listaGanadora.get(i).getIdHabitaculo()));
                    jsonArray.put(String.valueOf(listaGanadora.get(i).getNumero()));

                    jsonObject.put("paloma"+i,jsonArray);
                    jsonObject.put("idCanasta",canasta.getIdCanasta());

                }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                param.put("listaGanadora",jsonObject.toString());
                param.put("idCanasta",String.valueOf(canasta.getIdCanasta()));
                return param;
            }
        };

        rq.add(stringRequest);

    }

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void ejecutarAG() {

        Context contexto;
        contexto=AddPalomasACanasta.this;
        Ag algoritmoGenetico = new Ag(listapalomas, historialPerformances,distancia,contexto);
        listaGanadora=algoritmoGenetico.getListaGanadora();

    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void obtenerDetalleCanasta() {

        String URL = "http://" + LocalHost.LOCALHOST + ":3333/pigeonper/consultar_detalle_canasta.php";
        JsonArrayRequest jsonRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                historialPerformances.clear();
                JSONObject jo=null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jo = response.getJSONObject(i);

                        //int idHistorialPerformance, int idPaloma, int idLocalidad, int hora, String fecha, float velocidad, float distancia
                        historialPerformances.add(new HistorialPerformance(
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
        rq = Volley.newRequestQueue(AddPalomasACanasta.this);
        rq.add(jsonRequest);

    }


}



