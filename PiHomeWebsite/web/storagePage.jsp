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
		        
		        <s:if test="actionErrors.size > 0" >
				<div class="alert alert-danger">
			  		<p>
			        	<s:actionerror escape="false"/>
			        </p>
				</div>
				</s:if>
		        	         
		         <s:form theme="simple" action="manageFolders" id="storagePageFormId" cssClass="submit-once">     
		         
			         <div class="col-xs-9 col-md-6 main" style="margin-top:15px">			         	
				       	<label>Add new Folder</label>
				       	<div class="input-group ">				  
						   <s:textfield name="newFolder" cssClass="form-control" placeholder="Add new Folder" />
						   <span class="input-group-btn">
			   				  <button class="btn btn-default" type="submit" name="addNewFolderBtn" ><i class="fa fa-plus-circle"></i></button> 
			   			   </span>		
						 </div>						
			         </div>
			         
			         <div class="col-xs-18 col-md-12 main">
		
		         		<h3 class="page-header" > Folder List: </h3>
		         		<!-- buttons -->
		         		
			         	<div style="display:none" id="buttonsId">         		
			         		<s:hidden name="deleteFolderBtn" id="deleteBtnId" />
			         		<button type="button" class="btn btn-danger" title="Delete" data-toggle="modal" data-target="#confirm-delete"><i class="fa fa-trash" ></i>Delete</button> 
			         	</div>
		         		
		         		<div class="table-responsive">
			         		<table class="table table-hover" id="tableId">
						  		<thead>
							    	<tr>
							     		<th scope="col">Name</th>
							     		<th scope="col">Last Updated</th>
							     		<th scope="col">Owner</th>
							     		<th scope="col">Number of Files</th>
							     		<th scope="col">Folder Size</th>							     		
							     		<th></th>
							   		</tr>
							    </thead>
							    <tbody>
							    	<s:iterator value="dirList"  status="idx">
							    		
								   		<tr id="rowId<s:property value="#idx.index"  />" 
								   			  <s:if test="canDeleteFolder(#session.user.id)"> onclick="tableclick( <s:property value="#idx.index"  />,<s:property value="id"  /> );" 
								   			  style="cursor:pointer" </s:if>   >
								   			  
								   			<s:url id="viewFolderUrl" action="viewFolder" >
							    				<s:param name="folderId"><s:property value="id"/></s:param>
			 	   							</s:url>
							    			<s:hidden name="folderIdList[%{#idx.index}]" id="folderHiddenId%{#idx.index}"/>
								    		<td>
								    		<s:property value="folderName"/></td>
								    		<td><s:date name="lastUpated" format="yyyy-MM-dd" /></td>
								    		<td><s:property value="owner.fullName()"/></td>
								    		<td><s:property value="fileNumber"/></td>
								    		<td><s:property value="getReadeableFolderSize()"/></td>
								    		<td><s:a href="%{viewFolderUrl}" title="View" cssClass="btn btn-primary btn-xs"><span class="fa fa-user"></span>View</s:a>
								    			<s:if test="#session.user.id == ownerId">
								    				<button class="btn btn-success btn-xs" type="submit" name="shareFolderBtn" title="share"><i class="fa fa-share-alt-square"></i>Share</button>
								    			</s:if> 
								    		</td>
								   		</tr>
							   		</s:iterator>
							  	</tbody>
							</table>
						</div>
						
		         	</div>
		         	   	<%-- MODALE DELETE --%>
				 <div class="modal fade" id="confirm-delete" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
				    <div class="modal-dialog">
				        <div class="modal-content">
				            <div class="modal-header">
				             <strong> Delete Folders</strong>
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
       <!--  main content ENNDDDD  -->

    </div>
    <!-- /#wrapper -->

    <!-- jQuery -->
    <jsp:include page="/jsp/jquery.jsp" /> 
    
    <script>  
    var nbrRowSel = 0;
    var tdSelected = 0;
   	function tableclick(idx, id){
   		
   		if (tdSelected != 6){ //5 being the view  button row.
	   		
	   		if ($('#rowId'+idx).hasClass("success")){
	   			$('#rowId'+idx).removeClass("success");  			
	   			$('#folderHiddenId' + idx).val( null  );
	   			nbrRowSel = nbrRowSel - 1;
	   			
	   		}else{
	   			$('#rowId'+idx).addClass("success");
	   			$('#folderHiddenId' + idx).val( id );
	   			$('#buttonsId').show(300);
	   			nbrRowSel = nbrRowSel + 1;
	   		}
	   		
	   		if(nbrRowSel == 0){
	   			$('#buttonsId').hide(300);
	   		} 
   		}

   	}
    
    //pupulate the tdSelect to prevent the tableclick function to hilight the row.
    $("#tableId tbody td").on("mouseover",  function(event){    	
    	tdSelected = $(this).index();	 
    });
    
  	$("#deleteOk").click( function(){
  		$("#deleteBtnId").val("deleteBtn");
  		$("#storagePageFormId").submit();
  	});
 	
	//  $('#confirm-delete').on('show.bs.modal', function(e) {
	 //       $(this).find('.btn-ok').attr('href', $(e.relatedTarget).data('href'));
	  //      alert("sub");
	   //     $form.trigger('submit'); 
	        
	   // });
	  
    
    </script>
    
    <jsp:include page="/jsp/footer.jsp" /> 
