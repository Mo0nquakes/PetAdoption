package com.petadoption.service;

import com.petadoption.pojo.api.Result;
import com.petadoption.pojo.dto.EvaluationsQueryDto;
import com.petadoption.pojo.entity.Evaluations;

import java.util.List;

/**
 * 评论服务接口
 */
public interface EvaluationsService {

    Result<Object> insert(Evaluations evaluations);

    Result<Object> list(Integer contentId, String contentType);

    Result<Object> query(EvaluationsQueryDto evaluationsQueryDto);

    Result<String> deleteById(Integer id);

    Result<Void> update(Evaluations evaluations);

    Result<Object> upvoteOperation(Evaluations evaluations);

}
