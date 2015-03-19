# FAQ #

## Which files should be _configed_ ? ##
  * `/WEB-INF/web.xml`
> This is the java web container rule, we can not avoid this configuration for the servlet class and its url mapping. You may find the below text in the source code:
```
	<servlet>
		<description>
		</description>
		<display-name>JMapServlet</display-name>
		<servlet-name>JMapServlet</servlet-name>
   		<init-param>
			<param-name>CharacterEncoding</param-name> 
			<param-value>UTF-8</param-value> 
		</init-param>
		<init-param>
			<param-name>ContentType</param-name>
			<param-value>text/html; charset=UTF-8</param-value>
		</init-param>
		<servlet-class>net.javaonrails.webnav.jmap.servlet.JMapServlet</servlet-class>
	</servlet>
	<servlet-mapping>
		<servlet-name>JMapServlet</servlet-name>
		<url-pattern>/main/*</url-pattern>
	</servlet-mapping>
	<servlet-mapping>
		<servlet-name>JMapServlet</servlet-name>
		<url-pattern>/product/*</url-pattern>
	</servlet-mapping>
```
> You may notice that `init-param`, these encoding relative parameters have default values `UTF-8` if you don't want to change them.

  * `/WEB-INF/jmap-config.properties`
> This configuration file is optional and if you want a package prefix name for the controllers and beans class. This file just need only one line for the package prefix name:

> `jmap.controller.package = net.javaonrails.webnav.test.`

> You may notice the last character is `"."` and this dot is required, otherwise the controller and bean will be "net.javaonrails.webnav.testController".

> If you remove this file, all controller class package name will start with `controller.`, bean class package name will starts with `bean.`.

## Does your framework support Ajax? ##
> I think the Ajax is **_NOTHING_** to do with server side except [Comet](http://en.wikipedia.org/wiki/Comet_(programming)). Yes, in my opinion, this is a business tricky word after these yesrs. It is not a simple question about yes or no.

> Before the Gmail ground to this world, there is many _ajax_ things but used `frame` or `iframe`.

> The java web containers use servlet, it has a very clear http request handle pipe structure. It's easy to give a response of HTML piece or JSON data to the client to update a `<div>` or any block of area in the DOM use javascript. This kind of update avoid to refresh the whole page and reduce the server bytes output. In the age of ASP.NET 1.1, it is not easy to let the Ajax work and does not break the ASP.NET http handle pipe until the [Anthem](http://sourceforge.net/projects/anthem-dot-net) library exists.

> Until now, we still have 2 different way on ajax thing.

  * **One**. The web server just supply a set of data blocks, the client will get these data and use a large javascript library (may be Flex or JavaFX) to construct all kind of user interface. The particular libraries involve the DWR and many client side technology like ExtJS, YUI or a lot of js user interface libraries, or the new Flex something.

> Actually, in this way, they think that the web server just equals a database server, the HTTP is the protocol and the js libraries or Flex is a client side programming tools just like VB6 or Delphi did. The VB6 is running on top of the OS, but now, these things running in a client web browser. Even they have to face the same problem like the different OS and different browser behaves.

> What happen? Yes, we are going back to the old age. We are inventing the new version of VB6 or the other kind of Delphi and let them running on an OS named WEB Browser. Replace the database with a combining web server and a _real_ database server, replace the database client library communicating protocol with http. Hum..., by the way, I like the old fashion. Actually, before I dived into the web developing, I was a C++ and VB6 programmer.

  * **Two**. The web server supply a piece of HTML, the client javascript use DOM functions to update an area on the page. Many javascript libraries support this way, prototype, YUI and many others. So, in this way, programming language is not a matter, like asp(vbscript), php, python can support.

> So, let's look an example [AjaxEx](AjaxEx.md).