package com.fantechs.auth.service.impl;

import com.fantechs.common.base.dto.security.SysRoleDto;
import com.fantechs.common.base.entity.security.search.SearchSysRole;
import com.fantechs.common.base.general.dto.esop.EsopEquipmentDto;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.esop.EsopFeignApi;
import com.fantechs.provider.api.auth.service.AuthFeignApi;
import com.fantechs.auth.mapper.SysRoleMapper;
import com.fantechs.auth.service.SysLoginByEquipmentService;
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
    private AuthFeignApi securityFeignApi;

    @Resource
    private SysRoleMapper sysRoleMapper;

/*    @Resource
    private EamFeignApi eamFeignApi;*/
    @Resource
    private EsopFeignApi esopFeignApi;


    @Override
    public ResponseEntity eamLogin(String username, String password,Long orgId,String mac ,String type) {
        ResponseEntity responseEntity = null;
        //密码登录
        if("1".equals(type)) {
         //   if (StringUtils.isNotEmpty(sysRoleDtos)) {
                responseEntity = securityFeignApi.login(username, password,orgId,null,null);
         //   }
            //刷卡登录
        }else if("2".equals(type)){
        //    if (StringUtils.isNotEmpty(sysRoleDtos)) {
                responseEntity = securityFeignApi.login(username, "123456",orgId,type,null);
        //    }
        }

        //通过mac地址查询设备
        ResponseEntity<List<EsopEquipmentDto>> list = esopFeignApi.findByMac(mac, orgId);
        if (StringUtils.isEmpty(list.getData())) {
            return ControllerUtil.returnFail("登录错误，未查询到设备信息", 1);
        }
        //获取当前用户角色,根据产线进行登录
        if(!"admin".equals(username) && !"123456".equals(username)) {
            SearchSysRole searchSysRole = new SearchSysRole();
            searchSysRole.setUserName(username);
            searchSysRole.setRoleName(list.getData().get(0).getProLineName());
            List<SysRoleDto> sysRoleDtos = sysRoleMapper.findByUserName(searchSysRole);
            if (StringUtils.isEmpty(sysRoleDtos)) {
                return ControllerUtil.returnFail("角色无登录权限，请在系统角色中进行配置", 1);
            }
        }
        return responseEntity;
    }
}
