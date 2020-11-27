package com.fantechs.provider.client.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by lfz on 2020/11/27.
 */
@Data
public class User implements Serializable {
    private String id;
    private String name;
}
