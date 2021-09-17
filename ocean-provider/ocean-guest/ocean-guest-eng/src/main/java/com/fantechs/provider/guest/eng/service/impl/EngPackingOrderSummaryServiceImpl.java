package com.fantechs.provider.guest.eng.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.eng.EngPackingOrderSummaryDto;
import com.fantechs.common.base.general.dto.eng.imports.EngPackingOrderSummaryImport;
import com.fantechs.common.base.general.entity.basic.BaseSupplierReUser;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseSupplierReUser;
import com.fantechs.common.base.general.entity.eng.*;
import com.fantechs.common.base.general.entity.eng.history.EngHtPackingOrderSummary;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.guest.eng.mapper.*;
import com.fantechs.provider.guest.eng.service.EngPackingOrderSummaryService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 *
 * Created by leifengzhi on 2021/08/27.
 */
@Service
public class EngPackingOrderSummaryServiceImpl extends BaseService<EngPackingOrderSummary> implements EngPackingOrderSummaryService {

    @Resource
    private EngPackingOrderSummaryMapper engPackingOrderSummaryMapper;
    @Resource
    private EngHtPackingOrderSummaryMapper engHtPackingOrderSummaryMapper;
    @Resource
    private BaseFeignApi baseFeignApi;
    @Resource
    private EngPackingOrderMapper engPackingOrderMapper;
    @Resource
    private EngPackingOrderSummaryDetMapper engPackingOrderSummaryDetMapper;
    @Resource
    private EngContractQtyOrderMapper engContractQtyOrderMapper;
    @Resource
    private EngPurchaseReqOrderMapper engPurchaseReqOrderMapper;


    @Override
    public List<EngPackingOrderSummaryDto> findList(Map<String, Object> map) {
        SysUser user = getUser();
        map.put("orgId",user.getOrganizationId());
        return engPackingOrderSummaryMapper.findList(map);
    }

