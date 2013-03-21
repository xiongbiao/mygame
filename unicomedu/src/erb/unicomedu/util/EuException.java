package erb.unicomedu.util;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

public class EuException extends Exception {

	private String customerName;
	private String cMessage;

	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return cMessage;
	}

	public EuException() {
	}

	public EuException(String message) {
		cMessage = message;
	}
	public EuException(Exception ex) {
 		if(ex instanceof UnknownHostException){
 			cMessage = Def.getServiceMsg(502);	
		} else if(ex instanceof ConnectException){
			cMessage = Def.getServiceMsg(501);	
		}else if(ex instanceof SocketTimeoutException){
			cMessage = Def.getServiceMsg(503);	
		}else if(ex instanceof IOException){
			cMessage = Def.getServiceMsg(504);	
		}else {
			cMessage = Def.getServiceMsg(-1);	
		}
	}

	public EuException(String message, String customer) {
		super(message);
		customerName = customer;
	}

	public String getCustomerName() {
		return customerName;
	}

}
