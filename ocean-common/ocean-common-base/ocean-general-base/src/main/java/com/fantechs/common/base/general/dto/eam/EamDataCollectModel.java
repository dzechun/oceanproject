package com.fantechs.common.base.general.dto.eam;

import com.fantechs.common.base.general.entity.eam.EamDataCollect;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
public class EamDataCollectModel  implements Serializable {

    List<String> tableName;

    List<String> collectDate;
}
