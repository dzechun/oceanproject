package com.fantechs.provider.base.service.impl;


import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.basic.BaseProductProcessReM;
import com.fantechs.common.base.general.entity.basic.history.BaseHtProductProcessReM;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseProductProcessReM;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.BaseHtProductProcessReMMapper;
import com.fantechs.provider.base.mapper.BaseProductProcessReMMapper;
import com.fantechs.provider.base.service.BaseProductProcessReMService;
import com.fantechs.provider.base.vo.BaseProductProcessReMVo;
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
    private BaseHtProductProcessReMMapper baseHtProductProcessReMMapper;


    @Override
    public List<BaseProductProcessReMVo> findList(Map<String, Object> map) {
        List<BaseProductProcessReMVo> baseProductProcessReMVos = baseProductProcessReMMapper.findMaterialList(map);
        SearchBaseProductProcessReM searchBaseProductProcessReM = new SearchBaseProductProcessReM();

        for (BaseProductProcessReMVo baseProductProcessReMVo : baseProductProcessReMVos) {
            searchBaseProductProcessReM.setMaterialId(baseProductProcessReMVo.getMaterialId());
            List<BaseProductProcessReM> baseProductProcessReMS = baseProductProcessReMMapper.findList(ControllerUtil.dynamicConditionByEntity(searchBaseProductProcessReM));
            if (StringUtils.isNotEmpty(baseProductProcessReMS)){
                baseProductProcessReMVo.setBaseProductProcessReMS(baseProductProcessReMS);
            }
        }
        return baseProductProcessReMVos;
    }

    @Override
    public List<BaseProductProcessReMVo> findHtList(Map<String, Object> map) {
        List<BaseProductProcessReMVo> baseProductProcessReMVos = baseHtProductProcessReMMapper.findHtMaterialList(map);
        SearchBaseProductProcessReM searchBaseProductProcessReM = new SearchBaseProductProcessReM();

        for (BaseProductProcessReMVo baseProductProcessReMVo : baseProductProcessReMVos) {
            searchBaseProductProcessReM.setMaterialId(baseProductProcessReMVo.getMaterialId());
            List<BaseProductProcessReM> baseProductProcessReMS = baseHtProductProcessReMMapper.findHtList(ControllerUtil.dynamicConditionByEntity(searchBaseProductProcessReM));
            if (StringUtils.isNotEmpty(baseProductProcessReMS)){
                baseProductProcessReMVo.setBaseProductProcessReMS(baseProductProcessReMS);
            }
        }
        return baseProductProcessReMVos;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int save(BaseProductProcessReM baseProductProcessReM) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

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

        BaseHtProductProcessReM baseHtProductProcessReM = new BaseHtProductProcessReM();
        BeanUtils.copyProperties(baseProductProcessReM, baseHtProductProcessReM);
        baseHtProductProcessReMMapper.insert(baseHtProductProcessReM);

        return i;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int update(BaseProductProcessReM baseProductProcessReM) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

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
            BaseHtProductProcessReM baseHtProductProcessReM = new BaseHtProductProcessReM();
            BeanUtils.copyProperties(baseProductProcessReM, baseHtProductProcessReM);
            list.add(baseHtProductProcessReM);
        }

        baseHtProductProcessReMMapper.insertList(list);

        return baseProductProcessReMMapper.deleteByIds(ids);
    }


}
