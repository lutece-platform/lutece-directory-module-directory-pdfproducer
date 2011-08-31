<%@ page errorPage="../../../../ErrorPage.jsp" %>
<jsp:include page="../../../../AdminHeader.jsp" />
<jsp:useBean id="pdfProducerJspBean" scope="session" class="fr.paris.lutece.plugins.directory.modules.pdfproducer.web.PDFProducerJspBean" />
<% 
	pdfProducerJspBean.init( request, fr.paris.lutece.plugins.directory.web.ManageDirectoryJspBean.RIGHT_MANAGE_DIRECTORY);
%>
<%= pdfProducerJspBean.modifyConfigProducer( request ) %>
<%@ include file="../../../../AdminFooter.jsp" %>