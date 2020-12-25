package com.fantechs.provider.qms.service;

import com.fantechs.common.base.general.entity.qms.history.QmsHtMrbReview;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2020/12/24.
 */

public interface QmsHtMrbReviewService extends IService<QmsHtMrbReview> {

    List<QmsHtMrbReview> findHtList(Map<String, Object> dynamicConditionByEntity);
}
