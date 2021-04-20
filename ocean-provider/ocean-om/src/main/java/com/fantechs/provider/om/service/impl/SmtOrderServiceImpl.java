package com.fantechs.provider.om.service.impl;


import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.general.entity.basic.BaseMaterial;
import com.fantechs.common.base.general.entity.basic.BaseSupplier;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseMaterial;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseSupplier;
import com.fantechs.common.base.general.dto.om.*;
import com.fantechs.common.base.general.dto.mes.pm.search.SearchMesOrderMaterialListDTO;
import com.fantechs.common.base.general.dto.om.imports.SmtOrderImport;
import com.fantechs.common.base.general.entity.om.MesOrderMaterial;
import com.fantechs.common.base.general.entity.om.SmtOrder;
import com.fantechs.common.base.general.entity.mes.pm.history.MesHtOrderMaterial;
import com.fantechs.common.base.general.entity.om.SmtHtOrder;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.BeanUtils;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.om.mapper.SmtHtOrderMapper;
import com.fantechs.provider.om.mapper.SmtOrderMapper;
import com.fantechs.provider.om.service.SmtOrderService;
import com.fantechs.provider.om.service.ht.MesHtOrderMaterialService;
import com.fantechs.provider.om.service.ht.SmtHtOrderService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by leifengzhi on 2020/10/13.
 */
@Service
public class SmtOrderServiceImpl extends BaseService<SmtOrder> implements SmtOrderService {

    @Resource
    private SmtOrderMapper smtOrderMapper;
    @Resource
    private SmtHtOrderService smtHtOrderService;
    @Resource
    private SmtHtOrderMapper smtHtOrderMapper;
    @Resource
    private MesHtOrderMaterialService mesHtOrderMaterialService;
    @Resource
    private BaseFeignApi baseFeignApi;


    @Override
    public int save(SmtOrder smtOrder) {
        SysUser currentUserInfo = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(currentUserInfo)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        /*Example example = new Example(SmtOrder.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("orderCode",smtOrder.getOrderCode());
        SmtOrder order = smtOrderMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(order)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }*/
        smtOrder.setOrderCode(CodeUtils.getId("ORDER"));
        smtOrder.setCreateTime(new Date());
        smtOrder.setCreateUserId(currentUserInfo.getUserId());
        smtOrder.setModifiedTime(new Date());
        smtOrder.setModifiedUserId(currentUserInfo.getUserId());
        if(smtOrderMapper.insertSelective(smtOrder)<=0){
            return 0;
        }
        recordHistory(smtOrder,"新增");
        return 1;
    }

