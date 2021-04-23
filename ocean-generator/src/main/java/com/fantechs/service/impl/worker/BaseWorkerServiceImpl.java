package com.fantechs.service.impl.worker;

import com.fantechs.dao.mapper.BaseWorkerMapper;
import com.fantechs.model.BaseWorker;
import com.fantechs.service.BaseWorkerService;
import org.springframework.stereotype.Service;

/**
 *
 * Created by leifengzhi on 2021/04/23.
 */
@Service
public class BaseWorkerServiceImpl extends BaseService<BaseWorker> implements BaseWorkerService {

    @Resource
    private BaseWorkerMapper baseWorkerMapper;

    @Override
    public List<BaseWorkerDto> findList(Map<String, Object> map) {
        return baseWorkerMapper.findList(map);
    }
}
