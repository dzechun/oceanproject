package com.fantechs.provider.wms.storageBills.service.impl;

import com.fantechs.common.base.dto.storage.SaveMesPackageManagerDTO;
import com.fantechs.common.base.entity.apply.SmtBarcodeRuleSpec;
import com.fantechs.common.base.entity.storage.MesPackageManager;
import com.fantechs.common.base.dto.storage.MesPackageManagerDTO;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.provider.api.imes.apply.ApplyFeignApi;
import com.fantechs.provider.wms.storageBills.service.MesPackageManagerService;
import com.fantechs.provider.wms.storageBills.mapper.MesPackageManagerMapper;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.support.BaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import com.fantechs.common.base.utils.StringUtils;
import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Auther: bingo.ren
 * @Date: 2021年1月7日 16:38
 * @Description: 包装管理接口实现
 * @Version: 1.0
 */
@Service
public class MesPackageManagerServiceImpl extends BaseService<MesPackageManager>  implements MesPackageManagerService{

     @Resource
     private MesPackageManagerMapper mesPackageManagerMapper;
     @Resource
     private ApplyFeignApi applyFeignApi;

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
        return mesPackageManagerMapper.insertSelective(mesPackageManager);
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
        return mesPackageManagerMapper.updateByPrimaryKeySelective(mesPackageManager);
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
                throw new BizErrorException("修改工单完工数出错");
            }
        }
        return mesPackageManager;
    }

    @Override
    public int printCode(Long packageManagerId) {
        MesPackageManager mesPackageManager = this.selectByKey(packageManagerId);
        this.printCode(mesPackageManager);
        if(this.update(mesPackageManager)<=0){
            return 0;
        }
        return 1;
    }

    private void printCode(MesPackageManager mesPackageManager){
        //根据包装规格获取条码规则，生成条码
        List<SmtBarcodeRuleSpec> smtBarcodeRuleSpecList = mesPackageManagerMapper.findBarcodeRule(mesPackageManager.getPackageSpecificationId());
        //取总共条码生成数
        int printBarcodeCount = mesPackageManagerMapper.findPrintBarcodeCount();
        ResponseEntity<String> responseEntity = applyFeignApi.generateCode(smtBarcodeRuleSpecList, printBarcodeCount + "", null);
        if(responseEntity.getCode()!=0){
            throw new BizErrorException(ErrorCodeEnum.OPT20012008);
        }
        mesPackageManager.setBarCode(responseEntity.getData());
        //调用打印程序进行条码打印
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
     * @param id
     * @param operation
     */
    private void recordHistory(Long id,String operation){
        /*HT ht= new HT();
        ht.setOperation(operation);
        MesSchedule mesSchedule = selectByKey(id);
        if (StringUtils.isEmpty(mesSchedule)){
            return;
        }
        BeanUtils.autoFillEqFields(mesSchedule,ht);
        htService.save(ht);*/
    }
}
