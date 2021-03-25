package com.fantechs.provider.imes.basic.service.impl;

import cn.afterturn.easypoi.cache.manager.IFileLoader;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.dto.basic.SmtMaterialPackageDto;
import com.fantechs.common.base.dto.basic.SmtPackageSpecificationDto;
import com.fantechs.common.base.dto.basic.SmtPackingUnitDto;
import com.fantechs.common.base.dto.basic.imports.SmtPackageSpecificationImport;
import com.fantechs.common.base.entity.basic.*;
import com.fantechs.common.base.entity.basic.history.SmtHtFactory;
import com.fantechs.common.base.entity.basic.history.SmtHtPackageSpecification;
import com.fantechs.common.base.entity.basic.history.SmtHtPackingUnit;
import com.fantechs.common.base.entity.basic.search.SearchSmtMaterial;
import com.fantechs.common.base.entity.basic.search.SearchSmtMaterialPackage;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.imports.BasePlatePartsImport;
import com.fantechs.common.base.general.dto.mes.pm.SmtBarcodeRuleDto;
import com.fantechs.common.base.general.dto.mes.pm.search.SearchSmtBarcodeRule;
import com.fantechs.common.base.general.entity.bcm.search.SearchBcmBarCode;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.mes.pm.PMFeignApi;
import com.fantechs.provider.imes.basic.mapper.*;
import com.fantechs.provider.imes.basic.service.SmtPackageSpecificationService;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import javax.naming.NamingEnumeration;
import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by leifengzhi on 2020/11/04.
 */
@Service
public class SmtPackageSpecificationServiceImpl extends BaseService<SmtPackageSpecification> implements SmtPackageSpecificationService {

