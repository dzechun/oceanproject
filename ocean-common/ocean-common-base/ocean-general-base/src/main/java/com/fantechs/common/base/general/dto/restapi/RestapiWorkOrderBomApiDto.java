package com.fantechs.common.base.general.dto.restapi;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class RestapiWorkOrderBomApiDto implements Serializable {


    protected String RSPOS;  //预留相目号

    protected String ZJMATNR;   //组件物料号

    protected String ZJMATXT; //组件物料描述

    protected String ZJCHARG; //组件批次

    protected String MENGE;   //组件数量

    protected String EINHEIT;   //组件单位

    protected String XLOEK;   //组件状态

}
