package net.javaonrails.webnav.jmap.controller;

import net.javaonrails.webnav.jmap.servlet.JMapServlet;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.lang.reflect.InvocationTargetException;
import java.util.Locale;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class ControllerBase {

    private HttpServletRequest request;
    private HttpServletResponse response;
    private JMapServlet servlet;
    
    private Locale locale = Locale.getDefault();
    
    private String actionMethod = null;
    
    private String urlHint = null;
    
    public static String STD_ERROR_MSG_RESOURCE = "net/javaonrails/webnav/jmap/validator/errmessages";
    
    public static Log logger = LogFactory.getLog(ControllerBase.class);
    
    /**
     * This method is for the "/" uri
     * @param hint
     */
    public abstract void Index();

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
        return getServlet().createBean(this, this.getActionMethod());
    }
    
    public String getFormAction() {
        String q = request.getQueryString();
        return request.getContextPath() + request.getServletPath() 
                + request.getPathInfo() 
                + (StringUtils.isEmpty(q) ? "" : "?" + StringEscapeUtils.escapeHtml(q));
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

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public String getUrlHint() {
        return urlHint;
    }

    public void setUrlHint(String urlHint) {
        this.urlHint = urlHint;
    }
}
