package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseBadnessPhenotypeDto;
import com.fantechs.common.base.general.dto.basic.imports.BaseBadnessPhenotypeImport;
import com.fantechs.common.base.general.entity.basic.BaseBadnessPhenotype;
import com.fantechs.common.base.general.entity.basic.history.BaseHtBadnessPhenotype;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.BaseBadnessPhenotypeMapper;
import com.fantechs.provider.base.mapper.BaseHtBadnessPhenotypeMapper;
import com.fantechs.provider.base.service.BaseBadnessPhenotypeService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;

/**
 *
 * Created by leifengzhi on 2021/04/07.
 */
@Service
public class BaseBadnessPhenotypeServiceImpl extends BaseService<BaseBadnessPhenotype> implements BaseBadnessPhenotypeService {

    @Resource
    private BaseBadnessPhenotypeMapper baseBadnessPhenotypeMapper;

    @Resource
    private BaseHtBadnessPhenotypeMapper baseHtBadnessPhenotypeMapper;

    @Override
    public List<BaseBadnessPhenotypeDto> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId",user.getOrganizationId());
        return baseBadnessPhenotypeMapper.findList(map);
    }

    @Override
    public int save(BaseBadnessPhenotype record) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        this.codeIfRepeat(record);

        record.setCreateTime(new Date());
        record.setCreateUserId(user.getUserId());
        record.setModifiedTime(new Date());
        record.setModifiedUserId(user.getUserId());
        record.setOrgId(user.getOrganizationId());

        int i = baseBadnessPhenotypeMapper.insertUseGeneratedKeys(record);

        BaseHtBadnessPhenotype baseHtBadnessPhenotype = new BaseHtBadnessPhenotype();
        BeanUtils.copyProperties(record,baseHtBadnessPhenotype);
        baseHtBadnessPhenotypeMapper.insertSelective(baseHtBadnessPhenotype);

        return i;
    }

    @Override
    public int update(BaseBadnessPhenotype entity) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        this.codeIfRepeat(entity);

        entity.setModifiedUserId(user.getUserId());
        entity.setModifiedTime(new Date());
        entity.setOrgId(user.getOrganizationId());
        BaseHtBadnessPhenotype baseHtBadnessPhenotype = new BaseHtBadnessPhenotype();
        BeanUtils.copyProperties(entity,baseHtBadnessPhenotype);
        baseHtBadnessPhenotypeMapper.insertSelective(baseHtBadnessPhenotype);

        return baseBadnessPhenotypeMapper.updateByPrimaryKeySelective(entity);
    }

    @Override
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        List<BaseHtBadnessPhenotype> list = new ArrayList<>();
        String[] idsArr  = ids.split(",");
        for (String id : idsArr) {
            BaseBadnessPhenotype baseBadnessPhenotype = baseBadnessPhenotypeMapper.selectByPrimaryKey(id);
            if (StringUtils.isEmpty(baseBadnessPhenotype)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

            BaseHtBadnessPhenotype baseHtBadnessPhenotype = new BaseHtBadnessPhenotype();
            BeanUtils.copyProperties(baseBadnessPhenotype,baseHtBadnessPhenotype);
            list.add(baseHtBadnessPhenotype);
        }

        baseHtBadnessPhenotypeMapper.insertList(list);
        return baseBadnessPhenotypeMapper.deleteByIds(ids);
    }

    private void codeIfRepeat(BaseBadnessPhenotype entity){
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        Example example = new Example(BaseBadnessPhenotype.class);
        Example.Criteria criteria = example.createCriteria();
        //判断编码是否重复
        criteria.andEqualTo("orgId", user.getOrganizationId());
        criteria.andEqualTo("badnessPhenotypeCode",entity.getBadnessPhenotypeCode());
        if (StringUtils.isNotEmpty(entity.getBadnessPhenotypeId())){
            criteria.andNotEqualTo("badnessPhenotypeId",entity.getBadnessPhenotypeId());
        }
        BaseBadnessPhenotype baseBadnessPhenotype = baseBadnessPhenotypeMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(baseBadnessPhenotype)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importExcel(List<BaseBadnessPhenotypeImport> baseBadnessPhenotypeImports) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();

        Map<String, Object> resultMap = new HashMap<>();  //封装操作结果
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数
        LinkedList<BaseBadnessPhenotype> list = new LinkedList<>();
        LinkedList<BaseHtBadnessPhenotype> htList = new LinkedList<>();
        LinkedList<BaseBadnessPhenotypeImport> badnessPhenotypeImports = new LinkedList<>();

        for (int i = 0; i < baseBadnessPhenotypeImports.size(); i++) {
            BaseBadnessPhenotypeImport baseBadnessPhenotypeImport = baseBadnessPhenotypeImports.get(i);
            String badnessPhenotypeCode = baseBadnessPhenotypeImport.getBadnessPhenotypeCode();


            if (StringUtils.isEmpty(
                    badnessPhenotypeCode
            )){
                fail.add(i+4);
                continue;
            }

            //判断编码是否重复
            Example example = new Example(BaseBadnessPhenotype.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("orgId", currentUser.getOrganizationId());
            criteria.andEqualTo("badnessPhenotypeCode",badnessPhenotypeCode);
            if (StringUtils.isNotEmpty(baseBadnessPhenotypeMapper.selectOneByExample(example))){
                fail.add(i+4);
                continue;
            }

            //判断集合中是否存在该条数据
            boolean tag = false;
            if (StringUtils.isNotEmpty(badnessPhenotypeImports)){
                for (BaseBadnessPhenotypeImport badnessPhenotypeImport: badnessPhenotypeImports) {
                    if (badnessPhenotypeCode.equals(badnessPhenotypeImport.getBadnessPhenotypeCode())){
                        tag = true;
                        break;
                    }
                }
            }
            if (tag){
                fail.add(i+4);
                continue;
            }

            badnessPhenotypeImports.add(baseBadnessPhenotypeImport);
        }

        if (StringUtils.isNotEmpty(badnessPhenotypeImports)){

            for (BaseBadnessPhenotypeImport baseBadnessPhenotypeImport : badnessPhenotypeImports) {
                BaseBadnessPhenotype baseBadnessPhenotype = new BaseBadnessPhenotype();
                BeanUtils.copyProperties(baseBadnessPhenotypeImport,baseBadnessPhenotype);
                baseBadnessPhenotype.setCreateTime(new Date());
                baseBadnessPhenotype.setCreateUserId(currentUser.getUserId());
                baseBadnessPhenotype.setModifiedTime(new Date());
                baseBadnessPhenotype.setModifiedUserId(currentUser.getUserId());
                baseBadnessPhenotype.setOrgId(currentUser.getOrganizationId());
                baseBadnessPhenotype.setStatus((byte)1);
                list.add(baseBadnessPhenotype);
            }
            success = baseBadnessPhenotypeMapper.insertList(list);

            if(StringUtils.isNotEmpty(list)){
                for (BaseBadnessPhenotype baseBadnessPhenotype : list) {
                    BaseHtBadnessPhenotype baseHtBadnessPhenotype = new BaseHtBadnessPhenotype();
                    BeanUtils.copyProperties(baseBadnessPhenotype, baseHtBadnessPhenotype);
                    htList.add(baseHtBadnessPhenotype);
                }
                baseHtBadnessPhenotypeMapper.insertList(htList);
            }
        }

        resultMap.put("操作成功总数",success);
        resultMap.put("操作失败行数",fail);
        return resultMap;
    }

    @Override
    public int saveByApi(List<BaseBadnessPhenotype> baseBadnessPhenotypes) {
        List<BaseBadnessPhenotype> ins = new ArrayList<>();
        for(BaseBadnessPhenotype baseBadnessPhenotype : baseBadnessPhenotypes) {
            Example example = new Example(BaseBadnessPhenotype.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("badnessPhenotypeCode", baseBadnessPhenotype.getBadnessPhenotypeCode());
            criteria.andEqualTo("orgId", baseBadnessPhenotype.getOrgId());
            // baseBadnessCauseMapper.deleteByExample(example);
            BaseBadnessPhenotype phenotype = baseBadnessPhenotypeMapper.selectOneByExample(example);
            if (StringUtils.isEmpty(phenotype)) {
                baseBadnessPhenotype.setCreateTime(new Date());
              //  i = baseBadnessPhenotypeMapper.insertSelective(baseBadnessPhenotype);
                ins.add(baseBadnessPhenotype);
            } else {
                baseBadnessPhenotype.setBadnessPhenotypeId(phenotype.getBadnessPhenotypeId());
                baseBadnessPhenotypeMapper.updateByPrimaryKeySelective(baseBadnessPhenotype);
            }
        }
        int i = baseBadnessPhenotypeMapper.insertList(ins);
        return  i;
    }
}
