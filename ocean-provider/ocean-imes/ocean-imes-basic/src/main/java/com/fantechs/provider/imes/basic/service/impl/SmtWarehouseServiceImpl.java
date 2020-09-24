package com.fantechs.provider.imes.basic.service.impl;


import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.basic.SmtWarehouse;
import com.fantechs.common.base.entity.basic.history.SmtHtWarehouse;
import com.fantechs.common.base.entity.basic.search.SearchSmtWarehouse;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.imes.basic.mapper.SmtHtWarehouseMapper;
import com.fantechs.provider.imes.basic.mapper.SmtWarehouseMapper;
import com.fantechs.provider.imes.basic.service.SmtWarehouseService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * Created by wcz on 2020/09/23.
 */
@Service
public class SmtWarehouseServiceImpl extends BaseService<SmtWarehouse> implements SmtWarehouseService {

    @Resource
    private SmtWarehouseMapper smtWarehouseMapper;

    @Resource
    private SmtHtWarehouseMapper smtHtWarehouseMapper;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(SmtWarehouse smtWarehouse) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(SmtWarehouse.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("warehouseCode",smtWarehouse.getWarehouseCode());
        List<SmtWarehouse> smtWarehouses = smtWarehouseMapper.selectByExample(example);
        if(null!=smtWarehouses&&smtWarehouses.size()>0){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        smtWarehouse.setCreateUserId(currentUser.getUserId());
        smtWarehouse.setCreateTime(new Date());
         smtWarehouseMapper.insertUseGeneratedKeys(smtWarehouse);

        //新增仓库历史信息
        SmtHtWarehouse smtHtWarehouse=new SmtHtWarehouse();
        BeanUtils.copyProperties(smtWarehouse,smtHtWarehouse);
        int i = smtHtWarehouseMapper.insertSelective(smtHtWarehouse);
        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDel(String ids) {
        int i=0;
        List<SmtHtWarehouse> list=new ArrayList<>();

        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        String[] warehouseIds = ids.split(",");
        for (String warehouseId : warehouseIds) {
            SmtWarehouse smtWarehouse = smtWarehouseMapper.selectByPrimaryKey(Long.parseLong(warehouseId));
            if(StringUtils.isEmpty(smtWarehouse)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            //新增仓库历史信息
            SmtHtWarehouse smtHtWarehouse=new SmtHtWarehouse();
            BeanUtils.copyProperties(smtWarehouse,smtHtWarehouse);
            smtHtWarehouse.setModifiedUserId(currentUser.getUserId());
            smtHtWarehouse.setModifiedTime(new Date());
            list.add(smtHtWarehouse);
        }
        smtHtWarehouseMapper.insertList(list);

        return smtWarehouseMapper.deleteByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateById(SmtWarehouse smtWarehouse) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(SmtWarehouse.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("warehouseCode",smtWarehouse.getWarehouseCode());

        SmtWarehouse warehouse = smtWarehouseMapper.selectOneByExample(example);

        if(StringUtils.isNotEmpty(warehouse)&&!warehouse.getWarehouseId().equals(smtWarehouse.getWarehouseId())){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        smtWarehouse.setModifiedUserId(currentUser.getUserId());
        smtWarehouse.setModifiedTime(new Date());
        int i= smtWarehouseMapper.updateByPrimaryKeySelective(smtWarehouse);

        //新增仓库历史信息
        SmtHtWarehouse smtHtWarehouse=new SmtHtWarehouse();
        BeanUtils.copyProperties(smtWarehouse,smtHtWarehouse);
        smtHtWarehouse.setModifiedUserId(currentUser.getUserId());
        smtHtWarehouse.setModifiedTime(new Date());
        smtHtWarehouseMapper.insertSelective(smtHtWarehouse);
        return i;
    }

    @Override
    public SmtWarehouse selectByKey(Long id) {
        return smtWarehouseMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<SmtWarehouse> findList(SearchSmtWarehouse searchSmtWarehouse) {
        return smtWarehouseMapper.findList(searchSmtWarehouse);
    }
}
