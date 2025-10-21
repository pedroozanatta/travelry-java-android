package utfpr.edu.br.pedroozanatta.appdiarioviagens;

import androidx.annotation.NonNull;

import java.util.Comparator;

public class Viagem implements Cloneable{


    public static Comparator<Viagem> ordenacao = new Comparator<Viagem>() {
        @Override
        public int compare(Viagem viagem1, Viagem viagem2) {
            return viagem1.getPais().compareToIgnoreCase(viagem2.getPais());
        }
    };

    public static Comparator<Viagem> ordenacaoDecrescente = new Comparator<Viagem>() {
        @Override
        public int compare(Viagem viagem1, Viagem viagem2) {
            return -1 * viagem1.getPais().compareToIgnoreCase(viagem2.getPais());
        }
    };

    private String pais;
    private String local;
    private String data;
    private boolean capital;
    private TipoViagem tipoViagem;
    private int continente;

    public Viagem(String pais, String local, String data, boolean capital, TipoViagem tipoViagem, int continente) {
        this.pais = pais;
        this.local = local;
        this.data = data;
        this.capital = capital;
        this.tipoViagem = tipoViagem;
        this.continente = continente;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public boolean isCapital() {
        return capital;
    }

    public void setCapital(boolean capital) {
        this.capital = capital;
    }

    public TipoViagem getTipoViagem() {
        return tipoViagem;
    }

    public void setTipoViagem(TipoViagem tipoViagem) {
        this.tipoViagem = tipoViagem;
    }

    public int getContinente() {
        return continente;
    }

    public void setContinente(int continente) {
        this.continente = continente;
    }

    @NonNull
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return  pais + "\n" +
                local + "\n" +
                data + "\n" +
                capital + "\n" +
                tipoViagem + "\n" +
                continente;
    }
}