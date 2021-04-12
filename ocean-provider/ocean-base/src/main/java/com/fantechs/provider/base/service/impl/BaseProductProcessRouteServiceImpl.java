package com.fantechs.provider.base.service.impl;


import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.general.entity.basic.*;
import com.fantechs.common.base.general.entity.basic.history.BaseHtProductProcessRoute;
import com.fantechs.common.base.general.dto.basic.imports.BaseProductProcessRouteImport;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseProductProcessRoute;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.base.mapper.*;
import com.fantechs.provider.base.service.BaseProductProcessRouteService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;

import static java.util.Arrays.asList;

/**
 * Created by wcz on 2020/09/30.
 */
@Service
public class BaseProductProcessRouteServiceImpl extends BaseService<BaseProductProcessRoute> implements BaseProductProcessRouteService {

    @Resource
    private BaseProductProcessRouteMapper baseProductProcessRouteMapper;
    @Resource
    private BaseHtProductProcessRouteMapper baseHtProductProcessRouteMapper;
    @Resource
    private BaseRouteMapper baseRouteMapper;
    @Resource
    private BaseProductModelMapper baseProductModelMapper;
    @Resource
    private BaseProLineMapper baseProLineMapper;
    @Resource
    private BaseMaterialMapper baseMaterialMapper;

