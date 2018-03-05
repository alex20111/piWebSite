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
		
			<div class="row">
                <div class="col-lg-12">
                    <h1 class="page-header">Item</h1>
                </div>
				<div class="row">
                                <div class="col-lg-5">
                                    <s:form role="form" action="itemManagement" id="itemFormId" cssClass="cmxform" theme="simple"
                                    	    method="POST" enctype="multipart/form-data">
                                    
                                    <s:hidden name="inventory.id"/>
                                    <s:hidden name="inventory.ownerId"/>
                                        <div class="form-group">
                                            <label for="cname">Name:</label>
                                            <s:textfield cssClass="form-control" name="inventory.name"  id="cname" required="true"/>
                                        </div>
										
										<div class="form-group " >
											<div class="row">
												<div class="col-sm-2 col-lg-2">
													<label for="qtyId">Quantity</label>
													 <s:textfield cssClass="form-control" name="inventory.qty" id="qtyId"/>
												</div>
											</div>
										</div>
										
                                        <div class="form-group">
                                            <label for="catId">Category</label>
                                             <s:textfield cssClass="form-control" name="inventory.category" list="type" id="catId"/>                                  
											  <datalist id="type">
												<option value="SMD">
												<option value="PCB">
												<option value="MCU">
												<option value="Wire">
												<option value="Resistor">
											  </datalist>
                                        </div>
                                         <div class="form-group">
                                            <label for="grpId">Group</label>
                                            <s:select theme="simple" cssClass="form-control"  name="inventory.groupId" list="#session.sideMenuInventory" listValue="groupName" listKey="id" id="grpId"/>
                                            
                                        </div>
										<div class="form-group">
                                            <label class="control-label">References: </label>
											<div id="redGroupId">	
													<s:if test="!inventory.references.isEmpty()">
														<s:iterator value="inventory.references" status="index">													
														
															<div class="input-group"  style="<s:if test="#index.index > 0">margin-top:8px </s:if> " >																									
																<s:hidden name="inventory.references[%{#index.index}].id"/>
																<s:hidden name="inventory.references[%{#index.index}].type"/>																						
		
																<s:if test="type.name().equalsIgnoreCase('html')"> 														
																	<span class="input-group-addon htmlLink"  style="cursor:pointer"  > <i class="fa fa-unlink" title="Remove Html Link" > </i></span>
																	<s:textfield cssClass="form-control htmlTextFieldLink" cssStyle="text-decoration: underline;" name="inventory.references[%{#index.index}].referenceName"/>
																	<span class="input-group-addon " style="border: none; background-color: transparent;" >  
																		<a href="<s:property value='referenceName'/> " class="testHtmlLink"  target="_blank"> <i class="fa fa-share-square" title="Go To link"></i></a>  
																 	</span>
																</s:if>
																<s:else>
																	<span class="input-group-addon htmlLink"  style="cursor:pointer"  > <i class="fa fa-link" title="Add Html Link" > </i></span>
																	<s:textfield cssClass="form-control"  name="inventory.references[%{#index.index}].referenceName"/>
																</s:else>
															</div>														
														</s:iterator>
													</s:if>
													<s:else>
														<div class="input-group">
															<s:hidden name="inventory.references[0].id"/>
															<s:hidden name="inventory.references[0].type" value="text"/>													
															<span class="input-group-addon htmlLink"  style="cursor:pointer"  > <i class="fa fa-link" title="Add Html Link" > </i></span> 													
															<s:textfield cssClass="form-control" cssStyle="text-decoration: underline;" name="inventory.references[0].referenceName"/>
														</div>
													</s:else>
												
											</div>
                                           	
                                           	<p class="help-block">To remove just delete the text in the box.</p>
											<button type="button" class="btn btn-success btn-xs" id="addRefId">Add </button>
                                        </div>

                                        <div class="form-group">
                                            <label for="detailsId" for="catId">Details</label>
                                            <s:textarea cssClass="form-control" rows="3" name="inventory.details" id="detailsId"/>
                                        </div>
                                        
                                        
                                      <%--   <div class="form-group">
                                         	 <label for="imageId" for="catId">QR code:</label> <br/>
                                         	 <s:if test="!inventory.qrCode.isEmpty()">
                                         	 	will display
                                         	 </s:if>
                                         	 <s:else>
                                         	 	<s:hidden name="inventory.qrCode" id="invHiddenQrcodeId"/>
                                         	 	<div id="displayQrCode"></div>
                                         	 	<button type="button" class="btn btn-success btn-xs" id="genQrCodeId"> Generate QR code</button>
                                         	 </s:else>
                                         </div> --%>
                                        
                                         <div class="form-group">                                         	
                                            <label for="imageId">Thumbnail:</label> <br/>
                                            	 <s:if test="!inventory.thumbBase64.isEmpty()">
                                            		<IMG SRC="data:image/jpg;base64, <s:property value="inventory.thumbBase64" /> " /><br/>
                                            		<s:hidden name="inventory.thumbBase64"/>
                                            	</s:if>
                                            <s:file name="thumbImage" cssClass="form-control" id="imageId"/>                                       
                                        </div>
                                        
                                        <button type="submit" class="btn btn-primary">Save</button>
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

	 <script src="js/jquery.validate.min.js"></script>

    <script>  
    $(document).ready(function(){
		  
		$( "#itemFormId" ).validate({
		  rules: {
		    "inventory.name": {
		      required: true,
		      minlength: 1
		    },
			"inventory.qty": {
		      required: true,
		      number : true
		    },
			"inventory.groupId": {
		      required: true
		    }			    			    
		  }
		});
 
	  var refIdx = <s:if test="!inventory.references.isEmpty()" > <s:property value="inventory.references.size()"/> - 1</s:if> <s:else>0</s:else>;
		$("#addRefId").click(function(){	
			console.log(refIdx);
			refIdx++;
			$('#redGroupId').append('<div class="input-group" style="margin-top:8px"> ' +										
									'<input type="hidden" name="inventory.references['+refIdx+'].id" id="refId"/> ' +
									'<input type="hidden" name="inventory.references['+refIdx+'].type" value="text" id="refTypeId"/> ' +	
									'<span class="input-group-addon htmlLink"  style="cursor:pointer " > <i class="fa fa-link" title="Html Link"> </i></span>' + 										
									'<input type="text"  name="inventory.references['+refIdx+'].referenceName" class="form-control" value="" id="refTextId"  />	' + 
									'</div> ');					
		});
			
		//add html link to textfield
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
	  
	});
    
  //  $("#genQrCodeId").click(function(){
   // 	console.log("Generate qR code by AJAX");
    	
   // 	$.getJSON("generateQR",{ }, function(data){

			//$("genQrCodeId").hide();//
			
		//});
    	
    //});
    </script>
    
    <jsp:include page="/jsp/footer.jsp" /> 
