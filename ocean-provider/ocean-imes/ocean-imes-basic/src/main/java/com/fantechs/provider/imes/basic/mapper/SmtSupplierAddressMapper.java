package com.fantechs.provider.imes.basic.mapper;

import com.fantechs.common.base.entity.basic.SmtSupplierAddress;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface SmtSupplierAddressMapper extends MyMapper<SmtSupplierAddress> {
    List<SmtSupplierAddress> findList(Map<String, Object> map);

    int updateIfDefault(@Param("addressId") Long addressId,@Param("supplierId") Long supplierId);
}
