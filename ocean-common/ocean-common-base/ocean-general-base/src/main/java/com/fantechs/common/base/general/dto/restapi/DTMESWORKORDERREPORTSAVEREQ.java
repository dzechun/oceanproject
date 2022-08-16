
package com.fantechs.common.base.general.dto.restapi;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>DT_MES_WORKORDER_REPORT_SAVE_REQ complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
 * 
 * <pre>
 * &lt;complexType name="DT_MES_WORKORDER_REPORT_SAVE_REQ">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="AUFNR" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="VORNR" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="LMNGA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="RMNGA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="XMNGA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DT_MES_WORKORDER_REPORT_SAVE_REQ", propOrder = {
    "aufnr",
    "vornr",
    "lmnga",
    "rmnga",
    "xmnga"
})
public class DTMESWORKORDERREPORTSAVEREQ {

    @XmlElement(name = "AUFNR")
    protected String aufnr;
    @XmlElement(name = "VORNR")
    protected String vornr;
    @XmlElement(name = "LMNGA")
    protected String lmnga;
    @XmlElement(name = "RMNGA")
    protected String rmnga;
    @XmlElement(name = "XMNGA")
    protected String xmnga;

    /**
     * ��ȡaufnr���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAUFNR() {
        return aufnr;
    }

    /**
     * ����aufnr���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAUFNR(String value) {
        this.aufnr = value;
    }

    /**
     * ��ȡvornr���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVORNR() {
        return vornr;
    }

    /**
     * ����vornr���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVORNR(String value) {
        this.vornr = value;
    }

    /**
     * ��ȡlmnga���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLMNGA() {
        return lmnga;
    }

    /**
     * ����lmnga���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLMNGA(String value) {
        this.lmnga = value;
    }

    /**
     * ��ȡrmnga���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRMNGA() {
        return rmnga;
    }

    /**
     * ����rmnga���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRMNGA(String value) {
        this.rmnga = value;
    }

    /**
     * ��ȡxmnga���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getXMNGA() {
        return xmnga;
    }

    /**
     * ����xmnga���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setXMNGA(String value) {
        this.xmnga = value;
    }

}
