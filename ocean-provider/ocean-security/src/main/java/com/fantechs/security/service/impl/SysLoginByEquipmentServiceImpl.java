package com.fantechs.security.service.impl;

import com.fantechs.common.base.dto.security.SysRoleDto;
import com.fantechs.common.base.entity.security.search.SearchSysRole;
import com.fantechs.common.base.general.dto.eam.EamEquipmentDto;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.eam.EamFeignApi;
import com.fantechs.provider.api.security.service.SecurityFeignApi;
import com.fantechs.security.mapper.SysRoleMapper;
import com.fantechs.security.service.SysLoginByEquipmentService;
import com.fantechs.security.service.SysUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 * Created by leifengzhi on 2021/07/09.
 */
@Service
public class SysLoginByEquipmentServiceImpl  implements SysLoginByEquipmentService {

    @Resource
    private SecurityFeignApi securityFeignApi;

    @Resource
    private SysUserService sysUserService;

    @Resource
    private SysRoleMapper sysRoleMapper;

    @Resource
    private EamFeignApi eamFeignApi;



    @Override
    public ResponseEntity eamLogin(String username, String password,Long orgId,String mac ,String type) {
        ResponseEntity responseEntity = null;
        //密码登录
        if("1".equals(type)) {
         //   if (StringUtils.isNotEmpty(sysRoleDtos)) {
                responseEntity = securityFeignApi.login(username, password,orgId,null);
         //   }
            //刷卡登录
        }else if("2".equals(type)){
        //    if (StringUtils.isNotEmpty(sysRoleDtos)) {
                responseEntity = securityFeignApi.login(username, "123456",orgId,type);
        //    }
        }

        //通过mac地址查询设备
        ResponseEntity<List<EamEquipmentDto>> list = eamFeignApi.findByMac(mac, orgId);
        if (StringUtils.isEmpty(list.getData())) {
            return ControllerUtil.returnFail("登录错误，未查询到设备信息", 1);
        }
        //获取当前用户角色
        SearchSysRole searchSysRole = new SearchSysRole();
        searchSysRole.setUserName(username);
        searchSysRole.setRoleName(list.getData().get(0).getProcessName());
        List<SysRoleDto> sysRoleDtos = sysRoleMapper.findByUserName(searchSysRole);
        if (StringUtils.isEmpty(sysRoleDtos)) {
            return ControllerUtil.returnFail("角色无登录权限，请在系统角色中进行配置", 1);
        }
        return responseEntity;
    }
}
