package utfpr.edu.br.pedroozanatta.appdiarioviagens.models;

import static androidx.room.ForeignKey.CASCADE;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.time.LocalDateTime;
import java.util.Comparator;

@Entity(foreignKeys = {@ForeignKey( entity = Viagem.class,
                                    parentColumns = "id",
                                    childColumns = "idViagem",
                                    onDelete = CASCADE)})
public class PontoTuristico {

    public static Comparator<PontoTuristico> ordenacao = new Comparator<PontoTuristico>() {
        @Override
        public int compare(PontoTuristico ponto1, PontoTuristico ponto2) {
            return ponto1.getNomePontoTuristico().compareToIgnoreCase(ponto2.getNomePontoTuristico());
        }
    };

    public static Comparator<PontoTuristico> ordenacaoDecrescente = new Comparator<PontoTuristico>() {
        @Override
        public int compare(PontoTuristico ponto1, PontoTuristico ponto2) {
            return -1 * ponto1.getNomePontoTuristico().compareToIgnoreCase(ponto2.getNomePontoTuristico());
        }
    };

    @PrimaryKey(autoGenerate = true)
    private long id;

    private long idViagem;

    @NonNull
    @ColumnInfo(index = true)
    private String nomePontoTuristico;

    @NonNull
    private String endereco;

    @NonNull
    private int categoria;

    public PontoTuristico(long idViagem, @NonNull String nomePontoTuristico, @NonNull String endereco, int categoria) {
        this.idViagem = idViagem;
        this.nomePontoTuristico = nomePontoTuristico;
        this.endereco = endereco;
        this.categoria = categoria;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getIdViagem() {
        return idViagem;
    }

    public void setIdViagem(long idViagem) {
        this.idViagem = idViagem;
    }

    @NonNull
    public String getNomePontoTuristico() {
        return nomePontoTuristico;
    }

    public void setNomePontoTuristico(@NonNull String nomePontoTuristico) {
        this.nomePontoTuristico = nomePontoTuristico;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public int getCategoria() {
        return categoria;
    }

    public void setCategoria(int categoria) {
        this.categoria = categoria;
    }
}
