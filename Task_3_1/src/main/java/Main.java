import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        System.out.println("Какой день недели будет через 1024 дня:");
        LocalDate currDate = LocalDate.now();
        CalendarGregorian calendar1 = new CalendarGregorian(new CalendarGregorian.Date(currDate.getDayOfMonth(),currDate.getMonthValue(), currDate.getYear()));
        calendar1.addDays(1024);
        System.out.println(calendar1.getDayWeek()+"\n");
        calendar1.subDays(1024);

        System.out.println("Сколько лет, месяцев и дней назад был день победы 9 мая 1945 года:");
        CalendarGregorian.Date victoryDay = new CalendarGregorian.Date(9, CalendarGregorian.Month.MAY,1945);
        CalendarGregorian.Date dif = calendar1.difDateInFormat(victoryDay);
        System.out.println(dif.getDay() + " дней " + (dif.getMonthNumber()-1) + " месяцев " + (dif.getYear()-1) + " лет\n");

        System.out.println("В какой день недели вы родились:");
        CalendarGregorian calendar2 = new CalendarGregorian(new CalendarGregorian.Date(29, 3,2002));
        System.out.println(calendar2.getDayWeek() + "\n");

        System.out.println("Какой месяц будет через 17 недель");
        int x = 17*7;
        calendar1.addDays(x);
        System.out.println(calendar1.getMonth() + "\n");

        System.out.println("Сколько дней до нового года:");
        calendar1.subDays(x);
        CalendarGregorian.Date newYear = new CalendarGregorian.Date(1,1,calendar1.getYear()+1);
        System.out.println(calendar1.difDate(newYear) + "\n");

        System.out.println("Ближайшая пятница 13-го числа месяца:");
        CalendarGregorian.Date friday13 = new CalendarGregorian.Date(13, calendar1.getMonth(), calendar1.getYear());
        CalendarGregorian find13 = new CalendarGregorian(friday13);
        if (calendar1.getDayOfMonth()>13) {find13.addMonth();}
        while (find13.getDayWeek()!= CalendarGregorian.DAY_OF_WEEK.FRIDAY) {find13.addMonth();}
        System.out.println(find13.getDate());
    }
}
