package com.petadoption.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.petadoption.pojo.api.Result;
import com.petadoption.pojo.dto.ActiveNetQueryDto;
import com.petadoption.pojo.entity.ActiveNet;
import com.petadoption.pojo.vo.ChartVO;

import java.util.List;

/**
 * 行为互动业务逻辑接口
 */
public interface ActiveNetService extends IService<ActiveNet> {

    Result<List<ActiveNet>> query(ActiveNetQueryDto activeNetQueryDto);

    Result<String> saveEntity(ActiveNet activeNet);

    Result<String> deleteUserPetList();

    Result<String> deleteUserPetPostList();

    Result<List<ChartVO>> listChart(ActiveNetQueryDto activeNetQueryDto);

}
