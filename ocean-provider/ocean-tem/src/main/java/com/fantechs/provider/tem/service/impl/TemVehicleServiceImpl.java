package com.fantechs.provider.tem.service.impl;


import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.tem.TemVehicleDto;
import com.fantechs.common.base.general.dto.tem.TemVehicleImport;
import com.fantechs.common.base.general.entity.tem.TemVehicle;
import com.fantechs.common.base.general.entity.tem.history.TemHtVehicle;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.BeanUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.tem.mapper.TemHtVehicleMapper;
import com.fantechs.provider.tem.mapper.TemVehicleMapper;
import com.fantechs.provider.tem.service.TemVehicleService;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;

@Service
public class TemVehicleServiceImpl extends BaseService<TemVehicle> implements TemVehicleService {

    @Resource
    private TemVehicleMapper temVehicleMapper;

    @Resource
    private TemHtVehicleMapper temHtVehicleMapper;

    @Override
    public List<TemVehicleDto> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId", user.getOrganizationId());
        return temVehicleMapper.findList(map);
    }

    @Override
    public List<TemHtVehicle> findHtList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId", user.getOrganizationId());
        return temHtVehicleMapper.findList(map);
    }

    @Override
    @Transactional
    @GlobalTransactional
    public int save(TemVehicle temVehicle) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        Example example = new Example(TemVehicle.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("orgId", currentUser.getOrganizationId());
        criteria.andEqualTo("vehicleCode", temVehicle.getVehicleCode());
        TemVehicle temVehicle2 = temVehicleMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(temVehicle2)) {
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }
        temVehicle.setCreateUserId(currentUser.getUserId());
        temVehicle.setCreateTime(new Date());
        temVehicle.setModifiedUserId(currentUser.getUserId());
        temVehicle.setModifiedTime(new Date());
        temVehicle.setVehicleStatus((byte) 1);
        temVehicle.setStatus((byte) 1);
        temVehicle.setOrgId(StringUtils.isEmpty(temVehicle.getOrgId()) ? currentUser.getOrganizationId() : temVehicle.getOrgId());
        temVehicleMapper.insertUseGeneratedKeys(temVehicle);

        TemHtVehicle temHtVehicle = new TemHtVehicle();
        BeanUtils.autoFillEqFields(temVehicle, temHtVehicle);
        temHtVehicleMapper.insertSelective(temHtVehicle);

        return 1;
    }

    @Override
    @Transactional
    @GlobalTransactional
    public int delete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        String[] idsArr = ids.split(",");
        List<TemHtVehicle> temHtVehicleList = new LinkedList<>();
        for (String id : idsArr) {
            TemVehicle temVehicle = temVehicleMapper.selectByPrimaryKey(Long.valueOf(id));
            TemHtVehicle temHtVehicle = new TemHtVehicle();
            BeanUtils.autoFillEqFields(temVehicle, temHtVehicle);
            temHtVehicleList.add(temHtVehicle);
        }
        temHtVehicleMapper.insertList(temHtVehicleList);

        return temVehicleMapper.deleteByIds(ids);
    }

    @Override
    @Transactional
    @GlobalTransactional
    public int update(TemVehicle temVehicle) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();

        Example example = new Example(TemVehicle.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("orgId", currentUser.getOrganizationId());
        criteria.andEqualTo("vehicleCode", temVehicle.getVehicleCode());
        criteria.andNotEqualTo("vehicleId", temVehicle.getVehicleId());
        TemVehicle temVehicle2 = temVehicleMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(temVehicle2)) {
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        temVehicle.setModifiedUserId(currentUser.getUserId());
        temVehicle.setModifiedTime(new Date());
        temVehicleMapper.updateByPrimaryKeySelective(temVehicle);

        TemHtVehicle temHtVehicle = new TemHtVehicle();
        BeanUtils.autoFillEqFields(temVehicle, temHtVehicle);
        temHtVehicleMapper.insertSelective(temHtVehicle);

        return 1;
    }

    @Override
    public Map<String, Object> importExcel(List<TemVehicleImport> temVehicleImportList) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        Map<String, Object> resultMap = new HashMap<>();  //封装操作结果
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数
        List<TemVehicle> list = new LinkedList<>();
        List<TemHtVehicle> htList = new LinkedList<>();
        List<String> temVehicleCodeList = new LinkedList<>();

        for (int i = 0; i < temVehicleImportList.size(); i++) {
            TemVehicleImport temVehicleImport = temVehicleImportList.get(i);
            if (StringUtils.isEmpty(
                    temVehicleImport.getVehicleCode(),
                    temVehicleImport.getVehicleName(),
                    temVehicleImport.getAgvTaskTemplate())) {

                fail.add(i+4);
                continue;
            }
            if (temVehicleCodeList.contains(temVehicleImport.getVehicleCode())){
                fail.add(i+4);
                continue;
            } else {
                temVehicleCodeList.add(temVehicleImport.getVehicleCode());
            }
            Example example = new Example(TemVehicle.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("orgId", user.getOrganizationId());
            criteria.andEqualTo("vehicleCode", temVehicleImport.getVehicleCode());
            if (StringUtils.isNotEmpty(temVehicleMapper.selectOneByExample(example))) {
                fail.add(i+4);
                continue;
            }

            TemVehicle temVehicle = new TemVehicle();
            BeanUtils.autoFillEqFields(temVehicleImport, temVehicle);
            temVehicle.setVehicleStatus((byte) 1);
            temVehicle.setStatus((byte) 1);
            temVehicle.setCreateUserId(user.getUserId());
            temVehicle.setCreateTime(new Date());
            temVehicle.setModifiedUserId(user.getUserId());
            temVehicle.setModifiedTime(new Date());
            temVehicle.setVehicleStatus((byte) 1);
            temVehicle.setStatus((byte) 1);
            temVehicle.setOrgId(user.getOrganizationId());
            list.add(temVehicle);
        }

        if (StringUtils.isNotEmpty(list)) {
            success = temVehicleMapper.insertList(list);
            for (TemVehicle temVehicle : list) {
                TemHtVehicle temHtVehicle = new TemHtVehicle();
                BeanUtils.autoFillEqFields(temVehicle, temHtVehicle);
                htList.add(temHtVehicle);
            }
            temHtVehicleMapper.insertList(htList);
        }

        resultMap.put("操作成功总数",success);
        resultMap.put("操作失败行数",fail);
        return resultMap;
    }
}
