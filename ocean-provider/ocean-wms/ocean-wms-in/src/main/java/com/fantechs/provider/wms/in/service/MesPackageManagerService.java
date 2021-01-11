package com.fantechs.provider.wms.in.service;

import com.fantechs.common.base.dto.storage.SaveMesPackageManagerDTO;
import com.fantechs.common.base.entity.storage.MesPackageManager;
import com.fantechs.common.base.dto.storage.MesPackageManagerDTO;
import com.fantechs.common.base.support.IService;
import java.util.List;
import java.util.Map;

/**
 * @Auther: bingo.ren
 * @Date: 2021年1月7日 16:38
 * @Description: 包装管理接口
 * @Version: 1.0
 */
public interface MesPackageManagerService extends IService<MesPackageManager>  {
    //===========================基础功能 start============================
    //动态条件查询对象列表
    List<MesPackageManager> selectAll(Map<String,Object> map);
    //动态条件模糊查询对象列表
    List<MesPackageManager> selectLikeAll(Map<String,Object> map);
    //动态条件查询对象
    MesPackageManager selectByMap(Map<String,Object> map);
    //动态条件删除对象
    int deleteByMap(Map<String,Object> map);
    //通过ID查找用户名称
    String selectUserName(Object id);
    //===========================基础功能 end============================
    //以特定过滤条件查询
    List<MesPackageManagerDTO> selectFilterAll(Map<String,Object> map);
    //保存父级及子级
    MesPackageManager saveChildren(SaveMesPackageManagerDTO saveMesPackageManagerDTO);
    //补打条码
    int printCode(Long packageManagerId);
}
