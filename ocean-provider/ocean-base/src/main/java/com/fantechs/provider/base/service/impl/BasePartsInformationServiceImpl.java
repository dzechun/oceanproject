package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BasePartsInformationDto;
import com.fantechs.common.base.general.dto.basic.imports.BasePartsInformationImport;
import com.fantechs.common.base.general.entity.basic.BasePartsInformation;
import com.fantechs.common.base.general.entity.basic.history.BaseHtPartsInformation;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.BaseHtPartsInformationMapper;
import com.fantechs.provider.base.mapper.BasePartsInformationMapper;
import com.fantechs.provider.base.service.BasePartsInformationService;
import cz.mallat.uasparser.UASparser;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;

/**
 *
 * Created by leifengzhi on 2021/01/14.
 */
@Service
public class BasePartsInformationServiceImpl  extends BaseService<BasePartsInformation> implements BasePartsInformationService {

    @Resource
    private BasePartsInformationMapper basePartsInformationMapper;
    @Resource
    private BaseHtPartsInformationMapper baseHtPartsInformationMapper;

    @Override
    public List<BasePartsInformationDto> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId", user.getOrganizationId());
        return basePartsInformationMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(BasePartsInformation basePartsInformation) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        basePartsInformation.setCreateTime(new Date());
        basePartsInformation.setCreateUserId(user.getUserId());
        basePartsInformation.setModifiedTime(new Date());
        basePartsInformation.setModifiedUserId(user.getUserId());
        basePartsInformation.setStatus(StringUtils.isEmpty(basePartsInformation.getStatus())?1:basePartsInformation.getStatus());
        basePartsInformation.setOrganizationId(user.getOrganizationId());
        basePartsInformation.setPartsInformationCode(CodeUtils.getId("ZCB"));
        int i = basePartsInformationMapper.insertUseGeneratedKeys(basePartsInformation);

        BaseHtPartsInformation baseHtPartsInformation = new BaseHtPartsInformation();
        BeanUtils.copyProperties(basePartsInformation,baseHtPartsInformation);
        baseHtPartsInformationMapper.insertSelective(baseHtPartsInformation);

        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(BasePartsInformation basePartsInformation) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        basePartsInformation.setModifiedTime(new Date());
        basePartsInformation.setModifiedUserId(user.getUserId());
        basePartsInformation.setOrganizationId(user.getOrganizationId());

        BaseHtPartsInformation baseHtPartsInformation = new BaseHtPartsInformation();
        BeanUtils.copyProperties(basePartsInformation,baseHtPartsInformation);
        baseHtPartsInformationMapper.insertSelective(baseHtPartsInformation);

        return basePartsInformationMapper.updateByPrimaryKeySelective(basePartsInformation);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        List<BaseHtPartsInformation> baseHtPartsInformations = new ArrayList<>();
        String[] idsArr  = ids.split(",");
        for (String id : idsArr) {
            BasePartsInformation basePartsInformation = basePartsInformationMapper.selectByPrimaryKey(id);
            if (StringUtils.isEmpty(basePartsInformation)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

            BaseHtPartsInformation qmsHtInspectionType = new BaseHtPartsInformation();
            BeanUtils.copyProperties(basePartsInformation,qmsHtInspectionType);
            baseHtPartsInformations.add(qmsHtInspectionType);
        }

        baseHtPartsInformationMapper.insertList(baseHtPartsInformations);

        return basePartsInformationMapper.deleteByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importExcel(List<BasePartsInformationImport> basePartsInformationImports) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        Map<String, Object> resutlMap = new HashMap<>();  //封装操作结果
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数
        LinkedList<BasePartsInformation> list = new LinkedList<>();
        LinkedList<BaseHtPartsInformation> htList = new LinkedList<>();
        LinkedList<BasePartsInformationImport> partsInformationImports = new LinkedList<>();
        for (int i = 0; i < basePartsInformationImports.size(); i++) {
            BasePartsInformationImport basePartsInformationImport = basePartsInformationImports.get(i);
            String partsInformationCode = basePartsInformationImport.getPartsInformationCode();
            String partsInformationName = basePartsInformationImport.getPartsInformationName();

            if (StringUtils.isEmpty(
                    partsInformationCode,partsInformationName
            )){
                fail.add(i+4);
                continue;
            }

            //判断编码是否重复
            Example example = new Example(BasePartsInformation.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("partsInformationCode",partsInformationCode);
            if (StringUtils.isNotEmpty(basePartsInformationMapper.selectOneByExample(example))){
                fail.add(i+4);
                continue;
            }
            //判断集合中是否存在重复数据
            boolean tag = false;
            if (StringUtils.isNotEmpty(partsInformationImports)){
                for (BasePartsInformationImport partsInformationImport : partsInformationImports) {
                    if (partsInformationImport.getPartsInformationCode().equals(partsInformationCode)){
                        tag = true;
                    }
                }
            }
            if (tag){
                fail.add(i+4);
                continue;
            }

            partsInformationImports.add(basePartsInformationImport);
        }
        if (StringUtils.isNotEmpty(partsInformationImports)){
            for (BasePartsInformationImport partsInformationImport : partsInformationImports) {
                BasePartsInformation basePartsInformation = new BasePartsInformation();
                BeanUtils.copyProperties(partsInformationImport,basePartsInformation);
                basePartsInformation.setCreateTime(new Date());
                basePartsInformation.setCreateUserId(currentUser.getUserId());
                basePartsInformation.setModifiedTime(new Date());
                basePartsInformation.setModifiedUserId(currentUser.getUserId());
                basePartsInformation.setOrganizationId(currentUser.getOrganizationId());
                list.add(basePartsInformation);
            }
            success = basePartsInformationMapper.insertList(list);

            for (BasePartsInformation basePartsInformation : list) {
                BaseHtPartsInformation baseHtPartsInformation = new BaseHtPartsInformation();
                BeanUtils.copyProperties(basePartsInformation,baseHtPartsInformation);
                htList.add(baseHtPartsInformation);
            }
            baseHtPartsInformationMapper.insertList(htList);
        }

        resutlMap.put("操作成功总数",success);
        resutlMap.put("操作失败行数",fail);
        return resutlMap;
    }
}
