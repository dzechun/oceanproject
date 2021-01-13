package com.fantechs.provider.wms.in.service;

import com.fantechs.common.base.dto.storage.WmsInStorageBillsDetDTO;
import com.fantechs.common.base.entity.storage.WmsInStorageBillsDet;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 * @Auther: bingo.ren
 * @Date: 2020年12月17日 17:20
 * @Description: 仓库清单详情表接口
 * @Version: 1.0
 */
public interface WmsInStorageBillsDetService extends IService<WmsInStorageBillsDet> {
    //===========================基础功能 start============================
    //动态条件查询对象列表
    List<WmsInStorageBillsDet> selectAll(Map<String,Object> map);
    //动态条件模糊查询对象列表
    List<WmsInStorageBillsDet> selectLikeAll(Map<String,Object> map);
    //动态条件查询对象
    WmsInStorageBillsDet selectByMap(Map<String,Object> map);
    //动态条件删除对象
    int deleteByMap(Map<String,Object> map);
    //通过ID查找用户名称
    String selectUserName(Object id);
    //===========================基础功能 end============================
    //通过仓库清单ID找到所有对应的物料信息
    List<WmsInStorageBillsDetDTO> selectFilterAll(Long storageBillsId);
}
