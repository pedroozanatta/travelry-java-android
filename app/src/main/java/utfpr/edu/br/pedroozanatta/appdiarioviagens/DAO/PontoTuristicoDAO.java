package utfpr.edu.br.pedroozanatta.appdiarioviagens.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import utfpr.edu.br.pedroozanatta.appdiarioviagens.models.PontoTuristico;

@Dao
public interface PontoTuristicoDAO {
    @Insert
    long insertPontoTuristico(PontoTuristico pontoTuristico);

    @Delete
    int delete(PontoTuristico pontoTuristico);

    @Update
    int update(PontoTuristico pontoTuristico);

    @Query("SELECT * FROM PontoTuristico WHERE id =:id")
    PontoTuristico queryForId(long id);

    @Query("SELECT * FROM PontoTuristico WHERE idViagem =:idViagem ORDER BY nomePontoTuristico ASC")
    List<PontoTuristico> queryForIdViagem(long idViagem);

    @Query("SELECT count(*) FROM PontoTuristico WHERE idViagem=:idViagem")
    int totalIdViagem(long idViagem);
}
