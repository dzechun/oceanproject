package com.fantechs.provider.wms.storageBills.service;

import com.fantechs.common.base.dto.storage.SaveBilssDet;
import com.fantechs.common.base.dto.storage.WmsStorageBillsDTO;
import com.fantechs.common.base.entity.storage.WmsStorageBills;
import com.fantechs.common.base.exception.SQLExecuteException;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 * @Auther: bingo.ren
 * @Date: 2020年12月17日 14:52
 * @Description: 仓库清单表接口
 * @Version: 1.0
 */
public interface WmsStorageBillsService extends IService<WmsStorageBills> {
    //===========================基础功能 start============================
    //动态条件查询对象列表
    List<WmsStorageBills> selectAll(Map<String,Object> map);
    //动态条件模糊查询对象列表
    List<WmsStorageBills> selectLikeAll(Map<String,Object> map);
    //动态条件查询对象
    WmsStorageBills selectByMap(Map<String,Object> map);
    //动态条件删除对象
    int deleteByMap(Map<String,Object> map);
    //通过ID查找用户名称
    String selectUserName(Object id);
    //===========================基础功能 end============================
    //以特定过滤条件查询
    List<WmsStorageBillsDTO> selectFilterAll(Map<String,Object> map);
    //保存单据详情单
    WmsStorageBills saveBilssDet(SaveBilssDet saveBilssDet) throws SQLExecuteException;
}
