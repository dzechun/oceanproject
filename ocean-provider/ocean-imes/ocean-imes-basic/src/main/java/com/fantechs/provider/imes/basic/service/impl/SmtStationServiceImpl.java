package com.fantechs.provider.imes.basic.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.dto.basic.SmtProcessCategoryDto;
import com.fantechs.common.base.entity.basic.*;
import com.fantechs.common.base.entity.basic.history.SmtHtProcessCategory;
import com.fantechs.common.base.entity.basic.history.SmtHtStation;
import com.fantechs.common.base.entity.basic.search.SearchSmtStation;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.imes.basic.mapper.SmtHtStationMapper;
import com.fantechs.provider.imes.basic.mapper.SmtProcessMapper;
import com.fantechs.provider.imes.basic.mapper.SmtStationMapper;
import com.fantechs.provider.imes.basic.mapper.SmtWorkshopSectionMapper;
import com.fantechs.provider.imes.basic.service.SmtStationService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import java.util.*;

/**
 *
 * Created by wcz on 2020/09/27.
 */
@Service
public class SmtStationServiceImpl  extends BaseService<SmtStation> implements SmtStationService {

    @Resource
    private SmtStationMapper smtStationMapper;
    @Resource
    private SmtHtStationMapper smtHtStationMapper;
    @Resource
    private SmtWorkshopSectionMapper smtWorkshopSectionMapper;
    @Resource
    private SmtProcessMapper smtProcessMapper;

    @Override
    public List<SmtStation> findList(SearchSmtStation searchSmtStation) {
        return smtStationMapper.findList(searchSmtStation);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(SmtStation smtStation) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(SmtStation.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("stationCode",smtStation.getStationCode());
        List<SmtStation> smtStations = smtStationMapper.selectByExample(example);
        if(StringUtils.isNotEmpty(smtStations)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        smtStation.setCreateUserId(currentUser.getUserId());
        smtStation.setCreateTime(new Date());
        smtStation.setModifiedUserId(currentUser.getUserId());
        smtStation.setModifiedTime(new Date());
        smtStationMapper.insertUseGeneratedKeys(smtStation);

        //新增工位历史信息
        SmtHtStation smtHtStation=new SmtHtStation();
        BeanUtils.copyProperties(smtStation,smtHtStation);
        int i = smtHtStationMapper.insertSelective(smtHtStation);
        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(String ids) {
        int i=0;
        List<SmtHtStation> list=new ArrayList<>();

        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        String[] stationIds = ids.split(",");
        for (String stationId : stationIds) {
            SmtStation smtStation = smtStationMapper.selectByPrimaryKey(stationId);
            if(StringUtils.isEmpty(smtStation)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            //新增工位历史信息
            SmtHtStation smtHtStation=new SmtHtStation();
            BeanUtils.copyProperties(smtStation,smtHtStation);
            smtHtStation.setModifiedUserId(currentUser.getUserId());
            smtHtStation.setModifiedTime(new Date());
            list.add(smtHtStation);
        }
        smtHtStationMapper.insertList(list);

        return smtStationMapper.deleteByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(SmtStation smtStation) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(SmtStation.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("stationCode",smtStation.getStationCode());

        SmtStation station = smtStationMapper.selectOneByExample(example);

        if(StringUtils.isNotEmpty(station)&&!station.getStationId().equals(smtStation.getStationId())){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        smtStation.setModifiedUserId(currentUser.getUserId());
        smtStation.setModifiedTime(new Date());
        int i= smtStationMapper.updateByPrimaryKeySelective(smtStation);

        //新增工位历史信息
        SmtHtStation smtHtStation=new SmtHtStation();
        BeanUtils.copyProperties(smtStation,smtHtStation);
        smtHtStationMapper.insertSelective(smtHtStation);
        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importExcel(List<SmtStation> smtStations) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        Map<String, Object> resutlMap = new HashMap<>();  //封装操作结果
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数
        LinkedList<SmtStation> list = new LinkedList<>();
        LinkedList<SmtHtStation> htList = new LinkedList<>();
        for (int i = 0; i < smtStations.size(); i++) {
            SmtStation smtStation = smtStations.get(i);
            String stationCode = smtStation.getStationCode();
            String sectionCode = smtStation.getSectionCode();
            String processCode = smtStation.getProcessCode();

            if (StringUtils.isEmpty(
                    stationCode,sectionCode,processCode
            )){
                fail.add(i+3);
                continue;
            }

            //判断编码是否重复
            Example example = new Example(SmtStation.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("stationCode",stationCode);
            if (StringUtils.isNotEmpty(smtStationMapper.selectOneByExample(example))){
                fail.add(i+3);
                continue;
            }

            //判断工段是否存在
            Example example1 = new Example(SmtWorkshopSection.class);
            Example.Criteria criteria1 = example1.createCriteria();
            criteria1.andEqualTo("sectionCode",sectionCode);
            SmtWorkshopSection smtWorkshopSection = smtWorkshopSectionMapper.selectOneByExample(example1);
            if (StringUtils.isEmpty(smtWorkshopSection)){
                fail.add(i+3);
                continue;
            }

            //判断工序是否存在
            Example example2 = new Example(SmtProcess.class);
            Example.Criteria criteria2 = example2.createCriteria();
            criteria2.andEqualTo("processCode",processCode);
            SmtProcess smtProcess = smtProcessMapper.selectOneByExample(example2);
            if (StringUtils.isEmpty(smtProcess)){
                fail.add(i+3);
                continue;
            }

            smtStation.setCreateTime(new Date());
            smtStation.setCreateUserId(currentUser.getUserId());
            smtStation.setModifiedTime(new Date());
            smtStation.setModifiedUserId(currentUser.getUserId());
            smtStation.setStatus(1);
            smtStation.setProcessId(smtProcess.getProcessId());
            smtStation.setSectionId(smtWorkshopSection.getSectionId());
            list.add(smtStation);
        }

        if (StringUtils.isNotEmpty(list)){
            success = smtStationMapper.insertList(list);
        }

        for (SmtStation smtStation : list) {
            SmtHtStation smtHtStation = new SmtHtStation();
            BeanUtils.copyProperties(smtStation,smtHtStation);
            htList.add(smtHtStation);
        }

        if (StringUtils.isNotEmpty(htList)){
            smtHtStationMapper.insertList(htList);
        }
        resutlMap.put("操作成功总数",success);
        resutlMap.put("操作失败行数",fail);
        return resutlMap;
    }

}
