package com.fantechs.provider.electronic.service.Impl;


import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.electronic.entity.SmtSortingList;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.electronic.mapper.SmtSortingListMapper;
import com.fantechs.provider.electronic.service.SmtSortingListService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by leifengzhi on 2020/12/08.
 */
@Service
public class SmtSortingListServiceImpl extends BaseService<SmtSortingList> implements SmtSortingListService {

    @Resource
    private SmtSortingListMapper smtSortingListMapper;

    @Override
    public int save(SmtSortingList smtSortingList) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(SmtSortingList.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("sortingLisCode",smtSortingList.getSortingLisCode());
        List<SmtSortingList> smtSortingLists = smtSortingListMapper.selectByExample(example);
        if (StringUtils.isNotEmpty(smtSortingLists)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }
        smtSortingList.setStatus((byte) 1);
        return smtSortingListMapper.insertUseGeneratedKeys(smtSortingList);
    }

    @Override
    public int update(SmtSortingList smtSortingList) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(SmtSortingList.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("sortingLisCode",smtSortingList.getSortingLisCode())
                .andNotEqualTo("sortingListId",smtSortingList.getSortingListId());
        List<SmtSortingList> smtSortingLists = smtSortingListMapper.selectByExample(example);
        if (StringUtils.isNotEmpty(smtSortingLists)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

       return smtSortingListMapper.updateByPrimaryKeySelective(smtSortingList);

    }

    @Override
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        String[] idsArr  = ids.split(",");
        for(String  id : idsArr){
            SmtSortingList smtSortingList = smtSortingListMapper.selectByPrimaryKey(id);
            if (StringUtils.isEmpty(smtSortingList)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
        }

        return smtSortingListMapper.deleteByIds(ids);
    }

    @Override
    public List<SmtSortingList> findList(Map<String, Object> map) {
        return smtSortingListMapper.findList(map);
    }
}
