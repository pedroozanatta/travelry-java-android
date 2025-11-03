package utfpr.edu.br.pedroozanatta.appdiarioviagens;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import java.time.LocalDate;

import utfpr.edu.br.pedroozanatta.appdiarioviagens.DAO.ViagemDatabase;
import utfpr.edu.br.pedroozanatta.appdiarioviagens.models.PontoTuristico;
import utfpr.edu.br.pedroozanatta.appdiarioviagens.models.TipoViagem;
import utfpr.edu.br.pedroozanatta.appdiarioviagens.models.Viagem;
import utfpr.edu.br.pedroozanatta.appdiarioviagens.utillities.Alert;
import utfpr.edu.br.pedroozanatta.appdiarioviagens.utillities.DateUtils;

public class PontoTuristicoActivity extends AppCompatActivity {

    public static final String KEY_ID_VIAGEM = "KEY_ID_VIAGEM";
    public static final String KEY_ID_PONTO = "KEY_ID_PONTO";
    public static final String KEY_MODO = "MODO";
    public static final int MODO_NOVO = 0;
    public static final int MODO_EDITAR = 1;
    public static  final String KEY_SUGERIR_TIPO = "SUGERIR_TIPO";
    public static  final String KEY_ULTIMO_TIPO = "ULTIMO_TIPO";
    private boolean sugerirTipo = false;
    private int ultimoTipo = 0;
    private EditText editTextNomePonto, editTextEndereco;
    private Spinner spinnerCategoria;
    private int modo;
    private PontoTuristico pontoTuristicoOriginal;
    private long viagemId;

