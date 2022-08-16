package com.fantechs.common.base.general.dto.restapi;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class RestapiWorkOrderprocessApiDto implements Serializable {

    protected String VORNR;  //工序号
    protected String ARBPL;   //工作中心
    protected String LTXA1; //工序名称


}
