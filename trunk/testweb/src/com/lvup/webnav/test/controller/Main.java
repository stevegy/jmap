package com.lvup.webnav.test.controller;

import com.lvup.webnav.jmap.controller.*;
import com.lvup.webnav.test.bean.main.Login;
import com.lvup.webnav.test.bean.main.Upload;
import java.util.HashMap;

public class Main extends ControllerBase {
    
    /**
     * This is the default template jsp file path.
     */
    private final static String INDEX_JSP = "/mains/index.jsp";
    
    /**
     * This is an arbitrary variable name. This map will be initialized by the
     * servlet and put into the request scope and used by content include. 
     */
    private HashMap p = null;

    /**
     * This method will be called in JMapServlet after the controller bean
     * had been instanced and set the request, response and servlet object.
     */
    @Override
    public void init() {
            this.p = new HashMap();
            p.put("title", "Title from Index");
            p.put("pageContent", "/mains/default.jsp");
            this.getRequest().setAttribute("p", p);
    }

    public void Index(String hint) {
        try {
            // supply the basic bean class and deal with the file upload or 
            // normally POST or GET data
            // when the bean data function end, put the bean into the request
            // or other scope object, then render the jsp
            this.render(INDEX_JSP);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * this is response for the index.html or Index-html, index_html, index html
     * @param httpMethod
     * @param hint
     */
    public void IndexHtml(String hint) {
        this.Index(hint);
    }
    
    //@CreateBean(beanClassName="com.lvup.webnav.test.bean.main.Login", requestAttrName="MainLogin")
    public void Login(String hint) {
        try {
            // Login login = (Login) createBean();
            // this.getRequest().setAttribute("MainLogin", login);
            
            Login login = (Login) getRequest().getAttribute("MainLogin");
            
            this.p.put("pageContent", "/mains/login.jsp");
            this.p.put("title", "Login " + getRequest().getMethod());
            this.render(INDEX_JSP);
        } catch(Exception ex) {
            logger.error("", ex);
        }    
    }
    
    @CreateBean(createOnHttpMethod={"POST"})
    public void Upload(String hint) {
        try {
            if("POST".equals(getRequest().getMethod())) {
                Upload upload = (Upload) getRequest().getAttribute("MainUpLoad");
                // do something with upload ...
           }
            
            
            p.put("pageContent", "/mains/upload.jsp");
            p.put("title", "Upload file test");
        this.render(INDEX_JSP);
        } catch (Exception e) {
            logger.error("", e);
        }
    }

}
