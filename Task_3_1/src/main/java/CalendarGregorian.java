public class CalendarGregorian {
    /**
     * Класс, реализующий дату
     */
    public static class Date {
        private int dayOfMonth;
        private Month monthOfYear;
        private int year;

        /**
         * Конструктор даты
         * @param day день, целое число >0
         * @param month порядковый номер месяца, целое число >0
         * @param year год, целое число >0
         */
        public Date(int day, int month, int year){
            if ((day <1)||(day >31)||(month <1)||(month >12)||(year <1)) throw new IllegalArgumentException("Неверный формат даты");
            Month[] months = Month.values();

            this.monthOfYear = months[month -1];
            if (monthOfYear != Month.FEBRUARY){
                if (day > monthOfYear.getDaysInMonth()) throw new IllegalArgumentException("В этом месяце нет такого дня");
            }
            else {
                if (isLeap(year) && (day >29)) throw new IllegalArgumentException("В этом месяце нет такого дня");
                if (!isLeap(year) && (day >28)) throw new IllegalArgumentException("В этом месяце нет такого дня");
            }
            this.dayOfMonth = day;
            this.year = year;
        }
        /**
         * Аналог предыдущего конструктора даты
         * @param day день, целое число >0
         * @param month месяц, элемент перечисления Month
         * @param year год, целое число >0
         */
        public Date(int day, Month month, int year){
            if ((day <1)||(day >31)||(year <1)) throw new IllegalArgumentException("Неверный формат даты");
            this.monthOfYear = month;
            if (monthOfYear != Month.FEBRUARY){
                if (day > monthOfYear.getDaysInMonth()) throw new IllegalArgumentException("В этом месяце нет такого дня");
            }
            else {
                if (isLeap(year) && (day >29)) throw new IllegalArgumentException("В этом месяце нет такого дня");
                if (!isLeap(year) && (day >28)) throw new IllegalArgumentException("В этом месяце нет такого дня");
            }
            this.dayOfMonth = day;
            this.year = year;
        }

        /**
         * Получить день из даты
         * @return номер дня месяца, целое число >0
         */
        public int getDay(){return dayOfMonth;}

        /**
         * Получить месяц из даты
         * @return месяц, элемент перечисления Month
         */
        public Month getMonth(){return monthOfYear;}

        /**
         * Получить месяц из даты
         * @return номер месяца, целое число >0
         */
        public int getMonthNumber(){return monthOfYear.ordinal()+1;}

        /**
         * Получить год из даты
         * @return год, целое число >0
         */
        public int getYear(){return year;}

        /**
         * Изменить день в дате
         * @param day номер дня в месяце (>0)
         */
        public void changeDay(int day){
            if ((day <1)||(day > monthOfYear.getDaysInMonth())) throw new IllegalArgumentException("В этом месяце нет такого дня");
            this.dayOfMonth = day;
        }

        /**
         * Изменить месяц в дате
         * @param month номер месяца в году [1;12]
         */
        public void changeMonths(int month){
            if ((month <1)||(month >12)) throw new IllegalArgumentException("В Григорианском календаре только 12 месяцев");
            Month[] m = Month.values();
            this.monthOfYear = m[month -1];
        }

        /**
         * Изменить месяц в дате
         * @param month месяц из перечисления Month
         */
        public void changeMonths(Month month){
            if(month == null) throw new IllegalArgumentException("Передан null в качестве месяца");
            this.monthOfYear = month;
        }

        /**
         * Изменить год в дате
         * @param year год (>0)
         */
        public void changeYear(int year){
            if ((year <1)) throw new IllegalArgumentException("Год до нашей эры");
            this.year = year;
        }

        @Override
        public String toString() {
            String s = switch (dayOfMonth){
                case 1 -> "st";
                case 2 -> "nd";
                case 3 -> "rd";
                default -> "th";
            };
            return dayOfMonth + s +" of " + monthOfYear +", " + year + " year";
        }
    }

    /**
     * Перечисление месяцев
     */
    public enum Month {
        JANUARY(31),FEBRUARY(28),MARCH(31),APRIL(30),MAY(31),JUNE(30),
        JULY(31),AUGUST(31),SEPTEMBER(30),OCTOBER(31),NOVEMBER(30),DECEMBER(31);

        int DAYS_IN_MONTH;
        Month(int D){
            DAYS_IN_MONTH = D;
        }

        /**
         * Получить количество дней в месяце (в Феврале будет возвращаться 28)
         * @return количество дней в месяце
         */
        int getDaysInMonth(){
            return DAYS_IN_MONTH;
        }

        @Override
        public String toString() {
            return switch (this){
                case JANUARY -> "January";
                case FEBRUARY -> "February";
                case MARCH -> "March";
                case APRIL -> "April";
                case MAY -> "May";
                case JUNE -> "June";
                case JULY -> "July";
                case AUGUST -> "August";
                case SEPTEMBER -> "September";
                case OCTOBER -> "October";
                case NOVEMBER -> "November";
                case DECEMBER -> "December";
            };
        }
    }

    /**
     * Перечисление дней недели
     */
    public enum DAY_OF_WEEK{
        MONDAY,
        TUESDAY,
        WEDNESDAY,
        THURSDAY,
        FRIDAY,
        SATURDAY,
        SUNDAY
    }

    private int dayOfEra;
    private Date currentDate;

    /**
     * Конструктор календаря
     * @param date дата, котрую календарь будет хранить и считать текущей датой
     */
    public CalendarGregorian(Date date){
        if (date == null) throw new IllegalArgumentException("Аргумент date дожен быть != null");
        this.dayOfEra = toDays(date.getDay(),date.getMonthNumber(),date.getYear());
        this.currentDate = new Date(date.getDay(),date.getMonth(),date.getYear());
    }

    /**
     * Получить день из даты, записанной в каленаре
     * @return текущий день из даты
     */
    public int getDayOfMonth(){
        return currentDate.getDay();
    }

    /**
     * Получить месяц из даты, записанной в каленаре
     * @return текущий месяц из даты, представленный в типе перечисления Month,
     *         чтобы получить номер этого месяца, используйте getMonthNumber на полученном месяце
     */
    public Month getMonth(){
        return currentDate.getMonth();
    }

    /**
     * Получить год из даты, записанной в каленаре
     * @return текущий год из даты
     */
    public int getYear(){
        return currentDate.getYear();
    }

    /**
     * Получить текущую дату, записанную в календаре
     * @return !копия! текущей даты, записанной в календаре, типа Date
     */
    public Date getDate(){
        return new Date(currentDate.getDay(),currentDate.getMonth(),currentDate.getYear());
    }

    /**
     * Получить день недели, соответствующий текущей дате
     * @return день недели в типе перечисления DAY_OF_WEEK
     */
    public DAY_OF_WEEK getDayWeek() {
        DAY_OF_WEEK[] days = DAY_OF_WEEK.values();
        return days[(dayOfEra) % 7];
    }

    /**
     * добавить дни к дате
     * @param daysCount колличество добавляемых дней (может быть отрицательным, но тогда при вычитании не должно получиться даты раньше 01.01.0001)
     */
    public void addDays(int daysCount){
        int days = this.dayOfEra + daysCount;
        if (days < 0) throw new IllegalArgumentException("При сложении получится дата до нашей эры");
        this.dayOfEra = days;
        this.currentDate = toDate(dayOfEra);
    }

    /**
     * добавить один месяц к дате
     */
    public void addMonth(){
        Month[] m = Month.values();
        if(currentDate.getMonthNumber()!=12){
            this.currentDate.changeMonths(currentDate.getMonthNumber()+1);
            this.dayOfEra = toDays(currentDate.getDay(),currentDate.getMonthNumber(),currentDate.getYear());
        }
        else {
            this.currentDate.changeMonths(1);
            this.currentDate.changeYear(currentDate.year +1);
            this.dayOfEra = toDays(currentDate.getDay(),currentDate.getMonthNumber(),currentDate.getYear());
        }
    }

    /**
     * добавить 1 год к дате
     */
    public void addYear(){
        this.currentDate.changeYear(currentDate.getYear()+1);
        this.dayOfEra = toDays(currentDate.getDay(),currentDate.getMonthNumber(),currentDate.getYear());
    }

    /**
     * вычесть дни из текущей даты
     * @param daysCount количество дней для вычитания (при вычитании не должно получиться даты раньше 01.01.0001)
     *                  если daysCount < 0 дни будут прибавляться
     */
    public void subDays(int daysCount){
        int days = this.dayOfEra - daysCount;
        if (days < 0) throw new IllegalArgumentException("При вычитании получится дата до нашей эры");
        this.dayOfEra = days;
        this.currentDate = toDate(dayOfEra);
    }

    /**
     * вычесть один месяц из даты
     */
    public void subMonth(){
        Month[] m = Month.values();
        if(currentDate.getMonthNumber()!=1){
            this.currentDate.changeMonths(currentDate.getMonthNumber()-1);
            this.dayOfEra = toDays(currentDate.getDay(),currentDate.getMonthNumber(),currentDate.getYear());
        }
        else {
            this.currentDate.changeMonths(12);
            this.currentDate.changeYear(currentDate.year -1);
            this.dayOfEra = toDays(currentDate.getDay(),currentDate.getMonthNumber(),currentDate.getYear());
        }
    }

    /**
     * вычесть один год из даты
     */
    public void subYear(){
        this.currentDate.changeYear(currentDate.getYear()-1);
        dayOfEra = toDays(currentDate.getDay(),currentDate.getMonthNumber(),currentDate.getYear());
    }


    /**
     * количество дней до или после даты x
     * @param x дата, с которой будет сравниваться текущая дата
     * @return разница в днях между датами
     */
    public int difDate(Date x){
        if(x == null) throw new IllegalArgumentException("Дата x должна быть != null");
        int dayX = toDays(x.getDay(), x.getMonthNumber(), x.getYear());
        if (dayX<dayOfEra) return dayOfEra-dayX;
        else return dayX-dayOfEra;
    }

    /**
     * аналогично difDate вычисляет разницу между датами в формате с днями, месяцами и годами
     * @param x дата, с которой будет сравниваться текущая дата
     * @return разница между датами
     */
    public Date difDateInFormat(Date x){
        if(x == null) throw new IllegalArgumentException("Дата x должна быть != null");
        int dayX = toDays(x.getDay(), x.getMonthNumber(), x.getYear());
        int dayY;
        if (dayX < dayOfEra) dayY = dayOfEra;
        else {
            dayY = dayX;
            dayX = dayOfEra;
        }
        int cntDays = dayY-dayX;
        return toDate(cntDays);
    }

    private int toDays(int dayOfMonth, int month, int year){
        int tmp = year;
        int daysCnt = 0;
        year--;
        daysCnt+= 365 * year;
        daysCnt+=97 * (year/400);
        year %=400;
        daysCnt+=year/100 * 24;
        year %=100;
        daysCnt+=year/4;
        Month[] months = Month.values();
        for(int i = 0; i<month-1; i++) {
            if (i != 1) {
                daysCnt += months[i].getDaysInMonth();
            }
            else if (!isLeap(tmp)){
                daysCnt += months[i].getDaysInMonth();
            }
            else {
                daysCnt += months[i].getDaysInMonth()+1;
            }
        }
        daysCnt+= dayOfMonth -1;
        return daysCnt;
    }

    private Date toDate(int daysCnt){
        int year = 1;
        int month = 1;
        int day = 1;
        while (daysCnt>365){
            daysCnt -= 365;
            if(isLeap(year++)) daysCnt--;
        }
        if ((daysCnt==356)&&(!isLeap(year))){
            daysCnt -= 365;
            year++;
        }
        Month[] M = Month.values();
        for(int i = 0; i<12; i++){
            if(i!=1) {
                if (daysCnt > M[i].getDaysInMonth()) {
                    daysCnt -= M[i].getDaysInMonth();
                    month++;
                } else {
                    break;
                }
            }
            else{
                if(isLeap(year)){
                    if (daysCnt > M[i].getDaysInMonth()+1) {
                        daysCnt -= M[i].getDaysInMonth();
                        month++;
                    } else {
                        break;
                    }
                }
                else {
                    if (daysCnt > M[i].getDaysInMonth()) {
                        daysCnt -= M[i].getDaysInMonth();
                        month++;
                    } else {
                        break;
                    }
                }
            }
        }
        day+=daysCnt;
        return new Date(day,month,year);
    }


    private static boolean isLeap(int year){
        return (year % 400 == 0) || ((year % 100 != 0) && (year % 4 == 0));
    }
}
