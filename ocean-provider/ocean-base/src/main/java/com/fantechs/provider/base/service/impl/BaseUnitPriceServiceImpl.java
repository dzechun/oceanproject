package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseUnitPriceDetDto;
import com.fantechs.common.base.general.dto.basic.BaseUnitPriceDto;
import com.fantechs.common.base.general.entity.basic.BaseUnitPrice;
import com.fantechs.common.base.general.entity.basic.BaseUnitPriceDet;
import com.fantechs.common.base.general.entity.basic.history.BaseHtUnitPrice;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseUnitPriceDet;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.BaseHtUnitPriceMapper;
import com.fantechs.provider.base.mapper.BaseUnitPriceDetMapper;
import com.fantechs.provider.base.mapper.BaseUnitPriceMapper;
import com.fantechs.provider.base.service.BaseUnitPriceService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    @Resource
    private BaseUnitPriceDetMapper baseUnitPriceDetMapper;

    @Override
    public List<BaseUnitPriceDto> findList(Map<String, Object> map) {
        List<BaseUnitPriceDto> list = baseUnitPriceMapper.findList(map);
        if (StringUtils.isNotEmpty(list)){
            SearchBaseUnitPriceDet searchBaseUnitPriceDet = new SearchBaseUnitPriceDet();
            for (BaseUnitPriceDto baseUnitPriceDto : list) {
                searchBaseUnitPriceDet.setUnitPriceId(baseUnitPriceDto.getUnitPriceId());
                List<BaseUnitPriceDetDto> baseUnitPriceDetDtos = baseUnitPriceDetMapper.findList(ControllerUtil.dynamicConditionByEntity(searchBaseUnitPriceDet));
                if (StringUtils.isNotEmpty(baseUnitPriceDetDtos)){
                    baseUnitPriceDto.setBaseUnitPriceDetDtoList(baseUnitPriceDetDtos);
                }
            }
        }
        return list;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(BaseUnitPrice baseUnitPrice) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(BaseUnitPrice.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("materialId",baseUnitPrice.getMaterialId());
        BaseUnitPrice baseUnitPrice1 = baseUnitPriceMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(baseUnitPrice1)){
            throw new BizErrorException("该物料的单价信息已经存在");
        }

        //新增单价信息
        baseUnitPrice.setCreateTime(new Date());
        baseUnitPrice.setCreateUserId(user.getUserId());
        baseUnitPrice.setModifiedTime(new Date());
        baseUnitPrice.setModifiedUserId(user.getUserId());
        baseUnitPrice.setOrganizationId(user.getOrganizationId());
        baseUnitPrice.setOrganizationId(user.getOrganizationId());
        int i = baseUnitPriceMapper.insertUseGeneratedKeys(baseUnitPrice);

        //新增履历
        BaseHtUnitPrice baseHtUnitPrice = new BaseHtUnitPrice();
        BeanUtils.copyProperties(baseUnitPrice,baseHtUnitPrice);
        baseHtUnitPriceMapper.insertSelective(baseHtUnitPrice);

        //删除原绑定关系
        Example example1 = new Example(BaseUnitPriceDet.class);
        Example.Criteria criteria1 = example1.createCriteria();
        criteria1.andEqualTo("unitPriceId",baseUnitPrice.getUnitPriceId());
        baseUnitPriceDetMapper.deleteByExample(example1);

        //新增明细
        List<BaseUnitPriceDetDto> baseUnitPriceDetDtoList = baseUnitPrice.getBaseUnitPriceDetDtoList();
        if (StringUtils.isNotEmpty(baseUnitPriceDetDtoList)){
            for (BaseUnitPriceDet baseUnitPriceDet : baseUnitPriceDetDtoList) {
                baseUnitPriceDet.setUnitPriceId(baseUnitPrice.getUnitPriceId());
                baseUnitPriceDet.setCreateTime(new Date());
                baseUnitPriceDet.setCreateUserId(user.getUserId());
                baseUnitPriceDet.setModifiedTime(new Date());
                baseUnitPriceDet.setModifiedUserId(user.getUserId());
                baseUnitPriceDet.setOrganizationId(user.getOrganizationId());
            }
        }
        if (StringUtils.isNotEmpty(baseUnitPriceDetDtoList)){
            baseUnitPriceDetMapper.insertList(baseUnitPriceDetDtoList);
        }
        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
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
            throw new BizErrorException("该物料的单价信息已经存在");
        }

        baseUnitPrice.setModifiedTime(new Date());
        baseUnitPrice.setModifiedUserId(user.getUserId());
        baseUnitPrice.setOrganizationId(user.getOrganizationId());

        BaseHtUnitPrice baseHtUnitPrice = new BaseHtUnitPrice();
        BeanUtils.copyProperties(baseUnitPrice,baseHtUnitPrice);
        baseHtUnitPriceMapper.insertSelective(baseHtUnitPrice);

        //删除原绑定关系
        Example example1 = new Example(BaseUnitPriceDet.class);
        Example.Criteria criteria1 = example1.createCriteria();
        criteria1.andEqualTo("unitPriceId",baseUnitPrice.getUnitPriceId());
        baseUnitPriceDetMapper.deleteByExample(example1);

        //新增明细
        List<BaseUnitPriceDetDto> baseUnitPriceDetDtoList = baseUnitPrice.getBaseUnitPriceDetDtoList();
        if (StringUtils.isNotEmpty(baseUnitPriceDetDtoList)){
            for (BaseUnitPriceDet baseUnitPriceDet : baseUnitPriceDetDtoList) {
                baseUnitPriceDet.setUnitPriceId(baseUnitPrice.getUnitPriceId());
                baseUnitPriceDet.setCreateTime(new Date());
                baseUnitPriceDet.setCreateUserId(user.getUserId());
                baseUnitPriceDet.setModifiedTime(new Date());
                baseUnitPriceDet.setModifiedUserId(user.getUserId());
                baseUnitPriceDet.setOrganizationId(user.getOrganizationId());
            }
        }
        if (StringUtils.isNotEmpty(baseUnitPriceDetDtoList)){
            baseUnitPriceDetMapper.insertList(baseUnitPriceDetDtoList);
        }

        return baseUnitPriceMapper.updateByPrimaryKeySelective(baseUnitPrice);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
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

            //删除原绑定关系
            Example example = new Example(BaseUnitPriceDet.class);
            Example.Criteria criteria1 = example.createCriteria();
            criteria1.andEqualTo("unitPriceId",id);
            baseUnitPriceDetMapper.deleteByExample(example);
        }

        baseHtUnitPriceMapper.insertList(baseHtUnitPrices);

        return baseUnitPriceMapper.deleteByIds(ids);
    }
}
