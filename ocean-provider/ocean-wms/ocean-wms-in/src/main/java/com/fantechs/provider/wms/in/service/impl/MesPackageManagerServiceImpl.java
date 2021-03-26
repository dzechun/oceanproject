package com.fantechs.provider.wms.in.service.impl;

import com.alibaba.fastjson.JSON;
import com.fantechs.common.base.dto.storage.MesPackageManagerInDTO;
import com.fantechs.common.base.dto.storage.SaveMesPackageManagerDTO;
import com.fantechs.common.base.entity.security.SysSpecItem;
import com.fantechs.common.base.entity.security.search.SearchSysSpecItem;
import com.fantechs.common.base.general.dto.bcm.PrintDto;
import com.fantechs.common.base.general.dto.bcm.PrintModel;
import com.fantechs.common.base.general.entity.mes.pm.SmtBarcodeRuleSpec;
import com.fantechs.common.base.entity.basic.history.MesHtPackageManager;
import com.fantechs.common.base.entity.storage.MesPackageManager;
import com.fantechs.common.base.dto.storage.MesPackageManagerDTO;
import com.fantechs.common.base.general.entity.mes.pm.SmtWorkOrder;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.BeanUtils;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.provider.api.fileserver.service.BcmFeignApi;
import com.fantechs.provider.api.mes.pm.PMFeignApi;
import com.fantechs.provider.api.security.service.SecurityFeignApi;
import com.fantechs.provider.wms.in.service.MesPackageManagerService;
import com.fantechs.provider.wms.in.mapper.MesPackageManagerMapper;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.wms.in.service.history.MesHtPackageManagerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import com.fantechs.common.base.utils.StringUtils;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Auther: bingo.ren
 * @Date: 2021年1月7日 16:38
 * @Description: 包装管理接口实现
 * @Version: 1.0
 */
@Service
public class MesPackageManagerServiceImpl extends BaseService<MesPackageManager>  implements MesPackageManagerService {

     @Resource
     private MesPackageManagerMapper mesPackageManagerMapper;
     @Resource
     private PMFeignApi applyFeignApi;
     @Resource
     private MesHtPackageManagerService mesHtPackageManagerService;
    @Resource
    private BcmFeignApi bcmFeignApi;

    @Override
    public List<MesPackageManager> selectAll(Map<String,Object> map) {
        Example example = new Example(MesPackageManager.class);
        Example.Criteria criteria = example.createCriteria();
        Example.Criteria criteria1 = example.createCriteria();
        criteria1.andEqualTo("isDelete",1).orIsNull("isDelete");
        example.and(criteria1);
        if(StringUtils.isNotEmpty(map)){
            map.forEach((k,v)->{
                if(StringUtils.isNotEmpty(v)){
                    switch (k){
                        case "Name":
                            criteria.andLike(k,"%"+v+"%");
                            break;
                        default :
                            criteria.andEqualTo(k,v);
                            break;
                    }
                }
            });
        }
        return mesPackageManagerMapper.selectByExample(example);
    }

    @Override
    public List<MesPackageManager> selectLikeAll(Map<String,Object> map) {
        Example example = new Example(MesPackageManager.class);
        Example.Criteria criteria = example.createCriteria();
        Example.Criteria criteria1 = example.createCriteria();
        criteria1.andEqualTo("isDelete",1).orIsNull("isDelete");
        example.and(criteria1);
        if(StringUtils.isNotEmpty(map)){
            map.forEach((k,v)->{
                if(StringUtils.isNotEmpty(v)){
                    criteria.orLike(k,"%"+v+"%");
                }
            });
        }
        return mesPackageManagerMapper.selectByExample(example);
    }

    @Override
    public MesPackageManager selectByKey(Object id) {
        MesPackageManager mesPackageManager = mesPackageManagerMapper.selectByPrimaryKey(id);
        if(mesPackageManager != null && (mesPackageManager.getIsDelete() != null && mesPackageManager.getIsDelete() == 0)){
        mesPackageManager = null;
        }
        return mesPackageManager;
    }

    @Override
    public MesPackageManager selectByMap(Map<String,Object> map) {
        List<MesPackageManager> mesPackageManager = selectAll(map);
        if(StringUtils.isEmpty(mesPackageManager)){
            return null;
        }
        if(mesPackageManager.size()>1){
            return null;
        }
        return mesPackageManager.get(0);
    }

