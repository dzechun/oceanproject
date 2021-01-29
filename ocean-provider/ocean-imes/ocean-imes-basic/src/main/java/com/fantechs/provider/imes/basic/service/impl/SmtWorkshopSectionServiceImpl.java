package com.fantechs.provider.imes.basic.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.dto.basic.SmtFactoryDto;
import com.fantechs.common.base.entity.basic.SmtFactory;
import com.fantechs.common.base.entity.basic.SmtProcess;
import com.fantechs.common.base.entity.basic.SmtWorkShop;
import com.fantechs.common.base.entity.basic.SmtWorkshopSection;
import com.fantechs.common.base.entity.basic.history.SmtHtFactory;
import com.fantechs.common.base.entity.basic.history.SmtHtWorkshopSection;
import com.fantechs.common.base.entity.basic.search.SearchSmtWorkshopSection;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.imes.basic.mapper.SmtHtWorkshopSectionMapper;
import com.fantechs.provider.imes.basic.mapper.SmtProcessMapper;
import com.fantechs.provider.imes.basic.mapper.SmtWorkshopSectionMapper;
import com.fantechs.provider.imes.basic.service.SmtWorkshopSectionService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import java.util.*;

/**
 *
 * Created by leifengzhi on 2020/09/25.
 */
@Service
public class SmtWorkshopSectionServiceImpl extends BaseService<SmtWorkshopSection> implements SmtWorkshopSectionService {

    @Resource
    private SmtWorkshopSectionMapper workshopSectionMapper;
    @Resource
    private SmtHtWorkshopSectionMapper smtHtWorkshopSectionMapper;
    @Resource
    private SmtProcessMapper smtProcessMapper;

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
        smtWorkshopSection.setModifiedTime(new Date());
        smtWorkshopSection.setModifiedUserId(currentUser.getUserId());
        workshopSectionMapper.insertUseGeneratedKeys(smtWorkshopSection);

        //添加工段历史信息

        SmtHtWorkshopSection smtHtWorkshopSection = new SmtHtWorkshopSection();
        BeanUtils.copyProperties(smtWorkshopSection, smtHtWorkshopSection);
        int i=  smtHtWorkshopSectionMapper.insertSelective(smtHtWorkshopSection);

        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
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
        smtHtWorkshopSectionMapper.insertSelective(smtHtWorkshopSection);
        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
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

            //被工序引用
            Example example = new Example(SmtProcess.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("sectionId",smtWorkshopSection.getSectionId());
            List<SmtProcess> smtProcesses = smtProcessMapper.selectByExample(example);
            if(StringUtils.isNotEmpty(smtProcesses)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012004);
            }

            SmtHtWorkshopSection smtHtWorkshopSection = new SmtHtWorkshopSection();
            BeanUtils.copyProperties(smtWorkshopSection, smtHtWorkshopSection);
            smtHtWorkshopSection.setModifiedTime(new Date());
            smtHtWorkshopSection.setModifiedUserId(currentUser.getUserId());
            list.add(smtHtWorkshopSection);
        }
        smtHtWorkshopSectionMapper.insertList(list);
        return workshopSectionMapper.deleteByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importExcel(List<SmtWorkshopSection> smtWorkshopSections) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        Map<String, Object> resutlMap = new HashMap<>();  //封装操作结果
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数
        LinkedList<SmtWorkshopSection> list = new LinkedList<>();
        LinkedList<SmtHtWorkshopSection> htList = new LinkedList<>();
        for (int i = 0; i < smtWorkshopSections.size(); i++) {
            SmtWorkshopSection smtWorkshopSection = smtWorkshopSections.get(i);
            String sectionCode = smtWorkshopSection.getSectionCode();
            String sectionName = smtWorkshopSection.getSectionName();
            if (StringUtils.isEmpty(
                    sectionCode,sectionName
            )){
                fail.add(i+3);
                continue;
            }

            //判断编码是否重复
            Example example = new Example(SmtWorkshopSection.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("sectionCode",sectionCode);
            if (StringUtils.isNotEmpty(workshopSectionMapper.selectOneByExample(example))){
                fail.add(i+3);
                continue;
            }

            smtWorkshopSection.setCreateTime(new Date());
            smtWorkshopSection.setCreateUserId(currentUser.getUserId());
            smtWorkshopSection.setModifiedTime(new Date());
            smtWorkshopSection.setModifiedUserId(currentUser.getUserId());
            smtWorkshopSection.setStatus((byte) 1);
            list.add(smtWorkshopSection);
        }

        if (StringUtils.isNotEmpty(list)){
            success = workshopSectionMapper.insertList(list);
        }

        for (SmtWorkshopSection smtWorkshopSection : list) {
            SmtHtWorkshopSection smtHtWorkshopSection = new SmtHtWorkshopSection();
            BeanUtils.copyProperties(smtWorkshopSection,smtHtWorkshopSection);
            htList.add(smtHtWorkshopSection);
        }

        if (StringUtils.isNotEmpty(htList)){
            smtHtWorkshopSectionMapper.insertList(htList);
        }
        resutlMap.put("操作成功总数",success);
        resutlMap.put("操作失败行数",fail);
        return resutlMap;
    }
}
