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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import utfpr.edu.br.pedroozanatta.appdiarioviagens.DAO.ViagemDAO;
import utfpr.edu.br.pedroozanatta.appdiarioviagens.DAO.ViagemDatabase;
import utfpr.edu.br.pedroozanatta.appdiarioviagens.models.TipoViagem;
import utfpr.edu.br.pedroozanatta.appdiarioviagens.models.Viagem;
import utfpr.edu.br.pedroozanatta.appdiarioviagens.utillities.Alert;

public class ListaActivity extends AppCompatActivity {

    private ListView listViewViagens;
    private List<Viagem> listaViagens;
    private ViagemAdapter viagemAdapter;

    private int posicaoSelecionada = -1;
    private ActionMode actionMode;
    private View viewSelecionada;
    private Drawable backgroundDrawable;
    public static final String ARQUIVO_PREFERENCIAS = "utfpr.edu.br.pedroozanatta.appdiarioviagens.PREFERENCIAS";
    public static final String KEY_ORDENACAO_ASCENDENTE = "ORDENACAO_ASCENDENTE";

    public static final boolean PADRAO_INICIAL_ORDENACAO_ASCENDENTE = true;
    private boolean ordenacaoAscendente = PADRAO_INICIAL_ORDENACAO_ASCENDENTE;
    private MenuItem menuItemOrdenacao;
    private ActionMode.Callback actionCallback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater inflate = mode.getMenuInflater();
            inflate.inflate(R.menu.viagens_item_selecionado, menu);
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
                editarViagem();
                return true;
            } else {
                if (idMenuItem == R.id.menuItemExcluir) {
                    excluirViagem();
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

            listViewViagens.setEnabled(true);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);

        setTitle(getString(R.string.lista_de_viagens));

        listViewViagens = findViewById(R.id.listViewViagens);

        lerPreferencias();

        popularListaViagens();
    }

    public void popularListaViagens() {

        ViagemDatabase database = ViagemDatabase.getInstance(this);

        if(ordenacaoAscendente){
            listaViagens = database.getViagemDAO().queryAllAscending();
        } else{
            listaViagens = database.getViagemDAO().queryAllDownward();
        }

        viagemAdapter = new ViagemAdapter(listaViagens, this);

        listViewViagens.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                posicaoSelecionada = position;
                editarViagem();
            }
        });

        listViewViagens.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                if (actionMode != null) {
                    return false;
                }

                posicaoSelecionada = position;

                viewSelecionada = view;
                backgroundDrawable = view.getBackground();
                view.setBackgroundColor(getColor(R.color.corSelecionada));

                listViewViagens.setEnabled(false);

                actionMode = startSupportActionMode(actionCallback);

                return true;
            }
        });

        listViewViagens.setAdapter(viagemAdapter);
    }

    public void abrirSobre() {
        Intent intentAbertura = new Intent(this, SobreActivity.class);
        startActivity(intentAbertura);
    }

    ActivityResultLauncher<Intent> laucherNovaViagem = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == ListaActivity.RESULT_OK) {

                Intent intent = result.getData();

                Bundle bundle = intent.getExtras();

                if (bundle != null) {

                    long id = bundle.getLong(ViagemActivity.KEY_ID);
                    ViagemDatabase database = ViagemDatabase.getInstance(ListaActivity.this);
                    Viagem viagem = database.getViagemDAO().queryForId(id);
                    listaViagens.add(viagem);

                    ordenarLista();
                }
            }
        }
    });

    public void abrirCadastroViagem() {
        Intent intentAbertura = new Intent(this, ViagemActivity.class);
        intentAbertura.putExtra(ViagemActivity.KEY_MODO, ViagemActivity.MODO_NOVO);
        laucherNovaViagem.launch(intentAbertura);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.viagens_opcoes, menu);

        menuItemOrdenacao = menu.findItem(R.id.menuItemOrdenacao);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int idMenuItem = item.getItemId();

        if (idMenuItem == R.id.menuItemAdicionar) {
            abrirCadastroViagem();
            return true;
        } else if (idMenuItem == R.id.menuItemSobre) {
            abrirSobre();
            return true;
        } else if (idMenuItem == R.id.menuItemOrdenacao) {
            salvarPreferenciaOrdenacao(!ordenacaoAscendente);
            atualizarIconeOrdenacao();
            ordenarLista();
            return true;
        } else if(idMenuItem == R.id.menuItemRestaurar){
            confirmarRestaurarPadroes();
            return true;

        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        atualizarIconeOrdenacao();
        return true;
    }

    private void excluirViagem() {

        final Viagem viagem = listaViagens.get(posicaoSelecionada);
        String mensagem = getString(R.string.deletar_confirmacao, viagem.getPais());

        DialogInterface.OnClickListener listenerSim = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                ViagemDatabase database = ViagemDatabase.getInstance(ListaActivity.this);

                int qntAlterada = database.getViagemDAO().delete(viagem);

                if(qntAlterada != 1){
                    Alert.mostrarAviso(ListaActivity.this, R.string.erro_exclusao);
                    return;
                }

                listaViagens.remove(posicaoSelecionada);
                viagemAdapter.notifyDataSetChanged();
                actionMode.finish();
            }
        };
        Alert.confirmarAcao(this, mensagem, listenerSim, null);
    }

    ActivityResultLauncher<Intent> laucherEditarViagem = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == ListaActivity.RESULT_OK) {

                Intent intent = result.getData();

                Bundle bundle = intent.getExtras();

                if (bundle != null) {

                    final Viagem viagemOriginal = listaViagens.get(posicaoSelecionada);

                    long id = bundle.getLong(ViagemActivity.KEY_ID);
                    final ViagemDatabase database = ViagemDatabase.getInstance(ListaActivity.this);
                    Viagem viagem = database.getViagemDAO().queryForId(id);

                    final Viagem viagemEditada = database.getViagemDAO().queryForId(id);
                    listaViagens.set(posicaoSelecionada, viagemEditada);
                    ordenarLista();

                    final ConstraintLayout constraintLayout = findViewById(R.id.main);
                    Snackbar snackbar = Snackbar.make(constraintLayout, R.string.alteracao_realizada, Snackbar.LENGTH_LONG);

                    snackbar.setAction(R.string.desfazer, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            int qntAlterada = database.getViagemDAO().update(viagemOriginal);

                            if(qntAlterada != 1){
                                Alert.mostrarAviso(ListaActivity.this, R.string.erro_alteracao);
                                return;
                            }

                            listaViagens.remove(viagemEditada);
                            listaViagens.add(viagemOriginal);
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

    private void editarViagem() {
        Viagem viagem = listaViagens.get(posicaoSelecionada);

        Intent intentAbertura = new Intent(this, ViagemActivity.class);
        intentAbertura.putExtra(ViagemActivity.KEY_MODO, ViagemActivity.MODO_EDITAR);

        intentAbertura.putExtra(ViagemActivity.KEY_ID, viagem.getId());

        laucherEditarViagem.launch(intentAbertura);
    }

    private void ordenarLista() {
        if(ordenacaoAscendente){
            Collections.sort(listaViagens, Viagem.ordenacao);
        } else {
            Collections.sort(listaViagens, Viagem.ordenacaoDecrescente);
        }
        viagemAdapter.notifyDataSetChanged();
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

    private void salvarPreferenciaOrdenacao(boolean novoValor){
        SharedPreferences shared = getSharedPreferences(ARQUIVO_PREFERENCIAS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shared.edit();

        editor.putBoolean(KEY_ORDENACAO_ASCENDENTE, novoValor);
        editor.commit();
        ordenacaoAscendente = novoValor;
    }

    private void confirmarRestaurarPadroes(){
        DialogInterface.OnClickListener listenerSim = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                restaurarPadroes();
                atualizarIconeOrdenacao();
                ordenarLista();

                Toast.makeText(ListaActivity.this,
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