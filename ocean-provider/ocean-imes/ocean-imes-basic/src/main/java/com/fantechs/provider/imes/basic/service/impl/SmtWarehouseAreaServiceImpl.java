package com.fantechs.provider.imes.basic.service.impl;


import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.dto.basic.SmtWarehouseAreaDto;
import com.fantechs.common.base.entity.basic.SmtStorage;
import com.fantechs.common.base.entity.basic.SmtWarehouse;
import com.fantechs.common.base.entity.basic.SmtWarehouseArea;
import com.fantechs.common.base.entity.basic.history.SmtHtWarehouseArea;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.imes.basic.mapper.SmtHtWarehouseAreaMapper;
import com.fantechs.provider.imes.basic.mapper.SmtStorageMapper;
import com.fantechs.provider.imes.basic.mapper.SmtWarehouseAreaMapper;
import com.fantechs.provider.imes.basic.mapper.SmtWarehouseMapper;
import com.fantechs.provider.imes.basic.service.SmtWarehouseAreaService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2020/09/23.
 */
@Service
public class SmtWarehouseAreaServiceImpl  extends BaseService<SmtWarehouseArea> implements SmtWarehouseAreaService {

    @Autowired
    private SmtWarehouseAreaMapper  smtWarehouseAreaMapper;
    @Autowired
    private SmtHtWarehouseAreaMapper smtHtWarehouseAreaMapper;
    @Autowired
    private SmtStorageMapper smtStorageMapper;


    @Override
    public List<SmtWarehouseAreaDto> findList(Map<String, Object> map) {
        return smtWarehouseAreaMapper.findList(map);
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(SmtWarehouseArea smtWarehouseArea) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        int i=0;
        Example example = new Example(SmtWarehouseArea.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("warehouseAreaCode",smtWarehouseArea.getWarehouseAreaCode());
        List<SmtWarehouseArea> smtWarehouseAreaList = smtWarehouseAreaMapper.selectByExample(example);
        if(StringUtils.isNotEmpty(smtWarehouseAreaList)){
             throw new BizErrorException (ErrorCodeEnum.OPT20012001);
        }
        smtWarehouseArea.setCreateUserId(currentUser.getUserId());
        smtWarehouseArea.setCreateTime(new Date());
        smtWarehouseArea.setStatus((byte) 1);
        smtWarehouseAreaMapper.insertUseGeneratedKeys(smtWarehouseArea);

        //新增历史记录
        SmtHtWarehouseArea smtHtWarehouseArea=new SmtHtWarehouseArea();
        BeanUtils.copyProperties(smtWarehouseArea,smtHtWarehouseArea);
        i=smtHtWarehouseAreaMapper.insertSelective(smtHtWarehouseArea);
        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(SmtWarehouseArea smtWarehouseArea) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        int i=0;
        smtWarehouseArea.setModifiedUserId(currentUser.getUserId());
        smtWarehouseArea.setModifiedTime(new Date());
         i= smtWarehouseAreaMapper.updateByPrimaryKeySelective(smtWarehouseArea);

        //新增历史记录
        SmtHtWarehouseArea smtHtWarehouseArea=new SmtHtWarehouseArea();
        BeanUtils.copyProperties(smtWarehouseArea,smtHtWarehouseArea);
        smtHtWarehouseAreaMapper.insertSelective(smtHtWarehouseArea);
        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(String ids) {
        int i=0;
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        String[] idsArr = ids.split(",");
        List<SmtHtWarehouseArea>  smtHtWarehouseAreaList =  new LinkedList<>();
        for(String id :idsArr){
            SmtWarehouseArea smtWarehouseArea = smtWarehouseAreaMapper.selectByPrimaryKey(id);
            if(StringUtils.isEmpty(smtWarehouseArea)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012000,id);
            }

            //被储位引用
            Example example = new Example(SmtStorage.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("warehouseAreaId",smtWarehouseArea.getWarehouseAreaId());
            List<SmtStorage> smtStorages = smtStorageMapper.selectByExample(example);
            if(StringUtils.isNotEmpty(smtStorages)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012004);
            }

            SmtHtWarehouseArea smtHtWarehouseArea =  new SmtHtWarehouseArea();
            BeanUtils.copyProperties(smtWarehouseArea,smtHtWarehouseArea);
            smtHtWarehouseArea.setModifiedTime(new Date());
            smtHtWarehouseArea.setCreateUserId(currentUser.getUserId());
            smtHtWarehouseAreaList.add(smtHtWarehouseArea);
        }
        i= smtWarehouseAreaMapper.deleteByIds(ids);
        smtHtWarehouseAreaMapper.insertList(smtHtWarehouseAreaList);
        return i;
    }
}
