package com.petadoption.pojo.vo;

import com.petadoption.pojo.entity.Pet;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 宠物信息VO类
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PetVO extends Pet {
    /**
     * 宠物类别名
     */
    private String petTypeName;
}
