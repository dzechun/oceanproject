package com.fantechs.provider.imes.basic.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.basic.SmtWorkshopSection;
import com.fantechs.common.base.entity.basic.history.SmtHtWorkshopSection;
import com.fantechs.common.base.entity.basic.search.SearchSmtWorkshopSection;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.imes.basic.mapper.SmtHtWorkshopSectionMapper;
import com.fantechs.provider.imes.basic.mapper.SmtWorkshopSectionMapper;
import com.fantechs.provider.imes.basic.service.SmtWorkshopSectionService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * Created by leifengzhi on 2020/09/25.
 */
@Service
public class SmtSmtWorkshopSectionServiceImpl extends BaseService<SmtWorkshopSection> implements SmtWorkshopSectionService {

    @Resource
    private SmtWorkshopSectionMapper workshopSectionMapper;
    @Resource
    private SmtHtWorkshopSectionMapper smtHtWorkshopSectionMapper;

    @Override
    public List<SmtWorkshopSection> findList(SearchSmtWorkshopSection searchSmtWorkshopSection) {
        return workshopSectionMapper.findList(searchSmtWorkshopSection);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(SmtWorkshopSection smtWorkshopSection) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        Example example = new Example(SmtWorkshopSection.class);
        example.createCriteria().andEqualTo("sectionCode", smtWorkshopSection.getSectionCode());
        List<SmtWorkshopSection> list = workshopSectionMapper.selectByExample(example);
        if(list !=null && list.size()>0){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }
        smtWorkshopSection.setCreateUserId(currentUser.getUserId());
        smtWorkshopSection.setCreateTime(new Date());
        int i = workshopSectionMapper.insertSelective(smtWorkshopSection);

        //添加工段历史信息

        SmtHtWorkshopSection smtHtWorkshopSection = new SmtHtWorkshopSection();
        BeanUtils.copyProperties(smtWorkshopSection, smtHtWorkshopSection);
        smtHtWorkshopSectionMapper.insertSelective(smtHtWorkshopSection);

        return i;
    }

    @Override
    public int update(SmtWorkshopSection smtWorkshopSection) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        Example example = new Example(SmtWorkshopSection.class);
        example.createCriteria().andEqualTo("sectionCode", smtWorkshopSection.getSectionCode());
        SmtWorkshopSection workshopSection = workshopSectionMapper.selectOneByExample(example);
        if(StringUtils.isNotEmpty(workshopSection)&&!workshopSection.getSectionId().equals(smtWorkshopSection.getSectionId())){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }
        smtWorkshopSection.setModifiedUserId(currentUser.getUserId());
        smtWorkshopSection.setModifiedTime(new Date());
        int i = workshopSectionMapper.updateByPrimaryKeySelective(smtWorkshopSection);

        //添加工段历史信息

        SmtHtWorkshopSection smtHtWorkshopSection = new SmtHtWorkshopSection();
        BeanUtils.copyProperties(smtWorkshopSection, smtHtWorkshopSection);
        smtHtWorkshopSection.setModifiedTime(new Date());
        smtHtWorkshopSection.setModifiedUserId(currentUser.getUserId());
        smtHtWorkshopSectionMapper.insertSelective(smtHtWorkshopSection);
        return i;
    }

    @Override
    public int batchDelete(String ids) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        String[] idsArr = ids.split(",");
        List<SmtHtWorkshopSection> list = new LinkedList<>();
        for (String item:idsArr) {
            SmtWorkshopSection smtWorkshopSection = workshopSectionMapper.selectByPrimaryKey(item);
            if(StringUtils.isEmpty(smtWorkshopSection)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            SmtHtWorkshopSection smtHtWorkshopSection = new SmtHtWorkshopSection();
            BeanUtils.copyProperties(smtWorkshopSection, smtHtWorkshopSection);
            smtHtWorkshopSection.setModifiedTime(new Date());
            smtHtWorkshopSection.setModifiedUserId(currentUser.getUserId());
            list.add(smtHtWorkshopSection);

            workshopSectionMapper.deleteByPrimaryKey(item);
        }
        smtHtWorkshopSectionMapper.insertList(list);
        return workshopSectionMapper.deleteByIds(ids);
    }
}
