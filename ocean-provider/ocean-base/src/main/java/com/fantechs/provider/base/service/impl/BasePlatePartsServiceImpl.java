package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseMaterialDto;
import com.fantechs.common.base.general.dto.basic.BasePlatePartsDetDto;
import com.fantechs.common.base.general.dto.basic.BasePlatePartsDto;
import com.fantechs.common.base.general.dto.basic.imports.BasePlatePartsImport;
import com.fantechs.common.base.general.entity.basic.BasePartsInformation;
import com.fantechs.common.base.general.entity.basic.BasePlateParts;
import com.fantechs.common.base.general.entity.basic.BasePlatePartsDet;
import com.fantechs.common.base.general.entity.basic.BaseRoute;
import com.fantechs.common.base.general.entity.basic.history.BaseHtPlateParts;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseMaterial;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseRoute;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.*;
import com.fantechs.provider.base.service.BasePlatePartsService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by leifengzhi on 2021/01/15.
 */
@Service
public class BasePlatePartsServiceImpl extends BaseService<BasePlateParts> implements BasePlatePartsService {

    @Resource
    private BasePlatePartsMapper basePlatePartsMapper;
    @Resource
    private BaseHtPlatePartsMapper baseHtPlatePartsMapper;
    @Resource
    private BasePlatePartsDetMapper basePlatePartsDetMapper;
    @Resource
    private BasePartsInformationMapper basePartsInformationMapper;
    @Resource
    private BaseMaterialMapper baseMaterialMapper;
    @Resource
    private BaseRouteMapper baseRouteMapper;

