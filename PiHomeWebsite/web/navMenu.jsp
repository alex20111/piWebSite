<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<s:url id="signInUrl" action="displayLogin" />
<s:url id="logOutUrl" action="logOut" />
<s:url id="MainPageUrl" action="mainPageAction" />
<s:url id="weatherUrl" action="displayCurrentWeather" />  
<s:url id="changepass" action="changepassword" />
<s:url id="emailConfigUrl" action="loadEmailConfig" />

<div class="navbar-header">
	<s:if test="#session.user != null">
		<button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
			<span class="sr-only">Toggle navigation</span>
			<span class="icon-bar"></span>
			<span class="icon-bar"></span>
			<span class="icon-bar"></span>
		</button>
	</s:if>
	<s:a href="%{MainPageUrl}" title="Sign In" cssClass="navbar-brand">
		 <i class="fa fa-home"></i>	Pi Home	        		
	</s:a>
	
	<s:a href="%{weatherUrl}" title="Sign In" cssClass="navbar-brand">
		 <i class="fa fa-cloud"></i>	Weather       		
	</s:a>
 </div>
            <!-- /.navbar-header -->
	<ul class="nav navbar-top-links navbar-right">            	
				
		<s:if test="#session.user != null" >
        	<!-- /.dropdown -->
			<li class="dropdown">
                    <a class="dropdown-toggle" data-toggle="dropdown" href="#">
                        <i class="fa fa-cog fa-fw"></i> <i class="fa fa-caret-down"></i>
                    </a>
                    <ul class="dropdown-menu dropdown-tasks">
                        <li>
                        	<s:a href="%{emailConfigUrl}" title="Email Configuration" >                          
                            	 <i class="fa fa-envelope"></i>
                                Email Configuration
                            </s:a>
                        </li>
                        <li class="divider"></li>
                        <li>
                            <a href="#">
                                <div>
                                    <p>
                                        <strong>Task 2</strong>
                                        <span class="pull-right text-muted">20% Complete</span>
                                    </p>
                                    <div class="progress progress-striped active">
                                        <div class="progress-bar progress-bar-info" role="progressbar" aria-valuenow="20" aria-valuemin="0" aria-valuemax="100" style="width: 20%">
                                            <span class="sr-only">20% Complete</span>
                                        </div>
                                    </div>
                                </div>
                            </a>
                        </li>
                      
                       
                    </ul>
                    <!-- /.dropdown-tasks -->
                </li>
              
                <!-- /.dropdown -->
                <li class="dropdown">
                    <a class="dropdown-toggle" data-toggle="dropdown" href="#">
                        <i class="fa fa-user fa-fw"></i> <i class="fa fa-caret-down"></i>
                    </a>
                    <ul class="dropdown-menu dropdown-user">
                        <li>
                        	<s:url id="myInfoUrl" action="addEditUser" >
		 	   						<s:param name="userId"><s:property value="#session.user.id"/></s:param>
		 	   				</s:url> 
                        	<s:a href="%{#myInfoUrl}" >
                        		<i class="fa fa-user-circle"></i>
					            	My Information
						   	</s:a>
                        	
                        </li>
                        <li>
                        	<s:a href="%{changepass}" title="Change Password" >
		            			<i class="fa fa-lock"></i> 
								Change Password
							</s:a>
                        </li>
                        <li class="divider"></li>
                        <li>                       
                        	<s:a href="%{logOutUrl}" title="Log Out" onclick="document.body.style.cursor='wait';">
		        		 		<i class="fa fa-sign-out"></i>
		        		 		Log out
		        			</s:a>		                	
                        </li>
                    </ul>
                    <!-- /.dropdown-user -->
                </li>
                </s:if>
                <s:else>
                <li>
					<s:a href="%{signInUrl}" title="Sign In" onclick="document.body.style.cursor='wait';">
		        		 <i class="fa fa-sign-in"></i>
		        		 Login 
		        	</s:a> 
                </li>
                </s:else>
            </ul>
            <!-- /.navbar-top-links -->