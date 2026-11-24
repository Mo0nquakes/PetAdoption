package com.petadoption.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.petadoption.pojo.api.Result;
import com.petadoption.pojo.dto.ProposalFeedbackQueryDto;
import com.petadoption.pojo.entity.ProposalFeedback;
import com.petadoption.pojo.vo.ProposalFeedbackVO;

import java.util.List;

/**
 * 意见与反馈业务逻辑接口
 */
public interface ProposalFeedbackService extends IService<ProposalFeedback> {

    Result<String> saveEntity(ProposalFeedback proposalFeedback);

    Result<String> update(ProposalFeedback proposalFeedback);

    Result<List<ProposalFeedbackVO>> query(ProposalFeedbackQueryDto proposalFeedbackQueryDto);

}
