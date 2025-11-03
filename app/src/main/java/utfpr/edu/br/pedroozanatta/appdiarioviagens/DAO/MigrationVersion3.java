package utfpr.edu.br.pedroozanatta.appdiarioviagens.DAO;

import androidx.annotation.NonNull;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

public class MigrationVersion3 extends Migration {
    public MigrationVersion3() {
        super(3, 4);
    }

    @Override
    public void migrate(@NonNull SupportSQLiteDatabase database) {
        database.execSQL("CREATE TABLE IF NOT EXISTS `PontoTuristico` (" +
                "`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "`idViagem` INTEGER NOT NULL, " +
                "`nomePontoTuristico` TEXT NOT NULL, " +
                "`rua` TEXT NOT NULL, " +
                "`endereco` TEXT NOT NULL, " +
                "FOREIGN KEY(`idViagem`) REFERENCES `Viagem`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE)");

        database.execSQL("CREATE INDEX IF NOT EXISTS `index_PontoTuristico_nomePontoTuristico` ON `PontoTuristico` (`nomePontoTuristico`)");

    }
}
