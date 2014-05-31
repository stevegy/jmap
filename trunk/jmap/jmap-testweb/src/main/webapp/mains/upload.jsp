<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<div>${MainUpload.formattedErrorMessage}</div>
<p>chkTest value is ${MainUpload.chkTest[0]}, ${MainUpload.chkTest[1]}, ${MainUpload.chkTest[2]}</p>
<div>
    <form id="frmUpload" name="frmUpload" method="post" action="upload" enctype="multipart/form-data">
    <dl>
    <dt>file 1</dt>
    <dd><input type="file" id="file1" name="file1" /></dd>
    <dt>file 2</dt>
    <dd><input type="file" id="file2" name="file2" /> </dd>
    <dt>Test for array input</dt>
    <dd>
        <input type="checkbox" id="chkTest" name="chkTest" value="1" /> chkTest1
        <input type="checkbox" id="chkTest" name="chkTest" value="2" /> chkTest2
        <input type="checkbox" id="chkTest" name="chkTest" value="3" /> chkTest3
    </dd>
    <dt>Text box</dt>
    <dd><input type="text" name="text1" id="text1" value="<c:out value="${MainUpload.text1}"/>" /></dd>
    
    <dt></dt>
    <dd><input type="submit" name="btnSubmit" value="Upload" /> </dd>
    </dl>
    </form>
</div>