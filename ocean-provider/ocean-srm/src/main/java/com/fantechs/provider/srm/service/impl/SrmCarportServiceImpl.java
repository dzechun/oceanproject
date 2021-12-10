package com.fantechs.provider.srm.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.srm.SrmCarportDto;
import com.fantechs.common.base.general.entity.srm.SrmCarport;
import com.fantechs.common.base.general.entity.srm.SrmCarportTimeQuantum;
import com.fantechs.common.base.general.entity.srm.history.SrmHtCarport;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.srm.mapper.SrmCarportMapper;
import com.fantechs.provider.srm.mapper.SrmCarportTimeQuantumMapper;
import com.fantechs.provider.srm.mapper.SrmHtCarportMapper;
import com.fantechs.provider.srm.service.SrmCarportService;
import com.fantechs.provider.srm.service.SrmCarportTimeQuantumService;
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
 * Created by leifengzhi on 2021/11/23.
 */
@Service
public class SrmCarportServiceImpl extends BaseService<SrmCarport> implements SrmCarportService {

    @Resource
    private SrmCarportMapper srmCarportMapper;
    @Resource
    private SrmHtCarportMapper srmHtCarportMapper;
    @Resource
    private SrmCarportTimeQuantumService srmCarportTimeQuantumService;
    @Resource
    private SrmCarportTimeQuantumMapper srmCarportTimeQuantumMapper;

    @Override
    public List<SrmCarportDto> findList(Map<String, Object> map) {
        if(StringUtils.isEmpty(map.get("orgId"))){
            SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
            map.put("orgId",sysUser.getOrganizationId());
        }
        return srmCarportMapper.findList(map);
    }


    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int save(SrmCarportDto srmCarportDto) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        Example example = new Example(SrmCarport.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("warehouseId",srmCarportDto.getWarehouseId());
        List<SrmCarport> srmCarports = srmCarportMapper.selectByExample(example);
        if(StringUtils.isNotEmpty(srmCarports))
            throw new BizErrorException(ErrorCodeEnum.OPT20012001.getCode(),"仓库已存在，请勿重复添加");
        if(srmCarportDto.getCarportCount()<0 )
            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"车位数量不能小于0");

        srmCarportDto.setCreateUserId(user.getUserId());
        srmCarportDto.setCreateTime(new Date());
        srmCarportDto.setModifiedUserId(user.getUserId());
        srmCarportDto.setModifiedTime(new Date());
        srmCarportDto.setStatus(StringUtils.isEmpty(srmCarportDto.getStatus())?1: srmCarportDto.getStatus());
        srmCarportDto.setOrgId(user.getOrganizationId());
        srmCarportMapper.insertUseGeneratedKeys(srmCarportDto);

        //保存车位时间段
        List<SrmCarportTimeQuantum> list = new ArrayList<>();
        for(SrmCarportTimeQuantum srmCarportTimeQuantum : srmCarportDto.getList()){
            srmCarportTimeQuantum.setCarportId(srmCarportDto.getCarportId());
            srmCarportTimeQuantum.setCreateUserId(user.getUserId());
            srmCarportTimeQuantum.setCreateTime(new Date());
            srmCarportTimeQuantum.setModifiedUserId(user.getUserId());
            srmCarportTimeQuantum.setModifiedTime(new Date());
            srmCarportTimeQuantum.setStatus(StringUtils.isEmpty(srmCarportTimeQuantum.getStatus())?1: srmCarportTimeQuantum.getStatus());
            srmCarportTimeQuantum.setOrgId(user.getOrganizationId());
            list.add(srmCarportTimeQuantum);
        }
        if(StringUtils.isNotEmpty(list)) srmCarportTimeQuantumMapper.insertList(list);

        //保存履历表
        SrmHtCarport srmHtCarport = new SrmHtCarport();
        BeanUtils.copyProperties(srmCarportDto, srmHtCarport);
        int i = srmHtCarportMapper.insertSelective(srmHtCarport);

        return i;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int update(SrmCarportDto srmCarportDto) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        Example example = new Example(SrmCarport.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("warehouseId",srmCarportDto.getWarehouseId())
                .andNotEqualTo("carportId",srmCarportDto.getCarportId());
        List<SrmCarport> srmCarports = srmCarportMapper.selectByExample(example);
        if(StringUtils.isNotEmpty(srmCarports)) throw new BizErrorException(ErrorCodeEnum.OPT20012001.getCode(),"仓库已存在，请勿重复添加");
        if(srmCarportDto.getCarportCount()<0 )
            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"车位数量不能小于0");
        srmCarportDto.setModifiedTime(new Date());
        srmCarportDto.setModifiedUserId(user.getUserId());
        srmCarportMapper.updateByPrimaryKeySelective(srmCarportDto);

        //删除子表重新添加
        Example example1 = new Example(SrmCarportTimeQuantum.class);
        Example.Criteria criteria1 = example1.createCriteria();
        criteria1.andEqualTo("carportId",srmCarportDto.getCarportId());
        srmCarportTimeQuantumMapper.deleteByExample(example1);
        List<SrmCarportTimeQuantum> list = new ArrayList<>();
        for(SrmCarportTimeQuantum srmCarportTimeQuantum : srmCarportDto.getList()){
            srmCarportTimeQuantum.setCarportId(srmCarportDto.getCarportId());
            srmCarportTimeQuantum.setCreateUserId(user.getUserId());
            srmCarportTimeQuantum.setCreateTime(new Date());
            srmCarportTimeQuantum.setModifiedUserId(user.getUserId());
            srmCarportTimeQuantum.setModifiedTime(new Date());
            srmCarportTimeQuantum.setStatus(StringUtils.isEmpty(srmCarportTimeQuantum.getStatus())?1: srmCarportTimeQuantum.getStatus());
            srmCarportTimeQuantum.setOrgId(user.getOrganizationId());
            list.add(srmCarportTimeQuantum);
        }
        if(StringUtils.isNotEmpty(list)) srmCarportTimeQuantumMapper.insertList(list);

        SrmHtCarport srmHtCarport = new SrmHtCarport();
        BeanUtils.copyProperties(srmCarportDto, srmHtCarport);
        int i = srmHtCarportMapper.insertSelective(srmHtCarport);

        return i;
    }

    @Override
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        String[] arrIds = ids.split(",");
        for (String id : arrIds) {
            Example example1 = new Example(SrmCarportTimeQuantum.class);
            Example.Criteria criteria1 = example1.createCriteria();
            criteria1.andEqualTo("carportId",id);
            srmCarportTimeQuantumMapper.deleteByExample(example1);
        }
        int i = srmCarportMapper.deleteByIds(ids);
        return i;
    }

}
