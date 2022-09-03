import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.bean.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;
import java.io.*;
import java.lang.reflect.Type;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        //CSV to JSON parser
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileName = "C:\\Users\\user\\IdeaProjects\\JavaCore1.6\\Parser\\data.csv";
        List<Employee> list = parseCSV(columnMapping, fileName);
        String json = listToJson(list);
        writeString(json);

        //XML to JSON parser

        String fileNameXML = "C:\\Users\\user\\IdeaProjects\\JavaCore1.6\\Parser\\data.xml";
        try {
            List<Employee> listXML = parseXML(fileNameXML);
            writeString(listToJson(listXML));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static List<Employee> parseXML(String filename) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new File(filename));
        Node root = doc.getDocumentElement();
        read(root);

        return null;
    }


    private static void read(Node node) {
        NodeList nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node_ = nodeList.item(i);
            if (Node.ELEMENT_NODE == node_.getNodeType()) {
                System.out.println("Текущий узел: " + node_.getNodeName());
                Element element = (Element) node_;
                NamedNodeMap map = element.getAttributes();
                for (int a = 0; a < map.getLength(); a++) {
                    String attrName = map.item(a).getNodeName();
                    String attrValue = map.item(a).getNodeValue();
                    System.out.println("Атрибут: " + attrName + "; значение: " + attrValue);
                }
                read(node_);
            }
        }
    }

    private static void writeString(String json) {
        File data = new File("C:\\Users\\user\\IdeaProjects\\JavaCore1.6\\Parser\\data.json");
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
