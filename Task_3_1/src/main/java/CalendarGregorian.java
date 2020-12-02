public class CalendarGregorian {
    /**
     * Класс, имплементирующий дату
     */
    public static class DATE{
        private int DAY_OF_MONTH;
        private MONTH MONTH_OF_YEAR;
        private int YEAR;

        /**
         * Конструктор даты
         * @param DD день, целое число >0
         * @param MM порядковый номер месяца, целое число >0
         * @param YYYY год, целое число >0
         */
        public DATE (int DD, int MM, int YYYY){
            if ((DD<1)||(DD>31)||(MM<1)||(MM>12)||(YYYY<1)) throw new IllegalArgumentException("Неверный формат даты");
            MONTH[] months = MONTH.values();

            this.MONTH_OF_YEAR = months[MM-1];
            if (MONTH_OF_YEAR!=MONTH.FEBRUARY){
                if (DD>MONTH_OF_YEAR.getDAYS_IN_MONTH()) throw new IllegalArgumentException("В этом месяце нет такого дня");
            }
            else {
                if (isLEAP(YYYY) && (DD>29)) throw new IllegalArgumentException("В этом месяце нет такого дня");
                if (!isLEAP(YYYY) && (DD>28)) throw new IllegalArgumentException("В этом месяце нет такого дня");
            }
            this.DAY_OF_MONTH = DD;
            this.YEAR = YYYY;
        }
        /**
         * Аналог предыдущего конструктора даты
         * @param DD день, целое число >0
         * @param MM месяц, элемент перечисления MONTH
         * @param YYYY год, целое число >0
         */
        public DATE (int DD, MONTH MM, int YYYY){
            if ((DD<1)||(DD>31)||(YYYY<1)) throw new IllegalArgumentException("Неверный формат даты");
            this.MONTH_OF_YEAR = MM;
            if (MONTH_OF_YEAR!=MONTH.FEBRUARY){
                if (DD>MONTH_OF_YEAR.getDAYS_IN_MONTH()) throw new IllegalArgumentException("В этом месяце нет такого дня");
            }
            else {
                if (isLEAP(YYYY) && (DD>29)) throw new IllegalArgumentException("В этом месяце нет такого дня");
                if (!isLEAP(YYYY) && (DD>28)) throw new IllegalArgumentException("В этом месяце нет такого дня");
            }
            this.DAY_OF_MONTH = DD;
            this.YEAR = YYYY;
        }

        /**
         * Получить день из даты
         * @return номер дня месяца, целое число >0
         */
        public int getDAY(){return DAY_OF_MONTH;}

        /**
         * Получить месяц из даты
         * @return месяц, элемент перечисления MONTH
         */
        public MONTH getMONTH(){return MONTH_OF_YEAR;}

        /**
         * Получить месяц из даты
         * @return номер месяца, целое число >0
         */
        public int getMONTH_NUMBER(){return MONTH_OF_YEAR.ordinal()+1;}

        /**
         * Получить год из даты
         * @return год, целое число >0
         */
        public int getYEAR(){return YEAR;}

        /**
         * Изменить день в дате
         * @param DD номер дня в месяце (>0)
         */
        public void changeDAY(int DD){
            if ((DD<1)||(DD>MONTH_OF_YEAR.getDAYS_IN_MONTH())) throw new IllegalArgumentException("В этом месяце нет такого дня");
            this.DAY_OF_MONTH = DD;
        }

        /**
         * Изменить месяц в дате
         * @param MM номер месяца в году [1;12]
         */
        public void changeMonths(int MM){
            if ((MM<1)||(MM>12)) throw new IllegalArgumentException("В Григорианском календаре только 12 месяцев");
            MONTH[] m = MONTH.values();
            this.MONTH_OF_YEAR = m[MM-1];
        }

        /**
         * Изменить месяц в дате
         * @param MM месяц из перечисления MONTH
         */
        public void changeMonths(MONTH MM){
            if(MM == null) throw new IllegalArgumentException("Передан null в качестве месяца");
            this.MONTH_OF_YEAR = MM;
        }

        /**
         * Изменить год в дате
         * @param YYYY год (>0)
         */
        public void changeYear(int YYYY){
            if ((YYYY<1)) throw new IllegalArgumentException("Год до нашей эры");
            this.YEAR = YYYY;
        }

        @Override
        public String toString() {
            String s = switch (DAY_OF_MONTH){
                case 1 -> "st";
                case 2 -> "nd";
                case 3 -> "rd";
                default -> "th";
            };
            return DAY_OF_MONTH + s +" of " + MONTH_OF_YEAR +", " + YEAR + " year";
        }
    }

    /**
     * Перечисление месяцев
     */
    public enum MONTH{
        JANUARY(31),FEBRUARY(28),MARCH(31),APRIL(30),MAY(31),JUNE(30),
        JULY(31),AUGUST(31),SEPTEMBER(30),OCTOBER(31),NOVEMBER(30),DECEMBER(31);

        int DAYS_IN_MONTH;
        MONTH (int D){
            DAYS_IN_MONTH = D;
        }

        /**
         * Получить количество дней в месяце (в Феврале будет возвращаться 28)
         * @return количество дней в месяце
         */
        int getDAYS_IN_MONTH(){
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
    private DATE currentDate;

    /**
     * Конструктор календаря
     * @param date дата, котрую календарь будет хранить и считать текущей датой
     */
    public CalendarGregorian(DATE date){
        if (date == null) throw new IllegalArgumentException("Аргумент date дожен быть != null");
        this.dayOfEra = toDays(date.getDAY(),date.getMONTH_NUMBER(),date.getYEAR());
        this.currentDate = new DATE(date.getDAY(),date.getMONTH(),date.getYEAR());
    }

    /**
     * Получить день из даты, записанной в каленаре
     * @return текущий день из даты
     */
    public int getDayOfMonth(){
        return currentDate.getDAY();
    }

    /**
     * Получить месяц из даты, записанной в каленаре
     * @return текущий месяц из даты, представленный в типе перечисления MONTH,
     *         чтобы получить номер этого месяца, используйте getMONTH_NUMBER на полученном месяце
     */
    public MONTH getMonth(){
        return currentDate.getMONTH();
    }

    /**
     * Получить год из даты, записанной в каленаре
     * @return текущий год из даты
     */
    public int getYear(){
        return currentDate.getYEAR();
    }

    /**
     * Получить текущую дату, записанную в календаре
     * @return !копия! текущей даты, записанной в календаре, типа DATE
     */
    public DATE getDate(){
        return new DATE(currentDate.getDAY(),currentDate.getMONTH(),currentDate.getYEAR());
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
        MONTH[] m = MONTH.values();
        if(currentDate.getMONTH_NUMBER()!=12){
            this.currentDate.changeMonths(currentDate.getMONTH_NUMBER()+1);
            this.dayOfEra = toDays(currentDate.getDAY(),currentDate.getMONTH_NUMBER(),currentDate.getYEAR());
        }
        else {
            this.currentDate.changeMonths(1);
            this.currentDate.changeYear(currentDate.YEAR+1);
            this.dayOfEra = toDays(currentDate.getDAY(),currentDate.getMONTH_NUMBER(),currentDate.getYEAR());
        }
    }

    /**
     * добавить 1 год к дате
     */
    public void addYear(){
        this.currentDate.changeYear(currentDate.getYEAR()+1);
        this.dayOfEra = toDays(currentDate.getDAY(),currentDate.getMONTH_NUMBER(),currentDate.getYEAR());
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
        MONTH[] m = MONTH.values();
        if(currentDate.getMONTH_NUMBER()!=1){
            this.currentDate.changeMonths(currentDate.getMONTH_NUMBER()-1);
            this.dayOfEra = toDays(currentDate.getDAY(),currentDate.getMONTH_NUMBER(),currentDate.getYEAR());
        }
        else {
            this.currentDate.changeMonths(12);
            this.currentDate.changeYear(currentDate.YEAR-1);
            this.dayOfEra = toDays(currentDate.getDAY(),currentDate.getMONTH_NUMBER(),currentDate.getYEAR());
        }
    }

    /**
     * вычесть один год из даты
     */
    public void subYear(){
        this.currentDate.changeYear(currentDate.getYEAR()-1);
        dayOfEra = toDays(currentDate.getDAY(),currentDate.getMONTH_NUMBER(),currentDate.getYEAR());
    }


    /**
     * количество дней до или после даты x
     * @param x дата, с которой будет сравниваться текущая дата
     * @return разница в днях между датами
     */
    public int difDate(DATE x){
        if(x == null) throw new IllegalArgumentException("Дата x должна быть != null");
        int dayX = toDays(x.getDAY(), x.getMONTH_NUMBER(), x.getYEAR());
        if (dayX<dayOfEra) return dayOfEra-dayX;
        else return dayX-dayOfEra;
    }

    /**
     * аналогично difDate вычисляет разницу между датами в формате с днями, месяцами и годами
     * @param x дата, с которой будет сравниваться текущая дата
     * @return разница между датами
     */
    public DATE difDateInDDMMYYYYformat(DATE x){
        if(x == null) throw new IllegalArgumentException("Дата x должна быть != null");
        int dayX = toDays(x.getDAY(), x.getMONTH_NUMBER(), x.getYEAR());
        int dayY;
        if (dayX < dayOfEra) dayY = dayOfEra;
        else {
            dayY = dayX;
            dayX = dayOfEra;
        }
        int cntDays = dayY-dayX;
        return toDate(cntDays);
    }

    private int toDays(int day_of_month, int month, int year){
        int tmp = year;
        int daysCnt = 0;
        year--;
        daysCnt+= 365 * year;
        daysCnt+=97 * (year/400);
        year %=400;
        daysCnt+=year/100 * 24;
        year %=100;
        daysCnt+=year/4;
        //if(isLEAP(year+1))

        MONTH[] months = MONTH.values();
        for(int i = 0; i<month-1; i++) {
            if (i != 1) {
                daysCnt += months[i].getDAYS_IN_MONTH();
            }
            else if (!isLEAP(tmp)){
                daysCnt += months[i].getDAYS_IN_MONTH();
            }
            else {
                daysCnt += months[i].getDAYS_IN_MONTH()+1;
            }
        }
        daysCnt+=day_of_month-1;
        return daysCnt;
    }

    private DATE toDate(int daysCnt){
        int year = 1;
        int month = 1;
        int day = 1;
        while (daysCnt>365){
            daysCnt -= 365;
            if(isLEAP(year++)) daysCnt--;
        }
        if ((daysCnt==356)&&(!isLEAP(year))){
            daysCnt -= 365;
            year++;
        }
        MONTH[] M = MONTH.values();
        for(int i = 0; i<12; i++){
            if(i!=1) {
                if (daysCnt > M[i].getDAYS_IN_MONTH()) {
                    daysCnt -= M[i].getDAYS_IN_MONTH();
                    month++;
                } else {
                    break;
                }
            }
            else{
                if(isLEAP(year)){
                    if (daysCnt > M[i].getDAYS_IN_MONTH()+1) {
                        daysCnt -= M[i].getDAYS_IN_MONTH();
                        month++;
                    } else {
                        break;
                    }
                }
                else {
                    if (daysCnt > M[i].getDAYS_IN_MONTH()) {
                        daysCnt -= M[i].getDAYS_IN_MONTH();
                        month++;
                    } else {
                        break;
                    }
                }
            }
        }
        day+=daysCnt;
        return new DATE(day,month,year);
    }


    private static boolean isLEAP(int year){
        return (year % 400 == 0) || ((year % 100 != 0) && (year % 4 == 0));
    }
}
