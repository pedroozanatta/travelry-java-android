package utfpr.edu.br.pedroozanatta.appdiarioviagens.DAO;

import androidx.annotation.NonNull;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

public class MigrationData extends Migration {

    public MigrationData() {
        super(1, 2);
    }

    @Override
    public void migrate(@NonNull SupportSQLiteDatabase database) {

        database.execSQL("CREATE TABLE IF NOT EXISTS `ViagemTemp` ("+
                            " `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"+
                            " `pais` TEXT NOT NULL,"+
                            " `local` TEXT NOT NULL,"+
                            " `data` TEXT NOT NULL,"+
                            " `capital` INTEGER NOT NULL,"+
                            " `tipoViagem` INTEGER, "+
                            " `continente` INTEGER NOT NULL)");

        database.execSQL("INSERT INTO ViagemTemp (id, pais, local, data, capital, tipoViagem, continente) " +
                "SELECT id, pais, local, data, capital, " +
                "CASE " +
                "  WHEN tipoViagem = 'Nacional' THEN 0 " +
                "  WHEN tipoViagem = 'Internacional' THEN 1 " +
                "  ELSE -1 " +
                "END, " +
                "continente " +
                "FROM Viagem");

        database.execSQL("DROP TABLE Viagem");
        database.execSQL("ALTER TABLE ViagemTemp RENAME TO Viagem");
        database.execSQL("CREATE INDEX IF NOT EXISTS `index_Viagem_pais` ON `Viagem` (`pais`)");
    }
}
