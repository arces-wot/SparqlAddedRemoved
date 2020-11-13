package model;

public class EndPoint implements Cloneable{

	private  String host;
	private int port;
	private String path;
	private String authorization;//not implemented yet
	private String scheme ;
	
	
	public EndPoint(String scheme,String host, int port, String path, String authorization) {
		super();
		this.host = host;
		this.port = port;
		this.path = path;
		this.authorization = authorization;
		this.scheme = scheme;
	}

	public EndPoint(String scheme,String host,int port, String path) {
		super();
		this.host = host;
		this.port = port;
		this.path = path;
		this.scheme = scheme;
	}
	
	public String getScheme() {
		return scheme;
	}
	public void setScheme(String scheme) {
		this.scheme = scheme;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getAuthorization() {
		return authorization;
	}
	public void setAuthorization(String authorization) {
		this.authorization = authorization;
	}
	
	public EndPoint clone()throws CloneNotSupportedException{  
		return (EndPoint)super.clone();  
	}  
		  
	
}
