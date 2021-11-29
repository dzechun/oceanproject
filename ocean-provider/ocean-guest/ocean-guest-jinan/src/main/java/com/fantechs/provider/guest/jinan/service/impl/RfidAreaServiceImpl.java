package com.fantechs.provider.guest.jinan.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.jinan.Import.RfidAreaImport;
import com.fantechs.common.base.general.entity.jinan.RfidArea;
import com.fantechs.common.base.general.entity.jinan.history.RfidHtArea;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.guest.jinan.mapper.RfidAreaMapper;
import com.fantechs.provider.guest.jinan.mapper.RfidHtAreaMapper;
import com.fantechs.provider.guest.jinan.service.RfidAreaService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;

/**
 *
 * Created by leifengzhi on 2021/11/29.
 */
@Service
public class RfidAreaServiceImpl extends BaseService<RfidArea> implements RfidAreaService {

    @Resource
    private RfidAreaMapper rfidAreaMapper;
    @Resource
    private RfidHtAreaMapper rfidHtAreaMapper;

    @Override
    public List<RfidArea> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId",user.getOrganizationId());
        return rfidAreaMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int save(RfidArea record) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        Example example = new Example(RfidArea.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("areaCode", record.getAreaCode())
                .andEqualTo("orgId", user.getOrganizationId());
        RfidArea rfidArea = rfidAreaMapper.selectOneByExample(example);
        if(StringUtils.isNotEmpty(rfidArea)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        record.setCreateUserId(user.getUserId());
        record.setCreateTime(new Date());
        record.setModifiedUserId(user.getUserId());
        record.setModifiedTime(new Date());
        record.setStatus(StringUtils.isEmpty(record.getStatus())?1: record.getStatus());
        record.setOrgId(user.getOrganizationId());
        rfidAreaMapper.insertUseGeneratedKeys(record);

        RfidHtArea rfidHtArea = new RfidHtArea();
        BeanUtils.copyProperties(record, rfidHtArea);
        int i = rfidHtAreaMapper.insertSelective(rfidHtArea);

        return i;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int update(RfidArea entity) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        Example example = new Example(RfidArea.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("areaCode", entity.getAreaCode())
                .andEqualTo("orgId", user.getOrganizationId())
                .andNotEqualTo("areaId",entity.getAreaId());
        RfidArea rfidArea = rfidAreaMapper.selectOneByExample(example);
        if(StringUtils.isNotEmpty(rfidArea)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        entity.setModifiedUserId(user.getUserId());
        entity.setModifiedTime(new Date());
        rfidAreaMapper.updateByPrimaryKeySelective(entity);

        RfidHtArea rfidHtArea = new RfidHtArea();
        BeanUtils.copyProperties(entity, rfidHtArea);
        int i = rfidHtAreaMapper.insertSelective(rfidHtArea);

        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importExcel(List<RfidAreaImport> rfidAreaImports) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        Map<String, Object> resultMap = new HashMap<>();  //封装操作结果
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数
        LinkedList<RfidArea> list = new LinkedList<>();
        LinkedList<RfidHtArea> htList = new LinkedList<>();
        for (int i = 0; i < rfidAreaImports.size(); i++) {
            RfidAreaImport rfidAreaImport = rfidAreaImports.get(i);

            String areaCode = rfidAreaImport.getAreaCode();
            String areaName = rfidAreaImport.getAreaName();
            if (StringUtils.isEmpty(
                    areaCode,areaName
            )){
                fail.add(i+4);
                continue;
            }

            //判断编码是否重复
            Example example = new Example(RfidArea.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("orgId", user.getOrganizationId());
            criteria.andEqualTo("areaCode",areaCode);
            if (StringUtils.isNotEmpty(rfidAreaMapper.selectOneByExample(example))){
                fail.add(i+4);
                continue;
            }

            //判断集合中是否已经存在同样的数据
            boolean tag = false;
            if (StringUtils.isNotEmpty(list)){
                for (RfidArea rfidArea : list) {
                    if (rfidArea.getAreaCode().equals(areaCode)){
                        tag = true;
                    }
                }
            }
            if (tag){
                fail.add(i+4);
                continue;
            }


            RfidArea rfidArea = new RfidArea();
            BeanUtils.copyProperties(rfidAreaImport, rfidArea);
            rfidArea.setCreateTime(new Date());
            rfidArea.setCreateUserId(user.getUserId());
            rfidArea.setModifiedTime(new Date());
            rfidArea.setModifiedUserId(user.getUserId());
            rfidArea.setStatus((byte)1);
            rfidArea.setOrgId(user.getOrganizationId());
            list.add(rfidArea);
        }

        if (StringUtils.isNotEmpty(list)) {
            success = rfidAreaMapper.insertList(list);

            for (RfidArea rfidArea : list) {
                RfidHtArea rfidHtArea = new RfidHtArea();
                BeanUtils.copyProperties(rfidArea, rfidHtArea);
                htList.add(rfidHtArea);
            }
            if (StringUtils.isNotEmpty(htList)) {
                rfidHtAreaMapper.insertList(htList);
            }
        }

        resultMap.put("操作成功总数",success);
        resultMap.put("操作失败行数",fail);
        return resultMap;
    }
}
