
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
                <div class="col-lg-12">
                    <p>
						<h2 class="page-header">
						<s:url id="editProjectUrl" action="editProject" >
	                                        		<s:param name="viewProjectId"><s:property value="project.id"/></s:param>
												</s:url>
												<s:a href="%{#editProjectUrl}"   > <i class="fa fa-pencil-square-o" title="Edit"></i> </s:a> 
						  <s:property value="project.title"/>
						  </h2>			
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
					</p>
					
					 <p>
						
						 <s:property value="project.desc" escapeHtml="false"/>
				
					</p>
					 <p>
						<h3 class="page-header">Links:</h3>						  
							<ol>
								<s:if test="project.refs != null && !project.refs.isEmpty()">
									<s:iterator value="project.refs">
										<s:if test="type.name().equalsIgnoreCase('html')"> 	
											 <li><a href="<s:property value="title"/>" target="_blank"><s:property value="title"/> </a></li> 									 			
										 </s:if>
										 <s:else>
										 	<li><s:property value="title"/></li>	
										 </s:else>					
									</s:iterator>
								</s:if>
							<s:else>
								<li>No Links.</li>
							</s:else>
							</ol>				
					</p>
					 <p>
						<h3 class="page-header">Files:</h3>						  
							<ol>
							<s:if test="project.files != null && !project.files.isEmpty()">
								<s:iterator value="project.files">
									<li>
										<s:url id="downloadUrl" action="getFile">
											<s:param name="fileDownloadName">
												<s:property value="fileDiskName" />
									 		</s:param>
								 		</s:url>
				        				<s:a href="%{#downloadUrl}"   ><i class="fa fa-file fa-fw" ></i><s:property value="fileName"/> </s:a>
	        						</li>																	
								</s:iterator>
							</s:if>
							<s:else>
								<li>No files.</li>
							</s:else>							
							</ol>	
								
					</p>

                </div>
                <!-- /.col-lg-12 -->
            </div>
							
			
			</div>
		</div>
		<!--  main content ENNDDDD  -->

    </div>
    <!-- /#wrapper -->

    <!-- jQuery -->
    <jsp:include page="/jsp/jquery.jsp" /> 
    

    
    <jsp:include page="/jsp/footer.jsp" /> 




