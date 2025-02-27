package excel_to_xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ExcelToXMLConverter {
	public static void main(String[] args) {
		try (FileInputStream fis = new FileInputStream(new File("input.xlsx")); Workbook workbook = new XSSFWorkbook(fis)) {

			Sheet sheet = workbook.getSheetAt(0); // 첫 번째 시트 선택
			Iterator<Row> rowIterator = sheet.iterator();

			// DocumentBuilderFactory 생성
			DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();

			// 새로운 XML 문서 생성
			Document document = documentBuilder.newDocument();

			// 루트 엘리먼트 생성
			Element root = document.createElement("Rows");
			document.appendChild(root);

			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();
				Element rowElement = document.createElement("Row");
				root.appendChild(rowElement);

				Iterator<Cell> cellIterator = row.cellIterator();
				while (cellIterator.hasNext()) {
					Cell cell = cellIterator.next();
					Element cellElement = document.createElement("Cell");
					cellElement.appendChild(document.createTextNode(cell.toString()));
					rowElement.appendChild(cellElement);
				}
			}

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
