# JMap
Automatically exported from code.google.com/p/jmap, due to Google decided to close the code.google.com. 

I would take this as MIT license.

JMap is a Rails like Java web application framework started from 2008. Check the [Wiki](https://github.com/stevegy/jmap/wiki) for simple user guide.

In a short, let's say, you have a URL is /jmap/main/login. You don't have to plan a configuration file for URL mapping. 
When this request coming to JMap, a Bean will be created 
and its execute() method will be called. This Bean instance will be set into the servlet Request scope.
The controller class package-prefix.controller.Main method Login() will be called.

Intresting? :) go to [Wiki](https://github.com/stevegy/jmap/wiki) page or checkout the source code to see what happen...

Important Note: 
Considering the CVE-2014-0114 (http://www.cvedetails.com/cve/CVE-2014-0114/), please upgrade to use commons-beanutils 1.9.2 and above.
