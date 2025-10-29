package utfpr.edu.br.pedroozanatta.appdiarioviagens.utillities;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.chrono.IsoChronology;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.FormatStyle;
import java.util.Locale;

public final class DateUtils {
    private DateUtils(){

    }

    public static String formatLocalDate(LocalDate data){

        if(data == null){
            return null;
        }

        String formatPattern = DateTimeFormatterBuilder.getLocalizedDateTimePattern(FormatStyle.SHORT, null, IsoChronology.INSTANCE, Locale.getDefault());
        formatPattern = formatPattern.replaceAll("\\byy\\b", "yyyy");
        formatPattern = formatPattern.replaceAll("\\bM\\b", "MM");
        formatPattern = formatPattern.replaceAll("\\bd\\b", "dd");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formatPattern, Locale.getDefault());
        return data.format(formatter);
    }

    public static long toMiliSeconds(LocalDate data){

        if(data == null){
            return 0;
        }
        return data.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }
}
