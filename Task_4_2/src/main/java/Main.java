import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        NoteBook notebook = NoteBook.loadNoteBook("notes.txt");
        Scanner lineScanner = new Scanner(System.in);
        while (lineScanner.hasNextLine()){
            Scanner tokenScanner = new Scanner(lineScanner.nextLine());
            String rx = "[^\"\\s]+|\"(\\\\.|[^\\\\\"])*\"";
            if (!tokenScanner.hasNext(rx)) {
                System.out.println("Usage: notebook -oper arg1 arg2 ...");
                continue;
            }
            String tokenNotebook = tokenScanner.findInLine(rx);
            if (!tokenNotebook.equals("notebook")) throw new UnsupportedOperationException("Write \"notebook\" as program");
            if (!tokenScanner.hasNext(rx)) throw new UnsupportedOperationException("Write operation like -add or -show and its arguments");
            String tokenOperation = tokenScanner.findInLine(rx);
            switch (tokenOperation) {
                case "-add" -> {
                    if (!tokenScanner.hasNext(rx)) throw new UnsupportedOperationException("-add operation needs 2 String arguments: Title, Text");
                    String title = tokenScanner.findInLine(rx);
                    if (!tokenScanner.hasNext(rx)) throw new UnsupportedOperationException("-add operation needs 2 String arguments: Title, Text");
                    String text = tokenScanner.findInLine(rx);
                    if (tokenScanner.hasNext(rx)) throw new UnsupportedOperationException("Too many arguments for -add operation");
                    notebook.addNote(title,text);
                    notebook.save("notes.txt");
                }
                case "-rm" -> {
                    if (!tokenScanner.hasNext(rx)) throw new UnsupportedOperationException("-add operation needs 1 String argument: Title of note, that should be removed");
                    String title = tokenScanner.findInLine(rx);
                    if (tokenScanner.hasNext(rx)) throw new UnsupportedOperationException("Too many arguments for -rm operation");
                    notebook.removeNote(title);
                    notebook.save("notes.txt");
                }
                case "-show" -> {
                    if (!tokenScanner.hasNext(rx)){
                        NoteBook.Note[] notes = notebook.showAll();
                        for (NoteBook.Note n : notes){
                            System.out.println(n);
                        }
                    }
                    else {
                        String fromStr = tokenScanner.findInLine(rx);
                        if (!tokenScanner.hasNext(rx)) throw new UnsupportedOperationException("-show operation needs 2 or more String arguments: fromTime, toTime, keyWord1, ...");
                        String toStr = tokenScanner.findInLine(rx);
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
                        LocalDateTime from, to;
                        try{
                            from = LocalDateTime.parse(fromStr,formatter);
                            to = LocalDateTime.parse(toStr,formatter);
                        } catch (DateTimeParseException e) {
                            System.out.println("Couldn`t parse date");
                            continue;
                        }
                        if (!tokenScanner.hasNext(rx)){
                            NoteBook.Note[] notes = notebook.showPeriod(from, to);
                            for (NoteBook.Note n : notes){
                                System.out.println(n);
                            }
                        }
                        else {
                            ArrayList<String> keyWords = new ArrayList<String>();
                            while (tokenScanner.hasNext(rx)){
                                String kw = tokenScanner.findInLine(rx);
                                keyWords.add(kw);
                            }
                            NoteBook.Note[] notes = notebook.showPeriodWithKV(from, to, keyWords);
                            for (NoteBook.Note n : notes){
                                System.out.println(n);
                            }
                        }

                    }
                }
                default -> throw new UnsupportedOperationException("Write operation like -add or -show and its arguments");
            }
        }
    }
}
