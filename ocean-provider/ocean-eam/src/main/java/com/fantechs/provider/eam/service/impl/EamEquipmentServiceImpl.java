package com.fantechs.provider.eam.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.eam.EamEquInspectionOrderDto;
import com.fantechs.common.base.general.dto.eam.EamEquMaintainOrderDto;
import com.fantechs.common.base.general.dto.eam.EamEquipmentDto;
import com.fantechs.common.base.general.dto.eam.EamJigBackupDto;
import com.fantechs.common.base.general.entity.eam.*;
import com.fantechs.common.base.general.entity.eam.history.EamHtEquipment;
import com.fantechs.common.base.general.entity.eam.history.EamHtJig;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.eam.mapper.*;
import com.fantechs.provider.eam.service.EamEquipmentService;
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
 * Created by leifengzhi on 2021/06/25.
 */
@Service
public class EamEquipmentServiceImpl extends BaseService<EamEquipment> implements EamEquipmentService {

    @Resource
    private EamEquipmentMapper eamEquipmentMapper;
    @Resource
    private EamHtEquipmentMapper eamHtEquipmentMapper;
    @Resource
    private EamEquipmentBarcodeMapper eamEquipmentBarcodeMapper;
    @Resource
    private EamEquipmentAttachmentMapper eamEquipmentAttachmentMapper;
    @Resource
    private EamEquipmentBackupMapper eamEquipmentBackupMapper;

    @Override
    public List<EamEquipmentDto> findList(Map<String, Object> map) {
        if(StringUtils.isEmpty(map.get("orgId"))){
            SysUser user = getUser();
            map.put("orgId", user.getOrganizationId());
        }
        return eamEquipmentMapper.findList(map);
    }

    @Override
    public List<EamHtEquipment> findHtList(Map<String, Object> map) {
        SysUser user = getUser();
        map.put("orgId", user.getOrganizationId());
        return eamHtEquipmentMapper.findHtList(map);
    }

    @Override
    public int batchUpdate(List<EamEquipment> list) {
        return eamEquipmentMapper.batchUpdate(list);
    }

