package ${basePackage}.controller.${sign};

import ${basePackage}.entity.${modelNameUpperCamel};
import ${basePackage}.service.${modelNameUpperCamel}Service;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.StringUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *
 * Created by ${author} on ${date}.
 */
@RestController
@Api(tags = "${baseRequestMapping}控制器")
@RequestMapping("/${baseRequestMapping}")
public class ${modelNameUpperCamel}Controller {

    @Autowired
    private ${modelNameUpperCamel}Service ${modelNameLowerCamel}Service;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody ${modelNameUpperCamel} ${modelNameLowerCamel}) {
        return ControllerUtil.returnCRUD(${modelNameLowerCamel}Service.save(${modelNameLowerCamel}));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam String ids) {
        if(StringUtils.isEmpty(ids)){
            return ControllerUtil.returnFailByParameError();
        }
        return ControllerUtil.returnCRUD(${modelNameLowerCamel}Service.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody ${modelNameUpperCamel} ${modelNameLowerCamel}) {
        if(StringUtils.isEmpty(${modelNameLowerCamel}.get${modelNameUpperCamel}Id()
        )){
            return ControllerUtil.returnFailByParameError();
        }
        return ControllerUtil.returnCRUD(${modelNameLowerCamel}Service.update(${modelNameLowerCamel}));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<${modelNameUpperCamel}> detail(@ApiParam(value = "ID",required = true)@RequestParam Long id) {
        if(StringUtils.isEmpty(id)){
            return ControllerUtil.returnFailByParameError();
        }
        ${modelNameUpperCamel}  ${modelNameLowerCamel} = ${modelNameLowerCamel}Service.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(${modelNameLowerCamel},StringUtils.isEmpty(${modelNameLowerCamel})?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<${modelNameUpperCamel}>> findList(@ApiParam(value = "查询对象")@RequestBody Search${modelNameUpperCamel} search${modelNameUpperCamel}) {
        Page<Object> page = PageHelper.startPage(search${modelNameUpperCamel}.getStartPage(),search${modelNameUpperCamel}.getPageSize());
        List<${modelNameUpperCamel}> list = ${modelNameLowerCamel}Service.findList(ControllerUtil.dynamicConditionByEntity(search${modelNameUpperCamel}));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<${modelNameUpperCamel}>> findHtList(@ApiParam(value = "查询对象")@RequestBody Search${modelNameUpperCamel} search${modelNameUpperCamel}) {
        Page<Object> page = PageHelper.startPage(search${modelNameUpperCamel}.getStartPage(),search${modelNameUpperCamel}.getPageSize());
        List<${modelNameUpperCamel}> list = ${modelNameLowerCamel}Service.findList(search${modelNameUpperCamel});
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) Search${modelNameUpperCamel} search${modelNameUpperCamel}){
    List<${modelNameUpperCamel}> list = ${modelNameLowerCamel}Service.findList(ControllerUtil.dynamicConditionByEntity(search${modelNameUpperCamel}));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "${modelNameUpperCamel}信息", ${modelNameUpperCamel}.class, "${modelNameUpperCamel}.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
