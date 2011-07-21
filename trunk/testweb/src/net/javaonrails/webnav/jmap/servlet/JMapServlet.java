package net.javaonrails.webnav.jmap.servlet;

import net.javaonrails.webnav.jmap.controller.BasicBean;
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

import net.javaonrails.webnav.jmap.controller.ControllerBase;
import net.javaonrails.webnav.jmap.controller.CreateBean;
import java.util.Arrays;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Servlet implementation class JMapServlet
 */
public class JMapServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    
    private String prefixPackageName = "";
    
    public static final String CONTROLLER_PACKAGE = "controller.";
    
    public static final String BEAN_PACKAGE = "bean.";

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
        setPrefixPackageName(config.getString("jmap.controller.package", ""));
        } catch (Exception e) {
            getLogger().info("/WEB-INF/jmap-config.properties is not required\r\n"
                    + "But if you define a key: jmap.controller.package in it "
                    + "will be better to orgernize the packages.", e);
        }
    }
    
    private static Log logger = LogFactory.getLog(JMapServlet.class);
    public static Log getLogger() {
        return logger;
    }

    /**
     * You can override this method for other different behave.
     * @param cb is a annotation class of CreateBean
     * @param controller the controller class instance
     * @throws java.lang.reflect.InvocationTargetException
     * @throws java.lang.InstantiationException
     * @throws java.lang.IllegalAccessException
     */
    protected BasicBean createBean(CreateBean cb, ControllerBase controller) 
            throws InvocationTargetException, 
            InstantiationException, IllegalAccessException {
        
        String controllerClassName = controller.getClass().getSimpleName();
        String beanClassname = null;
        String attrName = null;
        String[] httpm = {"GET", "POST"};
        BasicBean bean = null;
        if (cb == null) {
            beanClassname = getBeanClassName(controller, controller.getActionMethod());
            if(logger.isDebugEnabled())
                logger.debug("CreateBean is null, default bean class name is " 
                    + beanClassname);
            attrName = controllerClassName + controller.getActionMethod();
            if(logger.isDebugEnabled())
                logger.debug("The bean Request attribute name is " + attrName);
        } else if (cb.create()) {
            httpm = cb.createOnHttpMethod();
            beanClassname = StringUtils.isEmpty(cb.beanClassName()) ? 
                getBeanClassName(controller, controller.getActionMethod()) : cb.beanClassName();
            attrName = StringUtils.isEmpty(cb.requestAttrName()) ? 
                controllerClassName + controller.getActionMethod() : cb.requestAttrName();
            if(logger.isDebugEnabled()) {
                logger.debug("The CreateBean is true, bean class name is "
                        + beanClassname);
                logger.debug("The bean Request attribute name is " + attrName);
            }
        }
        try {
            boolean createOnHttpMethod = false;
            String httpMethod = controller.getRequest().getMethod();
            if(httpm != null) {
                for(String method : httpm) {
                    httpMethod.equals(method);
                    createOnHttpMethod = true;
                    break;
                }
            }
            if(logger.isDebugEnabled())
                logger.debug("the createOnHttpMethod is " + Arrays.toString(httpm)
                    + ". The current HTTP method is " + httpMethod + ".");
            if (StringUtils.isNotEmpty(beanClassname) && createOnHttpMethod) {
                bean = createBean(controller, beanClassname);
                controller.getRequest().setAttribute(attrName, bean);
            }
        } catch (ClassNotFoundException e) {
            // do nothing
            logger.info("Cannot find the bean class " + beanClassname);
        }
        return bean;
    }
    /**
     * 
     * create and initialize the bean by the default rule.
     * the rule to find the bean is: packagePrefix + BEAN_PACKAGE + beanSimpleName
     * create this instance and call the abstract method init()
     * if the beanSimpleName contains '.', this name will be treated as full class
     * name, so the beanSimpleName will not add the package prefix name.
     * @param beanSimpleName, this is a simple class name, has no package name
     * @return
     */
    public BasicBean createBean(ControllerBase controller, String beanSimpleName) 
            throws InstantiationException, IllegalAccessException, 
            ClassNotFoundException, InvocationTargetException {
        BasicBean bean = null;
        String classname = getBeanClassName(controller, beanSimpleName);
        Class classbean = Class.forName(classname);
        bean = (BasicBean) classbean.newInstance();
        bean.initFormValues(controller);
        bean.execute();
        return bean;
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

    public String getBeanClassName(ControllerBase controller, String beanName) {
        return (beanName.indexOf('.') > -1) ? 
            beanName : 
            this.getPrefixPackageName() 
            + BEAN_PACKAGE + controller.getClass().getSimpleName().toLowerCase() 
            + "." + beanName;
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
     * @throws ServletException
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InstantiationExceptiond
     */
    @SuppressWarnings("unchecked")
    protected void processRequest(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
        try {
            response.setCharacterEncoding(characterEncoding);
            // The Sun AppServer should implement this setContentType call.
            response.setContentType(contentType);
            request.setCharacterEncoding(characterEncoding);

            String[] clz = getAction(request);

            Class controllerClass = Class.forName(getPrefixPackageName() 
                    + CONTROLLER_PACKAGE + clz[0]);

            Method m = controllerClass.getMethod(clz[1]);

            Object oc = controllerClass.newInstance();
            ControllerBase controller = (ControllerBase) oc;
            controller.setServlet(this);
            controller.setRequest(request);
            controller.setResponse(response);
            controller.setActionMethod(clz[1]);
            controller.setUrlHint(clz[2]);
            // modified on Jul 21st 2011 the return value is true then these 
            // actions will go on 
            if (controller.init()) {
	            createBean(m.getAnnotation(CreateBean.class), 
	                    controller);
	            m.invoke(oc);
            }
        } catch (ClassNotFoundException e) {
            // 404 response
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            getLogger().warn("Cannot find the class.", e);
        } catch (InstantiationException e) {
            // 501 error
            response.sendError(HttpServletResponse.SC_NOT_IMPLEMENTED);
            getLogger().error("Cannot instance the class.", e);
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
            getLogger().warn("Action class method not found.", e);
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
        this.processRequest(request, response);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        this.processRequest(request, response);
    }
}
