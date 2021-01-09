package com.fantechs.provider.qms.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.dto.storage.MesPackageManagerDTO;
import com.fantechs.common.base.dto.storage.SearchMesPackageManagerListDTO;
import com.fantechs.common.base.entity.security.SysSpecItem;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.entity.security.search.SearchSysSpecItem;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.qms.QmsAndinStorageQuarantineDto;
import com.fantechs.common.base.general.dto.qms.QmsPdaInspectionDetDto;
import com.fantechs.common.base.general.dto.qms.QmsPdaInspectionDto;
import com.fantechs.common.base.general.entity.qms.QmsDisqualification;
import com.fantechs.common.base.general.entity.qms.QmsPdaInspection;
import com.fantechs.common.base.general.entity.qms.QmsPdaInspectionDet;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.security.service.SecurityFeignApi;
import com.fantechs.provider.api.wms.bills.StorageBillsFeignApi;
import com.fantechs.provider.qms.mapper.QmsAndinStorageQuarantineMapper;
import com.fantechs.provider.qms.mapper.QmsDisqualificationMapper;
import com.fantechs.provider.qms.mapper.QmsPdaInspectionDetMapper;
import com.fantechs.provider.qms.mapper.QmsPdaInspectionMapper;
import com.fantechs.provider.qms.service.QmsPdaInspectionService;
import com.sun.org.apache.bcel.internal.generic.NEW;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/01/07.
 */
@Service
public class QmsPdaInspectionServiceImpl  extends BaseService<QmsPdaInspection> implements QmsPdaInspectionService {

     @Resource
     private QmsPdaInspectionMapper qmsPdaInspectionMapper;
     @Resource
     private QmsPdaInspectionDetMapper qmsPdaInspectionDetMapper;
     @Resource
     private QmsDisqualificationMapper qmsDisqualificationMapper;
     @Resource
     private SecurityFeignApi securityFeignApi;
     @Resource
     private QmsAndinStorageQuarantineMapper qmsAndinStorageQuarantineMapper;
     @Resource
     private StorageBillsFeignApi storageBillsFeignApi;

     @Override
     public List<QmsPdaInspectionDto> findList(Map<String, Object> map) {
          return qmsPdaInspectionMapper.findList(map);
     }

     @Override
     public MesPackageManagerDTO analysisCode(Map<String, Object> map) {
          QmsPdaInspectionDto qmsPdaInspection = new QmsPdaInspectionDto();

          Object barcode = map.get("barcode");
          SearchMesPackageManagerListDTO search = new SearchMesPackageManagerListDTO();
          search.setBarcode(barcode.toString());
          ResponseEntity<List<MesPackageManagerDTO>> list = storageBillsFeignApi.list(search);

          //判断是箱码还是栈板码
          if (StringUtils.isNotEmpty(list.getData()) && list.getData().get(0).getParentId() > 0){
               MesPackageManagerDTO mesPackageManagerDTO = list.getData().get(0);

               qmsPdaInspection.setWorkOrderCode(mesPackageManagerDTO.getWorkOrderCode());
               QmsPdaInspectionDetDto qmsPdaInspectionDet = qmsPdaInspection.getQmsPdaInspectionDet();
               System.out.println(mesPackageManagerDTO.getPackageManagerCode());
               qmsPdaInspectionDet.setBoxCode(mesPackageManagerDTO.getPackageManagerCode());
               qmsPdaInspectionDet.setPalletCode("栈板码");
               qmsPdaInspectionDet.setStorageName("储位名称");
               qmsPdaInspectionDet.setMaterialCode(mesPackageManagerDTO.getMaterialCode());
               qmsPdaInspectionDet.setMaterialDesc(mesPackageManagerDTO.getMaterialDesc());
               qmsPdaInspectionDet.setTotalQuantity(mesPackageManagerDTO.getTotal());
               qmsPdaInspectionDet.setProductModelName("产品型号");
               qmsPdaInspectionDet.setUnit("单位");
          }else {
               throw new BizErrorException("扫描错误，该条码不是箱码");
          }

          return null;
     }

