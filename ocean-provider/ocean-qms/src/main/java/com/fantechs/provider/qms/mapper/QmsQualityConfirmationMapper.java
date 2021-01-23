package com.fantechs.provider.qms.mapper;

import com.fantechs.common.base.general.dto.qms.QmsQualityConfirmationDto;
import com.fantechs.common.base.general.entity.qms.QmsQualityConfirmation;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface QmsQualityConfirmationMapper extends MyMapper<QmsQualityConfirmation> {
    List<QmsQualityConfirmationDto> findList(Map<String, Object> map);

    Integer updateQuantity(Map<String, Object> map);
}
