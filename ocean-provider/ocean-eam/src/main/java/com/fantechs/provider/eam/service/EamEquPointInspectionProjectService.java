package com.fantechs.provider.eam.service;

import com.fantechs.common.base.general.dto.eam.EamEquPointInspectionProjectDto;
import com.fantechs.common.base.general.entity.eam.EamEquPointInspectionProject;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by leifengzhi on 2021/08/20.
 */

public interface EamEquPointInspectionProjectService extends IService<EamEquPointInspectionProject> {
    List<EamEquPointInspectionProjectDto> findList(Map<String, Object> map);

    List<EamEquPointInspectionProjectDto> findHtList(Map<String, Object> map);

    /**
     * 新增点检项目以及点检项目事项
     * @param eamEquPointInspectionProject
     * @return
     */
    int save(EamEquPointInspectionProject eamEquPointInspectionProject);

    /**
     * 修改点检项目以及点检项目事项
     * @param eamEquPointInspectionProject
     * @return
     */
    int update(EamEquPointInspectionProject eamEquPointInspectionProject);

    /**
     * 修改点检项目状态
     * @param eamEquPointInspectionProject
     * @return
     */
    int updateStatus(EamEquPointInspectionProject eamEquPointInspectionProject);

    /**
     * 删除点检项目以及关联的点检项目事项
     * @param ids
     * @return
     */
    int batchDelete(String ids);
}
