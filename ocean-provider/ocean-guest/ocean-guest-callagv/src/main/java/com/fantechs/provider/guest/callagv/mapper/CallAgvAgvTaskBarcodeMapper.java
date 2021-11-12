package com.fantechs.provider.guest.callagv.mapper;

import com.fantechs.common.base.general.entity.callagv.CallAgvAgvTaskBarcode;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface CallAgvAgvTaskBarcodeMapper extends MyMapper<CallAgvAgvTaskBarcode> {
    List<CallAgvAgvTaskBarcode> findList(Map<String, Object> map);
}