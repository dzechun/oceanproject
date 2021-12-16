package com.fantechs.provider.base.service.impl;


import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.imports.BaseProductProcessRouteImport;
import com.fantechs.common.base.general.entity.basic.*;
import com.fantechs.common.base.general.entity.basic.history.BaseHtProductProcessRoute;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
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
    public List<BaseProductProcessRoute> findList(Map<String, Object> map) {
        if(StringUtils.isEmpty(map.get("orgId"))) {
            SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
            map.put("orgId", user.getOrganizationId());
        }
        List<BaseProductProcessRoute> list = baseProductProcessRouteMapper.findList(map);
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

        Integer productType = baseProductProcessRoute.getProductType();
        Long proLineId = baseProductProcessRoute.getProLineId();
        Long productModelId = baseProductProcessRoute.getProductModelId();
        Long materialId = baseProductProcessRoute.getMaterialId();
        if (productType == 0) {
            Example example = new Example(BaseProductProcessRoute.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("productType", productType)
                    .andEqualTo("organizationId",currentUser.getOrganizationId());
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

            if(StringUtils.isNotEmpty(materialId)){
                example.clear();
                Example.Criteria criteria2 = example.createCriteria();
                criteria2.andEqualTo("materialId",materialId);
                if(StringUtils.isNotEmpty(baseProductProcessRouteMapper.selectByExample(example))){
                    throw new BizErrorException("同个产品料号不能绑定多个产品工艺路线");
                }
            }
        }
        baseProductProcessRoute.setCreateUserId(currentUser.getUserId());
        baseProductProcessRoute.setCreateTime(new Date());
        baseProductProcessRoute.setModifiedUserId(currentUser.getUserId());
        baseProductProcessRoute.setModifiedTime(new Date());
        baseProductProcessRoute.setOrganizationId(currentUser.getOrganizationId());
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

        Integer productType = baseProductProcessRoute.getProductType();
        Long proLineId = baseProductProcessRoute.getProLineId();
        Long productModelId = baseProductProcessRoute.getProductModelId();
        Long materialId = baseProductProcessRoute.getMaterialId();
        if (productType == 0) {
            Example example = new Example(BaseProductProcessRoute.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("productType", productType)
                    .andEqualTo("organizationId",currentUser.getOrganizationId());
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

            if(StringUtils.isNotEmpty(materialId)){
                example.clear();
                Example.Criteria criteria2 = example.createCriteria();
                criteria2.andEqualTo("materialId",materialId)
                        .andNotEqualTo("productProcessRouteId",baseProductProcessRoute.getProductProcessRouteId());
                if(StringUtils.isNotEmpty(baseProductProcessRouteMapper.selectByExample(example))){
                    throw new BizErrorException("同个产品料号不能绑定多个产品工艺路线");
                }
            }
        }

        baseProductProcessRoute.setModifiedUserId(currentUser.getUserId());
        baseProductProcessRoute.setModifiedTime(new Date());
        baseProductProcessRoute.setOrganizationId(currentUser.getOrganizationId());
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
            baseProductProcessRoute.setOrganizationId(currentUser.getOrganizationId());
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


    @Override
    public BaseProductProcessRoute addOrUpdate(BaseProductProcessRoute baseProductProcessRoute) {
        Example example = new Example(BaseProductProcessRoute.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("materialId", baseProductProcessRoute.getMaterialId());
        criteria.andEqualTo("routeId", baseProductProcessRoute.getRouteId());
        criteria.andEqualTo("organizationId", baseProductProcessRoute.getOrganizationId());
        List<BaseProductProcessRoute> baseProductProcessRouteOld = baseProductProcessRouteMapper.selectByExample(example);

        baseProductProcessRoute.setModifiedTime(new Date());
        if (StringUtils.isNotEmpty(baseProductProcessRouteOld)){
            baseProductProcessRoute.setProductProcessRouteId(baseProductProcessRouteOld.get(0).getProductProcessRouteId());
            baseProductProcessRouteMapper.updateByPrimaryKey(baseProductProcessRoute);
        }else{
            baseProductProcessRoute.setCreateTime(new Date());
            baseProductProcessRouteMapper.insertUseGeneratedKeys(baseProductProcessRoute);
        }
        return baseProductProcessRoute;
    }


    @Override
    public List<BaseProductProcessRoute> findListByCondition(Map<String, Object> map) {
        if(StringUtils.isEmpty(map.get("orgId"))) {
            SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
            map.put("orgId", user.getOrganizationId());
        }
        List<BaseProductProcessRoute> list = new ArrayList<>();
        Map<String, Object> maps = new HashMap<>();
        maps.put("orgId", map.get("orgId"));
        //依照物料，产品，产线，默认的优先级进行查询
        if(StringUtils.isNotEmpty(map.get("materialId"))) {
            maps.put("materialId", map.get("materialId"));
            list = baseProductProcessRouteMapper.findList(maps);
            if(StringUtils.isNotEmpty(list)){
                setName(list);
                return list;
            }
        }
        if(StringUtils.isNotEmpty(map.get("materialCode"))) {
            maps.remove("materialId");
            maps.put("materialCode", map.get("materialCode"));
            list = baseProductProcessRouteMapper.findList(maps);
            if(StringUtils.isNotEmpty(list)){
                setName(list);
                return list;
            }
        }
        if(StringUtils.isNotEmpty(map.get("productModelId"))) {
            maps.remove("materialCode");
            maps.put("productModelId", map.get("productModelId"));
            list = baseProductProcessRouteMapper.findList(maps);
            if(StringUtils.isNotEmpty(list)){
                setName(list);
                return list;
            }
        }
        if(StringUtils.isNotEmpty(map.get("proLineId"))) {
            maps.remove("productModelId");
            maps.put("proLineId", map.get("proLineId"));
            list = baseProductProcessRouteMapper.findList(maps);
            if(StringUtils.isNotEmpty(list)){
                setName(list);
                return list;
            }
        }
        maps.remove("proLineId");
        maps.put("productType", 0);
        list = baseProductProcessRouteMapper.findList(maps);
        return list;
    }

    public void setName(List<BaseProductProcessRoute> list){
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
    }

}
