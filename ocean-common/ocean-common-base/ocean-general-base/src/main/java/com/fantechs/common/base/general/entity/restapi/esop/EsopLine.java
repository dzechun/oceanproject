package com.fantechs.common.base.general.entity.restapi.esop;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

;
;

/**
 * 拉线表
 * test_line
 * @author 81947
 * @date 2021-07-21 17:55:27
 */
@Data
@Table(name = "base_line")
public class EsopLine extends ValidGroup implements Serializable {
    /**
     * 拉线编码
     */
    @ApiModelProperty(name="code",value = "拉线编码")
    @Excel(name = "拉线编码", height = 20, width = 30,orderNum="") 
    @Id
    private String code;

    /**
     * 拉线名称
     */
    @ApiModelProperty(name="name",value = "拉线名称")
    @Excel(name = "拉线名称", height = 20, width = 30,orderNum="") 
    private String name;

    /**
     * 拉线简称
     */
    @ApiModelProperty(name="shortName",value = "拉线简称")
    @Excel(name = "拉线简称", height = 20, width = 30,orderNum="") 
    @Column(name = "short_name")
    private String shortName;

    /**
     * 拉长用户ID
     */
    @ApiModelProperty(name="lineMan",value = "拉长用户ID")
    @Excel(name = "拉长用户ID", height = 20, width = 30,orderNum="") 
    @Column(name = "line_man")
    private Integer lineMan;

    /**
     * 车间ID
     */
    @ApiModelProperty(name="workshopCode",value = "车间ID")
    @Excel(name = "车间ID", height = 20, width = 30,orderNum="") 
    @Column(name = "workshop_code")
    private String workshopCode;

    /**
     * hr组织id
     */
    @ApiModelProperty(name="hrCode",value = "hr组织id")
    @Excel(name = "hr组织id", height = 20, width = 30,orderNum="") 
    @Column(name = "hr_code")
    private String hrCode;

    /**
     * hr组织名称
     */
    @ApiModelProperty(name="hrName",value = "hr组织名称")
    @Excel(name = "hr组织名称", height = 20, width = 30,orderNum="") 
    @Column(name = "hr_name")
    private String hrName;

    /**
     * 创建人
     */
    @ApiModelProperty(name="createdUser",value = "创建人")
    @Excel(name = "创建人", height = 20, width = 30,orderNum="") 
    @Column(name = "created_user")
    private Integer createdUser;

    /**
     * 创建时间
     */
    @ApiModelProperty(name="createdTime",value = "创建时间")
    @Excel(name = "创建时间", height = 20, width = 30,orderNum="",exportFormat ="yyyy-MM-dd HH:mm:ss") 
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "created_time")
    private Date createdTime;

    /**
     * 修改人
     */
    @ApiModelProperty(name="modifyUser",value = "修改人")
    @Excel(name = "修改人", height = 20, width = 30,orderNum="") 
    @Column(name = "modify_user")
    private Integer modifyUser;

    /**
     * 修改时间
     */
    @ApiModelProperty(name="modifyTime",value = "修改时间")
    @Excel(name = "修改时间", height = 20, width = 30,orderNum="",exportFormat ="yyyy-MM-dd HH:mm:ss") 
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "modify_time")
    private Date modifyTime;

    /**
     * 是否删除
     */
    @ApiModelProperty(name="isDeleted",value = "是否删除")
    @Excel(name = "是否删除", height = 20, width = 30,orderNum="") 
    @Column(name = "is_deleted")
    private Byte isDeleted;

    private static final long serialVersionUID = 1L;
}