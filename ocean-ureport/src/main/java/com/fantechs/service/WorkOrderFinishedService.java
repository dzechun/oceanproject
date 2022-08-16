package com.fantechs.service;


import com.fantechs.dto.WorkOrderFinished;
import com.fantechs.entity.search.SearchWorkOrderFinished;

import java.util.List;

public interface WorkOrderFinishedService {

    List<WorkOrderFinished> findList(SearchWorkOrderFinished searchWorkOrderFinished);
}
