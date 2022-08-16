package com.fantechs.common.base.entity.basic.U9;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "Cust_GetItemInfo")
@Data
public class CustGetItemInfo {

    @Column(name = "料品ID")
    private String 料品id;

    private String 料品编码;

    private String 品名;

    private String 规格;

    private String 描述;

    private String 单位编码;

    private String 单位名称;

    @Column(name = "组织ID")
    private String 组织id;

    private String 组织编码;

    private String 组织名称;

    private String 物料分类;

    private String 可生产;

    private String 储位;

    private String 存储地点;

    private String 存储地点编码;

    private Date 最后更新时间;
}
