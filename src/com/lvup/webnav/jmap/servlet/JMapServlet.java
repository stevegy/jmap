package com.lvup.webnav.jmap.servlet;

import com.lvup.webnav.jmap.controller.BasicBean;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.commons.configuration.PropertiesConfiguration;

import com.lvup.webnav.jmap.controller.ControllerBase;
import com.lvup.webnav.jmap.controller.CreateBean;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Servlet implementation class JMapServlet
 */
public class JMapServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    
    private static final Class[] webMethodArgs = {String.class, String.class};

    private String prefixPackageName = "";
    
    public static final String CONTROLLER_PACKAGE = "controller.";

    private String characterEncoding;
    
    private String contentType;
    
    /**
     * @see HttpServlet#HttpServlet()
     */
    public JMapServlet() {
        super();
    // TODO Auto-generated constructor stub
    }

    /**
     * @see Servlet#init(ServletConfig)
     */
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        this.characterEncoding = config.getInitParameter("CharacterEncoding");
        this.contentType = config.getInitParameter("ContentType");
        if(this.characterEncoding == null) 
            this.characterEncoding = "UTF-8";
        if(this.contentType == null)
            this.contentType = "text/html; charset=" + characterEncoding;
        initJMap();
    }
    
    /**
     * This method cam be called by controller to re-initialize the config file.
     * Or, you may want to override this function.
     * This method get the /WEB-INF/jmap-config.properties through the 
     * org.apache.commons.configuration.PropertiesConfiguration
     */
    public void initJMap() {
        try {
        PropertiesConfiguration config = new PropertiesConfiguration();
        config.load(getServletContext()
                .getResourceAsStream("/WEB-INF/jmap-config.properties"));
        setPrefixPackageName(config.getString("jmap.controller.package", "controller."));
        } catch (Exception e) {
            getLogger().error("", e);
        }
    }
    
    private static Log logger = LogFactory.getLog(JMapServlet.class);
    public static Log getLogger() {
        return logger;
    }

    /**
     * You can override this method for other different behave.
     * @param m the caller method
     * @param controller the controller class instance
     * @param clz the string array, [0] is the controller's simple class name,
     * [1] is the method name, [2] is the hint string
     * @param request the HttpServletRequest object
     * @throws java.lang.reflect.InvocationTargetException
     * @throws java.lang.InstantiationException
     * @throws java.lang.IllegalAccessException
     */
    protected void createBean(Method m, ControllerBase controller, 
            String[] clz, HttpServletRequest request) 
            throws InvocationTargetException, 
            InstantiationException, IllegalAccessException {

        CreateBean cb = m.getAnnotation(CreateBean.class);
        String beanClassname = null;
        String attrName = null;
        if (cb == null) {
            beanClassname = controller.getBeanClassName(clz[1]);
            logger.debug("CreateBean is null, default bean class name is " + beanClassname);
            attrName = clz[0] + clz[1];
            logger.debug("The bean Request attribute name is " + attrName);
        } else if (cb.create()) {
            beanClassname = StringUtils.isEmpty(cb.beanClassName()) ? controller.getBeanClassName(clz[1]) : cb.beanClassName();
            attrName = StringUtils.isEmpty(cb.requestAttrName()) ? clz[0] + clz[1] : cb.requestAttrName();
            logger.debug("The CreateBean is true, bean class name is " + beanClassname);
            logger.debug("The bean Request attribute name is " + attrName);
        }
        try {
            if (StringUtils.isNotEmpty(beanClassname)) {
                BasicBean bean = controller.createBean(beanClassname);
                request.setAttribute(attrName, bean);
            }
        } catch (ClassNotFoundException e) {
            // do nothing
            logger.info("Cannot find the bean class " + beanClassname);
        }
    }

    /**
     * return the class name in String[0], 
     * action method name in String[1],
     * hint in the String[2]
     * 
     * @param request
     * @return
     */
    protected String[] getAction(HttpServletRequest request) {
        String[] ret = new String[3];
        String pathInfo = request.getPathInfo();
        String servletPath = request.getServletPath();
        String className = findControllerClassName(servletPath);
        String action = "Index"; // the controller class method
        String hint = "";
        if (StringUtils.isNotEmpty(pathInfo)) {
            if (pathInfo.length() > 1) {
                // pathInfo = "/classname/action/hint"
                int p = pathInfo.indexOf('/', 2);
                if (p > -1) {
                    action = this.normalizeName(StringUtils.substring(
                            pathInfo, 1, p));
                    hint = StringUtils.substring(pathInfo, p + 1);
                } else {
                    action = this.normalizeName(StringUtils.substring(
                            pathInfo, 1));
                }
            }
        }
        ret[0] = className;
        ret[1] = action;
        ret[2] = hint;
        return ret;
    }

    /**
     * You can override this function to define a new way to find the 
     * controller's class name.
     * If this application use the `/' as the servlet path, the Class Root
     * will be mapped.
     * The config file /WEB-INF/jmap-config.properties will define a key:
     * jmap.controller.package, this key will keep the package prefix name for
     * the controller Class.
     * 
     * @param servletPath
     * @return the full name with package name and you can use Class.forName
     * to find the Class.
     */
    protected String findControllerClassName(String servletPath) {
        if (servletPath.length() == 0) {
            return "Root";
        }
        return this.normalizeName(StringUtils.substring(servletPath, 1));
    }

    /**
     * get the prefix name from the web.xml? 
     * @return the controller and other class package's prefix name
     */
    public String getPrefixPackageName() {
        // TODO get the package prefix name from the configuration file
        return prefixPackageName;
    }

    public void setPrefixPackageName(String prefixPackageName) {
        this.prefixPackageName = prefixPackageName;
    }
    
    /**
     * 
     * @param request
     * @param response
     * @param method
     *            is "GET", "POST", "HEAD", "PUT", ...
     * @throws ServletException
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InstantiationExceptiond
     */
    protected void processRequest(HttpServletRequest request,
            HttpServletResponse response, String httpMethod)
            throws ServletException, IOException {
        try {
            response.setCharacterEncoding(characterEncoding);
            // The Sun AppServer should implement this setContentType call.
            response.setContentType(contentType);
            request.setCharacterEncoding(characterEncoding);

            String[] clz = getAction(request);

            Class controllerClass = Class.forName(getPrefixPackageName() 
                    + CONTROLLER_PACKAGE + clz[0]);

            Method m = controllerClass.getMethod(clz[1], webMethodArgs);

            Object oc = controllerClass.newInstance();
            ControllerBase controller = (ControllerBase) oc;
            controller.setServlet(this);
            controller.setRequest(request);
            controller.setResponse(response);
            controller.setActionMethod(clz[1]);
            controller.init();
            createBean(m, controller, clz, request);
            m.invoke(oc, httpMethod, clz[2]);
        } catch (ClassNotFoundException e) {
            // 404 response
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            getLogger().error("", e);
        } catch (InstantiationException e) {
            // 501 error
            response.sendError(HttpServletResponse.SC_NOT_IMPLEMENTED);
            getLogger().error("", e);
        } catch (IllegalAccessException e) {
            // also 501
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            getLogger().error("", e);
        } catch (SecurityException e) {
            // 403
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            getLogger().error("", e);
        } catch (NoSuchMethodException e) {
            // 404
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            getLogger().error("", e);
        } catch (IllegalArgumentException e) {
            // 404
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            getLogger().error("", e);
        } catch (InvocationTargetException e) {
            // 404
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            getLogger().error("", e);
        }

    }
    
    /**
     * This method will change the uri name to some other way.
     * You may override this function to supply another translate method.
     * By default, this method will translate the uri to capitalize way 
     * and replace the '.', ' ', '-' and '_' to empty string and cpaitalize 
     * the next character.
     * So, the uri like /index.html will be translate to IndexHtml, 
     * login-page will be LoginPage
     * 
     * @param uri
     * @return
     */
    protected String normalizeName(String uri) {
        char[] cap = {'.', ' ', '-', '_'};
        return WordUtils.capitalizeFully(uri, cap)
                .replaceAll("\\.", "")
                .replaceAll(" ", "")
                .replaceAll("-", "")
                .replaceAll("_", "");
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        this.processRequest(request, response, "GET");
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        this.processRequest(request, response, "POST");
    }
}
