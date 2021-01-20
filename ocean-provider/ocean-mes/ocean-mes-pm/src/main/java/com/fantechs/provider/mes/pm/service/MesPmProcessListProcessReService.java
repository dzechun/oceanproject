package com.fantechs.provider.mes.pm.service;

import com.fantechs.common.base.general.dto.mes.pm.SaveProcessListProcessReDTO;
import com.fantechs.common.base.general.entity.mes.pm.MesPmProcessListProcessRe;
import com.fantechs.common.base.general.dto.mes.pm.MesPmProcessListProcessReDTO;
import com.fantechs.common.base.support.IService;
import java.util.List;
import java.util.Map;

/**
 * @Auther: bingo.ren
 * @Date: 2021年1月19日 20:18
 * @Description: 流程单工序退回表接口
 * @Version: 1.0
 */
public interface MesPmProcessListProcessReService extends IService<MesPmProcessListProcessRe>  {
    //===========================基础功能 start============================
     //动态条件查询对象列表
    List<MesPmProcessListProcessRe> selectAll(Map<String,Object> map);
    //动态条件删除对象
    int deleteByMap(Map<String,Object> map);
    //===========================基础功能 end============================
    //以特定过滤条件查询
    List<MesPmProcessListProcessReDTO> selectFilterAll(Map<String,Object> map);
    //报工退回
    int save(SaveProcessListProcessReDTO saveProcessListProcessReDTO);
}
