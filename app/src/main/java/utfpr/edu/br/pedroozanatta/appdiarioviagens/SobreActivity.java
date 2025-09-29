package utfpr.edu.br.pedroozanatta.appdiarioviagens;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SobreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sobre);

        setTitle(getString(R.string.sobre_o_aplicativo));
    }

    public void abrirSiteAluno(View view){
        abrirSite("https://github.com/pedroozanatta");
    }

    private void abrirSite(String url){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));

        if(intent.resolveActivity(getPackageManager())!= null){
            startActivity(intent);
        } else{
            Toast.makeText(this,
                            R.string.nenhum_aplicativo_para_abrir_paginas_web,
                            Toast.LENGTH_LONG).show();
        }
    }

    public void enviarEmailAluno(View view){
        enviarEmail(new String[]{"pedroo.2023@alunos.utfpr.edu.br"},
                    getString(R.string.contato_pelo_aplicativo));
    }

    private void enviarEmail(String[] endereco, String assunto){
        Intent intent = new Intent(Intent.ACTION_SENDTO);

        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, endereco);
        intent.putExtra(Intent.EXTRA_SUBJECT, assunto);

        if(intent.resolveActivity(getPackageManager())!= null){
            startActivity(intent);
        } else{
            Toast.makeText(this,
                    R.string.nenhum_aplicativo_para_enviar_email,
                    Toast.LENGTH_LONG).show();
        }
    }
}