package ntukhpi.semit.dde.studentsdata.files;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import ntukhpi.semit.dde.studentsdata.entity.Student;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

public class JSONUtilities {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    public static Set<Student> readFromJSON(String filePath) throws IOException {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .setPrettyPrinting()
                .create();

        try (Reader reader = new FileReader(filePath)) {
            Student[] students = gson.fromJson(reader, Student[].class);
            return Set.of(students);
        } catch (JsonSyntaxException e) {
            throw new IOException("Invalid JSON syntax", e);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String writeToJSON(String filePath, List<Student> studentList) throws IOException {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .setPrettyPrinting()
                .create();

        try (Writer writer = new FileWriter("results/"+filePath + ".json")) {
            gson.toJson(studentList, writer);
        } catch (JsonIOException e) {
            throw new IOException("Error writing to JSON file", e);
        }
        return filePath + ".json";
    }

    private static class LocalDateAdapter extends TypeAdapter<LocalDate> {
        @Override
        public void write(JsonWriter out, LocalDate value) throws IOException {
            if (value == null) {
                out.nullValue();
                return;
            }
            out.value(value.format(DATE_FORMATTER));
        }

        @Override
        public LocalDate read(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return null;
            }
            String str = in.nextString();
            return LocalDate.parse(str, DATE_FORMATTER);
        }
    }
}
