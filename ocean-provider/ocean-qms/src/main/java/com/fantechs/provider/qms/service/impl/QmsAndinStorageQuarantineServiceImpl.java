package com.fantechs.provider.qms.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.dto.storage.MesPackageManagerDTO;
import com.fantechs.common.base.dto.storage.SearchMesPackageManagerListDTO;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.qms.QmsAndinStorageQuarantineDto;
import com.fantechs.common.base.general.entity.qms.QmsAndinStorageQuarantine;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.wms.bills.StorageBillsFeignApi;
import com.fantechs.provider.qms.mapper.QmsAndinStorageQuarantineMapper;
import com.fantechs.provider.qms.service.QmsAndinStorageQuarantineService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/01/07.
 */
@Service
public class QmsAndinStorageQuarantineServiceImpl extends BaseService<QmsAndinStorageQuarantine> implements QmsAndinStorageQuarantineService {

    @Resource
    private QmsAndinStorageQuarantineMapper qmsAndinStorageQuarantineMapper;
    @Resource
    private StorageBillsFeignApi storageBillsFeignApi;

    @Override
    public List<QmsAndinStorageQuarantineDto> findList(Map<String, Object> map) {
        return qmsAndinStorageQuarantineMapper.findList(map);
    }

    @Override
    public MesPackageManagerDTO analysisCode(Map<String, Object> map) {
        MesPackageManagerDTO managerDTO = new MesPackageManagerDTO();

        Object barcode = map.get("barcode");
        SearchMesPackageManagerListDTO search = new SearchMesPackageManagerListDTO();
        search.setBarcode(barcode.toString());
        ResponseEntity<List<MesPackageManagerDTO>> list = storageBillsFeignApi.list(search);
        Long parentId = 0L;
        //判断是箱码还是栈板码
        if (StringUtils.isNotEmpty(list.getData()) && list.getData().get(0).getParentId() > 0){
            parentId = list.getData().get(0).getParentId();
        }else{
            parentId = list.getData().get(0).getPackageManagerId();
        }

        search.setBarcode("");
        search.setParentId(parentId);
        list = storageBillsFeignApi.list(search);

        int total = 0;
        if (StringUtils.isNotEmpty(list.getData())){
            for (MesPackageManagerDTO datum : list.getData()) {
                total += datum.getTotal().intValue();
            }
        }

        if (StringUtils.isNotEmpty(list.getData()) && list.getData().size() != 0){

            search.setParentId(null);
            search.setPackageManagerId(parentId);
            list = storageBillsFeignApi.list(search);

            Example example = new Example(QmsAndinStorageQuarantine.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("palletId", parentId);
            List<QmsAndinStorageQuarantine> qmsAndinStorageQuarantines = qmsAndinStorageQuarantineMapper.selectByExample(example);
            if (qmsAndinStorageQuarantines.size()!=0){
                throw new BizErrorException("该栈板已绑定待检区域");
            }

            managerDTO.setMaterialCode(list.getData().get(0).getMaterialCode());
            managerDTO.setWorkOrderCode(list.getData().get(0).getWorkOrderCode());
            managerDTO.setMaterialDesc(list.getData().get(0).getMaterialDesc());
            managerDTO.setBarCode(list.getData().get(0).getBarCode());

            managerDTO.setPackageSpecificationQuantity(list.getData().get(0).getTotal().toString());
        }

        managerDTO.setPackageManagerId(parentId);
        managerDTO.setTotal(new BigDecimal(total));
        return managerDTO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(QmsAndinStorageQuarantine qmsAndinStorageQuarantine) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
//        if(StringUtils.isEmpty(user)){
//            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
//        }

        qmsAndinStorageQuarantine.setCreateTime(new Date());
//        qmsAndinStorageQuarantine.setCreateUserId(user.getUserId());
        qmsAndinStorageQuarantine.setModifiedTime(new Date());
//        qmsAndinStorageQuarantine.setModifiedUserId(user.getUserId());
        qmsAndinStorageQuarantine.setStatus(StringUtils.isEmpty(qmsAndinStorageQuarantine.getStatus())?1:qmsAndinStorageQuarantine.getStatus());

        //判断是否是栈板码逻辑
        SearchMesPackageManagerListDTO searchMesPackageManagerListDTO = new SearchMesPackageManagerListDTO();
        searchMesPackageManagerListDTO.setPackageManagerId(qmsAndinStorageQuarantine.getPalletId());
        ResponseEntity<List<MesPackageManagerDTO>> list = storageBillsFeignApi.list(searchMesPackageManagerListDTO);

        if (StringUtils.isNotEmpty(list) && list.getData().size()!=0 && list.getData().get(0).getParentId() > 0){
            qmsAndinStorageQuarantine.setPalletId(list.getData().get(0).getPackageManagerId());
        }

        int i = qmsAndinStorageQuarantineMapper.insert(qmsAndinStorageQuarantine);

        return i;
    }
}