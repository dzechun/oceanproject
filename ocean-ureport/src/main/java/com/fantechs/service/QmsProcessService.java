package com.fantechs.service;

import com.fantechs.entity.QmsProcessModel;
import com.fantechs.entity.QmsProcessModelShow;
import com.fantechs.entity.QmsProcessPassRateModel;

import java.util.List;
import java.util.Map;

/**
 * @Author
 * @Date 2021/12/01
 */
public interface QmsProcessService {
    List<QmsProcessModel> findDevoteQtyList(Map<String, Object> map);

    List<QmsProcessModel> findNotGoodList(Map<String, Object> map);

    List<QmsProcessModelShow> findList();

    List<QmsProcessModelShow> findProcessRateList();

    List<QmsProcessModel> findProcessQtyList(Map<String, Object> map);

    List<QmsProcessModel> findProcessNotGoodQtyList(Map<String, Object> map);

    List<QmsProcessPassRateModel> findProcessPassRateList();
}
