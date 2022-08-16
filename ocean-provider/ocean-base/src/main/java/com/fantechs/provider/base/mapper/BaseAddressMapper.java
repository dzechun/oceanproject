package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.dto.basic.BaseAddressDto;
import com.fantechs.common.base.general.entity.basic.BaseAddress;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface BaseAddressMapper extends MyMapper<BaseAddress> {

    List<BaseAddressDto> findList(Map<String, Object> map);

    List<BaseAddressDto> findBySupplierId(Long supplierId);

    List<Long> findDelete(@Param("list") List<BaseAddressDto> list, @Param("supplierId") Long supplierId);

    int batchUpdate(List<BaseAddress> list);

    List<BaseAddressDto> findAdd(Long supplierId);
}
