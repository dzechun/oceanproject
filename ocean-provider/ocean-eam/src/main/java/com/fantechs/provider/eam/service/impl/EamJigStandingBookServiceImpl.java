package com.fantechs.provider.eam.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.eam.EamJigStandingBookDto;
import com.fantechs.common.base.general.entity.eam.EamJigStandingBook;
import com.fantechs.common.base.general.entity.eam.history.EamHtJigStandingBook;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.eam.mapper.EamHtJigStandingBookMapper;
import com.fantechs.provider.eam.mapper.EamJigStandingBookMapper;
import com.fantechs.provider.eam.service.EamJigStandingBookService;
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
 * Created by leifengzhi on 2021/07/28.
 */
@Service
public class EamJigStandingBookServiceImpl extends BaseService<EamJigStandingBook> implements EamJigStandingBookService {

    @Resource
    private EamJigStandingBookMapper eamJigStandingBookMapper;

    @Resource
    private EamHtJigStandingBookMapper eamHtJigStandingBookMapper;

    @Override
    public List<EamJigStandingBookDto> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        map.put("orgId",user.getOrganizationId());
        return eamJigStandingBookMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int save(EamJigStandingBook record) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        this.codeIfRepeat(record);

        record.setCreateTime(new Date());
        record.setCreateUserId(user.getUserId());
        record.setModifiedTime(new Date());
        record.setModifiedUserId(user.getUserId());
        record.setOrgId(user.getOrganizationId());

        int i = eamJigStandingBookMapper.insertUseGeneratedKeys(record);

        EamHtJigStandingBook eamHtJigStandingBook = new EamHtJigStandingBook();
        BeanUtils.copyProperties(record,eamHtJigStandingBook);
        eamHtJigStandingBookMapper.insert(eamHtJigStandingBook);

        return i;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int update(EamJigStandingBook entity) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        this.codeIfRepeat(entity);

        entity.setModifiedUserId(user.getUserId());
        entity.setModifiedTime(new Date());
        entity.setOrgId(user.getOrganizationId());
        EamHtJigStandingBook eamHtJigStandingBook = new EamHtJigStandingBook();
        BeanUtils.copyProperties(entity,eamHtJigStandingBook);
        eamHtJigStandingBookMapper.insert(eamHtJigStandingBook);

        return eamJigStandingBookMapper.updateByPrimaryKeySelective(entity);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        List<EamHtJigStandingBook> list = new ArrayList<>();
        String[] idsArr  = ids.split(",");
        for (String id : idsArr) {
            EamJigStandingBook eamJigStandingBook = eamJigStandingBookMapper.selectByPrimaryKey(id);
            if (StringUtils.isEmpty(eamJigStandingBook)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

            EamHtJigStandingBook eamHtJigStandingBook = new EamHtJigStandingBook();
            BeanUtils.copyProperties(eamJigStandingBook,eamHtJigStandingBook);
            list.add(eamHtJigStandingBook);
        }

        eamHtJigStandingBookMapper.insertList(list);
        return eamJigStandingBookMapper.deleteByIds(ids);
    }

    private void codeIfRepeat(EamJigStandingBook entity){
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        Example example = new Example(EamJigStandingBook.class);
        Example.Criteria criteria = example.createCriteria();
        //判断编码是否重复
        criteria.andEqualTo("orgId", user.getOrganizationId());
        criteria.andEqualTo("jigBarcodeId",entity.getJigBarcodeId());
        if (StringUtils.isNotEmpty(entity.getJigStandingBookId())){
            criteria.andNotEqualTo("jigStandingBookId",entity.getJigStandingBookId());
        }
        EamJigStandingBook emJigStandingBook = eamJigStandingBookMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(emJigStandingBook)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }
    }


}