    @Override
    public List<BasePlatePartsDto> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId", user.getOrganizationId());
        return basePlatePartsMapper.findList(map);
    }

    @Override
    public int save(BasePlateParts basePlateParts) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        Example example = new Example(BasePlateParts.class);
        example.createCriteria().andEqualTo("materialId", basePlateParts.getMaterialId());
        List<BasePlateParts> basePlateParts1 = basePlatePartsMapper.selectByExample(example);
        if (StringUtils.isNotEmpty(basePlateParts1)) {
            throw new BizErrorException("该产品已配置组成部件，请勿重复配置");
        }

        basePlateParts.setCreateTime(new Date());
        basePlateParts.setCreateUserId(user.getUserId());
        basePlateParts.setModifiedTime(new Date());
        basePlateParts.setModifiedUserId(user.getUserId());
        basePlateParts.setStatus(StringUtils.isEmpty(basePlateParts.getStatus()) ? 1 : basePlateParts.getStatus());
        basePlateParts.setIfCustomized(StringUtils.isEmpty(basePlateParts.getIfCustomized()) ? 0 : basePlateParts.getIfCustomized());
        basePlateParts.setOrganizationId(user.getOrganizationId());

        int i = basePlatePartsMapper.insertUseGeneratedKeys(basePlateParts);

        BaseHtPlateParts baseHtPlateParts = new BaseHtPlateParts();
        BeanUtils.copyProperties(basePlateParts, baseHtPlateParts);
        baseHtPlatePartsMapper.insertSelective(baseHtPlateParts);

        List<BasePlatePartsDetDto> list = basePlateParts.getList();
        if (StringUtils.isNotEmpty(list)) {
            for (BasePlatePartsDet basePlatePartsDet : list) {
                if(StringUtils.isEmpty(basePlatePartsDet.getPartsInformationId())){
                    throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(),"部件不能为空");
                }
                if(StringUtils.isEmpty(basePlatePartsDet.getRouteId())){
                    throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(),"工艺路线不能为空");
                }
                basePlatePartsDet.setPlatePartsId(basePlateParts.getPlatePartsId());
            }
            basePlatePartsDetMapper.insertList(list);
        }

        return i;
    }

    @Override
    public int update(BasePlateParts basePlateParts) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        basePlateParts.setModifiedTime(new Date());
        basePlateParts.setModifiedUserId(user.getUserId());
        basePlateParts.setOrganizationId(user.getOrganizationId());

        BaseHtPlateParts baseHtPlateParts = new BaseHtPlateParts();
        BeanUtils.copyProperties(basePlateParts, baseHtPlateParts);
        baseHtPlatePartsMapper.insertSelective(baseHtPlateParts);

        Example example = new Example(BasePlatePartsDet.class);
        example.createCriteria().andEqualTo("platePartsId", basePlateParts.getPlatePartsId());
        basePlatePartsDetMapper.deleteByExample(example);

        System.out.println("数据：" + basePlateParts.getList());
        if (StringUtils.isNotEmpty(basePlateParts.getList())) {
            basePlatePartsDetMapper.insertList(basePlateParts.getList());
        }
        return basePlatePartsMapper.updateByPrimaryKeySelective(basePlateParts);
    }

    @Override
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        List<BaseHtPlateParts> list = new ArrayList<>();
        String[] idsArr = ids.split(",");
        for (String id : idsArr) {
            BasePlateParts basePlateParts = basePlatePartsMapper.selectByPrimaryKey(id);
            if (StringUtils.isEmpty(basePlateParts)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

            BaseHtPlateParts baseHtPlateParts = new BaseHtPlateParts();
            BeanUtils.copyProperties(basePlateParts, baseHtPlateParts);
            list.add(baseHtPlateParts);
        }

        baseHtPlatePartsMapper.insertList(list);

        Example example = new Example(BasePlatePartsDet.class);
        example.createCriteria().andIn("platePartsId", Arrays.asList(ids.split(",")));
        basePlatePartsDetMapper.deleteByExample(example);

        return basePlatePartsMapper.deleteByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importExcel(List<BasePlatePartsImport> basePlatePartsImports) {

        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();

        Map<String, Object> resultMap = new HashMap<>();  //封装操作结果
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数

        //排除不合法的数据
        Iterator<BasePlatePartsImport> iterator = basePlatePartsImports.iterator();
        int i = 0;
        while (iterator.hasNext()) {
            BasePlatePartsImport basePlatePartsImport = iterator.next();
            String materialCode = basePlatePartsImport.getMaterialCode();//产品编码
            String partsInformationCode = basePlatePartsImport.getPartsInformationCode();//部件编码
            String routeName = basePlatePartsImport.getRouteName();//工艺路线名称

            //产品编码必传
            if (StringUtils.isEmpty(
                    materialCode, partsInformationCode, routeName
            )) {
                fail.add(i + 4);
                iterator.remove();
                i++;
                continue;
            }

            //判断该物料、部件、工艺路线是否存在
            SearchBaseMaterial searchBaseMaterial = new SearchBaseMaterial();
            searchBaseMaterial.setMaterialCode(materialCode);
            searchBaseMaterial.setCodeQueryMark(1);
            List<BaseMaterialDto> baseMaterials = baseMaterialMapper.findList(ControllerUtil.dynamicConditionByEntity(searchBaseMaterial));

            Example example1 = new Example(BasePartsInformation.class);
            Example.Criteria criteria = example1.createCriteria();
            criteria.andEqualTo("partsInformationCode", partsInformationCode);
            BasePartsInformation basePartsInformation = basePartsInformationMapper.selectOneByExample(example1);

            SearchBaseRoute searchBaseRoute = new SearchBaseRoute();
            searchBaseRoute.setRouteName(routeName);
            searchBaseRoute.setNameQueryMark(1);
            List<BaseRoute> baseRoutes = baseRouteMapper.findList(ControllerUtil.dynamicConditionByEntity(searchBaseRoute));
            //编码对应的信息不存在或工艺路线的类型不是部件工艺路线
            if (StringUtils.isEmpty(basePartsInformation, baseRoutes, baseMaterials) || baseRoutes.get(0).getRouteType() != 3) {
                fail.add(i + 4);
                iterator.remove();
                i++;
                continue;
            }

            basePlatePartsImport.setPartsInformationId(basePartsInformation.getPartsInformationId());
            basePlatePartsImport.setMaterialId(baseMaterials.get(0).getMaterialId());
            basePlatePartsImport.setRouteId(baseRoutes.get(0).getRouteId());

            //判断该产品是否已经配置组成部件
            Example example = new Example(BasePlateParts.class);
            example.createCriteria().andEqualTo("materialId", baseMaterials.get(0).getMaterialId());
            List<BasePlateParts> basePlateParts1 = basePlatePartsMapper.selectByExample(example);
            if (StringUtils.isNotEmpty(basePlateParts1)) {
                fail.add(i + 4);
                iterator.remove();
                i++;
                continue;
            }
            i++;
        }

        //对合格数据进行分组
        Map<String, List<BasePlatePartsImport>> map = basePlatePartsImports.stream().collect(Collectors.groupingBy(BasePlatePartsImport::getMaterialCode, HashMap::new, Collectors.toList()));
        Set<String> codeList = map.keySet();
        for (String code : codeList) {
            List<BasePlatePartsImport> basePlatePartsImports1 = map.get(code);
            //新增部件组成父表
            BasePlateParts basePlateParts = new BasePlateParts();
            BeanUtils.copyProperties(basePlatePartsImports1.get(0), basePlateParts);
            basePlateParts.setCreateTime(new Date());
            basePlateParts.setCreateUserId(currentUser.getUserId());
            basePlateParts.setModifiedTime(new Date());
            basePlateParts.setModifiedUserId(currentUser.getUserId());
            basePlateParts.setStatus((byte) 1);
            basePlateParts.setOrganizationId(currentUser.getOrganizationId());
            basePlatePartsMapper.insertUseGeneratedKeys(basePlateParts);

            //新增部件组成履历
            BaseHtPlateParts baseHtPlateParts = new BaseHtPlateParts();
            BeanUtils.copyProperties(basePlateParts, baseHtPlateParts);
            baseHtPlatePartsMapper.insertSelective(baseHtPlateParts);

            for (BasePlatePartsImport platePartsImport : basePlatePartsImports1) {
                //新增部件组成明细
                BasePlatePartsDet basePlatePartsDet = new BasePlatePartsDet();
                BeanUtils.copyProperties(platePartsImport, basePlatePartsDet);
                basePlatePartsDet.setCreateTime(new Date());
                basePlatePartsDet.setCreateUserId(currentUser.getUserId());
                basePlatePartsDet.setModifiedTime(new Date());
                basePlatePartsDet.setModifiedUserId(currentUser.getUserId());
                basePlatePartsDet.setStatus((byte) 1);
                basePlatePartsDet.setPlatePartsId(basePlateParts.getPlatePartsId());
                basePlatePartsDet.setOrganizationId(currentUser.getOrganizationId());
                success += basePlatePartsDetMapper.insertSelective(basePlatePartsDet);
            }
        }
        resultMap.put("操作成功总数", success);
        resultMap.put("操作失败行", fail);
        return resultMap;
    }
}
