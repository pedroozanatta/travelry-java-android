package utfpr.edu.br.pedroozanatta.appdiarioviagens.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.Objects;

@Entity
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

    @PrimaryKey(autoGenerate = true)
    private long id;
    @NonNull
    @ColumnInfo(index = true)
    private String pais;
    @NonNull
    private String local;
    @NonNull
    private LocalDate data;
    private boolean capital;
    private TipoViagem tipoViagem;
    private int continente;

    public Viagem(String pais, String local, LocalDate data, boolean capital, TipoViagem tipoViagem, int continente) {
        this.pais = pais;
        this.local = local;
        this.data = data;
        this.capital = capital;
        this.tipoViagem = tipoViagem;
        this.continente = continente;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
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
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public boolean equals(Object o) {
        if(this == o)
            return true;

        if (o == null || getClass() != o.getClass())
            return false;

        Viagem viagem = (Viagem) o;

        if(data == null && viagem.data != null){
            return false;
        }
        if(data != null && data.equals(viagem.data) == false){
            return false;
        }

        return capital == viagem.capital &&
                          continente == viagem.continente &&
                          pais.equals(viagem.pais) &&
                          local.equals(viagem.local) &&
                          tipoViagem == viagem.tipoViagem;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pais, local, data, capital, tipoViagem, continente);
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