package com.fantechs.provider.imes.basic.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.dto.basic.SmtPackingUnitDto;
import com.fantechs.common.base.dto.basic.imports.SmtPackingUnitImport;
import com.fantechs.common.base.entity.basic.SmtFactory;
import com.fantechs.common.base.entity.basic.SmtPackingUnit;
import com.fantechs.common.base.entity.basic.SmtSignature;
import com.fantechs.common.base.entity.basic.history.SmtHtPackingUnit;
import com.fantechs.common.base.entity.basic.history.SmtHtSignature;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.imes.basic.mapper.SmtHtPackingUnitMapper;
import com.fantechs.provider.imes.basic.mapper.SmtPackingUnitMapper;
import com.fantechs.provider.imes.basic.service.SmtPackingUnitService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.*;

/**
 * Created by leifengzhi on 2020/11/03.
 */
@Service
public class SmtPackingUnitServiceImpl extends BaseService<SmtPackingUnit> implements SmtPackingUnitService {

    @Resource
    private SmtPackingUnitMapper smtPackingUnitMapper;
    @Resource
    private SmtHtPackingUnitMapper smtHtPackingUnitMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(SmtPackingUnit smtPackingUnit) {
        SysUser currentUserInfo = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(currentUserInfo)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(SmtPackingUnit.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("packingUnitName", smtPackingUnit.getPackingUnitName());
        List<SmtPackingUnit> smtPackingUnits = smtPackingUnitMapper.selectByExample(example);
        if (StringUtils.isNotEmpty(smtPackingUnits)) {
            throw new BizErrorException("包装单位名称重复");
        }
        smtPackingUnit.setCreateTime(new Date());
        smtPackingUnit.setCreateUserId(currentUserInfo.getUserId());
        smtPackingUnit.setModifiedTime(new Date());
        smtPackingUnit.setModifiedUserId(currentUserInfo.getUserId());
        int i = smtPackingUnitMapper.insertUseGeneratedKeys(smtPackingUnit);

        SmtHtPackingUnit smtHtPackingUnit = new SmtHtPackingUnit();
        BeanUtils.copyProperties(smtPackingUnit, smtHtPackingUnit);
        smtHtPackingUnitMapper.insert(smtHtPackingUnit);

        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(SmtPackingUnit smtPackingUnit) {
        SysUser currentUserInfo = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(currentUserInfo)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(SmtPackingUnit.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("packingUnitName", smtPackingUnit.getPackingUnitName())
                .andNotEqualTo("packingUnitId", smtPackingUnit.getPackingUnitId());
        List<SmtPackingUnit> smtPackingUnits = smtPackingUnitMapper.selectByExample(example);
        if (StringUtils.isNotEmpty(smtPackingUnits)) {
            throw new BizErrorException("包装单位名称重复");
        }

        smtPackingUnit.setModifiedTime(new Date());
        smtPackingUnit.setModifiedUserId(currentUserInfo.getUserId());
        int i = smtPackingUnitMapper.updateByPrimaryKeySelective(smtPackingUnit);

        SmtHtPackingUnit smtHtPackingUnit = new SmtHtPackingUnit();
        BeanUtils.copyProperties(smtPackingUnit, smtHtPackingUnit);
        smtHtPackingUnitMapper.insert(smtHtPackingUnit);

        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(String ids) {
        SysUser currentUserInfo = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(currentUserInfo)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        LinkedList<SmtHtPackingUnit> smtHtPackingUnits = new LinkedList<>();
        String[] idsArr = ids.split(",");
        for (String id : idsArr) {
            SmtPackingUnit smtPackingUnit = smtPackingUnitMapper.selectByPrimaryKey(id);
            if (StringUtils.isEmpty(smtPackingUnit)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

            SmtHtPackingUnit smtHtPackingUnit = new SmtHtPackingUnit();
            BeanUtils.copyProperties(smtPackingUnit, smtHtPackingUnit);
            smtHtPackingUnit.setModifiedUserId(currentUserInfo.getUserId());
            smtHtPackingUnit.setModifiedTime(new Date());
            smtHtPackingUnits.add(smtHtPackingUnit);
        }
        smtHtPackingUnitMapper.insertList(smtHtPackingUnits);
        return smtPackingUnitMapper.deleteByIds(ids);
    }

    @Override
    public List<SmtPackingUnitDto> findList(Map<String, Object> map) {
        return smtPackingUnitMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importExcel(List<SmtPackingUnitImport> smtPackingUnitImports) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        Map<String, Object> resultMap = new HashMap<>();  //封装操作结果
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数
        LinkedList<SmtPackingUnit> list = new LinkedList<>();
        LinkedList<SmtHtPackingUnit> htList = new LinkedList<>();
        LinkedList<SmtPackingUnitImport> packingUnitImports = new LinkedList<>();
        for (int i = 0; i < smtPackingUnitImports.size(); i++) {
            SmtPackingUnitImport smtPackingUnitImport = smtPackingUnitImports.get(i);
            String packingUnitName = smtPackingUnitImport.getPackingUnitName();
            if (StringUtils.isEmpty(
                    packingUnitName
            )){
                fail.add(i+4);
                continue;
            }

            //判断包装单位是否存在
            Example example = new Example(SmtPackingUnit.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("packingUnitName",packingUnitName);
            SmtPackingUnit smtPackingUnit1 = smtPackingUnitMapper.selectOneByExample(example);
            if (StringUtils.isNotEmpty(smtPackingUnit1)){
                fail.add(i+4);
                continue;
            }

            packingUnitImports.add(smtPackingUnitImport);
        }

        if (StringUtils.isNotEmpty(packingUnitImports)){

            for (SmtPackingUnitImport packingUnitImport : packingUnitImports) {
                SmtPackingUnit smtPackingUnit = new SmtPackingUnit();
                BeanUtils.copyProperties(packingUnitImport,smtPackingUnit);
                smtPackingUnit.setCreateTime(new Date());
                smtPackingUnit.setCreateUserId(currentUser.getUserId());
                smtPackingUnit.setModifiedTime(new Date());
                smtPackingUnit.setModifiedUserId(currentUser.getUserId());
                if (StringUtils.isEmpty(smtPackingUnit.getStatus())){
                    smtPackingUnit.setStatus((byte) 1);
                }
                list.add(smtPackingUnit);
            }
            success += smtPackingUnitMapper.insertList(list);

            for (SmtPackingUnit smtPackingUnit : list) {
                SmtHtPackingUnit smtHtPackingUnit = new SmtHtPackingUnit();
                BeanUtils.copyProperties(smtPackingUnit,smtHtPackingUnit);
                htList.add(smtHtPackingUnit);
            }
            smtHtPackingUnitMapper.insertList(htList);
        }

        resultMap.put("操作成功总数",success);
        resultMap.put("操作失败行数",fail);
        return resultMap;
    }
}
