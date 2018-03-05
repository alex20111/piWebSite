<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<%-- start header  --%>
	<jsp:include page="/jsp/header.jsp" />

	<script src="ckeditor/ckeditor.js"></script>

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
                <div class="col-lg-12">
                    <h1 class="page-header">Add/Edit Project</h1>
                </div>
				<div class="row">
                                <div class="col-lg-6">
                                
                                <s:if test="hasActionErrors()" >
							       	<div class="errors">
										<s:actionerror escape="false" />
									</div>
								</s:if>
								<s:if test="hasActionMessages()" >
									<div class="success">
										<s:actionmessage escape="false" />
									</div>
								</s:if>
                                
                                    <s:form role="form" action="saveProjectDetails" id="projectFormId"  theme="simple" 
                                    	    method="POST" enctype="multipart/form-data">
                                    	    
                                    	 <s:hidden name="project.id"/>                                   	    
                                    	    
                                        <div class="form-group">
                                            <label for="cname">Title</label>
                                            <s:textfield cssClass="form-control" name="project.title"  id="cname" required="true"/>                                                                       
                                        </div>
                                        <div class="form-group">       
                                            <label for="textAreaId">Description</label>
                                            <s:textarea cssClass="form-control ckeditor" rows="5" name="project.desc" id="textAreaId"/>
                                        </div>
                                        
                                       	<div class="form-group">
                                            <label class="control-label">References: </label>
											<div id="redGroupId">													
													<s:if test="!project.refs.isEmpty()">
														<s:iterator value="project.refs" status="index">
														
															<s:if test="#index.index == 0">
																<div class="input-group" >
															</s:if>
															<s:else>
																<div class="input-group" style="margin-top:8px" >
															</s:else>															
																<s:hidden name="project.refs[%{#index.index}].id" id="refId"/>
																<s:hidden name="project.refs[%{#index.index}].type" />																						
		
																<s:if test="type.name().equalsIgnoreCase('html')"> 														
																	<span class="input-group-addon htmlLink"  style="cursor:pointer"  > <i class="fa fa-unlink" title="Remove Html Link" > </i></span>
																	<s:textfield cssClass="form-control htmlTextFieldLink" cssStyle="text-decoration: underline;" name="project.refs[%{#index.index}].title"/>
																	<span class="input-group-addon " style="border: none; background-color: transparent;" >  
																		<a href="<s:property value='title'/> " class="testHtmlLink"  target="_blank"> <i class="fa fa-share-square" title="Go To link"></i></a>  
																 	</span>
																</s:if>
																<s:else>
																	<span class="input-group-addon htmlLink"  style="cursor:pointer"  > <i class="fa fa-link" title="Add Html Link" > </i></span>
																	<s:textfield cssClass="form-control"  name="project.refs[%{#index.index}].title"/>
																</s:else>
															</div>														
														</s:iterator>
													</s:if>
													<s:else>
														<div class="input-group">	
															<s:hidden name="project.refs[0].id" id="refId"/>
															<s:hidden name="project.refs[0].type" value="text"/>												
															<span class="input-group-addon htmlLink"  style="cursor:pointer"  > <i class="fa fa-link" title="Add Html Link" > </i></span> 													
															<s:textfield cssClass="form-control" cssStyle="text-decoration: underline;" name="project.refs[0].title"/>
														</div>
													</s:else>
												
											</div>
                                        
                                        	<p class="help-block">To remove just delete the text in the box.</p>
											<button type="button" class="btn btn-success btn-xs" id="addRefId"> <i class="fa fa-plus-circle fa-fw" ></i>Add Reference</button>
                                        </div>
                                        								
                                        
										<div class="form-group">
											<h3 class="page-header">Files</h3>											
                                            <!--<input type="file">-->
											
											<table class="table table-striped table-bordered table-hover" id="fileTableId">
												<thead>
													<tr>
														<th></th>
														<th>Name</th>
														<th>Size</th>
													</tr>
												</thead>
												<tbody>
													<s:iterator value="project.files" status="index">
														<tr>															
															<td><s:property value="#index.count"/>.
																<s:hidden name="project.files[%{#index.index}].id" />
																<s:hidden name="project.files[%{#index.index}].fileDiskName" id="fileDiskNameId%{#index.index}"/>
															</td>
															<td><s:textfield cssClass="form-control" name="project.files[%{#index.index}].fileName" id="fileTxtId%{#index.index}"  cssStyle="margin-bottom:5px"/>
																<s:file name="projectFileList[%{#index.index}]" id="fileId%{#index.index}" cssClass="changeFile" idIndex="%{#index.index}"/>														
															</td>
															<td class="deleteFile" clear-id="<s:property value="#index.index"/>" style="cursor:pointer"><i class="fa fa-times fa-fw" style="color:red" title="Delete"></i></td>
														</tr>
													</s:iterator>
																										
												</tbody>
											</table>
											<p class="help-block">To remove file just delete the text in the box. Or press the X to clear the box</p>
											<button type="button" class="btn btn-success btn-xs" id="addNewFileId"> <i class="fa fa-plus-circle fa-fw" ></i>Add File</button>
											
										</div>
                                        <button type="submit" class="btn btn-default">Save</button>
                                        <button type="reset" class="btn btn-default">Cancel</button>
                                    </s:form>
                                </div>
                               
                            </div>
                <!-- /.col-lg-12 -->
            </div>
		</div>
		<!--  main content ENNDDDD  -->

    </div>
    <!-- /#wrapper -->

    <!-- jQuery -->
    <jsp:include page="/jsp/jquery.jsp" /> 
    

 
