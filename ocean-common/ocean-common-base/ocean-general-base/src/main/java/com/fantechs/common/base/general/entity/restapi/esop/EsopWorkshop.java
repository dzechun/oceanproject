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
 * 车间表
 * test_workshop
 * @author 81947
 * @date 2021-07-21 17:55:27
 */
@Data
@Table(name = "base_workshop")
public class EsopWorkshop extends ValidGroup implements Serializable {
    /**
     * 车间编码
     */
    @ApiModelProperty(name="code",value = "车间编码")
    @Excel(name = "车间编码", height = 20, width = 30,orderNum="") 
    @Id
    private String code;

    /**
     * 车间名称
     */
    @ApiModelProperty(name="name",value = "车间名称")
    @Excel(name = "车间名称", height = 20, width = 30,orderNum="") 
    private String name;

    /**
     * 全名
     */
    @ApiModelProperty(name="longName",value = "全名")
    @Excel(name = "全名", height = 20, width = 30,orderNum="") 
    @Column(name = "long_name")
    private String longName;

    /**
     * 组织ID
     */
    @ApiModelProperty(name="deptCode",value = "组织ID")
    @Excel(name = "组织ID", height = 20, width = 30,orderNum="") 
    @Column(name = "dept_code")
    private String deptCode;

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