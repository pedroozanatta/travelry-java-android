package utfpr.edu.br.pedroozanatta.appdiarioviagens;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ViagemActivity extends AppCompatActivity {

    public static final String KEY_PAIS = "KEY_PAIS";
    public static final String KEY_LOCAL = "KEY_LOCAL";
    public static final String KEY_DATA = "KEY_DATA";
    public static final String KEY_CAPITAL = "KEY_CAPITAL";
    public static final String KEY_TIPO = "KEY_TIPO";
    public static final String KEY_CONTINENTE = "KEY_CONTINENTE";
    public static final String KEY_MODO = "MODO";
    public static  final String KEY_SUGERIR_TIPO = "SUGERIR_TIPO";
    public static  final String KEY_UTIMO_TIPO = "ULTIMO_TIPO";

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

        lerPreferencias();

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

                String pais = bundle.getString(ViagemActivity.KEY_PAIS);
                String local = bundle.getString(ViagemActivity.KEY_LOCAL);
                String data = bundle.getString(ViagemActivity.KEY_DATA);
                boolean capital = bundle.getBoolean(ViagemActivity.KEY_CAPITAL);
                String tipo = bundle.getString(ViagemActivity.KEY_TIPO);
                int continente = bundle.getInt(ViagemActivity.KEY_CONTINENTE);

                TipoViagem tipoViagem = TipoViagem.valueOf(tipo);

                viagemOriginal = new Viagem(pais, local, data, capital, tipoViagem, continente);

                editTextPais.setText(pais);
                editTextLocal.setText(local);
                editTextData.setText(data);
                checkBoxCapital.setChecked(capital);
                spinnerContinente.setSelection(continente);

                if(tipoViagem == TipoViagem.Nacional){
                    radioButtonNacional.setChecked(true);
                } else if(tipoViagem == TipoViagem.Internacional){
                    radioButtonInternacional.setChecked(true);
                }
            }
        }
    }

    public void limparInputs() {
        editTextPais.setText(null);
        editTextLocal.setText(null);
        editTextData.setText(null);
        checkBoxCapital.setChecked(false);
        radioGroupTipo.clearCheck();
        spinnerContinente.setSelection(0);

        editTextPais.requestFocus();

        Toast.makeText(this,
                       R.string.toast_limpar,
                       Toast.LENGTH_LONG).show();
    }

    public void salvarDados() {

        String pais = editTextPais.getText().toString();
        String local = editTextLocal.getText().toString();
        String data = editTextData.getText().toString();

        if (pais == null || pais.trim().isEmpty()) {
            Toast.makeText(this,
                           getString(R.string.not_null_pais),
                           Toast.LENGTH_LONG).show();

            editTextPais.requestFocus();
            return;
        }

        if (local == null || local.trim().isEmpty()) {
            Toast.makeText(this,
                           getString(R.string.not_null_local),
                           Toast.LENGTH_LONG).show();

            editTextLocal.requestFocus();
            return;
        }

        if (data == null || data.trim().isEmpty()) {
            Toast.makeText(this,
                           getString(R.string.not_null_data),
                           Toast.LENGTH_LONG).show();

            editTextData.requestFocus();
            return;
        }


        int radioButtonId = radioGroupTipo.getCheckedRadioButtonId();
        TipoViagem radioButtonSelecionado;

        if(radioButtonId == R.id.radioButtonNacional)
            radioButtonSelecionado = TipoViagem.Nacional;
        else if (radioButtonId == R.id.radioButtonInternacional)
            radioButtonSelecionado = TipoViagem.Internacional;
        else {
            Toast.makeText(this,
                            R.string.default_radiobutton,
                            Toast.LENGTH_LONG).show();
            return;
        }

        int continente = spinnerContinente.getSelectedItemPosition();

        if(continente == AdapterView.INVALID_POSITION){
            Toast.makeText(this,
                            R.string.not_null_continente,
                            Toast.LENGTH_LONG).show();
            return;
        }


        boolean capital = checkBoxCapital.isChecked();

        if(modo == MODO_EDITAR &&
           pais.equalsIgnoreCase(viagemOriginal.getPais()) &&
           local == viagemOriginal.getLocal() &&
           data == viagemOriginal.getData() &&
           capital == viagemOriginal.isCapital() &&
           radioButtonSelecionado == viagemOriginal.getTipoViagem() &&
           continente == viagemOriginal.getContinente()){

            setResult(ViagemActivity.RESULT_CANCELED);
            finish();
            return;
        }

        salvarUltimoTipo(continente);

        Intent intentResposta = new Intent();

        intentResposta.putExtra(KEY_PAIS, pais);
        intentResposta.putExtra(KEY_LOCAL, local);
        intentResposta.putExtra(KEY_DATA, data);
        intentResposta.putExtra(KEY_CAPITAL, capital);
        intentResposta.putExtra(KEY_TIPO, radioButtonSelecionado.toString());
        intentResposta.putExtra(KEY_CONTINENTE, continente);

        setResult(ViagemActivity.RESULT_OK, intentResposta);
        finish();
    }

    public void abrirCalendario(View view){
        Calendar calendario = Calendar.getInstance();

        editTextData.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                new DatePickerDialog(ViagemActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int ano, int mes, int dia) {
                        calendario.set(Calendar.YEAR, ano);
                        calendario.set(Calendar.MONTH, mes);
                        calendario.set(Calendar.DAY_OF_MONTH, dia);

                        String formatacao = "dd-MMM-yyyy";
                        SimpleDateFormat dateFormat = new SimpleDateFormat(formatacao, Locale.US);
                        editTextData.setText(dateFormat.format(calendario.getTime()));
                    }
                }, calendario.get(Calendar.YEAR), calendario.get(Calendar.MONTH), calendario.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
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
        }else if(idMenuItem == R.id.menuItemSugerirTipo){

            boolean valor = !item.isChecked();

            salvarSugerirTipo(valor);
            item.setChecked(valor);

            if(sugerirTipo){
                spinnerContinente.setSelection(ultimoTipo);
            }

            return true;

        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void lerPreferencias(){
        SharedPreferences shared = getSharedPreferences(ListaActivity.ARQUIVO_PREFERENCIAS, Context.MODE_PRIVATE);
        sugerirTipo = shared.getBoolean(KEY_SUGERIR_TIPO, sugerirTipo);
        ultimoTipo = shared.getInt(KEY_UTIMO_TIPO, ultimoTipo);
    }

    private void salvarSugerirTipo(boolean novoValor){
        SharedPreferences shared = getSharedPreferences(ListaActivity.ARQUIVO_PREFERENCIAS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shared.edit();

        editor.putBoolean(KEY_SUGERIR_TIPO, novoValor);
        editor.commit();
        sugerirTipo = novoValor;
    }

    private  void salvarUltimoTipo(int novoValor){
        SharedPreferences shared = getSharedPreferences(ListaActivity.ARQUIVO_PREFERENCIAS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shared.edit();

        editor.putInt(KEY_UTIMO_TIPO, novoValor);
        editor.commit();
        ultimoTipo = novoValor;
    }
}