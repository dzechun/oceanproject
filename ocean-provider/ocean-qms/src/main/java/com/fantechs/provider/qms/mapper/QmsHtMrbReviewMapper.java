package com.fantechs.provider.qms.mapper;

import com.fantechs.common.base.general.entity.qms.history.QmsHtMrbReview;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface QmsHtMrbReviewMapper extends MyMapper<QmsHtMrbReview> {
    List<QmsHtMrbReview> findHtList(Map<String, Object> map);
}
