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
		
		<div class="container-fluid">
	    	<div class="row">		
	        	<div class="col-xs-7 col-md-5 main"> 	     
	      
			      <h1><Strong>User Manager</Strong> </h1>
			      
			      <s:form theme="simple" action="saveUser" id="addUserId" cssClass="submit-once">      	
			  
			      		<s:token/>
				      	<s:hidden name="userId"/>
				      
				      	<div class="form-group">
					      	<label>User Name:</label>
					      	<s:if test="fieldErrors['userName'] != null" >
								<section class="alert alert-danger">
								   	<p>
								   		<s:fielderror escape="false">
											<s:param>userName</s:param>
										</s:fielderror>
									</p> 
						  		</section>
					  		</s:if>		      	
					      	<s:textfield name="userName" cssClass="form-control" required="required"/>
				      	</div>
				      	<div class="form-group">
					      	<label>First Name:</label>
					      	<s:if test="fieldErrors['firstName'] != null" >
								<section class="alert alert-danger">
								   	<p>
								   		<s:fielderror escape="false">
											<s:param>firstName</s:param>
										</s:fielderror>
									</p> 
						  		</section>
					  		</s:if>			      
					      	<s:textfield name="firstName" cssClass="form-control" required="required"/>
				      	</div>
				      	<div class="form-group">
					      	<label>Last Name:</label>
					      	<s:textfield name="lastName" cssClass="form-control"/>
				      	</div>
				      	<div class="form-group">
					      	<label for="usrEmail">E-Mail:</label>
					      	<s:if test="fieldErrors['email'] != null" >
								<section class="alert alert-danger">
								   	<p>
								   		<s:fielderror escape="false">
											<s:param>email</s:param>
										</s:fielderror>
									</p> 
						  		</section>
					  		</s:if>					
							<s:textfield type="email" name="email" size="25" placeholder="Email" id="usrEmail" cssClass="form-control"/>
				      	</div>
				      	<label for="usrEmail">API Key:</label>
				      	<div class="input-group">
				      		<div id="apiKeyTextId">
							   	<s:if test="!apiKey.isEmpty()">
							   		<s:property value="apiKey"/>						   	
							   	</s:if>
							   	<s:else>
							   		No Api Key. Please click Generate Key.						   		
							   	</s:else>
						   	</div>
						   	<s:hidden name="apiKey" id="apiKeyId"/>
						   	
						    <span class="input-group-btn">
						     <button class="btn btn-default" type="button" id="genApiKey">Generate Key</button>
						    </span>
						  </div>
				      	<div class="form-group">
				      		
				      	</div>
				      	<div class="form-group">
					      	<label>Access Type:</label>
					      	<s:if test="fieldErrors['access'] != null" >
								<section class="alert alert-danger">
								   	<p>
								   		<s:fielderror escape="false">
											<s:param>access</s:param>
										</s:fielderror>
									</p> 
						  		</section>
					  		</s:if>	     	
					      	
					      	<s:if test="#session.user.isAdministrator()">
					      		<s:select theme="simple" name="access" cssClass="form-control" list="@net.project.enums.AccessEnum@values()" listValue="getAccess()"/>
					      	</s:if>
					      	<s:else>
					      		<s:select theme="simple" name="access" cssClass="form-control" list="@net.project.enums.AccessEnum@values()" listValue="getAccess()" disabled="true"/>
					      		<s:hidden name="access"/>
					      	</s:else>
				      	</div>
				      	<br/>
				      	<s:submit type="button" name="btnSave" value="Save" cssClass="btn btn-primary" id="saveUserId" />		      
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
    
    
    <script>
    	$("#saveUserId").click(function(){ 
    		
    		$("#addUserId").validate({
    			errorElement :'div',
    			errorClass:'alert alert-danger',
    			errorPlacement: function(error, element) {         
    			      error.insertBefore(element);
    			},
    			highlight: function(element, errorClass, validClass) {
    			    $(element).removeClass(errorClass);
    			  }    		
    			 });
    		
        		if ($("#addUserId").valid()){
    				btnLoad('saveUserId');
    			}
    	});
    	
    	$("#genApiKey").click(function(){
    		
    		btnLoad('genApiKey');
    		
    		$.getJSON("genKey", function(data){
   			
    			var str = data;

    			$("#apiKeyId").val(str);
    			
    			
    			
    			$("#apiKeyTextId").html(data);
    			$("#genApiKey").html("Generate Key");
    			
    		});
    	});
    	
    </script>
    

	<jsp:include page="/jsp/footer.jsp" />

    