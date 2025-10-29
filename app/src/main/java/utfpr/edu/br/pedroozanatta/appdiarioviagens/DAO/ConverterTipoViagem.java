package utfpr.edu.br.pedroozanatta.appdiarioviagens.DAO;

import androidx.room.TypeConverter;

import utfpr.edu.br.pedroozanatta.appdiarioviagens.models.TipoViagem;

public class ConverterTipoViagem {

    public static TipoViagem[] tipoViagens = TipoViagem.values();
    @TypeConverter
    public static int fromEnumToInt(TipoViagem tipoViagem){
        if(tipoViagem == null){
            return -1;
        }
        return tipoViagem.ordinal();
    }

    @TypeConverter
    public static TipoViagem fromIntToEnum(int ordinal){
        if(ordinal < 0){
            return null;
        } else {
            return tipoViagens[ordinal];
        }
    }
}
