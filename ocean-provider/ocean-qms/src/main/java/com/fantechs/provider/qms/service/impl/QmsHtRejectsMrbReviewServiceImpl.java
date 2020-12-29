package com.fantechs.provider.qms.service.impl;

import com.fantechs.common.base.general.entity.qms.history.QmsHtRejectsMrbReview;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.qms.mapper.QmsHtRejectsMrbReviewMapper;
import com.fantechs.provider.qms.service.QmsHtRejectsMrbReviewService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2020/12/28.
 */
@Service
public class QmsHtRejectsMrbReviewServiceImpl extends BaseService<QmsHtRejectsMrbReview> implements QmsHtRejectsMrbReviewService {

    @Resource
    private QmsHtRejectsMrbReviewMapper qmsHtRejectsMrbReviewMapper;

    @Override
    public List<QmsHtRejectsMrbReview> findList(Map<String, Object> map) {
        return qmsHtRejectsMrbReviewMapper.findList(map);
    }
}
