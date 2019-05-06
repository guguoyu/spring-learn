package com.learn.spring.demo.webmvc.servlet;

import com.learn.spring.demo.webmvc.MyHandlerAdapter;
import com.learn.spring.demo.webmvc.MyHandlerMapping;
import com.learn.spring.demo.webmvc.MyViewResolver;
import com.learn.spring.demo.webmvc.context.MyApplicationContext;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Servlet只是作为一个MVC的启动入口
 *
 * @author guguoyu
 * @date 2019/4/14
 * @since JDK 1.8
 */

public class MyDispatcherServlet extends HttpServlet {

    private final String LOCATION="contextConfigLocation";

    //MyHandlerMapping 最核心的设计，也是最经典的
    private List<MyHandlerMapping> handlerMappings=new ArrayList<MyHandlerMapping>();

    private Map<MyHandlerMapping, MyHandlerAdapter> handlerAdapters = new HashMap<MyHandlerMapping, MyHandlerAdapter>();

    private List<MyViewResolver> viewResolvers = new ArrayList<MyViewResolver>();

    private MyApplicationContext context;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        //相当于把IOC容器初始化了
        context = new MyApplicationContext(config.getInitParameter(LOCATION));
        initStrategies(context);
    }

    private void initStrategies(MyApplicationContext context) {
        //有九种策略
        // 针对于每个用户请求，都会经过一些处理的策略之后，最终才能有结果输出
        // 每种策略可以自定义干预，但是最终的结果都是一致

        //文件上传解析，如果请求类型是multipart将通过MultipartResolver进行文件解析
        initMultipartResolver(context);
        //本地化解析
        initLocalResolver(context);
        //主题解析
        initThemeResolver(context);
        /*我们自己实现*/
        //MyHandlerMapping用来保存Controller中配置的RequestMapping和Method的一个对应关系
        initHandlerMapping(context);
        /*我们自己实现*/
        //MyHandlerAdapters用来动态匹配Method参数，包括类转换，动态赋值
        //通过HandlerAdapter进行多类型的参数动态匹配
        initHandlerAdapters(context);

        //如果执行过程中遇到异常，将交给HandlerExceptionResolver来解析
        initHandlerExceptionResolvers(context);
        //直接解析请求到视图名
        initRequestToViewNameTranslator(context);
        /*我们自己实现*/
        //通过viewResolvers解析逻辑视图到具体视图实现
        initViewResolvers(context);

        //flash映射管理器
        initFlashMapManager(context);

    }

    private void initFlashMapManager(MyApplicationContext context) {

    }

    private void initViewResolvers(MyApplicationContext context) {
    }

    private void initRequestToViewNameTranslator(MyApplicationContext context) {

    }

    private void initHandlerExceptionResolvers(MyApplicationContext context) {

    }

    private void initHandlerAdapters(MyApplicationContext context) {

    }
    //将Controller中配置的RequestMaping和Method进行一一对应
    private void initHandlerMapping(MyApplicationContext context) {

    }

    private void initThemeResolver(MyApplicationContext context) {

    }

    private void initLocalResolver(MyApplicationContext context) {

    }

    private void initMultipartResolver(MyApplicationContext context) {

    }
}
