package com.fantechs.provider.qms.service;

import com.fantechs.common.base.general.dto.qms.QmsRejectsMrbReviewDto;
import com.fantechs.common.base.general.entity.qms.QmsRejectsMrbReview;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2020/12/28.
 */

public interface QmsRejectsMrbReviewService extends IService<QmsRejectsMrbReview> {

    List<QmsRejectsMrbReviewDto> findList(Map<String, Object> map);
}
