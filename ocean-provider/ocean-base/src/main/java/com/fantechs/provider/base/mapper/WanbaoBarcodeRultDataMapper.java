package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.dto.basic.WanbaoBarcodeRultDataDto;
import com.fantechs.common.base.general.entity.basic.WanbaoBarcodeRultData;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface WanbaoBarcodeRultDataMapper extends MyMapper<WanbaoBarcodeRultData> {
    List<WanbaoBarcodeRultDataDto> findList(Map<String, Object> map);

    int batchUpdate(List<WanbaoBarcodeRultData> list);
}