package utfpr.edu.br.pedroozanatta.appdiarioviagens.DAO;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;


import utfpr.edu.br.pedroozanatta.appdiarioviagens.models.PontoTuristico;
import utfpr.edu.br.pedroozanatta.appdiarioviagens.models.Viagem;

@Database(entities = {Viagem.class, PontoTuristico.class}, version = 4, exportSchema = true)
@TypeConverters({ConverterTipoViagem.class, ConverterLocalDate.class})
public abstract class ViagemDatabase extends RoomDatabase {

    public abstract ViagemDAO getViagemDAO();
    public abstract PontoTuristicoDAO getPontoTuristicoDAO();
    private static ViagemDatabase INSTANCE;

    public static ViagemDatabase getInstance(final Context context){
        if (INSTANCE == null){
            synchronized (ViagemDatabase.class){
                if(INSTANCE == null){
                    Builder builder = Room.databaseBuilder(context, ViagemDatabase.class, "viagem.db");

                    builder.allowMainThreadQueries().build();

                    builder.addMigrations(new MigrationData());
                    builder.addMigrations(new MigrationLocalDate());
                    builder.addMigrations(new MigrationVersion3());

                    if(INSTANCE == null)
                        INSTANCE = (ViagemDatabase) builder.build();
                }
            }
        }
        return INSTANCE;
    }
}
