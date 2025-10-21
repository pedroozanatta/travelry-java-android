package utfpr.edu.br.pedroozanatta.appdiarioviagens.utillities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import utfpr.edu.br.pedroozanatta.appdiarioviagens.R;

public final class Alert {
    private Alert(){

    }

    public static void mostrarAviso(Context context, int id){
        mostrarAviso(context, context.getString(id), null);
    }

    public static void mostrarAviso(Context context, int id, DialogInterface.OnClickListener listener){
        mostrarAviso(context, context.getString(id), listener);
    }

    public static void mostrarAviso(Context context, String mensagem, DialogInterface.OnClickListener listener){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle(R.string.aviso);
        builder.setIcon(android.R.drawable.ic_dialog_info);
        builder.setMessage(mensagem);

        builder.setNeutralButton(R.string.ok, listener);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public static void confirmarAcao(Context context, String mensagem, DialogInterface.OnClickListener listenerSim, DialogInterface.OnClickListener listenerNao){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle(R.string.confirmacao);
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setMessage(mensagem);

        builder.setPositiveButton(R.string.sim_alert, listenerSim);
        builder.setNegativeButton(R.string.nao_alert, listenerNao);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public static void confirmarAcao(Context context, int id, DialogInterface.OnClickListener listenerSim, DialogInterface.OnClickListener listenerNao){
        confirmarAcao(context, context.getString(id), listenerSim, listenerNao);
    }
}
