
package utilities;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasXPath;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.Node;
import javax.xml.soap.SOAPBodyElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

import org.apache.commons.lang3.StringUtils;

import io.restassured.RestAssured;
import io.restassured.response.Response;

@SuppressWarnings("restriction")
public class RASoapService extends Global {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Response soapRequest(String baseURI, String inputPayload) {

		HashMap headermap = new HashMap<Object, Object>();
		//headermap.put("Content-Type", "text/xml");
		//headermap.put("Content-Type", "application/soap+xml; charset=utf-8");
		//headermap.put("Content-Type", "text/xml");
		headermap.put("Content-Type", "text/xml; charset=UTF-8");
		


		Response response = RestAssured.given().log().all()
				.headers(headermap).
				body(inputPayload).
				post(baseURI);
		System.out.println(response.asString());
		System.out.println(response);
		return response;
	}
	
	

	@SuppressWarnings({ "unused", "static-access" })
	public Map<String, String> getXMLNodeValuesMap(Response response) throws SOAPException, IOException {
		String nodeValue = "";
		Map<String, String> xmlNodeValuesMap = new TreeMap<String, String>(String.CASE_INSENSITIVE_ORDER);
		System.out.println(response.asString());
		Object resultOfWSDL = response.asString();
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
		return xmlNodeValuesMap;
	}

	public String responseCodeValidation(String statusCode, String statusMessage) {
		int expectedResponseCode, actualResponseCode;
		String result = "";

		expectedResponseCode = Integer.parseInt(inputPayloadElementsMap.get(expectedHttpResponseCode));
		if (!StringUtils.isEmpty(statusCode)) {
			actualResponseCode = Integer.parseInt(statusCode);

			System.out.println("Expected Response Code: " + expectedResponseCode);
			System.out.println("Actual Response Code: " + actualResponseCode);
			if (expectedResponseCode != actualResponseCode)
				result = "Received incorrect HttpResponse code. Exp Response Code: " + expectedResponseCode
						+ ", Actual Response Code: " + actualResponseCode + "."+"StatusMessage is:"+statusMessage;
		} else
			result = "Actual HTTP Response code is null.";

		return result;

	}

	public long responseTime(Response response) {
		long responseTimeOfOSB1 = 0;
		// long rs1 =0;
		responseTimeOfOSB1 = response.getTime();
		// rs1=(long) response.getTimeIn(TimeUnit.MILLISECONDS);
		return responseTimeOfOSB1;
	}
}
