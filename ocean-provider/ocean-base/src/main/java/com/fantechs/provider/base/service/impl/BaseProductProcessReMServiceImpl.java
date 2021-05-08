package com.fantechs.provider.base.service.impl;


import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.basic.BaseProductMaterialReP;
import com.fantechs.common.base.general.entity.basic.BaseProductProcessReM;
import com.fantechs.common.base.general.entity.basic.history.BaseHtProductMaterialReP;
import com.fantechs.common.base.general.entity.basic.history.BaseHtProductProcessReM;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseProductMaterialReP;
import com.fantechs.common.base.general.entity.mes.pm.MesPmWorkOrderMaterialReP;
import com.fantechs.common.base.general.entity.mes.pm.history.MesPmHtWorkOrderMaterialReP;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.BaseHtProductMaterialRePMapper;
import com.fantechs.provider.base.mapper.BaseHtProductProcessReMMapper;
import com.fantechs.provider.base.mapper.BaseProductMaterialRePMapper;
import com.fantechs.provider.base.mapper.BaseProductProcessReMMapper;
import com.fantechs.provider.base.service.BaseProductProcessReMService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/04/28.
 */
@Service
public class BaseProductProcessReMServiceImpl extends BaseService<BaseProductProcessReM> implements BaseProductProcessReMService {

    @Resource
    private BaseProductProcessReMMapper baseProductProcessReMMapper;

    @Resource
    private BaseProductMaterialRePMapper baseProductMaterialRePMapper;

    @Resource
    private BaseHtProductProcessReMMapper baseHtProductProcessReMMapper;

    @Resource
    private BaseHtProductMaterialRePMapper baseHtProductMaterialRePMapper;


    @Override
    public List<BaseProductProcessReM> findList(Map<String, Object> map) {
        List<BaseProductProcessReM> baseProductProcessReMS = baseProductProcessReMMapper.findList(map);
        SearchBaseProductMaterialReP searchBaseProductMaterialReP = new SearchBaseProductMaterialReP();

        for (BaseProductProcessReM baseProductProcessReM : baseProductProcessReMS) {
            searchBaseProductMaterialReP.setProductProcessReMId(baseProductProcessReM.getProductProcessReMId());
            List<BaseProductMaterialReP> list = baseProductMaterialRePMapper.findList(ControllerUtil.dynamicConditionByEntity(searchBaseProductMaterialReP));
            if (StringUtils.isNotEmpty(list)){
                baseProductProcessReM.setList(list);
            }
        }
        return baseProductProcessReMS;
    }

