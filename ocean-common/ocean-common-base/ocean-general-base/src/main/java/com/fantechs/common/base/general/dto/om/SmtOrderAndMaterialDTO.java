package com.fantechs.common.base.general.dto.om;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.LinkedList;
import java.util.List;

/**
 * @Auther: bingo.ren
 * @Date: 2021/1/20 9:46
 * @Description: 订单及对应的子料
 * @Version: 1.0
 */
@Data
public class SmtOrderAndMaterialDTO {
    @ApiModelProperty(value = "订单",example = "订单")
    private SmtOrderDto smtOrderDto;
    @ApiModelProperty(value = "子料",example = "子料")
    private List<MesOrderMaterialDTO> mesOrderMaterialDTOList=new LinkedList<>();
}
