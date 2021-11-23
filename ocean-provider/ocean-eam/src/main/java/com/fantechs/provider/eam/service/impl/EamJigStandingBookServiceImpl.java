package com.fantechs.provider.eam.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.eam.EamJigStandingBookDto;
import com.fantechs.common.base.general.entity.eam.EamJigAttachment;
import com.fantechs.common.base.general.entity.eam.EamJigStandingBook;
import com.fantechs.common.base.general.entity.eam.EamJigStandingBookAttachment;
import com.fantechs.common.base.general.entity.eam.history.EamHtJigStandingBook;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.eam.mapper.EamHtJigStandingBookMapper;
import com.fantechs.provider.eam.mapper.EamJigStandingBookAttachmentMapper;
import com.fantechs.provider.eam.mapper.EamJigStandingBookMapper;
import com.fantechs.provider.eam.service.EamJigStandingBookService;
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
public class EamJigStandingBookServiceImpl extends BaseService<EamJigStandingBook> implements EamJigStandingBookService {

    @Resource
    private EamJigStandingBookMapper eamJigStandingBookMapper;

    @Resource
    private EamHtJigStandingBookMapper eamHtJigStandingBookMapper;

    @Resource
    private EamJigStandingBookAttachmentMapper eamJigStandingBookAttachmentMapper;

    @Override
    public List<EamJigStandingBookDto> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId",user.getOrganizationId());
        return eamJigStandingBookMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int save(EamJigStandingBook record) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        this.codeIfRepeat(record);

        record.setCreateTime(new Date());
        record.setCreateUserId(user.getUserId());
        record.setModifiedTime(new Date());
        record.setModifiedUserId(user.getUserId());
        record.setOrgId(user.getOrganizationId());

        eamJigStandingBookMapper.insertUseGeneratedKeys(record);

        //附件列表
        List<EamJigStandingBookAttachment> eamJigStandingBookAttachmentList = record.getEamJigStandingBookAttachmentList();
        if(StringUtils.isNotEmpty(eamJigStandingBookAttachmentList)){
            for (EamJigStandingBookAttachment eamJigStandingBookAttachment : eamJigStandingBookAttachmentList){
                eamJigStandingBookAttachment.setJigStandingBookId(record.getJigStandingBookId());
                eamJigStandingBookAttachment.setCreateUserId(user.getUserId());
                eamJigStandingBookAttachment.setCreateTime(new Date());
                eamJigStandingBookAttachment.setModifiedUserId(user.getUserId());
                eamJigStandingBookAttachment.setModifiedTime(new Date());
                eamJigStandingBookAttachment.setStatus(StringUtils.isEmpty(eamJigStandingBookAttachment.getStatus())?1: eamJigStandingBookAttachment.getStatus());
                eamJigStandingBookAttachment.setOrgId(user.getOrganizationId());
            }
            eamJigStandingBookAttachmentMapper.insertList(eamJigStandingBookAttachmentList);
        }

        EamHtJigStandingBook eamHtJigStandingBook = new EamHtJigStandingBook();
        BeanUtils.copyProperties(record,eamHtJigStandingBook);
        int i = eamHtJigStandingBookMapper.insertSelective(eamHtJigStandingBook);

        return i;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int update(EamJigStandingBook entity) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        this.codeIfRepeat(entity);

        entity.setModifiedUserId(user.getUserId());
        entity.setModifiedTime(new Date());
        entity.setOrgId(user.getOrganizationId());
        EamHtJigStandingBook eamHtJigStandingBook = new EamHtJigStandingBook();
        BeanUtils.copyProperties(entity,eamHtJigStandingBook);
        eamHtJigStandingBookMapper.insertSelective(eamHtJigStandingBook);

        //删除原附件
        Example example1 = new Example(EamJigStandingBookAttachment.class);
        Example.Criteria criteria1 = example1.createCriteria();
        criteria1.andEqualTo("jigStandingBookId", entity.getJigStandingBookId());
        eamJigStandingBookAttachmentMapper.deleteByExample(example1);

        //附件列表
        List<EamJigStandingBookAttachment> eamJigStandingBookAttachmentList = entity.getEamJigStandingBookAttachmentList();
        if(StringUtils.isNotEmpty(eamJigStandingBookAttachmentList)){
            for (EamJigStandingBookAttachment eamJigStandingBookAttachment : eamJigStandingBookAttachmentList){
                eamJigStandingBookAttachment.setJigStandingBookId(entity.getJigStandingBookId());
                eamJigStandingBookAttachment.setCreateUserId(user.getUserId());
                eamJigStandingBookAttachment.setCreateTime(new Date());
                eamJigStandingBookAttachment.setModifiedUserId(user.getUserId());
                eamJigStandingBookAttachment.setModifiedTime(new Date());
                eamJigStandingBookAttachment.setStatus(StringUtils.isEmpty(eamJigStandingBookAttachment.getStatus())?1: eamJigStandingBookAttachment.getStatus());
                eamJigStandingBookAttachment.setOrgId(user.getOrganizationId());
            }
            eamJigStandingBookAttachmentMapper.insertList(eamJigStandingBookAttachmentList);
        }

        return eamJigStandingBookMapper.updateByPrimaryKeySelective(entity);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        List<EamHtJigStandingBook> list = new ArrayList<>();
        String[] idsArr  = ids.split(",");
        for (String id : idsArr) {
            EamJigStandingBook eamJigStandingBook = eamJigStandingBookMapper.selectByPrimaryKey(id);
            if (StringUtils.isEmpty(eamJigStandingBook)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

            EamHtJigStandingBook eamHtJigStandingBook = new EamHtJigStandingBook();
            BeanUtils.copyProperties(eamJigStandingBook,eamHtJigStandingBook);
            list.add(eamHtJigStandingBook);

            //删除附件信息
            Example example1 = new Example(EamJigStandingBookAttachment.class);
            Example.Criteria criteria1 = example1.createCriteria();
            criteria1.andEqualTo("jigStandingBookId", id);
            eamJigStandingBookAttachmentMapper.deleteByExample(example1);
        }

        eamHtJigStandingBookMapper.insertList(list);
        return eamJigStandingBookMapper.deleteByIds(ids);
    }

    private void codeIfRepeat(EamJigStandingBook entity){
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        Example example = new Example(EamJigStandingBook.class);
        Example.Criteria criteria = example.createCriteria();
        //判断编码是否重复
        criteria.andEqualTo("orgId", user.getOrganizationId());
        criteria.andEqualTo("jigBarcodeId",entity.getJigBarcodeId());
        if (StringUtils.isNotEmpty(entity.getJigStandingBookId())){
            criteria.andNotEqualTo("jigStandingBookId",entity.getJigStandingBookId());
        }
        EamJigStandingBook emJigStandingBook = eamJigStandingBookMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(emJigStandingBook)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }
    }


}
