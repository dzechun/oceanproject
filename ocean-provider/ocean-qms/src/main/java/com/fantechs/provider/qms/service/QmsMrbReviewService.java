package com.fantechs.provider.qms.service;

import com.fantechs.common.base.general.dto.qms.QmsMrbReviewDto;
import com.fantechs.common.base.general.entity.qms.QmsMrbReview;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2020/12/24.
 */

public interface QmsMrbReviewService extends IService<QmsMrbReview> {

    List<QmsMrbReviewDto> findList(Map<String, Object> map);
}
