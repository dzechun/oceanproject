package com.fantechs.provider.qms.service.impl;

import com.fantechs.common.base.general.entity.qms.history.QmsHtMrbReview;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.qms.mapper.QmsHtMrbReviewMapper;
import com.fantechs.provider.qms.service.QmsHtMrbReviewService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2020/12/24.
 */
@Service
public class QmsHtMrbReviewServiceImpl extends BaseService<QmsHtMrbReview> implements QmsHtMrbReviewService {

    @Resource
    private QmsHtMrbReviewMapper qmsHtMrbReviewMapper;

    @Override
    public List<QmsHtMrbReview> findHtList(Map<String, Object> map) {
        return qmsHtMrbReviewMapper.findHtList(map);
    }
}
