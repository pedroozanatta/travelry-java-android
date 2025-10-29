package utfpr.edu.br.pedroozanatta.appdiarioviagens;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import utfpr.edu.br.pedroozanatta.appdiarioviagens.models.TipoViagem;
import utfpr.edu.br.pedroozanatta.appdiarioviagens.models.Viagem;
import utfpr.edu.br.pedroozanatta.appdiarioviagens.utillities.DateUtils;

public class ViagemAdapter extends BaseAdapter {

    private Context context;
    private List<Viagem> listaViagens;
    private String[] continente;

    private static class ViagemHolder{
        public TextView textViewValorPais;
        public TextView textViewValorLocal;
        public TextView textViewValorData;
        public TextView textViewValorCapital;
        public TextView textViewValorTipo;
        public TextView textViewValorContinente;
    }

    public ViagemAdapter(List<Viagem> listaViagens, Context context) {
        this.listaViagens = listaViagens;
        this.context = context;
        continente = context.getResources().getStringArray(R.array.continentes);
    }

    @Override
    public int getCount() {
        return listaViagens.size();
    }

    @Override
    public Object getItem(int position) {
        return listaViagens.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViagemHolder holder;

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.lista_viagens, parent, false);

            holder = new ViagemHolder();

            holder.textViewValorPais = convertView.findViewById(R.id.textViewValorPais);
            holder.textViewValorLocal = convertView.findViewById(R.id.textViewValorLocal);
            holder.textViewValorData = convertView.findViewById(R.id.textViewValorData);
            holder.textViewValorCapital = convertView.findViewById(R.id.textViewValorCapital);
            holder.textViewValorTipo = convertView.findViewById(R.id.textViewValorTipo);
            holder.textViewValorContinente = convertView.findViewById(R.id.textViewValorContinente);

            convertView.setTag(holder);

        } else{
            holder = (ViagemHolder) convertView.getTag();
        }

        Viagem viagem = listaViagens.get(position);

        holder.textViewValorPais.setText(viagem.getPais());
        holder.textViewValorLocal.setText(viagem.getLocal());
        holder.textViewValorData.setText(DateUtils.formatLocalDate(viagem.getData()));

        if (viagem.isCapital())
            holder.textViewValorCapital.setText(R.string.checkbox_true);
        else
            holder.textViewValorCapital.setText(R.string.checkbox_false);


        if (viagem.getTipoViagem() == TipoViagem.Nacional)
            holder.textViewValorTipo.setText(R.string.radiobutton_nacional);
        else
            holder.textViewValorTipo.setText(R.string.radiobutton_internacional);

        holder.textViewValorContinente.setText(continente[viagem.getContinente()]);

        return convertView;
    }
}
