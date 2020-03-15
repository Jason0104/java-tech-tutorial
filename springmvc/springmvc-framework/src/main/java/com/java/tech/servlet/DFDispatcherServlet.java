package com.java.tech.servlet;

import com.java.tech.web.method.DFHandlerMethod;
import com.java.tech.web.method.DFRequestMappingInfoHandlerMapping;
import com.java.tech.web.view.DFModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * created by Jason on 2020/3/12
 */
public class DFDispatcherServlet extends DFrameworkServlet {

    private DFRequestMappingInfoHandlerMapping requestMappingInfoHandlerMapping;

    public DFDispatcherServlet() {
        this.requestMappingInfoHandlerMapping = new DFRequestMappingInfoHandlerMapping();
    }

    @Override
    protected void onRefresh() {
        initStrategies();
    }

    private void initStrategies() {
        initHandlerMappings();
    }

    private void initHandlerMappings() {
        requestMappingInfoHandlerMapping.registerMapping();
    }

    @Override
    protected void doService(HttpServletRequest request, HttpServletResponse response) {
        doDispatcher(request, response);

    }

    private void doDispatcher(HttpServletRequest request, HttpServletResponse response) {
        try {
            //获得url
            String requestUrl = request.getRequestURI();

            //根据url获得具体的handler
            DFHandlerExecutionChain handler = getHandler(requestUrl);
            if (handler == null) {
                noHandlerFound(request, response);
                return;
            }

            //执行handler方法
            DFModelAndView modelAndView = handler.handler();

            processDispatchResult(modelAndView, request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void processDispatchResult(DFModelAndView view, HttpServletRequest request, HttpServletResponse response) throws Exception {
        render(view, request, response);
    }

    private void render(DFModelAndView view, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String viewName = view.getView();
        request.getRequestDispatcher("/WEB-INF/pages/" + viewName + ".html").forward(request, response);
    }

    private void noHandlerFound(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
        response.getWriter().println("404");
    }

    private DFHandlerExecutionChain getHandler(String requestUrl) {
        DFHandlerMethod handlerMethod = requestMappingInfoHandlerMapping.getHandler(requestUrl);
        if (handlerMethod == null)
            return null;

        DFHandlerExecutionChain handlerExecutionChain = new DFHandlerExecutionChain(handlerMethod);
        return handlerExecutionChain;
    }
}
