package com.petadoption.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.petadoption.pojo.api.Result;
import com.petadoption.pojo.dto.ActiveNetQueryDto;
import com.petadoption.pojo.dto.PetPostQueryDto;
import com.petadoption.pojo.entity.ActiveNet;
import com.petadoption.pojo.entity.PetPost;
import com.petadoption.pojo.vo.PetPostListItemVO;
import com.petadoption.pojo.vo.PetPostSelectedVO;
import com.petadoption.pojo.vo.PetPostVO;

import java.util.List;

/**
 * 宠物经验帖子业务逻辑接口
 */
public interface PetPostService extends IService<PetPost> {

    Result<List<PetPostListItemVO>> list(PetPostQueryDto petPostQueryDto);

    Result<PetPostVO> getById(Integer id);

    Result<String> saveEntity(PetPost petPost);

    Result<String> updateEntity(PetPost petPost);

    Result<String> audit(Integer id);

    Result<List<PetPostListItemVO>> autoRecommend(Integer count);

    Result<List<PetPostListItemVO>> saveList();

    Result<List<PetPostSelectedVO>> listPetPostSelect();

}
