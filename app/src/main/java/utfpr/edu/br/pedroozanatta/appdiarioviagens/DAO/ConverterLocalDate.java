package utfpr.edu.br.pedroozanatta.appdiarioviagens.DAO;

import androidx.room.TypeConverter;

import java.time.LocalDate;

public class ConverterLocalDate {

    @TypeConverter
    public static Long fromLocalDatetoLong(LocalDate date){

        if(date == null){
            return null;
        }
        return date.toEpochDay();
    }

    @TypeConverter
    public static LocalDate fromLongToLocalDate(Long epochDay) {

        if (epochDay == null) {
            return null;
        }
        return LocalDate.ofEpochDay(epochDay);
    }
}
