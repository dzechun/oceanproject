
package com.fantechs.common.base.general.dto.restapi;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>fontText complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
 * 
 * <pre>
 * &lt;complexType name="fontText">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="text" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="wm_text_align" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="wm_text_color" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="wm_text_font" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="wm_text_size" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="wm_text_wrap" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "fontText", propOrder = {
    "text",
    "wmTextAlign",
    "wmTextColor",
    "wmTextFont",
    "wmTextSize",
    "wmTextWrap"
})
public class FontText {

    protected String text;
    @XmlElement(name = "wm_text_align")
    protected String wmTextAlign;
    @XmlElement(name = "wm_text_color")
    protected String wmTextColor;
    @XmlElement(name = "wm_text_font")
    protected String wmTextFont;
    @XmlElement(name = "wm_text_size")
    protected Integer wmTextSize;
    @XmlElement(name = "wm_text_wrap")
    protected boolean wmTextWrap;

    /**
     * ��ȡtext���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getText() {
        return text;
    }

    /**
     * ����text���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setText(String value) {
        this.text = value;
    }

    /**
     * ��ȡwmTextAlign���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWmTextAlign() {
        return wmTextAlign;
    }

    /**
     * ����wmTextAlign���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWmTextAlign(String value) {
        this.wmTextAlign = value;
    }

    /**
     * ��ȡwmTextColor���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWmTextColor() {
        return wmTextColor;
    }

    /**
     * ����wmTextColor���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWmTextColor(String value) {
        this.wmTextColor = value;
    }

    /**
     * ��ȡwmTextFont���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWmTextFont() {
        return wmTextFont;
    }

    /**
     * ����wmTextFont���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWmTextFont(String value) {
        this.wmTextFont = value;
    }

    /**
     * ��ȡwmTextSize���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getWmTextSize() {
        return wmTextSize;
    }

    /**
     * ����wmTextSize���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setWmTextSize(Integer value) {
        this.wmTextSize = value;
    }

    /**
     * ��ȡwmTextWrap���Ե�ֵ��
     * 
     */
    public boolean isWmTextWrap() {
        return wmTextWrap;
    }

    /**
     * ����wmTextWrap���Ե�ֵ��
     * 
     */
    public void setWmTextWrap(boolean value) {
        this.wmTextWrap = value;
    }

}
