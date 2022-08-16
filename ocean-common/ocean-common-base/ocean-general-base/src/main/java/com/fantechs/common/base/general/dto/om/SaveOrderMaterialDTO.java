package com.fantechs.common.base.general.dto.om;

import com.fantechs.common.base.general.entity.om.MesOrderMaterial;
import com.fantechs.common.base.general.entity.om.SmtOrder;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Auther: bingo.ren
 * @Date: 2021/1/7 9:51
 * @Description:
 * @Version: 1.0
 */
@Data
public class SaveOrderMaterialDTO {
     @ApiModelProperty(value = "订单信息",example = "订单信息")
     private SmtOrder smtOrder;
    @ApiModelProperty(value = "产品相关信息",example = "产品相关信息")
     private List<MesOrderMaterial> mesOrderMaterialList;
}
