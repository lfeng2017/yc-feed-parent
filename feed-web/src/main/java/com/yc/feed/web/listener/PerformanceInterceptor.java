package com.yc.feed.web.listener;

import org.apache.log4j.Logger;
import org.perf4j.StopWatch;
import org.perf4j.log4j.Log4JStopWatch;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by lfeng on 2017/1/17.
 * 利用perf4j来对各个请求做监控
 */
public class PerformanceInterceptor extends HandlerInterceptorAdapter {
    protected Logger logger = Logger.getLogger(this.getClass());

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        StopWatch stopWatch = new Log4JStopWatch();
        request.setAttribute("stopWatch", stopWatch);
        String reqURI = request.getRequestURI();
        stopWatch.start(reqURI);
        return super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        StopWatch stopWatch = (StopWatch) request.getAttribute("stopWatch");
        long spentTime = stopWatch.getElapsedTime();
        String reqURI = request.getRequestURI();
        stopWatch.stop(reqURI);
        super.afterCompletion(request, response, handler, ex);
    }
}

