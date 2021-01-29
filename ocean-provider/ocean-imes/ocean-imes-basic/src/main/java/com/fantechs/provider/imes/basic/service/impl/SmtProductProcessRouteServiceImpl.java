package com.fantechs.provider.imes.basic.service.impl;


import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.basic.SmtProductProcessRoute;
import com.fantechs.common.base.entity.basic.history.SmtHtProductProcessRoute;
import com.fantechs.common.base.entity.basic.search.SearchSmtProductProcessRoute;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.imes.basic.mapper.SmtHtProductProcessRouteMapper;
import com.fantechs.provider.imes.basic.mapper.SmtProductProcessRouteMapper;
import com.fantechs.provider.imes.basic.service.SmtProductProcessRouteService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * Created by wcz on 2020/09/30.
 */
@Service
public class SmtProductProcessRouteServiceImpl extends BaseService<SmtProductProcessRoute> implements SmtProductProcessRouteService {

    @Resource
    private SmtProductProcessRouteMapper smtProductProcessRouteMapper;

    @Resource
    private SmtHtProductProcessRouteMapper smtHtProductProcessRouteMapper;

    @Override
    public List<SmtProductProcessRoute> findList(SearchSmtProductProcessRoute searchSmtProductProcessRoute) {
        List<SmtProductProcessRoute> list = smtProductProcessRouteMapper.findList(searchSmtProductProcessRoute);
        for (SmtProductProcessRoute smtProductProcessRoute : list) {
            Integer productType = smtProductProcessRoute.getProductType();
            if(productType==0){
                smtProductProcessRoute.setProductName("*");
            }else if (productType==1){
                smtProductProcessRoute.setProductName(smtProductProcessRoute.getProName());
            }else if (productType==2){
                smtProductProcessRoute.setProductName(smtProductProcessRoute.getProductModelCode());
            }else if (productType==3){
                smtProductProcessRoute.setProductName(smtProductProcessRoute.getMaterialCode());
            }
        }
        return list;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(SmtProductProcessRoute smtProductProcessRoute) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Integer productType = smtProductProcessRoute.getProductType();
        Long proLineId = smtProductProcessRoute.getProLineId();
        Long productModelId = smtProductProcessRoute.getProductModelId();
        Long materialId = smtProductProcessRoute.getMaterialId();
        if(productType==0){
            Example example = new Example(SmtProductProcessRoute.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("productType",productType);
            List<SmtProductProcessRoute> smtProductProcessRoutes = smtProductProcessRouteMapper.selectByExample(example);
            if(StringUtils.isNotEmpty(smtProductProcessRoutes)){
                throw new BizErrorException("产品类别为All(*)的工艺路线只能有一条");
            }
        }else {
            Example example = new Example(SmtProductProcessRoute.class);
            Example.Criteria criteria = example.createCriteria();
            Example.Criteria criteria1 = example.createCriteria();
            criteria1.andEqualTo("proLineId",proLineId).orEqualTo("productModelId",productModelId).orEqualTo("materialId",materialId);
            example.and(criteria1);
            criteria.andEqualTo("productType",productType);
            criteria.andEqualTo("routeId",smtProductProcessRoute.getRouteId());
            List<SmtProductProcessRoute> smtProductProcessRoutes = smtProductProcessRouteMapper.selectByExample(example);
            if(StringUtils.isNotEmpty(smtProductProcessRoutes)){
                throw new BizErrorException("该产品名称的工艺路线已存在");
            }
        }
        smtProductProcessRoute.setCreateUserId(currentUser.getUserId());
        smtProductProcessRoute.setCreateTime(new Date());
        smtProductProcessRoute.setModifiedUserId(currentUser.getUserId());
        smtProductProcessRoute.setModifiedTime(new Date());
        int i = smtProductProcessRouteMapper.insertUseGeneratedKeys(smtProductProcessRoute);

        //新增产品工艺路线历史信息
        SmtHtProductProcessRoute smtHtProductProcessRoute=new SmtHtProductProcessRoute();
        BeanUtils.copyProperties(smtProductProcessRoute,smtHtProductProcessRoute);
        smtHtProductProcessRouteMapper.insertSelective(smtHtProductProcessRoute);
        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(SmtProductProcessRoute smtProductProcessRoute) {
        SysUser currentUser =CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Integer productType = smtProductProcessRoute.getProductType();
        Long proLineId = smtProductProcessRoute.getProLineId();
        Long productModelId = smtProductProcessRoute.getProductModelId();
        Long materialId = smtProductProcessRoute.getMaterialId();
        if(productType==0){
            Example example = new Example(SmtProductProcessRoute.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("productType",productType);
            SmtProductProcessRoute productProcessRoute = smtProductProcessRouteMapper.selectOneByExample(example);
            if(StringUtils.isNotEmpty(productProcessRoute)&&!productProcessRoute.getProductProcessRouteId().equals(smtProductProcessRoute.getProductProcessRouteId())){
                throw new BizErrorException("产品类别为All(*)的工艺路线只能有一条");
            }
        }else {
            Example example = new Example(SmtProductProcessRoute.class);
            Example.Criteria criteria = example.createCriteria();
            Example.Criteria criteria1 = example.createCriteria();
            criteria1.andEqualTo("proLineId",proLineId).orEqualTo("productModelId",productModelId).orEqualTo("materialId",materialId);
            example.and(criteria1);
            criteria.andEqualTo("productType",productType);
            criteria.andEqualTo("routeId",smtProductProcessRoute.getRouteId());
            SmtProductProcessRoute productProcessRoute = smtProductProcessRouteMapper.selectOneByExample(example);
            if(StringUtils.isNotEmpty(productProcessRoute)&&!productProcessRoute.getProductProcessRouteId().equals(smtProductProcessRoute.getProductProcessRouteId())){
                throw new BizErrorException("该产品名称的工艺路线已存在");
            }
        }

        smtProductProcessRoute.setModifiedUserId(currentUser.getUserId());
        smtProductProcessRoute.setModifiedTime(new Date());
        int i= smtProductProcessRouteMapper.updateByPrimaryKeySelective(smtProductProcessRoute);

        //新增产品工艺路线历史信息
        SmtHtProductProcessRoute smtHtProductProcessRoute=new SmtHtProductProcessRoute();
        BeanUtils.copyProperties(smtProductProcessRoute,smtHtProductProcessRoute);
        smtHtProductProcessRouteMapper.insertSelective(smtHtProductProcessRoute);
        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(String ids) {
        int i=0;
        List<SmtHtProductProcessRoute> list=new ArrayList<>();
        SysUser currentUser =CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        String[] idsArr =  ids.split(",");
        for (String productProcessRouteId : idsArr) {
            SmtProductProcessRoute smtProductProcessRoute = smtProductProcessRouteMapper.selectByPrimaryKey(productProcessRouteId);
            if(StringUtils.isEmpty(smtProductProcessRoute)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012000,productProcessRouteId);
            }

            //新增产品工艺路线历史信息
            SmtHtProductProcessRoute smtHtProductProcessRoute=new SmtHtProductProcessRoute();
            BeanUtils.copyProperties(smtProductProcessRoute,smtHtProductProcessRoute);
            smtHtProductProcessRoute.setModifiedUserId(currentUser.getUserId());
            smtHtProductProcessRoute.setModifiedTime(new Date());
            list.add(smtHtProductProcessRoute);

        }
        smtHtProductProcessRouteMapper.insertList(list);
        i=  smtProductProcessRouteMapper.deleteByIds(ids);
        return i;
    }

}
