package com.fantechs.provider.om.service.ht;

import com.fantechs.common.base.general.entity.mes.pm.history.SmtHtOrder;
import com.fantechs.common.base.general.dto.mes.pm.history.SmtHtOrderDTO;
import com.fantechs.common.base.support.IService;
import java.util.List;
import java.util.Map;

/**
 * @Auther: bingo.ren
 * @Date: 2021年1月8日 16:22
 * @Description: 订单历史信息表接口
 * @Version: 1.0
 */
public interface SmtHtOrderService extends IService<SmtHtOrder>  {
    //===========================基础功能 start============================
    //动态条件查询对象列表
    List<SmtHtOrder> selectAll(Map<String,Object> map);
    //动态条件删除对象
    int deleteByMap(Map<String,Object> map);
    //===========================基础功能 end============================
    //以特定过滤条件查询
    List<SmtHtOrderDTO> selectFilterAll(Map<String,Object> map);

}
