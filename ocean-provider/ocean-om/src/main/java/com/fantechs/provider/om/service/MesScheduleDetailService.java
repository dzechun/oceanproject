package com.fantechs.provider.om.service;


import com.fantechs.common.base.general.dto.om.MesScheduleDetailDTO;
import com.fantechs.common.base.general.entity.om.MesScheduleDetail;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 * @Auther: bingo.ren
 * @Date: 2021年1月9日 17:10
 * @Description: 排产详情接口
 * @Version: 1.0
 */
public interface MesScheduleDetailService extends IService<MesScheduleDetail> {
    //===========================基础功能 start============================
     //动态条件查询对象列表
    List<MesScheduleDetail> selectAll(Map<String,Object> map);
    //动态条件删除对象
    int deleteByMap(Map<String,Object> map);
    //===========================基础功能 end============================
    //以特定过滤条件查询
    List<MesScheduleDetailDTO> selectFilterAll(Map<String,Object> map);

}
