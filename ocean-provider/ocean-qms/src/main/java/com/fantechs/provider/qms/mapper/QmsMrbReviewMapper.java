package com.fantechs.provider.qms.mapper;

import com.fantechs.common.base.general.dto.qms.QmsMrbReviewDto;
import com.fantechs.common.base.general.entity.qms.QmsMrbReview;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface QmsMrbReviewMapper extends MyMapper<QmsMrbReview> {

    List<QmsMrbReviewDto> findList(Map<String, Object> map);

    QmsMrbReview getMax();
}
