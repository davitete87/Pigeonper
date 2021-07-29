package kennedy.com.Pigeonper.ui.encanastar;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import kennedy.com.Pigeonper.AdapterCanastas;
import kennedy.com.Pigeonper.AddPalomasACanasta;
import kennedy.com.Pigeonper.Entidades.Canasta;
import kennedy.com.Pigeonper.Entidades.HistorialPerformance;
import kennedy.com.Pigeonper.Entidades.Localidad;
import kennedy.com.Pigeonper.Entidades.Palomar;
import kennedy.com.Pigeonper.Entidades.Usuario;
import kennedy.com.Pigeonper.LocalHost;
import kennedy.com.Pigeonper.MainAdminActivity;
import kennedy.com.Pigeonper.R;

public class AddCanastas extends Fragment {

    public Button btnAgregarCanasta,btnFechaCanasta,btnHoraCanasta;
    public String URL = "http://api.openweathermap.org/data/2.5/weather?q=" ;
    public String URL2 = "&units=metric&appid=672860fe40d0e1c4e260b563464f6159";
    public String URLFINAL;
    public View root;
    public EditText etNomCanasta,etxHoraCanasta,etxFechaCanasta; //etCiudad
    public Spinner spinner, spinnerLinea;
    public double distancia;
    //public ImageView iconoBorrar;
    public int idLoc;

    private ArrayList<Canasta> listaCanasta;
    private AdapterCanastas adapterCanastas;
    private RecyclerView recyclerViewPersonas;
    private RequestQueue rq;
    private int dia, mes, anio,hora, minutos;
    Usuario usu = new Usuario();
    Palomar palomar= new Palomar();
    ArrayList<HistorialPerformance> historialPerformances;
    ArrayList<Localidad> listaLocalidades;
    ArrayList<String> nombresEnComboBox, nombreEnSpinnerLinea;
    ArrayList<Integer> idLinea;
    Localidad loc ;
    public int idPalomar=1; /////////// MODIFICAR CUANDO SE CREEE HAGA EL ABM DE PALOMARES PARA UN COLOMBOFILO AHORA ES SOLO PALOMAR con ID 1;
    final String[] nombreDeLocSeleccionada = new String[1];
    final String[] nombredeLineaSeleccionada = new String[1];
    public int idLineaSeleccionada;
    ////////////////////// FIN INICIALIZACION

    public View onCreateView(@NonNull LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        //rq=Volley.newRequestQueue(getContext());
        root = inflater.inflate(R.layout.fragment_encanastar, container, false);
        listaCanasta = new ArrayList<>();
        historialPerformances =new ArrayList<>();
        nombresEnComboBox= new ArrayList<>();
        nombreEnSpinnerLinea = new ArrayList<>();
        listaLocalidades= new ArrayList<>();
        loc=new Localidad();
        idLinea=new ArrayList<>();

        initVistas();

        //TOMAR LOS DATOS DE LA CLASE USUARIO LLENADO EN EL LOGINACTIVITY
        MainAdminActivity maa = (MainAdminActivity)getActivity();
        usu=maa.getusu();
        //------------------------------------------

        ///////////// LLENADO DE COMBOBOX o SPINNER
        obtenerLocalidad();
        //---------------------------------------
        ///////////////// LLENADO DE SPINNER LINEA
        obtenerLinea();

        ////////////////////////////// AGREGAR CANASTA
        btnAgregarCanasta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // solicitud(); //Toma los datos del clima
                if(nombredeLineaSeleccionada[0].equals("Todas")){
                    Toast.makeText(getActivity(), "ERROR SELECCIONE SOLO UNA LINEA DE COMPETENCIA, ERROR EN: "+nombredeLineaSeleccionada[0], Toast.LENGTH_SHORT).show();
                }else {
                    agregarCanasta();
                }
                //Toast.makeText(getActivity(), nombredeLineaSeleccionada[0], Toast.LENGTH_SHORT).show();

            }
        });

