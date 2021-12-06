package com.fantechs.provider.esop.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.esop.EsopEquipmentDto;
import com.fantechs.common.base.general.dto.esop.EsopIssueDto;
import com.fantechs.common.base.general.dto.esop.EsopWorkInstructionDto;
import com.fantechs.common.base.general.entity.esop.EsopIssue;
import com.fantechs.common.base.general.entity.esop.EsopIssueAttachment;
import com.fantechs.common.base.general.entity.esop.history.EsopHtIssue;
import com.fantechs.common.base.general.entity.esop.search.SearchEsopEquipment;
import com.fantechs.common.base.general.entity.esop.search.SearchEsopIssue;
import com.fantechs.common.base.general.entity.esop.search.SearchEsopWorkInstruction;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.esop.mapper.*;
import com.fantechs.provider.esop.service.EsopIssueService;
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
 * Created by leifengzhi on 2021/07/07.
 */
@Service
public class EsopIssueServiceImpl extends BaseService<EsopIssue> implements EsopIssueService {

    @Resource
    private EsopIssueMapper esopIssueMapper;
    @Resource
    private EsopHtIssueMapper esopHtIssueMapper;
    @Resource
    private EsopIssueAttachmentMapper esopIssueAttachmentMapper;
    @Resource
    private EsopEquipmentMapper esopEquipmentMapper;
    @Resource
    private EsopWorkInstructionMapper esopWorkInstructionMapper;


    @Override
    public List<EsopIssueDto> findList(Map<String, Object> map) {
        if(StringUtils.isEmpty(map.get("orgId"))){
            SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
            if (StringUtils.isEmpty(user)) {
                throw new BizErrorException(ErrorCodeEnum.UAC10011039);
            }
            map.put("orgId", user.getOrganizationId());
        }

        if (StringUtils.isNotEmpty(map.get("equipmentMacAddress"))) {
            List<EsopIssueDto> list = new ArrayList<>();

            SearchEsopEquipment searchEsopEquipment = new SearchEsopEquipment();
            searchEsopEquipment.setEquipmentMacAddress(map.get("equipmentMacAddress").toString());
            List<EsopEquipmentDto> esopEquipmentDtos = esopEquipmentMapper.findList(ControllerUtil.dynamicConditionByEntity(searchEsopEquipment));
            if (StringUtils.isNotEmpty(esopEquipmentDtos)) {
                //查询该设备所在工序的WI
                SearchEsopWorkInstruction searchEsopWorkInstruction = new SearchEsopWorkInstruction();
                searchEsopWorkInstruction.setEquipmentSeqNum(esopEquipmentDtos.get(0).getEquipmentSeqNum());
                searchEsopWorkInstruction.setProLineId(esopEquipmentDtos.get(0).getProLineId());
                List<EsopWorkInstructionDto> esopWorkInstructionDtos = esopWorkInstructionMapper.findList(searchEsopWorkInstruction);

                if (StringUtils.isNotEmpty(esopWorkInstructionDtos)) {
                    //查询该WI绑定的产品型号、工序的问题清单
                    SearchEsopIssue searchEsopIssue = new SearchEsopIssue();
                    searchEsopIssue.setProductModelCode(esopWorkInstructionDtos.get(0).getProductModelCode());
                    searchEsopIssue.setProcessId(esopWorkInstructionDtos.get(0).getProcessId());
                    searchEsopIssue.setCodeQueryMark(1);
                    searchEsopIssue.setOrgId(esopEquipmentDtos.get(0).getOrgId());
                    list = esopIssueMapper.findList(ControllerUtil.dynamicConditionByEntity(searchEsopIssue));

                    //工序编码
                    if(StringUtils.isNotEmpty(list)) {
                        for (EsopIssueDto EsopIssueDto : list) {
                            EsopIssueDto.setProcessCode(esopEquipmentDtos.get(0).getProcessCode());
                        }
                    }
                }
            }

            return list;
        }

        return esopIssueMapper.findList(map);
    }

