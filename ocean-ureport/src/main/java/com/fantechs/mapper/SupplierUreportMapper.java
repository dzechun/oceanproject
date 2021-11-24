package com.fantechs.mapper;

import com.fantechs.common.base.mybatis.MyMapper;
import com.fantechs.common.base.general.entity.ureport.BaseSupplierInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @Author lzw
 * @Date 2021/9/26
 */
@Mapper
public interface SupplierUreportMapper extends MyMapper<BaseSupplierInfo> {
    List<BaseSupplierInfo> findList(Map<String, Object> map);
}
