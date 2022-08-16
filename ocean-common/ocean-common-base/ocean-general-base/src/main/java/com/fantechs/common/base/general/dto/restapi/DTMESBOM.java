
package com.fantechs.common.base.general.dto.restapi;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>DT_MES_BOM complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
 * 
 * <pre>
 * &lt;complexType name="DT_MES_BOM">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="AENNR" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="OITXT" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MATNR" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MAKTX_F" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="POSNR" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="IDNRK" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MAKTX_Z" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MENGE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MEINS" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ZWH" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="POSTP" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="POTX1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="POTX2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SORTF" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="WERKS" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="STLAN" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="STLAL" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="BMENG" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="STLST" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NFEAG" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NFGRP" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ALPGR" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ALPRF" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ALPST" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="EWAHR" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DT_MES_BOM", propOrder = {
    "aennr",
    "oitxt",
    "matnr",
    "maktxf",
    "posnr",
    "idnrk",
    "maktxz",
    "menge",
    "meins",
    "zwh",
    "postp",
    "potx1",
    "potx2",
    "sortf",
    "werks",
    "stlan",
    "stlal",
    "bmeng",
    "stlst",
    "nfeag",
    "nfgrp",
    "alpgr",
    "alprf",
    "alpst",
    "ewahr"
})
public class DTMESBOM {

    @XmlElement(name = "AENNR")
    protected String aennr; //变更编号
    @XmlElement(name = "OITXT")
    protected String oitxt;  //对象管理记录描述
    @XmlElement(name = "MATNR")
    protected String matnr;    //父项物料号
    @XmlElement(name = "MAKTX_F")
    protected String maktxf;    //父项物料描述
    @XmlElement(name = "POSNR")
    protected String posnr;     //子行项目
    @XmlElement(name = "IDNRK")
    protected String idnrk;     //子项物料号
    @XmlElement(name = "MAKTX_Z")
    protected String maktxz;     //子项物料描述
    @XmlElement(name = "MENGE")
    protected String menge;     //用量
    @XmlElement(name = "MEINS")
    protected String meins;     //子项物料单位
    @XmlElement(name = "ZWH")
    protected String zwh;       //位号
    @XmlElement(name = "POSTP")
    protected String postp;     //项目类别
    @XmlElement(name = "POTX1")
    protected String potx1;     //文本1
    @XmlElement(name = "POTX2")
    protected String potx2;     //文本2
    @XmlElement(name = "SORTF")
    protected String sortf;     //备注（排序字符串）
    @XmlElement(name = "WERKS")
    protected String werks;     //工厂
    @XmlElement(name = "STLAN")
    protected String stlan;     //BOM用途
    @XmlElement(name = "STLAL")
    protected String stlal;     //可选的BOM
    @XmlElement(name = "BMENG")
    protected String bmeng;     //基本数量
    @XmlElement(name = "STLST")
    protected String stlst;     //BOM状态
    @XmlElement(name = "NFEAG")
    protected String nfeag;     //中止组
    @XmlElement(name = "NFGRP")
    protected String nfgrp;     //后继组
    @XmlElement(name = "ALPGR")
    protected String alpgr;     //替代群组
    @XmlElement(name = "ALPRF")
    protected String alprf;     //优先级
    @XmlElement(name = "ALPST")
    protected String alpst;     //替代策略
    @XmlElement(name = "EWAHR")
    protected String ewahr;     //百分比

    /**
     * ��ȡaennr���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAENNR() {
        return aennr;
    }

    /**
     * ����aennr���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAENNR(String value) {
        this.aennr = value;
    }

    /**
     * ��ȡoitxt���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOITXT() {
        return oitxt;
    }

    /**
     * ����oitxt���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOITXT(String value) {
        this.oitxt = value;
    }

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
     * ��ȡmaktxf���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMAKTXF() {
        return maktxf;
    }

    /**
     * ����maktxf���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMAKTXF(String value) {
        this.maktxf = value;
    }

    /**
     * ��ȡposnr���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPOSNR() {
        return posnr;
    }

    /**
     * ����posnr���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPOSNR(String value) {
        this.posnr = value;
    }

    /**
     * ��ȡidnrk���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIDNRK() {
        return idnrk;
    }

    /**
     * ����idnrk���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIDNRK(String value) {
        this.idnrk = value;
    }

    /**
     * ��ȡmaktxz���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMAKTXZ() {
        return maktxz;
    }

    /**
     * ����maktxz���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMAKTXZ(String value) {
        this.maktxz = value;
    }

    /**
     * ��ȡmenge���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMENGE() {
        return menge;
    }

    /**
     * ����menge���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMENGE(String value) {
        this.menge = value;
    }

    /**
     * ��ȡmeins���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMEINS() {
        return meins;
    }

    /**
     * ����meins���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMEINS(String value) {
        this.meins = value;
    }

    /**
     * ��ȡzwh���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getZWH() {
        return zwh;
    }

    /**
     * ����zwh���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setZWH(String value) {
        this.zwh = value;
    }

    /**
     * ��ȡpostp���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPOSTP() {
        return postp;
    }

    /**
     * ����postp���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPOSTP(String value) {
        this.postp = value;
    }

    /**
     * ��ȡpotx1���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPOTX1() {
        return potx1;
    }

    /**
     * ����potx1���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPOTX1(String value) {
        this.potx1 = value;
    }

    /**
     * ��ȡpotx2���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPOTX2() {
        return potx2;
    }

    /**
     * ����potx2���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPOTX2(String value) {
        this.potx2 = value;
    }

    /**
     * ��ȡsortf���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSORTF() {
        return sortf;
    }

    /**
     * ����sortf���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSORTF(String value) {
        this.sortf = value;
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
     * ��ȡstlan���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSTLAN() {
        return stlan;
    }

    /**
     * ����stlan���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSTLAN(String value) {
        this.stlan = value;
    }

    /**
     * ��ȡstlal���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSTLAL() {
        return stlal;
    }

    /**
     * ����stlal���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSTLAL(String value) {
        this.stlal = value;
    }

    /**
     * ��ȡbmeng���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBMENG() {
        return bmeng;
    }

    /**
     * ����bmeng���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBMENG(String value) {
        this.bmeng = value;
    }

    /**
     * ��ȡstlst���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSTLST() {
        return stlst;
    }

    /**
     * ����stlst���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSTLST(String value) {
        this.stlst = value;
    }

    /**
     * ��ȡnfeag���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNFEAG() {
        return nfeag;
    }

    /**
     * ����nfeag���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNFEAG(String value) {
        this.nfeag = value;
    }

    /**
     * ��ȡnfgrp���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNFGRP() {
        return nfgrp;
    }

    /**
     * ����nfgrp���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNFGRP(String value) {
        this.nfgrp = value;
    }

    /**
     * ��ȡalpgr���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getALPGR() {
        return alpgr;
    }

    /**
     * ����alpgr���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setALPGR(String value) {
        this.alpgr = value;
    }

    /**
     * ��ȡalprf���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getALPRF() {
        return alprf;
    }

    /**
     * ����alprf���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setALPRF(String value) {
        this.alprf = value;
    }

    /**
     * ��ȡalpst���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getALPST() {
        return alpst;
    }

    /**
     * ����alpst���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setALPST(String value) {
        this.alpst = value;
    }

    /**
     * ��ȡewahr���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEWAHR() {
        return ewahr;
    }

    /**
     * ����ewahr���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEWAHR(String value) {
        this.ewahr = value;
    }

}
