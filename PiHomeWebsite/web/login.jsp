<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<%-- start header  --%>
<jsp:include page="/jsp/header.jsp" />

 <!-- Custom styles for this template -->    
   	<link href="css/login.css" rel="stylesheet">

</head>
<%-- close header --%>

<body> <%-- need that here --%>
	
	<%-- MAIN CONTENT START HERE --%>
	<div class="container">
	
	
    

    <div class="omb_login">
  
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
    
    
    	<h3 class="omb_authTitle">Login or <a href="#">Sign up</a></h3>
		<div class="row omb_row-sm-offset-3 omb_socialButtons">
		
    	    <div class="col-xs-4 col-sm-2">
		        <a href="#" class="btn btn-lg btn-block omb_btn-facebook">
			        <i class="fa fa-facebook visible-xs"></i>
			        <span class="hidden-xs">Facebook</span>
		        </a>
	        </div>
        	<div class="col-xs-4 col-sm-2">
		        <a href="#" class="btn btn-lg btn-block omb_btn-yahoo">
			        <i class="fa fa-yahoo visible-xs"></i>
			        <span class="hidden-xs">Yahoo</span>
		        </a>
	        </div>	
        	<div class="col-xs-4 col-sm-2">
		        <a href="#" class="btn btn-lg btn-block omb_btn-google">
			        <i class="fa fa-google-plus visible-xs"></i>
			        <span class="hidden-xs">Google+</span>
		        </a>
	        </div>	
		</div>

		<div class="row omb_row-sm-offset-3 omb_loginOr">
			<div class="col-xs-12 col-sm-6">
				<hr class="omb_hrOr">
				<span class="omb_spanOr">or</span>
			</div>
		</div>

		<form class="omb_loginForm" action="loginUser" autocomplete="off" method="POST" class="submit-once">
			<div class="row omb_row-sm-offset-3">
				<div class="col-xs-12 col-sm-6">	
				    
				    
				    <%-- user name --%>
				    	<s:if test="fieldErrors['userName'] != null" >
							<section class="alert alert-danger">
							   	<p>
							   		<s:fielderror escape="false">
										<s:param>userName</s:param>
									</s:fielderror>
								</p> 
					  		</section>
			  			</s:if>	
						<div class="input-group">
							<span class="input-group-addon"><i class="fa fa-user"></i></span>
							<input type="text" class="form-control" name="userName" placeholder="email address / user name" id="userNameId">
						</div>
						<span class="help-block"></span>
					<%-- password --%>
						<s:if test="fieldErrors['password'] != null" >
							<section class="alert alert-danger">
							   	<p>
							   		<s:fielderror escape="false">
										<s:param>password</s:param>
									</s:fielderror>
								</p> 
					  		</section>
			  			</s:if>					
						<div class="input-group">
							<span class="input-group-addon"><i class="fa fa-lock"></i></span>
							<input  type="password" class="form-control" name="password" placeholder="Password">
						</div>
	                  
	
						<button class="btn btn-lg btn-primary btn-block" type="submit">Login</button>
					
				</div>
	    	</div>
			<div class="row omb_row-sm-offset-3">
				<div class="col-xs-12 col-sm-3">
	
				</div>
				<div class="col-xs-12 col-sm-3">
					<p class="omb_forgotPwd">
						<button class="btn btn-link" type="submit" name="forgotPassBtn">Forgot password?</button>
					
					</p>
				</div>
			</div>	
		</form>    	
	</div>



        </div>
    
    <%-- MAIN CONTENT END HERE --%>

	<jsp:include page="/jsp/jquery.jsp" /> 
    
 	<!--  any jquery script added here -->

 <!-- Script -->
 <script>
 $(document).ready(function(){
      
	 //focus on the userid
   $("#userNameId").focus();
       
    }); 
</script>
    
<jsp:include page="/jsp/footer.jsp" /> 

