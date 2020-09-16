package com.fantechs.security.service;


import com.fantechs.common.base.dto.security.SysMenuInListDTO;
import com.fantechs.common.base.dto.security.SysMenuInfoDto;
import com.fantechs.common.base.entity.security.SysMenuInfo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by lfz on 2020/8/15.
 */
@Service
public interface SysMenuinService {
    //动态条件查询对象列表
    List<SysMenuInfoDto> findAll(Map<String, Object> map, List<String> rolesId);

    //ID查询对象
    SysMenuInfoDto findById(Long id);

    //动态条件查询对象
    SysMenuInfoDto findByMap(Map<String, Object> map);

//    //添加对象
//    int insert(SysMenuInfo SysMenuinfo) throws Exception;
//
//    //ID删除对象
//    int deleteById(String id) throws Exception;
//
//    //ID删除对象
//    int deleteByIds(List<String> ids) throws Exception;
//    //ID更新对象
//    int updateById(SysMenuInfo SysMenuinfo);

    List<SysMenuInListDTO> findMenuList(Map<String, Object> map, List<String> roleIds);

}