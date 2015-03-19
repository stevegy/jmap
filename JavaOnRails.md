# A java web framework like Rails does #

## Introduction ##

### Action mapping ###
Look at this URI: **/jmap/main/login/some-hint-like-a-id**

  * **/jmap** is the Java Web Context path for the web application. This context path will be mapped to the web server. This name may be changed by the web application deployer and can be any name. So, it cannot be a class name mapping rule to use.
  * **/main** is the servlet map path and defined as pattern `"/main/*"` in web.xml. This name `main` will be translated as package.prefixname.Main java class.
> This controller class should be extends from the `net.javaonrails.webnav.jmap.controller.ControllerBase`. The `ControllerBase` provide many basic methods to connect the servlet context with controller. And the `ControllerBase` also provide a jsp file render method. You may refer to the source code of this class, or, you can refer to the example code `net.javaonrails.webnav.jmap.test.controller.Main.java`.
  * and after the /main/ is all in path\_info, but the first word **/login** will be mapped to the method:
> `public void Login()` in the `Main` class. A String 'some-hint-like-a-id' will be passed to the controller field `urlHint`. You can use `getUrlHint()` to get it.

### Form values validating and mapping ###
If you leave the controller method `Login` without annotation, the default java bean class will been looking for in this rule:

> The default form bean class full name will be (package.prefixname.)bean.(servlet-path-name).(response-method-name). The `bean.` is a fixed name that will be added automatically.
> So, the last example URI: **/jmap/main/login/some-hint-like-a-id** will be mapped to a java bean class name: (package.prefixname.)bean.main.Login.java. This bean should extends the BasicBean or FileUploadBean. These base class supply more default behave and helper method to support developers.

You can add an annotation `@CreateBean` to the controller class method `Login` to control the form value bean creating rule. These annotation attributes are:

  * `create`: default value is `true`. This flag can be assigned to `false` that the framework will not create any bean class for form values mapping.
  * `createOnHttpMethod`: default value is a String array, `{"GET", "POST"}`. This value can control the bean creating when the HTTP method is `GET` or `POST`. If you want the bean to be created just only when a `HTTP POST` request come in, you can assign this value to `{"POST"}`.
  * `requestAttrName`: default is an empty string "". This empty string means _"use the **default rule** to create a request scope attribute key name"_. The **_default rule_** means the controller class name and method name. For example, the last URI /main/login will create a attribute key name is `MainLogin`. The created java bean instance will be put in the request scope object with `request.setAttribute("MainLogin", LoginInstance);`.
  * `beanClassName`: default is an empty string "". This empty string means _"use the **default rule** to locate a java class"_. If this class defination cannot be found, a warning message will be logged, otherwise a java bean instance will be created and a serial of standard method call will be issued and these standard calls will set the form values to the bean's properties. In this case, the **_default rule_** means (package.prefixname.)bean.(servlet-path-name).(response-method-name) java class name.

> If you donot like this **_default rule_** name, you also can assign another one use these annotation attributes.

> After the bean has been created, `initFormValues(ControllerBase)` method will be called by the servlet. The developer also can override this method and call `super.initFormValues ` let the base class map the form values and validating the fields.

> Then, another bean class method will be called, `execute()`, the developer can override this method. In the time, the bean instance has been initialized and form values has been validated and set. You can sit and check these values do some database operation, retrive business data put them into List or Map in this inherited class instance. The servlet will put this instance into the HttpServletRequest attribute use the _**default name**_ or any other name you assigned in `@CreateBean(requestAttrName="some_name_you_like")`, so that jsp file can easily get them back like this `<c:foreach var="user" items="${MainLogin.usersList}">`.

> At last, the controller method `Login()` will be called by the servlet. So, before the controller method be called, the form data and business data had been prepared and ready to go. Your code just check the bean's status if some error occurred and make a `render` call to write out the response.

> As you know, the Rails controller structure has a little problem: all uri mapping methods are defined in it, if you do not execute a strict developing specification, programmers may write all business code in the controller class so that class is too long to review. I suggest that you may write the business code out of the controller class, maybe the bean's `execute()` method is a good choice.

You may refer to the example page in the source package. There is a java bean to map the web request form values for `/main/login` page and bean's class is net.javaonrails.webnav.test.bean.main.Login.

You can find the class `Login` is extends from `BasicBean`. The form bean should be extended from the `BasicBean` or `FileUploadBean`. Actually, the `FileUploadBean` extends the `BasicBean` and override the `initFormValues` method to handle the `multipart/form-data` form enctype. If your bean should accept the file upload form data, you should declare the bean to extend the `FileUploadBean`.

Until now, I provide `@Required`, `@MaxLength`, `@MinLength`, `@RulePattern` field annotations to mark the form field validation rules. You can refer to the bean class `Login` in source code and take a look at the field `loginName` for example.

You can find these annotations' defination in the package `net.javaonrails.webnav.jmap.validator`. And now, i'm checking the [JaValid](http://www.javalid.org) to replace my own implement validators.

Here is a simple test of [Performance](Performance.md) of this framework.