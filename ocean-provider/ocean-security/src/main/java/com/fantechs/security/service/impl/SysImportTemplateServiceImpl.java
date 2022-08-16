package com.fantechs.security.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.dto.security.SysMenuInListDTO;
import com.fantechs.common.base.entity.security.SysImportTemplate;
import com.fantechs.common.base.entity.security.SysSpecItem;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.JsonUtils;
import com.fantechs.common.base.utils.RedisUtil;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.security.mapper.SysImportTemplateMapper;
import com.fantechs.security.service.SysImportTemplateService;
import com.fantechs.security.service.SysMenuInfoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/10/13.
 */
@Service
public class SysImportTemplateServiceImpl extends BaseService<SysImportTemplate> implements SysImportTemplateService {

    // 菜单缓存redis的key
    private static String MENU_REDIS_KEY = "MENU_REDIS_KEY";

    @Resource
    private SysImportTemplateMapper sysImportTemplateMapper;
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private SysMenuInfoService sysMenuInfoService;


    @Override
    public List<SysImportTemplate> findList(Map<String, Object> map) {
        List<Long> menuIds = new ArrayList<>();
        if (StringUtils.isNotEmpty(map.get("menuId"))) {
            Object menuList = redisUtil.get(MENU_REDIS_KEY);
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
            SysMenuInListDTO dg = this.findNodes(menuInListDTOS, Long.parseLong(map.get("menuId").toString()));
            menuIds.add(dg.getSysMenuInfoDto().getMenuId());
            this.disassemblyTree(dg,menuIds);
            map.put("menuIds",menuIds);
        }

        return sysImportTemplateMapper.findList(map);
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
    public int save(SysImportTemplate record) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();

        Example example = new Example(SysImportTemplate.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("importTemplateCode",record.getImportTemplateCode());
        SysImportTemplate sysImportTemplate = sysImportTemplateMapper.selectOneByExample(example);
        if(StringUtils.isNotEmpty(sysImportTemplate)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        record.setCreateUserId(currentUser.getUserId());
        record.setCreateTime(new Date());
        record.setModifiedTime(new Date());
        record.setModifiedUserId(currentUser.getUserId());
        int i = sysImportTemplateMapper.insertSelective(record);

        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(SysImportTemplate entity) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();

        Example example = new Example(SysImportTemplate.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("importTemplateCode",entity.getImportTemplateCode())
                .andNotEqualTo("importTemplateId",entity.getImportTemplateId());
        SysImportTemplate sysImportTemplate = sysImportTemplateMapper.selectOneByExample(example);
        if(StringUtils.isNotEmpty(sysImportTemplate)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        entity.setModifiedTime(new Date());
        entity.setModifiedUserId(currentUser.getUserId());
        int i = sysImportTemplateMapper.updateByPrimaryKeySelective(entity);

        return i;
    }

}
