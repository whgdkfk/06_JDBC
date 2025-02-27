package excel_to_xml;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.io.*;
import java.util.Iterator;
// 클로드
public class Test {
	 public static void main(String[] args) {
     try (FileInputStream fis = new FileInputStream("input.xlsx");
          Workbook workbook = new XSSFWorkbook(fis)) {

         Sheet sheet = workbook.getSheetAt(0);
         DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
         DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
         Document document = documentBuilder.newDocument();

         Element root = document.createElement("Rows");
         document.appendChild(root);

         Iterator<Row> rowIterator = sheet.iterator();
         while (rowIterator.hasNext()) {
             Row row = rowIterator.next();
             Element rowElement = document.createElement("Row");
             root.appendChild(rowElement);

             Iterator<Cell> cellIterator = row.iterator();
             while (cellIterator.hasNext()) {
                 Cell cell = cellIterator.next();
                 System.out.println(cell);
                 Element cellElement = document.createElement("Cell");
                 cellElement.appendChild(document.createTextNode(cell.toString()));
                 rowElement.appendChild(cellElement);
             }
         }
//
//         TransformerFactory transformerFactory = TransformerFactory.newInstance();
//         Transformer transformer = transformerFactory.newTransformer();
//         DOMSource domSource = new DOMSource(document);
//         StreamResult streamResult = new StreamResult(new File("output.xml"));
//         transformer.transform(domSource, streamResult);

         System.out.println("XML 파일이 성공적으로 생성되었습니다.");

     } catch (Exception e) {
         e.printStackTrace();
     }
 }
}
