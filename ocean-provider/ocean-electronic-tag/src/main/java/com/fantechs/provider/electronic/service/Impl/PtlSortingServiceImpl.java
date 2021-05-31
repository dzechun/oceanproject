package com.fantechs.provider.electronic.service.Impl;


import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.electronic.dto.PtlSortingDto;
import com.fantechs.common.base.electronic.entity.PtlSorting;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.electronic.mapper.PtlSortingMapper;
import com.fantechs.provider.electronic.service.PtlSortingService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by leifengzhi on 2020/12/08.
 */
@Service
public class PtlSortingServiceImpl extends BaseService<PtlSorting> implements PtlSortingService {

    @Resource
    private PtlSortingMapper ptlSortingMapper;

    @Override
    public int save(PtlSorting PtlSorting) {
        if(StringUtils.isEmpty(PtlSorting.getUpdateStatus())){
            SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
            if(StringUtils.isEmpty(user)){
                throw new BizErrorException(ErrorCodeEnum.UAC10011039);
            }
        }
        Example example = new Example(PtlSorting.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("materialCode", PtlSorting.getMaterialCode());
        criteria.andEqualTo("sortingCode", PtlSorting.getSortingCode());
        List<PtlSorting> ptlSortings = ptlSortingMapper.selectByExample(example);
        if (StringUtils.isNotEmpty(ptlSortings)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }
        PtlSorting.setStatus((byte) 1);
        return ptlSortingMapper.insertUseGeneratedKeys(PtlSorting);
    }

    @Override
    public int update(PtlSorting PtlSorting) {
        if(StringUtils.isEmpty(PtlSorting.getUpdateStatus())){
            SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
            if(StringUtils.isEmpty(user)){
                throw new BizErrorException(ErrorCodeEnum.UAC10011039);
            }
            PtlSorting.setModifiedUserId(user.getUserId());
            PtlSorting.setModifiedTime(new Date());
        }
        Example example = new Example(PtlSorting.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("materialCode", PtlSorting.getMaterialCode());
        criteria.andEqualTo("sortingCode", PtlSorting.getSortingCode())
                .andNotEqualTo("sortingId", PtlSorting.getSortingId());
        List<PtlSorting> ptlSortings = ptlSortingMapper.selectByExample(example);
        if (StringUtils.isNotEmpty(ptlSortings)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

       return ptlSortingMapper.updateByPrimaryKeySelective(PtlSorting);

    }

    @Override
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        String[] idsArr  = ids.split(",");
        for(String  id : idsArr){
            PtlSorting PtlSorting = ptlSortingMapper.selectByPrimaryKey(id);
            if (StringUtils.isEmpty(PtlSorting)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
        }

        return ptlSortingMapper.deleteByIds(ids);
    }

    @Override
    public List<PtlSortingDto> findList(Map<String, Object> map) {
        return ptlSortingMapper.findList(map);
    }

    @Override
    @Transactional
    @LcnTransaction
    public int batchUpdate(List<PtlSorting> ptlSortings) {
        int i = 0;
        if (StringUtils.isNotEmpty(ptlSortings)){
            i = ptlSortingMapper.batchUpdate(ptlSortings);
        }
        return i;
    }

    @Override
    @Transactional
    @LcnTransaction
    public int delBatchBySortingCode(List<String> smtSortings) {
        return ptlSortingMapper.delBatchBySortingCode(smtSortings);
    }

    @Override
    @Transactional
    @LcnTransaction
    public int updateStatus(String sortingCode, Byte status) {

        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        PtlSorting ptlSorting = new PtlSorting();
        ptlSorting.setStatus(status);
        ptlSorting.setCreateUserId(StringUtils.isNotEmpty(user) ? user.getUserId() : null);
        ptlSorting.setModifiedTime(new Date());
        ptlSorting.setModifiedUserId(StringUtils.isNotEmpty(user) ? user.getUserId() : null);
        Example example = new Example(PtlSorting.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("sortingCode", sortingCode).andEqualTo("status", 0);

        return ptlSortingMapper.updateByExampleSelective(ptlSorting, example);
    }
}
