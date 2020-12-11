import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.stream.Stream;


public class NoteBook {
    static class Note{
        private final LocalDateTime dateTime;
        private final String title;
        private final String text;

        Note (String title, String text){
            if (title == null || text == null) throw new IllegalArgumentException("Null as argument");
            this.dateTime = LocalDateTime.now();
            this.title = title;
            this.text = text;
        }

        LocalDateTime getDateTime(){
            return dateTime;
        }
        String getTitle (){
            return title;
        }
        String getText(){
            return text;
        }

        @Override
        public String toString() {
            return "Заметка: " + dateTime.getDayOfMonth()+'.'+dateTime.getMonthValue() + '.' + dateTime.getYear() +
                    ' ' + dateTime.getHour() + ':' + dateTime.getMinute() +
                    " '" + title + '\'' +
                    " '" + text + '\'';
        }
    }

    ArrayList<Note> book;
    //int cnt = 0;

    NoteBook (){
        book = new ArrayList<Note>();
    }

    void addNote (String title, String text){
        if (title == null || text == null) throw new IllegalArgumentException("Null as argument");
        book.add(new Note(title,text));
        //cnt++;
    }

    void removeNote (String title){
        book.removeIf(x -> x.title.equals(title));
    }

    Note[] showAll (){
        return book.toArray(new Note[0]);
    }

    Note[] showPeriod (LocalDateTime timeFrom, LocalDateTime timeTo){
        //Stream<Note> tmp = book.stream();
        ArrayList<Note> res = new ArrayList<Note>(book);
        res.removeIf(x -> x.dateTime.compareTo(timeFrom)<0 || x.dateTime.compareTo(timeTo)>0);
        return res.toArray(new Note[0]);
    }

    Note[] showPeriodWithKV (LocalDateTime timeFrom, LocalDateTime timeTo, ArrayList<String> kwList){
        ArrayList<Note> res = new ArrayList<Note>(book);
        res.removeIf(x -> x.dateTime.compareTo(timeFrom)<0 || x.dateTime.compareTo(timeTo)>0);
        for (String kw : kwList){
            res.removeIf(x -> !x.getTitle().contains(kw));
        }
        return res.toArray(new Note[0]);
    }

    public void save (String fileName) {
        Gson gson = new Gson();
        //Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(this);
        try(FileWriter writer = new FileWriter(fileName, false)) {
            writer.write(json);
        } catch(IOException ex){
            System.out.println(ex.getMessage());
        }
    }

    static NoteBook loadNoteBook (String fileName) throws IOException{
        try {
            String json = new String(Files.readAllBytes(Paths.get(fileName)));
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            NoteBook res = gson.fromJson(json,NoteBook.class);
            if (res != null){
                return res;
            }
            return new NoteBook();
        }
        catch (IOException e) {
            System.out.println("Couldn`t read file" + fileName);
            return new NoteBook();
        }
    }
}
