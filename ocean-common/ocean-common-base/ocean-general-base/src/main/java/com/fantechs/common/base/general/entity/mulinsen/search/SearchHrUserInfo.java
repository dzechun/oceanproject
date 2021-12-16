package com.fantechs.common.base.general.entity.mulinsen.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchHrUserInfo extends BaseQuery implements Serializable {

    @ApiModelProperty(name="jobNum",value = "工号")
    private String jobNum;

    @ApiModelProperty(name="deptCodePrefix",value = "部门编码前缀")
    private String deptCodePrefix;

    @ApiModelProperty(name="notDataStatus",value = "不等于该数据状态：0未同步，1已同步, 2已变更")
    private Integer notDataStatus;
}
