package com.petadoption.mapper;

import com.petadoption.pojo.dto.PetTypeQueryDto;
import com.petadoption.pojo.entity.PetType;
import com.petadoption.pojo.vo.ChartVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 宠物类别持久化接口
 */
@Mapper
public interface PetTypeMapper {

    void save(PetType petType);

    void update(PetType petType);

    void deleteById(@Param(value = "id") Integer id);

    PetType queryByName(@Param(value = "name") String name);

    List<PetType> query(PetTypeQueryDto petTypeQueryDto);

    Integer queryCount(PetTypeQueryDto petTypeQueryDto);

    List<ChartVO> petTypeCount();

}
