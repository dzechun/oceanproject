package com.fantechs.provider.wms.storageBills.service.history;

import com.fantechs.common.base.entity.apply.history.WmsInHtStorageBillsDet;
import com.fantechs.common.base.dto.apply.history.WmsInHtStorageBillsDetDTO;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 * @Auther: bingo.ren
 * @Date: 2020年12月25日 14:01
 * @Description: 履历仓库清单详情表接口
 * @Version: 1.0
 */
public interface WmsInHtStorageBillsDetService extends IService<WmsInHtStorageBillsDet> {
    //===========================基础功能 start============================
    //动态条件查询对象列表
    List<WmsInHtStorageBillsDet> selectAll(Map<String, Object> map);

    //动态条件模糊查询对象列表
    List<WmsInHtStorageBillsDet> selectLikeAll(Map<String, Object> map);

    //动态条件查询对象
    WmsInHtStorageBillsDet selectByMap(Map<String, Object> map);

    //动态条件删除对象
    int deleteByMap(Map<String, Object> map);

    //通过ID查找用户名称
    String selectUserName(Object id);

    //===========================基础功能 end============================
    //以特定过滤条件查询
    List<WmsInHtStorageBillsDetDTO> selectFilterAll(Long storageBillsId);

}
