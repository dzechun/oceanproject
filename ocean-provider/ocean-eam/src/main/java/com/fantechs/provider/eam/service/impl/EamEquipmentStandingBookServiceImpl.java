package com.fantechs.provider.eam.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.eam.EamEquipmentStandingBookDto;
import com.fantechs.common.base.general.entity.eam.EamEquipmentStandingBook;
import com.fantechs.common.base.general.entity.eam.EamEquipmentStandingBookAttachment;
import com.fantechs.common.base.general.entity.eam.history.EamHtEquipmentStandingBook;
import com.fantechs.common.base.general.entity.eam.history.EamHtEquipmentStandingBookAttachment;
import com.fantechs.common.base.general.entity.eam.search.SearchEamEquipmentStandingBook;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.eam.mapper.EamEquipmentStandingBookAttachmentMapper;
import com.fantechs.provider.eam.mapper.EamEquipmentStandingBookMapper;
import com.fantechs.provider.eam.mapper.EamHtEquipmentStandingBookAttachmentMapper;
import com.fantechs.provider.eam.mapper.EamHtEquipmentStandingBookMapper;
import com.fantechs.provider.eam.service.EamEquipmentStandingBookService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 *
 * Created by leifengzhi on 2021/08/20.
 */
@Service
public class EamEquipmentStandingBookServiceImpl extends BaseService<EamEquipmentStandingBook> implements EamEquipmentStandingBookService {

    @Resource
    private EamEquipmentStandingBookMapper eamEquipmentStandingBookMapper;
    @Resource
    private EamHtEquipmentStandingBookMapper eamHtEquipmentStandingBookMapper;
    @Resource
    private EamHtEquipmentStandingBookAttachmentMapper eamHtEquipmentStandingBookAttachmentMapper;
    @Resource
    private EamEquipmentStandingBookAttachmentMapper eamEquipmentStandingBookAttachmentMapper;


    @Override
    public List<EamEquipmentStandingBookDto> findList(SearchEamEquipmentStandingBook searchEamEquipmentStandingBook) {
        if(StringUtils.isEmpty(searchEamEquipmentStandingBook.getOrgId())){
            SysUser sysUser = currentUser();
            searchEamEquipmentStandingBook.setOrgId(sysUser.getOrganizationId());
        }
        return eamEquipmentStandingBookMapper.findList(searchEamEquipmentStandingBook);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int save(EamEquipmentStandingBookDto eamEquipmentStandingBookDto) {
        SysUser user = currentUser();
        if(StringUtils.isEmpty(eamEquipmentStandingBookDto.getEquipmentBarcodeId())) throw new BizErrorException("设备条码不能为空");
        Example example = new Example(EamEquipmentStandingBook.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("equipmentBarcodeId", eamEquipmentStandingBookDto.getEquipmentBarcodeId());
        criteria.andEqualTo("orgId", user.getOrganizationId());
        EamEquipmentStandingBook eamEquipmentStandingBook = eamEquipmentStandingBookMapper.selectOneByExample(example);
        example.clear();
        if (StringUtils.isNotEmpty(eamEquipmentStandingBook)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        //保存主表
        eamEquipmentStandingBookDto.setCreateUserId(user.getUserId());
        eamEquipmentStandingBookDto.setCreateTime(new Date());
        eamEquipmentStandingBookDto.setModifiedUserId(user.getUserId());
        eamEquipmentStandingBookDto.setModifiedTime(new Date());
        eamEquipmentStandingBookDto.setStatus((byte)1);
        eamEquipmentStandingBookDto.setOrgId(user.getOrganizationId());
        eamEquipmentStandingBookMapper.insertUseGeneratedKeys(eamEquipmentStandingBookDto);

        EamHtEquipmentStandingBook eamHtEquipmentStandingBook = new EamHtEquipmentStandingBook();
        BeanUtils.copyProperties(eamEquipmentStandingBookDto, eamHtEquipmentStandingBook);
        int i = eamHtEquipmentStandingBookMapper.insertUseGeneratedKeys(eamHtEquipmentStandingBook);

        //保存明细
        saveFile( eamEquipmentStandingBookDto,  user);
        return i;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int update(EamEquipmentStandingBookDto eamEquipmentStandingBookDto) {
        SysUser user = currentUser();
        if(StringUtils.isEmpty(eamEquipmentStandingBookDto.getEquipmentBarcodeId()))
            throw new BizErrorException("设备条码不能为空");
        eamEquipmentStandingBookMapper.updateByPrimaryKeySelective(eamEquipmentStandingBookDto);

        Example example = new Example(EamEquipmentStandingBook.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("equipmentBarcodeId", eamEquipmentStandingBookDto.getEquipmentBarcodeId());
        criteria.andEqualTo("orgId", user.getOrganizationId());
        EamEquipmentStandingBook eamEquipmentStandingBook = eamEquipmentStandingBookMapper.selectOneByExample(example);

        //保存履历表
        EamHtEquipmentStandingBook eamHtEquipmentStandingBook = new EamHtEquipmentStandingBook();
        BeanUtils.copyProperties(eamEquipmentStandingBook, eamHtEquipmentStandingBook);
        int i = eamHtEquipmentStandingBookMapper.insertUseGeneratedKeys(eamHtEquipmentStandingBook);
        example.clear();

        Example fileExample = new Example(EamEquipmentStandingBookAttachment.class);
        Example.Criteria fileCriteria = fileExample.createCriteria();
        fileCriteria.andEqualTo("equipmentStandingBookId", eamEquipmentStandingBookDto.getEquipmentStandingBookId());
        eamEquipmentStandingBookAttachmentMapper.deleteByExample(fileExample);
        saveFile(eamEquipmentStandingBookDto,user);
        fileExample.clear();
        return i;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int batchDelete(String ids) {
        SysUser user = currentUser();
        String[] idsArr = ids.split(",");
        for (String id : idsArr) {
            Example fileExample = new Example(EamEquipmentStandingBookAttachment.class);
            Example.Criteria fileCriteria = fileExample.createCriteria();
            fileCriteria.andEqualTo("equipmentStandingBookId", id);
            eamEquipmentStandingBookAttachmentMapper.deleteByExample(fileExample);
            fileExample.clear();
        }

        return eamEquipmentStandingBookMapper.deleteByIds(ids);
    }

    public void saveFile(EamEquipmentStandingBookDto eamEquipmentStandingBookDto, SysUser user){
        if (StringUtils.isNotEmpty(eamEquipmentStandingBookDto.getEamEquipmentStandingBookAttachments())) {
            for(EamEquipmentStandingBookAttachment file : eamEquipmentStandingBookDto.getEamEquipmentStandingBookAttachments()) {
                file.setEquipmentStandingBookId(eamEquipmentStandingBookDto.getEquipmentStandingBookId());
                file.setCreateUserId(user.getUserId());
                file.setCreateTime(new Date());
                file.setModifiedUserId(user.getUserId());
                file.setModifiedTime(new Date());
                file.setStatus(StringUtils.isEmpty(file.getStatus()) ? 1 : file.getStatus());
                file.setOrgId(user.getOrganizationId());
                eamEquipmentStandingBookAttachmentMapper.insertUseGeneratedKeys(file);
                EamHtEquipmentStandingBookAttachment eamHtFile = new EamHtEquipmentStandingBookAttachment();
                BeanUtils.copyProperties(file, eamHtFile);
                eamHtEquipmentStandingBookAttachmentMapper.insertUseGeneratedKeys(eamHtFile);
            }
        }
    }

    /**
     * 获取当前登录用户
     * @return
     */
    private SysUser currentUser(){
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        return user;
    }
}