    @Override
    public List<BaseHtProductProcessReM> findHtList(Map<String, Object> map) {
        List<BaseHtProductProcessReM> baseHtProductProcessReMS = baseHtProductProcessReMMapper.findHtList(map);
        SearchBaseProductMaterialReP searchBaseProductMaterialReP = new SearchBaseProductMaterialReP();

        for (BaseHtProductProcessReM baseHtProductProcessReM : baseHtProductProcessReMS) {
            searchBaseProductMaterialReP.setProductProcessReMId(baseHtProductProcessReM.getProductProcessReMId());
            List<BaseHtProductMaterialReP> htList = baseHtProductMaterialRePMapper.findHtList(ControllerUtil.dynamicConditionByEntity(searchBaseProductMaterialReP));
            if (StringUtils.isNotEmpty(htList)){
                baseHtProductProcessReM.setHtList(htList);
            }
        }
        return baseHtProductProcessReMS;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int save(BaseProductProcessReM baseProductProcessReM) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        //物料工序关系表
        Example example = new Example(BaseProductProcessReM.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("materialId", baseProductProcessReM.getMaterialId())
                .andEqualTo("processId", baseProductProcessReM.getProcessId());
        BaseProductProcessReM baseProductProcessReM1 = baseProductProcessReMMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(baseProductProcessReM1)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        baseProductProcessReM.setCreateUserId(user.getUserId());
        baseProductProcessReM.setCreateTime(new Date());
        baseProductProcessReM.setModifiedUserId(user.getUserId());
        baseProductProcessReM.setModifiedTime(new Date());
        baseProductProcessReM.setStatus(StringUtils.isEmpty(baseProductProcessReM.getStatus())?1:baseProductProcessReM.getStatus());
        baseProductProcessReM.setOrgId(user.getOrganizationId());
        int i = baseProductProcessReMMapper.insertUseGeneratedKeys(baseProductProcessReM);

        //履历
        BaseHtProductProcessReM baseHtProductProcessReM = new BaseHtProductProcessReM();
        BeanUtils.copyProperties(baseProductProcessReM, baseHtProductProcessReM);
        baseHtProductProcessReMMapper.insert(baseHtProductProcessReM);

        //工序物料清单表
        List<BaseProductMaterialReP> baseProductMaterialRePS = baseProductProcessReM.getList();
        if(StringUtils.isNotEmpty(baseProductMaterialRePS)){
            baseProductMaterialRePMapper.insertList(baseProductMaterialRePS);
        }

        //履历
        BaseHtProductMaterialReP baseHtProductMaterialReP = new BaseHtProductMaterialReP();
        List<BaseHtProductMaterialReP> htList = new ArrayList<>();
        for (BaseProductMaterialReP baseProductMaterialReP:baseProductMaterialRePS) {
            BeanUtils.copyProperties(baseProductMaterialReP,baseHtProductMaterialReP);
            htList.add(baseHtProductMaterialReP);
        }
        baseHtProductMaterialRePMapper.insert(baseHtProductMaterialReP);

        return i;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int update(BaseProductProcessReM baseProductProcessReM) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        //物料工序关系表
        Example example = new Example(BaseProductProcessReM.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("materialId", baseProductProcessReM.getMaterialId())
                .andEqualTo("processId", baseProductProcessReM.getProcessId())
                .andNotEqualTo("product_process_re_m_id",baseProductProcessReM.getProductProcessReMId());
        BaseProductProcessReM baseProductProcessReM1 = baseProductProcessReMMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(baseProductProcessReM1)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        baseProductProcessReM.setModifiedUserId(user.getUserId());
        baseProductProcessReM.setModifiedTime(new Date());
        baseProductProcessReM.setOrgId(user.getOrganizationId());
        int i=baseProductProcessReMMapper.updateByPrimaryKeySelective(baseProductProcessReM);

        BaseHtProductProcessReM baseHtProductProcessReM = new BaseHtProductProcessReM();
        BeanUtils.copyProperties(baseProductProcessReM, baseHtProductProcessReM);
        baseHtProductProcessReMMapper.insert(baseHtProductProcessReM);

        //工序物料清单表
        Example example1 = new Example(BaseProductMaterialReP.class);
        example1.createCriteria().andEqualTo("productProcessReMId",baseProductProcessReM.getProductProcessReMId());
        baseProductMaterialRePMapper.deleteByExample(example1);

        List<BaseProductMaterialReP> baseProductMaterialRePS = baseProductProcessReM.getList();
        if(StringUtils.isNotEmpty(baseProductMaterialRePS)){
            baseProductMaterialRePMapper.insertList(baseProductMaterialRePS);
        }

        //履历
        BaseHtProductMaterialReP baseHtProductMaterialReP = new BaseHtProductMaterialReP();
        List<BaseHtProductMaterialReP> htList = new ArrayList<>();
        for (BaseProductMaterialReP baseProductMaterialReP:baseProductMaterialRePS) {
            BeanUtils.copyProperties(baseProductMaterialReP,baseHtProductMaterialReP);
            htList.add(baseHtProductMaterialReP);
        }
        baseHtProductMaterialRePMapper.insert(baseHtProductMaterialReP);

        return i;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        List<BaseHtProductProcessReM> list = new ArrayList<>();
        String[] idArry = ids.split(",");
        for (String id : idArry) {
            BaseProductProcessReM baseProductProcessReM = baseProductProcessReMMapper.selectByPrimaryKey(id);
            if(StringUtils.isEmpty(baseProductProcessReM)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

            Example example1 = new Example(BaseProductMaterialReP.class);
            example1.createCriteria().andEqualTo("productProcessReMId",baseProductProcessReM.getProductProcessReMId());
            baseProductMaterialRePMapper.deleteByExample(example1);

            BaseHtProductProcessReM baseHtProductProcessReM = new BaseHtProductProcessReM();
            BeanUtils.copyProperties(baseProductProcessReM, baseHtProductProcessReM);
            list.add(baseHtProductProcessReM);
        }

        baseHtProductProcessReMMapper.insertList(list);

        return baseProductProcessReMMapper.deleteByIds(ids);
    }


}
