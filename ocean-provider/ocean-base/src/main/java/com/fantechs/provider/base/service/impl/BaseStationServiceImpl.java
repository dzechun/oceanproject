package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.imports.BaseStationImport;
import com.fantechs.common.base.general.entity.basic.BaseProcess;
import com.fantechs.common.base.general.entity.basic.BaseStation;
import com.fantechs.common.base.general.entity.basic.BaseWorkshopSection;
import com.fantechs.common.base.general.entity.basic.history.BaseHtStation;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.BaseHtStationMapper;
import com.fantechs.provider.base.mapper.BaseProcessMapper;
import com.fantechs.provider.base.mapper.BaseStationMapper;
import com.fantechs.provider.base.mapper.BaseWorkshopSectionMapper;
import com.fantechs.provider.base.service.BaseStationService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;

/**
 *
 * Created by wcz on 2020/09/27.
 */
@Service
public class BaseStationServiceImpl extends BaseService<BaseStation> implements BaseStationService {

    @Resource
    private BaseStationMapper baseStationMapper;
    @Resource
    private BaseHtStationMapper baseHtStationMapper;
    @Resource
    private BaseWorkshopSectionMapper baseWorkshopSectionMapper;
    @Resource
    private BaseProcessMapper baseProcessMapper;

    @Override
    public List<BaseStation> findList(Map<String, Object> map) {
        if(StringUtils.isEmpty(map.get("orgId"))) {
            SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
            map.put("orgId", user.getOrganizationId());
        }
        return baseStationMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(BaseStation baseStation) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();

        Example example = new Example(BaseStation.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("organizationId", currentUser.getOrganizationId());
        criteria.andEqualTo("stationCode", baseStation.getStationCode());
        List<BaseStation> baseStations = baseStationMapper.selectByExample(example);
        if(StringUtils.isNotEmpty(baseStations)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        baseStation.setCreateUserId(currentUser.getUserId());
        baseStation.setCreateTime(new Date());
        baseStation.setModifiedUserId(currentUser.getUserId());
        baseStation.setModifiedTime(new Date());
        baseStation.setOrganizationId(currentUser.getOrganizationId());
        baseStationMapper.insertUseGeneratedKeys(baseStation);

        //新增工位历史信息
        BaseHtStation baseHtStation =new BaseHtStation();
        BeanUtils.copyProperties(baseStation, baseHtStation);
        int i = baseHtStationMapper.insertSelective(baseHtStation);
        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(String ids) {
        int i=0;
        List<BaseHtStation> list=new ArrayList<>();

        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();

        String[] stationIds = ids.split(",");
        for (String stationId : stationIds) {
            BaseStation baseStation = baseStationMapper.selectByPrimaryKey(stationId);
            if(StringUtils.isEmpty(baseStation)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            //新增工位历史信息
            BaseHtStation baseHtStation =new BaseHtStation();
            BeanUtils.copyProperties(baseStation, baseHtStation);
            baseHtStation.setModifiedUserId(currentUser.getUserId());
            baseHtStation.setModifiedTime(new Date());
            list.add(baseHtStation);
        }
        baseHtStationMapper.insertList(list);

        return baseStationMapper.deleteByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(BaseStation baseStation) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();

        Example example = new Example(BaseStation.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("organizationId", currentUser.getOrganizationId());
        criteria.andEqualTo("stationCode", baseStation.getStationCode());

        BaseStation station = baseStationMapper.selectOneByExample(example);

        if(StringUtils.isNotEmpty(station)&&!station.getStationId().equals(baseStation.getStationId())){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        baseStation.setModifiedUserId(currentUser.getUserId());
        baseStation.setModifiedTime(new Date());
        int i= baseStationMapper.updateByPrimaryKeySelective(baseStation);

        //新增工位历史信息
        BaseHtStation baseHtStation =new BaseHtStation();
        BeanUtils.copyProperties(baseStation, baseHtStation);
        baseHtStationMapper.insertSelective(baseHtStation);
        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importExcel(List<BaseStationImport> baseStationImports) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        Map<String, Object> resutlMap = new HashMap<>();  //封装操作结果
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数
        LinkedList<BaseStation> list = new LinkedList<>();
        LinkedList<BaseHtStation> htList = new LinkedList<>();
        ArrayList<BaseStationImport> baseStationImportList = new ArrayList<>();
        for (int i = 0; i < baseStationImports.size(); i++) {
            BaseStationImport baseStationImport = baseStationImports.get(i);
            String stationCode = baseStationImport.getStationCode();
            String stationName = baseStationImport.getStationName();
            String sectionCode = baseStationImport.getSectionCode();
            String processCode = baseStationImport.getProcessCode();

            if (StringUtils.isEmpty(
                    stationCode,stationName,sectionCode,processCode
            )){
                fail.add(i+3);
                continue;
            }

            //判断编码是否重复
            Example example = new Example(BaseStation.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("organizationId", currentUser.getOrganizationId());
            criteria.andEqualTo("stationCode",stationCode);
            if (StringUtils.isNotEmpty(baseStationMapper.selectOneByExample(example))){
                fail.add(i+3);
                continue;
            }


            //判断工段是否存在
            Example example1 = new Example(BaseWorkshopSection.class);
            Example.Criteria criteria1 = example1.createCriteria();
            criteria1.andEqualTo("organizationId", currentUser.getOrganizationId());
            criteria1.andEqualTo("sectionCode",sectionCode);
            BaseWorkshopSection baseWorkshopSection = baseWorkshopSectionMapper.selectOneByExample(example1);
            if (StringUtils.isEmpty(baseWorkshopSection)){
                fail.add(i+3);
                continue;
            }
            baseStationImport.setSectionId(baseWorkshopSection.getSectionId());


            //判断工序是否存在
            Example example2 = new Example(BaseProcess.class);
            Example.Criteria criteria2 = example2.createCriteria();
            criteria2.andEqualTo("organizationId", currentUser.getOrganizationId());
            criteria2.andEqualTo("processCode", processCode);
            BaseProcess baseProcess = baseProcessMapper.selectOneByExample(example2);
            if (StringUtils.isEmpty(baseProcess)) {
                fail.add(i + 3);
                continue;
            }
            baseStationImport.setProcessId(baseProcess.getProcessId());

            baseStationImportList.add(baseStationImport);
        }

        for (BaseStationImport baseStationImport : baseStationImportList){
            BaseStation baseStation = new BaseStation();
            BeanUtils.copyProperties(baseStationImport, baseStation);
            baseStation.setCreateTime(new Date());
            baseStation.setCreateUserId(currentUser.getUserId());
            baseStation.setModifiedTime(new Date());
            baseStation.setModifiedUserId(currentUser.getUserId());
            baseStation.setOrganizationId(currentUser.getOrganizationId());
            list.add(baseStation);
        }

        if (StringUtils.isNotEmpty(list)){
            success = baseStationMapper.insertList(list);
        }

        for (BaseStation baseStation : list) {
            BaseHtStation baseHtStation = new BaseHtStation();
            BeanUtils.copyProperties(baseStation, baseHtStation);
            htList.add(baseHtStation);
        }

        if (StringUtils.isNotEmpty(htList)){
            baseHtStationMapper.insertList(htList);
        }
        resutlMap.put("操作成功总数",success);
        resutlMap.put("操作失败行数",fail);
        return resutlMap;
    }

}
