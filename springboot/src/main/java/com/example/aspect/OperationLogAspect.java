package com.example.aspect;

import com.example.anno.LogOperation;
import com.example.mapper.OperateLogMapper;
import com.example.entity.Account;
import com.example.entity.OperateLog;
import com.example.utils.TokenUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.Arrays;

@Aspect
@Component
public class OperationLogAspect {

    private static final Logger log = LoggerFactory.getLogger(OperationLogAspect.class);

    @Autowired
    private OperateLogMapper operateLogMapper;

    // 环绕通知
    @Around("@annotation(logOperation)")
    public Object around(ProceedingJoinPoint joinPoint, LogOperation logOperation) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = null;
        Throwable throwable = null;

        String className = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        String methodParams = Arrays.toString(joinPoint.getArgs());
        int userId = getCurrentUserId();

        log.info("开始执行: {}.{} | userId={} | params={}", className, methodName, userId, methodParams);

        try {
            result = joinPoint.proceed();
            return result;
        } catch (Throwable ex) {
            throwable = ex;
            log.error("执行异常: {}.{} | userId={} | params={}", className, methodName, userId, methodParams, ex);
            throw ex;
        } finally {
            long costTime = System.currentTimeMillis() - startTime;

            if (throwable == null) {
                log.info("执行完成: {}.{} | userId={} | cost={}ms | result={}",
                        className, methodName, userId, costTime, String.valueOf(result));
            } else {
                log.warn("执行结束(异常): {}.{} | userId={} | cost={}ms | error={}",
                        className, methodName, userId, costTime, throwable.getMessage());
            }

            try {
                OperateLog operateLog = new OperateLog();
                operateLog.setOperateEmpId(userId);
                operateLog.setOperateTime(LocalDateTime.now());
                operateLog.setClassName(className);
                operateLog.setMethodName(methodName);
                operateLog.setMethodParams(methodParams);
                operateLog.setReturnValue(throwable == null ? String.valueOf(result) : "EXCEPTION: " + throwable.getMessage());
                operateLog.setCostTime(costTime);

                operateLogMapper.insert(operateLog);
            } catch (Exception e) {
                log.error("操作日志落库失败: {}.{} | userId={}", className, methodName, userId, e);
            }
        }
    }

    // 从当前登录用户里获取用户 ID
    private int getCurrentUserId() {
        try {
            Account currentUser = TokenUtils.getCurrentUser();
            if (currentUser != null && currentUser.getId() != null) {
                return currentUser.getId();
            }
            return 0;
        } catch (Exception e) {
            // 日志记录失败不影响业务主流程
            return 0;
        }
    }
}
