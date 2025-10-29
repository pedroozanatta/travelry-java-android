package utfpr.edu.br.pedroozanatta.appdiarioviagens.DAO;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;


import utfpr.edu.br.pedroozanatta.appdiarioviagens.models.Viagem;

@Database(entities = {Viagem.class}, version = 3, exportSchema = true)
@TypeConverters({ConverterTipoViagem.class, ConverterLocalDate.class})
public abstract class ViagemDatabase extends RoomDatabase {

    public abstract  ViagemDAO getViagemDAO();

    private static ViagemDatabase INSTANCE;

    public static ViagemDatabase getInstance(final Context context){
        if (INSTANCE == null){
            synchronized (ViagemDatabase.class){
                if(INSTANCE == null){
                    Builder builder = Room.databaseBuilder(context, ViagemDatabase.class, "viagem.db");

                    builder.allowMainThreadQueries().build();

                    builder.addMigrations(new MigrationData());

                    if(INSTANCE == null)
                    INSTANCE = (ViagemDatabase) builder.build();
                }
            }
        }
        return INSTANCE;
    }
}
