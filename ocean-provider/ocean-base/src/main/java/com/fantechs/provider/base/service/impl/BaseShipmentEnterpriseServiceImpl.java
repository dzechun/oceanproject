package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseShipmentEnterpriseDto;
import com.fantechs.common.base.general.dto.basic.imports.BaseShipmentEnterpriseImport;
import com.fantechs.common.base.general.entity.basic.BaseShipmentEnterprise;
import com.fantechs.common.base.general.entity.basic.history.BaseHtShipmentEnterprise;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.BaseHtShipmentEnterpriseMapper;
import com.fantechs.provider.base.mapper.BaseShipmentEnterpriseMapper;
import com.fantechs.provider.base.service.BaseShipmentEnterpriseService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by leifengzhi on 2020/12/16.
 */
@Service
public class BaseShipmentEnterpriseServiceImpl extends BaseService<BaseShipmentEnterprise> implements BaseShipmentEnterpriseService {

    @Resource
    private BaseShipmentEnterpriseMapper baseShipmentEnterpriseMapper;
    @Resource
    private BaseHtShipmentEnterpriseMapper baseHtShipmentEnterpriseMapper;

    @Override
    public int save(BaseShipmentEnterprise baseShipmentEnterprise) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        Example example = new Example(BaseShipmentEnterprise.class);
        Example.Criteria criteria1 = example.createCriteria();
        //判断编码是否重复
        criteria1.andEqualTo("organizationId", user.getOrganizationId());
        criteria1.andEqualTo("shipmentEnterpriseCode",baseShipmentEnterprise.getShipmentEnterpriseCode());
        BaseShipmentEnterprise baseShipmentEnterprise1 = baseShipmentEnterpriseMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(baseShipmentEnterprise1)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        Example example1 = new Example(BaseShipmentEnterprise.class);
        Example.Criteria criteria2 = example1.createCriteria();
        //判断名称是否重复
        criteria2.andEqualTo("organizationId", user.getOrganizationId());
        criteria2.andEqualTo("shipmentEnterpriseName",baseShipmentEnterprise.getShipmentEnterpriseCode());
        BaseShipmentEnterprise baseShipmentEnterprise2 = baseShipmentEnterpriseMapper.selectOneByExample(example1);
        if (StringUtils.isNotEmpty(baseShipmentEnterprise2)){
            throw new BizErrorException("物流商名称已经存在");
        }

        baseShipmentEnterprise.setCreateTime(new Date());
        baseShipmentEnterprise.setCreateUserId(user.getUserId());
        baseShipmentEnterprise.setModifiedTime(new Date());
        baseShipmentEnterprise.setModifiedUserId(user.getUserId());
        baseShipmentEnterprise.setStatus(StringUtils.isEmpty(baseShipmentEnterprise.getStatus())?1:baseShipmentEnterprise.getStatus());
        baseShipmentEnterprise.setOrganizationId(user.getOrganizationId());
        int i = baseShipmentEnterpriseMapper.insertUseGeneratedKeys(baseShipmentEnterprise);

        BaseHtShipmentEnterprise baseHtShipmentEnterprise = new BaseHtShipmentEnterprise();
        BeanUtils.copyProperties(baseShipmentEnterprise,baseHtShipmentEnterprise);
        baseHtShipmentEnterpriseMapper.insertSelective(baseHtShipmentEnterprise);

