package com.fantechs.provider.wms.in.service.history;

import com.fantechs.common.base.entity.basic.history.MesHtPackageManager;
import com.fantechs.common.base.dto.basic.history.MesHtPackageManagerDTO;
import com.fantechs.common.base.support.IService;
import java.util.List;
import java.util.Map;

/**
 * @Auther: bingo.ren
 * @Date: 2021年1月9日 14:04
 * @Description: 包装管理履历表接口
 * @Version: 1.0
 */
public interface MesHtPackageManagerService extends IService<MesHtPackageManager>  {
    //===========================基础功能 start============================
     //动态条件查询对象列表
    List<MesHtPackageManager> selectAll(Map<String,Object> map);
    //动态条件删除对象
    int deleteByMap(Map<String,Object> map);
    //===========================基础功能 end============================
    //以特定过滤条件查询
    List<MesHtPackageManagerDTO> selectFilterAll(Map<String,Object> map);

}