    @Override
    public EsopIssue selectByKey(Object key) {
        EsopIssue EsopIssue = esopIssueMapper.selectByPrimaryKey(key);

        Example example = new Example(EsopIssueAttachment.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("issueId",EsopIssue.getIssueId());
        List<EsopIssueAttachment> EsopIssueAttachments = esopIssueAttachmentMapper.selectByExample(example);
        EsopIssue.setList(EsopIssueAttachments);

        return EsopIssue;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int save(EsopIssue record) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        record.setIssueCode(CodeUtils.getId("WT-"));
        record.setCreateUserId(user.getUserId());
        record.setCreateTime(new Date());
        record.setModifiedUserId(user.getUserId());
        record.setModifiedTime(new Date());
        record.setStatus(StringUtils.isEmpty(record.getStatus())?1: record.getStatus());
        record.setOrgId(user.getOrganizationId());
        esopIssueMapper.insertUseGeneratedKeys(record);

        List<EsopIssueAttachment> EsopIssueAttachments = record.getList();
        if(StringUtils.isNotEmpty(EsopIssueAttachments)) {
            for (EsopIssueAttachment EsopIssueAttachment : EsopIssueAttachments) {
                EsopIssueAttachment.setIssueId(record.getIssueId());
                EsopIssueAttachment.setCreateUserId(user.getUserId());
                EsopIssueAttachment.setCreateTime(new Date());
                EsopIssueAttachment.setModifiedUserId(user.getUserId());
                EsopIssueAttachment.setModifiedTime(new Date());
                EsopIssueAttachment.setStatus(StringUtils.isEmpty(EsopIssueAttachment.getStatus()) ? 1 : EsopIssueAttachment.getStatus());
                EsopIssueAttachment.setOrgId(user.getOrganizationId());
            }
            esopIssueAttachmentMapper.insertList(EsopIssueAttachments);
        }

        EsopHtIssue EsopHtIssue = new EsopHtIssue();
        BeanUtils.copyProperties(record, EsopHtIssue);
        int i = esopHtIssueMapper.insertSelective(EsopHtIssue);

        return i;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int update(EsopIssue entity) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        entity.setModifiedTime(new Date());
        entity.setModifiedUserId(user.getUserId());
        int i = esopIssueMapper.updateByPrimaryKeySelective(entity);

        //删除原附件
        Example example1 = new Example(EsopIssueAttachment.class);
        Example.Criteria criteria1 = example1.createCriteria();
        criteria1.andEqualTo("issueId", entity.getIssueId());
        esopIssueAttachmentMapper.deleteByExample(example1);

        List<EsopIssueAttachment> EsopIssueAttachments = entity.getList();
        if(StringUtils.isNotEmpty(EsopIssueAttachments)) {
            for (EsopIssueAttachment EsopIssueAttachment : EsopIssueAttachments) {
                EsopIssueAttachment.setIssueId(entity.getIssueId());
                EsopIssueAttachment.setCreateUserId(user.getUserId());
                EsopIssueAttachment.setCreateTime(new Date());
                EsopIssueAttachment.setModifiedUserId(user.getUserId());
                EsopIssueAttachment.setModifiedTime(new Date());
                EsopIssueAttachment.setStatus(StringUtils.isEmpty(EsopIssueAttachment.getStatus()) ? 1 : EsopIssueAttachment.getStatus());
                EsopIssueAttachment.setOrgId(user.getOrganizationId());
            }
            esopIssueAttachmentMapper.insertList(EsopIssueAttachments);
        }

        EsopHtIssue EsopHtIssue = new EsopHtIssue();
        BeanUtils.copyProperties(entity, EsopHtIssue);
        esopHtIssueMapper.insertSelective(EsopHtIssue);

        return i;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int batchDelete(String ids) {
        String[] idArry = ids.split(",");
        for (String id : idArry) {
            //删除原附件
            Example example1 = new Example(EsopIssueAttachment.class);
            Example.Criteria criteria1 = example1.createCriteria();
            criteria1.andEqualTo("issueId",id);
            esopIssueAttachmentMapper.deleteByExample(example1);
        }
        return esopIssueMapper.deleteByIds(ids);
    }

    @Override
    public int batchAdd(List<EsopIssue> esopIssues) {
        int i=0;
        if(StringUtils.isNotEmpty(esopIssues)){
            Example example1 = new Example(EsopIssue.class);
            Example.Criteria criteria1 = example1.createCriteria();
            criteria1.andEqualTo("materialId", esopIssues.get(0).getMaterialId());
            esopIssueMapper.deleteByExample(example1);
            i = esopIssueMapper.insertList(esopIssues);
        }

        return i;
    }

}
