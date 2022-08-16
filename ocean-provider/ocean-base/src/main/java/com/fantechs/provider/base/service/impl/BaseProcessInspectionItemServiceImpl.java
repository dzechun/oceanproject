package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.basic.*;
import com.fantechs.common.base.general.entity.basic.history.BaseHtProcessInspectionItem;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.BaseHtProcessInspectionItemMapper;
import com.fantechs.provider.base.mapper.BaseProcessInspectionItemItemMapper;
import com.fantechs.provider.base.mapper.BaseProcessInspectionItemMapper;
import com.fantechs.provider.base.service.BaseProcessInspectionItemService;
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
 * Created by leifengzhi on 2021/06/02.
 */
@Service
public class BaseProcessInspectionItemServiceImpl extends BaseService<BaseProcessInspectionItem> implements BaseProcessInspectionItemService {

    @Resource
    private BaseProcessInspectionItemMapper baseProcessInspectionItemMapper;
    @Resource
    private BaseHtProcessInspectionItemMapper baseHtProcessInspectionItemMapper;
    @Resource
    private BaseProcessInspectionItemItemMapper baseProcessInspectionItemItemMapper;

    @Override
    public List<BaseProcessInspectionItem> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId", user.getOrganizationId());
        return baseProcessInspectionItemMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int save(BaseProcessInspectionItem baseProcessInspectionItem) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        //判断编码是否重复
        Example example = new Example(BaseProcessInspectionItem.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("orgId", user.getOrganizationId());
        criteria.andEqualTo("processInspectionItemCode", baseProcessInspectionItem.getProcessInspectionItemCode());
        BaseProcessInspectionItem baseProcessInspectionItem1 = baseProcessInspectionItemMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(baseProcessInspectionItem1)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        //新增过程检验项目
        baseProcessInspectionItem.setCreateUserId(user.getUserId());
        baseProcessInspectionItem.setCreateTime(new Date());
        baseProcessInspectionItem.setModifiedUserId(user.getUserId());
        baseProcessInspectionItem.setModifiedTime(new Date());
        baseProcessInspectionItem.setStatus(StringUtils.isEmpty(baseProcessInspectionItem.getStatus())?1:baseProcessInspectionItem.getStatus());
        baseProcessInspectionItem.setOrgId(user.getOrganizationId());
        int i = baseProcessInspectionItemMapper.insertUseGeneratedKeys(baseProcessInspectionItem);

        //新增过程检验项目检验项
        List<BaseProcessInspectionItemItem> baseProcessInspectionItemItemList = baseProcessInspectionItem.getBaseProcessInspectionItemItemList();
        if(StringUtils.isNotEmpty(baseProcessInspectionItemItemList)){
            for (BaseProcessInspectionItemItem baseProcessInspectionItemItem:baseProcessInspectionItemItemList){
                baseProcessInspectionItemItem.setProcessInspectionItemId(baseProcessInspectionItem.getProcessInspectionItemId());
                baseProcessInspectionItemItem.setCreateUserId(user.getUserId());
                baseProcessInspectionItemItem.setCreateTime(new Date());
                baseProcessInspectionItemItem.setModifiedUserId(user.getUserId());
                baseProcessInspectionItemItem.setModifiedTime(new Date());
                baseProcessInspectionItemItem.setStatus(StringUtils.isEmpty(baseProcessInspectionItemItem.getStatus())?1:baseProcessInspectionItemItem.getStatus());
                baseProcessInspectionItemItem.setOrgId(user.getOrganizationId());
            }
            baseProcessInspectionItemItemMapper.insertList(baseProcessInspectionItemItemList);
        }

        //履历
        BaseHtProcessInspectionItem baseHtProcessInspectionItem = new BaseHtProcessInspectionItem();
        BeanUtils.copyProperties(baseProcessInspectionItem, baseHtProcessInspectionItem);
        baseHtProcessInspectionItemMapper.insertSelective(baseHtProcessInspectionItem);

        return i;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int update(BaseProcessInspectionItem baseProcessInspectionItem) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        //判断编码是否重复
        Example example = new Example(BaseProcessInspectionItem.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("orgId", user.getOrganizationId());
        criteria.andEqualTo("processInspectionItemCode", baseProcessInspectionItem.getProcessInspectionItemCode())
                .andNotEqualTo("processInspectionItemId", baseProcessInspectionItem.getProcessInspectionItemId());
        BaseProcessInspectionItem baseProcessInspectionItem1 = baseProcessInspectionItemMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(baseProcessInspectionItem1)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        //修改过程检验项目
        baseProcessInspectionItem.setModifiedUserId(user.getUserId());
        baseProcessInspectionItem.setModifiedTime(new Date());
        baseProcessInspectionItem.setOrgId(user.getOrganizationId());
        int i=baseProcessInspectionItemMapper.updateByPrimaryKeySelective(baseProcessInspectionItem);

        //原来有的检验项只更新
        ArrayList<Long> idList = new ArrayList<>();
        List<BaseProcessInspectionItemItem> baseProcessInspectionItemItemList = baseProcessInspectionItem.getBaseProcessInspectionItemItemList();
        if(StringUtils.isNotEmpty(baseProcessInspectionItemItemList)) {
            for (BaseProcessInspectionItemItem baseProcessInspectionItemItem : baseProcessInspectionItemItemList) {
                if (StringUtils.isNotEmpty(baseProcessInspectionItemItem.getProcessInspectionItemItemId())) {
                    baseProcessInspectionItemItemMapper.updateByPrimaryKeySelective(baseProcessInspectionItemItem);
                    idList.add(baseProcessInspectionItemItem.getProcessInspectionItemItemId());
                }
            }
        }

        //删除原有过程检验项目检验项
        Example example1 = new Example(BaseProcessInspectionItemItem.class);
        Example.Criteria criteria1 = example1.createCriteria();
        criteria1.andEqualTo("processInspectionItemId", baseProcessInspectionItem.getProcessInspectionItemId());
        if(idList.size()>0){
            criteria1.andNotIn("processInspectionItemItemId",idList);
        }
        baseProcessInspectionItemItemMapper.deleteByExample(example1);

        //新增过程检验项目检验项
        if(StringUtils.isNotEmpty(baseProcessInspectionItemItemList)){
            for (BaseProcessInspectionItemItem baseProcessInspectionItemItem:baseProcessInspectionItemItemList){
                if(idList.contains(baseProcessInspectionItemItem.getProcessInspectionItemItemId())){
                    continue;
                }
                baseProcessInspectionItemItem.setProcessInspectionItemId(baseProcessInspectionItem.getProcessInspectionItemId());
                baseProcessInspectionItemItem.setCreateUserId(user.getUserId());
                baseProcessInspectionItemItem.setCreateTime(new Date());
                baseProcessInspectionItemItem.setModifiedUserId(user.getUserId());
                baseProcessInspectionItemItem.setModifiedTime(new Date());
                baseProcessInspectionItemItem.setStatus(StringUtils.isEmpty(baseProcessInspectionItemItem.getStatus())?1:baseProcessInspectionItemItem.getStatus());
                baseProcessInspectionItemItem.setOrgId(user.getOrganizationId());
                baseProcessInspectionItemItemMapper.insertSelective(baseProcessInspectionItemItem);
            }
        }

        //履历
        BaseHtProcessInspectionItem baseHtProcessInspectionItem = new BaseHtProcessInspectionItem();
        BeanUtils.copyProperties(baseProcessInspectionItem, baseHtProcessInspectionItem);
        baseHtProcessInspectionItemMapper.insertSelective(baseHtProcessInspectionItem);

        return i;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        List<BaseHtProcessInspectionItem> list = new ArrayList<>();
        String[] idArry = ids.split(",");
        for (String id : idArry) {
            BaseProcessInspectionItem baseProcessInspectionItem = baseProcessInspectionItemMapper.selectByPrimaryKey(id);
            if(StringUtils.isEmpty(baseProcessInspectionItem)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            BaseHtProcessInspectionItem baseHtProcessInspectionItem = new BaseHtProcessInspectionItem();
            BeanUtils.copyProperties(baseProcessInspectionItem, baseHtProcessInspectionItem);
            list.add(baseHtProcessInspectionItem);

            //删除过程检验项目检验项
            Example example1 = new Example(BaseProcessInspectionItemItem.class);
            Example.Criteria criteria1 = example1.createCriteria();
            criteria1.andEqualTo("processInspectionItemId", baseProcessInspectionItem.getProcessInspectionItemId());
            baseProcessInspectionItemItemMapper.deleteByExample(example1);
        }

        //履历
        baseHtProcessInspectionItemMapper.insertList(list);

        return baseProcessInspectionItemMapper.deleteByIds(ids);
    }
}
