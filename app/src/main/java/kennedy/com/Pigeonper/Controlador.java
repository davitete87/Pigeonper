package kennedy.com.Pigeonper;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

public class Controlador {

    public static RequestQueue rq;

    static public void  eliminar_canasta(int idCanasta){
        String URL="http://"+ LocalHost.LOCALHOST+":3333/pigeonper/eliminar_Canasta.php?idCanasta="+idCanasta;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("RESPONDIO!!!!!!!!!!!" + response);
                //notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("EERROR LOCURA!!!!!!!!!!!  ----  ");
            }
        }
        );
        rq.add(stringRequest);
    }

}
