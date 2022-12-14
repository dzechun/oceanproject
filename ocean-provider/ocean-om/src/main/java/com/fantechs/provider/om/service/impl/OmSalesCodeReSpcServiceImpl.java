package com.fantechs.provider.om.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseTabDto;
import com.fantechs.common.base.general.dto.om.OmHtSalesCodeReSpcDto;
import com.fantechs.common.base.general.dto.om.OmSalesCodeReSpcDto;
import com.fantechs.common.base.general.dto.om.imports.OmSalesCodeReSpcImport;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerInventoryDetDto;
import com.fantechs.common.base.general.entity.basic.BaseMaterial;
import com.fantechs.common.base.general.entity.basic.BaseProductModel;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseMaterial;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseTab;
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
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

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

        Map<String, Object> resultMap = new HashMap<>();  //??????????????????
        int success = 0;  //?????????????????????
        List<Integer> fail = new ArrayList<>();  //????????????????????????
        List<OmSalesCodeReSpc> omSalesCodeReSpcs = new ArrayList<>();
        List<OmHtSalesCodeReSpc> omHtSalesCodeReSpcs = new ArrayList<>();

        // ????????????????????????
        //List<BaseMaterial> baseMaterials = baseFeignApi.findMaterialAll().getData();
        // ????????????PO???
        List<OmSalesCodeReSpcDto> reSpcDtos = omSalesCodeReSpcMapper.findList(new HashMap<>());
        // ??????????????????
        for (int i = 0; i < list.size(); i++){
            OmSalesCodeReSpcImport item = list.get(i);
            if (StringUtils.isEmpty(
                    item.getSalesCode(),
                    item.getSamePackageCode(),
                    item.getMaterialCode(),
                    item.getSamePackageCodeQty())
            ){
                fail.add(i + 1);
                continue;
            }

            // ????????????
            /*for (BaseMaterial material : baseMaterials){
                if (material.getMaterialCode().equals(item.getMaterialCode())){
                    item.setMaterialId(material.getMaterialId());
                    break;
                }
            }
            if (StringUtils.isEmpty(item.getMaterialId())){
                fail.add(i + 1);
                continue;
            }*/
            String materialCode=item.getMaterialCode();
            materialCode=materialCode.replaceAll("\r", "");
            materialCode=materialCode.replaceAll("\n", "");
            materialCode=materialCode.trim();
            SearchBaseMaterial searchBaseMaterial=new SearchBaseMaterial();
            searchBaseMaterial.setMaterialCode(materialCode);
            searchBaseMaterial.setCodeQueryMark(1);
            List<BaseMaterial> materialList=baseFeignApi.findList(searchBaseMaterial).getData();
            if(StringUtils.isNotEmpty(materialList) && materialList.size()>0){
                item.setMaterialId(materialList.get(0).getMaterialId());
            }
            else {
                fail.add(i + 1);
                continue;
            }

            //????????????????????????
            if(StringUtils.isNotEmpty(item.getMaterialId())){
                SearchBaseTab searchBaseTab=new SearchBaseTab();
                searchBaseTab.setMaterialId(item.getMaterialId());
                List<BaseTabDto> tabDtoList=baseFeignApi.findTabList(searchBaseTab).getData();
                if(StringUtils.isNotEmpty(tabDtoList) && tabDtoList.size()>0){
                    item.setProductModelId(tabDtoList.get(0).getProductModelId());
                }
            }

            /*boolean flag = false;
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
            }*/

            List<OmSalesCodeReSpcDto> salesCodeReSpcDtoList = reSpcDtos.stream().filter(u -> (u.getSalesCode().equals(item.getSalesCode()) && u.getPriority().toString().equals(item.getPriority()))).collect(Collectors.toList());
            if(salesCodeReSpcDtoList.size()>0){
                fail.add(i + 1);
                continue;
            }

            OmSalesCodeReSpc reSpc = new OmSalesCodeReSpc();
            BeanUtil.copyProperties(item, reSpc);
            reSpc.setSamePackageCodeStatus((byte)1);
            reSpc.setMatchedQty(new BigDecimal(0));
            reSpc.setCreateTime(new Date());
            reSpc.setCreateUserId(user.getUserId());
            reSpc.setModifiedUserId(user.getUserId());
            reSpc.setModifiedTime(new Date());
            reSpc.setOrgId(user.getOrganizationId());
            reSpc.setStatus((byte) 1);
            reSpc.setIsDelete((byte) 1);
            omSalesCodeReSpcs.add(reSpc);

            // ??????
            OmHtSalesCodeReSpc ht = new OmHtSalesCodeReSpcDto();
            BeanUtil.copyProperties(reSpc, ht);
            omHtSalesCodeReSpcs.add(ht);
        }

        if (!omSalesCodeReSpcs.isEmpty()){
            success = omSalesCodeReSpcMapper.insertList(omSalesCodeReSpcs);
            // ????????????
            omHtSalesCodeReSpcService.batchSave(omHtSalesCodeReSpcs);
        }

        resultMap.put("??????????????????",success);
        resultMap.put("??????????????????",fail);
        return resultMap;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(OmSalesCodeReSpc omSalesCodeReSpc) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        omSalesCodeReSpc.setOrgId(user.getOrganizationId());

        Example example = new Example(OmSalesCodeReSpc.class);
        Example.Criteria criteria = example.createCriteria();
