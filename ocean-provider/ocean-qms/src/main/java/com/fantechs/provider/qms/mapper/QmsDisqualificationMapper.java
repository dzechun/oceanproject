package com.fantechs.provider.qms.mapper;

import com.fantechs.common.base.general.entity.qms.QmsDisqualification;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface QmsDisqualificationMapper extends MyMapper<QmsDisqualification> {
    List<QmsDisqualification> findByCheckoutId(@Param("checkoutId") Long checkoutId,@Param("checkoutType")Byte checkoutType);
}
