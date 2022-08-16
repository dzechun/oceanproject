package ${basePackage}.controller.${sign};

import com.fantechs.common.base.exception.BizErrorException;
import ${basePackage}.entity.${modelNameUpperCamel};
import ${basePackage}.service.${modelNameUpperCamel}Service;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 *
 * Created by ${author} on ${date}.
 */
@RestController
@Api(tags = "${baseRequestMapping}控制器")
@RequestMapping("/${baseRequestMapping}")
@Validated
public class ${modelNameUpperCamel}Controller {

    @Resource
    private ${modelNameUpperCamel}Service ${modelNameLowerCamel}Service;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated ${modelNameUpperCamel} ${modelNameLowerCamel}) {
        return ControllerUtil.returnCRUD(${modelNameLowerCamel}Service.save(${modelNameLowerCamel}));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(${modelNameLowerCamel}Service.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=${modelNameUpperCamel}.update.class) ${modelNameUpperCamel} ${modelNameLowerCamel}) {
        return ControllerUtil.returnCRUD(${modelNameLowerCamel}Service.update(${modelNameLowerCamel}));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<${modelNameUpperCamel}> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        ${modelNameUpperCamel}  ${modelNameLowerCamel} = ${modelNameLowerCamel}Service.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(${modelNameLowerCamel},StringUtils.isEmpty(${modelNameLowerCamel})?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<${modelNameUpperCamel}Dto>> findList(@ApiParam(value = "查询对象")@RequestBody Search${modelNameUpperCamel} search${modelNameUpperCamel}) {
        Page<Object> page = PageHelper.startPage(search${modelNameUpperCamel}.getStartPage(),search${modelNameUpperCamel}.getPageSize());
        List<${modelNameUpperCamel}Dto> list = ${modelNameLowerCamel}Service.findList(ControllerUtil.dynamicConditionByEntity(search${modelNameUpperCamel}));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("列表(不分页)")
    @PostMapping("/findAll")
    public ResponseEntity<List<${modelNameUpperCamel}Dto>> findAll(@ApiParam(value = "查询对象") @RequestBody Search${modelNameUpperCamel} search${modelNameUpperCamel}) {
        List<${modelNameUpperCamel}Dto> list = ${modelNameLowerCamel}Service.findList(ControllerUtil.dynamicConditionByEntity(search${modelNameUpperCamel}));
        return ControllerUtil.returnDataSuccess(list, list.size());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<${modelNameUpperCamel}>> findHtList(@ApiParam(value = "查询对象")@RequestBody Search${modelNameUpperCamel} search${modelNameUpperCamel}) {
        Page<Object> page = PageHelper.startPage(search${modelNameUpperCamel}.getStartPage(),search${modelNameUpperCamel}.getPageSize());
        List<${modelNameUpperCamel}> list = ${modelNameLowerCamel}Service.findHtList(ControllerUtil.dynamicConditionByEntity(search${modelNameUpperCamel}));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) Search${modelNameUpperCamel} search${modelNameUpperCamel}){
    List<${modelNameUpperCamel}Dto> list = ${modelNameLowerCamel}Service.findList(ControllerUtil.dynamicConditionByEntity(search${modelNameUpperCamel}));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "${modelNameUpperCamel}信息", ${modelNameUpperCamel}Dto.class, "${modelNameUpperCamel}.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }

    @PostMapping(value = "/import")
    @ApiOperation(value = "从excel导入",notes = "从excel导入")
    public ResponseEntity importExcel(@ApiParam(value ="输入excel文件",required = true) @RequestPart(value="file") MultipartFile file){
        try {
            // 导入操作
            List<${modelNameUpperCamel}> baseAddressImports = EasyPoiUtils.importExcel(file, 0, 1, ${modelNameUpperCamel}.class);
            Map<String, Object> resultMap = ${modelNameLowerCamel}Service.importExcel(baseAddressImports);
            return ControllerUtil.returnDataSuccess("操作结果集",resultMap);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return ControllerUtil.returnFail(e.getMessage(), ErrorCodeEnum.OPT20012002.getCode());
        }
    }
}
