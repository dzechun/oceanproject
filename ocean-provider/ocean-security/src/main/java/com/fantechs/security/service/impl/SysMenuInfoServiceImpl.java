package com.fantechs.security.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.dto.security.SysMenuInListDTO;
import com.fantechs.common.base.dto.security.SysMenuInfoDto;
import com.fantechs.common.base.dto.security.SysRoleDto;
import com.fantechs.common.base.entity.security.SysAuthRole;
import com.fantechs.common.base.entity.security.SysMenuInfo;
import com.fantechs.common.base.entity.security.SysRole;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.entity.security.history.SysHtMenuInfo;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.security.mapper.SysAuthRoleMapper;
import com.fantechs.security.mapper.SysHtMenuInfoMapper;
import com.fantechs.security.mapper.SysMenuInfoMapper;
import com.fantechs.security.mapper.SysRoleMapper;
import com.fantechs.security.service.SysMenuInfoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by lfz on 2020/8/15.
 */
@Service
public class SysMenuInfoServiceImpl extends BaseService<SysMenuInfo> implements SysMenuInfoService {
    @Autowired
    private SysMenuInfoMapper sysMenuInfoMapper;
    @Autowired
    private SysHtMenuInfoMapper sysHtMenuInfoMapper;
    @Autowired
    private SysAuthRoleMapper sysAuthRoleMapper;
    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Override
    public List<SysMenuInfoDto> findAll(Map<String, Object> map, List<String> rolesId) {
        map.put("orderNum","asc");
        List<SysMenuInfoDto> tSysMenuinfos = sysMenuInfoMapper.findList(map);
        if (tSysMenuinfos != null) {
            List<SysMenuInfoDto> removeObject = new LinkedList<>();
            for (int i = 0; i < tSysMenuinfos.size(); i++) {
                SysMenuInfoDto tSysMenuinfo = tSysMenuinfos.get(i);
                //---取菜单对应的所有角色
                Example example1 = new Example(SysAuthRole.class);
                Example.Criteria criteria1 = example1.createCriteria();
                criteria1.andEqualTo("menuId", tSysMenuinfo.getMenuId());
                List<SysAuthRole> tSysAuthforroles = sysAuthRoleMapper.selectByExample(example1);

                if (StringUtils.isNotEmpty(rolesId)) {
                    //---判断用户角色是否属于当前菜单
                    boolean isR = false;
                    for (SysAuthRole tSysAuthforrole : tSysAuthforroles) {
                        if (rolesId.contains(tSysAuthforrole.getRoleId().toString())) {
                            isR = true;
                            break;
                        }
                    }
                    if (!isR) {
                        removeObject.add(tSysMenuinfo);
                        continue;
                    }
                }

                List<SysRole> tSysRoles = new LinkedList<>();
                if (tSysAuthforroles != null) {
                    for (SysAuthRole tSysAuthforrole : tSysAuthforroles) {
                        SysRole tSysRole = sysRoleMapper.selectByPrimaryKey(tSysAuthforrole.getRoleId());

                        if (StringUtils.isEmpty(tSysRole)||(StringUtils.isNotEmpty(tSysRole.getStatus()) && tSysRole.getStatus() == 0)) {
                            continue;
                        }
                        if(StringUtils.isNotEmpty(tSysRole)){
                            tSysRoles.add(tSysRole);
                        }
                    }
                }
                if(StringUtils.isNotEmpty(tSysRoles)){
                    tSysMenuinfo.setRoles(tSysRoles);
                }

            }
            //---删除没有对应权限的菜单
            if (removeObject.size() > 0) {
                for (SysMenuInfo tSysMenuinfo : removeObject) {
                    tSysMenuinfos.remove(tSysMenuinfo);
                }
            }
        }
        return tSysMenuinfos;
    }

