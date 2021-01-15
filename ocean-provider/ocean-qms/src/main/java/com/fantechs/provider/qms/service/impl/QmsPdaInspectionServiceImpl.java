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
import com.fantechs.common.base.general.entity.qms.history.QmsHtPdaInspection;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.imes.apply.ApplyFeignApi;
import com.fantechs.provider.api.security.service.SecurityFeignApi;
import com.fantechs.provider.api.wms.in.InFeignApi;
import com.fantechs.provider.qms.mapper.*;
import com.fantechs.provider.qms.service.QmsPdaInspectionService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;

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
     private InFeignApi inFeignApi;
     @Resource
     private ApplyFeignApi applyFeignApi;
     @Resource
     private QmsHtPdaInspectionMapper qmsHtPdaInspectionMapper;

     @Override
     public List<QmsPdaInspectionDto> findList(Map<String, Object> map) {
          return qmsPdaInspectionMapper.findList(map);
     }

     @Override
     public QmsPdaInspectionDto analysisCode(Map<String, Object> map) {
          QmsPdaInspectionDto qmsPdaInspection = new QmsPdaInspectionDto();

          Object barcode = map.get("barcode");
          SearchMesPackageManagerListDTO search = new SearchMesPackageManagerListDTO();
          search.setBarcode(barcode.toString());
          ResponseEntity<List<MesPackageManagerDTO>> list = inFeignApi.list(search);

          //判断是否是箱码
          if (StringUtils.isNotEmpty(list.getData()) && list.getData().get(0).getParentId() > 0){
               Example example = new Example(QmsPdaInspectionDet.class);
               Example.Criteria criteria = example.createCriteria();
               criteria.andEqualTo("packageManagerId", list.getData().get(0).getPackageManagerId());
               List<QmsPdaInspectionDet> qmsPdaInspectionDets = qmsPdaInspectionDetMapper.selectByExample(example);
               if (StringUtils.isNotEmpty(qmsPdaInspectionDets) || qmsPdaInspectionDets.size() > 0){
                    throw new BizErrorException("该箱码已质检，请勿重复质检");
               }


               MesPackageManagerDTO mesPackageManagerDTO = new MesPackageManagerDTO();
               BeanUtils.copyProperties(list.getData().get(0),mesPackageManagerDTO);

               qmsPdaInspection.setWorkOrderCode(mesPackageManagerDTO.getWorkOrderCode());
               qmsPdaInspection.setWorkOrderId(mesPackageManagerDTO.getWorkOrderId());

               QmsPdaInspectionDetDto qmsPdaInspectionDet = new QmsPdaInspectionDetDto();
               qmsPdaInspectionDet.setPackageManagerId(list.getData().get(0).getPackageManagerId());
               search.setPackageManagerId(mesPackageManagerDTO.getParentId());
               search.setBarcode("");
               list = inFeignApi.list(search);

               map.put("palletId",list.getData().get(0).getParentId());
               List<QmsAndinStorageQuarantineDto> quarantineDtos = qmsAndinStorageQuarantineMapper.findList(map);

               qmsPdaInspection.setPackageManagerId(list.getData().get(0).getPackageManagerId());
               qmsPdaInspectionDet.setPalletCode(list.getData().get(0).getBarCode());
               qmsPdaInspectionDet.setBoxCode(mesPackageManagerDTO.getBarCode());
               qmsPdaInspectionDet.setWarehouseAreaName(StringUtils.isNotEmpty(quarantineDtos)?quarantineDtos.get(0).getWarehouseAreaName():"");
               qmsPdaInspectionDet.setMaterialCode(mesPackageManagerDTO.getMaterialCode());
               qmsPdaInspectionDet.setMaterialDesc(mesPackageManagerDTO.getMaterialDesc());
               qmsPdaInspectionDet.setTotalQuantity(mesPackageManagerDTO.getTotal());
               qmsPdaInspectionDet.setProductModelName(mesPackageManagerDTO.getProductModelName());
               qmsPdaInspectionDet.setUnit(mesPackageManagerDTO.getUnit());
               qmsPdaInspection.setQmsPdaInspectionDet(qmsPdaInspectionDet);
          }else {
               throw new BizErrorException("扫描错误，该条码不是箱码");
          }

          return qmsPdaInspection;
     }

     @Override
     @Transactional(rollbackFor = Exception.class)
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
          int i = 1;
          if (StringUtils.isEmpty(list) || (StringUtils.isNotEmpty(list)&&list.size() == 0)){
               qmsPdaInspection.setCreateTime(new Date());
               qmsPdaInspection.setCreateUserId(user.getUserId());
               qmsPdaInspection.setModifiedTime(new Date());
               qmsPdaInspection.setModifiedUserId(user.getUserId());
               qmsPdaInspection.setStatus(StringUtils.isEmpty(qmsPdaInspection.getStatus())?1:qmsPdaInspection.getStatus());
               qmsPdaInspection.setPdaInspectionCode(CodeUtils.getId("ZLJC"));
               i = qmsPdaInspectionMapper.insertUseGeneratedKeys(qmsPdaInspection);

               QmsHtPdaInspection qmsHtPdaInspection = new QmsHtPdaInspection();
               BeanUtils.copyProperties(qmsPdaInspection,qmsHtPdaInspection);
               qmsHtPdaInspection.setOperation("新增");
               qmsHtPdaInspectionMapper.insert(qmsHtPdaInspection);
          }
          QmsPdaInspectionDet qmsPdaInspectionDet = qmsPdaInspection.getQmsPdaInspectionDet();
          qmsPdaInspectionDet.setCreateTime(new Date());
          qmsPdaInspectionDet.setCreateUserId(user.getUserId());
          qmsPdaInspectionDet.setModifiedTime(new Date());
          qmsPdaInspectionDet.setModifiedUserId(user.getUserId());
          qmsPdaInspectionDet.setStatus(StringUtils.isEmpty(qmsPdaInspectionDet.getStatus())?1:qmsPdaInspectionDet.getStatus());
          qmsPdaInspectionDet.setPdaInspectionId(StringUtils.isNotEmpty(qmsPdaInspection.getPdaInspectionId())?qmsPdaInspection.getPdaInspectionId():list.get(0).getPdaInspectionId());

          qmsPdaInspectionDetMapper.insertUseGeneratedKeys(qmsPdaInspectionDet);

          List<QmsDisqualification> qmsDisqualifications = qmsPdaInspectionDet.getList();
          if (StringUtils.isNotEmpty(qmsDisqualifications) && qmsDisqualifications.size() !=0){
               Long maxLevel = 0L;
               for (QmsDisqualification qmsDisqualification : qmsDisqualifications) {
                    qmsDisqualification.setCheckoutType((byte) 0);
                    qmsDisqualification.setFirstInspectionIdId(qmsPdaInspectionDet.getPdaInspectionDetId());
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
                         if (maxLevel >= Long.valueOf(s)){
                              Long workOrderId = qmsPdaInspection.getWorkOrderId();
                              applyFeignApi.updateStatus(workOrderId,3);
                              break;
                         }
                    }

               }
          }
          return i;
     }

     @Override
     public int update(QmsPdaInspection qmsPdaInspection) {
          SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
          if(StringUtils.isEmpty(user)){
               throw new BizErrorException(ErrorCodeEnum.UAC10011039);
          }
          qmsPdaInspection.setModifiedTime(new Date());
          qmsPdaInspection.setModifiedUserId(user.getUserId());

          QmsHtPdaInspection qmsHtPdaInspection = new QmsHtPdaInspection();
          BeanUtils.copyProperties(qmsPdaInspection,qmsHtPdaInspection);
          qmsHtPdaInspection.setOperation("修改");
          qmsHtPdaInspectionMapper.insert(qmsHtPdaInspection);

          return qmsPdaInspectionMapper.updateByPrimaryKeySelective(qmsPdaInspection);
     }

     @Override
     public int batchDelete(String ids) {
          SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
          if(StringUtils.isEmpty(user)){
               throw new BizErrorException(ErrorCodeEnum.UAC10011039);
          }
          List<QmsHtPdaInspection> list = new ArrayList<>();
          String[] idsArr  = ids.split(",");
          for (String id : idsArr) {
               QmsPdaInspection qmsPdaInspection = qmsPdaInspectionMapper.selectByPrimaryKey(id);
               if (StringUtils.isEmpty(qmsPdaInspection)){
                    throw new BizErrorException(ErrorCodeEnum.OPT20012003);
               }

               QmsHtPdaInspection qmsHtPdaInspection = new QmsHtPdaInspection();
               BeanUtils.copyProperties(qmsPdaInspection,qmsHtPdaInspection);
               qmsHtPdaInspection.setOperation("删除");
               list.add(qmsHtPdaInspection);
          }

          qmsHtPdaInspectionMapper.insertList(list);

          Example example = new Example(QmsPdaInspectionDet.class);
          Example.Criteria criteria = example.createCriteria();
          String[] split = ids.split(",");
          criteria.andIn("pdaInspectionId",Arrays.asList(split));
          qmsPdaInspectionDetMapper.deleteByExample(example);

          return qmsPdaInspectionMapper.deleteByIds(ids);
     }
}