////////////////////////////////////////////////////////////

        btnFechaCanasta.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {

                final Calendar c =Calendar.getInstance();
                dia=c.get(Calendar.DAY_OF_MONTH);
                mes=c.get(Calendar.MONTH);
                anio=c.get(Calendar.YEAR);

                DatePickerDialog datePickerDialog=new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        etxFechaCanasta.setText(i2 + "/" + (i1+1) + "/" + i);
                    }
                },dia,mes,anio);

                datePickerDialog.show();
            }
        });

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        btnHoraCanasta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Calendar c =Calendar.getInstance();
                hora=c.get(Calendar.HOUR_OF_DAY);
                minutos=c.get(Calendar.MINUTE);


                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {

                        if(i1>=0 && i1<=9){

                            etxHoraCanasta .setText(i+":0"+i1);

                        }else etxHoraCanasta .setText(i+":"+i1);

                    }
                },hora,minutos,false);

                timePickerDialog.show();
            }
        });
//-----------------------------------------------------------------------------------

// ---------------------------------------------------------------------------------------

        cargarLista();
        mostrarData();
        return root;
    }//////////////////////////////////////////////////////////////// FIN CREATE VIEW
    //---------------------------------------------------------------------------------------------

    private void updateSpinner() {
        ArrayAdapter<CharSequence> adaptador = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item,nombresEnComboBox);
        spinner.setAdapter(adaptador);
        //guardo el item seleccionado del combo
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                nombreDeLocSeleccionada[0] = (String) spinner.getAdapter().getItem(i);
                for (int j=0 ; j < listaLocalidades.size() ; j++){
                    if (listaLocalidades.get(j).getNombreLocalidad().equals(nombreDeLocSeleccionada[0])) {
                        idLoc = listaLocalidades.get(j).getIdLocalidad() ;
                        break;
                    }
                }
                //Toast.makeText(getActivity(), nombreDeLocSeleccionada[0], Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    //-------------------------------------------------------------------------------------------

    private void updateSpinnerLinea() {

        ArrayAdapter<CharSequence> adaptador = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item,nombreEnSpinnerLinea);
        spinnerLinea.setAdapter(adaptador);
        //guardo el item seleccionado del combo
        spinnerLinea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                nombredeLineaSeleccionada[0] = (String) spinnerLinea.getAdapter().getItem(i);

                if(nombredeLineaSeleccionada[0].equals("Todas")){

                    // Toast.makeText(getActivity(), "ERRORRRR", Toast.LENGTH_SHORT).show();
                }else {
                    idLineaSeleccionada=i;
                    //Toast.makeText(getActivity(), "Linea: "+idLinea.get(i-1), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    //-------------------------------------------------------------------------------------------
    private void obtenerLinea() {
        String URL="http://"+ LocalHost.LOCALHOST+":3333/pigeonper/consultar_linea.php";

        JsonArrayRequest jsonArrayRequest=  new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                JSONObject jo=null;
                nombreEnSpinnerLinea.clear();
                nombreEnSpinnerLinea.add("Todas");
                idLinea.clear();

                for(int i=0;i<response.length();i++){
                    try{
                        jo = response.getJSONObject(i);
                        nombreEnSpinnerLinea.add(jo.getString("nombre"));
                        idLinea.add(jo.getInt("idHabitaculo"));
                        updateSpinnerLinea();
                    }catch (Exception e){
                        Toast.makeText(getContext(), e.getMessage() ,Toast.LENGTH_SHORT).show();
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getContext(), "Error en la conexion", Toast.LENGTH_SHORT).show();
            }
        }
        );

        rq=Volley.newRequestQueue(getContext());
        rq.add(jsonArrayRequest);
    }

    //--------------------------------------------------------------------------------------------
    private void obtenerLocalidad() {

        String URL="http://"+ LocalHost.LOCALHOST+":3333/pigeonper/consultar_localidad.php";

        JsonArrayRequest jsonArrayRequest=  new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                JSONObject jo=null;
                nombresEnComboBox.clear();
                listaLocalidades.clear();

                nombresEnComboBox.add("Seleccione");
                for(int i=0;i<response.length();i++){

                    try{
                        jo = response.getJSONObject(i);

                        listaLocalidades.add(new Localidad(

                            jo.getInt("idLocalidad"),
                            jo.getString("NombreLocalidad"),
                            jo.getDouble("lat"),
                            jo.getDouble("lon")

                        ));
                        nombresEnComboBox.add(jo.getString("NombreLocalidad"));
                        updateSpinner();
                    }catch (Exception e){
                        Toast.makeText(getContext(), e.getMessage() ,Toast.LENGTH_SHORT).show();
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getContext(), "Error en la conexion", Toast.LENGTH_SHORT).show();
            }
        }
        );

        rq=Volley.newRequestQueue(getContext());
        rq.add(jsonArrayRequest);

    }

