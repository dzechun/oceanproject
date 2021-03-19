package com.fantechs.common.base.general.dto.bcm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * @author Mr.Lei
 * @create 2021/3/18
 */
@Data
public class PrintModel implements Serializable {
    @Id
    @Column(name = "id")
    @ApiModelProperty(name = "id",value = "id")
    private Long id;
    @Column(name = "option1")
    @ApiModelProperty(name = "option1",value = "打印字段1")
    private String option1;
    @Column(name = "option2")
    @ApiModelProperty(name = "option2",value = "打印字段2")
    private String option2;
    @Column(name = "option3")
    @ApiModelProperty(name = "option3",value = "打印字段3")
    private String option3;
    @Column(name = "option4")
    @ApiModelProperty(name = "option4",value = "打印字段4")
    private String option4;
    @Column(name = "option5")
    @ApiModelProperty(name = "option5",value = "打印字段5")
    private String option5;
    @Column(name = "option6")
    @ApiModelProperty(name = "option6",value = "打印字段6")
    private String option6;
    @Column(name = "option7")
    @ApiModelProperty(name = "option7",value = "打印字段7")
    private String option7;
    @Column(name = "option8")
    @ApiModelProperty(name = "option8",value = "打印字段8")
    private String option8;
    @Column(name = "option9")
    @ApiModelProperty(name = "option9",value = "打印字段9")
    private String option9;
    @Column(name = "option10")
    @ApiModelProperty(name = "option10",value = "打印字段10")
    private String option10;
    @Column(name = "option11")
    @ApiModelProperty(name = "option11",value = "打印字段11")
    private String option11;
    @Column(name = "option12")
    @ApiModelProperty(name = "option12",value = "打印字段12")
    private String option12;

    private String qrCode;
}
