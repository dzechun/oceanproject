package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseInspectionItemDto;
import com.fantechs.common.base.general.entity.basic.BaseInspectionExemptedList;
import com.fantechs.common.base.general.entity.basic.BaseInspectionItem;
import com.fantechs.common.base.general.entity.basic.BaseProcessInspectionItem;
import com.fantechs.common.base.general.entity.basic.BaseProcessInspectionItemItem;
import com.fantechs.common.base.general.entity.basic.history.BaseHtInspectionExemptedList;
import com.fantechs.common.base.general.entity.basic.history.BaseHtInspectionItem;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseInspectionItem;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.BaseHtInspectionItemMapper;
import com.fantechs.provider.base.mapper.BaseInspectionItemMapper;
import com.fantechs.provider.base.service.BaseInspectionItemService;
import org.springframework.beans.BeanUtils;
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
 * Created by leifengzhi on 2021/06/03.
 */
@Service
public class BaseInspectionItemServiceImpl extends BaseService<BaseInspectionItem> implements BaseInspectionItemService {

    @Resource
    private BaseInspectionItemMapper baseInspectionItemMapper;
    @Resource
    private BaseHtInspectionItemMapper baseHtInspectionItemMapper;

    @Override
    public List<BaseInspectionItem> findList(Map<String, Object> map) {
        List<BaseInspectionItem> baseInspectionItemList = baseInspectionItemMapper.findList(map);
        SearchBaseInspectionItem searchBaseInspectionItem = new SearchBaseInspectionItem();

        for (BaseInspectionItem baseInspectionItem : baseInspectionItemList){
            searchBaseInspectionItem.setParentId(baseInspectionItem.getInspectionItemId());
            List<BaseInspectionItem> detList = baseInspectionItemMapper.findDetList(ControllerUtil.dynamicConditionByEntity(searchBaseInspectionItem));
            if (StringUtils.isNotEmpty(detList)){
                baseInspectionItem.setBaseInspectionItemDets(detList);
            }
        }

        return baseInspectionItemList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(BaseInspectionItem baseInspectionItem) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        //判断编码是否重复
        Example example = new Example(BaseInspectionItem.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("inspectionItemCode", baseInspectionItem.getInspectionItemCode())
                .andEqualTo("inspectionItemType",(byte)1);
        BaseInspectionItem baseInspectionItem1 = baseInspectionItemMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(baseInspectionItem1)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        //新增检验项目
        baseInspectionItem.setInspectionItemType(StringUtils.isEmpty(baseInspectionItem.getInspectionItemType())?(byte)1:baseInspectionItem.getInspectionItemType());
        baseInspectionItem.setCreateUserId(user.getUserId());
        baseInspectionItem.setCreateTime(new Date());
        baseInspectionItem.setModifiedUserId(user.getUserId());
        baseInspectionItem.setModifiedTime(new Date());
        baseInspectionItem.setStatus(StringUtils.isEmpty(baseInspectionItem.getStatus())?1: baseInspectionItem.getStatus());
        baseInspectionItem.setOrganizationId(user.getOrganizationId());
        int i = baseInspectionItemMapper.insertUseGeneratedKeys(baseInspectionItem);

        //新增检验项目明细
        List<BaseInspectionItem> baseInspectionItems = baseInspectionItem.getBaseInspectionItemDets();
        if(StringUtils.isNotEmpty(baseInspectionItems)){
            for (BaseInspectionItem baseInspectionItem2:baseInspectionItems){
                baseInspectionItem2.setParentId(baseInspectionItem.getInspectionItemId());
                baseInspectionItem2.setInspectionItemType(StringUtils.isEmpty(baseInspectionItem2.getInspectionItemType())?(byte)2:baseInspectionItem2.getInspectionItemType());
                baseInspectionItem2.setCreateUserId(user.getUserId());
                baseInspectionItem2.setCreateTime(new Date());
                baseInspectionItem2.setModifiedUserId(user.getUserId());
                baseInspectionItem2.setModifiedTime(new Date());
                baseInspectionItem2.setStatus(StringUtils.isEmpty(baseInspectionItem2.getStatus())?1:baseInspectionItem2.getStatus());
                baseInspectionItem2.setOrganizationId(user.getOrganizationId());
            }
            baseInspectionItemMapper.insertList(baseInspectionItems);
        }

        BaseHtInspectionItem baseHtInspectionItem = new BaseHtInspectionItem();
        BeanUtils.copyProperties(baseInspectionItem, baseHtInspectionItem);
        baseHtInspectionItemMapper.insert(baseHtInspectionItem);

        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(BaseInspectionItem baseInspectionItem) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        //判断编码是否重复
        Example example = new Example(BaseInspectionItem.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("inspectionItemCode", baseInspectionItem.getInspectionItemCode())
                .andEqualTo("inspectionItemType",(byte)1)
                .andNotEqualTo("inspectionItemId",baseInspectionItem.getInspectionItemId());
        BaseInspectionItem baseInspectionItem1 = baseInspectionItemMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(baseInspectionItem1)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        //修改检验项目
        baseInspectionItem.setInspectionItemType(StringUtils.isEmpty(baseInspectionItem.getInspectionItemType())?(byte)1:baseInspectionItem.getInspectionItemType());
        baseInspectionItem.setModifiedTime(new Date());
        baseInspectionItem.setModifiedUserId(user.getUserId());
        baseInspectionItem.setOrganizationId(user.getOrganizationId());
        int i = baseInspectionItemMapper.updateByPrimaryKeySelective(baseInspectionItem);

        //删除原有检验项目明细
        Example example1 = new Example(BaseInspectionItem.class);
        Example.Criteria criteria1 = example1.createCriteria();
        criteria1.andEqualTo("parentId", baseInspectionItem.getInspectionItemCode());
        baseInspectionItemMapper.deleteByExample(example1);

        //新增检验项目明细
        List<BaseInspectionItem> baseInspectionItems = baseInspectionItem.getBaseInspectionItemDets();
        if(StringUtils.isNotEmpty(baseInspectionItems)){
            for (BaseInspectionItem baseInspectionItem2:baseInspectionItems){
                baseInspectionItem2.setParentId(baseInspectionItem.getInspectionItemId());
                baseInspectionItem2.setInspectionItemType(StringUtils.isEmpty(baseInspectionItem2.getInspectionItemType())?(byte)2:baseInspectionItem2.getInspectionItemType());
                baseInspectionItem2.setCreateUserId(user.getUserId());
                baseInspectionItem2.setCreateTime(new Date());
                baseInspectionItem2.setModifiedUserId(user.getUserId());
                baseInspectionItem2.setModifiedTime(new Date());
                baseInspectionItem2.setStatus(StringUtils.isEmpty(baseInspectionItem2.getStatus())?1:baseInspectionItem2.getStatus());
                baseInspectionItem2.setOrganizationId(user.getOrganizationId());
            }
            baseInspectionItemMapper.insertList(baseInspectionItems);
        }

        BaseHtInspectionItem baseHtInspectionItem = new BaseHtInspectionItem();
        BeanUtils.copyProperties(baseInspectionItem, baseHtInspectionItem);
        baseHtInspectionItemMapper.insert(baseHtInspectionItem);

        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        List<BaseHtInspectionItem> list = new ArrayList<>();
        String[] idArry = ids.split(",");
        for (String id : idArry) {
            BaseInspectionItem baseInspectionItem = baseInspectionItemMapper.selectByPrimaryKey(id);
            if(StringUtils.isEmpty(baseInspectionItem)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            BaseHtInspectionItem baseHtInspectionItem = new BaseHtInspectionItem();
            BeanUtils.copyProperties(baseInspectionItem, baseHtInspectionItem);
            list.add(baseHtInspectionItem);

            //删除原有检验项目明细
            Example example1 = new Example(BaseInspectionItem.class);
            Example.Criteria criteria1 = example1.createCriteria();
            criteria1.andEqualTo("parentId", baseInspectionItem.getInspectionItemId());
            baseInspectionItemMapper.deleteByExample(example1);
        }

        baseHtInspectionItemMapper.insertList(list);

        return baseInspectionItemMapper.deleteByIds(ids);
    }
}
