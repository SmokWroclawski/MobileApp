package ovh.olo.smok.smokwroclawski.Parser;



import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Michal on 2017-05-22.
 */

public class ParserISO8601 {

    public static String toDate(String iso8601string) {
        iso8601string = iso8601string.replace("T", " ").replace("Z", "");
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //, Locale.getDefault()
        try {
            sdf.setTimeZone(TimeZone.getTimeZone("UTC")); //getTimeZone("UTC")
            date = sdf.parse(iso8601string);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+1:00"));
        String formattedDate = sdf.format(date);
        return formattedDate;
//        return iso8601string.replace("T", " ").replace("Z", "");
    }
}
