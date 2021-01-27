package com.fantechs.provider.om.controller;

import com.fantechs.common.base.general.entity.om.MesScheduleDetail;
import com.fantechs.common.base.general.dto.om.MesScheduleDetailDTO;
import com.fantechs.provider.om.service.MesScheduleDetailService;
import com.fantechs.common.base.general.dto.om.SearchMesScheduleDetailListDTO;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.utils.StringUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import javax.annotation.Resource;
import java.util.List;
/**
 * @Auther: bingo.ren
 * @Date: 2021年1月9日 17:10
 * @Description: 排产详情管理
 * @Version: 1.0
 */
@RestController
@Api(tags = "排产详情管理",basePath = "mesScheduleDetail")
@RequestMapping("mesScheduleDetail")
@Slf4j
public class MesScheduleDetailController {

    @Resource
    private MesScheduleDetailService mesScheduleDetailService;

    @ApiOperation("查询排产详情列表")
    @PostMapping("findList")
    public ResponseEntity<List<MesScheduleDetailDTO>> list(
            @ApiParam(value = "查询条件，请参考Model说明")@RequestBody(required = false) SearchMesScheduleDetailListDTO searchMesScheduleDetailListDTO
    ){
        Page<Object> page = PageHelper.startPage(searchMesScheduleDetailListDTO.getStartPage(), searchMesScheduleDetailListDTO.getPageSize());
        List<MesScheduleDetailDTO> mesScheduleDetailDTOList = mesScheduleDetailService.selectFilterAll(ControllerUtil.dynamicConditionByEntity(searchMesScheduleDetailListDTO));
        return ControllerUtil.returnDataSuccess(mesScheduleDetailDTOList,(int)page.getTotal());
    }

    @ApiOperation("通过ID查询排产详情")
    @GetMapping("detail")
    public ResponseEntity<MesScheduleDetail> one(@ApiParam(value = "排产详情对象ID",required = true)@RequestParam Long id){
        MesScheduleDetail mesScheduleDetail = mesScheduleDetailService.selectByKey(id);
        return ControllerUtil.returnDataSuccess(mesScheduleDetail, StringUtils.isEmpty(mesScheduleDetail)?0:1);
    }

    @ApiOperation("增加排产详情数据")
    @PostMapping("add")
    public ResponseEntity add(@ApiParam(value = "排产详情对象",required = true)@RequestBody MesScheduleDetail mesScheduleDetail){
        return ControllerUtil.returnCRUD(mesScheduleDetailService.save(mesScheduleDetail));
    }

    @ApiOperation("批量删除排产详情数据")
    @GetMapping("delete")
    public ResponseEntity batchDelete(@ApiParam(value = "排产详情对象ID集，多个用英文逗号隔开",required = true)@RequestParam String ids){
        return ControllerUtil.returnCRUD(mesScheduleDetailService.batchDelete(ids));
    }

    @ApiOperation("修改排产详情数据")
    @PostMapping("update")
    public ResponseEntity update(@ApiParam(value = "排产详情对象，对象ID必传",required = true)@RequestBody MesScheduleDetail mesScheduleDetail){
        return ControllerUtil.returnCRUD(mesScheduleDetailService.update(mesScheduleDetail));
    }

    @PostMapping(value = "export",produces = "application/octet-stream")
    @ApiOperation(value = "导出EXCEL")
    public void export(
            @ApiParam(value = "查询条件，请参考Model说明")@RequestBody(required = false) SearchMesScheduleDetailListDTO searchMesScheduleDetailListDTO,
            @ApiParam(value = "当前页",required = false,defaultValue = "1")@RequestParam(defaultValue = "1",required = false) int startPage,
            @ApiParam(value = "显示数量",required = false,defaultValue = "10")@RequestParam(defaultValue = "10",required = false) int pageSize,
            HttpServletResponse response){
        Page<Object> page = PageHelper.startPage(startPage, pageSize);
        List<MesScheduleDetailDTO> mesScheduleDetailDTOList = mesScheduleDetailService.selectFilterAll(ControllerUtil.dynamicConditionByEntity(searchMesScheduleDetailListDTO));
        if(StringUtils.isEmpty(mesScheduleDetailDTOList)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012007);
        }
        EasyPoiUtils.exportExcel(mesScheduleDetailDTOList ,"排产详情信息","排产详情信息", MesScheduleDetailDTO.class, "排产详情信息.xls", response);
    }
}
