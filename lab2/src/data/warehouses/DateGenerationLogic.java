package data.warehouses;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

public class DateGenerationLogic {
    public static LocalDate getRandomDate(int firstYear, int lastYear) {
        int randomYear = getRandomYear(firstYear, lastYear);
        int randomMonth = getRandomMonth();
        int randomDay = getRandomDay(randomMonth, randomYear);

        return LocalDate.of(randomYear, randomMonth, randomDay);
    }

    public static LocalDateTime getRandomDateWithHour(LocalDate date) {
        int hour = ThreadLocalRandom.current().nextInt(0, 24);
        int minute = ThreadLocalRandom.current().nextInt(0, 60);

        return date.atTime(hour, minute);
    }

    public static String getDateAsString(LocalDateTime dateWithHour) {
        StringBuilder result = new StringBuilder(dateWithHour.toString());
        result.setCharAt(10, ' ');
        result.append(":00");
        return result.toString();
    }

    public static String getRandomFutureDateAsString(String date, boolean newer, boolean checker) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(date, formatter);

        if (!checker && newer)
            return getRandomDateSince2017();
        else
            return newer ? getDateSince2017(dateTime) : getDateTo2017(dateTime);
    }

    public static LocalDate getPartiallyRandomDate() {
        int temp = ThreadLocalRandom.current().nextInt(0,30);
        int randomYear;

        if (temp < 2)
            randomYear = getRandomYear(1975, 1987);
        else if (temp < 6)
            randomYear = getRandomYear(1987, 1996);
        else if (temp < 15)
            randomYear = getRandomYear(1996, 2005);
        else
            randomYear = getRandomYear(2005, 2016);

        int randomMonth = getRandomMonth();
        int randomDay = getRandomDay(randomMonth, randomYear);

        return LocalDate.of(randomYear, randomMonth, randomDay);
    }

    private static String getRandomDateSince2017() {
        int randomYear = getRandomYear(2017, 2019);
        int randomMonth = getRandomMonth();
        int randomDay = getRandomDay(randomMonth, randomYear);

        LocalDate date = LocalDate.of(randomYear, randomMonth, randomDay);
        LocalDateTime localDateTime = date.atTime(12, 0);

        return getDateAsString(localDateTime);
    }

    private static String getDateSince2017(LocalDateTime dateTime) {
        dateTime = dateTime.plusMinutes(getRandom(60));
        dateTime = dateTime.plusDays(getRandom(60));

        return getDateAsString(dateTime);
    }

    private static String getDateTo2017(LocalDateTime dateTime) {
        int year = dateTime.getYear();
        int month = dateTime.getMonthValue();

        if (year == 2016 && month == 12) {
            dateTime = dateTime.plusMinutes(getRandom(dateTime.getDayOfYear() > 28 ? 10 : 120));
        }
        else if (year == 2016) {
            dateTime = dateTime.plusHours(getRandom(45));
            dateTime = dateTime.plusMinutes(getRandom(45));
        }
        else if (year > 2000 && year < 2016) {
            dateTime = dateTime.plusDays(getRandom(365));
            dateTime = dateTime.plusMinutes(getRandom(30));
        }
        else {
            dateTime = dateTime.plusMonths(getRandom(60));
            dateTime = dateTime.plusMinutes(getRandom(30));
        }

        return getDateAsString(dateTime);
    }

    private static int getRandomYear(int firstYear, int lastYear) {
        return ThreadLocalRandom.current().nextInt(firstYear, lastYear + 1);
    }

    private static int getRandomMonth() {
        return ThreadLocalRandom.current().nextInt(1, 12 + 1);
    }

    private static int getRandomDay(int month, int year) {
        return ThreadLocalRandom.current().nextInt(1, getMaxDay(month, year) + 1);
    }

    private static int getMaxDay(int month, int year) {
        if (month == 2)
            return Year.isLeap(year) ? 29 : 28;
        else
            return Arrays.stream(new int[] {1, 3, 5, 7, 8, 10, 12}).anyMatch(i -> i == month) ? 31 : 30;
    }

    private static int getRandom(int max) {
        return ThreadLocalRandom.current().nextInt(1, max);
    }
}