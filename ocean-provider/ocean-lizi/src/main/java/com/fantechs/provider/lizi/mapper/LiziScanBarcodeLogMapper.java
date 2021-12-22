package com.fantechs.provider.lizi.mapper;

import com.fantechs.common.base.mybatis.MyMapper;
import com.fantechs.provider.lizi.entity.LiziScanBarcodeLog;
import com.fantechs.provider.lizi.entity.dto.LiziScanBarcodeLogDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface LiziScanBarcodeLogMapper extends MyMapper<LiziScanBarcodeLog> {
    List<LiziScanBarcodeLogDto> findList(Map<String,Object> map);
}