package matrix.module.common.bean;

import java.io.InputStream;
import java.io.Serializable;

public class CertInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	private String password;

	private InputStream certStream;

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public InputStream getCertStream() {
		return certStream;
	}

	public void setCertStream(InputStream certStream) {
		this.certStream = certStream;
	}

	public CertInfo(String password, InputStream certStream) {
		this.password = password;
		this.certStream = certStream;
	}
}
