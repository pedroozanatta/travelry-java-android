package utfpr.edu.br.pedroozanatta.appdiarioviagens;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.snackbar.Snackbar;

import java.util.Collections;
import java.util.List;

import utfpr.edu.br.pedroozanatta.appdiarioviagens.DAO.ViagemDatabase;
import utfpr.edu.br.pedroozanatta.appdiarioviagens.models.PontoTuristico;
import utfpr.edu.br.pedroozanatta.appdiarioviagens.models.Viagem;
import utfpr.edu.br.pedroozanatta.appdiarioviagens.utillities.Alert;

public class ListaPontosTuristicosActivity extends AppCompatActivity {

    public static final String KEY_ID_VIAGEM = "KEY_ID_VIAGEM";
    public static final String KEY_ID_PONTO = "KEY_ID_PONTO";
    private ListView listViewPontosTuristicos;
    private List<PontoTuristico> listaPontosTuristicos;
    private PontoTuristicoAdapter pontoTuristicoAdapter;
    private int posicaoSelecionada = -1;
    private ActionMode actionMode;
    private View viewSelecionada;
    private Drawable backgroundDrawable;
    private Viagem viagem;
    public static final String ARQUIVO_PREFERENCIAS = "utfpr.edu.br.pedroozanatta.appdiarioviagens.PREFERENCIAS";
    public static final String KEY_ORDENACAO_ASCENDENTE = "ORDENACAO_ASCENDENTE";
    public static final boolean PADRAO_INICIAL_ORDENACAO_ASCENDENTE = true;
    private boolean ordenacaoAscendente = PADRAO_INICIAL_ORDENACAO_ASCENDENTE;
    private MenuItem menuItemOrdenacao;
    private ActionMode.Callback actionCallback = new ActionMode.Callback() {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater inflate = mode.getMenuInflater();
            inflate.inflate(R.menu.item_selecionado, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            int idMenuItem = item.getItemId();

            if (idMenuItem == R.id.menuItemEditar) {
                editarPontoTuristico();
                mode.finish();
                return true;
            } else {
                if (idMenuItem == R.id.menuItemExcluir) {
                    excluirPontoTuristico();
                    return true;
                } else {
                    return false;
                }
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {

            if (viewSelecionada != null) {
                viewSelecionada.setBackground(backgroundDrawable);
            }

            actionMode = null;
            viewSelecionada = null;
            backgroundDrawable = null;

            listViewPontosTuristicos.setEnabled(true);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pontos_turisticos);

        Intent intent = getIntent();
        long idViagem = intent.getLongExtra(KEY_ID_VIAGEM, -1);

        lerPreferencias();

        ViagemDatabase database = ViagemDatabase.getInstance(this);
        viagem = database.getViagemDAO().queryForId(idViagem);

        if (viagem != null) {
            setTitle(viagem.getLocal());
            popularListaPontosTuristicos();
        }
    }

    public void popularListaPontosTuristicos() {

        ViagemDatabase database = ViagemDatabase.getInstance(this);
        listaPontosTuristicos = database.getPontoTuristicoDAO().queryForIdViagem(viagem.getId());

        pontoTuristicoAdapter = new PontoTuristicoAdapter(listaPontosTuristicos, this);
        listViewPontosTuristicos = findViewById(R.id.listViewPontosTuristicos);


        listViewPontosTuristicos.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                posicaoSelecionada = position;
                editarPontoTuristico();
            }
        });

        listViewPontosTuristicos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                if (actionMode != null) {
                    return false;
                }

                posicaoSelecionada = position;
                viewSelecionada = view;
                backgroundDrawable = view.getBackground();
                view.setBackgroundColor(getColor(R.color.corSelecionada));

                listViewPontosTuristicos.setEnabled(false);
                actionMode = startSupportActionMode(actionCallback);

