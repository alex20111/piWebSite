<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<%-- start header  --%>
	<jsp:include page="/jsp/header.jsp" />
		
		<style>
			.list-group-item {
		    	display: list-item;
			}
		</style>

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
		
			<s:if test="actionMessages.size > 0" >
				<div class="alert alert-success">
			  		<p>
			        	<s:actionmessage escape="false"/>
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
			
			<h1>Share folders</h1> <br/>
			
	         
	         <i class="fa fa-share-alt-square" style="cursor:pointer" id="shareLinkId" >Add Share</i>
	         <s:form theme="simple" action="manageShares" cssClass="submit-once">
	         	<div class="row">
		         <div style=" <s:if test="!expandAddInShare">display:none</s:if>" id="addShareId" class="col-md-4 panel panel-default">
		 
	    			<div class="panel-body">
	
		         	<div class="form-group">
		         		<label>Folders</label>
		         		 <s:select name="folderId" id="userListId"						
							 cssClass="form-control"
							
							 list="dirList != null ? dirList : #{'Student':'Student','Teacher':'Teacher'}"
							 listKey="id"
						 	 listValue="folderName"
						 	 headerKey=''
							 headerValue=""
						 /> 
		         	</div>
		         	<div class="form-group">
					    <label for="exampleSelect1">Users </label>
					    <s:select name="userId" id="userListId"						
							 cssClass="form-control"
							 list="userList != null ? userList : #{'Student':'Student','Teacher':'Teacher'}"
		
							 listKey="id"
						 	 listValue="fullName()"
						 	 headerKey=''
							 headerValue=""
						 />   
					    
					 </div>  
					 <label>Access Rights</label> <br/> 
		         	<div class="form-check">
						  <label class="form-check-label">
						  	<s:checkbox name="addDownChk"  cssClass="form-check-input"/>
						    Download
						  </label>
					</div>
					<div class="form-check">
						  <label class="form-check-label">
						  	<s:checkbox name="addUplChk" cssClass="form-check-input"/>
						    Upload
						  </label>					
					</div>
		         	<div class="form-check">
						  <label class="form-check-label">
						  	<s:checkbox name="addDelFolderChk"  cssClass="form-check-input"/>
						    Delete Folder
						  </label>					
					</div>
					<div class="form-check">					  
						  <label class="form-check-label">
						  	<s:checkbox name="addDelFileChk"  cssClass="form-check-input"/>
						    Delete File(s)
						  </label>
					</div>
		         	<s:submit name="addShareBtn" value="Add" cssClass="btn btn-default" />
		      		</div>
		      		
		         </div>
		         </div>
	 
	         
	         <br/>
	         
	        <h3 class="page-header">Shared To
	        	<s:if test="shareToList != null && shareToList.size() > 0">
	        	 	<s:submit name="updateShareBtn" value="Save Update" cssClass="btn btn-primary btn-xs" />        	
	        	 	<s:submit name="deleteShareBtn" value="Delete" cssClass="btn btn-danger btn-xs" cssStyle="display:none" id="displayDeleteId"/>
	        	 	   	 
	        	 </s:if>
	       	</h3>
	       	
	        	<s:if test="shareToList != null && shareToList.size() > 0">
	        		<a href="#"><i class="fa fa-chevron-right" id="expandAllId"> Expand all</i></a>
	        	</s:if>       	
	        	<ol class="list-group">
	        	
	        		<s:set var="index" value="%{0}"/>        	
	        	
		        	<s:iterator value="shareToList" status="idx">
		
		        	<li class="list-group-item">
		        	<div> 
		        		
		        		<a href="#">    			
							<span style="cursor:pointer" id="sharedToId<s:property value="#idx.index"/>" onclick="expandShare(<s:property value="#idx.index"/>)"><s:property value="fullName"/></span>	
						</a> 			       	
		        	</div>
		        	<div style="display:none" id="sharedToId<s:property value="#idx.index"/>Result" >
		        		<br/>
		        		<i class="fa fa-check-square-o" title="Select All" id="selectAllId" style="cursor:pointer">Select All</i>  
						<div class="table-responsive"> 
			        		<table class="table table-hover">
			        			<thead>
				        			<tr>
				        				 <th rowspan="2"></th>
									    <th rowspan="2">Folder</th>
									    <th colspan="4" style="text-align: center;">Access Rights</th>
									    <th rowspan="2">Created Date</th>
									    <th rowspan="2">Last Updated</th>
									</tr>
									<tr>
									    <th>Download</th>
									    <th>Upload</th>
									    <th>Delete Folder</th>
									    <th>Delete File(s)</th>
									</tr>
								</thead>
								<tbody> 
									<s:iterator value="share"  >							
									
										<s:hidden name="shareIdSh[%{#index}]" value="%{id}"/>
										<s:hidden name="userIdSh[%{#index}]" value="%{toUserId}"/>
										<tr>
										 	<td><s:checkbox name="delShare[%{#index}]"  cssClass="form-check-input shrDelCl" fieldValue="%{id}" onclick="isOneChecked()"/></td>
										  	<td><s:property value="folderName"/></td>
										    <td><s:checkbox name="updDownChk[%{#index}]"  cssClass="form-check-input" value="download"/></td>
										    <td><s:checkbox name="updUplChk[%{#index}]"  cssClass="form-check-input" value="upload"/></td>
										    <td><s:checkbox name="updDelFldChk[%{#index}]"  cssClass="form-check-input" value="deleteFolder"/></td>
										    <td><s:checkbox name="updDelFileChk[%{#index}]"  cssClass="form-check-input" value="deleteFile"/></td>
										    <td><s:date name="shareCreated" format="yyyy-MM-dd hh:mm:ss" /></td>
										    <td><s:date name="shareUpdated" format="yyyy-MM-dd hh:mm:ss" /></td>
					  					</tr>
					  					
					  					<s:set var="index" value="%{1 + #attr.index}"/>
				  					</s:iterator>
			  					</tbody>        		
			        		</table>
		        		</div>
		        	</div>
		        	</li>
		        	</s:iterator>
	        	</ol>
	        	<br/>
	        	
	        	
			<h3 class="page-header">Shared From</h3>		
			
			<ol class="list-group">       	
	       	
		        	<s:iterator value="shareFrom" status="idx">	
			        	<li class="list-group-item">
				        	<div> 	        		
				        		<a href="#">    			
									<span style="cursor:pointer"  onclick="expandShareFrom(<s:property value="#idx.index"/>)"><s:property value="fullName"/></span>	
								</a> 			       	
				        	</div>
				        	<div style="display:none" id="sharedFromId<s:property value="#idx.index"/>Result" >
				        		<br/>
				        		<i class="fa fa-check-square-o" title="Select All" id="selectAllId" style="cursor:pointer">Select All</i>  
								 
								 <div class="table-responsive">
					        		<table class="table table-hover">
					        			<thead>
						        			<tr>
											    <th rowspan="2">Folder</th>
											    <th colspan="4" style="text-align: center;">Access Rights</th>
											    <th rowspan="2">Created Date</th>
											    <th rowspan="2">Last Updated</th>
											</tr>
											<tr>
											    <th>Download</th>
											    <th>Upload</th>
											    <th>Delete Folder</th>
											    <th>Delete File(s)</th>
											</tr>
										</thead>
										<tbody> 
											<s:iterator value="share"  >													
												<tr>
												  	<td><s:property value="folderName"/></td>
												    <td><s:if test="download">Yes </s:if><s:else>No</s:else> </td>
												    <td><s:if test="upload">Yes </s:if><s:else>No</s:else></td>
												    <td><s:if test="deleteFolder">Yes </s:if><s:else>No</s:else></td>
												    <td><s:if test="deleteFile">Yes </s:if><s:else>No</s:else> </td>
												    <td><s:date name="shareCreated" format="yyyy-MM-dd hh:mm:ss" /></td>
												    <td><s:date name="shareUpdated" format="yyyy-MM-dd hh:mm:ss" /></td>
							  					</tr>			  			
						  					</s:iterator>
					  					</tbody>        		
					        		</table>
				        		</div>
				        	</div>
			        	</li>
		        	</s:iterator>
	        	</ol>
	        	<br/>
	        	
			
		</s:form>
		
		
        </div>
       <!--  main content ENNDDDD  -->

    </div>
    <!-- /#wrapper -->

    <!-- jQuery -->
    <jsp:include page="/jsp/jquery.jsp" /> 
    
    <script>
    $( document ).ready(function() {

   		<s:if test="actionErrors.size > 0">
   			$("#addShareId").show(300);
   		</s:if>

    });
 
    var expanded = false;
	$("#expandAllId").click(function(){
		
		if (expanded){
			for (var i = 0; i < <s:property value="shareToList.size()"/> ; i++){			
				$("#sharedToId"+i+"Result").hide(300);
			}
			expanded = false;
			$("#expandAllId").removeClass("fa-chevron-down");
  			$("#expandAllId").addClass("fa-chevron-right");
		}else{
			for (var i = 0; i < <s:property value="shareToList.size()"/> ; i++){			
				$("#sharedToId"+i+"Result").show(300);
			}
			expanded = true;
			$("#expandAllId").removeClass("fa-chevron-right");
  			$("#expandAllId").addClass("fa-chevron-down");
		}
    });
    
    
    $("#shareLinkId").click(function(){    	
    	$("#addShareId").toggle(300);
    });
    
    function expandShare(idx){
    	$("#sharedToId"+idx+"Result").toggle(300);
    }
    function expandShareFrom(idx){
    	$("#sharedFromId"+idx+"Result").toggle(300);
    }
    
    function isOneChecked(){
    	
    	var nbrSelected = $('.shrDelCl:checked').length;
    	
    	if (nbrSelected == 1){
    		$("#displayDeleteId").show(400);
    	}else if (nbrSelected == 0){
    		$("#displayDeleteId").hide(400);
    	}

    }
    
    </script>   
    

	<jsp:include page="/jsp/footer.jsp" />

