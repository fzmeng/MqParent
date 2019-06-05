package com.study.mapper;

import com.study.model.ScoreModel;

/**
 * @author mengfanzhu
 * @Description:
 * @date 2019/6/5 09:58
 * @Version 1.0
 */
public interface ScoreMapper {

    Long insert(ScoreModel scoreModel);

    ScoreModel getByOrderNo(String orderNo);
}