    @Resource
    private SmtPackageSpecificationMapper smtPackageSpecificationMapper;
    @Resource
    private SmtHtPackageSpecificationMapper smtHtPackageSpecificationMapper;
    @Resource
    private SmtMaterialPackageMapper smtMaterialPackageMapper;
    @Resource
    private SmtMaterialMapper smtMaterialMapper;
    @Resource
    private SmtProcessMapper smtProcessMapper;
    @Resource
    private PMFeignApi pmFeignApi;
    @Resource
    private SmtPackingUnitMapper smtPackingUnitMapper;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(SmtPackageSpecification smtPackageSpecification) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(SmtPackageSpecification.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("packageSpecificationCode", smtPackageSpecification.getPackageSpecificationCode());
        List<SmtPackageSpecification> smtPackageSpecifications = smtPackageSpecificationMapper.selectByExample(example);
        if (StringUtils.isNotEmpty(smtPackageSpecifications)) {
            throw new BizErrorException("包装规格编码已存在");
        }

        smtPackageSpecification.setCreateUserId(user.getUserId());
        smtPackageSpecification.setCreateTime(new Date());
        smtPackageSpecification.setModifiedUserId(user.getUserId());
        smtPackageSpecification.setModifiedTime(new Date());

        //新增包装规格
        smtPackageSpecificationMapper.insertUseGeneratedKeys(smtPackageSpecification);

        //新增包装规格和物料关系
        List<SmtMaterialPackageDto> smtMaterialPackages = smtPackageSpecification.getSmtMaterialPackages();
        if (StringUtils.isNotEmpty(smtMaterialPackages)){
            for (SmtMaterialPackage smtMaterialPackage : smtMaterialPackages) {
                if (StringUtils.isNotEmpty(smtMaterialPackage.getProcessId())){
                    Example example1 = new Example(SmtMaterialPackage.class);
                    Example.Criteria criteria1 = example1.createCriteria();
                    criteria1.andEqualTo("materialId",smtMaterialPackage.getMaterialId())
                            .andEqualTo("processId",smtMaterialPackage.getProcessId());
                    List<SmtMaterialPackage> smtMaterialPackages1 = smtMaterialPackageMapper.selectByExample(example1);
                    if (StringUtils.isNotEmpty(smtMaterialPackages1)){
                        throw new BizErrorException("物料在工序下的包装规格已存在");
                    }
                }
                smtMaterialPackage.setPackageSpecificationId(smtPackageSpecification.getPackageSpecificationId());
            }
        }
        if (StringUtils.isNotEmpty(smtMaterialPackages)){
            smtMaterialPackageMapper.insertList(smtMaterialPackages);
        }

        SmtHtPackageSpecification smtHtPackageSpecification = new SmtHtPackageSpecification();
        BeanUtils.copyProperties(smtPackageSpecification, smtHtPackageSpecification);
        return smtHtPackageSpecificationMapper.insert(smtHtPackageSpecification);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(SmtPackageSpecification smtPackageSpecification) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(SmtPackageSpecification.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("packageSpecificationCode", smtPackageSpecification.getPackageSpecificationCode())
        .andNotEqualTo("packageSpecificationId",smtPackageSpecification.getPackageSpecificationId());
        List<SmtPackageSpecification> smtPackageSpecifications = smtPackageSpecificationMapper.selectByExample(example);
        if (StringUtils.isNotEmpty(smtPackageSpecifications)) {
            throw new BizErrorException("包装规格编码已存在");
        }

        smtPackageSpecification.setModifiedUserId(user.getUserId());
        smtPackageSpecification.setModifiedTime(new Date());

        SmtHtPackageSpecification smtHtPackageSpecification = new SmtHtPackageSpecification();
        BeanUtils.copyProperties(smtPackageSpecification, smtHtPackageSpecification);
        smtHtPackageSpecificationMapper.insert(smtHtPackageSpecification);

        //移除旧的绑定关系
        Example example1 = new Example(SmtMaterialPackage.class);
        Example.Criteria criteria1 = example1.createCriteria();
        criteria1.andEqualTo("packageSpecificationId",smtPackageSpecification.getPackageSpecificationId());
        smtMaterialPackageMapper.deleteByExample(example1);

        //新增绑定关系
        List<SmtMaterialPackageDto> smtMaterialPackages = smtPackageSpecification.getSmtMaterialPackages();
        if (StringUtils.isNotEmpty(smtMaterialPackages)){
            for (SmtMaterialPackage smtMaterialPackage : smtMaterialPackages) {
            if (StringUtils.isNotEmpty(smtMaterialPackage.getProcessId())){
                Example example2 = new Example(SmtMaterialPackage.class);
                Example.Criteria criteria2 = example2.createCriteria();
                criteria2.andEqualTo("materialId",smtMaterialPackage.getMaterialId())
                        .andEqualTo("processId",smtMaterialPackage.getProcessId())
                        .andNotEqualTo("packageSpecificationId",smtPackageSpecification.getPackageSpecificationId());
                List<SmtMaterialPackage> smtMaterialPackages1 = smtMaterialPackageMapper.selectByExample(example2);
                if (StringUtils.isNotEmpty(smtMaterialPackages1)){
                    throw new BizErrorException("物料在工序下的包装规格已存在");
                }
            }
            smtMaterialPackage.setPackageSpecificationId(smtPackageSpecification.getPackageSpecificationId());
            }
        }
        if (StringUtils.isNotEmpty(smtMaterialPackages)){
            smtMaterialPackageMapper.insertList(smtMaterialPackages);
        }
        return smtPackageSpecificationMapper.updateByPrimaryKeySelective(smtPackageSpecification);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        List<SmtHtPackageSpecification> smtPackageSpecifications = new LinkedList<>();
        String[] idsArr = ids.split(",");
        for (String id : idsArr) {
            SmtPackageSpecification smtPackageSpecification = smtPackageSpecificationMapper.selectByPrimaryKey(id);
            if (StringUtils.isEmpty(smtPackageSpecification)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            SmtHtPackageSpecification smtHtPackageSpecification = new SmtHtPackageSpecification();
            BeanUtils.copyProperties(smtPackageSpecification, smtHtPackageSpecification);
            smtHtPackageSpecification.setModifiedTime(new Date());
            smtHtPackageSpecification.setModifiedUserId(user.getUserId());
            smtPackageSpecifications.add(smtHtPackageSpecification);

            //删除包装规格物料绑定关系
            Example example = new Example(SmtMaterialPackage.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("packageSpecificationId",smtHtPackageSpecification.getPackageSpecificationId());
            smtMaterialPackageMapper.deleteByExample(example);
        }
        smtHtPackageSpecificationMapper.insertList(smtPackageSpecifications);
        return smtPackageSpecificationMapper.deleteByIds(ids);
    }

    @Override
    public List<SmtPackageSpecificationDto> findList(Map<String, Object> map) {
        List<SmtPackageSpecificationDto> smtPackageSpecificationDtos = smtPackageSpecificationMapper.findList(map);
        SearchSmtMaterialPackage searchSmtMaterialPackage = new SearchSmtMaterialPackage();

        for (SmtPackageSpecificationDto smtPackageSpecificationDto : smtPackageSpecificationDtos) {
            searchSmtMaterialPackage.setPackageSpecificationId(smtPackageSpecificationDto.getPackageSpecificationId());
            List<SmtMaterialPackageDto> smtMaterialPackageDtos = smtMaterialPackageMapper.findList(searchSmtMaterialPackage);
            if (StringUtils.isNotEmpty(smtMaterialPackageDtos)){
                smtPackageSpecificationDto.setSmtMaterialPackages(smtMaterialPackageDtos);
            }
        }

        return smtPackageSpecificationDtos;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importExcel(List<SmtPackageSpecificationImport> smtPackageSpecificationImports) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        Map<String, Object> resultMap = new HashMap<>();  //封装操作结果
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数
        LinkedList<SmtPackageSpecification> list = new LinkedList<>();
        LinkedList<SmtHtPackageSpecification> htList = new LinkedList<>();
        LinkedList<SmtPackageSpecificationImport> packageSpecificationImports = new LinkedList<>();
        LinkedList<SmtMaterialPackage> smtMaterialPackages = new LinkedList<>();
        for (int i = 0; i < smtPackageSpecificationImports.size(); i++) {
            SmtPackageSpecificationImport smtPackageSpecificationImport = smtPackageSpecificationImports.get(i);
            String packageSpecificationCode = smtPackageSpecificationImport.getPackageSpecificationCode();
            String packageSpecificationName = smtPackageSpecificationImport.getPackageSpecificationName();
            String materialCode = smtPackageSpecificationImport.getMaterialCode();
            String processCode = smtPackageSpecificationImport.getProcessCode();
            String barcodeRuleCode = smtPackageSpecificationImport.getBarcodeRuleCode();
            String packingUnitCode = smtPackageSpecificationImport.getPackingUnitCode();
            if (StringUtils.isEmpty(
                    packageSpecificationCode,packageSpecificationName,materialCode,processCode
            )){
                fail.add(i+4);
                continue;
            }

            //判断编码是否重复
            Example example = new Example(SmtSignature.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("packageSpecificationCode",packageSpecificationCode);
            if (StringUtils.isNotEmpty(smtPackageSpecificationMapper.selectOneByExample(example))){
                fail.add(i+4);
                continue;
            }

            //物料编码不为空则判断物料信息是否存在
            Example example1 = new Example(SmtMaterial.class);
            Example.Criteria criteria1 = example1.createCriteria();
            criteria1.andEqualTo("materialCode",materialCode);
            SmtMaterial smtMaterial = smtMaterialMapper.selectOneByExample(example1);
            if (StringUtils.isEmpty(smtMaterial)){
                fail.add(i+4);
                continue;
            }
            smtPackageSpecificationImport.setMaterialId(smtMaterial.getMaterialId());

            //如果工序编码不为空，则判断工序信息是否存在
            Example example2 = new Example(SmtProcess.class);
            Example.Criteria criteria2 = example2.createCriteria();
            criteria2.andEqualTo("processCode",processCode);
            SmtProcess smtProcess = smtProcessMapper.selectOneByExample(example2);
            if (StringUtils.isEmpty(smtProcess)){
                fail.add(i+4);
                continue;
            }
            smtPackageSpecificationImport.setProcessId(smtProcess.getProcessId());

            //如果条码规则编码不为空，则判断条码规则信息是否存在
            if (StringUtils.isNotEmpty(barcodeRuleCode)){
                SearchSmtBarcodeRule searchSmtBarcodeRule = new SearchSmtBarcodeRule();
                searchSmtBarcodeRule.setBarcodeRuleCode(barcodeRuleCode);
                searchSmtBarcodeRule.setCodeQueryMark((byte) 1);
                List<SmtBarcodeRuleDto> smtBarcodeRuleDtos = pmFeignApi.findBarcodeRulList(searchSmtBarcodeRule).getData();
                if (StringUtils.isEmpty(smtBarcodeRuleDtos)){
                    fail.add(i+4);
                    continue;
                }
                smtPackageSpecificationImport.setBarcodeRuleId(smtBarcodeRuleDtos.get(0).getBarcodeRuleId());
            }

            //如果包装单位编码不为空，则判断包装单位信息是否存在
            if (StringUtils.isNotEmpty(packingUnitCode)){
                Example example3 = new Example(SmtPackingUnit.class);
                Example.Criteria criteria3 = example1.createCriteria();
                criteria3.andEqualTo("packingUnitCode",packingUnitCode);
                SmtPackingUnit smtPackingUnit = smtPackingUnitMapper.selectOneByExample(example3);
                if (StringUtils.isEmpty(smtPackingUnit)){
                    fail.add(i+4);
                    continue;
                }
                smtPackageSpecificationImport.setPackingUnitId(smtPackingUnit.getPackingUnitId());
            }

            packageSpecificationImports.add(smtPackageSpecificationImport);
        }

        if (StringUtils.isNotEmpty(smtPackageSpecificationImports)){
            //对合格数据进行分组
            Map<String, List<SmtPackageSpecificationImport>> map = smtPackageSpecificationImports.stream().collect(Collectors.groupingBy(SmtPackageSpecificationImport::getPackageSpecificationCode, HashMap::new, Collectors.toList()));
            Set<String> codeList = map.keySet();
            for (String code : codeList) {
                List<SmtPackageSpecificationImport> PackageSpecificationImports1 = map.get(code);
                //新增包装规格父级数据
                SmtPackageSpecification smtPackageSpecification = new SmtPackageSpecification();
                BeanUtils.copyProperties(PackageSpecificationImports1.get(0),smtPackageSpecification);
                smtPackageSpecification.setCreateTime(new Date());
                smtPackageSpecification.setCreateUserId(currentUser.getUserId());
                smtPackageSpecification.setModifiedUserId(currentUser.getUserId());
                smtPackageSpecification.setModifiedTime(new Date());
                smtPackageSpecification.setOrganizationId(currentUser.getOrganizationId());
                smtPackageSpecification.setStatus((byte) 1);
                success += smtPackageSpecificationMapper.insertUseGeneratedKeys(smtPackageSpecification);

                SmtHtPackageSpecification smtHtPackageSpecification = new SmtHtPackageSpecification();
                BeanUtils.copyProperties(smtPackageSpecification,smtHtPackageSpecification);
                smtHtPackageSpecification.setModifiedTime(new Date());
                smtHtPackageSpecification.setModifiedUserId(currentUser.getUserId());
                htList.add(smtHtPackageSpecification);

                //新增包装规格物料数据
                for (SmtPackageSpecificationImport smtPackageSpecificationImport : PackageSpecificationImports1) {
                    SmtMaterialPackage smtMaterialPackage = new SmtMaterialPackage();
                    BeanUtils.copyProperties(smtPackageSpecificationImport,smtMaterialPackage);
                    smtMaterialPackage.setStatus((byte) 1);
                    smtMaterialPackages.add(smtMaterialPackage);
                }
                smtMaterialPackageMapper.insertList(smtMaterialPackages);
            }
            smtHtPackageSpecificationMapper.insertList(htList);
        }

        resultMap.put("操作成功总数",success);
        resultMap.put("操作失败行数",fail);
        return resultMap;
    }
}
