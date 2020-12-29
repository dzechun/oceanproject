package com.fantechs.provider.qms.mapper;

import com.fantechs.common.base.general.entity.qms.history.QmsHtRejectsMrbReview;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface QmsHtRejectsMrbReviewMapper extends MyMapper<QmsHtRejectsMrbReview> {
    List<QmsHtRejectsMrbReview> findList(Map<String, Object> map);
}