///////////////////////////////////////////////////////////////////////////////

    private void initVistas() {
        recyclerViewPersonas = root.findViewById(R.id.idRecyclerCanastas);
        btnAgregarCanasta=root.findViewById(R.id.btnAgregarCanasta);
        etNomCanasta=root.findViewById(R.id.etNomCanasta);
        spinner=root.findViewById(R.id.spinner);
        btnFechaCanasta=root.findViewById(R.id.btnFechaCanasta);
        btnHoraCanasta=root.findViewById((R.id.btnHoraCanasta));
        etxHoraCanasta=root.findViewById((R.id.edtHoraCanasta));
        etxFechaCanasta=root.findViewById((R.id.edtFechaCanasta));
        spinnerLinea=root.findViewById(R.id.spinnerLinea);
       // iconoBorrar=root.findViewById(R.id.idImgBorrarCard);
    }

    /////////////////////////////////////////////////////////////////////

    private void solicitud() {

       String CiudadTemp = loc.getNombreLocalidad();
       String nomCanasta = etNomCanasta.getText().toString();

        URLFINAL=URL+CiudadTemp+URL2;

        JsonObjectRequest jor = new JsonObjectRequest(
                Request.Method.GET,
                URLFINAL,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            JSONObject jo = response.getJSONObject("main");
                            String desc = jo.getString("temp");

                            JSONObject jo_Wind = response.getJSONObject("wind");
                            String Vel_Viento = jo_Wind.getString("speed");

                            JSONObject jo_Vel_Viento = response.getJSONObject("wind");
                            String viento = jo_Vel_Viento.getString("deg");


                           // Toast.makeText(getContext(), "Temperatura: "+desc+"°C\nDireccion del vtestiento: "+viento+"\nVelocidad del viento: "+Vel_Viento, Toast.LENGTH_SHORT).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        if(error instanceof ServerError){

                            Toast.makeText(getContext(), "SERVER ERROR (No se encuentra ciudad ingresada)", Toast.LENGTH_SHORT).show();

                        }
                        if(error instanceof NoConnectionError){

                            Toast.makeText(getContext(), "ERROR DE CONEXIÓN A OPENWEATHER", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        rq.add(jor);
    }
//////////////////////////////////////////////////////////////////////////////////

    public void agregarCanasta(){

        System.out.println("ID PALOMAR::::::  "+palomar.getIdPalomar());
        //final String ciudad=String.valueOf(loc.getIdLocalidad());
        final String nomCanasta=etNomCanasta.getText().toString();
        final String dia=etxFechaCanasta.getText().toString();
        final String hora=etxHoraCanasta.getText().toString();
        final String pal=String.valueOf(palomar.getIdPalomar());

        String URL="http://"+ LocalHost.LOCALHOST+":3333/pigeonper/insertar_canasta.php";
//?nombreCanasta="+nomCanasta+"&localidadDestino="+ciudad+"&palomar="+palomar.getIdPalomar(); //AGREGAR PARAMETRO DE DIA Y HORA PHP TAMBIEN
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("RESPUESTA:  " + response);
                        cargarLista();
                        Toast.makeText(getContext(), "CANASTA INGRESADA", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(getContext(), "ERROR EN EL INGRESO", Toast.LENGTH_SHORT).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> params = new HashMap<>();
                params.put("nombreCanasta",nomCanasta);
                params.put("localidadDestino",String.valueOf(idLoc));
                params.put("palomar",String.valueOf(idPalomar));

                params.put("dia",dia);
                params.put("hora",hora);

                params.put("linea",String.valueOf(idLineaSeleccionada));
                params.put("estado","1"); // el 1 es el estado "LISTA" en la BBDD
                return params;
            }
        };
       
        rq.add(stringRequest);

    }

    ////////////////////////////////////////////////////////////////////////////////

    public  void cargarLista(){

        String URL="http://"+ LocalHost.LOCALHOST+":3333/pigeonper/consultar_canastas.php?usuario="+usu.getIdUsu();

        JsonArrayRequest jsonArrayRequest=  new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                JSONObject jo=null;

                listaCanasta.clear(); //PROBANDOOOOO

                for(int i=0;i<response.length();i++){

                    try{

                        jo = response.getJSONObject(i);

                        if (i==0) palomar = new Palomar(jo.getInt("idPalomar"),jo.getInt("palomar_loc"),usu.getIdUsu(),jo.getString("nombre"),jo.getString("direccion"));

                        listaCanasta.add(new Canasta(

                                jo.getInt("idCanasta"),
                                jo.getInt("idLocalidad"),
                                jo.getInt("idPalomar"),
                                jo.getString("Nombre"),
                                jo.getString("dia"),
                                jo.getString("hora"),
                                jo.getInt("Habitaculo_idHabitaculo"),
                                jo.getInt("idEstado_Canasta")

                        ));

                        adapterCanastas.notifyDataSetChanged();//IMPORTANTE!!!! SIN ESTO NO ACTUALIZA EL ADAPTER EN EL MENU O EN CUALQUIER OTRA SELECCION Y NO SE VE EL RECYCLER!!!

                    }catch (Exception e){
                      Toast.makeText(getContext(), e.getMessage() ,Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                    if (listaCanasta.size()==0){
                        //palomar = new Palomar(jo.getInt("idPalomar"),jo.getInt("palomar_loc"),usu.getIdUsu(),jo.getString("nombre"),jo.getString("direccion"));
                    }else{
                        Toast.makeText(getContext(), "Error en la conexionNNNNNNNN", Toast.LENGTH_SHORT).show();
                    }
            }
        }
        );

        rq=Volley.newRequestQueue(getContext());
        rq.add(jsonArrayRequest);

    }

    //////////////////////////////////////////////////////////////////////////////////

    public void mostrarData(){

        recyclerViewPersonas.setLayoutManager(new LinearLayoutManager(getContext()));
        adapterCanastas = new AdapterCanastas(getContext(), listaCanasta);

        //EVENTO AL HACER CLICK EN EL CARD
        adapterCanastas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //recorrer lista de localidades en busca de la seleccion
                double lat1, lat2 = 0, lon1, lon2 = 0;
                //System.out.println("El id de la localidad es: "+listaCanasta.get(recyclerViewPersonas.getChildAdapterPosition(view)).getIdLocalidad());
                for(int i=0 ; i<listaLocalidades.size(); i++){
                    if(listaCanasta.get(recyclerViewPersonas.getChildAdapterPosition(view)).getIdLocalidad() ==listaLocalidades.get(i).getIdLocalidad()){
                        lat2=listaLocalidades.get(i).getLat();
                        lon2=listaLocalidades.get(i).getLon();
                        loc=listaLocalidades.get(i); // guardo todos los datos de la localidad en loc
                    }
                }

                lat1 = -34.69045539694708; //FIJO LA rIOJA 2128
                lon1 = -58.40965694530948;//FIJO LA// rIOJA 2128

                //la lat2 y long2 depende la localidad elegida en el combobox

                distancia=distance(lat1,lat2,lon1,lon2);
                //-----------------------------------

                Intent intent= new Intent(getContext(), AddPalomasACanasta.class);
                intent.putExtra("palomar",palomar);
                intent.putExtra("canasta",listaCanasta.get(recyclerViewPersonas.getChildAdapterPosition(view)));
                //Toast.makeText(getActivity(), "la distancia es: "+distancia, Toast.LENGTH_SHORT).show();
                intent.putExtra("distancia",distancia);
                intent.putExtra("listaLinea",nombreEnSpinnerLinea);
                intent.putExtra("listaLoc",listaLocalidades);


                startActivity(intent);
            }
        });

        recyclerViewPersonas.setAdapter(adapterCanastas);

    }

    //////////////////////////////////////////////////////////////////////////////////

    public static double distance(double lat1, double lat2, double lon1, double lon2) {
        final int R = 6371; // Radio terrestre
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c; // Convertir unidades a metros   ( double distance = R * c * 1000 )
        distance = Math.pow(distance, 2);
        return Math.sqrt(distance);
    }

    ////////////////////////////////////////////////////////////////////////////////////////
}


