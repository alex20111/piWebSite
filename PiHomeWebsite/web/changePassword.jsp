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
			<div class="container-fluid">
			      <div class="row">		
			        <div class="col-xs-7 col-md-5 main">  
				
				
				
					<h1>Change Password</h1>
			
			          <s:if test="actionErrors.size > 0" >
						<div class="alert alert-danger">
							<p>
							   	<s:actionerror escape="false"/>
						    </p>
						</div>
					 </s:if>
			         <s:if test="hasActionMessages()" >
			         	<div class="alert alert-success">
							<p>
							   	<s:actionmessage escape="false" />
						    </p>
						</div>
					</s:if>
			         
			         
				        <s:form theme="simple" action="savepassword" onsubmit="document.body.style.cursor='wait';" cssClass="submit-once">
				      		<s:token/> 
							
							<div class="form-group">
						      	<label>New Password:</label>
								<s:if test="fieldErrors['newpass'] != null" >
									<section class="alert alert-danger">
									   	<p>
									   		<s:fielderror escape="false">
												<s:param>newpass</s:param>
											</s:fielderror>
										</p> 
							  		</section>
						  		</s:if>	
								
								<s:password name="newPassword" size="20" cssClass="form-control"/> <br/>
							</div>
							
							<div class="form-group">
						      	<label>Confirm New Password:</label>
								<s:if test="fieldErrors['confpass'] != null" >
									<section class="alert alert-danger">
									   	<p>
									   		<s:fielderror escape="false">
												<s:param>confpass</s:param>
											</s:fielderror>
										</p> 
							  		</section>
						  		</s:if>	
								
								<s:password name="confirmPassword" size="20" cssClass="form-control"/>
							</div>			
					
							<s:submit value="Save" cssClass="btn btn-primary"/> <br/><br/>				
							
					  </s:form>
			       </div>
			               
			       </div>
       		</div>
        </div>
       <!--  main content ENNDDDD  -->

    </div>
    <!-- /#wrapper -->

    <!-- jQuery -->
    <jsp:include page="/jsp/jquery.jsp" /> 
    

	<jsp:include page="/jsp/footer.jsp" />

