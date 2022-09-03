import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.bean.*;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileName = "C:\\Users\\user\\IdeaProjects\\JavaCore1.6\\CSVconverter\\data.csv";
        List<Employee> list = parseCSV(columnMapping, fileName);
        String json = listToJson(list);
        writeString(json);
    }

    private static void writeString(String json) {
        File data = new File("C:\\Users\\user\\IdeaProjects\\JavaCore1.6\\CSVConverter\\data.json");
        try {
            if (data.createNewFile()) {
                FileWriter writer = new FileWriter(data);
                writer.write(json);
                writer.flush();
                writer.close();
            }
        } catch(IOException ex){
                System.out.println(ex.getMessage());
        }
    }


    private static String listToJson(List<Employee> list) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.setPrettyPrinting().create();
        Type listType = new TypeToken<List<Employee>>() {}.getType();
        return gson.toJson(list, listType);
    }

    public static List<Employee> parseCSV (String[] columnMapping, String fileName) {
        try (CSVReader data = new CSVReader(new FileReader(fileName))){
            ColumnPositionMappingStrategy<Employee> ColPosStr = new ColumnPositionMappingStrategy<>();
            ColPosStr.setType(Employee.class);
            ColPosStr.setColumnMapping(columnMapping);
            CsvToBean<Employee> toBean = new CsvToBeanBuilder<Employee>(data)
                    .withMappingStrategy(ColPosStr)
                    .build();
            return toBean.parse();
        }
         catch (Exception E) {
            E.printStackTrace();
        }
        return null;
    }
}