    @Override
    public EamEquipment detailByIp(String ip) {
        Example example = new Example(EamEquipment.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("equipmentIp",ip);
        EamEquipment eamEquipment = eamEquipmentMapper.selectOneByExample(example);
        if (StringUtils.isEmpty(eamEquipment)){
            throw new BizErrorException("未查询到ip对应的设备信息");
        }
        return eamEquipment;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int save(EamEquipment record) {
        SysUser user = getUser();

        check(record);

        record.setCreateUserId(user.getUserId());
        record.setCreateTime(new Date());
        record.setModifiedUserId(user.getUserId());
        record.setModifiedTime(new Date());
        record.setStatus(StringUtils.isEmpty(record.getStatus())?1: record.getStatus());
        record.setOrgId(user.getOrganizationId());
        eamEquipmentMapper.insertUseGeneratedKeys(record);

        EamHtEquipment eamHtEquipment = new EamHtEquipment();
        BeanUtils.copyProperties(record, eamHtEquipment);
        int i = eamHtEquipmentMapper.insertSelective(eamHtEquipment);

        //条码列表
        List<EamEquipmentBarcode> eamEquipmentBarcodeList = record.getEamEquipmentBarcodeList();
        if(StringUtils.isNotEmpty(eamEquipmentBarcodeList)){
            this.barcodeIfRepeat(eamEquipmentBarcodeList);

            for (EamEquipmentBarcode eamEquipmentBarcode : eamEquipmentBarcodeList){
                eamEquipmentBarcode.setEquipmentId(record.getEquipmentId());
                eamEquipmentBarcode.setCreateUserId(user.getUserId());
                eamEquipmentBarcode.setCreateTime(new Date());
                eamEquipmentBarcode.setModifiedUserId(user.getUserId());
                eamEquipmentBarcode.setModifiedTime(new Date());
                eamEquipmentBarcode.setStatus(StringUtils.isEmpty(eamEquipmentBarcode.getStatus())?1: eamEquipmentBarcode.getStatus());
                eamEquipmentBarcode.setOrgId(user.getOrganizationId());
                eamEquipmentBarcode.setEquipmentStatus((byte)5);
            }
            eamEquipmentBarcodeMapper.insertList(eamEquipmentBarcodeList);
        }

        //附件列表
        List<EamEquipmentAttachment> eamEquipmentAttachmentList = record.getEamEquipmentAttachmentList();
        if(StringUtils.isNotEmpty(eamEquipmentAttachmentList)){
            for (EamEquipmentAttachment eamJigAttachmentList : eamEquipmentAttachmentList){
                eamJigAttachmentList.setEquipmentId(record.getEquipmentId());
                eamJigAttachmentList.setCreateUserId(user.getUserId());
                eamJigAttachmentList.setCreateTime(new Date());
                eamJigAttachmentList.setModifiedUserId(user.getUserId());
                eamJigAttachmentList.setModifiedTime(new Date());
                eamJigAttachmentList.setStatus(StringUtils.isEmpty(eamJigAttachmentList.getStatus())?1: eamJigAttachmentList.getStatus());
                eamJigAttachmentList.setOrgId(user.getOrganizationId());
            }
            eamEquipmentAttachmentMapper.insertList(eamEquipmentAttachmentList);
        }

        //备用件列表
        List<EamEquipmentBackup> eamEquipmentBackupList = record.getEamEquipmentBackupList();
        if(StringUtils.isNotEmpty(eamEquipmentBackupList)){
            for (EamEquipmentBackup eamEquipmentBackup : eamEquipmentBackupList){
                eamEquipmentBackup.setEquipmentId(record.getEquipmentId());
                eamEquipmentBackup.setCreateUserId(user.getUserId());
                eamEquipmentBackup.setCreateTime(new Date());
                eamEquipmentBackup.setModifiedUserId(user.getUserId());
                eamEquipmentBackup.setModifiedTime(new Date());
                eamEquipmentBackup.setStatus(StringUtils.isEmpty(eamEquipmentBackup.getStatus())?1: eamEquipmentBackup.getStatus());
                eamEquipmentBackup.setOrgId(user.getOrganizationId());
            }
            eamEquipmentBackupMapper.insertList(eamEquipmentBackupList);
        }

        return i;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int update(EamEquipment entity) {
        SysUser user = getUser();

        check(entity);

        entity.setModifiedTime(new Date());
        entity.setModifiedUserId(user.getUserId());
        int i = eamEquipmentMapper.updateByPrimaryKeySelective(entity);

        //添加履历表
        EamHtEquipment eamHtEquipment = new EamHtEquipment();
        BeanUtils.copyProperties(entity, eamHtEquipment);
        eamHtEquipmentMapper.insertSelective(eamHtEquipment);

        //原来有的条码信息只更新
        List<Long> equipmentBarcodeIdList = new ArrayList<>();
        List<EamEquipmentBarcode> eamEquipmentBarcodeList = entity.getEamEquipmentBarcodeList();
        if(StringUtils.isNotEmpty(eamEquipmentBarcodeList)) {
            for (EamEquipmentBarcode eamEquipmentBarcode : eamEquipmentBarcodeList) {
                if (StringUtils.isNotEmpty(eamEquipmentBarcode.getEquipmentBarcodeId())) {
                    eamEquipmentBarcodeMapper.updateByPrimaryKeySelective(eamEquipmentBarcode);
                    equipmentBarcodeIdList.add(eamEquipmentBarcode.getEquipmentBarcodeId());
                }
            }
        }

        //删除原条码
        Example example1 = new Example(EamEquipmentBarcode.class);
        Example.Criteria criteria1 = example1.createCriteria();
        criteria1.andEqualTo("equipmentId", entity.getEquipmentId());
        if (equipmentBarcodeIdList.size() > 0) {
            criteria1.andNotIn("equipmentBarcodeId", equipmentBarcodeIdList);
        }
        eamEquipmentBarcodeMapper.deleteByExample(example1);

        //条码列表
        if(StringUtils.isNotEmpty(eamEquipmentBarcodeList)){
            this.barcodeIfRepeat(eamEquipmentBarcodeList);

            for (EamEquipmentBarcode eamEquipmentBarcode : eamEquipmentBarcodeList){
                if(equipmentBarcodeIdList.contains(eamEquipmentBarcode.getEquipmentBarcodeId())){
                    continue;
                }
                eamEquipmentBarcode.setEquipmentId(entity.getEquipmentId());
                eamEquipmentBarcode.setCreateUserId(user.getUserId());
                eamEquipmentBarcode.setCreateTime(new Date());
                eamEquipmentBarcode.setModifiedUserId(user.getUserId());
                eamEquipmentBarcode.setModifiedTime(new Date());
                eamEquipmentBarcode.setStatus(StringUtils.isEmpty(eamEquipmentBarcode.getStatus())?1: eamEquipmentBarcode.getStatus());
                eamEquipmentBarcode.setOrgId(user.getOrganizationId());
                eamEquipmentBarcode.setEquipmentStatus((byte)5);
                eamEquipmentBarcodeMapper.insert(eamEquipmentBarcode);
            }
        }

        //删除原附件
        Example example2 = new Example(EamEquipmentAttachment.class);
        Example.Criteria criteria2 = example2.createCriteria();
        criteria2.andEqualTo("equipmentId", entity.getEquipmentId());
        eamEquipmentAttachmentMapper.deleteByExample(example2);

        //附件列表
        List<EamEquipmentAttachment> eamEquipmentAttachmentList = entity.getEamEquipmentAttachmentList();
        if(StringUtils.isNotEmpty(eamEquipmentAttachmentList)){
            for (EamEquipmentAttachment eamJigAttachmentList : eamEquipmentAttachmentList){
                eamJigAttachmentList.setEquipmentId(entity.getEquipmentId());
                eamJigAttachmentList.setCreateUserId(user.getUserId());
                eamJigAttachmentList.setCreateTime(new Date());
                eamJigAttachmentList.setModifiedUserId(user.getUserId());
                eamJigAttachmentList.setModifiedTime(new Date());
                eamJigAttachmentList.setStatus(StringUtils.isEmpty(eamJigAttachmentList.getStatus())?1: eamJigAttachmentList.getStatus());
                eamJigAttachmentList.setOrgId(user.getOrganizationId());
            }
            eamEquipmentAttachmentMapper.insertList(eamEquipmentAttachmentList);
        }

        //删除原备用件
        Example example3 = new Example(EamEquipmentBackup.class);
        Example.Criteria criteria3 = example3.createCriteria();
        criteria3.andEqualTo("equipmentId", entity.getEquipmentId());
        eamEquipmentBackupMapper.deleteByExample(example3);

        //备用件列表
        List<EamEquipmentBackup> eamEquipmentBackupList = entity.getEamEquipmentBackupList();
        if(StringUtils.isNotEmpty(eamEquipmentBackupList)){
            for (EamEquipmentBackup eamEquipmentBackup : eamEquipmentBackupList){
                eamEquipmentBackup.setEquipmentId(entity.getEquipmentId());
                eamEquipmentBackup.setCreateUserId(user.getUserId());
                eamEquipmentBackup.setCreateTime(new Date());
                eamEquipmentBackup.setModifiedUserId(user.getUserId());
                eamEquipmentBackup.setModifiedTime(new Date());
                eamEquipmentBackup.setStatus(StringUtils.isEmpty(eamEquipmentBackup.getStatus())?1: eamEquipmentBackup.getStatus());
                eamEquipmentBackup.setOrgId(user.getOrganizationId());
            }
            eamEquipmentBackupMapper.insertList(eamEquipmentBackupList);
        }

        return i;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int batchDelete(String ids) {
        getUser();

        List<EamHtEquipment> htList = new ArrayList<>();
        String[] idArry = ids.split(",");
        for (String id : idArry) {
            EamEquipment eamEquipment = eamEquipmentMapper.selectByPrimaryKey(id);
            if(StringUtils.isEmpty(eamEquipment)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

            EamHtEquipment eamHtEquipment = new EamHtEquipment();
            BeanUtils.copyProperties(eamEquipment, eamHtEquipment);
            htList.add(eamHtEquipment);

            //删除原条码
            Example example1 = new Example(EamEquipmentBarcode.class);
            Example.Criteria criteria1 = example1.createCriteria();
            criteria1.andEqualTo("equipmentId", id);
            eamEquipmentBackupMapper.deleteByExample(example1);

            //删除原附件
            Example example2 = new Example(EamEquipmentAttachment.class);
            Example.Criteria criteria2 = example2.createCriteria();
            criteria2.andEqualTo("equipmentId",id);
            eamEquipmentAttachmentMapper.deleteByExample(example2);

            //删除原备用件
            Example example3 = new Example(EamEquipmentBackup.class);
            Example.Criteria criteria3 = example3.createCriteria();
            criteria3.andEqualTo("equipmentId",id);
            eamEquipmentBackupMapper.deleteByExample(example3);
        }

        eamHtEquipmentMapper.insertList(htList);

        return eamEquipmentMapper.deleteByIds(ids);
    }

    public void check(EamEquipment entity){
        getUser();
        Example example = new Example(EamEquipment.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("equipmentCode", entity.getEquipmentCode());
        if(StringUtils.isNotEmpty(entity.getEquipmentId())){
            criteria.andNotEqualTo("equipmentId",entity.getEquipmentId());
        }
        EamEquipment eamEquipment = eamEquipmentMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(eamEquipment)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }
    }

    /**
     * 判断条码是否重复
     * @param eamEquipmentBarcodeList
     */
    public void barcodeIfRepeat(List<EamEquipmentBarcode> eamEquipmentBarcodeList){
        List<String> equipmentBarcodes = new ArrayList<>();
        List<String> assetCodes = new ArrayList<>();

        for (EamEquipmentBarcode eamEquipmentBarcode : eamEquipmentBarcodeList) {
            if(StringUtils.isEmpty(eamEquipmentBarcode.getEquipmentBarcode(),eamEquipmentBarcode.getAssetCode())){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(), "资产条码和设备条码不能为空");
            }

            if(equipmentBarcodes.contains(eamEquipmentBarcode.getEquipmentBarcode())||assetCodes.contains(eamEquipmentBarcode.getAssetCode())){
                throw new BizErrorException(ErrorCodeEnum.OPT20012001.getCode(), "条码重复");
            }

            Example example = new Example(EamEquipmentBarcode.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("equipmentBarcode", eamEquipmentBarcode.getEquipmentBarcode());
            if(StringUtils.isNotEmpty(eamEquipmentBarcode.getEquipmentBarcodeId())){
                criteria.andNotEqualTo("equipmentBarcodeId",eamEquipmentBarcode.getEquipmentBarcodeId());
            }
            EamEquipmentBarcode equipmentBarcode = eamEquipmentBarcodeMapper.selectOneByExample(example);
            if (StringUtils.isNotEmpty(equipmentBarcode)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012001.getCode(), "设备条码重复");
            }

            example.clear();
            Example.Criteria criteria1 = example.createCriteria();
            criteria1.andEqualTo("assetCode", eamEquipmentBarcode.getAssetCode());
            if(StringUtils.isNotEmpty(eamEquipmentBarcode.getEquipmentBarcodeId())){
                criteria1.andNotEqualTo("equipmentBarcodeId",eamEquipmentBarcode.getEquipmentBarcodeId());
            }
            EamEquipmentBarcode equipmentBarcode1 = eamEquipmentBarcodeMapper.selectOneByExample(example);
            if (StringUtils.isNotEmpty(equipmentBarcode1)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012001.getCode(), "资产条码重复");
            }

            equipmentBarcodes.add(eamEquipmentBarcode.getEquipmentBarcode());
            assetCodes.add(eamEquipmentBarcode.getAssetCode());
        }
    }


    @Override
    public List<EamEquipmentDto> findNoGroup(Map<String, Object> map) {
        SysUser user = getUser();
        map.put("orgId", user.getOrganizationId());
        return eamEquipmentMapper.findNoGroup(map);
    }

    @Override
    public List<EamEquInspectionOrderDto> findListForInspectionOrder(Map<String, Object> map) {
        SysUser user = getUser();
        map.put("orgId", user.getOrganizationId());
        return eamEquipmentMapper.findListForInspectionOrder(map);
    }

    @Override
    public List<EamEquMaintainOrderDto> findListForMaintainOrder(Map<String, Object> map) {
        SysUser user = getUser();
        map.put("orgId", user.getOrganizationId());
        return eamEquipmentMapper.findListForMaintainOrder(map);
    }


    public SysUser getUser(){
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        return user;
    }

}
