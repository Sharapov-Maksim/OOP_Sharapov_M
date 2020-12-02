import org.junit.Test;

import static org.junit.Assert.*;

public class CalendarGregorianTest {

    @Test
    public void testCalendar1() {
        CalendarGregorian.DATE d1 = new CalendarGregorian.DATE(1,1,2021);
        CalendarGregorian calendar1 = new CalendarGregorian(d1);
        CalendarGregorian.DATE d2 = new CalendarGregorian.DATE(15,3,2021);
        calendar1.addDays(14);
        calendar1.addMonth();
        calendar1.addMonth();
        assertEqDates(d2, calendar1.getDate());
        calendar1.subMonth();
        calendar1.subMonth();
        calendar1.subMonth();
        calendar1.subMonth();
        d2.changeYear(2020);
        d2.changeMonths(11);
        assertEqDates(d2, calendar1.getDate());
        CalendarGregorian.DATE d3 = new CalendarGregorian.DATE(12, CalendarGregorian.MONTH.JULY,2020);
        CalendarGregorian calendar2 = new CalendarGregorian(d3);
        d3.changeMonths(CalendarGregorian.MONTH.JUNE);    // shouldn`t affect on calendar
        assertEquals(CalendarGregorian.DAY_OF_WEEK.SUNDAY, calendar2.getDayWeek());
    }

    @Test
    public void testCalendar2() {
        CalendarGregorian.DATE date = new CalendarGregorian.DATE(15, CalendarGregorian.MONTH.OCTOBER, 1582);
        CalendarGregorian calendar = new CalendarGregorian(date);
        assertEquals(CalendarGregorian.DAY_OF_WEEK.FRIDAY, calendar.getDayWeek());
        calendar.addYear();
        assertEquals(1583,calendar.getYear());
        assertEquals(365, calendar.difDate(date));
        calendar.subYear();
        calendar.subYear();
        calendar.subYear();
        calendar.subYear();
        assertEquals(1579,calendar.getYear());
    }

    void assertEqDates(CalendarGregorian.DATE d1, CalendarGregorian.DATE d2){
        assertEquals(d1.getDAY(), d2.getDAY());
        assertEquals(d1.getMONTH(), d2.getMONTH());
        assertEquals(d1.getYEAR(), d2.getYEAR());
    }
}