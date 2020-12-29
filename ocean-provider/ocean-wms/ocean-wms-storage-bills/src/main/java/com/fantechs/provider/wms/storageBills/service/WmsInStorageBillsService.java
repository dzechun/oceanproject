package com.fantechs.provider.wms.storageBills.service;

import com.fantechs.common.base.dto.storage.SaveBillsDetDTO;
import com.fantechs.common.base.dto.storage.WmsInStorageBillsDTO;
import com.fantechs.common.base.entity.storage.WmsInStorageBills;
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
public interface WmsInStorageBillsService extends IService<WmsInStorageBills> {
    //===========================基础功能 start============================
    //动态条件查询对象列表
    List<WmsInStorageBills> selectAll(Map<String,Object> map);
    //动态条件模糊查询对象列表
    List<WmsInStorageBills> selectLikeAll(Map<String,Object> map);
    //动态条件查询对象
    WmsInStorageBills selectByMap(Map<String,Object> map);
    //动态条件删除对象
    int deleteByMap(Map<String,Object> map);
    //通过ID查找用户名称
    String selectUserName(Object id);
    //===========================基础功能 end============================
    //以特定过滤条件查询
    List<WmsInStorageBillsDTO> selectFilterAll(Map<String,Object> map);



    //===========================PDA start============================
    //以特定过滤条件查询
    List<WmsInStorageBillsDTO> pdaSelectFilterAll(Map<String,Object> map);
    //保存单据详情单
    WmsInStorageBills pdaSaveBilssDet(SaveBillsDetDTO saveBillsDet) throws SQLExecuteException;
    //===========================PDA end============================
}