    @Override
    public List<BaseProductProcessRoute> findList(SearchBaseProductProcessRoute searchBaseProductProcessRoute) {
        List<BaseProductProcessRoute> list = baseProductProcessRouteMapper.findList(searchBaseProductProcessRoute);
        for (BaseProductProcessRoute baseProductProcessRoute : list) {
            Integer productType = baseProductProcessRoute.getProductType();
            if (productType == 0) {
                baseProductProcessRoute.setProductName("*");
            } else if (productType == 1) {
                baseProductProcessRoute.setProductName(baseProductProcessRoute.getProName());
            } else if (productType == 2) {
                baseProductProcessRoute.setProductName(baseProductProcessRoute.getProductModelCode());
            } else if (productType == 3) {
                baseProductProcessRoute.setProductName(baseProductProcessRoute.getMaterialCode());
            }
        }
        return list;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(BaseProductProcessRoute baseProductProcessRoute) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(currentUser)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Integer productType = baseProductProcessRoute.getProductType();
        Long proLineId = baseProductProcessRoute.getProLineId();
        Long productModelId = baseProductProcessRoute.getProductModelId();
        Long materialId = baseProductProcessRoute.getMaterialId();
        if (productType == 0) {
            Example example = new Example(BaseProductProcessRoute.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("productType", productType);
            List<BaseProductProcessRoute> baseProductProcessRoutes = baseProductProcessRouteMapper.selectByExample(example);
            if (StringUtils.isNotEmpty(baseProductProcessRoutes)) {
                throw new BizErrorException("产品类别为All(*)的工艺路线只能有一条");
            }
        } else {
            Example example = new Example(BaseProductProcessRoute.class);
            Example.Criteria criteria = example.createCriteria();
            Example.Criteria criteria1 = example.createCriteria();
            criteria1.andEqualTo("proLineId", proLineId).orEqualTo("productModelId", productModelId).orEqualTo("materialId", materialId);
            example.and(criteria1);
            criteria.andEqualTo("productType", productType);
            criteria.andEqualTo("routeId", baseProductProcessRoute.getRouteId());
            List<BaseProductProcessRoute> baseProductProcessRoutes = baseProductProcessRouteMapper.selectByExample(example);
            if (StringUtils.isNotEmpty(baseProductProcessRoutes)) {
                throw new BizErrorException("该产品名称的工艺路线已存在");
            }
        }
        baseProductProcessRoute.setCreateUserId(currentUser.getUserId());
        baseProductProcessRoute.setCreateTime(new Date());
        baseProductProcessRoute.setModifiedUserId(currentUser.getUserId());
        baseProductProcessRoute.setModifiedTime(new Date());
        int i = baseProductProcessRouteMapper.insertUseGeneratedKeys(baseProductProcessRoute);

        //新增产品工艺路线历史信息
        BaseHtProductProcessRoute baseHtProductProcessRoute = new BaseHtProductProcessRoute();
        BeanUtils.copyProperties(baseProductProcessRoute, baseHtProductProcessRoute);
        baseHtProductProcessRouteMapper.insertSelective(baseHtProductProcessRoute);
        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(BaseProductProcessRoute baseProductProcessRoute) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(currentUser)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Integer productType = baseProductProcessRoute.getProductType();
        Long proLineId = baseProductProcessRoute.getProLineId();
        Long productModelId = baseProductProcessRoute.getProductModelId();
        Long materialId = baseProductProcessRoute.getMaterialId();
        if (productType == 0) {
            Example example = new Example(BaseProductProcessRoute.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("productType", productType);
            BaseProductProcessRoute productProcessRoute = baseProductProcessRouteMapper.selectOneByExample(example);
            if (StringUtils.isNotEmpty(productProcessRoute) && !productProcessRoute.getProductProcessRouteId().equals(baseProductProcessRoute.getProductProcessRouteId())) {
                throw new BizErrorException("产品类别为All(*)的工艺路线只能有一条");
            }
        } else {
            Example example = new Example(BaseProductProcessRoute.class);
            Example.Criteria criteria = example.createCriteria();
            Example.Criteria criteria1 = example.createCriteria();
            criteria1.andEqualTo("proLineId", proLineId).orEqualTo("productModelId", productModelId).orEqualTo("materialId", materialId);
            example.and(criteria1);
            criteria.andEqualTo("productType", productType);
            criteria.andEqualTo("routeId", baseProductProcessRoute.getRouteId());
            BaseProductProcessRoute productProcessRoute = baseProductProcessRouteMapper.selectOneByExample(example);
            if (StringUtils.isNotEmpty(productProcessRoute) && !productProcessRoute.getProductProcessRouteId().equals(baseProductProcessRoute.getProductProcessRouteId())) {
                throw new BizErrorException("该产品名称的工艺路线已存在");
            }
        }

        baseProductProcessRoute.setModifiedUserId(currentUser.getUserId());
        baseProductProcessRoute.setModifiedTime(new Date());
        int i = baseProductProcessRouteMapper.updateByPrimaryKeySelective(baseProductProcessRoute);

        //新增产品工艺路线历史信息
        BaseHtProductProcessRoute baseHtProductProcessRoute = new BaseHtProductProcessRoute();
        BeanUtils.copyProperties(baseProductProcessRoute, baseHtProductProcessRoute);
        baseHtProductProcessRouteMapper.insertSelective(baseHtProductProcessRoute);
        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(String ids) {
        int i = 0;
        List<BaseHtProductProcessRoute> list = new ArrayList<>();
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(currentUser)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        String[] idsArr = ids.split(",");
        for (String productProcessRouteId : idsArr) {
            BaseProductProcessRoute baseProductProcessRoute = baseProductProcessRouteMapper.selectByPrimaryKey(productProcessRouteId);
            if (StringUtils.isEmpty(baseProductProcessRoute)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012000, productProcessRouteId);
            }

            //新增产品工艺路线历史信息
            BaseHtProductProcessRoute baseHtProductProcessRoute = new BaseHtProductProcessRoute();
            BeanUtils.copyProperties(baseProductProcessRoute, baseHtProductProcessRoute);
            baseHtProductProcessRoute.setModifiedUserId(currentUser.getUserId());
            baseHtProductProcessRoute.setModifiedTime(new Date());
            list.add(baseHtProductProcessRoute);

        }
        baseHtProductProcessRouteMapper.insertList(list);
        i = baseProductProcessRouteMapper.deleteByIds(ids);
        return i;
    }


    @Override
    public Map<String, Object> importExcel(List<BaseProductProcessRouteImport> baseProductProcessRouteImports) {

        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(currentUser)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Map<String, Object> resultMap = new HashMap<>();  //封装操作结果
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数

        //排除不合法的数据
        Iterator<BaseProductProcessRouteImport> iterator = baseProductProcessRouteImports.iterator();
        int i = 0;
        while (iterator.hasNext()) {
            BaseProductProcessRouteImport baseProductProcessRouteImport = iterator.next();
            Integer productType = baseProductProcessRouteImport.getProductType();
            String routeCode = baseProductProcessRouteImport.getRouteCode();
            String productModelCode = baseProductProcessRouteImport.getProductModelCode();
            String proCode = baseProductProcessRouteImport.getProCode();
            String materialCode = baseProductProcessRouteImport.getMaterialCode();

            List<Integer> arrayList = asList(0,1,2,3);
            //判断必传字段
            if (StringUtils.isEmpty(
                    productType,routeCode
            ) || !arrayList.contains(productType)) {
                fail.add(i + 4);
                iterator.remove();
                i++;
                continue;
            }

            //判断编码对应的信息是否存在
            Example example = new Example(BaseRoute.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("routeCode",routeCode);
            BaseRoute baseRoute = baseRouteMapper.selectOneByExample(example);
            if (StringUtils.isEmpty(baseRoute)){
                fail.add(i + 4);
                iterator.remove();
                i++;
                continue;
            }
            baseProductProcessRouteImport.setRouteId(baseRoute.getRouteId());

            if (StringUtils.isNotEmpty(productModelCode)){
                Example example1 = new Example(BaseProductModel.class);
                Example.Criteria criteria1 = example1.createCriteria();
                criteria1.andEqualTo("productModelCode",productModelCode);
                BaseProductModel baseProductModel = baseProductModelMapper.selectOneByExample(example1);
                if (StringUtils.isEmpty(baseProductModel)){
                    fail.add(i + 4);
                    iterator.remove();
                    i++;
                    continue;
                }
                baseProductProcessRouteImport.setProductModelId(baseProductModel.getProductModelId());
            }


            if (StringUtils.isNotEmpty(proCode)){
                Example example2 = new Example(BaseProLine.class);
                Example.Criteria criteria2 = example2.createCriteria();
                criteria2.andEqualTo("proCode",proCode);
                BaseProLine baseProLine = baseProLineMapper.selectOneByExample(example2);
                if (StringUtils.isEmpty(baseProLine)){
                    fail.add(i + 4);
                    iterator.remove();
                    i++;
                    continue;
                }
                baseProductProcessRouteImport.setProLineId(baseProLine.getProLineId());
            }

            if (StringUtils.isNotEmpty(materialCode)){
                Example example3 = new Example(BaseMaterial.class);
                Example.Criteria criteria3 = example3.createCriteria();
                criteria3.andEqualTo("materialCode",materialCode);
                BaseMaterial baseMaterial = baseMaterialMapper.selectOneByExample(example3);
                if (StringUtils.isEmpty(baseMaterial)){
                    fail.add(i + 4);
                    iterator.remove();
                    i++;
                    continue;
                }
                baseProductProcessRouteImport.setMaterialId(baseMaterial.getMaterialId());
            }

            i++;
        }

        for (BaseProductProcessRouteImport baseProductProcessRouteImport : baseProductProcessRouteImports) {
            BaseProductProcessRoute baseProductProcessRoute = new BaseProductProcessRoute();
            BeanUtils.copyProperties(baseProductProcessRouteImport, baseProductProcessRoute);
            baseProductProcessRoute.setCreateTime(new Date());
            baseProductProcessRoute.setCreateUserId(currentUser.getUserId());
            baseProductProcessRoute.setModifiedTime(new Date());
            baseProductProcessRoute.setModifiedUserId(currentUser.getUserId());
            success += baseProductProcessRouteMapper.insertUseGeneratedKeys(baseProductProcessRoute);

            //新增履历
            BaseHtProductProcessRoute baseHtProductProcessRoute = new BaseHtProductProcessRoute();
            BeanUtils.copyProperties(baseProductProcessRoute, baseHtProductProcessRoute);
            baseHtProductProcessRouteMapper.insertSelective(baseHtProductProcessRoute);
        }
        resultMap.put("操作成功总数", success);
        resultMap.put("操作失败行", fail);
        return resultMap;
    }
}
