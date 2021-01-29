package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.dto.basic.SmtProcessCategoryDto;
import com.fantechs.common.base.entity.basic.SmtMaterial;
import com.fantechs.common.base.entity.basic.SmtProcessCategory;
import com.fantechs.common.base.entity.basic.SmtRoute;
import com.fantechs.common.base.entity.basic.history.SmtHtProcessCategory;
import com.fantechs.common.base.entity.basic.search.SearchSmtMaterial;
import com.fantechs.common.base.entity.basic.search.SearchSmtRoute;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BasePlatePartsDetDto;
import com.fantechs.common.base.general.dto.basic.BasePlatePartsDto;
import com.fantechs.common.base.general.dto.basic.imports.BasePlatePartsImport;
import com.fantechs.common.base.general.entity.basic.BasePartsInformation;
import com.fantechs.common.base.general.entity.basic.BasePlateParts;
import com.fantechs.common.base.general.entity.basic.BasePlatePartsDet;
import com.fantechs.common.base.general.entity.basic.history.BaseHtPartsInformation;
import com.fantechs.common.base.general.entity.basic.history.BaseHtPlateParts;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.imes.basic.BasicFeignApi;
import com.fantechs.provider.base.mapper.BaseHtPlatePartsMapper;
import com.fantechs.provider.base.mapper.BasePartsInformationMapper;
import com.fantechs.provider.base.mapper.BasePlatePartsDetMapper;
import com.fantechs.provider.base.mapper.BasePlatePartsMapper;
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
    private BasicFeignApi basicFeignApi;
    @Resource
    private BasePartsInformationMapper basePartsInformationMapper;

    @Override
    public List<BasePlatePartsDto> findList(Map<String, Object> map) {
        return basePlatePartsMapper.findList(map);
    }

    @Override
    public int save(BasePlateParts basePlateParts) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

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
        baseHtPlatePartsMapper.insert(baseHtPlateParts);

        List<BasePlatePartsDetDto> list = basePlateParts.getList();
        if (StringUtils.isNotEmpty(list)) {
            for (BasePlatePartsDet basePlatePartsDet : list) {
                basePlatePartsDet.setPlatePartsId(basePlateParts.getPlatePartsId());
            }
            basePlatePartsDetMapper.insertList(list);
        }

        return i;
    }

    @Override
    public int update(BasePlateParts basePlateParts) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        basePlateParts.setModifiedTime(new Date());
        basePlateParts.setModifiedUserId(user.getUserId());

        BaseHtPlateParts baseHtPlateParts = new BaseHtPlateParts();
        BeanUtils.copyProperties(basePlateParts, baseHtPlateParts);
        baseHtPlatePartsMapper.insert(baseHtPlateParts);

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
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
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
        if (StringUtils.isEmpty(currentUser)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Map<String, Object> resutlMap = new HashMap<>();  //封装操作结果
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数

        //排除不合法的数据
        for (int i = 0; i < basePlatePartsImports.size(); i++) {
            BasePlatePartsImport basePlatePartsImport = basePlatePartsImports.get(i);
            String materialCode = basePlatePartsImport.getMaterialCode();
            String partsInformationCode = basePlatePartsImport.getPartsInformationCode();
            String routeCode = basePlatePartsImport.getRouteCode();
            //物料编码必传
            if (StringUtils.isEmpty(
                    materialCode
            )) {
                fail.add(i + 3);
                basePlatePartsImports.remove(i);
                continue;
            }

            //输入了部件编码则工艺路线编码必传
            if (StringUtils.isNotEmpty(partsInformationCode) && StringUtils.isEmpty(routeCode)) {
                fail.add(i + 3);
                basePlatePartsImports.remove(i);
                continue;
            }

            //判断该物料、部件、工艺路线是否存在
            SearchSmtMaterial searchSmtMaterial = new SearchSmtMaterial();
            searchSmtMaterial.setMaterialCode(materialCode);
            searchSmtMaterial.setCodeQueryMark(1);
            SmtMaterial smtMaterial = basicFeignApi.findSmtMaterialList(searchSmtMaterial).getData().get(0);
            if (StringUtils.isEmpty(smtMaterial)) {
                fail.add(i + 3);
                basePlatePartsImports.remove(i);
                continue;
            }

            if (StringUtils.isNotEmpty(partsInformationCode)) {
                Example example1 = new Example(BasePartsInformation.class);
                Example.Criteria criteria = example1.createCriteria();
                criteria.andEqualTo("partsInformationCode", partsInformationCode);
                BasePartsInformation basePartsInformation = basePartsInformationMapper.selectOneByExample(example1);

                SearchSmtRoute searchSmtRoute = new SearchSmtRoute();
                searchSmtRoute.setRouteCode(routeCode);
                searchSmtRoute.setCodeQueryMark(1);
                SmtRoute smtRoute = basicFeignApi.findRouteList(searchSmtRoute).getData().get(0);
                if (StringUtils.isEmpty(basePartsInformation, smtRoute)) {
                    fail.add(i + 3);
                    basePlatePartsImports.remove(i);
                    continue;
                }

                //判断该产品是否已经配置组成部件
                Example example = new Example(BasePlateParts.class);
                example.createCriteria().andEqualTo("materialId", smtMaterial.getMaterialId());
                List<BasePlateParts> basePlateParts1 = basePlatePartsMapper.selectByExample(example);
                if (StringUtils.isNotEmpty(basePlateParts1)) {
                    fail.add(i + 3);
                    basePlatePartsImports.remove(i);
                    continue;
                }

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
                basePlatePartsMapper.insertUseGeneratedKeys(basePlateParts);

                //新增部件组成履历
                BaseHtPlateParts baseHtPlateParts = new BaseHtPlateParts();
                BeanUtils.copyProperties(basePlateParts, baseHtPlateParts);
                baseHtPlatePartsMapper.insert(baseHtPlateParts);

                for (BasePlatePartsImport platePartsImport : basePlatePartsImports1) {
                    //新增部件组成明细
                    BasePlatePartsDet basePlatePartsDet = new BasePlatePartsDet();
                    BeanUtils.copyProperties(platePartsImport, basePlatePartsDet);
                    basePlatePartsDet.setPlatePartsId(basePlateParts.getPlatePartsId());
                    basePlatePartsDet.setCreateTime(new Date());
                    basePlatePartsDet.setCreateUserId(currentUser.getUserId());
                    basePlatePartsDet.setModifiedTime(new Date());
                    basePlatePartsDet.setModifiedUserId(currentUser.getUserId());
                    success += basePlatePartsDetMapper.insertSelective(basePlatePartsDet);
                }
            }

            resutlMap.put("操作成功总数", success);
            resutlMap.put("操作失败行数", fail);
        }
        return resutlMap;
    }
}
