package com.petadoption.mapper;

import com.petadoption.pojo.entity.EvaluationsUpvote;

/**
 * 评论点赞持久化接口
 */
public interface EvaluationsUpvoteMapper {

    void save(EvaluationsUpvote evaluationsUpvote);

    void delete(EvaluationsUpvote evaluationsUpvote);

    int queryCount(EvaluationsUpvote evaluationsUpvote);

}