    private Viagem viagem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_pontos_turisticos);

        editTextNomePonto = findViewById(R.id.editTextNomePonto);
        editTextEndereco = findViewById(R.id.editTextEndereco);
        spinnerCategoria = findViewById(R.id.spinnerCategoria);

        lerPreferencias();

        Intent intentAbretura = getIntent();

        Bundle bundle  = intentAbretura.getExtras();

        if (bundle != null) {
            modo = bundle.getInt(KEY_MODO);

            ViagemDatabase database = ViagemDatabase.getInstance(this);

            if (modo == MODO_NOVO) {
                viagemId = bundle.getLong(KEY_ID_VIAGEM);
                viagem = database.getViagemDAO().queryForId(viagemId);
                setTitle(getString(R.string.cadastro_dos_pontos_visitados_na_viagem, viagem.getLocal()));
            } else {
                long pontoId = bundle.getLong(KEY_ID_PONTO);
                pontoTuristicoOriginal = database.getPontoTuristicoDAO().queryForId(pontoId);

                if (pontoTuristicoOriginal == null) {
                    Alert.mostrarAviso(this, R.string.erro_insercao);
                    finish();
                    return;
                }

                viagemId = pontoTuristicoOriginal.getIdViagem();
                viagem = database.getViagemDAO().queryForId(viagemId);
                setTitle(getString(R.string.edicao_pontos_turisticos, viagem.getLocal()));

                editTextNomePonto.setText(pontoTuristicoOriginal.getNomePontoTuristico());
                editTextEndereco.setText(pontoTuristicoOriginal.getEndereco());
                spinnerCategoria.setSelection(pontoTuristicoOriginal.getCategoria());

                editTextNomePonto.requestFocus();
                editTextNomePonto.setSelection(editTextNomePonto.getText().length());
            }
        }
    }

    public void limparInputs() {

        final String nomePontoTuristico = editTextNomePonto.getText().toString();
        final String enderecoPontoTuristico = editTextEndereco.getText().toString();
        final int categoria = spinnerCategoria.getSelectedItemPosition();

        final ScrollView scrollView = findViewById(R.id.main);
        final View viewFocus = scrollView.findFocus();

        editTextNomePonto.setText(null);
        editTextEndereco.setText(null);
        spinnerCategoria.setSelection(0);

        Snackbar snackbar = Snackbar.make(scrollView, R.string.toast_limpar, Snackbar.LENGTH_LONG);
        snackbar.setAction(R.string.desfazer, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextNomePonto.setText(nomePontoTuristico);
                editTextEndereco.setText(enderecoPontoTuristico);
                spinnerCategoria.setSelection(categoria);

                if(viewFocus != null){
                    viewFocus.requestFocus();
                }
            }
        });
        snackbar.show();
    }

    public void salvarDados() {

        String nomePontoTuristico = editTextNomePonto.getText().toString().trim();
        String endereco = editTextEndereco.getText().toString().trim();
        int categoria = spinnerCategoria.getSelectedItemPosition();

        if(nomePontoTuristico.isEmpty()){
            Alert.mostrarAviso(this, R.string.erro_insercao_nome);
            editTextNomePonto.requestFocus();
            return;
        }

        if(endereco.isEmpty()){
            Alert.mostrarAviso(this, R.string.erro_insercao_endereco);
            editTextEndereco.requestFocus();
            return;
        }

        if(categoria == AdapterView.INVALID_POSITION){
            Alert.mostrarAviso(this, R.string.not_null_category);
            return;
        }

        if (viagemId <= 0) {
            Alert.mostrarAviso(this, R.string.erro_insercao);
            return;
        }

        PontoTuristico pontoTuristico = new PontoTuristico(viagemId, nomePontoTuristico, endereco, categoria);
        ViagemDatabase database = ViagemDatabase.getInstance(this);

        if (modo == MODO_NOVO) {
            long novoPontoTuristicoID = database.getPontoTuristicoDAO().insertPontoTuristico(pontoTuristico);

            if (novoPontoTuristicoID <= 0) {
                Alert.mostrarAviso(this, R.string.erro_insercao);
                return;
            }
            pontoTuristico.setId(novoPontoTuristicoID);

        } else {
            if (pontoTuristico.equals(pontoTuristicoOriginal)) {
                setResult(ViagemActivity.RESULT_CANCELED);
                finish();
                return;
            }

            pontoTuristico.setId(pontoTuristicoOriginal.getId());
            int linhasAlteradas = database.getPontoTuristicoDAO().update(pontoTuristico);

            if (linhasAlteradas != 1) {
                Alert.mostrarAviso(this, R.string.erro_alteracao);
                return;
            }
        }

        salvarUltimoTipo(categoria);

        Intent intentResposta = new Intent();
        intentResposta.putExtra(KEY_ID_PONTO, pontoTuristico.getId());
        setResult(RESULT_OK, intentResposta);
        finish();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.viagem_opcoes, menu);
        MenuItem itemPontoTuristico = menu.findItem(R.id.menuItemPontoTuristico);

        if(itemPontoTuristico != null){
            itemPontoTuristico.setVisible(modo == MODO_EDITAR);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int idMenuItem = item.getItemId();

        if(idMenuItem == R.id.menuItemSalvar){
            salvarDados();
            return true;
        } else if(idMenuItem == R.id.menuItemLimpar) {
            limparInputs();
            return true;
        } else if(idMenuItem == R.id.menuItemSugerirTipo){

            boolean valor = !item.isChecked();

            salvarSugerirTipo(valor);
            item.setChecked(valor);

            if(valor){
                spinnerCategoria.setSelection(ultimoTipo);
            }

            return true;

        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void lerPreferencias(){
        SharedPreferences shared = getSharedPreferences(ListaViagemActivity.ARQUIVO_PREFERENCIAS, Context.MODE_PRIVATE);
        sugerirTipo = shared.getBoolean(KEY_SUGERIR_TIPO, sugerirTipo);
        ultimoTipo = shared.getInt(KEY_ULTIMO_TIPO, ultimoTipo);
    }

    private void salvarSugerirTipo(boolean novoValor){
        SharedPreferences shared = getSharedPreferences(ListaViagemActivity.ARQUIVO_PREFERENCIAS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shared.edit();

        editor.putBoolean(KEY_SUGERIR_TIPO, novoValor);
        editor.commit();
        sugerirTipo = novoValor;
    }

    private  void salvarUltimoTipo(int novoValor){
        SharedPreferences shared = getSharedPreferences(ListaViagemActivity.ARQUIVO_PREFERENCIAS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shared.edit();

        editor.putInt(KEY_ULTIMO_TIPO, novoValor);
        editor.commit();
        ultimoTipo = novoValor;
    }
}
