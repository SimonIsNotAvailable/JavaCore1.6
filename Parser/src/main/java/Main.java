import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.bean.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;
import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        //CSV to JSON parser
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileName = "C:\\Users\\user\\IdeaProjects\\JavaCore1.6\\Parser\\data.csv";
        List<Employee> list = parseCSV(columnMapping, fileName);
        String json = listToJson(list);
        String newName = "data.json";
        writeString(json, newName);

        //XML to JSON parser
        String fileNameXML = "C:\\Users\\user\\IdeaProjects\\JavaCore1.6\\Parser\\data.xml";
        try {
            List<Employee> listXML = parseXML(fileNameXML);
            String newName1 = "data1.json";
            writeString(listToJson(listXML), newName1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static List<Employee> parseXML(String filename) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new File(filename));
        NodeList root = doc.getDocumentElement().getElementsByTagName("employee");
        return read(root);
    }

    private static long id;
    private static String fn;
    private static String ln;
    private static String cntr;
    private static int age;

    private static List<Employee> read(NodeList nodeList) {
        List<Employee> employeeList = new ArrayList<>();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            NodeList newNodeList = node.getChildNodes();
            for (int j = 0; j < newNodeList.getLength(); j++) {
                switch (newNodeList.item(j).getNodeName()) {
                    case "id":
                        id = Long.parseLong(newNodeList.item(j).getTextContent());
                        break;
                    case "firstName":
                        fn = newNodeList.item(j).getTextContent();
                        break;
                    case "lastName":
                        ln = newNodeList.item(j).getTextContent();
                        break;
                    case "country":
                        cntr = newNodeList.item(j).getTextContent();
                        break;
                    case "age":
                        age = Integer.parseInt(newNodeList.item(j).getTextContent());
                        break;
                }
            }
            try {
                employeeList.add(new Employee(id, fn, ln, cntr, age));
            }
                catch (NullPointerException e) {
                e.printStackTrace();
                }
        }
        return employeeList;
    }

    private static void writeString(String json, String name) {
        File data = new File("C:\\Users\\user\\IdeaProjects\\JavaCore1.6\\Parser\\" + name);
        try {
            if (data.createNewFile()) {
                FileWriter writer = new FileWriter(data);
                writer.write(json);
                writer.flush();
                writer.close();
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private static String listToJson(List<Employee> list) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.setPrettyPrinting().create();
        Type listType = new TypeToken<List<Employee>>() {
        }.getType();
        return gson.toJson(list, listType);
    }

    public static List<Employee> parseCSV(String[] columnMapping, String fileName) {
        try (CSVReader data = new CSVReader(new FileReader(fileName))) {
            ColumnPositionMappingStrategy<Employee> ColPosStr = new ColumnPositionMappingStrategy<>();
            ColPosStr.setType(Employee.class);
            ColPosStr.setColumnMapping(columnMapping);
            CsvToBean<Employee> toBean = new CsvToBeanBuilder<Employee>(data)
                    .withMappingStrategy(ColPosStr)
                    .build();
            return toBean.parse();
        } catch (Exception E) {
            E.printStackTrace();
        }
        return null;
    }
}
