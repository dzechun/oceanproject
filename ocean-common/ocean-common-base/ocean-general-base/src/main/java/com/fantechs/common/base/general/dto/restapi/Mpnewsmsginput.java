
package com.fantechs.common.base.general.dto.restapi;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>mpnewsmsginput complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
 * 
 * <pre>
 * &lt;complexType name="mpnewsmsginput">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="APPID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CONTENT" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="COVERWORDS" type="{http://ws.leige.com/}fontText" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="DIGEST" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MESSAGE_LEVEL" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SAFE" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="TITTLE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="USERID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="USERNAME" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "mpnewsmsginput", propOrder = {
    "appid",
    "content",
    "coverwords",
    "digest",
    "messagelevel",
    "safe",
    "tittle",
    "userid",
    "username"
})
public class Mpnewsmsginput {

    @XmlElement(name = "APPID")
    protected String appid;
    @XmlElement(name = "CONTENT")
    protected String content;
    @XmlElement(name = "COVERWORDS", nillable = true)
    protected List<FontText> coverwords;
    @XmlElement(name = "DIGEST")
    protected String digest;
    @XmlElement(name = "MESSAGE_LEVEL")
    protected String messagelevel;
    @XmlElement(name = "SAFE")
    protected int safe;
    @XmlElement(name = "TITTLE")
    protected String tittle;
    @XmlElement(name = "USERID")
    protected String userid;
    @XmlElement(name = "USERNAME")
    protected String username;

    /**
     * ��ȡappid���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAPPID() {
        return appid;
    }

    /**
     * ����appid���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAPPID(String value) {
        this.appid = value;
    }

    /**
     * ��ȡcontent���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCONTENT() {
        return content;
    }

    /**
     * ����content���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCONTENT(String value) {
        this.content = value;
    }

    /**
     * Gets the value of the coverwords property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the coverwords property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCOVERWORDS().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link FontText }
     * 
     * 
     */
    public List<FontText> getCOVERWORDS() {
        if (coverwords == null) {
            coverwords = new ArrayList<FontText>();
        }
        return this.coverwords;
    }

    /**
     * ��ȡdigest���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDIGEST() {
        return digest;
    }

    /**
     * ����digest���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDIGEST(String value) {
        this.digest = value;
    }

    /**
     * ��ȡmessagelevel���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMESSAGELEVEL() {
        return messagelevel;
    }

    /**
     * ����messagelevel���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMESSAGELEVEL(String value) {
        this.messagelevel = value;
    }

    /**
     * ��ȡsafe���Ե�ֵ��
     * 
     */
    public int getSAFE() {
        return safe;
    }

    /**
     * ����safe���Ե�ֵ��
     * 
     */
    public void setSAFE(int value) {
        this.safe = value;
    }

    /**
     * ��ȡtittle���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTITTLE() {
        return tittle;
    }

    /**
     * ����tittle���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTITTLE(String value) {
        this.tittle = value;
    }

    /**
     * ��ȡuserid���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUSERID() {
        return userid;
    }

    /**
     * ����userid���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUSERID(String value) {
        this.userid = value;
    }

    /**
     * ��ȡusername���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUSERNAME() {
        return username;
    }

    /**
     * ����username���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUSERNAME(String value) {
        this.username = value;
    }

}
