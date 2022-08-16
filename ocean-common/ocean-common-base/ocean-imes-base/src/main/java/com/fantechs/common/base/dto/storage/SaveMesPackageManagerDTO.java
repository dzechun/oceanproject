package com.fantechs.common.base.dto.storage;

import com.fantechs.common.base.entity.storage.MesPackageManager;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Auther: bingo.ren
 * @Date: 2021/1/7 17:48
 * @Description: 保存父级及子级
 * @Version: 1.0
 */
@Data
public class SaveMesPackageManagerDTO {
    @ApiModelProperty(value = "父级",example = "父级")
    private MesPackageManager mesPackageManager;
    @ApiModelProperty(value = "子级",example = "子级")
    private List<MesPackageManager> mesPackageManagerList;
    @ApiModelProperty("同工单数量叠加")
    private ManagerList managerList;
}
