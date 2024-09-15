package com.sky.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author 帅的被人砍
 * @create 2024-09-15 10:26
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OperateLog {

    /**
     * id字段，用于唯一标识一个操作记录
     */
    private Long id;

    /**
     * employeeId字段，记录执行操作的员工ID
     */
    private Long employeeId;

    /**
     * operateTime字段，记录操作的时间
     */
    private LocalDateTime operateTime;

    /**
     * operateClass字段，记录操作所属的类名
     */
    private String operateClass;

    /**
     * operateMethod字段，记录操作的具体方法名
     */
    private String operateMethod;

    /**
     * operateParams字段，记录操作的参数信息
     */
    private String operateParams;

    /**
     * methodResult字段，记录方法的执行结果
     */
    private String methodResult;

    /**
     * costTime字段，记录方法执行的耗时，单位为毫秒
     */
    private Integer costTime;

}
