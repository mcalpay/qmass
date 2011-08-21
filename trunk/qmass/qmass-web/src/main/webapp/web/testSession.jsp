<%@ page import="java.util.Enumeration" %>
<HTML>
<BODY>
<form method="post" action="testSession.jsp">
    Param name : <INPUT TYPE=TEXT NAME=paramname SIZE=20/><br/>
    Param value : <INPUT TYPE=TEXT NAME=paramvalue SIZE=20/><br/>
        <INPUT TYPE=SUBMIT />
</FORM>
<%
    String name = request.getParameter("paramname");
    if (name != null && !name.isEmpty()) {
        session.setAttribute(name, request.getParameter("paramvalue"));
    }

    Enumeration names = session.getAttributeNames();
    while (names.hasMoreElements()) {
        String attName = (String) names.nextElement();
%>
<%=attName%>, <%=session.getAttribute(attName)%><br/>
<%
    }
%>
</BODY>
</HTML>