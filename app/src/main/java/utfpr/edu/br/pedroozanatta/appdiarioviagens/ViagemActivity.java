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
import utfpr.edu.br.pedroozanatta.appdiarioviagens.models.TipoViagem;
import utfpr.edu.br.pedroozanatta.appdiarioviagens.models.Viagem;
import utfpr.edu.br.pedroozanatta.appdiarioviagens.utillities.Alert;
import utfpr.edu.br.pedroozanatta.appdiarioviagens.utillities.DateUtils;

public class ViagemActivity extends AppCompatActivity {


    public static final String KEY_ID_VIAGEM = "ID_VIAGEM";
    public static final String KEY_MODO = "MODO";
    public static  final String KEY_SUGERIR_TIPO = "SUGERIR_TIPO";
    public static  final String KEY_ULTIMO_TIPO = "ULTIMO_TIPO";
    public static final int MODO_NOVO = 0;
    public static final int MODO_EDITAR = 1;
    private EditText editTextPais, editTextLocal, editTextData;
    private CheckBox checkBoxCapital;
    private RadioGroup radioGroupTipo;
    private Spinner spinnerContinente;
    private RadioButton radioButtonNacional, radioButtonInternacional;
    private int modo;
    private Viagem viagemOriginal;
    private boolean sugerirTipo = false;
    private int ultimoTipo = 0;
    private LocalDate data;
    private int anosPassado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viagem);

        editTextPais = findViewById(R.id.editTextPais);
        editTextLocal = findViewById(R.id.editTextLocal);
        editTextData = findViewById(R.id.editTextData);
        checkBoxCapital = findViewById(R.id.checkBoxCapital);
        radioGroupTipo = findViewById(R.id.radioGroupTipo);
        spinnerContinente = findViewById(R.id.spinnerContinente);
        radioButtonNacional = findViewById(R.id.radioButtonNacional);
        radioButtonInternacional = findViewById(R.id.radioButtonInternacional);

        editTextData.setFocusable(false);
        editTextData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirCalendario();
            }
        });
        lerPreferencias();

        anosPassado = getResources().getInteger(R.integer.anos_passados);
        data = LocalDate.now().minusYears(anosPassado);

        Intent intentAbretura = getIntent();

        Bundle bundle  = intentAbretura.getExtras();

        if(bundle!=null){
            modo = bundle.getInt(KEY_MODO);

            if(modo == MODO_NOVO){
                setTitle(getString(R.string.cadastro_de_viagens));

                if(sugerirTipo){
                    spinnerContinente.setSelection(ultimoTipo);
                }

            } else {
                setTitle(getString(R.string.editar_viagem));

                long id = bundle.getLong(KEY_ID_VIAGEM);
                ViagemDatabase database = ViagemDatabase.getInstance(this);

                viagemOriginal = database.getViagemDAO().queryForId(id);

                editTextPais.setText(viagemOriginal.getPais());
                editTextLocal.setText(viagemOriginal.getLocal());

                if(viagemOriginal.getData() != null){
                    data = viagemOriginal.getData();
                }
                editTextData.setText(DateUtils.formatLocalDate(data));

                checkBoxCapital.setChecked(viagemOriginal.isCapital());
                spinnerContinente.setSelection(viagemOriginal.getContinente());

                TipoViagem tipoViagem = viagemOriginal.getTipoViagem();

                if(tipoViagem == TipoViagem.Nacional){
                    radioButtonNacional.setChecked(true);
                } else if(tipoViagem == TipoViagem.Internacional){
                    radioButtonInternacional.setChecked(true);
                }

                editTextPais.requestFocus();
                editTextPais.setSelection(editTextPais.getText().length());
            }
        }
    }

    public void limparInputs() {

        final String pais = editTextPais.getText().toString();
        final String local = editTextLocal.getText().toString();
        final LocalDate dataAnterior = data;
        final boolean capital = checkBoxCapital.isChecked();
        final int tipoViagem = radioGroupTipo.getCheckedRadioButtonId();
        final int continente = spinnerContinente.getSelectedItemPosition();

        final ScrollView scrollView = findViewById(R.id.main);
        final View viewFocus = scrollView.findFocus();

        editTextPais.setText(null);
        editTextLocal.setText(null);

        editTextData.setText(null);
        data = LocalDate.now().minusYears(anosPassado);

        checkBoxCapital.setChecked(false);
        radioGroupTipo.clearCheck();
        spinnerContinente.setSelection(0);
        editTextPais.requestFocus();

        Snackbar snackbar = Snackbar.make(scrollView, R.string.toast_limpar, Snackbar.LENGTH_LONG);

        snackbar.setAction(R.string.desfazer, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextPais.setText(pais);
                editTextLocal.setText(local);

                data = dataAnterior;
                editTextData.setText(DateUtils.formatLocalDate(data));

                checkBoxCapital.setChecked(capital);

                if(tipoViagem == R.id.radioButtonNacional){
                    radioButtonNacional.setChecked(true);
                } else if(tipoViagem == R.id.radioButtonInternacional){
                    radioButtonInternacional.setChecked(true);
                }

                spinnerContinente.setSelection(continente);

                if(viewFocus != null){
                    viewFocus.requestFocus();
                }
            }
        });
        snackbar.show();
    }

    public void salvarDados() {

        String pais = editTextPais.getText().toString();
        String local = editTextLocal.getText().toString();
        String dataSave = editTextData.getText().toString();

        if (pais == null || pais.trim().isEmpty()) {
            Alert.mostrarAviso(this, R.string.not_null_pais);
            editTextPais.requestFocus();
            return;
        }

        if (local == null || local.trim().isEmpty()) {
            Alert.mostrarAviso(this, R.string.not_null_local);
            editTextLocal.requestFocus();
            return;
        }

        if (dataSave == null || dataSave.trim().isEmpty()) {
            Alert.mostrarAviso(this, R.string.not_null_data);
            return;
        }


        int radioButtonId = radioGroupTipo.getCheckedRadioButtonId();
        TipoViagem radioButtonSelecionado;

        if(radioButtonId == R.id.radioButtonNacional)
            radioButtonSelecionado = TipoViagem.Nacional;
        else if (radioButtonId == R.id.radioButtonInternacional)
            radioButtonSelecionado = TipoViagem.Internacional;
        else {
            Alert.mostrarAviso(this, R.string.default_radiobutton);
            return;
        }

        int continente = spinnerContinente.getSelectedItemPosition();

        if(continente == AdapterView.INVALID_POSITION){
            Alert.mostrarAviso(this, R.string.not_null_continent);
            return;
        }


        boolean capital = checkBoxCapital.isChecked();

        Viagem viagem = new Viagem(pais, local, data, capital, radioButtonSelecionado, continente);

        if(viagem.equals(viagemOriginal)){
            setResult(ViagemActivity.RESULT_CANCELED);
            finish();
            return;
        }

        Intent intentResposta = new Intent();

        ViagemDatabase database = ViagemDatabase.getInstance(this);
        if(modo == MODO_NOVO){
            long novaViagem = database.getViagemDAO().insert(viagem);

            if(novaViagem <= 0){
                Alert.mostrarAviso(this, R.string.erro_insercao);
                return;
            }

            viagem.setId(novaViagem);

        } else{
            viagem.setId(viagemOriginal.getId());
            int linhasAlteradas = database.getViagemDAO().update(viagem);

            if(linhasAlteradas != 1){
                Alert.mostrarAviso(this, R.string.erro_alteracao);
                return;
            }
        }

        salvarUltimoTipo(continente);

        intentResposta.putExtra(KEY_ID_VIAGEM, viagem.getId());
        setResult(ViagemActivity.RESULT_OK, intentResposta);
        finish();
    }

    public void abrirCalendario(){
        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                data = LocalDate.of(year, month + 1, dayOfMonth);
                editTextData.setText(DateUtils.formatLocalDate(data));
            }
        };

        if(data == null){
            data = LocalDate.now().minusYears(anosPassado);
        }

        DatePickerDialog picker = new DatePickerDialog(this, R.style.SpinnerDatePickerDialog, listener, data.getYear(), data.getMonthValue() - 1, data.getDayOfMonth());

        long dataMax = DateUtils.toMiliSeconds(LocalDate.now());
        picker.getDatePicker().setMaxDate(dataMax);
        picker.show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.viagem_opcoes, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.menuItemSugerirTipo);
        item.setChecked(sugerirTipo);

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
        }else if(idMenuItem == R.id.menuItemPontoTuristico){
            abrirNovoPontoTuristico();
            return true;
        } else if(idMenuItem == R.id.menuItemSugerirTipo){

            boolean valor = !item.isChecked();

            salvarSugerirTipo(valor);
            item.setChecked(valor);

            if(valor){
                spinnerContinente.setSelection(ultimoTipo);
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

    private void abrirNovoPontoTuristico() {
        if (viagemOriginal == null) {
            Alert.mostrarAviso(this, R.string.erro_insercao);
            return;
        }

        Intent intentAbertura = new Intent(this, ListaPontosTuristicosActivity.class);
        intentAbertura.putExtra(ListaPontosTuristicosActivity.KEY_ID_VIAGEM, viagemOriginal.getId());
        startActivity(intentAbertura);
    }
}