package com.fantechs.common.base.entity.apply;

import com.fantechs.common.base.support.ValidGroup;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Table(name = "smt_order")
@Data
public class  SmtOrder extends ValidGroup implements Serializable {
    /**
     * 订单ID
     */
    @Id
    @Column(name = "order_id")
    @NotNull(groups = update.class,message = "订单id不能为空")
    private Long orderId;

    /**
     * 订单号
     */
    @Column(name = "order_code")
    @NotBlank(message = "订单号不能为空")
    private String orderCode;

    /**
     * 客户ID
     */
    @Column(name = "customer_id")
    private Long customerId;

    /**
     * 产品料号ID
     */
    @Column(name = "material_id")
    @NotNull(message = "产品料号id不能为空")
    private Long materialId;

    /**
     * 订单数量
     */
    @Column(name = "order_quantity")
    @NotNull(message = "订单数量不能为空")
    private Integer orderQuantity;

    /**
     * 已交付数量
     */
    @Column(name = "delivered_quantity")
    private Integer deliveredQuantity;

    /**
     * 喷绘信息
     */
    @Column(name = "inkjet_paint")
    private String inkjetPaint;

    /**
     * 镭雕信息
     */
    @Column(name = "radium_carving")
    private String radiumCarving;

    /**
     * 状态（0、无效 1、有效）
     */
    private Byte status;

    /**
     * 交货日期
     */
    @Column(name = "delivery_date")
    private Date deliveryDate;

    /**
     * 照片链接
     */
    @Column(name = "photo_url")
    private String photoUrl;

    /**
     * 创建人ID
     */
    @Column(name = "create_user_id")
    private Long createUserId;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 修改人ID
     */
    @Column(name = "modified_user_id")
    private Long modifiedUserId;

    /**
     * 修改时间
     */
    @Column(name = "modified_time")
    private Date modifiedTime;

    /**
     * 逻辑删除（0、删除 1、正常）
     */
    @Column(name = "is_delete")
    private Byte isDelete;

    /**
     * 扩展字段1
     */
    private String option1;

    /**
     * 扩展字段2
     */
    private String option2;

    /**
     * 扩展字段3
     */
    private String option3;

}