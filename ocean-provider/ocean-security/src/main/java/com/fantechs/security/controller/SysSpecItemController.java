package com.fantechs.security.controller;


import com.fantechs.common.base.entity.security.SysSpecItem;
import com.fantechs.common.base.entity.security.history.SysHtSpecItem;
import com.fantechs.common.base.entity.security.search.SearchSysSpecItem;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.security.service.SysHtSpecItemService;
import com.fantechs.security.service.SysMenuInfoService;
import com.fantechs.security.service.SysSpecItemService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Auther: wcz
 * @Date: 2020/8/19 13:50
 * @Description:
 * @Version: 1.0
 */
@RestController
@RequestMapping(value = "/sysSpecItem")
@Api(tags = "程序配置项")
@Slf4j
@Validated
public class SysSpecItemController {
    @Autowired
    private SysSpecItemService sysSpecItemService;
    @Autowired
    private SysHtSpecItemService sysHtSpecItemService;
    @Resource
    private SysMenuInfoService sysMenuInfoService;

    @ApiOperation("根据条件查询程序配置项列表")
    @PostMapping("/findList")
    public ResponseEntity<List<SysSpecItem>> findList(
            @ApiParam(value = "查询条件，请参考Model说明")@RequestBody(required = false) SearchSysSpecItem searchSysSpecItem){
        List<Long> menuIds = sysMenuInfoService.getMenu(searchSysSpecItem.getMenuId());
        searchSysSpecItem.setMenuIds(menuIds);
        Page<Object> page = PageHelper.startPage(searchSysSpecItem.getStartPage(),searchSysSpecItem.getPageSize());
        List<SysSpecItem> SysSpecItems = sysSpecItemService.findList(searchSysSpecItem);
        return ControllerUtil.returnDataSuccess(SysSpecItems,(int)page.getTotal());
    }

    @GetMapping("/detail")
    @ApiOperation(value = "程序配置项详情",notes = "程序配置项详情息")
    public ResponseEntity<SysSpecItem> selectSpecItemById(@ApiParam(value = "传入主键specId",required = true) @RequestParam @NotNull(message = "id不能为空") Long id) {
        SysSpecItem sysSpecItem=sysSpecItemService.selectByKey(id);
        return ControllerUtil.returnDataSuccess(sysSpecItem,StringUtils.isEmpty(sysSpecItem)?0:1);
    }


    @ApiOperation("增加程序配置项")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：specCode、specName,para",required = true)@RequestBody @Validated SysSpecItem sysSpecItem){
        if(StringUtils.isEmpty(
                sysSpecItem.getSpecCode(),
                sysSpecItem.getSpecName(),
                sysSpecItem.getPara())){
            return ControllerUtil.returnFailByParameError();
        }
        return ControllerUtil.returnCRUD(sysSpecItemService.save(sysSpecItem));
    }

    @ApiOperation("修改程序配置项")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "程序配置项对象，程序配置项Id必传",required = true)@RequestBody @Validated(value =SysSpecItem.update.class ) SysSpecItem sysSpecItem){
        return ControllerUtil.returnCRUD(sysSpecItemService.update(sysSpecItem));
    }

    @ApiOperation("删除程序配置项")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "程序配置项对象ID",required = true)@RequestParam @NotBlank(message = "ids不能为空") String ids){
        return ControllerUtil.returnCRUD(sysSpecItemService.batchDelete(ids));
    }

    /**
     * 导出数据
     * @return
     * @throws
     */
    @PostMapping(value = "/export")
    @ApiOperation(value = "导出程序配置项excel",notes = "导出程序配置项excel",produces = "application/octet-stream")
    public void exportSpecItems(HttpServletResponse response, @ApiParam(value ="输入查询条件",required = false)
                            @RequestBody(required = false) SearchSysSpecItem searchSysSpecItem){
        List<SysSpecItem> list = sysSpecItemService.findList(searchSysSpecItem);
        try {
            // 导出操作
            EasyPoiUtils.exportExcel(list, "程序配置项信息导出", "程序配置项信息", SysSpecItem.class, "程序配置项信息.xls", response);
        } catch (Exception e) {
            throw new BizErrorException(e);
        }
    }

    @PostMapping("/findHtList")
    @ApiOperation(value = "根据条件查询程序配置项履历信息",notes = "根据条件查询程序配置项履历信息")
    public ResponseEntity<List<SysHtSpecItem>> selectHtSpecItems(@RequestBody(required = false) SearchSysSpecItem searchSysSpecItem) {
        Page<Object> page = PageHelper.startPage(searchSysSpecItem.getStartPage(),searchSysSpecItem.getPageSize());
        List<SysHtSpecItem> SysHtSpecItems = sysHtSpecItemService.findHtSpecItemList(ControllerUtil.dynamicConditionByEntity(searchSysSpecItem));
        return  ControllerUtil.returnDataSuccess(SysHtSpecItems, (int)page.getTotal());
    }

}

