package com.fantechs.provider.wms.in.service.history;

import com.fantechs.common.base.entity.basic.history.WmsInHtStorageBills;
import com.fantechs.common.base.support.IService;
import com.fantechs.common.base.dto.basic.history.WmsInHtStorageBillsDTO;

import java.util.List;
import java.util.Map;

/**
 * @Auther: bingo.ren
 * @Date: 2020年12月25日 9:35
 * @Description: 履历仓库清单表接口
 * @Version: 1.0
 */
public interface WmsInHtStorageBillsService extends IService<WmsInHtStorageBills>  {
    //===========================基础功能 start============================
    //动态条件查询对象列表
    List<WmsInHtStorageBills> selectAll(Map<String,Object> map);
    //动态条件模糊查询对象列表
    List<WmsInHtStorageBills> selectLikeAll(Map<String,Object> map);
    //动态条件查询对象
    WmsInHtStorageBills selectByMap(Map<String,Object> map);
    //动态条件删除对象
    int deleteByMap(Map<String,Object> map);
    //通过ID查找用户名称
    String selectUserName(Object id);
    //===========================基础功能 end============================
    //以特定过滤条件查询
    List<WmsInHtStorageBillsDTO> selectFilterAll(Map<String,Object> map);

}
