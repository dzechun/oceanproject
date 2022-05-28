package com.fantechs.provider.chinafiveringapi.api.service.impl;

import com.fantechs.common.base.dto.security.SysRoleDto;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.entity.security.search.SearchSysRole;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseExecuteResultDto;
import com.fantechs.common.base.general.dto.basic.BaseOrganizationDto;
import com.fantechs.common.base.general.dto.basic.BaseWarehouseAreaDto;
import com.fantechs.common.base.general.dto.basic.BaseWorkingAreaDto;
import com.fantechs.common.base.general.dto.wms.out.WmsOutDeliveryOrderDetDto;
import com.fantechs.common.base.general.dto.wms.out.WmsOutDeliveryOrderTempDto;
import com.fantechs.common.base.general.entity.basic.BaseMaterial;
import com.fantechs.common.base.general.entity.basic.BaseStorage;
import com.fantechs.common.base.general.entity.basic.BaseSupplier;
import com.fantechs.common.base.general.entity.basic.BaseSupplierReUser;
import com.fantechs.common.base.general.entity.basic.search.*;
import com.fantechs.common.base.general.entity.eng.EngContractQtyOrder;
import com.fantechs.common.base.general.entity.eng.EngPurchaseReqOrder;
import com.fantechs.common.base.general.entity.wms.out.WmsOutDeliveryOrder;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.BeanUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.api.guest.eng.EngFeignApi;
import com.fantechs.provider.api.auth.service.AuthFeignApi;
import com.fantechs.provider.api.wms.out.OutFeignApi;
import com.fantechs.provider.chinafiveringapi.api.service.ImportDataService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ImportDataServiceImpl implements ImportDataService {

    // Webservice接口地址(测试环境)
    private final String address = "http://mattest.cwcec.com/LocWebServices/WebService1.asmx";
    //Webservice接口地址(生产环境)
    //private final String address = "http://mat.cwcec.com/LocWebServices/WebService1.asmx";

    @Resource
    BaseFeignApi baseFeignApi;
    @Resource
    OutFeignApi outFeignApi;
    @Resource
    EngFeignApi engFeignApi;
    @Resource
    AuthFeignApi securityFeignApi;

    @Override
    /**
     * create by: Dylan
     * description: 合同量单接口
     * create time:
     * @return
     */
    public BaseExecuteResultDto getPoDetails(String projectID) throws Exception {
//        WebService1HttpGet webService1HttpGet=null;
//        // 代理工厂
//        JaxWsProxyFactoryBean jaxWsProxyFactoryBean = new JaxWsProxyFactoryBean();
//        // 设置代理地址
//        jaxWsProxyFactoryBean.setAddress(address);
//        // 设置接口类型
//        jaxWsProxyFactoryBean.setServiceClass(WebService1HttpGet.class);
//        // 创建一个代理接口实现
//        webService1HttpGet = (WebService1HttpGet) jaxWsProxyFactoryBean.create();
//        return webService1HttpGet.getPoDetails(projectID.toString());

        BaseExecuteResultDto baseExecuteResultDto=new BaseExecuteResultDto();
        try {
            ResponseEntity<List<BaseOrganizationDto>> responseEntityOrg=this.getOrId();
            if(StringUtils.isEmpty(responseEntityOrg.getData())){
                throw new BizErrorException("找不到相应组织");
            }
            Long orgId=responseEntityOrg.getData().get(0).getOrganizationId();

            baseExecuteResultDto= callWebService(address,"getPoDetails",projectID);
            if(baseExecuteResultDto.getIsSuccess()==false)
                throw new BizErrorException(baseExecuteResultDto.getFailMsg());

            //转换为实体类集合
            String strResult=baseExecuteResultDto.getExecuteResult().toString();
            String s0=strResult.replaceAll("材料用途","materialPurpose");
            String s1=s0.replaceAll("合同号","contractCode");
            String s2=s1.replaceAll("材料编码","materialCode");
            String s3=s2.replaceAll("位号","locationNum");
            String s4=s3.replaceAll("采购量","purQty");
            String s5=s4.replaceAll("备注","remark");
            String s6=s5.replaceAll("装置号","deviceCode");
            String s7=s6.replaceAll("主项号","dominantTermCode");
            String s8=s7.replaceAll("PPGUID","option1");//合同明细id
            String s9=s8.replaceAll("PSGUID","option2");//合同头id
            String s10=s9.replaceAll("RDGUID","option3");//请购单明细ID
            String s11=s10.replaceAll("专业","professionName");
            String s12=s11.replaceAll("企业中文名称","professionCode");

            //同步到数据库
            int indexb=s12.indexOf("[");
            int indexe=s12.lastIndexOf("]");
            String str=s12.substring(indexb,indexe+1);
            List<EngContractQtyOrder> listPO= BeanUtils.jsonToListObject(str,EngContractQtyOrder.class);
            for (EngContractQtyOrder engContractQtyOrder : listPO) {
                //通过供应商名称找供应商ID
                SearchBaseSupplier searchBaseSupplier=new SearchBaseSupplier();
                searchBaseSupplier.setOrganizationId(orgId);
                searchBaseSupplier.setSupplierName(engContractQtyOrder.getProfessionCode());
                ResponseEntity<List<BaseSupplier>> responseEntityL=baseFeignApi.findSupplierList(searchBaseSupplier);
                if(StringUtils.isNotEmpty(responseEntityL.getData())){
                    engContractQtyOrder.setSupplierId(responseEntityL.getData().get(0).getSupplierId());
                }
                engContractQtyOrder.setProfessionCode("");
                engContractQtyOrder.setOrgId(orgId);
                engFeignApi.saveByApi(engContractQtyOrder);
            }

            baseExecuteResultDto.setExecuteResult("");
            baseExecuteResultDto.setIsSuccess(true);
            baseExecuteResultDto.setSuccessMsg("操作成功");

        }catch (Exception ex){
            baseExecuteResultDto.setIsSuccess(false);
            baseExecuteResultDto.setFailMsg(ex.getMessage());
        }

        return baseExecuteResultDto;
    }

    @Override
    /**
     * create by: Dylan
     * description: 领料单接口
     * create time:
     * @return
     */
    public BaseExecuteResultDto getIssueDetails(String projectID) throws Exception{
        BaseExecuteResultDto baseExecuteResultDto=new BaseExecuteResultDto();
        try{
            ResponseEntity<List<BaseOrganizationDto>> responseEntityOrg=this.getOrId();
            if(StringUtils.isEmpty(responseEntityOrg.getData())){
                throw new BizErrorException("找不到相应组织");
            }
            Long orgId=responseEntityOrg.getData().get(0).getOrganizationId();

            baseExecuteResultDto= callWebService(address,"getIssueDetails",projectID);
            if(baseExecuteResultDto.getIsSuccess()==false)
                throw new BizErrorException(baseExecuteResultDto.getFailMsg());

            String strResult=baseExecuteResultDto.getExecuteResult().toString();
            String s0=strResult.replaceAll("领料单号","deliveryOrderCode");
            String s1=s0.replaceAll("领料时间","outMaterialTime");
            String s2=s1.replaceAll("领料单位","customerCode");
            String s3=s2.replaceAll("领料截止时间","outMaterialTimeEnd");
            String s4=s3.replaceAll("项目ID","projectId");
            String s5=s4.replaceAll("领料单审批状态代码","confirmCode");
            String s6=s5.replaceAll("材料编码","materialCode");
            String s7=s6.replaceAll("发料备注","remark");
            String s8=s7.replaceAll("IDGUID","option1");
            String s9=s8.replaceAll("ISGUID","option2");
            String s10=s9.replaceAll("PLGUID","option3");
            String s11=s10.replaceAll("PSGUID","option4");
            String s12=s11.replaceAll("DHGUID","option5");
            String s13=s12.replaceAll("专业","option6");
            String s14=s13.replaceAll("位号","option7");
            String s15=s14.replaceAll("主项号","option8");
            String s16=s15.replaceAll("装置号","option9");
            String s17=s16.replaceAll("申领量","option10");
            String s18=s17.replaceAll("实发量","option11");
            String s19=s18.replaceAll("批准量","dispatchQty");

            String s20=s19.replaceAll("领料人","pickMaterialUserName");
            String s21=s20.replaceAll("审批人","auditUserName");
            String s22=s21.replaceAll("审批时间","auditTime");
            String s23=s22.replaceAll("管线号","pipelineNumber");

            //同步到数据库
            int indexb=s23.indexOf("[");
            int indexe=s23.lastIndexOf("]");
            String str=s23.substring(indexb,indexe+1);
            List<WmsOutDeliveryOrderTempDto> listWmsOut= BeanUtils.jsonToListObject(str,WmsOutDeliveryOrderTempDto.class);

            //按照领料单号分组
            Map<String, List<WmsOutDeliveryOrderTempDto>> result = listWmsOut.stream().collect(Collectors.groupingBy(c -> {
                String deliveryOrderCode = c.getDeliveryOrderCode();
                return deliveryOrderCode;
            }, TreeMap::new, Collectors.toList()));

            System.out.println(result);

            //循环分组数据 组成领料单
            for (String key:result.keySet()) {
                WmsOutDeliveryOrder wmsOutDeliveryOrder=new WmsOutDeliveryOrder();
                wmsOutDeliveryOrder.setOrderTypeId(8L);//单据类型 领料出库=8
                wmsOutDeliveryOrder.setOrgId(orgId);
                wmsOutDeliveryOrder.setMaterialOwnerId(153L);
                wmsOutDeliveryOrder.setOrderDate(new Date());
                wmsOutDeliveryOrder.setStatus((byte) 1);
                wmsOutDeliveryOrder.setOption1("1");//领料类型
                wmsOutDeliveryOrder.setOrderStatus((byte)1);//单据状态

                Long customerId=0L;
                List<WmsOutDeliveryOrderTempDto> listDto=result.get(key);
                List<WmsOutDeliveryOrderDetDto> wmsOutDeliveryOrderDetList= new ArrayList<>();
                for (WmsOutDeliveryOrderTempDto tempDto : listDto) {
                    //表头 领料单号
                    wmsOutDeliveryOrder.setDeliveryOrderCode(tempDto.getDeliveryOrderCode());
                    // 表头 项目ID
                    wmsOutDeliveryOrder.setOption3(tempDto.getProjectId());
                    // 表头 领料单审批状态代码
                    wmsOutDeliveryOrder.setOption4(tempDto.getConfirmCode());
                    // 表头 领料时间
                    wmsOutDeliveryOrder.setOption5(tempDto.getOutMaterialTime());
                    // 表头 领料截止时间
                    wmsOutDeliveryOrder.setOption6(tempDto.getOutMaterialTimeEnd());

                    // 表头 领料人
                    wmsOutDeliveryOrder.setPickMaterialUserName(tempDto.getPickMaterialUserName());
                    // 表头 审批人
                    wmsOutDeliveryOrder.setAuditUserName(tempDto.getAuditUserName());
                    // 表头 审批时间
                    wmsOutDeliveryOrder.setAuditTime(tempDto.getAuditTime());

                    if(customerId==0L) {
                        SearchBaseSupplier searchBaseSupplier = new SearchBaseSupplier();
                        searchBaseSupplier.setSupplierCode(tempDto.getCustomerCode());
                        searchBaseSupplier.setSupplierType((byte) 2);// 1 是供应商 2 是客户
                        searchBaseSupplier.setOrganizationId(orgId);
                        ResponseEntity<List<BaseSupplier>> supplierList = baseFeignApi.findSupplierList(searchBaseSupplier);
                        if (StringUtils.isNotEmpty(supplierList.getData())) {
                            customerId = supplierList.getData().get(0).getSupplierId();
                        }
                    }
                    // 表头 客户ID
                    wmsOutDeliveryOrder.setCustomerId(customerId);

                    //明细 取物料ID
                    WmsOutDeliveryOrderDetDto detDto=new WmsOutDeliveryOrderDetDto();
                    String materialCode=tempDto.getMaterialCode();
                    SearchBaseMaterial searchBaseMaterial=new SearchBaseMaterial();
                    searchBaseMaterial.setMaterialCode(materialCode);
                    searchBaseMaterial.setOrganizationId(orgId);
                    ResponseEntity<List<BaseMaterial>> baseList=baseFeignApi.findList(searchBaseMaterial);
                    if(StringUtils.isEmpty(baseList.getData())){
                        //throw new BizErrorException("找不到物料编码的信息-->"+materialCode);
                        continue;
                    }
                    //设置物料ID
                    detDto.setMaterialId(baseList.getData().get(0).getMaterialId());
                    //IDGUID
                    detDto.setOption1(tempDto.getOption1());
                    //ISGUID
                    detDto.setOption2(tempDto.getOption2());
                    //PLGUID
                    detDto.setOption3(tempDto.getOption3());
                    //PSGUID
                    detDto.setOption4(tempDto.getOption4());
                    //DHGUID
                    detDto.setOption5(tempDto.getOption5());
                    //专业
                    detDto.setOption6(tempDto.getOption6());
                    //位号
                    detDto.setOption7(tempDto.getOption7());
                    //主项号
                    detDto.setOption8(tempDto.getOption8());
                    //装置号
                    detDto.setOption9(tempDto.getOption9());
                    //申领量
                    detDto.setOption10(tempDto.getOption10());
                    //实发量
                    detDto.setOption11(tempDto.getOption11());
                    //备注
                    detDto.setRemark(tempDto.getRemark());
                    //管线号
                    detDto.setPipelineNumber(tempDto.getPipelineNumber());

                    //批准量 保存到 包装数量 packingQty
                    if(StringUtils.isEmpty(tempDto.getDispatchQty()))
                        detDto.setPackingQty(BigDecimal.ZERO);
                    else
                        detDto.setPackingQty(new BigDecimal(tempDto.getDispatchQty()));

                    //拣货数量默认0
                    detDto.setPickingQty(new BigDecimal(0));
                    //包装单位 packing_unit_name option2
                    detDto.setPackingUnitName(baseList.getData().get(0).getOption2());
                    //发货库位和发货仓库 库位类型为发货的 storageType=3
                    SearchBaseStorage searchBaseStorage=new SearchBaseStorage();
                    //searchBaseStorage.setStorageCode("default");
                    //storageType
                    searchBaseStorage.setStorageType((byte)3);
                    searchBaseStorage.setOrgId(orgId);
                    ResponseEntity<List<BaseStorage>> listStorage=baseFeignApi.findList(searchBaseStorage);
                    if(StringUtils.isNotEmpty(listStorage.getData())){
                        detDto.setStorageId(listStorage.getData().get(0).getStorageId());
                        detDto.setWarehouseId(listStorage.getData().get(0).getWarehouseId());
                    }
                    //拣货库位 拣货库位为“DHGUID”的库位 库位信息option1栏位存DHGUID
                    //searchBaseStorage.setStorageCode("");
                    SearchBaseStorage searchBaseStoragePick=new SearchBaseStorage();
                    searchBaseStoragePick.setOrgId(orgId);
                    searchBaseStoragePick.setOption1(tempDto.getOption5());
                    listStorage=baseFeignApi.findList(searchBaseStoragePick);
                    if(StringUtils.isNotEmpty(listStorage.getData())){
                        detDto.setPickingStorageId(listStorage.getData().get(0).getStorageId());
                    }

                    wmsOutDeliveryOrderDetList.add(detDto);
                }

                //设置领料单明细
                wmsOutDeliveryOrder.setWmsOutDeliveryOrderDetList(wmsOutDeliveryOrderDetList);

                //同步到数据库
                outFeignApi.saveByApi(wmsOutDeliveryOrder);

            }


            baseExecuteResultDto.setExecuteResult("");
            baseExecuteResultDto.setIsSuccess(true);
            baseExecuteResultDto.setSuccessMsg("操作成功");
        }catch (Exception ex){
            baseExecuteResultDto.setIsSuccess(false);
            baseExecuteResultDto.setFailMsg(ex.getMessage());
        }


        return baseExecuteResultDto;
    }

    @Override
    /**
     * create by: Dylan
     * description: 材料信息接口
     * create time:
     * @return
     */
    public BaseExecuteResultDto getPartNoInfo(String projectID) throws Exception{
        BaseExecuteResultDto baseExecuteResultDto=new BaseExecuteResultDto();
        try {
            ResponseEntity<List<BaseOrganizationDto>> responseEntityOrg=this.getOrId();
            if(StringUtils.isEmpty(responseEntityOrg.getData())){
                throw new BizErrorException("找不到相应组织");
            }
            Long orgId=responseEntityOrg.getData().get(0).getOrganizationId();

            baseExecuteResultDto = callWebService(address, "getPartNoInfo", projectID);
            if (baseExecuteResultDto.getIsSuccess() == false)
                throw new BizErrorException(baseExecuteResultDto.getFailMsg());

            //转换为实体类集合
            String strResult=baseExecuteResultDto.getExecuteResult().toString();
            String s0=strResult.replaceAll("项目ID","systemSource");
            String s1=s0.replaceAll("材料编码","materialCode");
            String s2=s1.replaceAll("位号","option1");
            String s3=s2.replaceAll("材料名称","materialName");
            String s4=s3.replaceAll("规格描述","materialDesc");
            String s5=s4.replaceAll("计量单位","option2");

            //同步到数据库
            int indexb=s5.indexOf("[");
            int indexe=s5.lastIndexOf("]");
            String str=s5.substring(indexb,indexe+1);
            List<BaseMaterial> listBM= BeanUtils.jsonToListObject(str,BaseMaterial.class);
            for (BaseMaterial baseMaterial : listBM) {
                baseMaterial.setOrganizationId(orgId);
                baseMaterial.setStatus((byte)1);
                baseFeignApi.saveByApi(baseMaterial);
            }

            baseExecuteResultDto.setExecuteResult("");
            baseExecuteResultDto.setIsSuccess(true);
            baseExecuteResultDto.setSuccessMsg("操作成功");
        }catch (Exception ex){
            baseExecuteResultDto.setIsSuccess(false);
            baseExecuteResultDto.setFailMsg(ex.getMessage());
        }
        return baseExecuteResultDto;
    }

    @Override
    /**
     * create by: Dylan
     * description: 库位信息接口
     * create time:
     * @return
     */
    public BaseExecuteResultDto getShelvesNo(String projectID) throws Exception{
        BaseExecuteResultDto baseExecuteResultDto=new BaseExecuteResultDto();
        try {
            ResponseEntity<List<BaseOrganizationDto>> responseEntityOrg=this.getOrId();
            if(StringUtils.isEmpty(responseEntityOrg.getData())){
                throw new BizErrorException("找不到相应组织");
            }
            Long orgId=responseEntityOrg.getData().get(0).getOrganizationId();

            baseExecuteResultDto= callWebService(address,"getShelvesNo",projectID);
            if(baseExecuteResultDto.getIsSuccess()==false)
                throw new BizErrorException(baseExecuteResultDto.getFailMsg());

            //转换为实体类集合
            String strResult=baseExecuteResultDto.getExecuteResult().toString();
            String s0=strResult.replaceAll("货架编号描述","storageName");
            String s1=s0.replaceAll("货架编号","storageCode");
            String s2=s1.replaceAll("DHGUID","option1");

            //同步到数据库
            int indexb=s2.indexOf("[");
            int indexe=s2.lastIndexOf("]");
            String str=s2.substring(indexb,indexe+1);
            List<BaseStorage> listBC= BeanUtils.jsonToListObject(str,BaseStorage.class);

            //取工作区编码为default的ID
            SearchBaseWorkingArea searchBaseWorkingArea=new SearchBaseWorkingArea();
            searchBaseWorkingArea.setWorkingAreaCode("default");
            searchBaseWorkingArea.setOrgId(orgId);
            ResponseEntity<List<BaseWorkingAreaDto>> responseEntityList=baseFeignApi.findWorkingAreaList(searchBaseWorkingArea);
            if(StringUtils.isEmpty(responseEntityList.getData())){
                throw new BizErrorException("default工作区编码不存在");
            }

            //取库区编码为default的ID base_warehouse_area
            SearchBaseWarehouseArea searchBaseWarehouseArea=new SearchBaseWarehouseArea();
            searchBaseWarehouseArea.setWarehouseAreaCode("default");
            searchBaseWarehouseArea.setOrgId(orgId);
            ResponseEntity<List<BaseWarehouseAreaDto>> responseEntityListWA=baseFeignApi.findWarehouseAreaList(searchBaseWarehouseArea);
            if(StringUtils.isEmpty(responseEntityListWA.getData())){
                throw new BizErrorException("default库区编码不存在");
            }

            for (BaseStorage baseStorage : listBC) {
                baseStorage.setOrgId(orgId);
                baseStorage.setStorageType((byte)1);//库位类型
                baseStorage.setWorkingAreaId(responseEntityList.getData().get(0).getWorkingAreaId());//工作区ID
                baseStorage.setRoadway(1);//项道
                baseStorage.setRowNo(1);//排
                baseStorage.setColumnNo(1);//列
                baseStorage.setLevelNo(1);//层
                baseStorage.setPutawayMoveLineNo(1);//上架动线号
                baseStorage.setPickingMoveLineNo(1);//拣货动线号
                baseStorage.setStockMoveLineNo(1);//盘点动线号
                baseStorage.setStatus(1);//状态默认1
                //库区ID
                baseStorage.setWarehouseAreaId(responseEntityListWA.getData().get(0).getWarehouseAreaId());
                //仓库
                baseStorage.setWarehouseId(responseEntityListWA.getData().get(0).getWarehouseId());

                baseFeignApi.saveByApi(baseStorage);
            }

            baseExecuteResultDto.setExecuteResult("");
            baseExecuteResultDto.setIsSuccess(true);
            baseExecuteResultDto.setSuccessMsg("操作成功");

        }catch (Exception ex){
            baseExecuteResultDto.setIsSuccess(false);
            baseExecuteResultDto.setFailMsg(ex.getMessage());
        }

        return baseExecuteResultDto;
    }

    @Override
    /**
     * create by: Dylan
     * description: 客户信息接口
     * create time:
     * @return
     */
    public BaseExecuteResultDto getSubcontractor(String projectID) throws Exception{
        BaseExecuteResultDto baseExecuteResultDto=new BaseExecuteResultDto();
        try {
            ResponseEntity<List<BaseOrganizationDto>> responseEntityOrg=this.getOrId();
            if(StringUtils.isEmpty(responseEntityOrg.getData())){
                throw new BizErrorException("找不到相应组织");
            }
            Long orgId=responseEntityOrg.getData().get(0).getOrganizationId();

            baseExecuteResultDto= callWebService(address,"getSubcontractor",projectID);
            if(baseExecuteResultDto.getIsSuccess()==false)
                throw new BizErrorException(baseExecuteResultDto.getFailMsg());

            //转换为实体类集合
            String strResult=baseExecuteResultDto.getExecuteResult().toString();
            String s0=strResult.replaceAll("分包商编号","supplierCode");
            String s1=s0.replaceAll("分包商名称","supplierName");

            //同步到数据库
            int indexb=s1.indexOf("[");
            int indexe=s1.lastIndexOf("]");
            String str=s1.substring(indexb,indexe+1);
            List<BaseSupplier> listBC= BeanUtils.jsonToListObject(str,BaseSupplier.class);
            for (BaseSupplier baseSupplier : listBC) {
                baseSupplier.setOrganizationId(orgId);
                baseSupplier.setStatus((byte)1);
                //客户供应商用同一个表 SupplierType=2 表示客户
                baseSupplier.setSupplierType((byte)2);
                baseFeignApi.saveByApi(baseSupplier);
            }

            baseExecuteResultDto.setExecuteResult("");
            baseExecuteResultDto.setIsSuccess(true);
            baseExecuteResultDto.setSuccessMsg("操作成功");

        }catch (Exception ex){
            baseExecuteResultDto.setIsSuccess(false);
            baseExecuteResultDto.setFailMsg(ex.getMessage());
        }

        return baseExecuteResultDto;
    }

    @Override
    /**
     * create by: Dylan
     * description: 供应商信息接口
     * create time:
     * @return
     */
    public BaseExecuteResultDto getVendor() throws Exception{
        BaseExecuteResultDto baseExecuteResultDto = new BaseExecuteResultDto();
        try {
            ResponseEntity<List<BaseOrganizationDto>> responseEntityOrg=this.getOrId();
            if(StringUtils.isEmpty(responseEntityOrg.getData())){
                throw new BizErrorException("找不到相应组织");
            }
            Long orgId=responseEntityOrg.getData().get(0).getOrganizationId();

            baseExecuteResultDto = callWebService(address, "getVendor", "");
            if (baseExecuteResultDto.getIsSuccess() == false)
                throw new BizErrorException(baseExecuteResultDto.getFailMsg());

            String strResult = baseExecuteResultDto.getExecuteResult().toString();
            String s0 = strResult.replaceAll("企业中文名称", "supplierName");
            String s1 = s0.replaceAll("厂商企标ID", "supplierCode");

            int indexb = s1.indexOf("[");
            int indexe = s1.lastIndexOf("]");
            String str = s1.substring(indexb, indexe + 1);
            List<BaseSupplier> listSu = BeanUtils.jsonToListObject(str, BaseSupplier.class);

            //同步到数据库
            for (BaseSupplier baseSupplier : listSu) {
                baseSupplier.setOrganizationId(orgId);
                baseSupplier.setStatus((byte)1);
                //客户供应商用同一个表 SupplierType=1 表示供应商
                baseSupplier.setSupplierType((byte)1);
                baseFeignApi.saveByApi(baseSupplier);
            }

            baseExecuteResultDto.setExecuteResult("");
            baseExecuteResultDto.setIsSuccess(true);
            baseExecuteResultDto.setSuccessMsg("操作成功");

        }catch (Exception ex){
            baseExecuteResultDto.setIsSuccess(false);
            baseExecuteResultDto.setFailMsg(ex.getMessage());
        }

        return baseExecuteResultDto;
    }

    @Override
    /**
     * create by: Dylan
     * description: 供应商用户接口
     * create time:
     * @return
     */
    public BaseExecuteResultDto getVendorUserNameAndPwd() throws Exception {
        BaseExecuteResultDto baseExecuteResultDto = new BaseExecuteResultDto();
        try {
            ResponseEntity<List<BaseOrganizationDto>> responseEntityOrg=this.getOrId();
            if(StringUtils.isEmpty(responseEntityOrg.getData())){
                throw new BizErrorException("找不到相应组织");
            }
            Long orgId=responseEntityOrg.getData().get(0).getOrganizationId();

            baseExecuteResultDto = callWebService(address, "getVendorUserNameAndPwd", "");
            if (baseExecuteResultDto.getIsSuccess() == false)
                throw new BizErrorException(baseExecuteResultDto.getFailMsg());

            String strResult = baseExecuteResultDto.getExecuteResult().toString();
            String s0 = strResult.replaceAll("供应商名称", "userName");
            String s1 = s0.replaceAll("账号", "userCode");
            String s2 = s1.replaceAll("密码", "password");

            int indexb = s2.indexOf("[");
            int indexe = s2.lastIndexOf("]");
            String str = s2.substring(indexb, indexe + 1);
            List<SysUser> listUser = BeanUtils.jsonToListObject(str, SysUser.class);

            //登录
            //securityFeignApi.login("admin","123456",1004L,null);

            //获取供应商角色ID
            Long roleId=null;
            SearchSysRole searchSysRole=new SearchSysRole();
            searchSysRole.setRoleCode("SROLE");
            ResponseEntity<List<SysRoleDto>> responseEntityRoles= securityFeignApi.selectRoles(searchSysRole);
            if(StringUtils.isNotEmpty(responseEntityRoles.getData())){
                roleId=responseEntityRoles.getData().get(0).getRoleId();
            }

            //同步到数据库
            for (SysUser user : listUser) {
                user.setOrganizationId(orgId);
                user.setStatus((byte)1);
                user.setRoleId(roleId);
                if(StringUtils.isEmpty(user.getUserName())){
                    user.setUserName(user.getUserCode());
                }
                if(StringUtils.isEmpty(user.getNickName())){
                    user.setNickName(user.getUserName());
                }
                ResponseEntity<SysUser> responseEntityUser=securityFeignApi.saveByApi(user);
                if(StringUtils.isNotEmpty(responseEntityUser.getData())){
                    //用户ID
                    Long userId=responseEntityUser.getData().getUserId();
                    if(StringUtils.isNotEmpty(userId)) {
                        //找供应商ID
                        if (StringUtils.isNotEmpty(user.getUserName())) {
                            Long supplierId = null;
                            SearchBaseSupplier searchBaseSupplier = new SearchBaseSupplier();
                            searchBaseSupplier.setSupplierName(user.getUserName());
                            searchBaseSupplier.setOrganizationId(orgId);
                            ResponseEntity<List<BaseSupplier>> responseEntitySupplier = baseFeignApi.findSupplierList(searchBaseSupplier);
                            if (StringUtils.isNotEmpty(responseEntitySupplier.getData())) {
                                supplierId = responseEntitySupplier.getData().get(0).getSupplierId();
                            }
                            //增加用户绑定供应商
                            if(StringUtils.isNotEmpty(supplierId)) {
                                BaseSupplierReUser baseSupplierReUser = new BaseSupplierReUser();
                                baseSupplierReUser.setSupplierId(supplierId);
                                baseSupplierReUser.setUserId(userId);
                                baseSupplierReUser.setOrganizationId(orgId);
                                baseFeignApi.saveByApi(baseSupplierReUser);
                            }
                        }
                    }
                }
            }

            baseExecuteResultDto.setExecuteResult("");
            baseExecuteResultDto.setIsSuccess(true);
            baseExecuteResultDto.setSuccessMsg("操作成功");

        }catch (Exception ex){
            baseExecuteResultDto.setIsSuccess(false);
            baseExecuteResultDto.setFailMsg(ex.getMessage());
        }

        return baseExecuteResultDto;
    }

    @Override
    /**
     * create by: Dylan
     * description: 请购单信息接口
     * create time:
     * @return
     */
    public BaseExecuteResultDto getReqDetails(String projectID) throws Exception{
        BaseExecuteResultDto baseExecuteResultDto=new BaseExecuteResultDto();
        try{
            ResponseEntity<List<BaseOrganizationDto>> responseEntityOrg=this.getOrId();
            if(StringUtils.isEmpty(responseEntityOrg.getData())){
                throw new BizErrorException("找不到相应组织");
            }
            Long orgId=responseEntityOrg.getData().get(0).getOrganizationId();

            baseExecuteResultDto= callWebService(address,"getReqDetails",projectID);
            if(baseExecuteResultDto.getIsSuccess()==false)
                throw new BizErrorException(baseExecuteResultDto.getFailMsg());

            //转换为实体类集合
            String strResult=baseExecuteResultDto.getExecuteResult().toString();
            String s0=strResult.replaceAll("请购单号","purchaseReqOrderCode");
            String s1=s0.replaceAll("请购单名称","purchaseReqOrderName");
            String s2=s1.replaceAll("专业名称","remark");
            String s3=s2.replaceAll("材料等级","materialGrade");
            String s4=s3.replaceAll("材料编码","materialCode");
            String s5=s4.replaceAll("位号","locationNum");
            String s6=s5.replaceAll("设计量","designQty");
            String s7=s6.replaceAll("余量","surplusQty");
            String s8=s7.replaceAll("请购量","purchaseReqQty");
            String s9=s8.replaceAll("请购说明","purchaseReqExplain");
            String s10=s9.replaceAll("装置号","deviceCode");
            String s11=s10.replaceAll("主项号","dominantTermCode");
            String s12=s11.replaceAll("材料用途","materialPurpose");
            String s13=s12.replaceAll("采购说明","purchaseExplain");
            String s14=s13.replaceAll("RSGUID","option2");
            String s15=s14.replaceAll("RDGUID","option3");//请购单明细ID

            //同步到数据库
            int indexb=s15.indexOf("[");
            int indexe=s15.lastIndexOf("]");
            String str=s15.substring(indexb,indexe+1);
            List<EngPurchaseReqOrder> listPO= BeanUtils.jsonToListObject(str,EngPurchaseReqOrder.class);
            for (EngPurchaseReqOrder engPurchaseReqOrder : listPO) {
                engPurchaseReqOrder.setOrgId(orgId);
                engFeignApi.saveByApi(engPurchaseReqOrder);
            }

            baseExecuteResultDto.setExecuteResult("");
            baseExecuteResultDto.setIsSuccess(true);
            baseExecuteResultDto.setSuccessMsg("操作成功");

        }catch (Exception ex){
            baseExecuteResultDto.setIsSuccess(false);
            baseExecuteResultDto.setFailMsg(ex.getMessage());
        }

        return baseExecuteResultDto;
    }

    private  String actionBySOAP(String method,String projectID){
        String str="";
        if(StringUtils.isEmpty(projectID)){
            str="<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:tem=\"http://tempuri.org/\">\n" +
                    "   <soapenv:Header/>\n" +
                    "   <soapenv:Body>\n" +
                    "      <tem:"+method+"/>\n" +
                    "   </soapenv:Body>\n" +
                    "</soapenv:Envelope>";
        }
        else {
            str = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:tem=\"http://tempuri.org/\">\n" +
                    "   <soapenv:Header/>\n" +
                    "   <soapenv:Body>\n" +
                    "      <tem:" + method + ">\n" +
                    "         <tem:projectID>" + projectID + "</tem:projectID>\n" +
                    "      </tem:" + method + ">\n" +
                    "   </soapenv:Body>\n" +
                    "</soapenv:Envelope>";

        }

        return str;
    }

    private BaseExecuteResultDto callWebService(String wsdl, String method, String projectID) throws Exception {
        StringBuilder sb = new StringBuilder();
        BaseExecuteResultDto baseExecuteResultDto=new BaseExecuteResultDto();
        try {
            System.setProperty("sun.net.client.defaultConnectTimeout", "300000");
            System.setProperty("sun.net.client.defaultReadTimeout", "300000");

            // URL连接
            URL url = new URL(wsdl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            // conn.setRequestMethod("GET");
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Length", String.valueOf(actionBySOAP(method, projectID).getBytes().length));
            conn.setRequestProperty("Content-Type", "text/xml; charset=GBK");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setConnectTimeout(300000);
            // 请求输入内容
            OutputStream output = conn.getOutputStream();
            output.write(actionBySOAP(method, projectID).getBytes());
            output.flush();
            output.close();
            // 请求返回内容
            InputStreamReader isr = new InputStreamReader(conn.getInputStream());
            BufferedReader br = new BufferedReader(isr);

            String str = null;
            while ((str = br.readLine()) != null) {
                sb.append(str + "\n");
            }
            br.close();
            isr.close();

            baseExecuteResultDto.setIsSuccess(true);
            baseExecuteResultDto.setExecuteResult(sb.toString());

        }catch (Exception ex) {
            baseExecuteResultDto.setIsSuccess(false);
            baseExecuteResultDto.setFailMsg(ex.getMessage());
        }

        return baseExecuteResultDto;
    }


    /**
     * create by: Dylan
     * description: 获取组织
     * create time:
     * @return
     */
    public ResponseEntity<List<BaseOrganizationDto>> getOrId() {
        ResponseEntity<List<BaseOrganizationDto>> baseOrganizationDtoList=null;
        SearchBaseOrganization searchBaseOrganization = new SearchBaseOrganization();
        searchBaseOrganization.setOrganizationCode("3919");
        baseOrganizationDtoList = baseFeignApi.findOrganizationList(searchBaseOrganization);
        return baseOrganizationDtoList;
    }

}
