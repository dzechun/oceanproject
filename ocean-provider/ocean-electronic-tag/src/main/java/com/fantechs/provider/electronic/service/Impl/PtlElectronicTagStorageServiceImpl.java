package com.fantechs.provider.electronic.service.Impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.electronic.dto.PtlElectronicTagStorageDto;
import com.fantechs.common.base.electronic.dto.PtlEquipmentDto;
import com.fantechs.common.base.electronic.entity.PtlElectronicTagStorage;
import com.fantechs.common.base.electronic.entity.search.SearchPtlEquipment;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.basic.BaseStorage;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.electronic.mapper.PtlElectronicTagStorageMapper;
import com.fantechs.provider.electronic.service.PtlElectronicTagStorageService;
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
public class PtlElectronicTagStorageServiceImpl extends BaseService<PtlElectronicTagStorage> implements PtlElectronicTagStorageService {

    @Resource
    private PtlElectronicTagStorageMapper ptlElectronicTagStorageMapper;
    @Resource
    private PtlEquipmentServiceImpl smtEquipmentService;
    @Resource
    private BaseFeignApi baseFeignApi;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(PtlElectronicTagStorage ptlElectronicTagStorage) {

        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        if (StringUtils.isEmpty(ptlElectronicTagStorage.getStorageId(),
                ptlElectronicTagStorage.getElectronicTagId(),
                ptlElectronicTagStorage.getEquipmentId(),
                ptlElectronicTagStorage.getEquipmentAreaId(),
                ptlElectronicTagStorage.getElectronicTagLangType(),
                ptlElectronicTagStorage.getStorageId(),
                ptlElectronicTagStorage.getWarehouseId(),
                ptlElectronicTagStorage.getWarehouseAreaId())) {
            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "缺少必要参数");
        }

        Example example = new Example(PtlElectronicTagStorage.class);
        Example.Criteria criteria = example.createCriteria();
        Example.Criteria criteria1 = example.createCriteria();
        criteria.andEqualTo("storageId", ptlElectronicTagStorage.getStorageId());
        criteria1.andEqualTo("equipmentId", ptlElectronicTagStorage.getEquipmentId())
                .andEqualTo("electronicTagId", ptlElectronicTagStorage.getElectronicTagId());
        example.or(criteria1);
        List<PtlElectronicTagStorage> ptlElectronicTagStorages = ptlElectronicTagStorageMapper.selectByExample(example);
        if (StringUtils.isNotEmpty(ptlElectronicTagStorages)){
            throw new BizErrorException("绑定关系已存在");
        }

        ptlElectronicTagStorage.setCreateUserId(user.getUserId());
        ptlElectronicTagStorage.setCreateTime(new Date());
        ptlElectronicTagStorage.setModifiedUserId(user.getUserId());
        ptlElectronicTagStorage.setModifiedTime(new Date());
        ptlElectronicTagStorage.setStatus(StringUtils.isEmpty(ptlElectronicTagStorage.getStatus())?1: ptlElectronicTagStorage.getStatus());
        int i = ptlElectronicTagStorageMapper.insertUseGeneratedKeys(ptlElectronicTagStorage);

        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(PtlElectronicTagStorage ptlElectronicTagStorage) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        Example example = new Example(PtlElectronicTagStorage.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("storageId", ptlElectronicTagStorage.getStorageId())
        .andNotEqualTo("electronicTagStorageId", ptlElectronicTagStorage.getElectronicTagStorageId());
        List<PtlElectronicTagStorage> ptlElectronicTagStorages = ptlElectronicTagStorageMapper.selectByExample(example);
        if (StringUtils.isNotEmpty(ptlElectronicTagStorages)){
            throw new BizErrorException("储位id已存在");
        }

        example.clear();
        Example.Criteria criteria1 = example.createCriteria();
        criteria1.andEqualTo("equipmentId", ptlElectronicTagStorage.getEquipmentId())
                .andEqualTo("electronicTagId", ptlElectronicTagStorage.getElectronicTagId())
                .andNotEqualTo("electronicTagStorageId", ptlElectronicTagStorage.getElectronicTagStorageId());
        List<PtlElectronicTagStorage> ptlElectronicTagStorages1 = ptlElectronicTagStorageMapper.selectByExample(example);
        if (StringUtils.isNotEmpty(ptlElectronicTagStorages1)){
            throw new BizErrorException("设备和电子标签的绑定关系已存在");
        }

        ptlElectronicTagStorage.setModifiedTime(new Date());
        ptlElectronicTagStorage.setModifiedUserId(user.getUserId());

        return ptlElectronicTagStorageMapper.updateByPrimaryKeySelective(ptlElectronicTagStorage);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
//        String[] idArr = ids.split(",");
//        for (String id : idArr) {
//            SmtElectronicTagStorage smtElectronicTagStorage = smtElectronicTagStorageMapper.selectByPrimaryKey(id);
//            if (StringUtils.isEmpty(smtElectronicTagStorage)){
//                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
//            }
//        }
        return ptlElectronicTagStorageMapper.deleteByIds(ids);
    }

