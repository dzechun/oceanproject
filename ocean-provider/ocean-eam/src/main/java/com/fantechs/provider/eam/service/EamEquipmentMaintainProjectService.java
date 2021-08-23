package com.fantechs.provider.eam.service;

import com.fantechs.common.base.general.dto.eam.EamEquipmentMaintainProjectDto;
import com.fantechs.common.base.general.entity.eam.EamEquipmentMaintainProject;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by leifengzhi on 2021/08/21.
 */

public interface EamEquipmentMaintainProjectService extends IService<EamEquipmentMaintainProject> {
    List<EamEquipmentMaintainProjectDto> findList(Map<String, Object> map);
    List<EamEquipmentMaintainProjectDto> findHtList(Map<String, Object> map);

    /**
     * 新增保养项目以及保养项目事项
     * @param eamEquipmentMaintainProject
     * @return
     */
    int save(EamEquipmentMaintainProject eamEquipmentMaintainProject);

    /**
     * 修改保养项目以及保养项目事项
     * @param eamEquipmentMaintainProject
     * @return
     */
    int update(EamEquipmentMaintainProject eamEquipmentMaintainProject);

    /**
     * 修改保养项目状态
     * @param eamEquipmentMaintainProject
     * @return
     */
    int updateStatus(EamEquipmentMaintainProject eamEquipmentMaintainProject);

    /**
     * 删除保养项目以及关联的保养项目事项
     * @param ids
     * @return
     */
    int batchDelete(String ids);
}
