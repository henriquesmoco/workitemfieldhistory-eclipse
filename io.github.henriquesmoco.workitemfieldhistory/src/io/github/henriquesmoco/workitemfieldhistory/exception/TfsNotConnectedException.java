package io.github.henriquesmoco.workitemfieldhistory.exception;


@SuppressWarnings("serial")
public class TfsNotConnectedException extends RuntimeException {
	public static final short ID = 1;
	
	public TfsNotConnectedException() {
		super("Not connected to TFS");
	}
	
}
