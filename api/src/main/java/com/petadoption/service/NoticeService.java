package com.petadoption.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.petadoption.pojo.api.Result;
import com.petadoption.pojo.dto.NoticeQueryDto;
import com.petadoption.pojo.dto.PetTypeQueryDto;
import com.petadoption.pojo.entity.Notice;
import com.petadoption.pojo.entity.PetType;
import com.petadoption.pojo.vo.NoticeListItemVO;

import java.util.List;

/**
 * 公告业务逻辑接口
 */
public interface NoticeService extends IService<Notice> {

    Result<String> saveEntity(Notice notice);

    Result<String> update(Notice notice);

    Result<List<NoticeListItemVO>> query(NoticeQueryDto noticeQueryDto);

}