//        criteria.andEqualTo("samePackageCode",omSalesCodeReSpc.getSamePackageCode());
//        if(omSalesCodeReSpcMapper.selectCountByExample(example)>0){
//            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"??????PO");
//        }
//        example.clear();
//        criteria = example.createCriteria();
        criteria.andEqualTo("salesCode", omSalesCodeReSpc.getSalesCode());
        criteria.andEqualTo("priority", omSalesCodeReSpc.getPriority());
        int count = omSalesCodeReSpcMapper.selectCountByExample(example);
        if (count > 0){
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "?????????????????????????????????????????????");
        }

        omSalesCodeReSpc.setIsDelete((byte) 1);
        omSalesCodeReSpc.setCreateTime(new Date());
        omSalesCodeReSpc.setCreateUserId(user.getUserId());
        omSalesCodeReSpc.setModifiedTime(new Date());
        omSalesCodeReSpc.setModifiedUserId(user.getUserId());
        omSalesCodeReSpc.setStatus((byte) 1);
        omSalesCodeReSpc.setOrgId(user.getOrganizationId());
        int i = omSalesCodeReSpcMapper.insertSelective(omSalesCodeReSpc);

        // ???????????????
        OmHtSalesCodeReSpc ht = new OmHtSalesCodeReSpc();
        BeanUtil.copyProperties(omSalesCodeReSpc, ht);
        omHtSalesCodeReSpcService.save(ht);
        return i;
    }

    @Override
    public int update(OmSalesCodeReSpc omSalesCodeReSpc){
        if (omSalesCodeReSpc.getSamePackageCodeStatus().equals((byte) 1)
                && omSalesCodeReSpc.getSamePackageCodeQty().compareTo(omSalesCodeReSpc.getMatchedQty()) == 0){
            throw new BizErrorException(ErrorCodeEnum.OPT20012002.getCode(), "PO?????????????????????????????????????????????");
        }

        Example example = new Example(OmSalesCodeReSpc.class);
        Example.Criteria criteria = example.createCriteria();
//        criteria.andEqualTo("samePackageCode",omSalesCodeReSpc.getSamePackageCode()).andNotEqualTo("salesCodeReSpcId",omSalesCodeReSpc.getSalesCodeReSpcId());
//        if(omSalesCodeReSpcMapper.selectCountByExample(example)>0){
//            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"??????PO");
//        }
//        example.clear();
//        criteria = example.createCriteria();
        criteria.andEqualTo("salesCode", omSalesCodeReSpc.getSalesCode())
                .andEqualTo("priority", omSalesCodeReSpc.getPriority())
                .andNotEqualTo("salesCodeReSpcId", omSalesCodeReSpc.getSalesCodeReSpcId());
        int count = omSalesCodeReSpcMapper.selectCountByExample(example);
        if (count > 0){
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "?????????????????????????????????????????????");
        }
        int i = omSalesCodeReSpcMapper.updateByPrimaryKeySelective(omSalesCodeReSpc);

        // ???????????????
        OmHtSalesCodeReSpc ht = new OmHtSalesCodeReSpc();
        BeanUtil.copyProperties(omSalesCodeReSpc, ht);
        omHtSalesCodeReSpcService.save(ht);
        return i;
    }
}
