
package com.fantechs.common.base.general.dto.restapi;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>DT_MES_BADCODE_QUERY_REQ complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
 * 
 * <pre>
 * &lt;complexType name="DT_MES_BADCODE_QUERY_REQ">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="KATALOGART" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CODEGRUPPE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DT_MES_BADCODE_QUERY_REQ", propOrder = {
    "katalogart",
    "codegruppe"
})
public class DTMESBADCODEQUERYREQ {

    @XmlElement(name = "KATALOGART")
    protected String katalogart;
    @XmlElement(name = "CODEGRUPPE")
    protected String codegruppe;

    /**
     * ��ȡkatalogart���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getKATALOGART() {
        return katalogart;
    }

    /**
     * ����katalogart���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setKATALOGART(String value) {
        this.katalogart = value;
    }

    /**
     * ��ȡcodegruppe���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCODEGRUPPE() {
        return codegruppe;
    }

    /**
     * ����codegruppe���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCODEGRUPPE(String value) {
        this.codegruppe = value;
    }

}
