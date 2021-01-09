package com.fantechs.provider.qms.controller;

import com.fantechs.common.base.dto.storage.MesPackageManagerDTO;
import com.fantechs.common.base.dto.storage.SearchMesPackageManagerListDTO;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.qms.QmsAndinStorageQuarantineDto;
import com.fantechs.common.base.general.entity.qms.QmsAndinStorageQuarantine;
import com.fantechs.common.base.general.entity.qms.search.SearchQmsAndinStorageQuarantine;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.qms.service.QmsAndinStorageQuarantineService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 *
 * Created by leifengzhi on 2021/01/07.
 */
@RestController
@Api(tags = "入库待检")
@RequestMapping("/qmsAndinStorageQuarantine")
@Validated
public class QmsAndinStorageQuarantineController {

    @Autowired
    private QmsAndinStorageQuarantineService qmsAndinStorageQuarantineService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated QmsAndinStorageQuarantine qmsAndinStorageQuarantine) {
        return ControllerUtil.returnCRUD(qmsAndinStorageQuarantineService.save(qmsAndinStorageQuarantine));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(qmsAndinStorageQuarantineService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=QmsAndinStorageQuarantine.update.class) QmsAndinStorageQuarantine qmsAndinStorageQuarantine) {
        return ControllerUtil.returnCRUD(qmsAndinStorageQuarantineService.update(qmsAndinStorageQuarantine));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<QmsAndinStorageQuarantine> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        QmsAndinStorageQuarantine  qmsAndinStorageQuarantine = qmsAndinStorageQuarantineService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(qmsAndinStorageQuarantine,StringUtils.isEmpty(qmsAndinStorageQuarantine)?0:1);
    }

    @ApiOperation("解析传入的箱码或者栈板码")
    @PostMapping("/analysisCode")
    public ResponseEntity<MesPackageManagerDTO> analysisCode(@ApiParam(value = "查询条件，请参考Model说明")@RequestBody(required = false) SearchMesPackageManagerListDTO searchMesPackageManagerListDTO) {
        MesPackageManagerDTO  mesPackageManagerDTO = qmsAndinStorageQuarantineService.analysisCode(ControllerUtil.dynamicConditionByEntity(searchMesPackageManagerListDTO));
        return  ControllerUtil.returnDataSuccess(mesPackageManagerDTO,StringUtils.isEmpty(mesPackageManagerDTO)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<QmsAndinStorageQuarantineDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchQmsAndinStorageQuarantine searchQmsAndinStorageQuarantine) {
        Page<Object> page = PageHelper.startPage(searchQmsAndinStorageQuarantine.getStartPage(),searchQmsAndinStorageQuarantine.getPageSize());
        List<QmsAndinStorageQuarantineDto> list = qmsAndinStorageQuarantineService.findList(ControllerUtil.dynamicConditionByEntity(searchQmsAndinStorageQuarantine));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

}
