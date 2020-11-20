import java.util.ArrayList;

public class RecordBook {
    public static class Rec {
        String disciplineName;
        int grade;
        Rec(String name, int grade){
            disciplineName = name;
            this.grade = grade;
        }
        int getGrade(){
            return grade;
        }

    }


    int currentSemester;  // number of current semester
    int gradeForQualifyingWork = 0;
    ArrayList<ArrayList<Rec>> book;

    /**
     * Конструктор создания пустой зачётной книжки
     * @param NumberOfCurrentSemester номер текущего семестра
     */
    RecordBook(int NumberOfCurrentSemester){
        currentSemester = NumberOfCurrentSemester;
        book = new ArrayList<ArrayList<Rec>>(NumberOfCurrentSemester);
        for (int i = 0; i<NumberOfCurrentSemester-1; i++){
            book.add(null);                   // needed for correct work of replaceSemester method
        }
    }

    /**
     * Конструктор создания зачётной книжки по списку семестров
     * @param NumberOfCurrentSemester номер текущего семестра (>=1)
     * @param book список семестров
     * @throws IllegalArgumentException в случае, если передан некорректный номер текщего семестра
     */
    RecordBook(int NumberOfCurrentSemester, ArrayList<ArrayList<Rec>> book) throws IllegalArgumentException{
        if (NumberOfCurrentSemester < 1) throw new IllegalArgumentException("Номер семестра должен быть >= 0");
        currentSemester = NumberOfCurrentSemester;
        this.book = book;
    }

    /**
     * Конструктор создания зачётной книжки по списку семестров вместе с оценкой за дипломную работу
     * @param NumberOfCurrentSemester номер текущего семестра (>=1)
     * @param book список семестров
     * @param qualifyingWork оценка за дипломную работу
     * @throws IllegalArgumentException в случае, если передан некорректный номер текщего семестра
     */
    RecordBook(int NumberOfCurrentSemester, ArrayList<ArrayList<Rec>> book, int qualifyingWork) throws IllegalArgumentException{
        if (NumberOfCurrentSemester < 1) throw new IllegalArgumentException("Номер семестра должен быть >= 0");
        currentSemester = NumberOfCurrentSemester;
        gradeForQualifyingWork = qualifyingWork;
        this.book = book;
    }


    /**
     * Установка оценки за дипломную работу
     * @param grade оценка за дипломную работу [2;5]
     * @throws IllegalArgumentException в случае, если передана некорректная оценка
     */
    void setGradeForQualifyingWork (int grade) throws IllegalArgumentException{
        if(grade<2 || grade>5) throw new IllegalArgumentException("Оценка дожна находиться в пределах от 2 до 5");
        gradeForQualifyingWork = grade;
    }


    /**
     * заменить семестр на переданный
     * @param semesterNumber номер заменяемого семестра
     * @param page семестр, состоящий из Rec записей об оценках
     */
    void replaceSemester(int semesterNumber, ArrayList<Rec> page){
        book.set(semesterNumber-1,page);
    }

    /**
     * Добавить к существующему семестру запсиь
     * @param numSemester номер изменяемого семестра
     * @param record запись
     * @throws IllegalArgumentException если передан некорректный номер изменяемого семестра
     */
    void addRecordToSemester(int numSemester, Rec record) throws IllegalArgumentException, IllegalStateException{
        if (numSemester<1) throw new IllegalArgumentException("Номер семестра должен быть >= 0");
        book.get(numSemester-1).add(record);
    }

    /**
     * текущий средний балл за все время обучения
     * @return средний балл
     * @throws IllegalStateException если не хватает семестровых страниц
     */
    double avg  () throws IllegalStateException {
        int sum = 0;
        int cnt = 0;
        for(int i = 0; i<currentSemester-1; i++){
            if (book.get(i)==null) throw new IllegalStateException("В зачётной книжке не хватает страницы за "+ (i+1) +" семестр");
            cnt += book.get(i).size();
            sum += book.get(i).stream().mapToInt(Rec::getGrade).sum();
        }
        return 1.0 * sum/cnt;
    }

    /**
     * может ли студент получить «красный» диплом с отличием
     * @return true - если студент может получить «красный» диплом с отличием, false - иначе
     * @throws IllegalStateException если не хватает семестровых страниц
     */
    boolean isRedDiplomaPossible () throws IllegalStateException{
        if ((gradeForQualifyingWork!=0)&&(gradeForQualifyingWork!=5)) return false;
        ArrayList<Rec> lastGrades = new ArrayList<Rec>();
        for(ArrayList<Rec> sem : book){
            if (sem==null) throw new IllegalStateException("В зачётной книжке не хватает страницы за семестр");
            for(Rec r : sem){
                if (r.getGrade()<=3) return false;
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
            sum += r.getGrade();
        }
        return ((1.0 * sum/cnt) >= 4.75);
    }

    /**
     * будет ли повышенная стипендия в этом семестре
     * @return true - если буде повышенная стипендия, false - иначе
     * @throws IllegalStateException если не хватает семестровых страниц
     */
    boolean increasedScholarship() throws IllegalStateException{
        ArrayList<Rec> sem;
        if ((currentSemester==1)||(sem = book.get(currentSemester-2)) == null) throw new IllegalStateException("В зачётной книжке нет предыдущего семестра");
        boolean flag = true;
        for (Rec r : sem){
            if (r.getGrade() != 5) {
                flag = false;
                break;
            }
        }
        return flag;
    }
}
