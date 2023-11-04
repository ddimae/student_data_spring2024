package ntukhpi.semit.dde.studentsdata.files;

import ntukhpi.semit.dde.studentsdata.entity.Student;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class XMLUtils {
    public static List<Student> readFromXML(String filePath) throws Exception {
        //System.out.println("XMLUtils#readFromXML: "+filePath);

        List<Student> studentList = new ArrayList<>();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        Document document = builder.parse(new File(filePath));
        document.getDocumentElement().normalize();

        NodeList nodeList = document.getElementsByTagName("student");

        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;

                String fullName = element.getElementsByTagName("fullName").item(0).getTextContent();


                Student student = new Student(fullName);
                studentList.add(student);
            }
        }

        return studentList;
    }

    public static List<Student> readFromXMLFull(String filePath) throws Exception {
        //System.out.println("XMLUtils#readFromXML: "+filePath);

        List<Student> studentList = new ArrayList<>();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        Document document = builder.parse(new File(filePath));
        document.getDocumentElement().normalize();

        NodeList nodeList = document.getElementsByTagName("student");

        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;

                String fullName = element.getElementsByTagName("fullName").item(0).getTextContent();


                Student student = new Student(fullName);
                studentList.add(student);
            }
        }

        return studentList;
    }

    public static String writeToXML(String filePath, List<Student> studentList) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        // Create the root element
        Document document = builder.newDocument();
        Element rootElement = document.createElement("students");
        document.appendChild(rootElement);

        // Create the student elements and add them to the root element
        for (Student student : studentList) {
            Element studentElement = document.createElement("student");
            rootElement.appendChild(studentElement);

            Element lastNameElement = document.createElement("lastName");
            lastNameElement.appendChild(document.createTextNode(student.getLastName()));
            studentElement.appendChild(lastNameElement);

            Element firstNameElement = document.createElement("firstName");
            firstNameElement.appendChild(document.createTextNode(student.getFirstName()));
            studentElement.appendChild(firstNameElement);

            Element middleNameElement = document.createElement("middleName");
            middleNameElement.appendChild(document.createTextNode(student.getMiddleName()));
            studentElement.appendChild(middleNameElement);

            Element dateOfBirthElement = document.createElement("dateOfBirth");
            dateOfBirthElement.appendChild(document.createTextNode(student.getDateOfBirth().toString()));
            studentElement.appendChild(dateOfBirthElement);

        }

        // Write the document to file
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        DOMSource source = new DOMSource(document);
        StreamResult result = new StreamResult(new File("results/"+filePath + ".xml"));
        transformer.transform(source, result);

        return filePath + ".xml";
    }


}
