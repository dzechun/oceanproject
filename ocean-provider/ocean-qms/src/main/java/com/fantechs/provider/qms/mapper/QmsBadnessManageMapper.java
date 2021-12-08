package com.fantechs.provider.qms.mapper;

import com.fantechs.common.base.general.entity.qms.QmsBadnessManage;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface QmsBadnessManageMapper extends MyMapper<QmsBadnessManage> {
    List<QmsBadnessManage> findList(Map<String, Object> map);
}