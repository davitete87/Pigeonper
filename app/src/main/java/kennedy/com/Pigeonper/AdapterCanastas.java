package kennedy.com.Pigeonper;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import kennedy.com.Pigeonper.Entidades.Canasta;


public class AdapterCanastas extends RecyclerView.Adapter<AdapterCanastas.ViewHolder> implements  View.OnClickListener{

    RequestQueue rq;
    ArrayList<Canasta> model;
    LayoutInflater inflater;
    Context cont;

    //listener
    private View.OnClickListener listener;

    public AdapterCanastas(Context context, ArrayList<Canasta> model) {
        this.inflater=LayoutInflater.from(context);
        this.model = model;
        rq=Volley.newRequestQueue(context);
        cont=context;
        
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener=listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.canastas_list,parent,false);

        view.setOnClickListener(this);

        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        String nombre=model.get(position).getNombre();
        String idLocalidad=String.valueOf(model.get(position).getIdLocalidad());
        String idCanasta= String.valueOf(model.get(position).getIdCanasta());

        holder.nombre.setText(nombre);
        holder.canasta.setText(idCanasta);
        holder.localidad.setText(idLocalidad);

        holder.icBorrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//-----------------------------------------------------------------------
                AlertDialog.Builder alerta = new AlertDialog.Builder(cont);
                alerta.setMessage("Esta seguro de eliminar la canasta: "+model.get(position).getNombre()+" ?")
                        .setCancelable(false)
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                eliminarCanasta( model.get(position).getIdCanasta());
                                model.remove(position);
                                notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });

                AlertDialog titulo = alerta.create();
                titulo.setTitle("Eliminar canasta");
                titulo.show();

//-----------------------------------------------------------------------

            }
        });
    }
///////////////////////////////

    public void eliminarCanasta(final int idCanasta) {

        String URL="http://"+ LocalHost.LOCALHOST+":3333/pigeonper/eliminar_Canasta.php?idCanasta="+idCanasta;
        StringRequest sr = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //System.out.println("RESPONDIO!!!!!!!!!!!" + response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("EERROR LOCURA!!!!!!!!!!!  ----  ");
            }
        }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("idCanasta",String.valueOf(idCanasta));
                return param;
            }
        };
        notifyDataSetChanged();
        rq.add(sr);
    }
////////////////////////////////////////////////////////////////////////

    @Override
    public int getItemCount() {
        return model.size();
    }

    @Override
    public void onClick(View view) {

        if(listener!=null){
            listener.onClick(view);
        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView nombre, localidad, canasta;
        ImageView icBorrar;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);

            nombre=itemView.findViewById(R.id.txtNombre);
            localidad=itemView.findViewById(R.id.txtLocalidad);
            canasta=itemView.findViewById(R.id.txtidCanasta);
            icBorrar=itemView.findViewById(R.id.idImgBorrarCard);
            //cantPalomas=itemView.findViewById(R.id.txtCantPalomas);

        }

    }

}
