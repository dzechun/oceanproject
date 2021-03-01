package com.fantechs.provider.imes.basic.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.dto.basic.SmtMaterialPackageDto;
import com.fantechs.common.base.dto.basic.SmtPackageSpecificationDto;
import com.fantechs.common.base.dto.basic.SmtPackingUnitDto;
import com.fantechs.common.base.entity.basic.*;
import com.fantechs.common.base.entity.basic.history.SmtHtFactory;
import com.fantechs.common.base.entity.basic.history.SmtHtPackageSpecification;
import com.fantechs.common.base.entity.basic.history.SmtHtPackingUnit;
import com.fantechs.common.base.entity.basic.search.SearchSmtMaterialPackage;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import javax.naming.NamingEnumeration;
import javax.validation.constraints.NotNull;
import java.util.*;

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
    private PMFeignApi pmFeignApi;
    @Resource
    private SmtMaterialMapper smtMaterialMapper;
    @Resource
    private SmtPackingUnitMapper smtPackingUnitMapper;
    @Resource
    private SmtProcessMapper smtProcessMapper;
    @Resource
    private SmtMaterialPackageMapper smtMaterialPackageMapper;


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
        for (SmtMaterialPackage smtMaterialPackage : smtMaterialPackages) {
            Example example1 = new Example(SmtMaterialPackage.class);
            Example.Criteria criteria1 = example1.createCriteria();
            criteria1.andEqualTo("materialId",smtMaterialPackage.getMaterialId())
                    .andEqualTo("processId",smtMaterialPackage.getProcessId());
            List<SmtMaterialPackage> smtMaterialPackages1 = smtMaterialPackageMapper.selectByExample(example1);
            if (StringUtils.isNotEmpty(smtMaterialPackages1)){
                throw new BizErrorException("物料在工序下的包装规格已存在");
            }
            smtMaterialPackage.setPackageSpecificationId(smtPackageSpecification.getPackageSpecificationId());
        }
        smtMaterialPackageMapper.insertList(smtMaterialPackages);

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
        for (SmtMaterialPackage smtMaterialPackage : smtMaterialPackages) {
            Example example2 = new Example(SmtMaterialPackage.class);
            Example.Criteria criteria2 = example1.createCriteria();
            criteria2.andEqualTo("materialId",smtMaterialPackage.getMaterialId())
                    .andEqualTo("processId",smtMaterialPackage.getProcessId());
            List<SmtMaterialPackage> smtMaterialPackages1 = smtMaterialPackageMapper.selectByExample(example2);
            if (StringUtils.isNotEmpty(smtMaterialPackages1)){
                throw new BizErrorException("物料在工序下的包装规格已存在");
            }
            smtMaterialPackage.setPackageSpecificationId(smtHtPackageSpecification.getPackageSpecificationId());
        }
        smtMaterialPackageMapper.insertList(smtMaterialPackages);
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
    public Map<String, Object> importExcel(List<SmtPackageSpecificationDto> smtPackageSpecificationDtos) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        Map<String, Object> resutlMap = new HashMap<>();  //封装操作结果
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数
        LinkedList<SmtPackageSpecification> list = new LinkedList<>();
        LinkedList<SmtHtPackageSpecification> htList = new LinkedList<>();
        for (int i = 0; i < smtPackageSpecificationDtos.size(); i++) {
            SmtPackageSpecificationDto smtPackageSpecificationDto = smtPackageSpecificationDtos.get(i);
            String packageSpecificationCode = smtPackageSpecificationDto.getPackageSpecificationCode();
            String packageSpecificationName = smtPackageSpecificationDto.getPackageSpecificationName();
            if (StringUtils.isEmpty(
                    packageSpecificationCode,packageSpecificationName
            )){
                fail.add(i+3);
                continue;
            }

            //判断编码是否重复
            Example example = new Example(SmtSignature.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("packageSpecificationCode",smtPackageSpecificationDto.getPackageSpecificationCode());
            if (StringUtils.isNotEmpty(smtPackageSpecificationMapper.selectOneByExample(example))){
                fail.add(i+3);
                continue;
            }


            SmtPackageSpecification smtPackageSpecification = new SmtPackageSpecification();
            BeanUtils.copyProperties(smtPackageSpecificationDto,smtPackageSpecification);
            smtPackageSpecification.setCreateTime(new Date());
            smtPackageSpecification.setCreateUserId(currentUser.getUserId());
            smtPackageSpecification.setModifiedTime(new Date());
            smtPackageSpecification.setModifiedUserId(currentUser.getUserId());
            smtPackageSpecification.setStatus((byte) 1);
            list.add(smtPackageSpecification);
        }

        if (StringUtils.isNotEmpty(list)){
            success = smtPackageSpecificationMapper.insertList(list);
        }

        for (SmtPackageSpecification smtPackageSpecification : list) {
            SmtHtPackageSpecification smtHtPackageSpecification = new SmtHtPackageSpecification();
            BeanUtils.copyProperties(smtPackageSpecification,smtHtPackageSpecification);
            htList.add(smtHtPackageSpecification);
        }

        if (StringUtils.isNotEmpty(htList)){
            smtHtPackageSpecificationMapper.insertList(htList);
        }
        resutlMap.put("操作成功总数",success);
        resutlMap.put("操作失败行数",fail);
        return resutlMap;
    }
}
