package com.fantechs.provider.om.service.ht;

import com.fantechs.common.base.entity.apply.history.MesHtOrderMaterial;
import com.fantechs.common.base.dto.apply.history.MesHtOrderMaterialDTO;
import com.fantechs.common.base.support.IService;
import java.util.List;
import java.util.Map;

/**
 * @Auther: bingo.ren
 * @Date: 2021年1月9日 11:51
 * @Description: 销售订单与物料历史接口
 * @Version: 1.0
 */
public interface MesHtOrderMaterialService extends IService<MesHtOrderMaterial>  {
    //===========================基础功能 start============================
     //动态条件查询对象列表
    List<MesHtOrderMaterial> selectAll(Map<String,Object> map);
    //动态条件删除对象
    int deleteByMap(Map<String,Object> map);
    //===========================基础功能 end============================
    //以特定过滤条件查询
    List<MesHtOrderMaterialDTO> selectFilterAll(Map<String,Object> map);

}
