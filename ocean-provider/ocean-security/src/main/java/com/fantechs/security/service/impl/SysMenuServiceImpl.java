package com.fantechs.security.service.impl;

import com.fantechs.common.base.dto.security.SysMenuInListDTO;
import com.fantechs.common.base.dto.security.SysMenuInfoDto;
import com.fantechs.common.base.entity.security.SysAuthRole;
import com.fantechs.common.base.entity.security.SysMenuInfo;
import com.fantechs.common.base.entity.security.SysRole;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.security.mapper.SysAuthRoleMapper;
import com.fantechs.security.mapper.SysMenuInfoMapper;
import com.fantechs.security.mapper.SysRoleMapper;
import com.fantechs.security.service.SysMenuinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by lfz on 2020/8/15.
 */
@Service
public class SysMenuServiceImpl  implements SysMenuinService {
    @Autowired
    private SysMenuInfoMapper sysMenuInfoMapper;
//    @Autowired
//    private SysHtMenuInfoMapper SysHtMenuInfoMapper;
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
                        if (rolesId.contains(tSysAuthforrole.getRoleId())) {
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
    public SysMenuInfoDto findById(Long id) {
        return sysMenuInfoMapper.selectById(id);
    }

    @Override
    public SysMenuInfoDto findByMap(Map<String, Object> map) {
        List<SysMenuInfoDto> sysMenuinfos = findAll(map,null);
        if(StringUtils.isNotEmpty(sysMenuinfos)&&sysMenuinfos.size()>0){
            return sysMenuinfos.get(0);
        }
        return null;
    }

//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public int insert(SysMenuinfo SysMenuinfo) throws Exception {
//        SysUser user=getCurrentUserInfo();
//        if(StringUtils.isEmpty(user)){
//            return   ConstantUtils.USER_TOKEN_ERROR;
//        }
//        Example example = new Example(SysMenuinfo.class);
//        Example.Criteria criteria = example.createCriteria();
//        criteria.andEqualTo("menuCode", SysMenuinfo.getMenuCode());
//
//        SysMenuinfo odlSysMenuinfo = SysMenuinfoMapper.selectOneByExample(example);
//
//        if(StringUtils.isNotEmpty(odlSysMenuinfo)){
//            return ConstantUtils.SYS_CODE_REPEAT;
//        }
//        SysMenuinfo.setMenuId(UUIDUtils.getUUID());
//        SysMenuinfo.setCreateUserId(user.getUserId());
//        SysMenuinfo.setCreateTime(new Date());
//        SysMenuinfo.setModifiedUserId(user.getUserId());
//        SysMenuinfo.setModifiedTime(new Date());
//        SysMenuinfo.setIsDelete(new Long(0));
//        SysMenuinfo.setParentId(StringUtils.isEmpty(SysMenuinfo.getParentId())?"0":SysMenuinfo.getParentId());
//
//        //插入历史
//        SysHtMenuInfo SysHtMenuInfo  = new SysHtMenuInfo();
//        BeanUtils.copyProperties(SysMenuinfo,SysHtMenuInfo);
//        SysHtMenuInfo.setHtMenuId(UUIDUtils.getUUID());
//        SysHtMenuInfoMapper.insertSelective(SysHtMenuInfo);
//
//        return SysMenuinfoMapper.insertSelective(SysMenuinfo);
//    }
//
//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public int deleteById(String id) throws Exception {
//        SysUser SysUser = getCurrentUserInfo();
//        if(StringUtils.isEmpty(SysUser)){
//            return   ConstantUtils.USER_TOKEN_ERROR;
//        }
//        SysMenuinfo SysMenuinfo = SysMenuinfoMapper.selectByPrimaryKey(id);
//        if(StringUtils.isEmpty(SysMenuinfo)){
//            return   ConstantUtils. SYS_DATA_NOT_EXISI;
//        }
//
//        //通过当前节点获取所有子节点
//        List<SysMenuinfo> list =getMenuList(SysMenuinfo);
//        List<SysHtMenuInfo> htList =  new LinkedList<>();
//        List<String> menuIds =  new LinkedList<>();
//        list.add(SysMenuinfo);
//        for(SysMenuinfo menuinfo : list ){
//            SysHtMenuInfo SysHtMenuInfo  = new SysHtMenuInfo();
//            BeanUtils.copyProperties(menuinfo,SysHtMenuInfo);
//            SysHtMenuInfo.setHtMenuId(UUIDUtils.getUUID());
//            SysHtMenuInfo.setCreateUserId(SysUser.getUserId());
//            SysHtMenuInfo.setCreateTime(new Date());
//            SysHtMenuInfo.setModifiedTime(new Date());
//            SysHtMenuInfo.setModifiedUserId(SysUser.getUserId());
//            htList.add(SysHtMenuInfo);
//            menuIds.add(menuinfo.getMenuId());
//        }
//        if(StringUtils.isNotEmpty(htList)){
//            //批量插入历史
//            SysHtMenuInfoMapper.addBatch(htList);
//            //删除当前节点和所有子节点菜单
//            SysAuthRoleMapper.delBatchByMenuIds(menuIds);
//        }
//
//                //批量删除当前菜单和子节点
//        return  SysMenuinfoMapper.delBatchByIds(menuIds);
//    }
//
//    @Override
//    public int deleteByIds(List<String> ids) throws Exception {
//        SysUser SysUser = getCurrentUserInfo();
//        if(StringUtils.isEmpty(SysUser)){
//            return   ConstantUtils.USER_TOKEN_ERROR;
//        }
//        List<String> menuIds =  new LinkedList<>();
//        List<SysHtMenuInfo> htList =  new LinkedList<>();
//
//        for(String id : ids){
//            SysMenuinfo SysMenuinfo = SysMenuinfoMapper.selectByPrimaryKey(id);
//            if(StringUtils.isEmpty(SysMenuinfo)){
//                return   ConstantUtils. SYS_DATA_NOT_EXISI;
//            }
//            //通过当前节点获取所有子节点
//            List<SysMenuinfo> list =getMenuList(SysMenuinfo);
//
//
//            list.add(SysMenuinfo);
//            for(SysMenuinfo menuinfo : list ){
//                SysHtMenuInfo SysHtMenuInfo  = new SysHtMenuInfo();
//                BeanUtils.copyProperties(menuinfo,SysHtMenuInfo);
//                SysHtMenuInfo.setHtMenuId(UUIDUtils.getUUID());
//                SysHtMenuInfo.setCreateUserId(SysUser.getUserId());
//                SysHtMenuInfo.setCreateTime(new Date());
//                SysHtMenuInfo.setModifiedTime(new Date());
//                SysHtMenuInfo.setModifiedUserId(SysUser.getUserId());
//                htList.add(SysHtMenuInfo);
//                menuIds.add(menuinfo.getMenuId());
//            }
//        }
//        if(StringUtils.isNotEmpty(htList)){
//            //批量插入历史
//            SysHtMenuInfoMapper.addBatch(htList);
//            //删除当前节点和所有子节点菜单
//            SysAuthRoleMapper.delBatchByMenuIds(menuIds);
//        }
//        //批量删除当前菜单和子节点
//        return  SysMenuinfoMapper.delBatchByIds(menuIds);
//    }

//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public int updateById(SysMenuinfo SysMenuinfo) {
//        SysUser SysUser = getCurrentUserInfo();
//        if(StringUtils.isEmpty(SysUser)){
//            return   ConstantUtils.USER_TOKEN_ERROR;
//        }
//
//        Example example = new Example(SysMenuinfo.class);
//        Example.Criteria criteria = example.createCriteria();
//        criteria.andEqualTo("menuCode", SysMenuinfo.getMenuCode());
//
//        SysMenuinfo odlSysMenuinfo = SysMenuinfoMapper.selectOneByExample(example);
//
//        if(StringUtils.isNotEmpty(odlSysMenuinfo)&&!odlSysMenuinfo.getMenuId().equals(SysMenuinfo.getMenuId())){
//            return ConstantUtils.SYS_CODE_REPEAT;
//        }
//
//        SysMenuinfo.setModifiedUserId(getCurrentUserInfo().getUserId());
//
//        //插入历史表
//        SysHtMenuInfo SysHtMenuInfo  = new SysHtMenuInfo();
//        BeanUtils.copyProperties(SysMenuinfo,SysHtMenuInfo);
//        SysHtMenuInfo.setHtMenuId(UUIDUtils.getUUID());
//        SysHtMenuInfo.setModifiedUserId(SysUser.getUserId());
//        SysHtMenuInfo.setCreateUserId(SysUser.getUserId());
//        SysHtMenuInfo.setCreateTime(new Date());
//        SysHtMenuInfo.setModifiedTime(new Date());
//
//        SysHtMenuInfoMapper.insertSelective(SysHtMenuInfo);
//
//        return SysMenuinfoMapper.updateByPrimaryKeySelective(SysMenuinfo);
//    }

    @Override
    public List<SysMenuInListDTO> findMenuList(Map<String, Object> map, List<String> roleIds) {
        //获取所有的父级目录
        List<SysMenuInfoDto> menuinfos = findAll(map,roleIds);
        List<SysMenuInListDTO> sysMenuInListDTO = null;
        if(StringUtils.isNotEmpty(menuinfos)){
            sysMenuInListDTO =new LinkedList<>();
            for (SysMenuInfoDto menuinfo : menuinfos) {
                SysMenuInListDTO tSysMenuinfoListDTO = new SysMenuInListDTO();
                tSysMenuinfoListDTO.setSysMenuInfoDto(menuinfo);
                sysMenuInListDTO.add(tSysMenuinfoListDTO);
                traverseFolder(tSysMenuinfoListDTO,sysMenuInListDTO,roleIds);

            }
        }
        return sysMenuInListDTO;
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
