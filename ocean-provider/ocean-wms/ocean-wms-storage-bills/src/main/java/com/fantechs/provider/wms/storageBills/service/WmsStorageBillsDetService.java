package com.fantechs.provider.wms.storageBills.service;

import com.fantechs.common.base.dto.storage.WmsStorageBillsDetDTO;
import com.fantechs.common.base.entity.storage.WmsStorageBills;
import com.fantechs.common.base.entity.storage.WmsStorageBillsDet;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 * @Auther: bingo.ren
 * @Date: 2020年12月17日 17:20
 * @Description: 仓库清单详情表接口
 * @Version: 1.0
 */
public interface WmsStorageBillsDetService extends IService<WmsStorageBillsDet> {
    //===========================基础功能 start============================
    //动态条件查询对象列表
    List<WmsStorageBillsDet> selectAll(Map<String,Object> map);
    //动态条件模糊查询对象列表
    List<WmsStorageBillsDet> selectLikeAll(Map<String,Object> map);
    //动态条件查询对象
    WmsStorageBillsDet selectByMap(Map<String,Object> map);
    //动态条件删除对象
    int deleteByMap(Map<String,Object> map);
    //通过ID查找用户名称
    String selectUserName(Object id);
    //===========================基础功能 end============================
    //通过仓库清单ID找到所有对应的物料信息,1、对应取仓库信息 2、对应取储位信息
    List<WmsStorageBillsDetDTO> selectDTOByBillId(Long billId);
}