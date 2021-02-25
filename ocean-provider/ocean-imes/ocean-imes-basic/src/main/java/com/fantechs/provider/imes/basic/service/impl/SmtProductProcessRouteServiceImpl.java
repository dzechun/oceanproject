package com.fantechs.provider.imes.basic.service.impl;


import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.basic.*;
import com.fantechs.common.base.entity.basic.history.SmtHtProductProcessRoute;
import com.fantechs.common.base.dto.basic.imports.SmtProductProcessRouteImport;
import com.fantechs.common.base.entity.basic.search.SearchSmtProductProcessRoute;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseOrganizationDto;
import com.fantechs.common.base.general.entity.basic.BaseOrganization;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseOrganization;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.imes.basic.mapper.*;
import com.fantechs.provider.imes.basic.service.SmtProductProcessRouteService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.lang.reflect.Array;
import java.util.*;

import static java.util.Arrays.asList;

/**
 * Created by wcz on 2020/09/30.
 */
@Service
public class SmtProductProcessRouteServiceImpl extends BaseService<SmtProductProcessRoute> implements SmtProductProcessRouteService {

    @Resource
    private SmtProductProcessRouteMapper smtProductProcessRouteMapper;
    @Resource
    private SmtHtProductProcessRouteMapper smtHtProductProcessRouteMapper;
    @Resource
    private SmtRouteMapper smtRouteMapper;
    @Resource
    private SmtProductModelMapper smtProductModelMapper;
    @Resource
    private SmtProLineMapper smtProLineMapper;
    @Resource
    private SmtMaterialMapper smtMaterialMapper;
    @Resource
    private BaseFeignApi baseFeignApi;


