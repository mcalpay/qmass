<HTML>
<BODY>
<%
   String name = request.getParameter( "username" );
   if(name != null && !name.isEmpty()) {
        session.setAttribute( "theName", name );
   }
%>
The name : <%=session.getAttribute( "theName")%>
<FORM METHOD=POST ACTION="testSession.jsp">
What's your name? <INPUT TYPE=TEXT NAME=username SIZE=20>
<P><INPUT TYPE=SUBMIT>
</FORM>
</BODY>
</HTML>