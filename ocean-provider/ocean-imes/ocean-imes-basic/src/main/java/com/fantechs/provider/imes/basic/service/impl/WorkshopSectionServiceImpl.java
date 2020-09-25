package com.fantechs.provider.imes.basic.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.basic.SmtMaterial;
import com.fantechs.common.base.entity.basic.WorkshopSection;
import com.fantechs.common.base.entity.basic.history.HtWorkshopSection;
import com.fantechs.common.base.entity.basic.search.SearchWorkshopSection;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.imes.basic.mapper.HtWorkshopSectionMapper;
import com.fantechs.provider.imes.basic.mapper.WorkshopSectionMapper;
import com.fantechs.provider.imes.basic.service.WorkshopSectionService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2020/09/25.
 */
@Service
public class WorkshopSectionServiceImpl  extends BaseService<WorkshopSection> implements WorkshopSectionService {

    @Resource
    private WorkshopSectionMapper workshopSectionMapper;
    @Resource
    private HtWorkshopSectionMapper htWorkshopSectionMapper;

    @Override
    public List<WorkshopSection> findList(SearchWorkshopSection searchWorkshopSection) {
        return workshopSectionMapper.findList(searchWorkshopSection);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(WorkshopSection workshopSection) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        Example example = new Example(WorkshopSection.class);
        example.createCriteria().andEqualTo("sectionCode",workshopSection.getSectionCode());
        List<WorkshopSection> list = workshopSectionMapper.selectByExample(example);
        if(list !=null && list.size()>0){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }
        workshopSection.setCreateUserId(currentUser.getUserId());
        workshopSection.setCreateTime(new Date());
        int i = workshopSectionMapper.insertSelective(workshopSection);

        //添加工段历史信息

        HtWorkshopSection htWorkshopSection = new HtWorkshopSection();
        BeanUtils.copyProperties(workshopSection,htWorkshopSection);
        htWorkshopSectionMapper.insertSelective(htWorkshopSection);

        return i;
    }

    @Override
    public int update(WorkshopSection workshopSection) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        Example example = new Example(WorkshopSection.class);
        example.createCriteria().andEqualTo("sectionCode",workshopSection.getSectionCode());
        List<WorkshopSection> list = workshopSectionMapper.selectByExample(example);
        if(list !=null && list.size()>0){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }
        workshopSection.setModifiedUserId(currentUser.getUserId());
        workshopSection.setModifiedTime(new Date());
        int i = workshopSectionMapper.updateByPrimaryKeySelective(workshopSection);

        //添加工段历史信息

        HtWorkshopSection htWorkshopSection = new HtWorkshopSection();
        BeanUtils.copyProperties(workshopSection,htWorkshopSection);
        htWorkshopSection.setModifiedTime(new Date());
        htWorkshopSection.setModifiedUserId(currentUser.getUserId());
        htWorkshopSectionMapper.insertSelective(htWorkshopSection);
        return i;
    }

    @Override
    public int batchDelete(String ids) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        String[] idsArr = ids.split(",");
        List<HtWorkshopSection> list = new LinkedList<>();
        for (String item:idsArr) {
            WorkshopSection workshopSection = workshopSectionMapper.selectByPrimaryKey(item);
            if(StringUtils.isEmpty(workshopSection)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            HtWorkshopSection htWorkshopSection = new HtWorkshopSection();
            BeanUtils.copyProperties(workshopSection,htWorkshopSection);
            htWorkshopSection.setModifiedTime(new Date());
            htWorkshopSection.setModifiedUserId(currentUser.getUserId());
            list.add(htWorkshopSection);

            workshopSectionMapper.deleteByPrimaryKey(item);
        }
        htWorkshopSectionMapper.insertList(list);
        return workshopSectionMapper.deleteByIds(ids);
    }
}