    @Override
    public List<SmtProductProcessRoute> findList(SearchSmtProductProcessRoute searchSmtProductProcessRoute) {
        List<SmtProductProcessRoute> list = smtProductProcessRouteMapper.findList(searchSmtProductProcessRoute);
        for (SmtProductProcessRoute smtProductProcessRoute : list) {
            Integer productType = smtProductProcessRoute.getProductType();
            if (productType == 0) {
                smtProductProcessRoute.setProductName("*");
            } else if (productType == 1) {
                smtProductProcessRoute.setProductName(smtProductProcessRoute.getProName());
            } else if (productType == 2) {
                smtProductProcessRoute.setProductName(smtProductProcessRoute.getProductModelCode());
            } else if (productType == 3) {
                smtProductProcessRoute.setProductName(smtProductProcessRoute.getMaterialCode());
            }
        }
        return list;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(SmtProductProcessRoute smtProductProcessRoute) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(currentUser)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Integer productType = smtProductProcessRoute.getProductType();
        Long proLineId = smtProductProcessRoute.getProLineId();
        Long productModelId = smtProductProcessRoute.getProductModelId();
        Long materialId = smtProductProcessRoute.getMaterialId();
        if (productType == 0) {
            Example example = new Example(SmtProductProcessRoute.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("productType", productType);
            List<SmtProductProcessRoute> smtProductProcessRoutes = smtProductProcessRouteMapper.selectByExample(example);
            if (StringUtils.isNotEmpty(smtProductProcessRoutes)) {
                throw new BizErrorException("产品类别为All(*)的工艺路线只能有一条");
            }
        } else {
            Example example = new Example(SmtProductProcessRoute.class);
            Example.Criteria criteria = example.createCriteria();
            Example.Criteria criteria1 = example.createCriteria();
            criteria1.andEqualTo("proLineId", proLineId).orEqualTo("productModelId", productModelId).orEqualTo("materialId", materialId);
            example.and(criteria1);
            criteria.andEqualTo("productType", productType);
            criteria.andEqualTo("routeId", smtProductProcessRoute.getRouteId());
            List<SmtProductProcessRoute> smtProductProcessRoutes = smtProductProcessRouteMapper.selectByExample(example);
            if (StringUtils.isNotEmpty(smtProductProcessRoutes)) {
                throw new BizErrorException("该产品名称的工艺路线已存在");
            }
        }
        smtProductProcessRoute.setCreateUserId(currentUser.getUserId());
        smtProductProcessRoute.setCreateTime(new Date());
        smtProductProcessRoute.setModifiedUserId(currentUser.getUserId());
        smtProductProcessRoute.setModifiedTime(new Date());
        int i = smtProductProcessRouteMapper.insertUseGeneratedKeys(smtProductProcessRoute);

        //新增产品工艺路线历史信息
        SmtHtProductProcessRoute smtHtProductProcessRoute = new SmtHtProductProcessRoute();
        BeanUtils.copyProperties(smtProductProcessRoute, smtHtProductProcessRoute);
        smtHtProductProcessRouteMapper.insertSelective(smtHtProductProcessRoute);
        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(SmtProductProcessRoute smtProductProcessRoute) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(currentUser)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Integer productType = smtProductProcessRoute.getProductType();
        Long proLineId = smtProductProcessRoute.getProLineId();
        Long productModelId = smtProductProcessRoute.getProductModelId();
        Long materialId = smtProductProcessRoute.getMaterialId();
        if (productType == 0) {
            Example example = new Example(SmtProductProcessRoute.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("productType", productType);
            SmtProductProcessRoute productProcessRoute = smtProductProcessRouteMapper.selectOneByExample(example);
            if (StringUtils.isNotEmpty(productProcessRoute) && !productProcessRoute.getProductProcessRouteId().equals(smtProductProcessRoute.getProductProcessRouteId())) {
                throw new BizErrorException("产品类别为All(*)的工艺路线只能有一条");
            }
        } else {
            Example example = new Example(SmtProductProcessRoute.class);
            Example.Criteria criteria = example.createCriteria();
            Example.Criteria criteria1 = example.createCriteria();
            criteria1.andEqualTo("proLineId", proLineId).orEqualTo("productModelId", productModelId).orEqualTo("materialId", materialId);
            example.and(criteria1);
            criteria.andEqualTo("productType", productType);
            criteria.andEqualTo("routeId", smtProductProcessRoute.getRouteId());
            SmtProductProcessRoute productProcessRoute = smtProductProcessRouteMapper.selectOneByExample(example);
            if (StringUtils.isNotEmpty(productProcessRoute) && !productProcessRoute.getProductProcessRouteId().equals(smtProductProcessRoute.getProductProcessRouteId())) {
                throw new BizErrorException("该产品名称的工艺路线已存在");
            }
        }

        smtProductProcessRoute.setModifiedUserId(currentUser.getUserId());
        smtProductProcessRoute.setModifiedTime(new Date());
        int i = smtProductProcessRouteMapper.updateByPrimaryKeySelective(smtProductProcessRoute);

        //新增产品工艺路线历史信息
        SmtHtProductProcessRoute smtHtProductProcessRoute = new SmtHtProductProcessRoute();
        BeanUtils.copyProperties(smtProductProcessRoute, smtHtProductProcessRoute);
        smtHtProductProcessRouteMapper.insertSelective(smtHtProductProcessRoute);
        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(String ids) {
        int i = 0;
        List<SmtHtProductProcessRoute> list = new ArrayList<>();
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(currentUser)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        String[] idsArr = ids.split(",");
        for (String productProcessRouteId : idsArr) {
            SmtProductProcessRoute smtProductProcessRoute = smtProductProcessRouteMapper.selectByPrimaryKey(productProcessRouteId);
            if (StringUtils.isEmpty(smtProductProcessRoute)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012000, productProcessRouteId);
            }

            //新增产品工艺路线历史信息
            SmtHtProductProcessRoute smtHtProductProcessRoute = new SmtHtProductProcessRoute();
            BeanUtils.copyProperties(smtProductProcessRoute, smtHtProductProcessRoute);
            smtHtProductProcessRoute.setModifiedUserId(currentUser.getUserId());
            smtHtProductProcessRoute.setModifiedTime(new Date());
            list.add(smtHtProductProcessRoute);

        }
        smtHtProductProcessRouteMapper.insertList(list);
        i = smtProductProcessRouteMapper.deleteByIds(ids);
        return i;
    }


    @Override
    public Map<String, Object> importExcel(List<SmtProductProcessRouteImport> smtProductProcessRouteImports) {

        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(currentUser)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Map<String, Object> resultMap = new HashMap<>();  //封装操作结果
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数

        //排除不合法的数据
        Iterator<SmtProductProcessRouteImport> iterator = smtProductProcessRouteImports.iterator();
        int i = 0;
        while (iterator.hasNext()) {
            SmtProductProcessRouteImport smtProductProcessRouteImport = iterator.next();
            Integer productType = smtProductProcessRouteImport.getProductType();
            String routeCode = smtProductProcessRouteImport.getRouteCode();
            String productModelCode = smtProductProcessRouteImport.getProductModelCode();
            String proCode = smtProductProcessRouteImport.getProCode();
            String materialCode = smtProductProcessRouteImport.getMaterialCode();
            String organizationCode = smtProductProcessRouteImport.getOrganizationCode();

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
            Example example = new Example(SmtRoute.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("routeCode",routeCode);
            SmtRoute smtRoute = smtRouteMapper.selectOneByExample(example);
            if (StringUtils.isEmpty(smtRoute)){
                fail.add(i + 4);
                iterator.remove();
                i++;
                continue;
            }
            smtProductProcessRouteImport.setRouteId(smtRoute.getRouteId());

            if (StringUtils.isNotEmpty(productModelCode)){
                Example example1 = new Example(SmtProductModel.class);
                Example.Criteria criteria1 = example1.createCriteria();
                criteria1.andEqualTo("productModelCode",productModelCode);
                SmtProductModel smtProductModel = smtProductModelMapper.selectOneByExample(example1);
                if (StringUtils.isEmpty(smtProductModel)){
                    fail.add(i + 4);
                    iterator.remove();
                    i++;
                    continue;
                }
                smtProductProcessRouteImport.setProductModelId(smtProductModel.getProductModelId());
            }


            if (StringUtils.isNotEmpty(proCode)){
                Example example2 = new Example(SmtProLine.class);
                Example.Criteria criteria2 = example2.createCriteria();
                criteria2.andEqualTo("proCode",proCode);
                SmtProLine smtProLine = smtProLineMapper.selectOneByExample(example2);
                if (StringUtils.isEmpty(smtProLine)){
                    fail.add(i + 4);
                    iterator.remove();
                    i++;
                    continue;
                }
                smtProductProcessRouteImport.setProLineId(smtProLine.getProLineId());
            }

            if (StringUtils.isNotEmpty(materialCode)){
                Example example3 = new Example(SmtMaterial.class);
                Example.Criteria criteria3 = example3.createCriteria();
                criteria3.andEqualTo("materialCode",materialCode);
                SmtMaterial smtMaterial = smtMaterialMapper.selectOneByExample(example3);
                if (StringUtils.isEmpty(smtMaterial)){
                    fail.add(i + 4);
                    iterator.remove();
                    i++;
                    continue;
                }
                smtProductProcessRouteImport.setMaterialId(smtMaterial.getMaterialId());
            }

            if (StringUtils.isNotEmpty(organizationCode)){
                SearchBaseOrganization searchBaseOrganization = new SearchBaseOrganization();
                searchBaseOrganization.setCodeQueryMark(1);
                searchBaseOrganization.setOrganizationCode(organizationCode);
                List<BaseOrganizationDto> baseOrganizationDtos = baseFeignApi.findOrganizationList(searchBaseOrganization).getData();
                if (StringUtils.isNotEmpty(baseOrganizationDtos)){
                    BaseOrganization baseOrganization = baseOrganizationDtos.get(0);
                    smtProductProcessRouteImport.setOrganizationId(baseOrganization.getOrganizationId());
                }else {
                    fail.add(i + 4);
                    iterator.remove();
                    i++;
                    continue;
                }
            }
            i++;
        }

        for (SmtProductProcessRouteImport smtProductProcessRouteImport : smtProductProcessRouteImports) {
            SmtProductProcessRoute smtProductProcessRoute = new SmtProductProcessRoute();
            BeanUtils.copyProperties(smtProductProcessRouteImport,smtProductProcessRoute);
            smtProductProcessRoute.setCreateTime(new Date());
            smtProductProcessRoute.setCreateUserId(currentUser.getUserId());
            smtProductProcessRoute.setModifiedTime(new Date());
            smtProductProcessRoute.setModifiedUserId(currentUser.getUserId());
            success += smtProductProcessRouteMapper.insertUseGeneratedKeys(smtProductProcessRoute);

            //新增履历
            SmtHtProductProcessRoute smtHtProductProcessRoute = new SmtHtProductProcessRoute();
            BeanUtils.copyProperties(smtProductProcessRoute,smtHtProductProcessRoute);
            smtHtProductProcessRouteMapper.insertSelective(smtHtProductProcessRoute);
        }
        resultMap.put("操作成功总数", success);
        resultMap.put("操作失败行", fail);
        return resultMap;
    }
}
