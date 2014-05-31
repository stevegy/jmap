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