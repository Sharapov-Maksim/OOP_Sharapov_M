import java.util.ArrayList;

public class RecordBook {
    public static class Rec {
        private final String disciplineName;
        private Grade grade;

        /**
         * Конструктор класса Rec
         * @param name Название предмета
         * @param grade Оценка по предмету. Допустимые значения: TWO, THREE, FOUR, FIVE
         */
        public Rec(String name, Grade grade){
            if (grade == null) throw new IllegalArgumentException("оценка grade должна быть != null");
            if (grade == Grade.NONE) throw new IllegalArgumentException("оценка grade должна быть от 2 до 5");
            disciplineName = name;
            this.grade = grade;
        }

        /**
         * Возвращает оценку типа Grade
         */
        public Grade getGrade(){
            return grade;
        }

    }

    public enum Grade{
        NONE(0,"0","ноль"),
        TWO(2,"2", "неудовлетворительно"),
        THREE(3, "3", "удовлетворительно"),
        FOUR(4, "4", "хорошо"),
        FIVE(5, "5", "отлично");
        private final int intVal;
        private final String strVal;
        private final String word;

        Grade(int v, String sv, String w){
            intVal = v;
            strVal = sv;
            word = w;
        }

        /**
         * Получить целочисленное значение, соответствующее оценке
         */
        public int getVal(){
            return intVal;
        }

        @Override
        public String toString() {
            return '\"' + word + '\"';
        }
    }

    private final int currentSemester;  // number of current semester
    private Grade gradeForQualifyingWork = Grade.NONE;
    private final ArrayList<ArrayList<Rec>> book;

    /**
     * Конструктор создания пустой зачётной книжки
     * @param NumberOfCurrentSemester номер текущего семестра (>=1)
     */
    public RecordBook(int NumberOfCurrentSemester){
        if (NumberOfCurrentSemester < 1) throw new IllegalArgumentException("Номер семестра должен быть больше 0");
        currentSemester = NumberOfCurrentSemester;
        book = new ArrayList<ArrayList<Rec>>(NumberOfCurrentSemester);
        for (int i = 0; i<NumberOfCurrentSemester-1; i++){
            book.add(new ArrayList<Rec>());                   // needed for correct work of replaceSemester method
        }
    }

    /**
     * Конструктор создания зачётной книжки по списку семестров
     * @param NumberOfCurrentSemester номер текущего семестра (>=1)
     * @param book список семестров (!= null)
     */
    public RecordBook(int NumberOfCurrentSemester, ArrayList<ArrayList<Rec>> book){
        if (NumberOfCurrentSemester < 1) throw new IllegalArgumentException("Номер семестра должен быть больше 0");
        if (book==null) throw new IllegalArgumentException("book должен быть != null");
        this.currentSemester = NumberOfCurrentSemester;
        this.book = book;
    }

    /**
     * Конструктор создания зачётной книжки по списку семестров вместе с оценкой за дипломную работу
     * @param NumberOfCurrentSemester номер текущего семестра (>=1)
     * @param book список семестров
     * @param qualifyingWork оценка за дипломную работу
     */
    public RecordBook(int NumberOfCurrentSemester, ArrayList<ArrayList<Rec>> book, Grade qualifyingWork){
        if (NumberOfCurrentSemester < 1) throw new IllegalArgumentException("Номер семестра должен быть больше 0");
        if (book==null) throw new IllegalArgumentException("book должен быть != null");
        if (qualifyingWork == null) throw new IllegalArgumentException("оценка qualifyingWork должна быть != null");
        this.currentSemester = NumberOfCurrentSemester;
        this.gradeForQualifyingWork = qualifyingWork;
        this.book = book;
    }


    /**
     * Установка оценки за дипломную работу
     * @param grade оценка за дипломную работу [2;5]
     */
    public void setGradeForQualifyingWork (Grade grade){
        if(grade==Grade.NONE) throw new IllegalArgumentException("Оценка дожна находиться в пределах от 2 до 5");
        gradeForQualifyingWork = grade;
    }


    /**
     * заменить семестр на переданный
     * @param semesterNumber номер заменяемого семестра (>=1)
     * @param page семестр, состоящий из Rec записей об оценках (!=null)
     */
    public void replaceSemester(int semesterNumber, ArrayList<Rec> page){
        if (semesterNumber<1) throw new IllegalArgumentException("Номер семестра должен быть больше нуля");
        if (page==null) throw new IllegalArgumentException("аргумент page должен быть != null");
        book.set(semesterNumber-1,page);
    }

    /**
     * Добавить к существующему семестру запсиь
     * @param numSemester номер изменяемого семестра (>=1)
     * @param record запись (!=null)
     */
    public void addRecordToSemester(int numSemester, Rec record) {
        if (numSemester<1) throw new IllegalArgumentException("Номер семестра должен быть больше 0");
        if (record == null) throw new IllegalArgumentException("аргумент record должен быть != null");
        book.get(numSemester-1).add(record);
    }

    /**
     * текущий средний балл за все время обучения
     * @return средний балл
     */
    public double avg  () {
        int sum = 0;
        int cnt = 0;
        for(int i = 0; i<currentSemester-1; i++){
            if (book.get(i)==null) throw new IllegalStateException("В зачётной книжке не хватает страницы за "+ (i+1) +" семестр");
            cnt += book.get(i).size();
            sum += book.get(i).stream().mapToInt(x -> x.getGrade().getVal()).sum();
        }
        if(cnt==0) throw new IllegalStateException("В зачётной книжке нет ни одной записи");
        return 1.0 * sum/cnt;
    }

    /**
     * может ли студент получить «красный» диплом с отличием
     * @return true - если студент может получить «красный» диплом с отличием, false - иначе
     */
    public boolean isRedDiplomaPossible () {
        if ((gradeForQualifyingWork!=Grade.NONE)&&(gradeForQualifyingWork!=Grade.FIVE)) return false;
        ArrayList<Rec> lastGrades = new ArrayList<Rec>();
        for(ArrayList<Rec> sem : book){
            if (sem==null) throw new IllegalStateException("В зачётной книжке не хватает страницы за семестр");
            for(Rec r : sem){
                if (r.getGrade().getVal()<=3) return false;
                boolean fl = true;
                for(Rec last : lastGrades){
                    if (last.disciplineName.equals(r.disciplineName)){
                        last.grade = r.getGrade();
                        fl = false;
                        break;
                    }
                }
                if (fl) lastGrades.add(new Rec(r.disciplineName, r.getGrade()));
            }
        }
        double cnt = 0, sum = 0;
        for(Rec r : lastGrades){
            cnt++;
            sum += r.getGrade().getVal();
        }
        if(cnt==0) throw new IllegalStateException("В зачётной книжке нет ни одной записи");
        return ((1.0 * sum/cnt) >= 4.75);
    }

    /**
     * будет ли повышенная стипендия в этом семестре
     * @return true - если буде повышенная стипендия, false - иначе
     */
    public boolean increasedScholarship() {
        ArrayList<Rec> sem = book.get(currentSemester-2);
        if ((currentSemester==1)|| sem == null) throw new IllegalStateException("В зачётной книжке нет предыдущего семестра");
        if (sem.size()==0) throw new IllegalStateException("В зачётной книжке нет записей предыдущего семестра");
        boolean flag = true;
        for (Rec r : sem){
            if (r.getGrade() != Grade.FIVE) {
                flag = false;
                break;
            }
        }
        return flag;
    }
}
