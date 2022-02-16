package com.fantechs.common.base.general.dto.basic;

import com.fantechs.common.base.general.entity.basic.BaseBarcodeRuleSpec;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Data;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.Serializable;
import java.util.List;

@Data
public class BatchGenerateCodeDto implements Serializable {

    @ApiModelProperty(name="list",value = "条码规则集合")
    List<BaseBarcodeRuleSpec> list;

    @ApiModelProperty(name="qty",value = "生成数量")
    Integer qty;

    @ApiModelProperty(name="key",value = "redis key")
    String key;

    @ApiModelProperty(name="code",value = "产品料号、生产线别、客户料号")
    String code;

    @ApiModelProperty(name="params",value = "执行函数参数")
    String params;
}