    @Override
    public int batchDelete(String ids) {
        SysUser currentUserInfo = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(currentUserInfo)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        String[] idArray = ids.split(",");
        for (String id : idArray) {
            SmtOrder smtOrder = smtOrderMapper.selectByPrimaryKey(id);
            if (StringUtils.isEmpty(smtOrder)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            recordHistory(smtOrder,"删除");
        }
        if(smtOrderMapper.deleteByIds(ids)<=0){
            return 0;
        }
        return 1;
    }

    @Override
    public int update(SmtOrder smtOrder) {
        SysUser currentUserInfo = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(currentUserInfo)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        /*Example example = new Example(SmtOrder.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("orderCode",smtOrder.getOrderCode());
        SmtOrder order = smtOrderMapper.selectOneByExample(example);

        if (StringUtils.isNotEmpty(order) && !order.getOrderId().equals(smtOrder.getOrderId())){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }*/

        smtOrder.setModifiedUserId(currentUserInfo.getUserId());
        smtOrder.setModifiedTime(new Date());
        if(smtOrderMapper.updateByPrimaryKey(smtOrder)<=0){
            return 0;
        }
        recordHistory(smtOrder,"更新");
        return 1;
    }

    @Override
    public List<SmtOrderDto> findList(Map<String,Object> parm) {
        return smtOrderMapper.findList(parm);
    }

    @Override
    public int orderMaterialSchedule(Long orderMaterialId) {
        return smtOrderMapper.orderMaterialSchedule(orderMaterialId);
    }

    @Override
    public int saveOrderMaterial(SaveOrderMaterialDTO saveOrderMaterialDTO) {
        SysUser sysUser = currentUser();
        SmtOrder smtOrder = saveOrderMaterialDTO.getSmtOrder();
        List<MesOrderMaterial> mesOrderMaterialList = saveOrderMaterialDTO.getMesOrderMaterialList();
        //=====遍历累加物料的总数量
        int total=0;//订单物料总共数量
        if(StringUtils.isNotEmpty(mesOrderMaterialList)){
            for (MesOrderMaterial mesOrderMaterial : mesOrderMaterialList) {
                total+=mesOrderMaterial.getTotal().intValue();
            }
            smtOrder.setOrderQuantity(total);
        }
        //=====
        if(StringUtils.isEmpty(smtOrder.getOrderId())){
            if(this.save(smtOrder)<=0){
                return 0;
            }
        }else{
            if(this.update(smtOrder)<=0){
                return 0;
            }
        }
        //删除原有的关联产品信息
        smtOrderMapper.deleteMaterialByOrderId(smtOrder.getOrderId());
        if(StringUtils.isNotEmpty(mesOrderMaterialList)){
            for (MesOrderMaterial mesOrderMaterial : mesOrderMaterialList) {
                mesOrderMaterial.setOrderId(smtOrder.getOrderId());
                mesOrderMaterial.setCreateTime(new Date());
                mesOrderMaterial.setCreateUserId(sysUser.getUserId());
                mesOrderMaterial.setModifiedTime(new Date());
                mesOrderMaterial.setModifiedUserId(sysUser.getUserId());
            }
            if(smtOrderMapper.batchAddOrderMaterial(mesOrderMaterialList)<=0){
                throw new BizErrorException(ErrorCodeEnum.OPT20012006);
            }
        }
        return 1;
    }

    @Override
    public List<MesOrderMaterialDTO> findOrderMaterial(SearchMesOrderMaterialListDTO searchMesOrderMaterialListDTO) {
        return smtOrderMapper.findOrderMaterial(searchMesOrderMaterialListDTO);
    }

    /**
     * 销售情况报表
     * @param searchSmtOrderReportDto
     * @return
     */
    @Override
    public List<SmtOrderReportDto> orderReport(SearchSmtOrderReportDto searchSmtOrderReportDto) {
        return smtOrderMapper.orderReport(searchSmtOrderReportDto);
    }

    @Override
    public List<FindOrderMaterialDto> findOrder() {
        return smtOrderMapper.findOrder();
    }

    /**
     * 记录操作历史
     * @param smtOrder
     * @param operation
     */
    private void recordHistory(SmtOrder smtOrder,String operation){
        SmtHtOrder smtHtOrder = new SmtHtOrder();
        smtHtOrder.setOption1(operation);
        if (StringUtils.isEmpty(smtOrder)){
            return;
        }
        BeanUtils.autoFillEqFields(smtOrder,smtHtOrder);
        smtHtOrderService.save(smtHtOrder);
    }

    /**
     * 记录订单物料操作历史
     * @param id
     * @param operation
     */
    private void recordOrderMaterialHistory(Long id,String operation){
        MesHtOrderMaterial mesHtOrderMaterial = new MesHtOrderMaterial();
        mesHtOrderMaterial.setOperation(operation);
        SearchMesOrderMaterialListDTO searchMesOrderMaterialListDTO = new SearchMesOrderMaterialListDTO();
        List<MesOrderMaterialDTO> orderMaterialDTOList = smtOrderMapper.findOrderMaterial(searchMesOrderMaterialListDTO);
        if (StringUtils.isEmpty(orderMaterialDTOList)){
            return;
        }
        BeanUtils.autoFillEqFields(orderMaterialDTOList.get(0),mesHtOrderMaterial);
        mesHtOrderMaterialService.save(mesHtOrderMaterial);
    }

    /**
     * 获取当前登录用户
     * @return
     */
    private SysUser currentUser(){
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        return user;
    }

    @Override
    public Map<String, Object> importExcel(List<SmtOrderImport> smtOrderImports) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        Map<String, Object> resultMap = new HashMap<>();  //封装操作结果
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数

        LinkedList<SmtHtOrder> htList = new LinkedList<>();
        LinkedList<SmtOrderImport> orderImports = new LinkedList<>();
        LinkedList<MesOrderMaterial> mesOrderMaterials = new LinkedList<>();
        for (int i = 0; i < smtOrderImports.size(); i++) {
            SmtOrderImport smtOrderImport = smtOrderImports.get(i);
            String freightNum = smtOrderImport.getFreightNum();
            String contractCode = smtOrderImport.getContractCode();
            String materialCode = smtOrderImport.getMaterialCode();
            String supplierCode = smtOrderImport.getSupplierCode();

            if (StringUtils.isEmpty(
                    freightNum,contractCode,materialCode,supplierCode
            )){
                fail.add(i+4);
                continue;
            }

            //判断编码是否重复
            Example example = new Example(SmtOrder.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("contractCode",contractCode).orEqualTo("freightNum",freightNum);
            List<SmtOrder> smtOrders = smtOrderMapper.selectByExample(example);
            if (StringUtils.isNotEmpty(smtOrders)){
                fail.add(i+4);
                continue;
            }

            //判断物料信息是否存在
            SearchBaseMaterial searchBaseMaterial = new SearchBaseMaterial();
            searchBaseMaterial.setCodeQueryMark(1);
            searchBaseMaterial.setMaterialCode(materialCode);
            List<BaseMaterial> baseMaterials = baseFeignApi.findSmtMaterialList(searchBaseMaterial).getData();
            if (StringUtils.isEmpty(baseMaterials)){
                fail.add(i+4);
                continue;
            }
            smtOrderImport.setMaterialId(baseMaterials.get(0).getMaterialId());

            //判断客户信息是否存在
            SearchBaseSupplier searchBaseSupplier = new SearchBaseSupplier();
            searchBaseSupplier.setSupplierCode(supplierCode);
            searchBaseSupplier.setCodeQueryMark((byte) 1);
            List<BaseSupplier> baseSuppliers = baseFeignApi.findSupplierList(searchBaseSupplier).getData();
            if (StringUtils.isEmpty(baseSuppliers)){
                fail.add(i+4);
                continue;
            }
            smtOrderImport.setSupplierId(baseSuppliers.get(0).getSupplierId());

            orderImports.add(smtOrderImport);
        }

        if (StringUtils.isNotEmpty(orderImports)) {
            //对合格数据进行分组
            Map<String, List<SmtOrderImport>> map = orderImports.stream().collect(Collectors.groupingBy(SmtOrderImport::getContractCode, HashMap::new, Collectors.toList()));
            Set<String> codeList = map.keySet();
            for (String code : codeList) {
                List<SmtOrderImport> smtOrderImports1 = map.get(code);

                //新增订单
                if (StringUtils.isNotEmpty(smtOrderImports1)){
                    SmtOrderImport smtOrderImport = smtOrderImports1.get(0);
                    SmtOrder smtOrder = new SmtOrder();
                    org.springframework.beans.BeanUtils.copyProperties(smtOrderImport,smtOrder);
                    smtOrder.setCreateUserId(currentUser.getUserId());
                    smtOrder.setCreateTime(new Date());
                    smtOrder.setModifiedUserId(currentUser.getUserId());
                    smtOrder.setModifiedTime(new Date());
                    smtOrder.setSalesManName(currentUser.getNickName());
                    smtOrder.setOrderCode(CodeUtils.getId("ORDER"));
                    smtOrderMapper.insertUseGeneratedKeys(smtOrder);

                    SmtHtOrder smtHtOrder = new SmtHtOrder();
                    org.springframework.beans.BeanUtils.copyProperties(smtOrder,smtHtOrder);
                    smtHtOrder.setModifiedTime(new Date());
                    smtHtOrder.setModifiedUserId(currentUser.getUserId());
                    htList.add(smtHtOrder);

                    for (SmtOrderImport orderImport : smtOrderImports1) {
                        MesOrderMaterial mesOrderMaterial = new MesOrderMaterial();
                        org.springframework.beans.BeanUtils.copyProperties(orderImport,mesOrderMaterial);
                        mesOrderMaterial.setCreateTime(new Date());
                        mesOrderMaterial.setCreateUserId(currentUser.getUserId());
                        mesOrderMaterial.setModifiedTime(new Date());
                        mesOrderMaterial.setModifiedUserId(currentUser.getUserId());
                        mesOrderMaterial.setOrderId(smtOrder.getOrderId());
                        mesOrderMaterials.add(mesOrderMaterial);
                    }

                    smtHtOrderMapper.insertList(htList);
                    success += smtOrderMapper.batchAddOrderMaterial(mesOrderMaterials);
                }
            }
        }

        resultMap.put("操作成功总数",success);
        resultMap.put("操作失败行数",fail);
        return resultMap;
    }
}