<script>

	
	var fileCount =  0;
		
		$("#addNewFileId").click(function(){
			
			var tableRows = $('#fileTableId tbody tr').length;
			console.log("tableRows: " + tableRows)
			
			$('#fileTableId tbody').append('<tr ><td>New <input name="project.files['+tableRows+'].fileDiskName" id="fileDiskNameId'+tableRows+'" type="hidden" > </td>' +
											'<td><input type="text" name="project.files['+tableRows+'].fileName" class="form-control" style="margin-bottom:5px" id="fileTxtId'+ tableRows  +'"/>'+
											     '<input type="file" name="projectFileList['+tableRows+']" id="fileId'+tableRows+'" class="changeFile" idIndex="'+ tableRows  +'" ></td>'+
											'<td class="deleteFile" clear-id="'+tableRows+'" style="cursor:pointer"><i class="fa fa-times fa-fw" style="color:red" title="Delete"></i></td></tr>');
			fileCount++;
		});

	CKEDITOR.replace( 'textAreaId', {
	toolbarGroups: [
		{ name: 'document',	   groups: [ 'mode', 'document' ] },			// Displays document group with its two subgroups.
 		{ name: 'clipboard',   groups: [ 'clipboard', 'undo' ] },			// Group's name will be used to create voice label. 																		
 		{ name: 'basicstyles', groups: [ 'basicstyles', 'cleanup' ] },
		'/',// Line break - next group will be placed in new line.
		{ name: 'paragraph', groups: [ 'list', 'indent', 'blocks', 'align' ] },
		{ name: 'styles' },
 		{ name: 'links' }
	]

});
	
	
	
	var refIdx = <s:if test="!project.refs.isEmpty()" > <s:property value="project.refs.size()"/> - 1</s:if> <s:else>0</s:else>;
	$("#addRefId").click(function(){	
		console.log(refIdx);
		refIdx++;
		$('#redGroupId').append('<div class="input-group" style="margin-top:8px"> ' +										
								'<input type="hidden" name="project.refs['+refIdx+'].id" id="refId"/> ' +
								'<input type="hidden" name="project.refs['+refIdx+'].type" value="text" /> ' +	
								'<span class="input-group-addon htmlLink"  style="cursor:pointer " > <i class="fa fa-link" title="Html Link"> </i></span>' + 										
								'<input type="text"  name="project.refs['+refIdx+'].title" class="form-control" value="" id="refTextId"  />	' + 
								'</div> ');					
	});
		
	$('body').on('click', '.htmlLink', function() {

		var textField = $(this).next();
		var typeField = $(this).prev();
		
		console.log(textField);
		console.log(typeField);

		if (textField.hasClass("htmlTextFieldLink")){
			$(this).html('<i class="fa fa-link" title="Add Html Link" style="cursor:pointer"></i>  ');
			textField.removeClass("htmlTextFieldLink");
			textField.css({'text-decoration':''})
			textField.next().remove();
			typeField.val("text");	
		
		}else{			
			$(this).html('<i class="fa fa-unlink" title="Remove Html Link" style="cursor:pointer"></i>  ');
			textField.addClass("htmlTextFieldLink");
			textField.css({'text-decoration':'underline'})
			typeField.val("html");
			textField.after('<span class="input-group-addon " style="border: none; background-color: transparent;" > ' + 
							'<a href="' + textField.val()  + '" class="testHtmlLink"  target="_blank"> <i class="fa fa-share-square" title="Test link"></i></a>   </span> ');

		}				
	});
	
	//use current textbox link as a test link.
	$('body').on('click', '.testHtmlLink', function(e) {	
		
		var textField = $(this).parent().prev();
		
		console.log(textField);
		$(this).attr("href", textField.val());			
	});		  
  
	$('body').on("click",'.deleteFile', function(){
		console.log("in");
		console.log("deleteFile with ID: " + $(this).attr("clear-id"));
		
		//var textBox = $(this).prev();
		 // $(this).parent("tr:first").remove()
			$("#fileTxtId"+$(this).attr("clear-id")).val(" ");		 
		 
		});
	$('body').on('change', '.changeFile', function() {
	console.log("change");
	//$(".changeFile").on("change", function(){
		
		
		
//		var fullPath = $("#fileId"+$(this).attr("clear-id")).value.split('.')[0];
		var fullPath = $(this).val();
		var filename = fullPath.replace(/^.*[\\\/]/, '');
		console.log("fullpath: " + fullPath + " FileName: " + filename + " " + $(this).attr("idIndex"));
		
		$("#fileDiskNameId"+$(this).attr("idIndex")).val(filename);
		
	});
	
	
    </script>
    
    <jsp:include page="/jsp/footer.jsp" /> 


	
	
	