                return true;
            }
        });

        listViewPontosTuristicos.setAdapter(pontoTuristicoAdapter);
    }


    ActivityResultLauncher<Intent> laucherNovoPontoTuristico = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == ListaPontosTuristicosActivity.RESULT_OK) {

                Intent intent = result.getData();
                Bundle bundle = intent.getExtras();
                if (bundle != null) {
                    long id = intent.getExtras().getLong(KEY_ID_PONTO);
                    ViagemDatabase database = ViagemDatabase.getInstance(ListaPontosTuristicosActivity.this);
                    PontoTuristico pontoTuristico = database.getPontoTuristicoDAO().queryForId(id);
                    listaPontosTuristicos.add(pontoTuristico);

                    ordenarLista();
                }
            }
        }
    });

    ActivityResultLauncher<Intent> laucherEditarPontoTuristico = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == ListaPontosTuristicosActivity.RESULT_OK) {

                Intent intent = result.getData();
                Bundle bundle = intent.getExtras();

                if (bundle != null) {

                    final PontoTuristico pontoTuristicoOriginal = listaPontosTuristicos.get(posicaoSelecionada);
                    long id = intent.getExtras().getLong(KEY_ID_PONTO);
                    final ViagemDatabase database = ViagemDatabase.getInstance(ListaPontosTuristicosActivity.this);
                    final PontoTuristico pontoTuristicoEditado = database.getPontoTuristicoDAO().queryForId(id);
                    listaPontosTuristicos.set(posicaoSelecionada, pontoTuristicoEditado);
                    ordenarLista();

                    final ConstraintLayout constraintLayout = findViewById(R.id.main);
                    Snackbar snackbar = Snackbar.make(constraintLayout, R.string.alteracao_realizada, Snackbar.LENGTH_LONG);

                    snackbar.setAction(R.string.desfazer, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            int qntAlterada = database.getPontoTuristicoDAO().update(pontoTuristicoOriginal);

                            if(qntAlterada != 1){
                                Alert.mostrarAviso(ListaPontosTuristicosActivity.this, R.string.erro_alteracao);
                                return;
                            }
                            listaPontosTuristicos.remove(pontoTuristicoEditado);
                            listaPontosTuristicos.add(pontoTuristicoOriginal);
                            ordenarLista();
                        }
                    });
                    snackbar.show();
                }
            }
            posicaoSelecionada = -1;

            if (actionMode != null) {
                actionMode.finish();
            }
        }
    });

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.pontos_turisticos_opcoes, menu);
        menuItemOrdenacao = menu.findItem(R.id.menuItemOrdenacao);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int idMenuItem = item.getItemId();

        if (idMenuItem == R.id.menuItemAdicionar) {
            novoPontoTuristico();
            return true;
        } else if (idMenuItem == R.id.menuItemOrdenacao) {
            salvarPreferenciaOrdenacao(!ordenacaoAscendente);
            atualizarIconeOrdenacao();
            ordenarLista();
            return true;
        }else if(idMenuItem == R.id.menuItemRestaurar){
            confirmarRestaurarPadroes();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void excluirPontoTuristico() {

        final PontoTuristico pontoTuristico = listaPontosTuristicos.get(posicaoSelecionada);
        String mensagem = getString(R.string.deletar_confirmacao, pontoTuristico.getNomePontoTuristico());

        DialogInterface.OnClickListener listenerSim = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ViagemDatabase database = ViagemDatabase.getInstance(ListaPontosTuristicosActivity.this);
                int qntAlterada = database.getPontoTuristicoDAO().delete(pontoTuristico);

                if(qntAlterada != 1){
                    Alert.mostrarAviso(ListaPontosTuristicosActivity.this, R.string.erro_exclusao);
                    return;
                }

                listaPontosTuristicos.remove(posicaoSelecionada);
                pontoTuristicoAdapter.notifyDataSetChanged();
                actionMode.finish();
            }
        };
        Alert.confirmarAcao(this, mensagem, listenerSim, null);
    }

    private void editarPontoTuristico() {
        PontoTuristico pontoTuristico = listaPontosTuristicos.get(posicaoSelecionada);
        Intent intentAbertura = new Intent(this, PontoTuristicoActivity.class);
        intentAbertura.putExtra(PontoTuristicoActivity.KEY_MODO, PontoTuristicoActivity.MODO_EDITAR);
        intentAbertura.putExtra(PontoTuristicoActivity.KEY_ID_PONTO, pontoTuristico.getId());
        laucherEditarPontoTuristico.launch(intentAbertura);
    }

    public void novoPontoTuristico() {
        Intent intentAbertura = new Intent(this, PontoTuristicoActivity.class);
        intentAbertura.putExtra(PontoTuristicoActivity.KEY_MODO, PontoTuristicoActivity.MODO_NOVO);
        intentAbertura.putExtra(KEY_ID_VIAGEM, viagem.getId());
        laucherNovoPontoTuristico.launch(intentAbertura);
    }

    private void ordenarLista() {
        if(ordenacaoAscendente){
            Collections.sort(listaPontosTuristicos, PontoTuristico.ordenacao);
        } else {
            Collections.sort(listaPontosTuristicos, PontoTuristico.ordenacaoDecrescente);
        }
        pontoTuristicoAdapter.notifyDataSetChanged();
    }

    private void salvarPreferenciaOrdenacao(boolean novoValor){
        SharedPreferences shared = getSharedPreferences(ARQUIVO_PREFERENCIAS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shared.edit();

        editor.putBoolean(KEY_ORDENACAO_ASCENDENTE, novoValor);
        editor.commit();
        ordenacaoAscendente = novoValor;
    }

    private void atualizarIconeOrdenacao(){
        if(ordenacaoAscendente){
            menuItemOrdenacao.setIcon(R.drawable.ic_action_ascending_order);
        } else{
            menuItemOrdenacao.setIcon(R.drawable.ic_action_descending_order);
        }
    }

    private void lerPreferencias(){
        SharedPreferences shared = getSharedPreferences(ARQUIVO_PREFERENCIAS, Context.MODE_PRIVATE);
        ordenacaoAscendente = shared.getBoolean(KEY_ORDENACAO_ASCENDENTE, ordenacaoAscendente);
    }

    private void confirmarRestaurarPadroes(){
        DialogInterface.OnClickListener listenerSim = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                restaurarPadroes();
                atualizarIconeOrdenacao();
                ordenarLista();

                Toast.makeText(ListaPontosTuristicosActivity.this,
                        getString(R.string.as_configuracoes_voltaram_para_o_padrao_de_instalacao),
                        Toast.LENGTH_LONG).show();
            }
        };

        Alert.confirmarAcao(this, R.string.deseja_voltar_padroes, listenerSim, null);
    }

    private void restaurarPadroes(){
        SharedPreferences shared = getSharedPreferences(ARQUIVO_PREFERENCIAS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shared.edit();

        editor.clear();
        editor.commit();

        ordenacaoAscendente = PADRAO_INICIAL_ORDENACAO_ASCENDENTE;
    }
}
