package dk.telenor;import java.io.ByteArrayOutputStreamimport org.xmlsoap.schemas.soap.envelope.Envelope;

import com.sun.xml.bind.marshaller.NamespacePrefixMapper;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;



/**
 * Helper class to take care of the marshalling/unmarshalling.
 * 
 * @author anders
 */
class JAXBHelper{
		static final String soapNameSpaces = "org.xmlsoap.schemas.soap.envelope:org.dslforum.cwmp_1_0";	
	static final Map PREFIX_MAP = new HashMap();		
	static {
		PREFIX_MAP.put("http://schemas.xmlsoap.org/soap/envelope/", "soapenv");
		PREFIX_MAP.put("urn:dslforum-org:cwmp-1-0", "cwmp");
		PREFIX_MAP.put("http://schemas.xmlsoap.org/soap/encoding/", "soap");
		PREFIX_MAP.put("http://www.w3.org/2001/XMLSchema", "xsd");
		PREFIX_MAP.put("http://www.w3.org/2001/XMLSchema-instance", "xsi");			
	}
		
	private Unmarshaller unmarshaller;
	private Marshaller marshaller;
		
	/**
	 * Construct.
	 */
	public JAXBHelper() {
		try {
			
			final JAXBContext jAXBContext = JAXBContext.newInstance(soapNameSpaces);
			
				this.unmarshaller = jAXBContext.createUnmarshaller();			
				this.marshaller = jAXBContext.createMarshaller();
				
				marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
				marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
				marshaller.setProperty("com.sun.xml.bind.namespacePrefixMapper", new TACSNamespacePrefixMapper(PREFIX_MAP: PREFIX_MAP));
				
			} catch(final JAXBException e) {
				println "Error setting up JAXB" + e
			}
		}
		
		/**
		 * @param data the bytes to unmarshal.
		 * @return the unmarshalled object
		 * @throws JAXBException in case of malformed XML
		 */		
		public Envelope unmarshal(final byte[] data) throws JAXBException {		
			final JAXBElement object = (JAXBElement)unmarshaller.unmarshal(new ByteArrayInputStream(data));
			return object.getValue();					
		}
		
		/**
		 * Write the object out to the outputstream.
		 * @param outputStream that is written to
		 * @param envelope the soap envelope to write
		 */
		public void marshalTo(final OutputStream outputStream, final Envelope envelope){
			try {			
				if(envelope != null) {					
					marshaller.marshal(envelope, outputStream);
				}
			} catch(final JAXBException e) {			
				println "Error marshalling message " + e
			}
		}
		
		/** Write the object out to the outputstream.
		 * @param outputStream that is written to
		 * @param envelope the soap envelope to write
		 */
		public String marshalToString(final Envelope envelope){
			 def baos = new ByteArrayOutputStream()
			 marshalTo(baos, envelope)			 return new String(baos.toByteArray())
		}
	}