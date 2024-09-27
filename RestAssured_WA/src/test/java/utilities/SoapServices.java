package utilities;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Map;
import java.util.TreeMap;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.Node;
import javax.xml.soap.SOAPBodyElement;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

@SuppressWarnings("restriction")
public class SoapServices {

	SOAPConnectionFactory soapConnectionFactory = null;
	SOAPConnection soapConnection = null;
	MessageFactory messageFactory = null;
	SOAPMessage soapMessage = null;
	SOAPPart soapPart = null;
	String nameSpace = null;

	@SuppressWarnings({ "static-access", "unused" })
	public Map<String, String> postTelematicsSOAPService(String inputXML, String nameSpace, String endPointURL) {
		String nodeValue = "";
		Map<String, String> xmlNodeValuesMap = new TreeMap<String, String>(String.CASE_INSENSITIVE_ORDER);
		try {
			System.setProperty("java.net.useSystemProxies", "true");

			soapConnectionFactory = SOAPConnectionFactory.newInstance();
			soapConnection = soapConnectionFactory.createConnection();

			messageFactory = MessageFactory.newInstance();
			soapMessage = messageFactory.createMessage();
			soapPart = soapMessage.getSOAPPart();

			StreamSource streamSource = new StreamSource(new StringReader(inputXML));

			soapPart.setContent(streamSource);

			MimeHeaders mimeHeader = soapMessage.getMimeHeaders();
			mimeHeader.addHeader("SOAPAction", nameSpace);
			SOAPMessage messagerequest = soapConnection.call(soapMessage, endPointURL);

			ByteArrayOutputStream out = new ByteArrayOutputStream();
			messagerequest.writeTo(out);
			Object resultOfWSDL = out.toString();

			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			Source source = messagerequest.getSOAPPart().getContent();

			StringWriter writer = new StringWriter();
			StreamResult sResult = new StreamResult(writer);

			transformer.transform(source, sResult);

			String resultOftelematicsSOAPService = writer.getBuffer().toString();

			xmlNodeValuesMap.put("resultOftelematicsSOAPService", resultOftelematicsSOAPService);

			MessageFactory messageFactory = MessageFactory.newInstance();
			SOAPMessage message = messageFactory.createMessage(new MimeHeaders(),
					new ByteArrayInputStream(((String) resultOfWSDL).getBytes()));
			javax.xml.soap.SOAPBody body = message.getSOAPBody();
			org.w3c.dom.NodeList n2 = body.getElementsByTagName("*");
			for (int i = 0; i < ((org.w3c.dom.NodeList) n2).getLength(); i++) {
				String nodeName = ((org.w3c.dom.NodeList) n2).item(i).getNodeName();
				Node node = (Node) n2.item(i);
				if (node.getNodeType() == node.ELEMENT_NODE) {
					SOAPBodyElement bodyElement = (SOAPBodyElement) node;
					nodeValue = node.getValue();
					if (nodeName.contains(":"))
						nodeName = nodeName.split("\\:")[1];
					if (!(xmlNodeValuesMap.containsKey(nodeName))) {
						xmlNodeValuesMap.put(nodeName, nodeValue);
						// System.out.println(nodeName);
					} else {
						for (int j = 1; j < 30; j++) {
							if (!(xmlNodeValuesMap.containsKey(nodeName + "_" + j))) {
								xmlNodeValuesMap.put(nodeName + "_" + j, nodeValue);
								// System.out.println(nodeName+"_"+j);
								break;
							}
						} // end of J for loop
					} // End of Else
				}
			}
			// System.out.println(xmlNodeValuesMap);
			soapConnection.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return xmlNodeValuesMap;
	}
}
