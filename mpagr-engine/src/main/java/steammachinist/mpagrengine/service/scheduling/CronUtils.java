package steammachinist.mpagrengine.service.scheduling;

import org.springframework.stereotype.Component;

@Component
public class CronUtils {
    public static String toCron(String time) {
        String[] parts = time.split(":");
        String hours = parts[0];
        if (hours.charAt(0) == '0') {
            hours = hours.substring(1);
        }
        String minutes = parts[1];
        if (minutes.charAt(0) == '0') {
            minutes = minutes.substring(1);
        }
        return String.format("0 %s %s * * *", minutes, hours);
    }

    public static String fromCron(String cron) {
        String[] parts = cron.split(" ");
        String hours = parts[2];
        if (hours.length() < 2) {
            hours = '0' + hours;
        }
        String minutes = parts[1];
        if (minutes.length() < 2) {
            minutes = '0' + minutes;
        }
        return String.format("%s:%s", hours, minutes);
    }
}
