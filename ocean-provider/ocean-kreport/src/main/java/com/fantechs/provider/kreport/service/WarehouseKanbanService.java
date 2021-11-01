package com.fantechs.provider.kreport.service;

import com.fantechs.common.base.general.entity.kreport.JobDocumentProgress;
import com.fantechs.common.base.general.entity.kreport.PersonnelPickingRanking;
import com.fantechs.common.base.general.entity.kreport.Warehouse;
import com.fantechs.common.base.general.entity.kreport.WarehouseKanban;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

public interface WarehouseKanbanService extends IService<WarehouseKanban> {
    WarehouseKanban findKanbanData(Map<String, Object> map);

    List<Warehouse> findWarehouse();

    List<PersonnelPickingRanking> findPersonnelPickingRankingList(Map<String, Object> map);

    List<JobDocumentProgress> findJobDocumentProgressList(Map<String, Object> map);

}
