<%@ page errorPage="../../../../../ErrorPage.jsp" %><jsp:useBean id="directoryPDFJspBean" scope="session" class="fr.paris.lutece.plugins.directory.modules.pdfproducer.web.DirectoryPDFJspBean" /><%
	directoryPDFJspBean.init( request, fr.paris.lutece.plugins.directory.web.ManageDirectoryJspBean.RIGHT_MANAGE_DIRECTORY);
	directoryPDFJspBean.doDownloadPDF( request , response );
%>