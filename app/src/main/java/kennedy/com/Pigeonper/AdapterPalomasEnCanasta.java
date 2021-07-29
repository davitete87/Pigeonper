package kennedy.com.Pigeonper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import kennedy.com.Pigeonper.Entidades.Palomas;



public class AdapterPalomasEnCanasta  extends RecyclerView.Adapter<AdapterPalomasEnCanasta.ViewHolder> implements  View.OnClickListener{

    static public ArrayList<Palomas> model;
    LayoutInflater inflater;

    //listener
    private View.OnClickListener listener;

    public AdapterPalomasEnCanasta(Context context, ArrayList<Palomas> model) {
        this.inflater=LayoutInflater.from(context);
        this.model = model;
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener=listener;
    }

    @Override
    public void onClick(View view) {
        if(listener!=null){
            listener.onClick(view);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.palomas_en_canasta_list,parent,false);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterPalomasEnCanasta.ViewHolder holder, int position) {
        String numero=String.valueOf(model.get(position).getNumero());
        String color=String.valueOf(model.get(position).getRaza());
        String habitaculo= String.valueOf(model.get(position).getIdHabitaculo());

        holder.numero.setText(numero);
        holder.color.setText(color);
        holder.habitaculo.setText(habitaculo);
    }

    @Override
    public int getItemCount() {
        return model.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView numero, color, habitaculo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            numero=itemView.findViewById(R.id.txtNumero);
            color=itemView.findViewById(R.id.txtColor);
            habitaculo=itemView.findViewById(R.id.txtidHabitaculo);

        }
    }
}
