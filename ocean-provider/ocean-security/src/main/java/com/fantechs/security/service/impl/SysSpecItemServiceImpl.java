package com.fantechs.security.service.impl;


import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.dto.security.SysMenuInListDTO;
import com.fantechs.common.base.entity.security.SysSpecItem;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.entity.security.history.SysHtSpecItem;
import com.fantechs.common.base.entity.security.search.SearchSysSpecItem;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.JsonUtils;
import com.fantechs.common.base.utils.RedisUtil;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.security.mapper.SysHtSpecItemMapper;
import com.fantechs.security.mapper.SysSpecItemMapper;
import com.fantechs.security.service.SysSpecItemService;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SysSpecItemServiceImpl extends BaseService<SysSpecItem> implements SysSpecItemService {
    // 菜单缓存redis的key
    private static String MENU_REDIS_KEY = "MENU_REDIS_KEY";

    @Resource
    private SysSpecItemMapper sysSpecItemMapper;
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private SysHtSpecItemMapper sysHtSpecItemMapper;

    @Override
    public List<SysSpecItem> findList(SearchSysSpecItem searchSysSpecItem) {
        Object specItemList =redisUtil.get("specItemList");
        if (StringUtils.isNotEmpty(specItemList) && (StringUtils.isEmpty(searchSysSpecItem.getIfHotData()) || searchSysSpecItem.getIfHotData() == 1)) {
            List<SysSpecItem>  specItemList1 = JsonUtils.jsonToList(specItemList.toString(),SysSpecItem.class);
            List<Long> menuIds = new ArrayList<>();
            if (StringUtils.isNotEmpty(searchSysSpecItem.getMenuId())) {
                /*Object menuList = redisUtil.get(MENU_REDIS_KEY);
                if(ObjectUtil.isNull(menuList)){
                    if (!redisUtil.hasKey(MENU_REDIS_KEY)) {
                        menuList = sysMenuInfoService.findMenuList(ControllerUtil.dynamicCondition(
                                "parentId", "0",
                                "menuType", 2 + ""
                        ), null);
                        redisUtil.set(MENU_REDIS_KEY, JsonUtils.objectToJson(menuList));
                    }
                }
                List<SysMenuInListDTO> menuInListDTOS = JsonUtils.jsonToList(menuList.toString(), SysMenuInListDTO.class);
                SysMenuInListDTO dg = this.findNodes(menuInListDTOS, searchSysSpecItem.getMenuId());
                menuIds.add(dg.getSysMenuInfoDto().getMenuId());
                this.disassemblyTree(dg,menuIds);*/
                menuIds = searchSysSpecItem.getMenuIds();
            }

            for (int i = 0; i < specItemList1.size(); i++) {
                if (StringUtils.isNotEmpty(menuIds) && menuIds.size()>0 && !menuIds.contains(specItemList1.get(i).getMenuId())){
                    specItemList1.remove(i);
                    i--;
                }else if (StringUtils.isNotEmpty(searchSysSpecItem.getSpecCode()) && !specItemList1.get(i).getSpecCode().contains(searchSysSpecItem.getSpecCode())) {
                    specItemList1.remove(i);
                    i--;
                }else if (StringUtils.isNotEmpty(searchSysSpecItem.getSpecName()) && !specItemList1.get(i).getSpecName().contains(searchSysSpecItem.getSpecName())) {
                    specItemList1.remove(i);
                    i--;
                }else if (StringUtils.isNotEmpty(searchSysSpecItem.getPara()) && !specItemList1.get(i).getPara().contains(searchSysSpecItem.getPara())) {
                    specItemList1.remove(i);
                    i--;
                }
            }
            PageHelper.getLocalPage().setTotal(specItemList1.size());
            List<SysSpecItem> collect = specItemList1.stream().skip(searchSysSpecItem.getPageSize() * (searchSysSpecItem.getStartPage()-1)).limit(searchSysSpecItem.getPageSize()).collect(Collectors.toList());
            return collect;
        }
        return sysSpecItemMapper.findByMenuIdList(searchSysSpecItem);
    }

    public SysMenuInListDTO findNodes(List<SysMenuInListDTO> menuList, Long menuId){
        if (StringUtils.isEmpty(menuList)) {
            return null;
        }
        for (SysMenuInListDTO sysMenuInListDTO : menuList) {
            if (sysMenuInListDTO.getSysMenuInfoDto().getMenuId().equals(menuId)) {
                return sysMenuInListDTO;
            }else {
                SysMenuInListDTO nodes = this.findNodes(sysMenuInListDTO.getSysMenuinList(), menuId);
                if (StringUtils.isNotEmpty(nodes)) {
                    return nodes;
                }else {
                    this.findNodes(sysMenuInListDTO.getSysMenuinList(), menuId);
                }
            }
        }
        return null;
    }

    public void disassemblyTree(SysMenuInListDTO sysMenuInListDTO,List<Long> sysMenuinList){
        List<SysMenuInListDTO> sysMenuinList1 = sysMenuInListDTO.getSysMenuinList();
        if (StringUtils.isNotEmpty(sysMenuinList1)) {
            for (SysMenuInListDTO menuInListDTO : sysMenuinList1) {
                disassemblyTree(menuInListDTO,sysMenuinList);
                sysMenuinList.add(menuInListDTO.getSysMenuInfoDto().getMenuId());
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(SysSpecItem sysSpecItem) {
        SysUser currentUser =CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(SysSpecItem.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("specCode",sysSpecItem.getSpecCode())
                .orEqualTo("specName",sysSpecItem.getSpecName());
        List<SysSpecItem> sysSpecItems = sysSpecItemMapper.selectByExample(example);
        if(StringUtils.isNotEmpty(sysSpecItems)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        sysSpecItem.setCreateUserId(currentUser.getUserId());
        sysSpecItem.setCreateTime(new Date());
        sysSpecItem.setModifiedTime(new Date());
        sysSpecItem.setModifiedUserId(currentUser.getUserId());
        sysSpecItemMapper.insertUseGeneratedKeys(sysSpecItem);

        //新增配置项历史信息
        SysHtSpecItem sysHtSpecItem=new SysHtSpecItem();
        BeanUtils.copyProperties(sysSpecItem,sysHtSpecItem);
        int i = sysHtSpecItemMapper.insertSelective(sysHtSpecItem);
        this.updateHotData();
        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(SysSpecItem sysSpecItem) {
        SysUser currentUser =CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        Example example = new Example(SysSpecItem.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("specCode",sysSpecItem.getSpecCode());
        SysSpecItem specItem = sysSpecItemMapper.selectOneByExample(example);
        if(StringUtils.isNotEmpty(specItem)&&!specItem.getSpecId().equals(sysSpecItem.getSpecId())){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        sysSpecItem.setModifiedUserId(currentUser.getUserId());
        sysSpecItem.setModifiedTime(new Date());
        int i = sysSpecItemMapper.updateByPrimaryKeySelective(sysSpecItem);

        SysHtSpecItem sysHtSpecItem=new SysHtSpecItem();
        BeanUtils.copyProperties(sysSpecItem,sysHtSpecItem);
        sysHtSpecItemMapper.insertSelective(sysHtSpecItem);

        if (sysSpecItem.getSpecCode().equals("specialWord")){
            redisUtil.set("specialWord",sysSpecItem);
        }
        this.updateHotData();
        return i;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(String specIds) {
        List<SysHtSpecItem> list=new ArrayList<>();
        SysUser currentUser =CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        String[] idsArr = specIds.split(",");
        for (String specId : idsArr) {
            SysSpecItem sysSpecItem = sysSpecItemMapper.selectByPrimaryKey(specId);
            if(StringUtils.isEmpty(sysSpecItem)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            SysHtSpecItem sysHtSpecItem=new SysHtSpecItem();
            BeanUtils.copyProperties(sysSpecItem,sysHtSpecItem);
            sysHtSpecItem.setModifiedUserId(currentUser.getUserId());
            sysHtSpecItem.setModifiedTime(new Date());
            list.add(sysHtSpecItem);

        }
        sysHtSpecItemMapper.insertList(list);
        int i = sysSpecItemMapper.deleteByIds(specIds);
        this.updateHotData();
        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchSave(List<SysSpecItem> list){
        List<SysHtSpecItem> htList=new ArrayList<>();
        SysUser currentUser =CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        for (SysSpecItem sysSpecItem : list) {
            SysHtSpecItem sysHtSpecItem=new SysHtSpecItem();
            BeanUtils.copyProperties(sysSpecItem,sysHtSpecItem);
            sysHtSpecItem.setModifiedUserId(currentUser.getUserId());
            sysHtSpecItem.setModifiedTime(new Date());
            htList.add(sysHtSpecItem);
        }
        sysSpecItemMapper.insertList(list);
        this.updateHotData();
        return sysHtSpecItemMapper.insertList(htList);
    }

    private void updateHotData(){
        //更新热点数据
        SearchSysSpecItem searchSysSpecItem = new SearchSysSpecItem();
        List<SysSpecItem> byMenuIdList = sysSpecItemMapper.findByMenuIdList(searchSysSpecItem);
        if (StringUtils.isNotEmpty(byMenuIdList)) {
            redisUtil.set("specItemList", JsonUtils.objectToJson(byMenuIdList));
        }
    }

}
