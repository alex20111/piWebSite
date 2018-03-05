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
	

        <s:if test="actionErrors.size > 0" >
			<div class="alert alert-danger">
				<p>
			      	<s:actionerror escape="false"/>
			    </p>
			</div>
		</s:if>
         
         <br/>
         <div class="row">
         
        	<div class="col-md-8">
	         <s:form theme="simple" id="emailFormId" action="manageEmailsAction">
		         <section class="panel panel-primary">
		 				<header class="panel-heading">
		 					<h3 class="panel-title">Email Configuration</h3>
		 				</header>
		 				<div class="panel-body">
		 					<p class="help-block" >
								Configuration used in can the system needs to send e-mails.
							</p>
		 					<div class="form-group">
		 						<label for="emailUserId">Email user:</label>
		 						<s:textfield size="55" name="cfg.emailUserName" id="emailUserId" cssClass="form-control"/>
		 					</div>
		 					
		 					<div class="form-group">
		 						<label for="emailPassId">Email password:</label>
		 						<div class="input-group ">				  
							 	  <s:password size="25" name="cfg.emailPassword" id="emailPassId" cssClass="form-control"/>
							   		<span class="input-group-btn">
			   					 	 <button class="btn btn-default" type="button"  id="toggelPswdBtnId">Show Password</button>		   					  
			   				  		</span>			   
						
							 </div>
		 						
		 					</div>
		 					
		 					<div class="form-group">
		 						<button type="submit" name="saveEmailBtn" value="Save" id="saveBtnId" class="btn btn-primary" >Save </button>
		 						<button type="submit" name="testEmailBtn" value="Test Email" id="testEmailId" class="btn btn-primary" >Test Email </button>
		 					</div>
		 					
		 				</div>
		 		</section>
	        </s:form>       
	
  			</div>        
        </div>
        </div>
       <!--  main content ENNDDDD  -->

    </div>
    <!-- /#wrapper -->

    <!-- jQuery -->
    <jsp:include page="/jsp/jquery.jsp" /> 
 	<script>
	    
 	
 	$(document).ready(function(){
 		
 		$("#emailFormId").validate({
			errorElement :'div',
			errorClass:'alert alert-danger',
			errorPlacement: function(error, element) {         
			      error.insertBefore(element);
			},
			highlight: function(element, errorClass, validClass) {
			    $(element).removeClass(errorClass);
			},
			rules: {
				"cfg.emailUserName" : "required",
				"cfg.emailPassword" : "required"
		        },
			messages: {
				"cfg.emailUserName": "Please enter your User Name or e-mail address",
				"cfg.emailPassword": "Please enter your password"
			//      password: {
			 //       required: "Please provide a password",
			  //      minlength: "Your password must be at least 5 characters long"
			   //   },
			    //  email: "Please enter a valid email address"
			    },
		        submitHandler: function(form) {
		        	var submitButtonName =  $(this.submitButton).attr("name");
		        	
		        	var id = $("button[name="+submitButtonName+"]").attr("id");
		        	
		        	if (id.length > 0){
		        		alert(id);
		        		btnLoad(id);
		        	}

		            form.submit();
		          }

			
			
			
			 });
 		
 		$("#emailPassId").val("<s:property value="cfg.emailPassword"/>");
 		
 	});
 	
 	
 	$("#toggelPswdBtnId").click(function(){
 		
 		if ($("#emailPassId").hasClass("showPass")){
 			$("#emailPassId").attr("type", "password");
 			$("#emailPassId").removeClass("showPass");
 		}else{
 			$("#emailPassId").attr("type", "text");
 			$("#emailPassId").addClass("showPass");
 		}
 		
 		
 	});
 	
    	
    

	</script> 
	
	<jsp:include page="/jsp/footer.jsp" />
