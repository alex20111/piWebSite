<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>


<%-- start header  --%>
	<jsp:include page="/jsp/header.jsp" />

	</head>
<%-- --%>

<body>

    <div id="wrapper">
        <!-- Navigation -->
        <nav class="navbar navbar-default navbar-static-top" role="navigation" style="margin-bottom: 0">        
        	<!--  	TOP NAV START 	-->
            <jsp:include page="/jsp/navMenu.jsp" />

			<!--  TOP NAV END  -->
			
			<!--  side menu -->
			<jsp:include page="/jsp/sideMenu.jsp" />
			<!--  side menu ENDDDD  -->           
        </nav>
	
	
	<!--main content start  -->
	
        <div id="page-wrapper">		
		
		<%--
			Display the errors, if any
		--%>		
		<s:if test="fieldErrors.size > 0" >
		    <div class="alert alert-danger"> 
		        <p>
			        <s:fielderror escape="false"/>
		        </p> 
		    </div>
		</s:if> 
		
		<s:if test="actionErrors.size > 0" >
		    <div class="alert alert-danger"> 
		    	<p>
		        	<s:actionerror escape="false"/>
		        </p>
		    </div>
		</s:if> 
		
		<s:if test="actionMessages.size > 0" >
		    <div class="alert alert-success"> 
		    	<p>
		        	<s:actionmessage escape="false"/>
		        </p>
		    </div>
		</s:if>
	      
	     	<h1><Strong>ACCESS DENIED.. Sorry!</Strong> </h1>
		
        </div>
       <!--  main content ENNDDDD  -->

    </div>
    <!-- /#wrapper -->

    <!-- jQuery -->
    <jsp:include page="/jsp/jquery.jsp" /> 
    

	<jsp:include page="/jsp/footer.jsp" />

    