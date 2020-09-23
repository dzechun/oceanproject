package com.fantechs.provider.imes.basic.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.basic.SmtHtStorage;
import com.fantechs.common.base.entity.basic.SmtStorage;
import com.fantechs.common.base.entity.basic.SmtWarehouse;
import com.fantechs.common.base.entity.basic.history.SmtHtWarehouse;
import com.fantechs.common.base.entity.basic.search.SearchSmtStorage;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.imes.basic.mapper.SmtHtStorageMapper;
import com.fantechs.provider.imes.basic.mapper.SmtStorageMapper;
import com.fantechs.provider.imes.basic.service.SmtStorageService;
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
public class SmtStorageServiceImpl extends BaseService<SmtStorage> implements SmtStorageService {

    @Resource
    private SmtStorageMapper smtStorageMapper;
    @Resource
    private SmtHtStorageMapper smtHtStorageMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(SmtStorage smtStorage) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(SmtStorage.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("storageCode",smtStorage.getStorageCode());
        List<SmtStorage> smtStorages = smtStorageMapper.selectByExample(example);
        if(null!=smtStorages&&smtStorages.size()>0){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        smtStorage.setCreateUserId(currentUser.getUserId());
        smtStorage.setCreateTime(new Date());
        smtStorageMapper.insertUseGeneratedKeys(smtStorage);

        //新增储位历史信息
        SmtHtStorage smtHtStorage=new SmtHtStorage();
        BeanUtils.copyProperties(smtStorage,smtHtStorage);
        int i = smtHtStorageMapper.insertSelective(smtHtStorage);
        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDel(String ids) {
        int i=0;
        List<SmtHtStorage> list=new ArrayList<>();

        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        String[] deptIds = ids.split(",");
        for (String deptId : deptIds) {
            SmtStorage smtStorage = smtStorageMapper.selectByPrimaryKey(Long.parseLong(deptId));
            if(StringUtils.isEmpty(smtStorage)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            //新增储位历史信息
            SmtHtStorage smtHtStorage=new SmtHtStorage();
            BeanUtils.copyProperties(smtStorage,smtHtStorage);
            smtHtStorage.setModifiedUserId(currentUser.getUserId());
            smtHtStorage.setModifiedTime(new Date());
            list.add(smtHtStorage);
        }
        smtHtStorageMapper.insertList(list);

        return smtStorageMapper.deleteByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateById(SmtStorage storage) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(SmtStorage.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("storageCode",storage.getStorageCode());

        SmtStorage smtStorage = smtStorageMapper.selectOneByExample(example);

        if(StringUtils.isNotEmpty(smtStorage)&&!smtStorage.getWarehouseId().equals(storage.getWarehouseId())){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        storage.setModifiedUserId(currentUser.getUserId());
        storage.setModifiedTime(new Date());
        int i= smtStorageMapper.updateByPrimaryKeySelective(storage);

        //新增储位历史信息
        SmtHtStorage smtHtStorage=new SmtHtStorage();
        BeanUtils.copyProperties(storage,smtHtStorage);
        smtHtStorage.setModifiedUserId(currentUser.getUserId());
        smtHtStorage.setModifiedTime(new Date());
        smtHtStorageMapper.insertSelective(smtHtStorage);
        return i;
    }

    @Override
    public SmtStorage selectByKey(Long id) {
        return smtStorageMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<SmtStorage> findList(SearchSmtStorage searchSmtStorage) {
        return smtStorageMapper.findList(searchSmtStorage);
    }
}
