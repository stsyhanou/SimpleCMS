<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/tags.tld" prefix="s" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<html>

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Site</title>
    <link rel="stylesheet" href="http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css" />
    <script src="http://code.jquery.com/jquery-1.9.1.js"></script>
    <script src="http://code.jquery.com/ui/1.10.1/jquery-ui.js"></script>
    <script type="text/javascript">
        $(function() {
            $( ".menu").menu();
        });

//        $(document).ready(function(){
//            $('.close').click(function(){
//                document.location.href='/site?deleteId='+this.id;
//            });
//        });
    </script>
    <style>
        .ui-menu { width: 250px; }
    </style>
</head>

<body>

<table width="100%">
    <tr class="style">
        <td colspan="2" height="10%" align="center">
            <h1>
                <c:forEach var="root" items="${rootSections}">
                    <c:if test="${root.published}">
                        <a href="/site?rootId=${root.id}&sectionId=${root.id}">${root.name}</a>
                    </c:if>
                </c:forEach>
            </h1>
        </td>
    </tr>
    <tr>
        <td width="20%" height="90%">
            <ul class="menu">
            <s:section list="${sections}"/>
            </ul>
        </td>
        <td width="80%" height="80%" align="center">
            <div class="date">
                <c:out value="${mainSection.date}"/>
            </div>
            <div>
                ${mainSection.content.body}
            </div>
        </td>
    </tr>
</table>

</body>
</html>