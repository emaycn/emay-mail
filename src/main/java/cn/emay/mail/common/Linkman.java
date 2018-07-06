package cn.emay.mail.common;

public class Linkman {

	private String address;

	private String personal;

	public Linkman() {

	}
	
	public Linkman(String address) {
		this.address = address;
	}

	public Linkman(String address,String personal) {
		this.address = address;
		this.personal = personal;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPersonal() {
		return personal;
	}

	public void setPersonal(String personal) {
		this.personal = personal;
	}

}
