package dk.telenor;import java.io.ByteArrayOutputStreamimport org.xmlsoap.schemas.soap.envelope.Envelope;

import com.sun.xml.bind.marshaller.NamespacePrefixMapper;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

class TACSNamespacePrefixMapper extends NamespacePrefixMapper {		private PREFIX_MAP
	
	public String getPreferredPrefix(final String namespaceUri, final String suggestion, final boolean requirePrefix) {
		if(PREFIX_MAP.containsKey(namespaceUri)){
			return PREFIX_MAP.get(namespaceUri);
		} 
		return suggestion;												
	}

	public String[] getPreDeclaredNamespaceUris() {
		return new ArrayList<String>(PREFIX_MAP.keySet()).toArray(new String[1]);
	}
}
