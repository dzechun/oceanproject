package com.fantechs.service.impl;

import com.fantechs.dto.WorkOrderFinished;
import com.fantechs.entity.search.SearchWorkOrderFinished;
import com.fantechs.mapper.WorkOrderFinishedMapper;
import com.fantechs.service.WorkOrderFinishedService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class WorkOrderFinishedServiceImpl implements WorkOrderFinishedService {

    @Resource
    WorkOrderFinishedMapper workOrderFinishedMapper;

    @Override
    public List<WorkOrderFinished> findList(SearchWorkOrderFinished searchWorkOrderFinished) {
        return workOrderFinishedMapper.findList(searchWorkOrderFinished);
    }
}
