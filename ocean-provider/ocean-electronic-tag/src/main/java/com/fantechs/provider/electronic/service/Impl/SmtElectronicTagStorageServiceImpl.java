package com.fantechs.provider.electronic.service.Impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.dto.SmtElectronicTagStorageDto;
import com.fantechs.common.base.entity.SmtElectronicTagStorage;
import com.fantechs.common.base.entity.history.SmtHtElectronicTagStorage;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.electronic.mapper.SmtElectronicTagStorageMapper;
import com.fantechs.provider.electronic.mapper.SmtHtElectronicTagControllerMapper;
import com.fantechs.provider.electronic.mapper.SmtHtElectronicTagStorageMapper;
import com.fantechs.provider.electronic.service.SmtElectronicTagStorageService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by leifengzhi on 2020/11/17.
 */
@Service
public class SmtElectronicTagStorageServiceImpl extends BaseService<SmtElectronicTagStorage> implements SmtElectronicTagStorageService {

    @Resource
    private SmtElectronicTagStorageMapper smtElectronicTagStorageMapper;
    @Resource
    private SmtHtElectronicTagStorageMapper smtHtElectronicTagStorageMapper;

    @Override
    public int save(SmtElectronicTagStorage smtElectronicTagStorage) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(SmtElectronicTagStorage.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("storageId",smtElectronicTagStorage.getStorageId())
                .orEqualTo("electronicTagControllerId",smtElectronicTagStorage.getElectronicTagControllerId())
                .orEqualTo("electronicTagId",smtElectronicTagStorage.getElectronicTagId());
        List<SmtElectronicTagStorage> smtElectronicTagStorages = smtElectronicTagStorageMapper.selectByExample(example);
        if (StringUtils.isNotEmpty(smtElectronicTagStorages)){
            throw new BizErrorException("储位id或电子标签控制器id或电子标签id已存在");
        }
        smtElectronicTagStorage.setCreateUserId(user.getUserId());
        smtElectronicTagStorage.setCreateTime(new Date());
        smtElectronicTagStorage.setModifiedUserId(user.getUserId());
        smtElectronicTagStorage.setModifiedTime(new Date());
        smtElectronicTagStorage.setStatus(StringUtils.isEmpty(smtElectronicTagStorage.getStatus())?1:smtElectronicTagStorage.getStatus());
        int i = smtElectronicTagStorageMapper.insertUseGeneratedKeys(smtElectronicTagStorage);

        SmtHtElectronicTagStorage smtHtElectronicTagStorage = new SmtHtElectronicTagStorage();
        BeanUtils.copyProperties(smtElectronicTagStorage,smtHtElectronicTagStorage);
        smtHtElectronicTagStorageMapper.insert(smtHtElectronicTagStorage);

        return i;
    }

    @Override
    public int update(SmtElectronicTagStorage smtElectronicTagStorage) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        Example example = new Example(SmtElectronicTagStorage.class);
        Example.Criteria criteria = example.createCriteria();
        Example.Criteria criteria1 = example.createCriteria();
        criteria.andEqualTo("storageId",smtElectronicTagStorage.getStorageId())
                .orEqualTo("electronicTagControllerId",smtElectronicTagStorage.getElectronicTagControllerId())
                .orEqualTo("electronicTagId",smtElectronicTagStorage.getElectronicTagId());
        criteria1.andNotEqualTo("electronicTagStorageId",smtElectronicTagStorage.getElectronicTagStorageId());
        example.and(criteria1);
        List<SmtElectronicTagStorage> smtElectronicTagStorages = smtElectronicTagStorageMapper.selectByExample(example);
        if (StringUtils.isNotEmpty(smtElectronicTagStorages)){
            throw new BizErrorException("储位id或电子标签控制器id或电子标签id已存在");
        }

        smtElectronicTagStorage.setModifiedTime(new Date());
        smtElectronicTagStorage.setModifiedUserId(user.getUserId());

        SmtHtElectronicTagStorage smtHtElectronicTagStorage = new SmtHtElectronicTagStorage();
        BeanUtils.copyProperties(smtElectronicTagStorage,smtHtElectronicTagStorage);
        smtHtElectronicTagStorageMapper.insert(smtHtElectronicTagStorage);
        return smtElectronicTagStorageMapper.updateByPrimaryKeySelective(smtElectronicTagStorage);
    }

    @Override
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        LinkedList<SmtHtElectronicTagStorage> list = new LinkedList<>();
        String[] idArr = ids.split(",");
        for (String id : idArr) {
            SmtElectronicTagStorage smtElectronicTagStorage = smtElectronicTagStorageMapper.selectByPrimaryKey(id);
            if (StringUtils.isEmpty(smtElectronicTagStorage)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            SmtHtElectronicTagStorage smtHtElectronicTagStorage = new SmtHtElectronicTagStorage();
            BeanUtils.copyProperties(smtElectronicTagStorage,smtHtElectronicTagStorage);
            smtElectronicTagStorage.setModifiedTime(new Date());
            smtElectronicTagStorage.setModifiedUserId(user.getUserId());
            list.add(smtHtElectronicTagStorage);
        }
        smtHtElectronicTagStorageMapper.insertList(list);
        return smtElectronicTagStorageMapper.deleteByIds(ids);
    }

    @Override
    public List<SmtElectronicTagStorageDto> findList(Map<String, Object> map) {
        return smtElectronicTagStorageMapper.findList(map);
    }
}
