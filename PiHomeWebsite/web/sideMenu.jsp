<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<s:url id="adduser" action="addEditUser" /> 
  <s:url id="changepass" action="changepassword" />
  <s:url id="addFolderPageUrl" action="displayAddFolderPage" />
  <s:url id="manageSharesUrl" action="manageFolders" >
  	<s:param name="shareFolderBtn" >SharesPage</s:param>
  </s:url>
  <s:url id="listAllUsersUrl" action="userList" />
  <s:url id="tasksUrl" action="loadUserTask" />



<div class="navbar-default sidebar" role="navigation">
	<div class="sidebar-nav navbar-collapse">
    	<ul class="nav" id="side-menu">
        	<li style="padding:5px 5px 5px 5px;">
               <s:if test="#session.user != null" >
              	 	Welcome <s:property value="#session.user.firstName"/>
               </s:if>
               <s:else>
               		Welcome Guest
               </s:else>
            </li>
            <s:if test="#session.user != null" >
	            <li>
	                <a href="#"><i class="fa fa-folder fa-fw"></i> Folders<span class="fa arrow"></span></a>
		            <ul class="nav nav-second-level" id="folderDropId">
			            <li>
			            	<s:a href="%{addFolderPageUrl}" title="Manage Folders" >
			                	<i class="fa fa-sitemap"></i>
			                	Manage Folders
			                </s:a> 
			            </li>
			            <li>
			                <s:a href="%{manageSharesUrl}" title="Manage Shares" >
			                	<i class="fa fa-cogs"></i>
			                	Manage shares
			                </s:a>
			            </li>
			            <s:iterator value="#session.sideMenuFolderList"> 
			            
			     
			             	<s:url id="loadFolderUrl" action="viewFolder" >
			             		<s:param name="folderId" ><s:property value="id"/></s:param>
			             	</s:url>
			             	<li>		                			
			             		<s:a href="%{loadFolderUrl}">	
			             			<i class="fa fa-folder"> </i>
			             			<s:property value="folderName"/>			             				
			             				<span class="badge pull-right" id="flNbr<s:property value="id"/>">
			             					<s:property value="fileNumber"/>
			             				</span>
			             				&nbsp;
			             				<s:if test="sharedFolder(#session.user.id)">
			             					<i class="fa fa-share-alt-square fa-lg pull-right" style="vertical-align: -25%" title="shared folder"></i>
			             				</s:if>
			             		</s:a>
			             	 </li>
			             </s:iterator>		            
		        	</ul>
	                 <!-- /.nav-second-level -->
	            </li>
	            <!-- tasks -->	            
	            <li>
	            	<a href="#"><i class="fa fa-tasks fa-fw"></i> Tasks<span class="fa arrow"></span></a>
	            		 <ul class="nav nav-second-level"  id="taskDropId">
	            		 
	            		 	<s:set var="cntValue" value="%{0}"/>
	            		 	<s:iterator value="#session.sideMenuTasks"> 
	            		 		  <s:url id="tasksInboxUrl" action="loadUserTask" >
								  	<s:param name="categoryName" ><s:property value="key"/></s:param>
								  </s:url>
	            		 	
	            		 	
	            		 		<s:set var="cntValue" value="%{#attr.cntValue + value}"/>	
	            		 		<li>
	            		 			<s:a href="%{tasksInboxUrl}"><i class="fa fa-inbox"></i> 
		            					<s:property value="key"/>
		            					<span class="badge pull-right" id="taskBadgeId<s:property value="key"/>">
				             					<s:property value="value"/>
				             			</span>
		            				</s:a>
	            		 		
	            		 		</li>

	            			</s:iterator>
	            			<li>
		            			<s:a href="%{tasksUrl}"><i class="fa fa-navicon"></i> 
		            				All Tasks
		            				<span class="badge pull-right" id="allTasksId">
				            					<s:property value="%{#attr.cntValue}"/>
				            		</span>
		            			</s:a>
		            		</li>            	
		            		 <li>
		            		 		<a data-toggle="modal" href="#myModal"><i class="fa fa-plus"></i> <small><em>Add a category</em></small></a>
		            		</li> 
	            		</ul>
	            </li> 
	            <!--  inventory -->
	            <li>
                            <a href="#"><i class="fa fa-book fa-fw"></i> Inventory<span class="fa arrow"></span></a>
                            <ul class="nav nav-second-level">
                                <li>
                                	<s:url id="loadAllinvUrl" action="loadAllInvGroup" >						
			 	   					</s:url> 
                                    <s:a href="%{#loadAllinvUrl}" >  <i class="fa fa-list fa-fw" ></i>List All Inventory</s:a>
                                </li>
                                
                                
                                <s:iterator value="#session.sideMenuInventory">                                
                                	<s:url id="loadGroup" action="loadUserGroup" >
			 	   						<s:param name="groupId"><s:property value="id"/></s:param>
			 	   					</s:url>    
                                	<li>
                                		<s:a href="%{#loadGroup}" >
                                			<i class="fa fa-archive fa-fw" ></i>
							            	<s:property value="groupName" />
								    	</s:a>
                                	</li>
                                </s:iterator>                  
                                
                                <li  id="addGroupLiId">
                                    <a href="index.html"  data-toggle="modal" data-target="#newInvModal">
                                     	<i class="fa fa-plus-circle fa-fw" style="color:green;"></i>
                                     	Add group
                                     </a>
                                </li>
               
                            </ul>
                        </li>
                        <li>
                        	<s:url id="loadProjectPage" action="loadAllProjects" >
			 	   					</s:url> 
                        	<s:a href="%{#loadProjectPage}" >
                                			<i class="fa fa-archive fa-fw" ></i>
							            	Projects
								    	</s:a>
                        </li>
	            
	            
            </s:if>
            <s:if test="#session.user.access == @net.project.enums.AccessEnum@ADMIN" >   
            	<li>
	                <a href="#"><i class="fa fa-cog fa-fw"></i> Admin Menu<span class="fa arrow"></span></a>
		            <ul class="nav nav-second-level">
			            <li>
			            	<s:a href="%{adduser}" title="Add new User" onclick="document.body.style.cursor='wait';">
		                		<i class="fa fa-user-plus"></i>  
		            			Add user   
		           			</s:a>  
			            </li>
			            <li>
			                <s:a href="%{listAllUsersUrl}" title="List all users" onclick="document.body.style.cursor='wait';">
		           				<i class="fa fa-users"></i>  
	            				List Users
	            			</s:a>	
			            </li>
			                        
		        	</ul>
	                 <!-- /.nav-second-level -->
	            </li>
	         </s:if>
                                   
		</ul>
	</div>
   <!-- /.sidebar-collapse -->
</div>
 <!-- /.navbar-static-side -->
 
 
 
 
 
 
 
 
 