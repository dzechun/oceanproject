package com.fantechs.provider.qms.mapper;

import com.fantechs.common.base.general.dto.qms.QmsRejectsMrbReviewDto;
import com.fantechs.common.base.general.entity.qms.QmsMrbReview;
import com.fantechs.common.base.general.entity.qms.QmsRejectsMrbReview;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface QmsRejectsMrbReviewMapper extends MyMapper<QmsRejectsMrbReview> {

    List<QmsRejectsMrbReviewDto> findList(Map<String, Object> map);

    QmsRejectsMrbReview getMax();
}