    @Override
    public SysMenuInfoDto findByMap(Map<String, Object> map) {
        List<SysMenuInfoDto> sysMenuinfos = findAll(map,null);
        if(StringUtils.isNotEmpty(sysMenuinfos)&&sysMenuinfos.size()>0){
            return sysMenuinfos.get(0);
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(SysMenuInfo sysMenuInfo){
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(SysMenuInfo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("menuCode", sysMenuInfo.getMenuCode());

        SysMenuInfo odlSysMenuinfo = sysMenuInfoMapper.selectOneByExample(example);

        if(StringUtils.isNotEmpty(odlSysMenuinfo)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }
        sysMenuInfo.setCreateUserId(currentUser.getUserId());
        sysMenuInfo.setCreateTime(new Date());
        sysMenuInfo.setModifiedUserId(currentUser.getUserId());
        sysMenuInfo.setModifiedTime(new Date());
        sysMenuInfo.setIsDelete((byte) 1);
        sysMenuInfo.setParentId(null == sysMenuInfo.getParentId()? 0 : sysMenuInfo.getParentId());
        int i = sysMenuInfoMapper.insertUseGeneratedKeys(sysMenuInfo);

        //插入历史
        SysHtMenuInfo SysHtMenuInfo  = new SysHtMenuInfo();
        BeanUtils.copyProperties(sysMenuInfo,SysHtMenuInfo);
        sysHtMenuInfoMapper.insertSelective(SysHtMenuInfo);

        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteById(Long id){
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        SysMenuInfo sysMenuInfo = sysMenuInfoMapper.selectByPrimaryKey(id);
        if(StringUtils.isEmpty(sysMenuInfo)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012003);
        }

        //通过当前节点获取所有子节点
        List<SysMenuInfo> list =getMenuList(sysMenuInfo);
        List<SysHtMenuInfo> htList =  new LinkedList<>();
        List<Long> menuIds =  new LinkedList<>();
        list.add(sysMenuInfo);
        for(SysMenuInfo menuinfo : list ){
            SysHtMenuInfo SysHtMenuInfo  = new SysHtMenuInfo();
            BeanUtils.copyProperties(menuinfo,SysHtMenuInfo);
            SysHtMenuInfo.setCreateUserId(currentUser.getUserId());
            SysHtMenuInfo.setCreateTime(new Date());
            SysHtMenuInfo.setModifiedTime(new Date());
            SysHtMenuInfo.setModifiedUserId(currentUser.getUserId());
            htList.add(SysHtMenuInfo);
            menuIds.add(menuinfo.getMenuId());
        }
        if(StringUtils.isNotEmpty(htList)){
            //批量插入历史
            sysHtMenuInfoMapper.insertList(htList);
            //删除当前节点和所有子节点菜单
            sysAuthRoleMapper.delBatchByMenuIds(menuIds);
        }

                //批量删除当前菜单和子节点
        return  sysMenuInfoMapper.delBatchByIds(menuIds);
    }

    @Override
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        List<Long> menuIds =  new LinkedList<>();
        List<SysHtMenuInfo> htList =  new LinkedList<>();

        String[] idsArr = ids.split(",");
        for(String id : idsArr){
            SysMenuInfo sysMenuInfo = sysMenuInfoMapper.selectByPrimaryKey(id);
            if(StringUtils.isEmpty(sysMenuInfo)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            //通过当前节点获取所有子节点
            List<SysMenuInfo> list =getMenuList(sysMenuInfo);


            list.add(sysMenuInfo);
            for(SysMenuInfo menuinfo : list ){
                SysHtMenuInfo SysHtMenuInfo  = new SysHtMenuInfo();
                BeanUtils.copyProperties(menuinfo,SysHtMenuInfo);
                SysHtMenuInfo.setCreateUserId(user.getUserId());
                SysHtMenuInfo.setCreateTime(new Date());
                SysHtMenuInfo.setModifiedTime(new Date());
                SysHtMenuInfo.setModifiedUserId(user.getUserId());
                htList.add(SysHtMenuInfo);
                menuIds.add(menuinfo.getMenuId());
            }
        }
        if(StringUtils.isNotEmpty(htList)){
            //批量插入历史
            sysHtMenuInfoMapper.insertList(htList);
            //删除当前节点和所有子节点菜单
            sysAuthRoleMapper.delBatchByMenuIds(menuIds);
        }
        //批量删除当前菜单和子节点
        return  sysMenuInfoMapper.delBatchByIds(menuIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(SysMenuInfo sysMenuInfo) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
            //return ErrorCodeEnum.UAC10011039.getCode();
        }

        Example example = new Example(SysMenuInfo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("menuCode", sysMenuInfo.getMenuCode());

        SysMenuInfo odlSysMenuinfo = sysMenuInfoMapper.selectOneByExample(example);

        if(StringUtils.isNotEmpty(odlSysMenuinfo)&&!odlSysMenuinfo.getMenuId().equals(sysMenuInfo.getMenuId())){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        sysMenuInfo.setModifiedUserId(currentUser.getUserId());

        //插入历史表
        SysHtMenuInfo SysHtMenuInfo  = new SysHtMenuInfo();
        BeanUtils.copyProperties(sysMenuInfo,SysHtMenuInfo);
        SysHtMenuInfo.setModifiedUserId(currentUser.getUserId());
        SysHtMenuInfo.setModifiedTime(new Date());

        sysHtMenuInfoMapper.insertSelective(SysHtMenuInfo);

        return sysMenuInfoMapper.updateByPrimaryKeySelective(sysMenuInfo);
    }

    @Override
    public List<SysMenuInListDTO> findMenuList(Map<String, Object> map, List<String> roleIds) {

        //获取所有的父级目录
        List<SysMenuInfoDto> menuinfos = findAll(map,roleIds);
        List<SysMenuInListDTO> sysMenuInListDTO = null;
        //获取当前登录所属角色所有的子菜单
        List<SysMenuInfoDto> list = sysMenuInfoMapper.findByUsreId(roleIds,map.get("menuType"));

        List<SysRoleDto> menuRoles = sysRoleMapper.findMenuRoles();
        Map<Long, List<SysRoleDto>> roleMap = menuRoles.stream().collect(Collectors.groupingBy(SysRoleDto::getMenuId));

        if(StringUtils.isNotEmpty(menuinfos)){
            sysMenuInListDTO =new LinkedList<>();
            for (SysMenuInfoDto menuinfo : menuinfos) {
                SysMenuInListDTO tSysMenuinfoListDTO = new SysMenuInListDTO();
                tSysMenuinfoListDTO.setSysMenuInfoDto(menuinfo);
                sysMenuInListDTO.add(tSysMenuinfoListDTO);
//                traverseFolder(tSysMenuinfoListDTO,sysMenuInListDTO,roleIds);
                buildMenu(tSysMenuinfoListDTO,list,roleMap);
            }
        }
        return sysMenuInListDTO;
    }
    private int buildMenu (SysMenuInListDTO tSysMenuinfoListDTO,List<SysMenuInfoDto> menuList,Map<Long, List<SysRoleDto>> roleMap) {
        List<SysMenuInListDTO> tSysMenuinfoListDTOList =new LinkedList<>();
        int count = 0;
        for(int i=0;i<menuList.size();) {
            if (tSysMenuinfoListDTO.getSysMenuInfoDto().getMenuId().equals(menuList.get(i).getParentId())){
                count++;
                SysMenuInListDTO tSysMenuinfoListDTO1 = new SysMenuInListDTO();
                SysMenuInfoDto remove = menuList.remove(i);
                List<SysRoleDto> roleDtos = roleMap.remove(remove.getMenuId());
                if (StringUtils.isNotEmpty(roleDtos)){
                    remove.getRoles().addAll(roleDtos);
                }
                tSysMenuinfoListDTO1.setSysMenuInfoDto(remove);
                tSysMenuinfoListDTOList.add(tSysMenuinfoListDTO1);
                tSysMenuinfoListDTO.setSysMenuinList(tSysMenuinfoListDTOList);
                if (remove.getIsMenu() != null && remove.getIsMenu() != 2){
                    int totalCount = buildMenu(tSysMenuinfoListDTO1, menuList,roleMap);
                    count += totalCount;
                    if (totalCount > i){
                        i=0;
                        continue;
                    }
                    i -= totalCount;
                }
            }else {
                i++;
            }
        }
        return count;
    }

    @Override
    public SysMenuInfoDto findById(Long id) {
        return sysMenuInfoMapper.selectById(id);
    }

    private void traverseFolder(SysMenuInListDTO tSysMenuinfoListDTO,List<SysMenuInListDTO> tSysMenuinfoListDTOList,List<String> roleIds){
        List<SysMenuInfoDto> menuinfos = findAll(ControllerUtil.dynamicCondition("parentId", tSysMenuinfoListDTO.getSysMenuInfoDto().getMenuId()),roleIds);
        if(StringUtils.isNotEmpty(menuinfos)){
            List<SysMenuInListDTO> tSysMenuinfoListDTOList1 =new LinkedList<>();
            for (SysMenuInfoDto tSysMenuinfo : menuinfos) {
                SysMenuInListDTO tSysMenuinfoListDTO1 = new SysMenuInListDTO();
                tSysMenuinfoListDTO1.setSysMenuInfoDto(tSysMenuinfo);
                tSysMenuinfoListDTOList1.add(tSysMenuinfoListDTO1);
                tSysMenuinfoListDTO.setSysMenuinList(tSysMenuinfoListDTOList1);
                traverseFolder(tSysMenuinfoListDTO1,tSysMenuinfoListDTOList1,roleIds);
            }
        }
    }

    private  List<SysMenuInfo> getMenuList(SysMenuInfo SysMenuinfo){
        Example example = new Example(SysMenuInfo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("parentId", SysMenuinfo.getMenuId());
        List<SysMenuInfo> getMenuList= new LinkedList<>();
        //通过当前节点查找所有下面节点
        List<SysMenuInfo> list= sysMenuInfoMapper.selectByExample(example);
        for(SysMenuInfo delSysMenuinfo:list){
            getMenuList(delSysMenuinfo);
        }
        getMenuList.addAll(list);

        return getMenuList;
    }
}
