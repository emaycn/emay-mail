package cn.emay.mail.common;

/**
 * 联系人
 *
 * @author Frank
 */
public class Linkman {

    /**
     * 邮件地址
     */
    private String address;

    /**
     * 名称
     */
    private String personal;

    public Linkman() {

    }

    /**
     * @param address 邮件地址
     */
    public Linkman(String address) {
        this.address = address;
    }

    /**
     * @param address  邮件地址
     * @param personal 名称
     */
    public Linkman(String address, String personal) {
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
