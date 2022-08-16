package com.fantechs.common.base.entity.basic.U9;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Table(name = "Cust_GetWhInfo")
public class CustGetWhInfo {

    private String u9id;

    private String code;

    private String name;

    private Date udpatetime;

    private String orgid;

    private String orgcode;

    private String orgname;
}
