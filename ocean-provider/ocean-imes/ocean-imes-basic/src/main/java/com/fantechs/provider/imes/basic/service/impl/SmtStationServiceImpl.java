package com.fantechs.provider.imes.basic.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.basic.SmtStation;
import com.fantechs.common.base.entity.basic.history.SmtHtStation;
import com.fantechs.common.base.entity.basic.search.SearchSmtStation;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.imes.basic.mapper.SmtHtStationMapper;
import com.fantechs.provider.imes.basic.mapper.SmtStationMapper;
import com.fantechs.provider.imes.basic.service.SmtStationService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

}
