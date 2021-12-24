package com.fantechs.mapper;

import com.fantechs.entity.QmsProcessModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @Author
 * @Date 2021/12/01
 */
@Mapper
public interface QmsProcessMapper {
    List<QmsProcessModel> findDevoteQtyList(Map<String, Object> map);

    List<QmsProcessModel> findNotGoodList(Map<String, Object> map);

}
