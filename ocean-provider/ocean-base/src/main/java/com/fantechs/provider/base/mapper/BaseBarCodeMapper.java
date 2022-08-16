package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.dto.basic.BaseBarCodeDto;
import com.fantechs.common.base.general.dto.basic.BaseBarCodeWorkDto;
import com.fantechs.common.base.general.entity.basic.BaseBarCode;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseBarCode;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface BaseBarCodeMapper extends MyMapper<BaseBarCode> {
    List<BaseBarCodeDto> findList(Map<String, Object> map);

    BaseBarCodeWorkDto sel_work_order(SearchBaseBarCode searchBaseBarCode);

    String selMaxCode(@Param("workOrderId")Long workOrderId);

    List<BaseBarCodeDto> reprintList(@Param("workOrderId")String workOrderId);
}