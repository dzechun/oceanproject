package com.fantechs.mapper;


import com.fantechs.dto.WorkOrderFinished;
import com.fantechs.entity.search.SearchWorkOrderFinished;

import java.util.List;

public interface WorkOrderFinishedMapper {

    List<WorkOrderFinished> findList(SearchWorkOrderFinished searchWorkOrderFinished);
}
