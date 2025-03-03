package excel_to_xml;

import java.io.*;
import java.util.*;
import java.util.zip.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;

public class ExcelToXmlWithoutApachePoi {
    public static void main(String[] args) {
        String excelFilePath = "sample.xlsx";  // 엑셀 파일 경로
        String xmlFilePath = "output.xml";     // 출력할 XML 파일 경로

        try {
            // .xlsx 파일을 ZIP 형식으로 처리
            try (ZipFile zipFile = new ZipFile(excelFilePath)) {
                // sharedStrings.xml 파일을 찾기
                ZipEntry sharedStringsEntry = zipFile.getEntry("xl/sharedStrings.xml");
                Map<Integer, String> sharedStrings = new HashMap<>();
                
                if (sharedStringsEntry != null) {
                    // sharedStrings.xml을 읽어 텍스트 값을 가져오기
                    try (InputStream inputStream = zipFile.getInputStream(sharedStringsEntry)) {
                        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                        DocumentBuilder builder = factory.newDocumentBuilder();
                        Document document = builder.parse(inputStream);
                        
                        NodeList stringList = document.getElementsByTagName("si");
                        for (int i = 0; i < stringList.getLength(); i++) {
                            Node node = stringList.item(i);
                            if (node.getNodeType() == Node.ELEMENT_NODE) {
                                Element element = (Element) node;
                                String text = element.getElementsByTagName("t").item(0).getTextContent();
                                sharedStrings.put(i, text);
                            }
                        }
                    }
                }

                // sheet1.xml 파일을 찾기
                ZipEntry sheetEntry = zipFile.getEntry("xl/worksheets/sheet1.xml");
                if (sheetEntry == null) {
                    System.err.println("sheet1.xml not found in the .xlsx file");
                    return;  // sheet1.xml 파일을 찾을 수 없으면 종료
                }

                // XML 파일을 읽기 위한 InputStream
                try (InputStream inputStream = zipFile.getInputStream(sheetEntry)) {
                    // XML 파서 준비
                    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder builder = factory.newDocumentBuilder();
                    Document document = builder.parse(inputStream);

                    // XML 파일 생성 준비
                    Document outputDocument = builder.newDocument();
                    Element rootElement = outputDocument.createElement("Rows");
                    outputDocument.appendChild(rootElement);

                    // sheet1.xml에서 <row> 태그들 읽기
                    NodeList rowList = document.getElementsByTagName("row");

                    // 각 <row>에 대해 <Row> 엘리먼트 생성
                    for (int i = 0; i < rowList.getLength(); i++) {
                        Node rowNode = rowList.item(i);
                        if (rowNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element rowElement = outputDocument.createElement("Row");
                            rootElement.appendChild(rowElement);

                            // <row> 내부의 <c> (셀) 태그들 읽기
                            NodeList cellList = ((Element) rowNode).getElementsByTagName("c");
                            for (int j = 0; j < cellList.getLength(); j++) {
                                Node cellNode = cellList.item(j);
                                if (cellNode.getNodeType() == Node.ELEMENT_NODE) {
                                    Element cellElement = outputDocument.createElement("Cell");
                                    String cellValue = getCellValue((Element) cellNode, sharedStrings);
                                    cellElement.appendChild(outputDocument.createTextNode(cellValue));
                                    rowElement.appendChild(cellElement);
                                }
                            }
                        }
                    }

                    // XML 파일로 저장
                    try (FileWriter writer = new FileWriter(new File(xmlFilePath))) {
                        writeXmlToFile(outputDocument, writer);
                        System.out.println("XML 파일이 생성되었습니다: " + xmlFilePath);
                    } catch (IOException e) {
                        System.err.println("파일 저장 중 오류 발생: " + e.getMessage());
                    }
                } catch (Exception e) {
                    System.err.println("엑셀 파일 파싱 중 오류 발생: " + e.getMessage());
                }
            } catch (IOException e) {
                System.err.println("엑셀 파일을 열 수 없습니다: " + e.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 셀 값 추출 (공유 문자열을 처리하는 부분 추가)
    private static String getCellValue(Element cellElement, Map<Integer, String> sharedStrings) {
        String value = "";
        String cellType = cellElement.getAttribute("t"); // 셀의 타입 (예: "s", "b", "n" 등)
        
        // 텍스트 값 (영문, 한글 포함) 처리
        if ("s".equals(cellType)) {
            // 's'는 공유 문자열을 나타내며, v 태그가 존재
            Node valueNode = cellElement.getElementsByTagName("v").item(0);
            if (valueNode != null) {
                int index = Integer.parseInt(valueNode.getTextContent());
                value = sharedStrings.getOrDefault(index, "");
            }
        } 
        // 숫자 값 처리
        else if ("n".equals(cellType) || cellType.isEmpty()) {
            Node valueNode = cellElement.getElementsByTagName("v").item(0);
            if (valueNode != null) {
                value = valueNode.getTextContent();
            }
        }
        // Boolean 값 처리
        else if ("b".equals(cellType)) {
            Node valueNode = cellElement.getElementsByTagName("v").item(0);
            if (valueNode != null) {
                value = valueNode.getTextContent().equals("1") ? "TRUE" : "FALSE";
            }
        } 
        // 그 외 기본 텍스트 처리
        else {
            Node valueNode = cellElement.getElementsByTagName("v").item(0);
            if (valueNode != null) {
                value = valueNode.getTextContent();
            }
        }
        
        return value;
    }

    // XML을 파일로 출력
    private static void writeXmlToFile(Document document, FileWriter writer) throws IOException {
        try {
            // XML을 파일로 출력하는 Transformer 생성
            javax.xml.transform.TransformerFactory transformerFactory = javax.xml.transform.TransformerFactory.newInstance();
            javax.xml.transform.Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(javax.xml.transform.OutputKeys.INDENT, "yes");

            // XML을 파일로 기록
            transformer.transform(new javax.xml.transform.dom.DOMSource(document), new javax.xml.transform.stream.StreamResult(writer));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
