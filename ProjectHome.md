# Java On Rails #
It has a little bit Rails like and has some annotations for automatic form fields validation, and ... it's very small and quick. You don't need to know anything about the Spring or Struts and this framework do not depends on Spring or Struts. It only depends on apache java commons library and common java web container like tomcat or jetty. You can browse the source code `WEB-INF/lib` to check these jars. It's a really ligth and simple way. But, if you like, you can add your Spring support to your web application use this framework freely.

Currently, you may browse or checkout the source code here http://code.google.com/p/jmap/source/browse/, or go to Wiki JavaOnRails, [FAQs](FAQs.md) to know more. I'm constructing a web site on the [JavaOnRails](http://www.javaonrails.net/) for more contents in future.

Since the Jan 2014, Google code has stopped the file download in project host. I have moved the jar file download to Google Drive. The current version is [r78](https://drive.google.com/folderview?id=0BygWbkqNmgC_eGs3R25LblRHbEE&usp=sharing).


## Now, it can map the URI to Class and Method ##
/jmap/main/login

/jmap is a web context path, /main is the servlet map path that servlet map pattern is `/main/*`, this URI will be mapped to Controller Class `package.prefixname.controller.Main` and the Method name is Login. See the source code snippet below, you may find it in the source file `net.javaonrails.webnav.test.controller.Main.java`.

```
package net.javaonrails.webnav.test.controller;
...
public class Main extends ControllerBase {
...
    @CreateBean(beanClassName="net.javaonrails.webnav.test.bean.main.Login", requestAttrName="MainLogin")
    public void Login() {
        try {
            // If you use @CreateBean(false), you have to write 2 line code below.
            // Login login = (Login) createBean();
            // this.getRequest().setAttribute("MainLogin", login);
            // this request scope object may be used in jsp for data display.            
            Login login = (Login) getRequest().getAttribute("MainLogin");

            // the "p" is a HashMap for jsp file include
            this.p.put("pageContent", "/mains/login.jsp");
            this.p.put("title", "Login " + getRequest().getMethod() + 
               " hint string is " + this.getUrlHint());

            // the method "render" takes a string like "/mains/index.jsp"
            // in this case, INDEX_JSP is a final static string in class "Main"
            // you can refer to the /mains/index.jsp to understand the usage of HashMap "p"
            this.render(INDEX_JSP);
        } catch(Exception ex) {
            ex.printStackTrace();
        }    
    }
...
}
```
In that full Class name the `controller` is a fixed prefix. And the `net.javaonrails.webnav.test.` is the package prefix name.

Until now, the only thing needs to config is the 'package.prefixname', because the java package name rule, many developers are required to use some company name plus project name as the package prefix name.

If your another servlet map pattern is `/product/*`, the controller class will be mapped to `com.lvup.webnav.test.controller.Product`. If the URI is omitted like `/jmap/product`, the method 'Index' will be called in controller class.

## Supply a set of annotations to validate the form values ##
You may notice the `@CreateBean` annotation but this annotation is not required. You may ignore it, even do not give an annotation. But if you do not want the framework generate the form bean automatically for you, you have to add `@CreateBean(create=false)` on this response method.

The form bean will be created automatically even without any configuration file. Just like the above URI, /jmap/main/login will instance a java bean class `package.prefixname.bean.main.Login` and it will be add to servlet request attribute and the name key is `MainLogin`.

If you declare the fields with public getter and setter in the bean, and use the same names in the HTML form, these fields will be copied form values that the web client posted. You also can add some validating annotations on that field, the framework will validate these field according to these additional annotations.

You may notice the bean class full name with `bean.main`, yes, this is a default mapping, the `bean` is fixed and the `main` is the servlet map pattern. So, if your another servlet map pattern is `/product/*`, the mapped bean will be `net.javaonrails.webnav.test.bean.product.` plus a method name as a class name java bean.

For example, if the request URI is `/jmap/product`, the form bean class name `net.javaonrails.webnav.test.bean.product.ProductIndex` will be instanced and add to the servlet request attribute by the key `ProductIndex`. But, if the named class cannot be found, this will not stop anything and the controller method will still be called.

_source is ready to go, document not finished yet ..._
_and now, i'm checking the [JaValid](http://www.javalid.org/) for the annotated validation_
