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
     * Modified the return type to boolean on Jul 21st 2011 
     * You can set the return value to false to indicate the JMap servlet stop 
     * to call the rest of Bean Annotation creation and mapping method calling.
     * So, if you should return true if you want these parts running.
     * This change is for basically user authentication, if the session is not
     * there, you could return false to skip the rest steps.
     * 
     * =======================================================================
     * this method will be called after the setServlet(), setRequest() and 
     * setResponse(). So, in this overrided function, the request, servlet and
     * response are avaiable.
     * 
     */
    public boolean init() { return true; }

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
    	Object contextPath = request.getAttribute("javax.servlet.forward.context_path");
    	if(contextPath == null){
    		String q = this.getRequest().getQueryString();
    		String pathInfo = this.getRequest().getPathInfo();
    		// this is the tomcat version less then 6.0.18
    		return this.getRequest().getContextPath() + this.getRequest().getServletPath()
            + (StringUtils.isEmpty(pathInfo) ? "" : pathInfo)
            + (StringUtils.isEmpty(q) ? "" : "?" + StringEscapeUtils.escapeHtml(q));
    	} else {
	        Object q = request.getAttribute("javax.servlet.forward.query_string");
	        // Change from the tomcat 6.0.20?
	        // After the dispatch to a jsp file, the request.getServletPath() will
	        // return the jsp file path instead of servlet path. I am not clear
	        // this is a bug or not.
	        // refer to the Servlet spec. 2.4 - 8.4.2, these attributes should remain
	        // the original request even under the situation that multiplue forwards and
	        // subsequent includes are called.
	        // BUT, the tomcat 6.0.18 has some error in these specifications implement.
	        // In 6.0.18, these attributes like "javax.servlet.forward.context_path"
	        // are not correct implemented. The getAttribute will return null.
	        Object pathInfo = request.getAttribute("javax.servlet.forward.path_info");
	        return contextPath.toString()
	                + request.getAttribute("javax.servlet.forward.servlet_path").toString()
	                + ((pathInfo==null) ? "" : pathInfo.toString())
	                + ((q==null) ? "" : "?" + StringEscapeUtils.escapeHtml(q.toString()));
    	}
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