        return i;
    }

    @Override
    public int update(BaseShipmentEnterprise baseShipmentEnterprise) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        Example example = new Example(BaseShipmentEnterprise.class);
        Example.Criteria criteria1 = example.createCriteria();
        //判断编码是否重复
        criteria1.andEqualTo("organizationId", user.getOrganizationId());
        criteria1.andEqualTo("shipmentEnterpriseCode",baseShipmentEnterprise.getShipmentEnterpriseCode())
                .andNotEqualTo("shipmentEnterpriseId",baseShipmentEnterprise.getShipmentEnterpriseId());
        BaseShipmentEnterprise baseShipmentEnterprise1 = baseShipmentEnterpriseMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(baseShipmentEnterprise1)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        Example example1 = new Example(BaseShipmentEnterprise.class);
        Example.Criteria criteria2 = example1.createCriteria();
        //判断名称是否重复
        criteria2.andEqualTo("organizationId", user.getOrganizationId());
        criteria2.andEqualTo("shipmentEnterpriseName",baseShipmentEnterprise.getShipmentEnterpriseName())
                .andNotEqualTo("shipmentEnterpriseId",baseShipmentEnterprise.getShipmentEnterpriseId());
        BaseShipmentEnterprise baseShipmentEnterprise2 = baseShipmentEnterpriseMapper.selectOneByExample(example1);
        if (StringUtils.isNotEmpty(baseShipmentEnterprise2)){
            throw new BizErrorException("物流商名称已存在");
        }

        baseShipmentEnterprise.setModifiedUserId(user.getUserId());
        baseShipmentEnterprise.setModifiedTime(new Date());
        baseShipmentEnterprise.setOrganizationId(user.getOrganizationId());

        BaseHtShipmentEnterprise baseHtShipmentEnterprise = new BaseHtShipmentEnterprise();
        BeanUtils.copyProperties(baseShipmentEnterprise,baseHtShipmentEnterprise);
        baseHtShipmentEnterpriseMapper.insertSelective(baseHtShipmentEnterprise);

        return baseShipmentEnterpriseMapper.updateByPrimaryKeySelective(baseShipmentEnterprise);
    }

    @Override
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        List<BaseHtShipmentEnterprise> baseHtShipmentEnterprises = new ArrayList<>();
        String[] idsArr  = ids.split(",");
        for (String id : idsArr) {
            BaseShipmentEnterprise baseShipmentEnterprise = baseShipmentEnterpriseMapper.selectByPrimaryKey(id);
            if (StringUtils.isEmpty(baseShipmentEnterprise)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

            BaseHtShipmentEnterprise baseHtShipmentEnterprise = new BaseHtShipmentEnterprise();
            BeanUtils.copyProperties(baseShipmentEnterprise,baseHtShipmentEnterprise);
            baseHtShipmentEnterprises.add(baseHtShipmentEnterprise);
        }

        baseHtShipmentEnterpriseMapper.insertList(baseHtShipmentEnterprises);
        return baseShipmentEnterpriseMapper.deleteByIds(ids);
    }

    @Override
    public List<BaseShipmentEnterpriseDto> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId", user.getOrganizationId());
        return baseShipmentEnterpriseMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importExcel(List<BaseShipmentEnterpriseImport> baseShipmentEnterpriseImports) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();

        Map<String, Object> resutlMap = new HashMap<>();  //封装操作结果
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数
        LinkedList<BaseShipmentEnterprise> list = new LinkedList<>();
        LinkedList<BaseHtShipmentEnterprise> htList = new LinkedList<>();
        for (int i = 0; i < baseShipmentEnterpriseImports.size(); i++) {
            BaseShipmentEnterpriseImport shipmentEnterpriseImport = baseShipmentEnterpriseImports.get(i);

            String shipmentEnterpriseCode = shipmentEnterpriseImport.getShipmentEnterpriseCode();
            if (StringUtils.isEmpty(
                    shipmentEnterpriseCode
            )){
                fail.add(i+4);
                continue;
            }

            //判断编码是否重复
            Example example = new Example(BaseShipmentEnterprise.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("organizationId", currentUser.getOrganizationId());
            criteria.andEqualTo("shipmentEnterpriseCode",shipmentEnterpriseCode);
            if (StringUtils.isNotEmpty(baseShipmentEnterpriseMapper.selectOneByExample(example))){
                fail.add(i+4);
                continue;
            }

            //判断集合中是否已经存在同样的数据
            boolean tag = false;
            if (StringUtils.isNotEmpty(list)){
                for (BaseShipmentEnterprise baseShipmentEnterprise : list) {
                    if (baseShipmentEnterprise.getShipmentEnterpriseCode().equals(shipmentEnterpriseCode)){
                        tag = true;
                    }
                }
            }
            if (tag){
                fail.add(i+4);
                continue;
            }


            BaseShipmentEnterprise baseShipmentEnterprise = new BaseShipmentEnterprise();
            BeanUtils.copyProperties(shipmentEnterpriseImport, baseShipmentEnterprise);
            baseShipmentEnterprise.setCreateTime(new Date());
            baseShipmentEnterprise.setCreateUserId(currentUser.getUserId());
            baseShipmentEnterprise.setModifiedTime(new Date());
            baseShipmentEnterprise.setModifiedUserId(currentUser.getUserId());
            baseShipmentEnterprise.setStatus((byte)1);
            baseShipmentEnterprise.setOrganizationId(currentUser.getOrganizationId());
            baseShipmentEnterprise.setTransportCategoryId(StringUtils.isEmpty(shipmentEnterpriseImport.getTransportCategoryId())?null:shipmentEnterpriseImport.getTransportCategoryId().byteValue());
            list.add(baseShipmentEnterprise);
        }

        if (StringUtils.isNotEmpty(list)){
            success = baseShipmentEnterpriseMapper.insertList(list);
        }

        for (BaseShipmentEnterprise baseShipmentEnterprise : list) {
            BaseHtShipmentEnterprise baseHtShipmentEnterprise = new BaseHtShipmentEnterprise();
            BeanUtils.copyProperties(baseShipmentEnterprise, baseHtShipmentEnterprise);
            htList.add(baseHtShipmentEnterprise);
        }

        if (StringUtils.isNotEmpty(htList)){
            baseHtShipmentEnterpriseMapper.insertList(htList);
        }

        resutlMap.put("操作成功总数",success);
        resutlMap.put("操作失败行数",fail);
        return resutlMap;
    }
}
