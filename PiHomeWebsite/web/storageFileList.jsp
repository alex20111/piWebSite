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
		
		
		
		
		<!--  .fa-download  .fa-share-square-o  .  .fa-upload -->
	
		    <div class="container-fluid">
		      <div class="row">   			
		        <div class="col-xs-18 col-md-12 main">
			        <h1><s:property value="viewFolder.folderName"/></h1>
			        
			        <s:if test="actionErrors.size > 0" >
						<div class="alert alert-danger">
					  		<p>
					        	<s:actionerror escape="false"/>
					        </p>
						</div>
					</s:if>
					
					
					<s:form theme="simple" action="manageFiles" id="storageFileListFormId" cssClass="submit-once">
						<s:hidden name="folderId"/>
					
						
						
						<h3 class="page-header" > File List: 
							<s:if test="viewFolder.canUpload(#session.user.id)">
								<s:url id="uploadFileUrl"			action="toUploadFilePage" escapeAmp="false" >		
									<s:param name="folderId" ><s:property value="viewFolder.id"/></s:param>
								</s:url>
								<s:a href="%{uploadFileUrl}" title="Upload file" cssClass="btn btn-primary btn-xs"><span class="fa fa-upload"></span>Upload</s:a>
							</s:if>						
						</h3>
						<s:if test="viewFolder.sharedFolder(#session.user.id)">
							<p class="help-block" >
								<small>Owner: <s:property value="viewFolder.owner.fullName()"/></small>
							</p>
						</s:if>
		 
		 				<s:if test="viewFolder.files != null  && viewFolder.files.size() > 0">	 				
		 				<div class="pull-right"> <s:a href="#" title="Details" ><i class="fa fa-th-list fa-lg"></i></s:a></div>
		 				
		 				<s:if test="viewFolder.canDownload(#session.user.id) || viewFolder.canDeleteFile(#session.user.id)">	
		 					<div > <s:a href="#" title="Details" id="selectAllId"  onclick="selectAll()"><i class="fa fa-check-square-o" title="Select All"></i>Select All</s:a></div>
		 				</s:if>
		 		
		 					<div style="display:none" id="buttonsId">         		
		 						<s:if test="viewFolder.canDownload(#session.user.id)">	         						         			
			         				<button class="btn btn-primary" type="submit" name="downloadFilesBtn" title="Download files"><i class="fa fa-download"></i>Download</button>
			         			</s:if> 
			         			<s:if test="viewFolder.canDeleteFile(#session.user.id)">
			         				<s:hidden name="deleteFilesBtn" id="deleteBtnId" />	
			         				<button type="button" class="btn btn-danger" title="Delete" data-toggle="modal" data-target="#confirm-delete"><i class="fa fa-trash" ></i>Delete</button> 
			         			</s:if>
		         			</div>
		         			<div class="table-responsive">
				         		<table class="table table-hover" id="fileTableId">
							  		<thead>
								    	<tr>
								     		<th scope="col">Name</th>
								     		<th scope="col">File Created</th>
								     		<th scope="col">File Size</th>
								   		</tr>
								    </thead>
								    <tbody>
								    	<s:iterator value="viewFolder.files"  status="idx">
								    		
									   		<tr id="rowId<s:property value="#idx.index"  />"  
									   			<s:if test="viewFolder.canDeleteFile(#session.user.id) || viewFolder.canDownload(#session.user.id)">							   		
									   				 onclick="tableclick( <s:property value="#idx.index"  />,<s:property value="id"  /> );" style="cursor:pointer" 
									   			</s:if>
									   		  >
									   		  
									   				<s:hidden name="fileIdList[%{#idx.index}]" id="fileHiddenId%{#idx.index}"/>
									    		<td>						    			
									    			<s:property value="fileName"/>
									    		</td>
									    		<td><s:date name="fileCreated" format="yyyy-MM-dd" /></td>
									    		<td><s:property value="getReadeableFileSize()"/></td>							    							    				    		
									   		</tr>
								   		</s:iterator>
								  	</tbody>
								</table> 
							</div>		        
			        	</s:if>
			        	<s:else>No files</s:else>
			        	
			        	<%-- MODALE DELETE --%>
						 <div class="modal fade" id="confirm-delete" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
						    <div class="modal-dialog">
						        <div class="modal-content">
						            <div class="modal-header">
						             <strong> Delete File(s)</strong>
						            </div>
						            <div class="modal-body">
						              Are you sure?
						            </div>
						            <div class="modal-footer">
						                <button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
						                <a class="btn btn-danger btn-ok" id="deleteOk">Delete</a>
						            </div>
						        </div>
						    </div>
						</div>
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
    var nbrRowSel = 0;
    var allSelected = false;
   	function tableclick(idx, id){
   		   		
		if ($('#rowId'+idx).hasClass("success")){
			removeTableSuccessClass(idx);				
		}else{
			addTableSuccessClass(idx, id);
		}
		   		
		if(nbrRowSel == 0){
			$('#buttonsId').hide(300);
		}
	}
    
   	function selectAll(){
   		var rowCount = $('#fileTableId tbody tr').length;
   		
   		var filesIds;
   		
   		if (!allSelected){
   			var first = true;
   			
   			<s:set var="first" value="true"/>
   			
   			filesIds = [ <s:iterator value="viewFolder.files">
   							<s:if test="first">
   								<s:property value="id" /> 
   								<s:set var="first" value="false"/>
   							</s:if>
   							<s:else>
   								, <s:property value="id" /> 
   							</s:else>
   						 </s:iterator>
   						];   			
   		}
   		
   	   	for (var i = 0; i < rowCount ; i++){
   	     	   		
   	   		if (allSelected){
   	   			if ($('#rowId'+i).hasClass("success")){
   	   				removeTableSuccessClass(i);	
   	   			}   	 	  		
   	   		}else{
   	   			if (!$('#rowId'+i).hasClass("success")){ //no success class
   	   				addTableSuccessClass(i, filesIds[i]);
   	   			}   	   			
   	   		}
   	   	}
   	   	
   	   	if (allSelected){
   	   		allSelected = false;
   	   	}else{
   			allSelected = true;
   		}
   	   	
   	 	if(nbrRowSel == 0){
			$('#buttonsId').hide(300);
		}   		
   	}
   	
   	function addTableSuccessClass(idx, id){
   		$('#rowId'+idx).addClass("success");
	   	$('#fileHiddenId' + idx).val( id );
	   	$('#buttonsId').show(300);
	   	nbrRowSel = nbrRowSel + 1;
   	}
   	function removeTableSuccessClass(idx){		
	   	
	   	$('#rowId'+idx).removeClass("success");  			
		$('#fileHiddenId' + idx).val( null  );
		nbrRowSel = nbrRowSel - 1;
	   	
   	}

    
	    $("#deleteOk").click( function(){
	  		$("#deleteBtnId").val("deleteBtn");
	  		$("#storageFileListFormId").submit();
	  	});
    </script>
    
    <jsp:include page="/jsp/footer.jsp" /> 
