package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.basic.history.SmtHtFactory;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseUnitPriceDto;
import com.fantechs.common.base.general.entity.basic.BaseStaff;
import com.fantechs.common.base.general.entity.basic.BaseStaffProcess;
import com.fantechs.common.base.general.entity.basic.BaseUnitPrice;
import com.fantechs.common.base.general.entity.basic.history.BaseHtUnitPrice;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.BaseHtUnitPriceMapper;
import com.fantechs.provider.base.mapper.BaseUnitPriceMapper;
import com.fantechs.provider.base.service.BaseUnitPriceService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/01/27.
 */
@Service
public class BaseUnitPriceServiceImpl extends BaseService<BaseUnitPrice> implements BaseUnitPriceService {

    @Resource
    private BaseUnitPriceMapper baseUnitPriceMapper;
    @Resource
    private BaseHtUnitPriceMapper baseHtUnitPriceMapper;

    @Override
    public List<BaseUnitPriceDto> findList(Map<String, Object> map) {
        return baseUnitPriceMapper.findList(map);
    }

    @Override
    public int save(BaseUnitPrice baseUnitPrice) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(BaseUnitPrice.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("materialId",baseUnitPrice.getMaterialId())
                .andEqualTo("processId",baseUnitPrice.getProcessId());
        BaseUnitPrice baseUnitPrice1 = baseUnitPriceMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(baseUnitPrice1)){
            throw new BizErrorException("该物料在工序下的单价信息已经存在");
        }

        baseUnitPrice.setCreateTime(new Date());
        baseUnitPrice.setCreateUserId(user.getUserId());
        baseUnitPrice.setModifiedTime(new Date());
        baseUnitPrice.setModifiedUserId(user.getUserId());
        int i = baseUnitPriceMapper.insertUseGeneratedKeys(baseUnitPrice);

        BaseHtUnitPrice baseHtUnitPrice = new BaseHtUnitPrice();
        BeanUtils.copyProperties(baseUnitPrice,baseHtUnitPrice);
        baseHtUnitPriceMapper.insertSelective(baseHtUnitPrice);

        return i;
    }

    @Override
    public int update(BaseUnitPrice baseUnitPrice) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(BaseUnitPrice.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("materialId",baseUnitPrice.getMaterialId())
                .andNotEqualTo("unitPriceId",baseUnitPrice.getUnitPriceId());
        BaseUnitPrice baseUnitPrice1 = baseUnitPriceMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(baseUnitPrice1)){
            throw new BizErrorException("该物料在工序下的单价信息已经存在");
        }

        baseUnitPrice.setModifiedTime(new Date());
        baseUnitPrice.setModifiedUserId(user.getUserId());

        BaseHtUnitPrice baseHtUnitPrice = new BaseHtUnitPrice();
        BeanUtils.copyProperties(baseUnitPrice,baseHtUnitPrice);
        baseHtUnitPriceMapper.insertSelective(baseHtUnitPrice);

        return baseUnitPriceMapper.updateByPrimaryKeySelective(baseUnitPrice);
    }

    @Override
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        String[] idsArr = ids.split(",");
        List<BaseHtUnitPrice>  baseHtUnitPrices = new LinkedList<>();
        for (String id : idsArr) {
            BaseUnitPrice baseUnitPrice = baseUnitPriceMapper.selectByPrimaryKey(id);
            if (StringUtils.isEmpty(baseUnitPrice)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

            BaseHtUnitPrice baseHtUnitPrice = new BaseHtUnitPrice();
            BeanUtils.copyProperties(baseUnitPrice,baseHtUnitPrice);
            baseHtUnitPrice.setCreateTime(new Date());
            baseHtUnitPrice.setCreateUserId(user.getUserId());
            baseHtUnitPrice.setModifiedTime(new Date());
            baseHtUnitPrice.setMaterialId(user.getUserId());
            baseHtUnitPrices.add(baseHtUnitPrice);
        }

        baseHtUnitPriceMapper.insertList(baseHtUnitPrices);

        return baseUnitPriceMapper.deleteByIds(ids);
    }
}
