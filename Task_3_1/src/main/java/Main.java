import java.time.LocalDate;
import java.util.Date;
import java.util.GregorianCalendar;

public class Main {
    public static void main(String[] args) {
        System.out.println("Какой день недели будет через 1024 дня:");
        LocalDate currDate = LocalDate.now();
        CalendarGregorian calendar1 = new CalendarGregorian(new CalendarGregorian.DATE(currDate.getDayOfMonth(),currDate.getMonthValue(), currDate.getYear()));
        calendar1.addDays(1024);
        System.out.println(calendar1.getDayWeek()+"\n");
        calendar1.subDays(1024);

        System.out.println("Сколько лет, месяцев и дней назад был день победы 9 мая 1945 года:");
        CalendarGregorian.DATE victoryDay = new CalendarGregorian.DATE(9, CalendarGregorian.MONTH.MAY,1945);
        CalendarGregorian.DATE dif = calendar1.difDateInDDMMYYYYformat(victoryDay);
        System.out.println(dif.getDAY() + " дней " + (dif.getMONTH_NUMBER()-1) + " месяцев " + (dif.getYEAR()-1) + " лет\n");

        System.out.println("В какой день недели вы родились:");
        CalendarGregorian calendar2 = new CalendarGregorian(new CalendarGregorian.DATE(29, 3,2002));
        System.out.println(calendar2.getDayWeek() + "\n");

        System.out.println("Какой месяц будет через 17 недель");
        int x = 17*7;
        calendar1.addDays(x);
        System.out.println(calendar1.getMonth() + "\n");

        System.out.println("Сколько дней до нового года:");
        calendar1.subDays(x);
        CalendarGregorian.DATE newYear = new CalendarGregorian.DATE(1,1,calendar1.getYear()+1);
        System.out.println(calendar1.difDate(newYear) + "\n");

        System.out.println("Ближайшая пятница 13-го числа месяца:");
        CalendarGregorian.DATE friday13 = new CalendarGregorian.DATE(13, calendar1.getMonth(), calendar1.getYear());
        CalendarGregorian find13 = new CalendarGregorian(friday13);
        if (calendar1.getDayOfMonth()>13) {find13.addMonth();}
        while (find13.getDayWeek()!= CalendarGregorian.DAY_OF_WEEK.FRIDAY) {find13.addMonth();}
        System.out.println(find13.getDate());
    }
}
