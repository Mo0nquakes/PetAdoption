package com.petadoption.service;

import com.petadoption.pojo.api.Result;
import com.petadoption.pojo.dto.PetQueryDto;
import com.petadoption.pojo.entity.Pet;
import com.petadoption.pojo.vo.PetListItemVO;
import com.petadoption.pojo.vo.PetVO;

import java.util.List;

/**
 * 宠物业务逻辑接口
 */
public interface PetService {

    Result<String> save(Pet pet);

    Result<String> update(Pet pet);

    Result<String> deleteById(Integer id);

    Result<PetVO> getById(Integer id);

    Result<List<PetListItemVO>> list(PetQueryDto petQueryDto);

    Result<List<PetListItemVO>> recommend();

    Result<List<PetListItemVO>> autoRecommend(Integer count);

    Result<PetVO> viewById(Integer id);

    Result<List<PetListItemVO>> saveList();

}
