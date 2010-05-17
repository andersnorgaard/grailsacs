package dk.telenor

import org.dslforum.cwmp_1_0.ID;
import org.dslforum.cwmp_1_0.Inform;
import org.dslforum.cwmp_1_0.InformResponse;
import org.xmlsoap.schemas.soap.envelope.Body;
import org.xmlsoap.schemas.soap.envelope.Envelope;
import org.xmlsoap.schemas.soap.envelope.Header;


/**
 * ssh an@oak.code3.dk -R 3336:localhost:8080
 * 
 * @author co3ano
 */
class TACSServiceController {
	
	def jaxbHelper = new JAXBHelper()
	def mainService

	private String readRequest(){
		final String requestBody = request.reader.text
		println("Got requestBody " + requestBody)
		println("With scheme: " + request.scheme) 
		println("With headers: ")
		request.headerNames.each{headerName -> println "\t$headerName : ${request.getHeaders(headerName).collect{it}.join(",")}"}
		return requestBody
	}
	
	private void doNextOrNothing(){
		def jobObj = mainService.getNextCommand()
		if(jobObj){
			//final respString = jaxbHelper.marshalToString( getEnvelopeWith(jobObj, null) );
			final respString = jobObj			
			println "Sending request to cpe: $respString"
			render(text: respString, contentType:"text/xml",encoding:"UTF-8")
		}else{
			println "Nothing more to do at ${new Date()}. Sending 204"
			render(text: "", contentType:"text/xml",encoding:"UTF-8", status: 204)
		}
	}
	
    def index = { 
		final String requestBody = readRequest()
		println "Read request"
		if(requestBody){
			def reqEnv = jaxbHelper.unmarshal(requestBody.getBytes())
			def reqObj = reqEnv.body.any.first()
			println "ReqObj $reqObj"
			if(reqObj instanceof Inform){
				def respObj = new InformResponse(maxEnvelopes: 1);
				final respString = jaxbHelper.marshalToString( getEnvelopeWith(respObj, reqEnv.header) );
				println "Returning reply: $respString"
				render(text: respString, contentType:"text/xml",encoding:"UTF-8")
			} else if (reqObj.class.name.endsWith("Response")){
				doNextOrNothing()
			}			 
		} else {
			doNextOrNothing()			
		}
	}
	
	
	
	/**
	 * Get the envelope packaged with content.
	 * 
	 * @param content the content to send
	 * @return the envelope packaged with content
	 */
	public Envelope getEnvelopeWith(final Object content, final Header reqHeader){		
		/* Prepare the envelope and body to send back to the CPE */
    	final envelope = new Envelope(body: new Body(any: [content]));
    	
    	//Copy header - CPE can only set ID in header, so if header != null, we assume an ID is there.
    	if (reqHeader != null){
    		envelope.header = new Header(any: [reqHeader.getAny().find{ it instanceof ID }])
    	}else{
    		envelope.header = new Header(any: [ new ID(value: "0", mustUnderstand: Boolean.TRUE ) ])
    	}
		return envelope
	}    
}
