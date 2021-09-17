package com.fantechs.provider.eam.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.eam.EamJigBackupDto;
import com.fantechs.common.base.general.dto.eam.EamJigDto;
import com.fantechs.common.base.general.dto.eam.EamJigPointInspectionProjectItemDto;
import com.fantechs.common.base.general.dto.eam.EamSparePartReJigDto;
import com.fantechs.common.base.general.entity.eam.*;
import com.fantechs.common.base.general.entity.eam.history.EamHtJig;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.fileserver.service.FileFeignApi;
import com.fantechs.provider.eam.mapper.*;
import com.fantechs.provider.eam.service.EamJigService;
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
 * Created by leifengzhi on 2021/07/28.
 */
@Service
public class EamJigServiceImpl extends BaseService<EamJig> implements EamJigService {

    @Resource
    private EamJigMapper eamJigMapper;
    @Resource
    private EamHtJigMapper eamHtJigMapper;
    @Resource
    private EamJigBarcodeMapper eamJigBarcodeMapper;
    @Resource
    private EamJigAttachmentMapper eamJigAttachmentMapper;
    @Resource
    private EamSparePartReJigMapper eamSparePartReJigMapper;

    @Override
    public List<EamJigDto> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        map.put("orgId", user.getOrganizationId());

        return eamJigMapper.findList(map);
    }


    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int save(EamJig record) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(EamJig.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("jigCode", record.getJigCode());
        EamJig eamJig = eamJigMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(eamJig)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        example.clear();
        Example.Criteria criteria1 = example.createCriteria();
        criteria1.andEqualTo("jigName",record.getJigName());
        EamJig eamJig1 = eamJigMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(eamJig1)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001.getCode(),"名称重复");
        }

        record.setCreateUserId(user.getUserId());
        record.setCreateTime(new Date());
        record.setModifiedUserId(user.getUserId());
        record.setModifiedTime(new Date());
        record.setStatus(StringUtils.isEmpty(record.getStatus())?1: record.getStatus());
        record.setOrgId(user.getOrganizationId());
        eamJigMapper.insertUseGeneratedKeys(record);

        //条码列表
        List<EamJigBarcode> eamJigBarcodeList = record.getEamJigBarcodeList();
        if(StringUtils.isNotEmpty(eamJigBarcodeList)){
            this.barcodeIfRepeat(eamJigBarcodeList);

            for (EamJigBarcode eamJigBarcode : eamJigBarcodeList){
                eamJigBarcode.setJigId(record.getJigId());
                eamJigBarcode.setCreateUserId(user.getUserId());
                eamJigBarcode.setCreateTime(new Date());
                eamJigBarcode.setModifiedUserId(user.getUserId());
                eamJigBarcode.setModifiedTime(new Date());
                eamJigBarcode.setStatus(StringUtils.isEmpty(eamJigBarcode.getStatus())?1: eamJigBarcode.getStatus());
                eamJigBarcode.setOrgId(user.getOrganizationId());
                eamJigBarcode.setUsageStatus((byte)2);
            }
            eamJigBarcodeMapper.insertList(eamJigBarcodeList);
        }

        //附件列表
        List<EamJigAttachment> eamJigAttachmentList = record.getEamJigAttachmentList();
        if(StringUtils.isNotEmpty(eamJigAttachmentList)){
            for (EamJigAttachment eamJigAttachment : eamJigAttachmentList){
                eamJigAttachment.setJigId(record.getJigId());
                eamJigAttachment.setCreateUserId(user.getUserId());
                eamJigAttachment.setCreateTime(new Date());
                eamJigAttachment.setModifiedUserId(user.getUserId());
                eamJigAttachment.setModifiedTime(new Date());
                eamJigAttachment.setStatus(StringUtils.isEmpty(eamJigAttachment.getStatus())?1: eamJigAttachment.getStatus());
                eamJigAttachment.setOrgId(user.getOrganizationId());
            }
            eamJigAttachmentMapper.insertList(eamJigAttachmentList);
        }

        //备用件列表
        List<EamSparePartReJigDto> eamJigBackupDtoList = record.getEamJigBackupDtoList();
        if(StringUtils.isNotEmpty(eamJigBackupDtoList)){
            for (EamSparePartReJigDto eamSparePartReJigDto : eamJigBackupDtoList){
                eamSparePartReJigDto.setJigId(record.getJigId());
                eamSparePartReJigDto.setCreateUserId(user.getUserId());
                eamSparePartReJigDto.setCreateTime(new Date());
                eamSparePartReJigDto.setModifiedUserId(user.getUserId());
                eamSparePartReJigDto.setModifiedTime(new Date());
                eamSparePartReJigDto.setStatus(StringUtils.isEmpty(eamSparePartReJigDto.getStatus())?1: eamSparePartReJigDto.getStatus());
                eamSparePartReJigDto.setOrgId(user.getOrganizationId());
            }
            eamSparePartReJigMapper.insertList(eamJigBackupDtoList);
        }

        //履历
        EamHtJig eamHtJig = new EamHtJig();
        BeanUtils.copyProperties(record, eamHtJig);
        int i = eamHtJigMapper.insert(eamHtJig);

        return i;
    }

    /**
     * 判断条码是否重复
     * @param eamJigBarcodeList
     */
    public void barcodeIfRepeat(List<EamJigBarcode> eamJigBarcodeList){
        List<String> jigBarcodes = new ArrayList<>();
        List<String> assetCodes = new ArrayList<>();

        for (EamJigBarcode eamJigBarcode : eamJigBarcodeList) {
            if(StringUtils.isEmpty(eamJigBarcode.getJigBarcode(),eamJigBarcode.getAssetCode())){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(), "资产条码和治具条码不能为空");
            }

            if(jigBarcodes.contains(eamJigBarcode.getJigBarcode())||assetCodes.contains(eamJigBarcode.getAssetCode())){
                throw new BizErrorException(ErrorCodeEnum.OPT20012001.getCode(), "条码重复");
            }

            Example example = new Example(EamJigBarcode.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("jigBarcode", eamJigBarcode.getJigBarcode());
            if(StringUtils.isNotEmpty(eamJigBarcode.getJigBarcodeId())){
                criteria.andNotEqualTo("jigBarcodeId",eamJigBarcode.getJigBarcodeId());
            }
            EamJigBarcode jigBarcode = eamJigBarcodeMapper.selectOneByExample(example);
            if (StringUtils.isNotEmpty(jigBarcode)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012001.getCode(), "治具条码重复");
            }

            example.clear();
            Example.Criteria criteria1 = example.createCriteria();
            criteria1.andEqualTo("assetCode", eamJigBarcode.getAssetCode());
            if(StringUtils.isNotEmpty(eamJigBarcode.getJigBarcodeId())){
                criteria1.andNotEqualTo("jigBarcodeId",eamJigBarcode.getJigBarcodeId());
            }
            EamJigBarcode jigBarcode1 = eamJigBarcodeMapper.selectOneByExample(example);
            if (StringUtils.isNotEmpty(jigBarcode1)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012001.getCode(), "资产条码重复");
            }

            jigBarcodes.add(eamJigBarcode.getJigBarcode());
            assetCodes.add(eamJigBarcode.getAssetCode());
        }
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int update(EamJig entity) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(EamJig.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("jigCode", entity.getJigCode())
                .andNotEqualTo("jigId",entity.getJigId());
        EamJig eamJig = eamJigMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(eamJig)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        example.clear();
        Example.Criteria criteria4 = example.createCriteria();
        criteria4.andEqualTo("jigName",entity.getJigName())
                 .andNotEqualTo("jigId",entity.getJigId());
        EamJig eamJig1 = eamJigMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(eamJig1)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001.getCode(),"名称重复");
        }

        entity.setModifiedTime(new Date());
        entity.setModifiedUserId(user.getUserId());
        int i = eamJigMapper.updateByPrimaryKeySelective(entity);

        //原来有的条码信息只更新
        List<Long> jigBarcodeIdList = new ArrayList<>();
        List<EamJigBarcode> eamJigBarcodeList = entity.getEamJigBarcodeList();
        if(StringUtils.isNotEmpty(eamJigBarcodeList)) {
            for (EamJigBarcode eamJigBarcode : eamJigBarcodeList) {
                if (StringUtils.isNotEmpty(eamJigBarcode.getJigBarcodeId())) {
                    eamJigBarcode.setCurrentUsageDays(null);
                    eamJigBarcodeMapper.updateByPrimaryKeySelective(eamJigBarcode);
                    jigBarcodeIdList.add(eamJigBarcode.getJigBarcodeId());
                }
            }
        }

        //删除原条码
        Example example1 = new Example(EamJigBarcode.class);
        Example.Criteria criteria1 = example1.createCriteria();
        criteria1.andEqualTo("jigId", entity.getJigId());
        if (jigBarcodeIdList.size() > 0) {
            criteria1.andNotIn("jigBarcodeId", jigBarcodeIdList);
        }
        eamJigBarcodeMapper.deleteByExample(example1);

        //条码列表
        if(StringUtils.isNotEmpty(eamJigBarcodeList)){
            this.barcodeIfRepeat(eamJigBarcodeList);

            for (EamJigBarcode eamJigBarcode : eamJigBarcodeList){
                if(jigBarcodeIdList.contains(eamJigBarcode.getJigBarcodeId())){
                    continue;
                }
                eamJigBarcode.setJigId(entity.getJigId());
                eamJigBarcode.setCreateUserId(user.getUserId());
                eamJigBarcode.setCreateTime(new Date());
                eamJigBarcode.setModifiedUserId(user.getUserId());
                eamJigBarcode.setModifiedTime(new Date());
                eamJigBarcode.setStatus(StringUtils.isEmpty(eamJigBarcode.getStatus())?1: eamJigBarcode.getStatus());
                eamJigBarcode.setOrgId(user.getOrganizationId());
                eamJigBarcode.setUsageStatus((byte)2);
                eamJigBarcodeMapper.insert(eamJigBarcode);
            }
        }

        //删除原附件
        Example example2 = new Example(EamJigAttachment.class);
        Example.Criteria criteria2 = example2.createCriteria();
        criteria2.andEqualTo("jigId", entity.getJigId());
        eamJigAttachmentMapper.deleteByExample(example2);

        //附件列表
        List<EamJigAttachment> eamJigAttachmentList = entity.getEamJigAttachmentList();
        if(StringUtils.isNotEmpty(eamJigAttachmentList)){
            for (EamJigAttachment eamJigAttachment : eamJigAttachmentList){
                eamJigAttachment.setJigId(entity.getJigId());
                eamJigAttachment.setCreateUserId(user.getUserId());
                eamJigAttachment.setCreateTime(new Date());
                eamJigAttachment.setModifiedUserId(user.getUserId());
                eamJigAttachment.setModifiedTime(new Date());
                eamJigAttachment.setStatus(StringUtils.isEmpty(eamJigAttachment.getStatus())?1: eamJigAttachment.getStatus());
                eamJigAttachment.setOrgId(user.getOrganizationId());
            }
            eamJigAttachmentMapper.insertList(eamJigAttachmentList);
        }

        //删除原备用件
        Example example3 = new Example(EamSparePartReJig.class);
        Example.Criteria criteria3 = example3.createCriteria();
        criteria3.andEqualTo("jigId", entity.getJigId());
        eamSparePartReJigMapper.deleteByExample(example3);

        //备用件列表
        List<EamSparePartReJigDto> eamJigBackupDtoList = entity.getEamJigBackupDtoList();
        if(StringUtils.isNotEmpty(eamJigBackupDtoList)){
            for (EamSparePartReJigDto eamSparePartReJigDto : eamJigBackupDtoList){
                eamSparePartReJigDto.setJigId(entity.getJigId());
                eamSparePartReJigDto.setCreateUserId(user.getUserId());
                eamSparePartReJigDto.setCreateTime(new Date());
                eamSparePartReJigDto.setModifiedUserId(user.getUserId());
                eamSparePartReJigDto.setModifiedTime(new Date());
                eamSparePartReJigDto.setStatus(StringUtils.isEmpty(eamSparePartReJigDto.getStatus())?1: eamSparePartReJigDto.getStatus());
                eamSparePartReJigDto.setOrgId(user.getOrganizationId());
            }
            eamSparePartReJigMapper.insertList(eamJigBackupDtoList);
        }

        EamHtJig eamHtJig = new EamHtJig();
        BeanUtils.copyProperties(entity, eamHtJig);
        eamHtJigMapper.insert(eamHtJig);

        return i;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        List<EamHtJig> list = new ArrayList<>();
        String[] idArry = ids.split(",");
        for (String id : idArry) {
            EamJig eamJig = eamJigMapper.selectByPrimaryKey(id);
            if(StringUtils.isEmpty(eamJig)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

            EamHtJig eamHtJig = new EamHtJig();
            BeanUtils.copyProperties(eamJig, eamHtJig);
            list.add(eamHtJig);

            //删除条码
            Example example1 = new Example(EamJigBarcode.class);
            Example.Criteria criteria1 = example1.createCriteria();
            criteria1.andEqualTo("jigId", id);
            eamJigBarcodeMapper.deleteByExample(example1);

            //删除附件信息
            Example example2 = new Example(EamJigAttachment.class);
            Example.Criteria criteria2 = example2.createCriteria();
            criteria2.andEqualTo("jigId", id);
            eamJigAttachmentMapper.deleteByExample(example2);

            //删除备用件
            Example example3 = new Example(EamSparePartReJig.class);
            Example.Criteria criteria3 = example3.createCriteria();
            criteria3.andEqualTo("jigId", id);
            eamSparePartReJigMapper.deleteByExample(example3);
        }

        eamHtJigMapper.insertList(list);

        return eamJigMapper.deleteByIds(ids);
    }
}
