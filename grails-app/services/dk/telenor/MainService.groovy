package dk.telenor

import org.dslforum.cwmp_1_0.GetParameterAttributes;
import org.dslforum.cwmp_1_0.GetParameterNames;
import org.dslforum.cwmp_1_0.GetParameterValues;
import org.dslforum.cwmp_1_0.ParameterNames;
import org.xmlsoap.schemas.soap.envelope.Envelope;

class MainService {

	static scope = "session"	
    boolean transactional = true
    
    def s = 
"""<soapenv:Envelope xmlns:cwmp="urn:dslforum-org:cwmp-1-0" xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:soap="http://schemas.xmlsoap.org/soap/encoding/" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
        <soapenv:Header>
    <cwmp:ID soapenv:mustUnderstand="1">0</cwmp:ID>
</soapenv:Header>
<soapenv:Body>
    <cwmp:GetParameterNames>
        <ParameterPath>InternetGatewayDevice.</ParameterPath>
        <NextLevel>1</NextLevel>
    </cwmp:GetParameterNames>
</soapenv:Body>
</soapenv:Envelope>"""

    def cmdList = null
    
    def initCommands(){
		
		cmdList = [  new GetParameterAttributes(parameterNames : new ParameterNames(any : [""])),
		             new GetParameterValues(parameterNames : new ParameterNames(any : [""]) ),		                 
		             new GetParameterNames(parameterPath: "", nextLevel: false)]
		
	}
                   
                
    def getNextCommand(){
		if(cmdList == null){
			//initCommands()
			//cmdList = [new GetParameterNames(parameterPath: "", nextLevel: false)]
			cmdList = [s]
		}
		if(cmdList.isEmpty()){
			return null
		}
		return cmdList.pop()
	}
	
}
