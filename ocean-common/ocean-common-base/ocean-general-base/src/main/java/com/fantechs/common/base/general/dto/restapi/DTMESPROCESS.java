
package com.fantechs.common.base.general.dto.restapi;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>DT_MES_PROCESS complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
 * 
 * <pre>
 * &lt;complexType name="DT_MES_PROCESS">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="MATNR" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="WERKS" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="PLNAL" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="KTEXT" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="VERWE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="STATU" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="VORNR" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="LTXA1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ARBPL" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="WERKW" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="STEUS" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="BMSCH" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="VGW01" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="VGW02" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SPLIM" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SPMUS" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DISPO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DT_MES_PROCESS", propOrder = {
    "matnr",
    "werks",
    "plnal",
    "ktext",
    "verwe",
    "statu",
    "vornr",
    "ltxa1",
    "arbpl",
    "werkw",
    "steus",
    "bmsch",
    "vgw01",
    "vgw02",
    "splim",
    "spmus",
    "dispo"
})
public class DTMESPROCESS {

    @XmlElement(name = "MATNR")
    protected String matnr;
    @XmlElement(name = "WERKS")
    protected String werks;
    @XmlElement(name = "PLNAL")
    protected String plnal;
    @XmlElement(name = "KTEXT")
    protected String ktext;
    @XmlElement(name = "VERWE")
    protected String verwe;
    @XmlElement(name = "STATU")
    protected String statu;
    @XmlElement(name = "VORNR")
    protected String vornr;
    @XmlElement(name = "LTXA1")
    protected String ltxa1;
    @XmlElement(name = "ARBPL")
    protected String arbpl;
    @XmlElement(name = "WERKW")
    protected String werkw;
    @XmlElement(name = "STEUS")
    protected String steus;
    @XmlElement(name = "BMSCH")
    protected String bmsch;
    @XmlElement(name = "VGW01")
    protected String vgw01;
    @XmlElement(name = "VGW02")
    protected String vgw02;
    @XmlElement(name = "SPLIM")
    protected String splim;
    @XmlElement(name = "SPMUS")
    protected String spmus;
    @XmlElement(name = "DISPO")
    protected String dispo;

    /**
     * ��ȡmatnr���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMATNR() {
        return matnr;
    }

    /**
     * ����matnr���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMATNR(String value) {
        this.matnr = value;
    }

    /**
     * ��ȡwerks���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWERKS() {
        return werks;
    }

    /**
     * ����werks���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWERKS(String value) {
        this.werks = value;
    }

    /**
     * ��ȡplnal���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPLNAL() {
        return plnal;
    }

    /**
     * ����plnal���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPLNAL(String value) {
        this.plnal = value;
    }

    /**
     * ��ȡktext���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getKTEXT() {
        return ktext;
    }

    /**
     * ����ktext���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setKTEXT(String value) {
        this.ktext = value;
    }

    /**
     * ��ȡverwe���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVERWE() {
        return verwe;
    }

    /**
     * ����verwe���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVERWE(String value) {
        this.verwe = value;
    }

    /**
     * ��ȡstatu���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSTATU() {
        return statu;
    }

    /**
     * ����statu���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSTATU(String value) {
        this.statu = value;
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
     * ��ȡltxa1���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLTXA1() {
        return ltxa1;
    }

    /**
     * ����ltxa1���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLTXA1(String value) {
        this.ltxa1 = value;
    }

    /**
     * ��ȡarbpl���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getARBPL() {
        return arbpl;
    }

    /**
     * ����arbpl���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setARBPL(String value) {
        this.arbpl = value;
    }

    /**
     * ��ȡwerkw���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWERKW() {
        return werkw;
    }

    /**
     * ����werkw���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWERKW(String value) {
        this.werkw = value;
    }

    /**
     * ��ȡsteus���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSTEUS() {
        return steus;
    }

    /**
     * ����steus���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSTEUS(String value) {
        this.steus = value;
    }

    /**
     * ��ȡbmsch���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBMSCH() {
        return bmsch;
    }

    /**
     * ����bmsch���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBMSCH(String value) {
        this.bmsch = value;
    }

    /**
     * ��ȡvgw01���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVGW01() {
        return vgw01;
    }

    /**
     * ����vgw01���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVGW01(String value) {
        this.vgw01 = value;
    }

    /**
     * ��ȡvgw02���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVGW02() {
        return vgw02;
    }

    /**
     * ����vgw02���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVGW02(String value) {
        this.vgw02 = value;
    }

    /**
     * ��ȡsplim���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSPLIM() {
        return splim;
    }

    /**
     * ����splim���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSPLIM(String value) {
        this.splim = value;
    }

    /**
     * ��ȡspmus���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSPMUS() {
        return spmus;
    }

    /**
     * ����spmus���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSPMUS(String value) {
        this.spmus = value;
    }

    /**
     * ��ȡdispo���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDISPO() {
        return dispo;
    }

    /**
     * ����dispo���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDISPO(String value) {
        this.dispo = value;
    }

}
