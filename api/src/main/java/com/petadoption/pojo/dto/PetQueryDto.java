package com.petadoption.pojo.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 宠物查询条件类
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PetQueryDto extends QueryDto{
    /**
     * 类别名
     */
    private String name;
    /**
     * 是否推荐（0：未推荐；1：推荐）
     */
    private Boolean isRecommend;
    /**
     * 宠物类别ID
     */
    private Integer petTypeId;
    /**
     * 性别（0：公；1：母）
     */
    private Integer gender;
    /**
     * 是否接种疫苗
     */
    private Boolean isVaccine;
    /**
     * 排序方式（new：最新发布；ageAsc：月龄升序；ageDesc：月龄降序）
     */
    private String sort;
}
