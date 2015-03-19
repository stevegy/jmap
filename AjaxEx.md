# Introduction #

**Yes, actually, every web application support Ajax, whatever its platform, languages or frameworks.**


# Details #

Modify the `/WebContent/mains/index.jsp` to add the javascript library.
```
<script type="text/javascript" src="${pageContext.request.contextPath}/static/js/prototype-1.6.0.3.js"></script>
```

Add a jsp page for content included in the main page, you may look at the `/WebContent/mains/ajax-dynalist.jsp`. This page include a `<select>` control and some `<option>`.
```
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<div id="dynalist">
    <div>
        <select id="list1" name="list1" size="10">
            <option value="1">option1</option>
            <option value="2">option2</option>
            <option value="3">option3</option>
            <option value="4">option4</option>
            <option value="5">option5</option>
        </select>
     </div>
    <div id="divlist2"></div>
</div>
<script type="text/javascript">
    document.observe('dom:loaded', function() {
        // when the document loaded
        $('list1').observe('change', function(event) {
            // the uri 'update-list' will be mapped to method UpdateList
            new Ajax.Updater('divlist2', 'update-list/' + $('list1').value);
        });
    })
</script>
```

You may refer to the [prototype](http://prototypejs.org) javascript library guide for the `Ajax.Updater`. This url will mapped to `/jmap/main/update-list/{the selected option value}`. In this case, the `urlHint` is {the selected option value}. When you change the selection of the `list1` a request will be issued and a method named `UpdateList` will be called. For example, if you select the `option2`, the request uri will be `/main/update-list/2`.

Add these code to the `controller.Main` class:
```
    public void UpdateList() {
        try {
            this.render("/mains/selectlist.jsp");
        } catch(Exception e) {
            logger.error("", e);
        }
    }
```
This method will response to the uri `/jmap/main/update-list` and by the protocol of bean name and request attribute key name, the java bean class `bean.UpdateList` will be mapped to and the request attribute name `MainUpdateList` will be added.

You may notice this line `this.render("/mains/selectlist.jsp");`, this will output a jsp response to the client browser.

Create a new class `UpdateList` in the `bean` package:
```
public class UpdateList extends BasicBean {

    private ArrayList<String> options;
    
    @Override
    public void execute() {
        this.setOptions(new ArrayList<String>());
        for(int i=0; i<5; i++) {
            this.options.add("option " + i + " " 
                    + this.getController().getUrlHint());
        }
    }

    public ArrayList<String> getOptions() {
        return options;
    }

    public void setOptions(ArrayList<String> options) {
        this.options = options;
    }
}
```

Create a jsp file in the `/WebContent/mains/selectlist.jsp`:
```
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<select id="list2" name="list2" multiple="true" size="10">
    <c:forEach var="option" items="${MainUpdateList.options}">
    <option value="<c:out value="${option}" />"><c:out value="${option}" /></option>
    </c:forEach>
</select>
```
You can see how the `${MainUpdateList}` work in this page. This page will output a piece of html code and the prototype library will replace the DOM with these output. If you run this test page, you can click the `<select>` list1 and see a new `<select>` updated under it.