    public int save(EngPackingOrderSummaryDto engPackingOrderSummaryDto) {
        SysUser user = getUser();
        EngPackingOrder engPackingOrder = getEngPackingOrder(user.getOrganizationId(), null,engPackingOrderSummaryDto.getPackingOrderId());
        //规则校验
        check(engPackingOrderSummaryDto,user,engPackingOrder);

        //判断编码是否重复
        Example example = new Example(EngPackingOrderSummary.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("orgId", user.getOrganizationId());
        criteria.andEqualTo("cartonCode",engPackingOrderSummaryDto.getCartonCode());
        if (StringUtils.isNotEmpty(engPackingOrderSummaryMapper.selectOneByExample(example))){
            throw new BizErrorException("添加失败，编码重复");
        }


        if(StringUtils.isNotEmpty(engPackingOrder)){
            engPackingOrderSummaryDto.setPackingOrderSummaryId(engPackingOrder.getPackingOrderId());
        }else{
            throw new BizErrorException("添加失败，未查询到上级数据");
        }

        engPackingOrderSummaryDto.setCreateTime(new Date());
        engPackingOrderSummaryDto.setCreateUserId(user.getUserId());
        engPackingOrderSummaryDto.setModifiedTime(new Date());
        engPackingOrderSummaryDto.setModifiedUserId(user.getUserId());
        engPackingOrderSummaryDto.setStatus((byte)1);
        engPackingOrderSummaryDto.setOrgId(user.getOrganizationId());

        int i = engPackingOrderSummaryMapper.insertUseGeneratedKeys(engPackingOrderSummaryDto);

        EngHtPackingOrderSummary engHtPackingOrderSummary =new EngHtPackingOrderSummary();
        BeanUtils.copyProperties(engPackingOrderSummaryDto, engHtPackingOrderSummary);
        engHtPackingOrderSummaryMapper.insertSelective(engHtPackingOrderSummary);
        return i;
    }


    @Override
    public int batchAdd(List<EngPackingOrderSummaryDto> engPackingOrderSummaryDtos) {
        List<EngPackingOrderSummary> ins = new ArrayList<EngPackingOrderSummary>();
        List<EngHtPackingOrderSummary> engHtPackingOrderSummarys = new ArrayList<EngHtPackingOrderSummary>();
        SysUser user = getUser();
        int result =1;
        for(EngPackingOrderSummaryDto dto : engPackingOrderSummaryDtos) {

            EngPackingOrder engPackingOrder = getEngPackingOrder(user.getOrganizationId(), null,dto.getPackingOrderId());
            if(StringUtils.isNotEmpty(engPackingOrder)){
                dto.setPackingOrderId(engPackingOrder.getPackingOrderId());
            }else{
                 throw new BizErrorException("添加失败，未查询到上级数据");
            }
            //规则校验
            check(dto,user,engPackingOrder);

            if (StringUtils.isNotEmpty(dto.getPackingOrderSummaryId())) {
                if(StringUtils.isNotEmpty(dto.getCartonCode())){
                    Example example = new Example(EngPackingOrderSummaryDet.class);
                    Example.Criteria criteria = example.createCriteria();
                    criteria.andEqualTo("orgId", user.getOrganizationId());
                    criteria.andEqualTo("packingOrderSummaryId",dto.getPackingOrderSummaryId());
                    List<EngPackingOrderSummaryDet> engPackingOrderSummaryDets = engPackingOrderSummaryDetMapper.selectByExample(example);
                    if(StringUtils.isNotEmpty(engPackingOrderSummaryDets)){
                        for(EngPackingOrderSummaryDet det : engPackingOrderSummaryDets){
                            det.setCartonCode(dto.getCartonCode());
                            engPackingOrderSummaryDetMapper.updateByPrimaryKeySelective(det);
                        }
                    }
                }
                int i = engPackingOrderSummaryMapper.updateByPrimaryKeySelective(dto);
                if (i<1) result=i;
                continue;
            }else{
                dto.setCreateTime(new Date());
                dto.setCreateUserId(user.getUserId());
                dto.setModifiedTime(new Date());
                dto.setModifiedUserId(user.getUserId());
                dto.setStatus((byte)1);
                dto.setOrgId(user.getOrganizationId());
                ins.add(dto);
                EngHtPackingOrderSummary engHtPackingOrderSummary =new EngHtPackingOrderSummary();
                BeanUtils.copyProperties(dto, engHtPackingOrderSummary);
                engHtPackingOrderSummarys.add(engHtPackingOrderSummary);
            }

        }

        if(StringUtils.isNotEmpty(ins)) {
           int i= engPackingOrderSummaryMapper.insertList(ins);
           if (i<1) result=i;
        }
        //新增历史信息
        if(StringUtils.isNotEmpty(engHtPackingOrderSummarys))
            engHtPackingOrderSummaryMapper.insertList(engHtPackingOrderSummarys);

            return result;


    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(String ids) {
        SysUser user = getUser();

        List<EngHtPackingOrderSummary> htList = new ArrayList<>();
        String[] split = ids.split(",");
        for (String id : split){
            EngPackingOrderSummary engPackingOrderSummary = engPackingOrderSummaryMapper.selectByPrimaryKey(id);
            if (StringUtils.isEmpty(engPackingOrderSummary)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

            //新增履历信息
            EngHtPackingOrderSummary engHtPackingOrderSummary = new EngHtPackingOrderSummary();
            BeanUtils.copyProperties(engPackingOrderSummary, engHtPackingOrderSummary);
            htList.add(engHtPackingOrderSummary);

            Example example = new Example(EngPackingOrderSummaryDet.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("packingOrderSummaryId",id);
            engPackingOrderSummaryDetMapper.deleteByExample(example);
        }

        engHtPackingOrderSummaryMapper.insertList(htList);

        return engPackingOrderSummaryMapper.deleteByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importExcel(List<EngPackingOrderSummaryImport> engPackingOrderSummaryImports,Long packingOrderId) {
        SysUser user = getUser();
        Map<String, Object> resutlMap = new HashMap<>();  //封装操作结果
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数
        LinkedList<EngPackingOrderSummary> list = new LinkedList<>();
        LinkedList<EngHtPackingOrderSummary> htList = new LinkedList<>();
        for (int i = 0; i < engPackingOrderSummaryImports.size(); i++) {
            EngPackingOrderSummaryImport engPackingOrderSummaryImport = engPackingOrderSummaryImports.get(i);

            //判断非空
            String cartonCode = engPackingOrderSummaryImport.getCartonCode();
            String purchaseReqOrderCode = engPackingOrderSummaryImport.getPurchaseReqOrderCode();
            String contractCode = engPackingOrderSummaryImport.getContractCode();
            String materialName = engPackingOrderSummaryImport.getMaterialName();
            if (StringUtils.isEmpty(
                    cartonCode,purchaseReqOrderCode,materialName,contractCode
            )){
                throw new BizErrorException("添加失败，箱号、请购单号、合同号和货物名称不能为空,"+"错误行数为:"+(i+2));
               /* fail.add(i+2);
                continue;*/
            }

            //判断集合中是否已经存在同样的数据
            boolean tag = false;
            if (StringUtils.isNotEmpty(list)){
                for (EngPackingOrderSummary engPackingOrderSummary : list) {
                    if (engPackingOrderSummary.getCartonCode().equals(cartonCode)){
                        tag = true;
                    }
                }
            }
            if (tag){
                fail.add(i+2);
                continue;
            }

            //判断各参数是否大于0
            BigDecimal netWeight = engPackingOrderSummaryImport.getNetWeight();
            BigDecimal grossWeight = engPackingOrderSummaryImport.getGrossWeight();
            BigDecimal length = engPackingOrderSummaryImport.getLength();
            BigDecimal width = engPackingOrderSummaryImport.getWidth();
            BigDecimal height = engPackingOrderSummaryImport.getHeight();
            BigDecimal volume = engPackingOrderSummaryImport.getVolume();
            if(netWeight.compareTo(BigDecimal.ZERO)<0 || grossWeight.compareTo(BigDecimal.ZERO)<0 || length.compareTo(BigDecimal.ZERO)<0
                    ||width.compareTo(BigDecimal.ZERO)<0 || height.compareTo(BigDecimal.ZERO)<0 || volume.compareTo(BigDecimal.ZERO)<0 ){
                throw new BizErrorException("添加失败，长宽高、毛重、净重等参数必须大于0,"+"错误行数为:"+(i+2));
               /* fail.add(i+2);
                continue;*/
            }
            if(grossWeight.subtract(netWeight).compareTo(BigDecimal.ZERO)<=0){
                throw new BizErrorException("添加失败，毛重不能小于净重,"+"错误行数为:"+(i+2));
            }


            //判断编码是否重复
            Example example = new Example(EngPackingOrderSummary.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("orgId", user.getOrganizationId());
            criteria.andEqualTo("cartonCode",cartonCode);
            criteria.andEqualTo("packingOrderId",packingOrderId);
            if (StringUtils.isNotEmpty(engPackingOrderSummaryMapper.selectOneByExample(example))){
                throw new BizErrorException("添加失败，编码重复,"+"错误行数为:"+(i+2));
                /*fail.add(i+2);
                continue;*/
            }
            example.clear();

            EngPackingOrderSummaryDto dto = new EngPackingOrderSummaryDto();
            BeanUtils.copyProperties(engPackingOrderSummaryImport, dto);

            EngPackingOrder engPackingOrder = engPackingOrderMapper.selectByPrimaryKey(packingOrderId);


            //查询合同量单，获取专业
            Example qtyExample = new Example(EngContractQtyOrder.class);
            Example.Criteria qtyCriteria = qtyExample.createCriteria();
            qtyCriteria.andEqualTo("supplierId",engPackingOrder.getSupplierId());
            qtyCriteria.andEqualTo("contractCode",contractCode);
            List<EngContractQtyOrder> engContractQtyOrders = engContractQtyOrderMapper.selectByExample(qtyExample);
            if(StringUtils.isEmpty(engContractQtyOrders)){
                throw new BizErrorException("添加失败，供应商与合同号未匹配到合同量单,"+"错误行数为:"+(i+2));
               /* fail.add(i+2);
                continue;*/
            }else{
                dto.setProfessionName(engContractQtyOrders.get(0).getProfessionName());
            }

            //校验请购单
            Example orderExample = new Example(EngPurchaseReqOrder.class);
            Example.Criteria orderCriteria = orderExample.createCriteria();
            orderCriteria.andEqualTo("purchaseReqOrderCode",engPackingOrderSummaryImport.getPurchaseReqOrderCode());
            orderCriteria.andEqualTo("option3",engContractQtyOrders.get(0).getOption3());
            List<EngPurchaseReqOrder> engPurchaseReqOrders = engPurchaseReqOrderMapper.selectByExample(orderExample);
            if(StringUtils.isEmpty(engPurchaseReqOrders)){
                throw new BizErrorException("添加失败，合同量单与请购单号未匹配到请购单,"+"错误行数为:"+(i+2));
               /* fail.add(i+2);
                continue;*/
            }

            if(StringUtils.isEmpty(dto.getCartonQty()))
                dto.setCartonQty(1);
            dto.setPackingOrderId(packingOrderId);
            dto.setCreateTime(new Date());
            dto.setCreateUserId(user.getUserId());
            dto.setModifiedTime(new Date());
            dto.setModifiedUserId(user.getUserId());
            dto.setStatus((byte)1);
            dto.setOrgId(user.getOrganizationId());
            list.add(dto);
        }

        if (StringUtils.isNotEmpty(list)){
            success = engPackingOrderSummaryMapper.insertList(list);
        }

        for (EngPackingOrderSummary engPackingOrderSummary : list) {
            EngHtPackingOrderSummary engHtPackingOrderSummary = new EngHtPackingOrderSummary();
            BeanUtils.copyProperties(engPackingOrderSummary, engHtPackingOrderSummary);
            htList.add(engHtPackingOrderSummary);
        }
        if (StringUtils.isNotEmpty(htList)){
            engHtPackingOrderSummaryMapper.insertList(htList);
        }
        resutlMap.put("操作成功总数",success);
        resutlMap.put("操作失败行数",fail);
        return resutlMap;
    }


    public SysUser getUser(){
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        return currentUser;
    }


    public EngPackingOrder getEngPackingOrder(Long userId, String code, Long id){
        Example example = new Example(EngPackingOrder.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("orgId", userId);
        if(StringUtils.isNotEmpty(code))
            criteria.andEqualTo("packingOrderCode", code);
        if(StringUtils.isNotEmpty(id))
            criteria.andEqualTo("packingOrderId", id);
        EngPackingOrder engPackingOrder = engPackingOrderMapper.selectOneByExample(example);
        return engPackingOrder;
    }

    public void check(EngPackingOrderSummaryDto dto, SysUser user,EngPackingOrder engPackingOrder){
        if (StringUtils.isEmpty(dto.getCartonCode()))
            throw new BizErrorException("添加失败，包装箱号不能为空");
        if (StringUtils.isEmpty(dto.getPurchaseReqOrderCode()))
            throw new BizErrorException("添加失败，请购单号不能为空");
        if (StringUtils.isEmpty(dto.getContractCode()))
            throw new BizErrorException("添加失败，合同号不能为空");
        if (StringUtils.isEmpty(dto.getMaterialName()))
            throw new BizErrorException("添加失败，货物名称不能为空");

        //查询合同量单，获取专业
        Example qtyExample = new Example(EngContractQtyOrder.class);
        Example.Criteria qtyCriteria = qtyExample.createCriteria();
        qtyCriteria.andEqualTo("contractCode",dto.getContractCode());
        qtyCriteria.andEqualTo("supplierId",engPackingOrder.getSupplierId());
        List<EngContractQtyOrder> engContractQtyOrders = engContractQtyOrderMapper.selectByExample(qtyExample);
        if(StringUtils.isEmpty(engContractQtyOrders))
            throw new BizErrorException("添加失败，供应商与合同号未匹配到合同量单");

        //校验请购单
        Example orderExample = new Example(EngPurchaseReqOrder.class);
        Example.Criteria orderCriteria = orderExample.createCriteria();
        orderCriteria.andEqualTo("purchaseReqOrderCode",dto.getPurchaseReqOrderCode());
        List<EngPurchaseReqOrder> engPurchaseReqOrders = engPurchaseReqOrderMapper.selectByExample(orderExample);
        if(StringUtils.isEmpty(engPurchaseReqOrders))
            throw new BizErrorException("添加失败，合同量单与请购单号未匹配到请购单");

        //判断各参数是否大于0
        BigDecimal netWeight = dto.getNetWeight();
        BigDecimal grossWeight = dto.getGrossWeight();
        BigDecimal length = dto.getLength();
        BigDecimal width = dto.getWidth();
        BigDecimal height = dto.getHeight();
        BigDecimal volume = dto.getVolume();
        if(netWeight.compareTo(BigDecimal.ZERO)<0 || grossWeight.compareTo(BigDecimal.ZERO)<0 || length.compareTo(BigDecimal.ZERO)<0
                ||width.compareTo(BigDecimal.ZERO)<0 || height.compareTo(BigDecimal.ZERO)<0 || volume.compareTo(BigDecimal.ZERO)<0 ){
            throw new BizErrorException("添加失败，长宽高等参数必须大于0");
        }
        if(grossWeight.subtract(netWeight).compareTo(BigDecimal.ZERO)<=0){
            throw new BizErrorException("添加失败，毛重不能小于净重,"+"错误行数为:");
        }
    }

}
