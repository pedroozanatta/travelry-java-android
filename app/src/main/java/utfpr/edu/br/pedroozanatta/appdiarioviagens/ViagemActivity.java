package utfpr.edu.br.pedroozanatta.appdiarioviagens;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

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
    private EditText editTextPais, editTextLocal, editTextData;
    private CheckBox checkBoxCapital;
    private RadioGroup radioGroupTipo;
    private Spinner spinnerContinente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viagem);

        setTitle(getString(R.string.cadastro_de_viagens));

        editTextPais = findViewById(R.id.editTextPais);
        editTextLocal = findViewById(R.id.editTextLocal);
        editTextData = findViewById(R.id.editTextData);
        checkBoxCapital = findViewById(R.id.checkBoxCapital);
        radioGroupTipo = findViewById(R.id.radioGroupTipo);
        spinnerContinente = findViewById(R.id.spinnerContinente);
    }

    public void limparInputs(View view) {
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

    public void salvarDados(View view) {

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
}