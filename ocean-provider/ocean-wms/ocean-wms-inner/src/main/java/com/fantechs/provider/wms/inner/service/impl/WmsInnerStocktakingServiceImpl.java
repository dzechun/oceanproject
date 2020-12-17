package com.fantechs.provider.wms.inner.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerStocktakingDto;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerStocktaking;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.wms.inner.mapper.WmsInnerStocktakingMapper;
import com.fantechs.provider.wms.inner.service.WmsInnerStocktakingService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by leifengzhi on 2020/12/16.
 */
@Service
public class WmsInnerStocktakingServiceImpl extends BaseService<WmsInnerStocktaking> implements WmsInnerStocktakingService {

    @Resource
    private WmsInnerStocktakingMapper wmsInnerStocktakingMapper;

    @Override
    public int save(WmsInnerStocktaking wmsInnerStocktaking) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(WmsInnerStocktaking.class);
        Example.Criteria criteria1 = example.createCriteria();
        //判断编码是否重复
        criteria1.andEqualTo("stocktakingCode",wmsInnerStocktaking.getStocktakingCode());
        WmsInnerStocktaking wmsInnerStocktaking1 = wmsInnerStocktakingMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(wmsInnerStocktaking1)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        wmsInnerStocktaking.setCreateTime(new Date());
        wmsInnerStocktaking.setCreateUserId(user.getUserId());
        wmsInnerStocktaking.setModifiedTime(new Date());
        wmsInnerStocktaking.setModifiedUserId(user.getUserId());
        wmsInnerStocktaking.setStatus(StringUtils.isEmpty(wmsInnerStocktaking.getStatus())?1:wmsInnerStocktaking.getStatus());
        return wmsInnerStocktakingMapper.insertUseGeneratedKeys(wmsInnerStocktaking);

    }

    @Override
    public int update(WmsInnerStocktaking wmsInnerStocktaking) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(WmsInnerStocktaking.class);
        Example.Criteria criteria1 = example.createCriteria();
        //判断编码是否重复
        criteria1.andEqualTo("stocktakingCode",wmsInnerStocktaking.getStocktakingCode())
                .andNotEqualTo("stocktakingId",wmsInnerStocktaking.getStocktakingId());
        WmsInnerStocktaking wmsInnerStocktaking1 = wmsInnerStocktakingMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(wmsInnerStocktaking1)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        wmsInnerStocktaking.setModifiedUserId(user.getUserId());
        wmsInnerStocktaking.setModifiedTime(new Date());

        return wmsInnerStocktakingMapper.updateByPrimaryKeySelective(wmsInnerStocktaking);
    }

    @Override
    public int batchDelete(String ids) {

        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        String[] idsArr  = ids.split(",");
        for (String id : idsArr) {
            WmsInnerStocktaking wmsInnerStocktaking = wmsInnerStocktakingMapper.selectByPrimaryKey(id);
            if (StringUtils.isEmpty(wmsInnerStocktaking)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

        }

        return wmsInnerStocktakingMapper.deleteByIds(ids);
    }

    @Override
    public List<WmsInnerStocktakingDto> findList(Map<String, Object> map) {
        return wmsInnerStocktakingMapper.findList(map);
    }
}