    @Override
    public List<PtlElectronicTagStorageDto> findList(Map<String, Object> map) {
        return ptlElectronicTagStorageMapper.findList(map);
    }

    @Override
    public Map<String, Object> importElectronicTagController(List<PtlElectronicTagStorageDto> ptlElectronicTagStorageDtos) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        Map<String, Object> resutlMap = new HashMap<>();  //封装操作结果
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数
        LinkedList<PtlElectronicTagStorage> list = new LinkedList<>();
        for (int i = 0; i < ptlElectronicTagStorageDtos.size(); i++) {
            PtlElectronicTagStorageDto ptlElectronicTagStorageDto = ptlElectronicTagStorageDtos.get(i);
            String storageCode = ptlElectronicTagStorageDto.getStorageCode();//储位编码
            String equipmentCode = ptlElectronicTagStorageDto.getEquipmentCode();//设备编码
            String equipmentIp = ptlElectronicTagStorageDto.getEquipmentIp();//设备ip
            if (StringUtils.isEmpty(
                    storageCode,equipmentCode,equipmentIp
            )){
                fail.add(i+3);
                continue;
            }

            //判断该编码对应的储位是否存在
            BaseStorage storage = baseFeignApi.detail(Long.valueOf(ptlElectronicTagStorageDto.getStorageId())).getData();
            //判断该编码对应的设备是否存在
            SearchPtlEquipment searchPtlEquipment = new SearchPtlEquipment();
            searchPtlEquipment.setEquipmentCode(equipmentCode);
            searchPtlEquipment.setCodeQueryMark((byte) 1);
            List<PtlEquipmentDto> ptlEquipmentDtos = smtEquipmentService.findList(ControllerUtil.dynamicConditionByEntity(searchPtlEquipment));
            PtlEquipmentDto ptlEquipmentDto = ptlEquipmentDtos.get(0);
            if (StringUtils.isEmpty(storage, ptlEquipmentDto)){
                fail.add(i+3);
                continue;
            }

            //判断绑定关系是否存在
            Example example = new Example(PtlElectronicTagStorage.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("storageId",storage.getStorageId())
                    .andEqualTo("equipmentId", ptlEquipmentDto.getEquipmentId())
                    .andNotEqualTo("equipmentIp", ptlEquipmentDto.getEquipmentId());
            List<PtlElectronicTagStorage> ptlElectronicTagStorages = ptlElectronicTagStorageMapper.selectByExample(example);
            if (StringUtils.isNotEmpty(ptlElectronicTagStorages)){
                fail.add(i+3);
                continue;
            }
            PtlElectronicTagStorage ptlElectronicTagStorage = new PtlElectronicTagStorage();
            BeanUtils.copyProperties(ptlElectronicTagStorageDto, ptlElectronicTagStorage);
            ptlElectronicTagStorage.setStorageId(String.valueOf(storage.getStorageId()));
            ptlElectronicTagStorage.setEquipmentId(String.valueOf(ptlEquipmentDto.getEquipmentId()));
            ptlElectronicTagStorage.setCreateTime(new Date());
            ptlElectronicTagStorage.setCreateUserId(currentUser.getUserId());
            ptlElectronicTagStorage.setModifiedTime(new Date());
            ptlElectronicTagStorage.setModifiedUserId(currentUser.getUserId());
            list.add(ptlElectronicTagStorage);
        }

        if (StringUtils.isNotEmpty(list)){
            success = ptlElectronicTagStorageMapper.insertList(list);
        }

        resutlMap.put("操作成功总数",success);
        resutlMap.put("操作失败行数",fail);
        return resutlMap;
    }

}
