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
		       <div class="col-xs-18 col-md-12 main">  
		       
		       	<s:if test="actionErrors.size > 0" >
					<div class="alert alert-danger">
						<p>
					    	<s:actionerror escape="false"/>
					     </p>
					</div>
				</s:if>
				
		  		<h1><Strong>List Users</Strong> </h1>
		 	   	
		 	   	<i>Login block happend when a user tries to log in with the wrong password to many times (3 times)</i>
		 	   	<div class="table-responsive">
			 	   	<table class="table table-hover">
			 	   		<tr>
			 	   			<th>User Name</th>
			 	   			<th>Access Level</th>
			 	   			<th>Login Blocked</th>
			 	   			<th>Last Login</th>
			 	   			<th></th>
			 	   		</tr>
			 	   		<s:iterator value="userList" var="usr" status="indx">
			 	   			<tr>
			 	   				<s:url id="editUserUrl" action="addEditUser" >
			 	   						<s:param name="userId"><s:property value="#usr.id"/></s:param>
			 	   				</s:url>  
			 	   				<td>  	   				
			 	   					<s:a href="%{#editUserUrl}" >
						            	<s:property value="#usr.userName" />
							    	</s:a>
			 	   				</td>
			 	   				<td><s:property value="#usr.access.getAccess()" /></td>
			 	   				<td><s:url id="userAccess" action="blockUnBlockUser" >
			  								<s:param name="userId"><s:property value="#usr.id"/></s:param>
			  						</s:url> 	   				
			 	   					<s:if test="#usr.nbOfTries < 3">
			 	   						<s:a href="%{#userAccess}" title="Click to block access to the user">
						            		No
							    		</s:a> 
			 	   					</s:if>
			 	   					<s:else> 	   						
				  						<s:a href="%{#userAccess}" title="Click to unblock access to the user">
							            	Yes
								    	</s:a> 	   						
			 	   					</s:else>	   				
			 	   				</td>
			 	   				<td><s:date name="#usr.lastLogin" format="yyyy-MM-dd HH:mm.ss" /></td>
			 	   				<td><s:url id="deleteUserUrl" action="deleteUser" >
			 	   						<s:param name="userId"><s:property value="#usr.getId()"/></s:param>
			 	   					</s:url>			   			
									
									<s:a href="%{editUserUrl}" title="Edit User" cssClass="btn btn-primary btn-xs" id="editId" onclick="btnLoad('editId');"><span class="fa fa-user"></span>Edit User</s:a>								
									 	   					
			 	   					<button class="btn btn-danger btn-xs" data-href="<s:property value="#deleteUserUrl"/>" data-toggle="modal" data-target="#confirm-delete">
									    <span class="fa fa-times"></span>Delete
									</button>
			 			 	   				
			 	   				</td>
			 	   			</tr>
			 	   		</s:iterator>
			 	   	</table>
		 	   	</div>
		 	   	
		 	   	<%-- MODALE DELETE --%>
				 <div class="modal fade" id="confirm-delete" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
				    <div class="modal-dialog">
				        <div class="modal-content">
				            <div class="modal-header">
				             <strong> Delete User</strong>
				            </div>
				            <div class="modal-body">
				              Are you sure?
				            </div>
				            <div class="modal-footer">
				                <button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
				                <a class="btn btn-danger btn-ok">Delete</a>
				            </div>
				        </div>
				    </div>
				</div>					
					 	   	
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

    