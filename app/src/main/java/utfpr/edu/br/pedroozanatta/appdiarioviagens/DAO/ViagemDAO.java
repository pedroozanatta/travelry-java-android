package utfpr.edu.br.pedroozanatta.appdiarioviagens.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import utfpr.edu.br.pedroozanatta.appdiarioviagens.models.Viagem;

@Dao
public interface ViagemDAO {

    @Insert
    long insert(Viagem viagem);

    @Delete
    int delete(Viagem viagem);

    @Update
    int update(Viagem viagem);

    @Query("SELECT * FROM Viagem ORDER BY pais ASC")
    List<Viagem> queryAllAscending();

    @Query("SELECT * FROM Viagem ORDER BY pais DESC")
    List<Viagem> queryAllDownward();

    @Query("SELECT * FROM Viagem WHERE id=:id")
    Viagem queryForId(long id);
}
