import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.io.*;

public class Main {
    public static void main(String[] args) {
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileName = "data.csv";

        List<Employee> list = parseCSV(columnMapping, fileName);
        String json = listToJson(list);
        writeString(json, "data.json");

        List<Employee> list2 = parseXML("data.xml");
        String json2 = listToJson(list2);
        writeString(json2, "data2.json");

    }
    public static List<Employee> parseCSV (String[] columnMapping, String fileName){
        try (CSVReader csvReader = new CSVReader(new FileReader(fileName))){
            ColumnPositionMappingStrategy<Employee> strategy = new ColumnPositionMappingStrategy<>();

            strategy.setType(Employee.class);
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

    public static void writeString(String json, String name){
        try(FileWriter file = new FileWriter(name)){
            file.write(json);
            file.flush();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public static List<Employee> parseXML(String data) {
      List<Employee> list = new ArrayList<>();

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File(data));

            Node root = doc.getDocumentElement();
            NodeList nodeList = root.getChildNodes();

            for(int i = 0; i < nodeList.getLength(); i++){
                Node node_ = nodeList.item(i);
                if(Node.ELEMENT_NODE == node_.getNodeType()){
                Element element = (Element) node_;
                Employee employee = new Employee();
                String el = element.getTextContent().trim();
                String[] info = el.split("\n        ");

                employee.id = Integer.parseInt(info[0]);
                employee.firstName = info[1];
                employee.lastName = info[2];
                employee.country = info[3];
                employee.age = Integer.parseInt(info[4]);

                list.add(employee);
                }
            }
        } catch (ParserConfigurationException | IOException | SAXException e) {
                    e.printStackTrace();
                }
        return list;
   }
}
