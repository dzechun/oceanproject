package com.fantechs.provider.electronic.service.Impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.electronic.dto.SmtElectronicTagStorageDto;
import com.fantechs.common.base.electronic.dto.SmtEquipmentDto;
import com.fantechs.common.base.electronic.entity.SmtElectronicTagStorage;
import com.fantechs.common.base.electronic.entity.history.SmtHtElectronicTagStorage;
import com.fantechs.common.base.electronic.entity.search.SearchSmtEquipment;
import com.fantechs.common.base.entity.basic.SmtStorage;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.imes.basic.BasicFeignApi;
import com.fantechs.provider.electronic.mapper.SmtElectronicTagStorageMapper;
import com.fantechs.provider.electronic.mapper.SmtHtElectronicTagStorageMapper;
import com.fantechs.provider.electronic.service.SmtElectronicTagStorageService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by leifengzhi on 2020/11/17.
 */
@Service
public class SmtElectronicTagStorageServiceImpl extends BaseService<SmtElectronicTagStorage> implements SmtElectronicTagStorageService {

    @Resource
    private SmtElectronicTagStorageMapper smtElectronicTagStorageMapper;
    @Resource
    private SmtHtElectronicTagStorageMapper smtHtElectronicTagStorageMapper;
    @Resource
    private SmtEquipmentServiceImpl smtEquipmentService;
    @Resource
    private BasicFeignApi basicFeignApi;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(SmtElectronicTagStorage smtElectronicTagStorage) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(SmtElectronicTagStorage.class);
        Example.Criteria criteria = example.createCriteria();
        Example.Criteria criteria1 = example.createCriteria();
        criteria.andEqualTo("storageId",smtElectronicTagStorage.getStorageId());
        criteria1.andEqualTo("equipmentId",smtElectronicTagStorage.getEquipmentId())
                .andEqualTo("electronicTagId",smtElectronicTagStorage.getElectronicTagId());
        example.or(criteria1);
        List<SmtElectronicTagStorage> smtElectronicTagStorages = smtElectronicTagStorageMapper.selectByExample(example);
        if (StringUtils.isNotEmpty(smtElectronicTagStorages)){
            throw new BizErrorException("绑定关系已存在");
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
    @Transactional(rollbackFor = Exception.class)
    public int update(SmtElectronicTagStorage smtElectronicTagStorage) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        Example example = new Example(SmtElectronicTagStorage.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("storageId",smtElectronicTagStorage.getStorageId())
        .andNotEqualTo("electronicTagStorageId",smtElectronicTagStorage.getElectronicTagStorageId());
        List<SmtElectronicTagStorage> smtElectronicTagStorages = smtElectronicTagStorageMapper.selectByExample(example);
        if (StringUtils.isNotEmpty(smtElectronicTagStorages)){
            throw new BizErrorException("储位id已存在");
        }

        example.clear();
        criteria.andEqualTo("equipmentId",smtElectronicTagStorage.getEquipmentId())
                .andEqualTo("electronicTagId",smtElectronicTagStorage.getElectronicTagId())
                .andNotEqualTo("electronicTagStorageId",smtElectronicTagStorage.getElectronicTagStorageId());
        List<SmtElectronicTagStorage> smtElectronicTagStorages1 = smtElectronicTagStorageMapper.selectByExample(example);
        if (StringUtils.isNotEmpty(smtElectronicTagStorages1)){
            throw new BizErrorException("储位id已存在");
        }

        smtElectronicTagStorage.setModifiedTime(new Date());
        smtElectronicTagStorage.setModifiedUserId(user.getUserId());

        SmtHtElectronicTagStorage smtHtElectronicTagStorage = new SmtHtElectronicTagStorage();
        BeanUtils.copyProperties(smtElectronicTagStorage,smtHtElectronicTagStorage);
        smtHtElectronicTagStorageMapper.insert(smtHtElectronicTagStorage);
        return smtElectronicTagStorageMapper.updateByPrimaryKeySelective(smtElectronicTagStorage);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
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

    @Override
    public Map<String, Object> importElectronicTagController(List<SmtElectronicTagStorageDto> smtElectronicTagStorageDtos) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        Map<String, Object> resutlMap = new HashMap<>();  //封装操作结果
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数
        LinkedList<SmtElectronicTagStorage> list = new LinkedList<>();
        LinkedList<SmtHtElectronicTagStorage> htList = new LinkedList<>();
        for (int i = 0; i < smtElectronicTagStorageDtos.size(); i++) {
            SmtElectronicTagStorageDto smtElectronicTagStorageDto = smtElectronicTagStorageDtos.get(i);
            String storageCode = smtElectronicTagStorageDto.getStorageCode();//储位编码
            String equipmentCode = smtElectronicTagStorageDto.getEquipmentCode();//设备编码
            String equipmentIp = smtElectronicTagStorageDto.getEquipmentIp();//设备ip
            if (StringUtils.isEmpty(
                    storageCode,equipmentCode,equipmentIp
            )){
                fail.add(i+3);
                continue;
            }

            //判断该编码对应的储位是否存在
            SmtStorage storage = basicFeignApi.detail(Long.valueOf(smtElectronicTagStorageDto.getStorageId())).getData();
            //判断该编码对应的设备是否存在
            SearchSmtEquipment searchSmtEquipment = new SearchSmtEquipment();
            searchSmtEquipment.setEquipmentCode(equipmentCode);
            searchSmtEquipment.setCodeQueryMark((byte) 1);
            List<SmtEquipmentDto> smtEquipmentDtos = smtEquipmentService.findList(ControllerUtil.dynamicConditionByEntity(searchSmtEquipment));
            SmtEquipmentDto smtEquipmentDto = smtEquipmentDtos.get(0);
            if (StringUtils.isEmpty(storage,smtEquipmentDto)){
                fail.add(i+3);
                continue;
            }

            //判断绑定关系是否存在
            Example example = new Example(SmtElectronicTagStorage.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("storageId",storage.getStorageId())
                    .andEqualTo("equipmentId",smtEquipmentDto.getEquipmentId())
                    .andNotEqualTo("equipmentIp",smtEquipmentDto.getEquipmentId());
            List<SmtElectronicTagStorage> smtElectronicTagStorages = smtElectronicTagStorageMapper.selectByExample(example);
            if (StringUtils.isNotEmpty(smtElectronicTagStorages)){
                fail.add(i+3);
                continue;
            }
            SmtElectronicTagStorage smtElectronicTagStorage = new SmtElectronicTagStorage();
            BeanUtils.copyProperties(smtElectronicTagStorageDto,smtElectronicTagStorage);
            smtElectronicTagStorage.setStorageId(String.valueOf(storage.getStorageId()));
            smtElectronicTagStorage.setEquipmentId(String.valueOf(smtEquipmentDto.getEquipmentId()));
            smtElectronicTagStorage.setCreateTime(new Date());
            smtElectronicTagStorage.setCreateUserId(currentUser.getUserId());
            smtElectronicTagStorage.setModifiedTime(new Date());
            smtElectronicTagStorage.setModifiedUserId(currentUser.getUserId());
            list.add(smtElectronicTagStorage);
        }

        if (StringUtils.isNotEmpty(list)){
            success = smtElectronicTagStorageMapper.insertList(list);
        }

        for (SmtElectronicTagStorage smtElectronicTagStorage : list) {
            SmtHtElectronicTagStorage smtHtElectronicTagStorage = new SmtHtElectronicTagStorage();
            BeanUtils.copyProperties(smtElectronicTagStorage,smtHtElectronicTagStorage);
            htList.add(smtHtElectronicTagStorage);
            smtHtElectronicTagStorageMapper.insertList(htList);
        }
        resutlMap.put("操作成功总数",success);
        resutlMap.put("操作失败行数",fail);
        return resutlMap;
    }

}
