package com.fantechs.provider.eam.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.eam.EamIssueDto;
import com.fantechs.common.base.general.entity.eam.EamIssue;
import com.fantechs.common.base.general.entity.eam.EamIssueAttachment;
import com.fantechs.common.base.general.entity.eam.EamNews;
import com.fantechs.common.base.general.entity.eam.EamNewsAttachment;
import com.fantechs.common.base.general.entity.eam.history.EamHtIssue;
import com.fantechs.common.base.general.entity.eam.history.EamHtNews;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.eam.mapper.EamHtIssueMapper;
import com.fantechs.provider.eam.mapper.EamIssueAttachmentMapper;
import com.fantechs.provider.eam.mapper.EamIssueMapper;
import com.fantechs.provider.eam.service.EamIssueService;
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
public class EamIssueServiceImpl extends BaseService<EamIssue> implements EamIssueService {

    @Resource
    private EamIssueMapper eamIssueMapper;
    @Resource
    private EamHtIssueMapper eamHtIssueMapper;
    @Resource
    private EamIssueAttachmentMapper eamIssueAttachmentMapper;

    @Override
    public List<EamIssueDto> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        map.put("orgId", user.getOrganizationId());
        return eamIssueMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int save(EamIssue record) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        record.setIssueCode(CodeUtils.getId("WT-"));
        record.setCreateUserId(user.getUserId());
        record.setCreateTime(new Date());
        record.setModifiedUserId(user.getUserId());
        record.setModifiedTime(new Date());
        record.setStatus(StringUtils.isEmpty(record.getStatus())?1: record.getStatus());
        record.setOrgId(user.getOrganizationId());
        eamIssueMapper.insertUseGeneratedKeys(record);

        List<EamIssueAttachment> eamIssueAttachments = record.getList();
        if(StringUtils.isNotEmpty(eamIssueAttachments)) {
            for (EamIssueAttachment eamIssueAttachment : eamIssueAttachments) {
                eamIssueAttachment.setIssueId(record.getIssueId());
                eamIssueAttachment.setCreateUserId(user.getUserId());
                eamIssueAttachment.setCreateTime(new Date());
                eamIssueAttachment.setModifiedUserId(user.getUserId());
                eamIssueAttachment.setModifiedTime(new Date());
                eamIssueAttachment.setStatus(StringUtils.isEmpty(eamIssueAttachment.getStatus()) ? 1 : eamIssueAttachment.getStatus());
                eamIssueAttachment.setOrgId(user.getOrganizationId());
            }
            eamIssueAttachmentMapper.insertList(eamIssueAttachments);
        }

        EamHtIssue eamHtIssue = new EamHtIssue();
        BeanUtils.copyProperties(record, eamHtIssue);
        int i = eamHtIssueMapper.insert(eamHtIssue);

        return i;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int update(EamIssue entity) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        entity.setModifiedTime(new Date());
        entity.setModifiedUserId(user.getUserId());
        int i = eamIssueMapper.updateByPrimaryKeySelective(entity);

        //删除原附件
        Example example1 = new Example(EamIssueAttachment.class);
        Example.Criteria criteria1 = example1.createCriteria();
        criteria1.andEqualTo("issueId", entity.getIssueId());
        eamIssueAttachmentMapper.deleteByExample(example1);

        List<EamIssueAttachment> eamIssueAttachments = entity.getList();
        if(StringUtils.isNotEmpty(eamIssueAttachments)) {
            for (EamIssueAttachment eamIssueAttachment : eamIssueAttachments) {
                eamIssueAttachment.setIssueId(entity.getIssueId());
                eamIssueAttachment.setCreateUserId(user.getUserId());
                eamIssueAttachment.setCreateTime(new Date());
                eamIssueAttachment.setModifiedUserId(user.getUserId());
                eamIssueAttachment.setModifiedTime(new Date());
                eamIssueAttachment.setStatus(StringUtils.isEmpty(eamIssueAttachment.getStatus()) ? 1 : eamIssueAttachment.getStatus());
                eamIssueAttachment.setOrgId(user.getOrganizationId());
            }
            eamIssueAttachmentMapper.insertList(eamIssueAttachments);
        }

        EamHtIssue eamHtIssue = new EamHtIssue();
        BeanUtils.copyProperties(entity, eamHtIssue);
        eamHtIssueMapper.insert(eamHtIssue);

        return i;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        List<EamHtIssue> list = new ArrayList<>();
        String[] idArry = ids.split(",");
        for (String id : idArry) {
            EamIssue eamIssue = eamIssueMapper.selectByPrimaryKey(id);
            if(StringUtils.isEmpty(eamIssue)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

            EamHtIssue eamHtIssue = new EamHtIssue();
            BeanUtils.copyProperties(eamIssue, eamHtIssue);
            list.add(eamHtIssue);

            //删除原附件
            Example example1 = new Example(EamIssueAttachment.class);
            Example.Criteria criteria1 = example1.createCriteria();
            criteria1.andEqualTo("issueId", eamIssue.getIssueId());
            eamIssueAttachmentMapper.deleteByExample(example1);
        }

        eamHtIssueMapper.insertList(list);

        return eamIssueMapper.deleteByIds(ids);
    }
}
