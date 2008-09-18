package com.lvup.webnav.jmap.controller;

import com.lvup.webnav.jmap.servlet.JMapServlet;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.lang.reflect.InvocationTargetException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class ControllerBase {

    private HttpServletRequest request;
    private HttpServletResponse response;
    private JMapServlet servlet;
    
    private String actionMethod = null;
    
    public static final String BEAN_PACKAGE = "bean.";
    
    public static Log logger = LogFactory.getLog(ControllerBase.class);
    
    /**
     * This method is for the "/" uri
     * @param httpMethod
     * @param hint
     */
    public abstract void Index(String httpMethod, String hint);

    public String getBeanClassName(String beanName) {
        return (beanName.indexOf('.') > -1) ? 
            beanName : 
            this.getServlet().getPrefixPackageName() 
            + BEAN_PACKAGE + this.getClass().getSimpleName().toLowerCase() 
            + "." + beanName;
    }
    
    /**
     * this method will be called after the setServlet(), setRequest() and 
     * setResponse(). So, in this overrided function, the request, servlet and
     * response are avaiable.
     */
    public abstract void init();

    /**
     * This method will render the jsp file in the `path' parameter.
     * You can override this method to render other template system.
     * 
     * @param path the jsp file path
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     */
    protected void render(String path) throws ServletException, IOException {
        RequestDispatcher dispatcher = this.getRequest().getRequestDispatcher(path);
        if (dispatcher != null) {
            dispatcher.forward(this.getRequest(), this.getResponse());
        } else {
            throw new FileNotFoundException("Can not find the resource " + path);
        }
    }
    
    /**
     * 
     * create and initialize the bean by the default rule.
     * the rule to find the bean is: packagePrefix + BEAN_PACKAGE + beanName
     * create this instance and call the abstract method init()
     * if the beanName contains '.', this name will be treated as full class
     * name, so the beanName will not add the package prefix name.
     * @param beanName
     * @return
     */
    public BasicBean createBean(String beanName) 
            throws InstantiationException, IllegalAccessException, 
            ClassNotFoundException, InvocationTargetException {
        BasicBean bean = null;
        String classname = getBeanClassName(beanName);
        Class classbean = Class.forName(classname);
        bean = (BasicBean) classbean.newInstance();
        bean.initFormValues(this);
        bean.execute();
        return bean;
    }
    
    /**
     * Use the action method name to find the bean name.
     * If your package prefix name is "com.lvup.web.test." and the 
     * action method is "Login", then the class name should be 
     * "com.lvup.web.test.bean.Login"
     * @return
     * @throws java.lang.InstantiationException
     * @throws java.lang.IllegalAccessException
     * @throws java.lang.ClassNotFoundException
     */
    public BasicBean createBean() throws InstantiationException, 
            IllegalAccessException, ClassNotFoundException, InvocationTargetException {
        return createBean(this.getActionMethod());
    }
    
    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public void setServlet(JMapServlet servlet) {
        this.servlet = servlet;
    }

    public JMapServlet getServlet() {
        return servlet;
    }

    public String getActionMethod() {
        return actionMethod;
    }

    public void setActionMethod(String actionMethod) {
        this.actionMethod = actionMethod;
    }
}
