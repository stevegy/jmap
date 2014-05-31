package net.javaonrails.webnav.test.controller;

import net.javaonrails.webnav.jmap.controller.*;
import net.javaonrails.webnav.test.bean.main.Login;
import net.javaonrails.webnav.test.bean.main.Upload;
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
     * 
     * if you return false, the mapped action method will not be called
     */
    @Override
    @SuppressWarnings("unchecked")
    public boolean init() {
        this.p = new HashMap();
        p.put("title", "Title from Index");
        p.put("pageContent", "/mains/default.jsp");
        this.getRequest().setAttribute("p", p);
        return true;
    }

    @SuppressWarnings("unchecked")
    public void Index() {
        try {
            // supply the basic bean class and deal with the file upload or 
            // normally POST or GET data
            // when the bean data function end, put the bean into the request
            // or other scope object, then render the jsp
            this.p.put("title", "Index page " + getRequest().getMethod() 
                    + " UrlHint is " + this.getUrlHint() );
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
    public void IndexHtml() {
        this.Index();
    }
    
    //@CreateBean(beanClassName="com.lvup.webnav.test.bean.main.Login", requestAttrName="MainLogin")
    @SuppressWarnings("unchecked")
    public void Login() {
        try {
            // Login login = (Login) createBean();
            // this.getRequest().setAttribute("MainLogin", login);
            
            Login login = (Login) getRequest().getAttribute("MainLogin");
            
            this.p.put("pageContent", "/mains/login.jsp");
            this.p.put("title", "Login " + getRequest().getMethod() 
                    + " UrlHint is " + this.getUrlHint() );
            this.render(INDEX_JSP);
        } catch(Exception ex) {
            logger.error("", ex);
        }    
    }
    
    @CreateBean(createOnHttpMethod={"POST"})
    @SuppressWarnings("unchecked")
    public void Upload() {
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
    
    @SuppressWarnings("unchecked")
    public void Dynalist() {
        try {
            p.put("pageContent", "/mains/ajax-dynalist.jsp");
            p.put("title", "Upload file test");
            this.render(INDEX_JSP);
        } catch(Exception e) {
            logger.error("", e);
        }
    }
    
    public void UpdateList() {
        try {
            this.render("/mains/selectlist.jsp");
        } catch(Exception e) {
            logger.error("", e);
        }
    }

}
