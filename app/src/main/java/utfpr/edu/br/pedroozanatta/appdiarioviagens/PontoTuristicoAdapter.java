package utfpr.edu.br.pedroozanatta.appdiarioviagens;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import utfpr.edu.br.pedroozanatta.appdiarioviagens.models.PontoTuristico;

public class PontoTuristicoAdapter extends BaseAdapter {

    private Context context;
    private List<PontoTuristico> listaPontoTuristico;
    public String[] categoria;

    private static class PontoTuristicoHolder{
        public TextView textViewValorNomePonto;
        public TextView textViewValorEndereco;
        public TextView textViewValorCategoria;
    }

    public PontoTuristicoAdapter(List<PontoTuristico> listaPontoTuristico, Context context) {
        this.listaPontoTuristico = listaPontoTuristico;
        this.context = context;
        categoria = context.getResources().getStringArray(R.array.categorias);
    }

    @Override
    public int getCount() {
        return listaPontoTuristico.size();
    }

    @Override
    public Object getItem(int position) {
        return listaPontoTuristico.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        PontoTuristicoHolder holder;

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.lista_pontos_turisticos, parent, false);

            holder = new PontoTuristicoHolder();

            holder.textViewValorNomePonto = convertView.findViewById(R.id.textViewRotuloNomePonto);
            holder.textViewValorEndereco = convertView.findViewById(R.id.textViewRotuloEndereco);
            holder.textViewValorCategoria = convertView.findViewById(R.id.textViewRotuloCategoria);

            convertView.setTag(holder);

        } else{
            holder = (PontoTuristicoHolder) convertView.getTag();
        }

        PontoTuristico pontoTuristico = listaPontoTuristico.get(position);

        holder.textViewValorNomePonto.setText(pontoTuristico.getNomePontoTuristico());
        holder.textViewValorEndereco.setText(pontoTuristico.getEndereco());

        holder.textViewValorCategoria.setText(categoria[pontoTuristico.getCategoria()]);

        return convertView;
    }
}
