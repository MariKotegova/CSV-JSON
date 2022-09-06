import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import java.lang.reflect.Type;
import java.util.List;
import java.io.*;

public class Main {
    public static void main(String[] args){
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileName = "data.csv";
        List<Employee> list = parseCSV(columnMapping, fileName);

        String json = listToJson(list);

        writeString(json);


    }
    public static List<Employee> parseCSV (String[] columnMapping, String fileName){
        try (CSVReader csvReader = new CSVReader(new FileReader(fileName))){
            ColumnPositionMappingStrategy<Employee> strategy = new ColumnPositionMappingStrategy<>();

            strategy.setType(Employee.class);
            //strategy.setColumnMapping("id", "firstName", "lastName", "country", "age");
            strategy.setColumnMapping(columnMapping);

            CsvToBean<Employee> csv = new CsvToBeanBuilder<Employee>(csvReader)
                    .withMappingStrategy(strategy)
                    .build();

            List<Employee> staff = csv.parse();
            return staff;
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    public static String listToJson(List<Employee> list){
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        Type listType = new TypeToken<List<Employee>>(){}.getType();
        String json = gson.toJson(list, listType);
        return json;
    }

    public static void writeString(String json){
        try(FileWriter file = new FileWriter("data.json")){
            file.write(json);
            file.flush();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
