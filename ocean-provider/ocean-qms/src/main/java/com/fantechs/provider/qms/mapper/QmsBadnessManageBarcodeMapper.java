package com.fantechs.provider.qms.mapper;

import com.fantechs.common.base.general.entity.qms.QmsBadnessManageBarcode;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface QmsBadnessManageBarcodeMapper extends MyMapper<QmsBadnessManageBarcode> {
    List<QmsBadnessManageBarcode> findList(Map<String, Object> map);
}