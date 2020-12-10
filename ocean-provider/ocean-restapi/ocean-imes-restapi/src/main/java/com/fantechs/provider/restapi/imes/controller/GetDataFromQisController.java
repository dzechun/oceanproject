package com.fantechs.provider.restapi.imes.controller;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/QISAPI")
@Api(tags = "同步Qis数据", basePath = "/QISAPI")
public class GetDataFromQisController {
}
