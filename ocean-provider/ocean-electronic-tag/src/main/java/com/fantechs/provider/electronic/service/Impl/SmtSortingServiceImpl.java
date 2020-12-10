package com.fantechs.provider.electronic.service.Impl;


import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.electronic.dto.SmtSortingDto;
import com.fantechs.common.base.electronic.entity.SmtSorting;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.electronic.mapper.SmtSortingMapper;
import com.fantechs.provider.electronic.service.SmtSortingService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by leifengzhi on 2020/12/08.
 */
@Service
public class SmtSortingServiceImpl extends BaseService<SmtSorting> implements SmtSortingService {

    @Resource
    private SmtSortingMapper smtSortingMapper;

    @Override
    public int save(SmtSorting SmtSorting) {
        if(StringUtils.isEmpty(SmtSorting.getUpdateStatus())){
            SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
            if(StringUtils.isEmpty(user)){
                throw new BizErrorException(ErrorCodeEnum.UAC10011039);
            }
        }
        Example example = new Example(SmtSorting.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("materialCode",SmtSorting.getMaterialCode());
        criteria.andEqualTo("sortingCode",SmtSorting.getSortingCode());
        List<SmtSorting> SmtSortings = smtSortingMapper.selectByExample(example);
        if (StringUtils.isNotEmpty(SmtSortings)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }
        SmtSorting.setStatus((byte) 1);
        return smtSortingMapper.insertUseGeneratedKeys(SmtSorting);
    }

    @Override
    public int update(SmtSorting SmtSorting) {
        if(StringUtils.isEmpty(SmtSorting.getUpdateStatus())){
            SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
            if(StringUtils.isEmpty(user)){
                throw new BizErrorException(ErrorCodeEnum.UAC10011039);
            }
        }
        Example example = new Example(SmtSorting.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("materialCode",SmtSorting.getMaterialCode());
        criteria.andEqualTo("sortingCode",SmtSorting.getSortingCode())
                .andNotEqualTo("sortingId",SmtSorting.getSortingId());
        List<SmtSorting> SmtSortings = smtSortingMapper.selectByExample(example);
        if (StringUtils.isNotEmpty(SmtSortings)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

       return smtSortingMapper.updateByPrimaryKeySelective(SmtSorting);

    }

    @Override
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        String[] idsArr  = ids.split(",");
        for(String  id : idsArr){
            SmtSorting SmtSorting = smtSortingMapper.selectByPrimaryKey(id);
            if (StringUtils.isEmpty(SmtSorting)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
        }

        return smtSortingMapper.deleteByIds(ids);
    }

    @Override
    public List<SmtSortingDto> findList(Map<String, Object> map) {
        return smtSortingMapper.findList(map);
    }

    @Override
    public int batchUpdate(List<SmtSorting> smtSortings) {
        int i = 0;
        if (StringUtils.isNotEmpty()){
            i = smtSortingMapper.batchUpdate(smtSortings);
        }
        return i;
    }

    @Override
    public int delBatchBySortingCode(List<String> smtSortings) {
        return smtSortingMapper.delBatchBySortingCode(smtSortings);
    }


}
