<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<%-- start header  --%>
<jsp:include page="/jsp/header.jsp" />

</head>
<%-- --%>

<body>

	<div id="wrapper">
		<!-- Navigation -->
		<nav class="navbar navbar-default navbar-static-top" role="navigation"
			style="margin-bottom: 0">
			<!--  	TOP NAV START 	-->
			<jsp:include page="/jsp/navMenu.jsp" />

			<!--  TOP NAV END  -->

			<!--  side menu -->
			<jsp:include page="/jsp/sideMenu.jsp" />
			<!--  side menu ENDDDD  -->
		</nav>


		<!--main content start  -->

		<div id="page-wrapper">
			<s:if test="actionErrors.size > 0">
				<div class="alert alert-danger">
					<p>
						<s:actionerror escape="false" />
					</p>
				</div>
			</s:if>
			<div class="row">
				<div class="col-lg-12">
					<h1>Inventory List</h1>
				</div>
				<!-- /.col-lg-12 -->
			</div>
			<s:if test="groupId != null && groupId > 0">
				<s:url id="renameGroupUrl" action="addNewItem">
					<s:param name="groupId">
						<s:property value="groupId" />
					</s:param>
				</s:url>
				
				<h3 class="page-header">
					<strong>Group: 
						<s:a href="%{#renameGroupUrl}"  id="renameIdLink" title="Rename Group">
					
						<s:property value="#session.groupName" /><small><i class="fa fa-pencil "></i></small></s:a>	
					</strong>
				</h3>
				<div id="errorsId" class="alert alert-danger" style="display: none"></div>
			</s:if>
			<s:else>
				<h3 class="page-header">
					<strong>All Items</strong>
				</h3>
			</s:else>



			<%-- Delete group --%>
			<s:if test="groupId != null && groupId > 0">
				<%-- add new item --%>
				<s:url id="newItemUrl" action="addNewItem">
					<s:param name="groupId">
						<s:property value="groupId" />
					</s:param>
				</s:url>
				<s:a href="%{#newItemUrl}" cssClass="btn btn-primary btn-xs "
					style="margin-bottom:10px">
					<i class="fa fa-pencil "></i>Add Item </s:a>

				<%--Delete --%>
				<s:url id="deleteGroupUrl" action="deleteGrp">
					<s:param name="groupId">
						<s:property value="groupId" />
					</s:param>
				</s:url>
				<s:a href="%{#deleteGroupUrl}" cssClass="btn btn-danger btn-xs "
					cssStyle="margin-bottom:10px" id="deleteLnkId">
					<i class="fa fa-times "></i>Delete Group </s:a>
			</s:if>
			<s:else>
				<s:url id="newItemUrl" action="addNewItem">
				</s:url>
				<s:a href="%{#newItemUrl}" cssClass="btn btn-primary btn-xs "
					cssStyle="margin-bottom:10px">
					<i class="fa fa-pencil "></i>Add Item </s:a>
			</s:else>

			



			<!-- /.row -->
			<table width="100%"
				class="table table-striped table-bordered table-hover"
				id="itemsTable">
				<thead>
					<tr>
						<th></th>
						<th>Name</th>
						<s:if test="groupId == null || groupId <= 0">
							<th>Group</th>
						</s:if>
						<th>Category</th>
						<th>Qty.</th>
						<th>Reference</th>
						<th></th>
					</tr>
				</thead>
				<tbody>
					<s:iterator value="inventoryList" status="idx">
						<tr>

							<td><s:property value="#idx.count" />. <%--	<s:if test="thumbBase64.length() > 0"> --%>
								<IMG
								SRC="data:image/jpg;base64, <s:property value="thumbBase64" /> " />

							</td>
							<td><s:property value="name" /></td>
							<s:if test="group != null">
								<td><s:property value="group.groupName" /></td>
							</s:if>
							<td><s:property value="category" /></td>
							<td><s:property value="qty" /></td>
							<td><ul>
									<s:iterator value="references">
										<s:if test="type.name().equalsIgnoreCase('html')">
											<li><a href="<s:property value="referenceName"/>"
												target="_blank"><s:property value="referenceName" /> </a></li>
										</s:if>
										<s:else>
											<li><s:property value="referenceName" /></li>
										</s:else>
									</s:iterator>
								</ul></td>
							<td><s:url id="editUrl" action="editItem">
									<s:param name="inventory.id">
										<s:property value="id" />
									</s:param>
								</s:url> <i class="fa fa-info-circle "
								style="color: blue; cursor: pointer; margin-right: 5px"
								title="details"> </i> <s:a href="%{#editUrl}">
									<i class="fa fa-edit"
										style="color: blue; cursor: pointer; margin-right: 5px"
										title="Edit"> </i>
								</s:a> <span class="deleteRow" itemId="<s:property value="id"/> "><i
									class="fa fa-times" style="color: red; cursor: pointer;"
									title="delete"> </i> </span></td>
						</tr>
					</s:iterator>


				</tbody>
			</table>



			<div id="confirm" class="modal  fade" role="dialog">
				<div class="modal-dialog">
					<div class="modal-content">
						<div class="modal-header">Delete Group</div>
						<div class="modal-body">Are you sure?</div>
						<div class="modal-footer">
							<button type="button" data-dismiss="modal" class="btn btn-danger"
								id="delete">Delete</button>
							<button type="button" data-dismiss="modal" class="btn btn-default"
								id="delCancel">Cancel</button>
						</div>
					</div>
				</div>
			</div>

			<div id="renameGroupModal" class="modal  fade" role="dialog">
				<div class="modal-dialog">
					<div class="modal-content">
						<div class="modal-header">Rename Group</div>
						<div class="modal-body">
							<s:form id="renameFormId" action="renameGroup">
								<s:hidden name="groupId"/>
								 <div class="form-group">       
                                    <label for="textAreaId">New Group Name:</label>
									<s:textfield name="newGroupName" id="textRenameGroupId" cssClass="form-control"/>
								</div>
							</s:form>
						</div>
						<div class="modal-footer">
							<button type="button" data-dismiss="modal" class="btn btn-primary"
								id="renameModIdBtn">Rename</button>
							<button type="button" data-dismiss="modal" class="btn btn-default"
								id="renameCancel">Cancel</button>
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

	<!-- DataTables JavaScript -->
	<script src="js/jquery.dataTables.min.js"></script>
	<script src="js/dataTables.bootstrap.min.js"></script>
	<script src="js/dataTables.responsive.js"></script>

	<script>  
    var deleteRunning = false;
	  $(document).ready(function(){
		  
		  var table = $('#itemsTable').DataTable({
				responsive: true
			});	  
		  
		 
		  //delete row
		  $('#itemsTable tbody').on( 'click', '.deleteRow', function () {

			  if (!deleteRunning){
				  deleteRunning = true;				  
			  }else{
				  return;
			  }
			  			  
			  var id = $(this).attr('itemId');			  
		  
			  $(document.body).css({'cursor' : 'wait'});
			  
			  $("#errorsId").hide();
			  
			  var row = table.row( $(this).parents('tr') );
			  
			  $.getJSON("deleteItem", {itemId : id}, function(data){	 
				  	  			  
				  if (data.deleteSuccess){
					row.remove().draw( false );					
				  }else{
					  $("#errorsId").show(200);
					  $("#errorsId").text(data.error);
				  }				  
				  $(document.body).css({'cursor' : 'default'});				  
				  
			  }).always(function() {
				  deleteRunning = false; // finished the request
			  });
			} );
		  
		  
		  //delete group
			$('#deleteLnkId').on('click', function(e) {				
				var link = $(this);
				  e.preventDefault();
				  $('#confirm').modal({
					  backdrop: 'static',
					  keyboard: false
					})
					.one('click', '#delete', function(e) {
					  $('#confirm').unbind();
					  window.location.href = link.attr("href");
					}).one('click', '#delCancel', function(e) {
						$('#confirm').modal('hide');
						//  $('#confirm').unbind();
					});
			});
		  
			 //rename group
			$('#renameIdLink').on('click', function(e) {				
				var link = $(this);
				  e.preventDefault();
				  $('#renameGroupModal').modal({
					  backdrop: 'static',
					  keyboard: false
					})
					.one('click', '#renameModIdBtn', function(e) {
					  $('#renameGroupModal').unbind();
					  
					  //submit form with field
					  $("#renameFormId").submit();
					  
					}).one('click', '#renameCancel', function(e) {
						$('#renameGroupModal').modal('hide');
						//  $('#confirm').unbind();
					});
			});	
		  
		});	  
	  
    </script>

	<jsp:include page="/jsp/footer.jsp" />