     @Override
     public int save(QmsPdaInspection qmsPdaInspection) {
          SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
          if(StringUtils.isEmpty(user)){
               throw new BizErrorException(ErrorCodeEnum.UAC10011039);
          }
          Map<String, Object> map = new HashMap();
          map.put("palletId",qmsPdaInspection.getPackageManagerId());
          List<QmsAndinStorageQuarantineDto> qmsAndinStorageQuarantines = qmsAndinStorageQuarantineMapper.findList(map);
          if (StringUtils.isEmpty(qmsAndinStorageQuarantines) || (StringUtils.isNotEmpty(qmsAndinStorageQuarantines) && qmsAndinStorageQuarantines.size()==0)){
               throw new BizErrorException("该栈板未进入待检区域");
          }

          Example example = new Example(QmsPdaInspection.class);
          Example.Criteria criteria = example.createCriteria();
          criteria.andEqualTo("workOrderId", qmsPdaInspection.getWorkOrderId())
                  .andEqualTo("packageManagerId",qmsPdaInspection.getPackageManagerId());
          List<QmsPdaInspection> list = qmsPdaInspectionMapper.selectByExample(example);
          int i = 0;
          if (StringUtils.isEmpty(list) || (StringUtils.isNotEmpty(list)&&list.size() == 0)){
               qmsPdaInspection.setCreateTime(new Date());
               qmsPdaInspection.setCreateUserId(user.getUserId());
               qmsPdaInspection.setModifiedTime(new Date());
               qmsPdaInspection.setModifiedUserId(user.getUserId());
               qmsPdaInspection.setStatus(StringUtils.isEmpty(qmsPdaInspection.getStatus())?1:qmsPdaInspection.getStatus());
               i = qmsPdaInspectionMapper.insertUseGeneratedKeys(qmsPdaInspection);
          }
          QmsPdaInspectionDet qmsPdaInspectionDet = qmsPdaInspection.getQmsPdaInspectionDet();
          qmsPdaInspectionDet.setCreateTime(new Date());
          qmsPdaInspectionDet.setCreateUserId(user.getUserId());
          qmsPdaInspectionDet.setModifiedTime(new Date());
          qmsPdaInspectionDet.setModifiedUserId(user.getUserId());
          qmsPdaInspectionDet.setStatus(StringUtils.isEmpty(qmsPdaInspectionDet.getStatus())?1:qmsPdaInspectionDet.getStatus());
          qmsPdaInspectionDet.setAndinStorageQuarantineId(StringUtils.isEmpty(qmsPdaInspection)?qmsPdaInspection.getAndinStorageQuarantineId():list.get(0).getAndinStorageQuarantineId());
          qmsPdaInspectionDetMapper.insertUseGeneratedKeys(qmsPdaInspectionDet);

          List<QmsDisqualification> qmsDisqualifications = qmsPdaInspectionDet.getList();

          if (qmsDisqualifications.size() !=0){
               Long maxLevel = 0L;
               for (QmsDisqualification qmsDisqualification : qmsDisqualifications) {
                    qmsDisqualification.setCheckoutType((byte) 0);
                    qmsDisqualification.setFirstInspectionIdId(qmsPdaInspectionDet.getAndinStorageQuarantineDetId());
                    qmsDisqualification.setCreateTime(new Date());
                    qmsDisqualification.setCreateUserId(user.getUserId());
                    qmsDisqualification.setModifiedTime(new Date());
                    qmsDisqualification.setModifiedUserId(user.getUserId());
                    qmsDisqualification.setStatus(StringUtils.isEmpty(qmsDisqualification.getStatus())?1:qmsDisqualification.getStatus());
                    qmsDisqualification.setCheckoutType((byte) 1);
                    if (qmsDisqualification.getLevel() > maxLevel){
                         maxLevel = qmsDisqualification.getLevel();
                    }
               }
               qmsDisqualificationMapper.insertList(qmsDisqualifications);
               SearchSysSpecItem searchSysSpecItem = new SearchSysSpecItem();
               searchSysSpecItem.setSpecCode("severityLevel");
               ResponseEntity<List<SysSpecItem>> severityLevel = securityFeignApi.findSpecItemList(searchSysSpecItem);

               if (StringUtils.isNotEmpty(severityLevel) && StringUtils.isNotEmpty(severityLevel.getData()) && severityLevel.getData().size()!=0){
                    SysSpecItem sysSpecItem = severityLevel.getData().get(0);
                    String paraValue = sysSpecItem.getParaValue();
                    String[] split = paraValue.split(",");
                    for (String s : split) {
                         if (s.equals(maxLevel.toString())){
                              Long workOrderId = qmsPdaInspection.getWorkOrderId();
                         }
                    }

               }
          }
          return i;
     }


}
