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

					<div class="row">
						<div class="col-lg-12">
							<h1 class="page-header">Projects List</h1>
						</div>
						<!-- /.col-lg-12 -->
					</div>
					<div class="row">
						<div class="col-lg-12">
							<s:if test="actionErrors.size > 0">
								<div class="alert alert-danger">
									<p>
										<s:actionerror escape="false" />
									</p>
								</div>
							</s:if>
							<s:if test="hasActionMessages()">
								<div class="alert alert-success"> 
									<p>
										<s:actionmessage escape="false" />
									</p>
								</div>
							</s:if>

							<s:url id="newProjectUrl" action="addProject">
							</s:url>
							<s:a href="%{#newProjectUrl}" class="btn btn-success btn-xs "
								style="margin-bottom:10px">
								<i class="fa fa-pencil "></i>Add new Project </s:a>
							<br /> <br />
						</div>
						<!-- /.col-lg-12 -->
					</div>
					<div class="row">
						<div class="col-lg-12">
							<!-- /.row -->
							<table width="100%"
								class="table table-striped table-bordered table-hover"
								id="dataTables-example">
								<thead>
									<tr>
										<th></th>
										<th>Title</th>
										<th>Last Updated</th>
										<th>Created</th>
										<th></th>
									</tr>
								</thead>
								<tbody>
									<s:iterator value="projectList" status="idx">
										<tr projectId="1">
											<td><s:property value="#idx.count" />.</td>
											<td><s:url id="viewProjectUrl" action="viewProject">
													<s:param name="viewProjectId">
														<s:property value="id" />
													</s:param>
												</s:url> <s:a href="%{#viewProjectUrl}"
													cssStyle="display:block;width:100%;">
													<s:property value="title" />
												</s:a></td>
											<td class="center"><s:date name="updDt"
													format="yyyy-MM-dd HH:mm" /></td>
											<td class="center"><s:date name="crtDt"
													format="yyyy-MM-dd HH:mm" /></td>
											<td class="center"><s:url id="editProjectUrl"
													action="editProject">
													<s:param name="viewProjectId">
														<s:property value="id" />
													</s:param>
												</s:url> <s:a href="%{#editProjectUrl}">
													<i class="fa fa-pencil-square-o" title="Edit"></i>
												</s:a> &nbsp; <s:url id="deleteUrl" action="deleteProject">
													<s:param name="viewProjectId">
														<s:property value="id" />
													</s:param>
												</s:url> <s:a href="%{#deleteUrl}"
													onclick="return confirm('Are you sure?');">
													<i class="fa fa-times fa-fw" style="color: red"
														title="Delete"></i>
												</s:a></td>
										</tr>
									</s:iterator>

								</tbody>
							</table>
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
		$(document).ready(function() {
			$('#dataTables-example').DataTable({
				responsive: true
			});
		});
		
			
    </script>
    
    <jsp:include page="/jsp/footer.jsp" /> 



