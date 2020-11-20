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
     * ����������� �������� ������ �������� ������
     * @param NumberOfCurrentSemester ����� �������� ��������
     */
    RecordBook(int NumberOfCurrentSemester){
        currentSemester = NumberOfCurrentSemester;
        book = new ArrayList<ArrayList<Rec>>(NumberOfCurrentSemester);
        for (int i = 0; i<NumberOfCurrentSemester-1; i++){
            book.add(null);                   // needed for correct work of replaceSemester method
        }
    }

    /**
     * ����������� �������� �������� ������ �� ������ ���������
     * @param NumberOfCurrentSemester ����� �������� �������� (>=1)
     * @param book ������ ���������
     * @throws IllegalArgumentException � ������, ���� ������� ������������ ����� ������� ��������
     */
    RecordBook(int NumberOfCurrentSemester, ArrayList<ArrayList<Rec>> book) throws IllegalArgumentException{
        if (NumberOfCurrentSemester < 1) throw new IllegalArgumentException("����� �������� ������ ���� >= 0");
        currentSemester = NumberOfCurrentSemester;
        this.book = book;
    }

    /**
     * ����������� �������� �������� ������ �� ������ ��������� ������ � ������� �� ��������� ������
     * @param NumberOfCurrentSemester ����� �������� �������� (>=1)
     * @param book ������ ���������
     * @param qualifyingWork ������ �� ��������� ������
     * @throws IllegalArgumentException � ������, ���� ������� ������������ ����� ������� ��������
     */
    RecordBook(int NumberOfCurrentSemester, ArrayList<ArrayList<Rec>> book, int qualifyingWork) throws IllegalArgumentException{
        if (NumberOfCurrentSemester < 1) throw new IllegalArgumentException("����� �������� ������ ���� >= 0");
        currentSemester = NumberOfCurrentSemester;
        gradeForQualifyingWork = qualifyingWork;
        this.book = book;
    }


    /**
     * ��������� ������ �� ��������� ������
     * @param grade ������ �� ��������� ������ [2;5]
     * @throws IllegalArgumentException � ������, ���� �������� ������������ ������
     */
    void setGradeForQualifyingWork (int grade) throws IllegalArgumentException{
        if(grade<2 || grade>5) throw new IllegalArgumentException("������ ����� ���������� � �������� �� 2 �� 5");
        gradeForQualifyingWork = grade;
    }


    /**
     * �������� ������� �� ����������
     * @param semesterNumber ����� ����������� ��������
     * @param page �������, ��������� �� Rec ������� �� �������
     */
    void replaceSemester(int semesterNumber, ArrayList<Rec> page){
        book.set(semesterNumber-1,page);
    }

    /**
     * �������� � ������������� �������� ������
     * @param numSemester ����� ����������� ��������
     * @param record ������
     * @throws IllegalArgumentException ���� ������� ������������ ����� ����������� ��������
     */
    void addRecordToSemester(int numSemester, Rec record) throws IllegalArgumentException, IllegalStateException{
        if (numSemester<1) throw new IllegalArgumentException("����� �������� ������ ���� >= 0");
        book.get(numSemester-1).add(record);
    }

    /**
     * ������� ������� ���� �� ��� ����� ��������
     * @return ������� ����
     * @throws IllegalStateException ���� �� ������� ����������� �������
     */
    double avg  () throws IllegalStateException {
        int sum = 0;
        int cnt = 0;
        for(int i = 0; i<currentSemester-1; i++){
            if (book.get(i)==null) throw new IllegalStateException("� �������� ������ �� ������� �������� �� "+ (i+1) +" �������");
            cnt += book.get(i).size();
            sum += book.get(i).stream().mapToInt(Rec::getGrade).sum();
        }
        return 1.0 * sum/cnt;
    }

    /**
     * ����� �� ������� �������� �������� ������ � ��������
     * @return true - ���� ������� ����� �������� �������� ������ � ��������, false - �����
     * @throws IllegalStateException ���� �� ������� ����������� �������
     */
    boolean isRedDiplomaPossible () throws IllegalStateException{
        if ((gradeForQualifyingWork!=0)&&(gradeForQualifyingWork!=5)) return false;
        ArrayList<Rec> lastGrades = new ArrayList<Rec>();
        for(ArrayList<Rec> sem : book){
            if (sem==null) throw new IllegalStateException("� �������� ������ �� ������� �������� �� �������");
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
     * ����� �� ���������� ��������� � ���� ��������
     * @return true - ���� ���� ���������� ���������, false - �����
     * @throws IllegalStateException ���� �� ������� ����������� �������
     */
    boolean increasedScholarship() throws IllegalStateException{
        ArrayList<Rec> sem;
        if ((currentSemester==1)||(sem = book.get(currentSemester-2)) == null) throw new IllegalStateException("� �������� ������ ��� ����������� ��������");
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
