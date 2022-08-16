package com.fantechs.provider.wanbao.api.mapper;

import com.fantechs.common.base.mybatis.MyMapper;
import com.fantechs.provider.wanbao.api.entity.MiddleProduct;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface MiddleProductMapper extends MyMapper<MiddleProduct> {


    /**
     * 万宝-产品条码信息查询
     * @return
     */
    List<MiddleProduct> findBarcodeData(Map<String, Object> map);

    /**
     * 插入中间库
     * @param middleProduct
     * @return
     */
    int save(MiddleProduct middleProduct);
}