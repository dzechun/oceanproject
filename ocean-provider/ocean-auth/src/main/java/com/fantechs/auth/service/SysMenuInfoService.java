package com.fantechs.auth.service;


import com.fantechs.common.base.dto.security.SysMenuInListDTO;
import com.fantechs.common.base.dto.security.SysMenuInfoDto;
import com.fantechs.common.base.entity.security.SysMenuInfo;
import com.fantechs.common.base.support.IService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by lfz on 2020/8/15.
 */
@Service
public interface SysMenuInfoService  extends IService<SysMenuInfo>{
    //动态条件查询对象列表
    List<SysMenuInfoDto> findAll(Map<String, Object> map, List<String> rolesId);

    //动态条件查询对象
    SysMenuInfoDto findByMap(Map<String, Object> map);

    List<SysMenuInListDTO> findMenuList(Map<String, Object> map, List<String> roleIds);

    //ID查询对象
    SysMenuInfoDto findById(Long id);

    //ID删除对象
    int deleteById(Long id);

    List<Long> getMenu(Long menuId);

}
