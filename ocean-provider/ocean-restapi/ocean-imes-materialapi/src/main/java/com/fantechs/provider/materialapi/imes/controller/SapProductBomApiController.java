package com.fantechs.provider.materialapi.imes.controller;

import com.fantechs.common.base.general.dto.restapi.SearchSapProductBomApi;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.provider.materialapi.imes.service.SapProductBomApiService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.text.ParseException;

/**
 *
 * Created by leifengzhi on 2021/05/31.
 */
@RestController
@Api(tags = "雷赛物料bom信息接口")
@RequestMapping("/productBomApi")
@Validated
public class SapProductBomApiController {

    @Resource
    private SapProductBomApiService sapProductBomApiService;

    @ApiOperation(value = "请求雷赛物料bom信息",notes = "请求雷赛物料bom信息")
    @PostMapping("/getProductBom")
    public ResponseEntity getSupplier(@ApiParam(value = "查询对象")@RequestBody SearchSapProductBomApi searchSapProductBomApi) throws ParseException {
        return ControllerUtil.returnCRUD(sapProductBomApiService.getProductBom(searchSapProductBomApi));
    }


    @ApiOperation("同步Bom")
    @PostMapping("/getBom")
    public ResponseEntity getBom() {
        sapProductBomApiService.getBom();
        System.out.println("========================================接口结束");
        return ControllerUtil.returnCRUD(1);
    }
}