    @Override
    public int save(MesPackageManager mesPackageManager) {
        mesPackageManager.setPackageManagerCode(CodeUtils.getId("PACKAGE"));
        mesPackageManager.setCreateUserId(null);
        mesPackageManager.setIsDelete((byte)1);
        if(mesPackageManagerMapper.insertSelective(mesPackageManager)<=0){
            throw new BizErrorException(ErrorCodeEnum.OPT20012006);
        }
        recordHistory(mesPackageManager,"新增");
        return 1;
    }

    @Override
    public int deleteByKey(Object id) {
        MesPackageManager mesPackageManager = new MesPackageManager();
        mesPackageManager.setPackageManagerId((long)id);
        mesPackageManager.setIsDelete((byte)0);
        return update(mesPackageManager);
    }

    @Override
    public int deleteByMap(Map<String,Object> map){
        List<MesPackageManager> mesPackageManagers = selectAll(map);
        if (StringUtils.isNotEmpty(mesPackageManagers)) {
            for (MesPackageManager mesPackageManager : mesPackageManagers) {
                if(deleteByKey(mesPackageManager.getPackageManagerId())<=0){
                    return 0;
                }
            }
        }
        return 1;
    }

    @Override
    public int update(MesPackageManager mesPackageManager) {
        mesPackageManager.setModifiedUserId(null);
        if(mesPackageManagerMapper.updateByPrimaryKeySelective(mesPackageManager)<=0){
            throw new BizErrorException(ErrorCodeEnum.OPT20012006);
        }
        recordHistory(mesPackageManager,"更新");
        return 1;
    }

    @Override
    public String selectUserName(Object id) {
        return mesPackageManagerMapper.selectUserName(id);
    }

   @Override
   public List<MesPackageManagerDTO> selectFilterAll(Map<String, Object> map) {
       return mesPackageManagerMapper.selectFilterAll(map);
   }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MesPackageManager saveChildren(SaveMesPackageManagerDTO saveMesPackageManagerDTO) {
        MesPackageManager mesPackageManager = saveMesPackageManagerDTO.getMesPackageManager();
        if(mesPackageManager.getType()==1){
            //查询剩余可打印数量
            int result = mesPackageManagerMapper.remainQty(mesPackageManager.getWorkOrderId(), mesPackageManager.getType());
            if(result==0){
                throw new BizErrorException("已全部包装完毕，不允许再打印");
            }else if(mesPackageManager.getTotal().intValue()>result){
                throw new BizErrorException("包装产品数量超过剩余待包装数量");
            }
        }
        this.printCode(mesPackageManager);
        if(this.save(mesPackageManager)<=0){
            return null;
        }
        List<MesPackageManager> mesPackageManagerList = saveMesPackageManagerDTO.getMesPackageManagerList();
        if(StringUtils.isNotEmpty(mesPackageManagerList)){
            double total=0.0;//包装箱打包的产品数量
            for (MesPackageManager packageManager : mesPackageManagerList) {
                packageManager.setParentId(mesPackageManager.getPackageManagerId());
                total+=packageManager.getTotal().doubleValue();
                if(this.update(packageManager)<=0){
                    throw new BizErrorException(ErrorCodeEnum.OPT20012006);
                }
            }
            ResponseEntity<Integer> responseEntity = applyFeignApi.finishedProduct(mesPackageManager.getWorkOrderId(), total);
            if(responseEntity.getCode()!=0){
                throw new BizErrorException(responseEntity.getMessage());
            }
        }
        //包箱修改工单打印状态
        if(mesPackageManager.getType()==(byte)1){
            mesPackageManagerMapper.updWorkOrderStatus(mesPackageManager.getWorkOrderId());
        }

        //调用打印程序进行条码打印
        try {
            PrintDto printDto = new PrintDto();
            if(mesPackageManager.getType()==(byte)1){
                printDto.setLabelName("包箱.btw");
            }else if(mesPackageManager.getType()==(byte)2){
                printDto.setLabelName("栈板.btw");
            }
            printDto.setPrintName("测试");
            PrintModel printModel = mesPackageManagerMapper.findPrintModel(mesPackageManager.getPackageManagerId());
            printModel.setQrCode(mesPackageManager.getBarCode());
            List<PrintModel> printModelList = new ArrayList<>();
            printModelList.add(printModel);
            printDto.setPrintModelList(printModelList);
            ResponseEntity res = bcmFeignApi.print(printDto);
            if(res.getCode()!=0){
                throw new BizErrorException("打印失败");
            }
        }catch (Exception e){
            throw new BizErrorException(e.getMessage());
        }

        return mesPackageManager;
    }

    @Override
    public int printCode(Long packageManagerId,String printName) {
        MesPackageManager mesPackageManager = this.selectByKey(packageManagerId);
        mesPackageManager.setPrintName(printName);
        this.printCode(mesPackageManager);
        if(this.update(mesPackageManager)<=0){
            return 0;
        }
        return 1;
    }

