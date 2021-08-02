package com.fantechs.common.base.general.dto.eam;

import com.fantechs.common.base.general.entity.eam.EamEquipmentDataGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;
import java.util.List;

@Data
public class EamEquipmentDataGroupDto extends EamEquipmentDataGroup implements Serializable {
    /**
     * 分组参数
     */
    @Transient
    List<EamEquipmentDataGroupParamDto> eamEquipmentDataGroupParamDtos;

}
