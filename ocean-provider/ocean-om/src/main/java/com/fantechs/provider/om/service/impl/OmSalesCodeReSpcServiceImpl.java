package com.fantechs.provider.om.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.om.OmHtSalesCodeReSpcDto;
import com.fantechs.common.base.general.dto.om.OmSalesCodeReSpcDto;
import com.fantechs.common.base.general.dto.om.imports.OmSalesCodeReSpcImport;
import com.fantechs.common.base.general.entity.basic.BaseMaterial;
import com.fantechs.common.base.general.entity.basic.BaseProductModel;
import com.fantechs.common.base.general.entity.om.OmHtSalesCodeReSpc;
import com.fantechs.common.base.general.entity.om.OmSalesCodeReSpc;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.om.mapper.OmSalesCodeReSpcMapper;
import com.fantechs.provider.om.service.OmSalesCodeReSpcService;
import com.fantechs.provider.om.service.ht.OmHtSalesCodeReSpcService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;

/**
 *
 * Created by leifengzhi on 2021/10/15.
 */
@Service
public class OmSalesCodeReSpcServiceImpl extends BaseService<OmSalesCodeReSpc> implements OmSalesCodeReSpcService {

    @Resource
    private OmSalesCodeReSpcMapper omSalesCodeReSpcMapper;

    @Resource
    private OmHtSalesCodeReSpcService omHtSalesCodeReSpcService;

    @Resource
    private BaseFeignApi baseFeignApi;

    @Override
    public List<OmSalesCodeReSpcDto> findList(Map<String, Object> map) {
        if(StringUtils.isEmpty(map.get("orgId"))) {
            SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
            map.put("orgId", user.getOrganizationId());
        }
        return omSalesCodeReSpcMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importExcel(List<OmSalesCodeReSpcImport> list) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        Map<String, Object> resultMap = new HashMap<>();  //封装操作结果
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数
        List<OmSalesCodeReSpc> omSalesCodeReSpcs = new ArrayList<>();
        List<OmHtSalesCodeReSpc> omHtSalesCodeReSpcs = new ArrayList<>();

        // 获取物料编码集合
        List<BaseMaterial> baseMaterials = baseFeignApi.findMaterialAll().getData();
        // 获取所有PO号
        List<OmSalesCodeReSpcDto> reSpcDtos = omSalesCodeReSpcMapper.findList(new HashMap<>());
        // 循环便利数据
        for (int i = 0; i < list.size(); i++){
            OmSalesCodeReSpcImport item = list.get(i);
            if (StringUtils.isEmpty(
                    item.getSalesCode(),
                    item.getSamePackageCode(),
                    item.getMaterialCode(),
                    item.getSamePackageCodeQty(),
                    item.getSamePackageCodeStatus())
            ){
                fail.add(i + 1);
                continue;
            }

            // 判断物料
            for (BaseMaterial material : baseMaterials){
                if (material.getMaterialCode().equals(item.getMaterialCode())){
                    item.setMaterialId(material.getMaterialId());
                    break;
                }
            }
            if (StringUtils.isEmpty(item.getMaterialId())){
                fail.add(i + 1);
                continue;
            }

            boolean flag = false;
            for (OmSalesCodeReSpcDto dto : reSpcDtos){
                if (dto.getSalesCode().equals(item.getSalesCode())
                        && dto.getPriority().toString().equals(item.getPriority())){
                    flag = true;
                    break;
                }
            }
            if (flag){
                fail.add(i + 1);
                continue;
            }

            OmSalesCodeReSpc reSpc = new OmSalesCodeReSpc();
            BeanUtil.copyProperties(item, reSpc);
            reSpc.setCreateTime(new Date());
            reSpc.setCreateUserId(user.getUserId());
            reSpc.setModifiedUserId(user.getUserId());
            reSpc.setModifiedTime(new Date());
            reSpc.setOrgId(user.getOrganizationId());
            reSpc.setStatus((byte) 1);
            reSpc.setIsDelete((byte) 1);
            omSalesCodeReSpcs.add(reSpc);

            // 履历
            OmHtSalesCodeReSpc ht = new OmHtSalesCodeReSpcDto();
            BeanUtil.copyProperties(reSpc, ht);
            omHtSalesCodeReSpcs.add(ht);
        }

        if (!omSalesCodeReSpcs.isEmpty()){
            success = omSalesCodeReSpcMapper.insertList(omSalesCodeReSpcs);
            // 保存履历
            omHtSalesCodeReSpcService.batchSave(omHtSalesCodeReSpcs);
        }

        resultMap.put("操作成功总数",success);
        resultMap.put("操作失败行数",fail);
        return resultMap;
    }

    @Override
    public int save(OmSalesCodeReSpc omSalesCodeReSpc) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        omSalesCodeReSpc.setOrgId(user.getOrganizationId());

        Example example = new Example(OmSalesCodeReSpc.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("salesCode", omSalesCodeReSpc.getSalesCode())
                .andEqualTo("priority", omSalesCodeReSpc.getPriority());
        int count = omSalesCodeReSpcMapper.selectCountByExample(example);
        if (count > 0){
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "同个销售编码不能存在相同优先级");
        }

        omSalesCodeReSpc.setIsDelete((byte) 1);
        omSalesCodeReSpc.setCreateTime(new Date());
        omSalesCodeReSpc.setCreateUserId(user.getUserId());
        omSalesCodeReSpc.setModifiedTime(new Date());
        omSalesCodeReSpc.setModifiedUserId(user.getUserId());
        omSalesCodeReSpc.setStatus((byte) 1);
        omSalesCodeReSpc.setOrgId(user.getOrganizationId());
        int i = omSalesCodeReSpcMapper.insertSelective(omSalesCodeReSpc);

        // 保存履历表
        OmHtSalesCodeReSpc ht = new OmHtSalesCodeReSpc();
        BeanUtil.copyProperties(omSalesCodeReSpc, ht);
        omHtSalesCodeReSpcService.save(ht);
        return i;
    }

    @Override
    public int update(OmSalesCodeReSpc omSalesCodeReSpc){
        if (omSalesCodeReSpc.getSamePackageCodeStatus().equals((byte) 1)
                && omSalesCodeReSpc.getSamePackageCodeQty().compareTo(omSalesCodeReSpc.getMatchedQty()) == 0){
            throw new BizErrorException(ErrorCodeEnum.OPT20012002.getCode(), "PO号匹配数量已满足，不可重新激活");
        }

        Example example = new Example(OmSalesCodeReSpc.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("salesCode", omSalesCodeReSpc.getSalesCode())
                .andEqualTo("priority", omSalesCodeReSpc.getPriority())
                .andNotEqualTo("salesCodeReSpcId", omSalesCodeReSpc.getSalesCodeReSpcId());
        int count = omSalesCodeReSpcMapper.selectCountByExample(example);
        if (count > 0){
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "同个销售编码不能存在相同优先级");
        }
        int i = omSalesCodeReSpcMapper.updateByPrimaryKeySelective(omSalesCodeReSpc);

        // 保存履历表
        OmHtSalesCodeReSpc ht = new OmHtSalesCodeReSpc();
        BeanUtil.copyProperties(omSalesCodeReSpc, ht);
        omHtSalesCodeReSpcService.save(ht);
        return i;
    }
}
