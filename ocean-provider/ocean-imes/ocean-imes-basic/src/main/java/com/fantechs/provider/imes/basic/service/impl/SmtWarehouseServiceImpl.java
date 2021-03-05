package com.fantechs.provider.imes.basic.service.impl;


import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.basic.SmtStorage;
import com.fantechs.common.base.entity.basic.SmtWarehouse;
import com.fantechs.common.base.entity.basic.SmtWarehouseArea;
import com.fantechs.common.base.entity.basic.SmtWarehousePersonnel;
import com.fantechs.common.base.entity.basic.history.SmtHtWarehouse;
import com.fantechs.common.base.entity.basic.search.SearchSmtWarehouse;
import com.fantechs.common.base.entity.basic.search.SearchSmtWarehousePersonnel;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.imes.basic.mapper.*;
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

    @Resource
    private SmtWarehouseAreaMapper smtWarehouseAreaMapper;

    @Resource
    private SmtWarehousePersonnelMapper smtWarehousePersonnelMapper;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(SmtWarehouse smtWarehouse) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(SmtWarehouse.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("warehouseCode",smtWarehouse.getWarehouseCode());
        List<SmtWarehouse> smtWarehouses = smtWarehouseMapper.selectByExample(example);
        if(StringUtils.isNotEmpty(smtWarehouses)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        smtWarehouse.setCreateUserId(currentUser.getUserId());
        smtWarehouse.setCreateTime(new Date());
        smtWarehouse.setModifiedUserId(currentUser.getUserId());
        smtWarehouse.setModifiedTime(new Date());
        smtWarehouseMapper.insertUseGeneratedKeys(smtWarehouse);

        //新增仓库历史信息
        SmtHtWarehouse smtHtWarehouse = new SmtHtWarehouse();
        BeanUtils.copyProperties(smtWarehouse,smtHtWarehouse);
        int i = smtHtWarehouseMapper.insertSelective(smtHtWarehouse);

        //新增仓库人员关系
        List<SmtWarehousePersonnel> smtWarehousePersonnels = smtWarehouse.getSmtWarehousePersonnels();
        if (StringUtils.isNotEmpty(smtWarehousePersonnels)){
            for (SmtWarehousePersonnel smtWarehousePersonnel : smtWarehousePersonnels) {
                smtWarehousePersonnel.setWarehouseId(smtWarehouse.getWarehouseId());
            }
            smtWarehousePersonnelMapper.insertList(smtWarehousePersonnels);
        }

        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(String ids) {
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

            //被仓库区域引用
            Example example = new Example(SmtWarehouseArea.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("warehouseId",warehouseId);
            List<SmtWarehouseArea> smtWarehouseAreas = smtWarehouseAreaMapper.selectByExample(example);
            if(StringUtils.isNotEmpty(smtWarehouseAreas)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012004);
            }

            //新增仓库历史信息
            SmtHtWarehouse smtHtWarehouse=new SmtHtWarehouse();
            BeanUtils.copyProperties(smtWarehouse,smtHtWarehouse);
            smtHtWarehouse.setModifiedUserId(currentUser.getUserId());
            smtHtWarehouse.setModifiedTime(new Date());
            list.add(smtHtWarehouse);

            //删除仓库人员关系
            Example example1 = new Example(SmtWarehousePersonnel.class);
            Example.Criteria criteria1 = example1.createCriteria();
            criteria1.andEqualTo("warehouseId",warehouseId);
            smtWarehousePersonnelMapper.deleteByExample(example1);
        }
        smtHtWarehouseMapper.insertList(list);

        return smtWarehouseMapper.deleteByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(SmtWarehouse smtWarehouse) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(SmtWarehouse.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("warehouseCode",smtWarehouse.getWarehouseCode())
                .andNotEqualTo("warehouseId",smtWarehouse.getWarehouseId());
        SmtWarehouse warehouse = smtWarehouseMapper.selectOneByExample(example);
        if(StringUtils.isNotEmpty(warehouse)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        smtWarehouse.setModifiedUserId(currentUser.getUserId());
        smtWarehouse.setModifiedTime(new Date());
        int i= smtWarehouseMapper.updateByPrimaryKeySelective(smtWarehouse);

        //删除原有绑定关系
        Example example1 = new Example(SmtWarehousePersonnel.class);
        Example.Criteria criteria1 = example1.createCriteria();
        criteria1.andEqualTo("warehouseId",smtWarehouse.getWarehouseId());
        smtWarehousePersonnelMapper.deleteByExample(example1);

        //新增绑定关系
        List<SmtWarehousePersonnel> smtWarehousePersonnels = smtWarehouse.getSmtWarehousePersonnels();
        if (StringUtils.isNotEmpty(smtWarehousePersonnels)){
            for (SmtWarehousePersonnel smtWarehousePersonnel : smtWarehousePersonnels) {
                smtWarehousePersonnel.setWarehouseId(smtWarehouse.getWarehouseId());
            }
            smtWarehousePersonnelMapper.insertList(smtWarehousePersonnels);
        }

        //新增仓库历史信息
        SmtHtWarehouse smtHtWarehouse=new SmtHtWarehouse();
        BeanUtils.copyProperties(smtWarehouse,smtHtWarehouse);
        smtHtWarehouseMapper.insertSelective(smtHtWarehouse);
        return i;
    }


    @Override
    public List<SmtWarehouse> findList(SearchSmtWarehouse searchSmtWarehouse) {
        List<SmtWarehouse> smtWarehouses = smtWarehouseMapper.findList(searchSmtWarehouse);
        if (StringUtils.isNotEmpty(smtWarehouses)){
            SearchSmtWarehousePersonnel searchSmtWarehousePersonnel = new SearchSmtWarehousePersonnel();

            for (SmtWarehouse smtWarehouse : smtWarehouses) {
                searchSmtWarehousePersonnel.setWarehouseId(smtWarehouse.getWarehouseId());
                List<SmtWarehousePersonnel> list = smtWarehousePersonnelMapper.findList(searchSmtWarehousePersonnel);
                if (StringUtils.isNotEmpty(list)){
                    smtWarehouse.setSmtWarehousePersonnels(list);
                }
            }
        }
        return smtWarehouses;
    }

    @Override
    public int batchUpdateByCode(List<SmtWarehouse> smtWarehouses) {
        if (StringUtils.isNotEmpty(smtWarehouses)){
            return smtWarehouseMapper.batchUpdateByCode(smtWarehouses);
        }else {
            return 0;
        }
    }

    @Override
    public int insertList(List<SmtWarehouse> smtWarehouses) {
        return smtWarehouseMapper.insertList(smtWarehouses);
    }
}