    @Override
    public MesPackageManagerInDTO findParentByBarcode(String barcode) {
        MesPackageManagerInDTO mesPackageManagerInDTO = new MesPackageManagerInDTO();
        List<MesPackageManagerDTO> mesPackageManagerDTOList = this.selectFilterAll(ControllerUtil.dynamicCondition("barcode",barcode));
        if(StringUtils.isEmpty(mesPackageManagerDTOList)){
            return null;
        }
        MesPackageManagerDTO mesPackageManagerDTO = mesPackageManagerDTOList.get(0);
        org.springframework.beans.BeanUtils.copyProperties(mesPackageManagerDTO,mesPackageManagerInDTO);
        if(mesPackageManagerDTO.getType() == 1){
            //扫描的包箱
            if(mesPackageManagerDTO.getParentId() != 0){
                MesPackageManager mesPackageManager = this.selectByKey(mesPackageManagerDTO.getParentId());
                mesPackageManagerInDTO.setPackageManagerCode(mesPackageManager.getPackageManagerCode());
                mesPackageManagerInDTO.setPackageManagerId(mesPackageManager.getPackageManagerId());
                mesPackageManagerInDTO.setBarCode(mesPackageManager.getBarCode());
                mesPackageManagerInDTO.setType(mesPackageManager.getType());
                mesPackageManagerInDTO.setBoxCount(mesPackageManager.getTotal());//设置父级绑定了多少子级
            }else{
                //没有绑定父级不返回数据
                return null;
            }
        }else if(mesPackageManagerDTO.getType() == 2){
            mesPackageManagerInDTO.setBoxCount(mesPackageManagerInDTO.getTotal());//设置父级绑定了多少子级
        }
        //查询所有的子级
        mesPackageManagerDTOList = this.selectFilterAll(ControllerUtil.dynamicCondition("parentId",mesPackageManagerInDTO.getPackageManagerId()));
        //=====设置子级的总数
        double total=0.0;
        for (MesPackageManagerDTO packageManagerDTO : mesPackageManagerDTOList) {
            total+=packageManagerDTO.getTotal().doubleValue();
        }
        mesPackageManagerInDTO.setTotal(new BigDecimal(total));
        //=====
        return mesPackageManagerInDTO;
    }

    private void printCode(MesPackageManager mesPackageManager){
        //根据包装规格获取条码规则，生成条码
        List<SmtBarcodeRuleSpec> smtBarcodeRuleSpecList = new ArrayList<>();
        if(mesPackageManager.getType()==(byte)1){
            //包箱条码
             smtBarcodeRuleSpecList = mesPackageManagerMapper.findBarcodeRule(mesPackageManager.getPackageSpecificationId(),(byte)4,(long)109);
        }else if(mesPackageManager.getType()==(byte)2){
            //栈板
            smtBarcodeRuleSpecList = mesPackageManagerMapper.findBarcodeRule(mesPackageManager.getPackageSpecificationId(),(byte)5,(long)108);
        }

        if(StringUtils.isEmpty(smtBarcodeRuleSpecList)){
            throw new BizErrorException("请设置包箱条码规则");
        }
        //取总共条码生成数
        int printBarcodeCount = mesPackageManagerMapper.findPrintBarcodeCount();
        ResponseEntity<String> responseEntity = applyFeignApi.generateCode(smtBarcodeRuleSpecList, printBarcodeCount + "", null);
        if(responseEntity.getCode()!=0){
            throw new BizErrorException(ErrorCodeEnum.OPT20012008,responseEntity.getMessage());
        }
        if(StringUtils.isEmpty(mesPackageManager.getPrintBarcodeCount())){
            mesPackageManager.setPrintBarcodeCount(1);
        }else{
            mesPackageManager.setPrintBarcodeCount(mesPackageManager.getPrintBarcodeCount()+1);
        }

        mesPackageManager.setBarCode(responseEntity.getData());
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

    /**
     * 记录操作历史
     * @param mesPackageManager
     * @param operation
     */
    private void recordHistory(MesPackageManager mesPackageManager,String operation){
        MesHtPackageManager mesHtPackageManager = new MesHtPackageManager();
        mesHtPackageManager.setOperation(operation);
        if (StringUtils.isEmpty(mesPackageManager)){
            return;
        }
        BeanUtils.autoFillEqFields(mesPackageManager,mesHtPackageManager);
        mesHtPackageManagerService.save(mesHtPackageManager);
    }
}
