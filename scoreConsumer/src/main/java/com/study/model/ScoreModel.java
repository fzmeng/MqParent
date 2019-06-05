package com.study.model;

import lombok.Data;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * @Description:
 * @Author mengfanzhu
 * @Date 2019/6/5 09:59
 * @Version 1.0
 */
@Data
public class ScoreModel {

    private Long id;
    private String orderNo;
    private Integer score;

    @Override
    public String toString(){
        return ReflectionToStringBuilder.toString(this);
    }

}
