package utfpr.edu.br.pedroozanatta.appdiarioviagens;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class ListaActivity extends AppCompatActivity {

    private ListView listViewViagens;
    private List<Viagem> listaViagens;
    private ViagemAdapter viagemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);

        setTitle(getString(R.string.lista_de_viagens));

        listViewViagens = findViewById(R.id.listViewViagens);

        listViewViagens.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent,
                                    View view,
                                    int position,
                                    long id) {
                Viagem viagem = (Viagem) listViewViagens.getItemAtPosition(position);

                Toast.makeText(getApplicationContext(),
                               getString(R.string.viagem_realizada_noa)+ viagem.getPais(),
                                Toast.LENGTH_LONG).show();
            }
        });

        popularListaViagens();
    }

    public void popularListaViagens(){

        listaViagens = new ArrayList<>();
        viagemAdapter = new ViagemAdapter(listaViagens, this);
        listViewViagens.setAdapter(viagemAdapter);
    }

    public void abrirSobre(View view){
        Intent intentAbertura = new Intent(this, SobreActivity.class);

        startActivity(intentAbertura);
    }

    ActivityResultLauncher<Intent> laucherNovaViagem = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if(result.getResultCode() == ListaActivity.RESULT_OK){

                Intent intent = result.getData();

                Bundle bundle = intent.getExtras();

                if(bundle != null){

                    String pais = bundle.getString(ViagemActivity.KEY_PAIS);
                    String local = bundle.getString(ViagemActivity.KEY_LOCAL);
                    String data = bundle.getString(ViagemActivity.KEY_DATA);
                    boolean capital = bundle.getBoolean(ViagemActivity.KEY_CAPITAL);
                    String tipo = bundle.getString(ViagemActivity.KEY_TIPO);
                    int continente = bundle.getInt(ViagemActivity.KEY_CONTINENTE);

                    Viagem viagem = new Viagem(pais, local, data, capital, TipoViagem.valueOf(tipo), continente);
                    listaViagens.add(viagem);
                    viagemAdapter.notifyDataSetChanged();
                }
            }
        }
    });

    public void abrirCadastroViagem(View view){
        Intent intentAbertura = new Intent(this, ViagemActivity.class);
        laucherNovaViagem.launch(intentAbertura);
    }
}