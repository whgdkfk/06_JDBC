package excel_to_xml;

import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class XMLWriter {
    public static void main(String[] args) {
        try {
            // DocumentBuilderFactory 생성
            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();

            // 새로운 XML 문서 생성
            Document document = documentBuilder.newDocument();

            // 루트 엘리먼트 생성
            Element root = document.createElement("Rows");
            document.appendChild(root);

            // 예시 데이터 추가
            Element row = document.createElement("Row");
            root.appendChild(row);

            Element cell1 = document.createElement("Cell");
            cell1.appendChild(document.createTextNode("데이터1"));
            row.appendChild(cell1);

            Element cell2 = document.createElement("Cell");
            cell2.appendChild(document.createTextNode("데이터2"));
            row.appendChild(cell2);

            // XML 파일로 저장
            FileOutputStream fos = new FileOutputStream(new File("output.xml"));
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(new DOMSource(document), new StreamResult(fos));

            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
