package com.fantechs.provider.imes.basic.mapper;

import com.fantechs.common.base.dto.basic.SmtAddressDto;
import com.fantechs.common.base.entity.basic.SmtAddress;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface SmtAddressMapper extends MyMapper<SmtAddress> {

    List<SmtAddressDto> findList(Map<String, Object> map);

    List<SmtAddressDto> findBySupplierId(Long supplierId);

    List<Long> findDelete(@Param("list") List<SmtAddressDto> list,@Param("supplierId") Long supplierId);

    int batchUpdate(List<SmtAddress> list);

    List<SmtAddressDto> findAdd(Long supplierId);
}
