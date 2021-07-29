package kennedy.com.Pigeonper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import kennedy.com.Pigeonper.Entidades.Usuario;
import kennedy.com.Pigeonper.R;

public class LoginActivity extends AppCompatActivity {

    EditText Email,Pass;
    Button Logear;
    Usuario usuario;
    ToggleButton mt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        usuario = new Usuario();

        Email=(EditText)findViewById(R.id.edtUsuario);
        Pass = (EditText)findViewById(R.id.edtPwd);
        Logear=(Button)findViewById(R.id.btnIngresar);
        mt = (ToggleButton) findViewById(R.id.toggleRecordarSesion);


        Logear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                usuario.setPsw(Pass.getText().toString());
                usuario.setEmail(Email.getText().toString());

                //if(!usuario.getEmail().isEmpty() && !usuario.getPsw().isEmpty()) ValidarEmail("http://192.168.1.103:3333/pigeonper/validar_usuario.php?usuario="+usuario.getEmail()+"&password="+usuario.getPsw());

                if(!usuario.getEmail().isEmpty() && !usuario.getPsw().isEmpty()) ValidarEmail("http://"+LocalHost.LOCALHOST+":3333/pigeonper/validar_usuario.php?usuario="+usuario.getEmail()+"&password="+usuario.getPsw());

                else Toast.makeText(LoginActivity.this, "Correo o passwod vacios", Toast.LENGTH_SHORT).show();
            }
        });

        mt.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                    Toast.makeText(getApplicationContext(), "Toggle sin funcionalidad", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void ValidarEmail(String URL) {

        JsonObjectRequest jsonObjectRequest= new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    usuario.setIdUsu(Integer.parseInt(response.getString("idUsuario")));
                    usuario.setApe(response.getString("Apellido"));
                    usuario.setDireccion(response.getString("direccion"));
                    usuario.setTelefono(response.getString("Telefono"));
                    usuario.setDni(response.getString("DNI"));

                    Intent intent=new Intent(getApplicationContext(),MainAdminActivity.class);
                    intent.putExtra("usuario",usuario);

                    startActivity(intent);
                    finish();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoginActivity.this, "NO SE ENCUENTRA EL USUARIO", Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }


    /////////////////////////////////////////
}