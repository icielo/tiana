/**
 * lincl
 * 2016年9月13日 上午9:20:33
 * 
 */
package com.lezic.tiana.web.aspect;

import java.lang.reflect.Method;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import com.lezic.tiana.util.DataUtil;
import com.lezic.tiana.util.ReflectionUtil;
import com.lezic.tiana.web.annotation.Log;
import com.lezic.tiana.web.cache.LogCache;
import com.lezic.tiana.web.util.SpringContextUtil;
import com.lezic.tiana.web.vo.LogVo;

/**
 * 日志切面
 * 
 * @author lincl
 * 
 */
@Aspect
@Component
public class LogAspect {

    private Logger logger = LogManager.getLogger();

    private LogCache logCache;

    @Around("@annotation(com.lezic.tiana.web.annotation.Log)")
    public Object doAround(ProceedingJoinPoint pjd) throws Throwable {
        // 获取Log注解
        Class<?> cl = pjd.getTarget().getClass();
        String methodName = pjd.getSignature().getName();
        Object[] args = pjd.getArgs();
        Class<?>[] parameterTypes = new Class<?>[args.length];
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < args.length; i++) {
            parameterTypes[i] = args[i].getClass();
            sb.append(ReflectionUtil.toString(args[i]) + ",");
        }
        Method method = cl.getDeclaredMethod(methodName, parameterTypes);
        Log log = method.getAnnotation(Log.class);

        // 打印日志
        logger.info("------ Begin：" + log.value());
        logger.debug("Class：" + cl.getName());
        logger.debug("Method：" + methodName);
        logger.debug("Args：" + DataUtil.trim(sb.toString(), ","));
        long beginTime = System.currentTimeMillis();
        Object result = pjd.proceed();
        long costTime = System.currentTimeMillis() - beginTime;
        logger.info("CostTime：" + costTime + "ms");
        logger.info("Result：" + result);
        logger.info("------ End：" + log.value());

        // 添加到日志队列中，由异步进程进行写日志操作
        LogVo logVo = new LogVo(UUID.randomUUID().toString(), beginTime, log.value(), result, costTime, null);
        if (logCache == null) {
            logCache = SpringContextUtil.getBean(LogCache.class);
        }
        logCache.add(logVo);
        return result;
    }
}
