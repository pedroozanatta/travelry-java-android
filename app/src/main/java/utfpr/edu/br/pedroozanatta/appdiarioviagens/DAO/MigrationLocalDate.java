package utfpr.edu.br.pedroozanatta.appdiarioviagens.DAO;

import androidx.annotation.NonNull;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

public class MigrationLocalDate extends Migration {
    public MigrationLocalDate(){
        super(2, 3);
    }

    @Override
    public void migrate (@NonNull SupportSQLiteDatabase database){
        database.execSQL("ALTER TABLE Viagem ADD COLUMN data INTEGER");
    }
}
