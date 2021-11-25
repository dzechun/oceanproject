package com.fantechs.mapper;

import com.fantechs.common.base.general.entity.ureport.BaseSupplierInfo;
import com.fantechs.common.base.general.entity.ureport.SrmAsnOrder;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @Author jbb
 * @Date 2021/9/26
 */
@Mapper
public interface AsnOrderFildUreportMapper extends MyMapper<SrmAsnOrder> {
    List<SrmAsnOrder> findList(Map<String, Object> map);
}
