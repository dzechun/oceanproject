package com.fantechs.provider.qms.service;

import com.fantechs.common.base.general.entity.qms.history.QmsHtRejectsMrbReview;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2020/12/28.
 */

public interface QmsHtRejectsMrbReviewService extends IService<QmsHtRejectsMrbReview> {

    List<QmsHtRejectsMrbReview> findList(Map<String, Object> map);
}
