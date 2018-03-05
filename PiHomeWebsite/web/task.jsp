<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<%-- start header  --%>
	<jsp:include page="/jsp/header.jsp" />
	
	<link href="css/overlay.css" rel="stylesheet"/>
	
	<link href="css/jquery.datetimepicker.min.css" rel="stylesheet" type="text/css">
	
	</head>
<%-- --%>

<body>

    <div id="wrapper">
        <!-- Navigation -->
        <nav class="navbar navbar-default navbar-static-top" role="navigation" style="margin-bottom: 0">        
        	<!--  	TOP NAV START 	-->
            <jsp:include page="/jsp/navMenu.jsp" />

			<!--  TOP NAV END  -->
			
			<s:if test="#session.user != null">
				<!--  side menu -->			
				<jsp:include page="/jsp/sideMenu.jsp" />
				<!--  side menu ENDDDD  -->
			</s:if>           
        </nav>
	
	
	<!--main content start  -->

       	<div id="page-wrapper">
       	 <s:form theme="simple" action="tasks" cssClass="submit-once">     
	       	<div class="row"> 
	       		<div class="col-xs-9 col-md-6 main" style="margin-top:15px">
	       			<s:if test="fieldErrors.size > 0" >
					    <div class="alert alert-danger"> 
					        <p>
						        <s:fielderror escape="false"/>
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
					
					<s:if test="actionMessages.size > 0" >
					    <div class="alert alert-success"> 
					    	<p>
					        	<s:actionmessage escape="false"/>
					        </p>
					    </div>
					</s:if>
	       		
	       			       						         	
					<label>Add new Task</label>
					<div class="input-group ">				  
						<s:textfield name="newTask" cssClass="form-control" placeholder="Add new Task" />
							<span class="input-group-btn">
				   				 <button class="btn btn-default" type="submit" name="addNewTaskBtn" ><i class="fa fa-plus-circle"></i></button>
				   			</span>	
					</div>							
				 </div>
	       	</div>
	
			<div class="row">
			  <div class="col-xs-12 col-sm-12 col-md-8">
			  	
			  	<h3 class="page-header" >
			  		<s:if test="categoryName != null && !categoryName.equals('inbox') && !categoryName.equals('deleted')">
			  			<span class="glyphicon glyphicon-remove" title="Remove category" style="cursor:pointer;color:red" id="deleteCatId"> </span>
			  		</s:if>			  		 
			  	    Current tasks:
			  	</h3>
				<s:if test="tasks != null && !tasks.isEmpty()">
					<div class="list-group" id="taskGroupGroupId">
						<s:iterator value="tasks"  status="indx">
							 <a  class="list-group-item list-group-item-action " id="groupItemId<s:property value="id"/>" style="margin: 8px 0" >
							 	<s:if test="categoryName != null && !categoryName.isEmpty()" >
							 		<input type="checkbox" name="test1"  class="form-check-input taskItemChkBox" taskIdInfo="<s:property value="id"/>"/>
							 	</s:if>
							 	<span style="cursor:pointer" class="tasksItem" taskIdInfo="<s:property value="id"/>">
							 		<s:property value="task"/>
							 		<s:if test="toBeCompletedBy != null">
							 			<i class="fa fa-clock-o pull-right" Title="Due <s:date name="toBeCompletedBy" format="yyyy-MM-dd HH:mm.ss" />"></i>
							 		</s:if>
							 		
							 	</span>
							 </a>
						</s:iterator>
					</div>
				</s:if>
				<s:else>
					<div class="list-group" id="noTaskId">
						-No Tasks										
					</div>
				</s:else>
			</div>	
			
			<div class="row" style="<s:if test="taskCompleted != null && !taskCompleted.isEmpty()"> </s:if><s:else>display:none</s:else> "  id="completedId">
			  <div class="col-xs-12 col-sm-12 col-md-8">
	       		<h3 class="page-header" > <span id="nbrTaskCompleteId"><s:property value="taskCompleted.size()"/></span> task(s) Completed </h3>
	       			<div class="list-group" id="completedGroupId">
						<s:iterator value="taskCompleted"  status="indx">
							 <a  class="list-group-item list-group-item-action list-group-item-success" id="groupItemId<s:property value="id"/>" style="margin: 8px 0" >
							 	<s:if test="categoryName != null && !categoryName.isEmpty()" >
							 		<input type="checkbox" name="test1"  class="form-check-input taskItemChkBox" taskIdInfo="<s:property value="id"/>"/>
							 	</s:if>
							 	    <span >
							 			<s><s:property value="task"/></s>
							 		</span>
							 </a>
						</s:iterator>
					</div>
	       		
	       	   </div>
	       	</div>
			
			
			
			<%-- OVERlay right --%>
			<div id="mySidenav" class="sidenav">
			  <a href="javascript:void(0)" class="closebtn" onclick="closeNav()">&times;</a>
			  
			  	<div id="taskLoadId" style="display:none"> 	<i class="fa fa-spinner fa-pulse fa-3x fa-fw"></i> </div>
			  	
				<div class="panel panel-primary" id="taskInformationId" style="display:none">
                        <div class="panel-heading">
                            <i class="fa fa-tasks fa-fw"></i> Task Information
                        </div>
                        <!-- /.panel-heading -->
                        <div class="panel-body">
                            <div class="list-group">
                            	<s:hidden name="taskId" id="formTaskId"/>							
								<input type="text" class="form-control list-group-item" name="formTaskName" id="formTaskNameId"/>								
								<hr/>
								<span class="text-primary" style="cursor:pointer">									
									<s:hidden name="formTaskDueDate" id="formTaskDueDateId"/>
									<div id="formTaskDueDateViewId"> 										
									
									</div>
								</span>
								<hr/>								
								<div class="form-group input-group">
									<span class="input-group-addon"><i class="fa fa-comment" title="Comments"></i> </span>
									<textarea class="form-control list-group-item" name="formTaskComment" id="formTaskCommentId"> </textarea>     
                                </div>
                                <hr/>
								<div class="form-group input-group">
									<span class="input-group-addon"><i class="fa fa-list-ul" title="Category"></i></span>
									 <s:select name="formCategory"				
										 cssClass="form-control"
										 cssStyle="width: 150px"
										 list="#session.taskCategoryList != null ? #session.taskCategoryList : #{'Student':'Student','Teacher':'Teacher'}"
										 listKey="categoryName"
									 	 listValue="categoryName"
										 id="formTaskCategoryId"
									 />                                   
                                </div>								
								<hr/>							
								<div class="panel panel-default" style="margin-top:15px">
									<div class="panel-heading">
										<i class="fa fa-bell fa-fw"></i> Sub Tasks
									</div>
									<!-- /.panel-heading -->
									<div class="panel-body">	
										<div id="subTaskBodyId">				
			
										</div>
										 									
										<button type="button" id="addSubTskId" class="btn btn-primary btn-xs pull-right" style="margin-top:7px">Add</button>
									</div>
								</div>  
                            </div>
                            <!-- /.list-group -->
                            <button class="btn btn-success btn-block" type="submit" name="updateTaskBtn" >Update</button>
                        </div>
                        <!-- /.panel-body -->
                    </div>
				</div>	
				
				<!-- task Delete Category modale -->
			 	<div class="container">  	
					<!-- Modal -->
					<div class="modal fade" id="deleteCategoryModale" role="dialog">
						<div class="modal-dialog">			    
						<!-- Modal content-->
							<div class="modal-content">
								<div class="modal-header">
							    	<button type="button" class="close" data-dismiss="modal">&times;</button>
							        	<h4><span class="glyphicon glyphicon-remove" title="Remove category" > </span>
							        		 Delete Category</h4>
							    </div>
							    <div class="modal-body" style="padding:40px 50px;">
							    	
							     	<div class="form-group">
							           	<p> Are you sure you want to delete this category?</p>
							           	<p><small><em> All unfinished tasks will be returned to the inbox.</em></small></p>
							            
							      	
							        </div>
							      	<div class="form-group">
							      	   	<label><s:checkbox name="deleteTaskChkbox"/>Select if you also want to delete tasks under this category	</label>
							      	 </div>       
							        <button type="submit" class="btn btn-warning pull-left" name="deleteCategoryBtn"><span class="glyphicon glyphicon-remove"></span>
							               Yes
							        </button> 							               
							     
							    </div>
							    <div class="modal-footer">
							    	<button type="submit" class="btn btn-danger btn-default pull-left" data-dismiss="modal"><span class="glyphicon glyphicon-remove"></span> Cancel</button>
							    </div>
							</div>				      
						</div>
					</div>				      	
				</div> 							
			</div>	       
			<s:hidden name="categoryName"/>
  		</s:form>
        </div>
       <!--  main content ENNDDDD  -->

    </div>
    <!-- /#wrapper -->

    <!-- jQuery -->
    <jsp:include page="/jsp/jquery.jsp" /> 
    
    <script src="js/jquery.datetimepicker.full.min.js"></script>
    
    <script>
    
    var bigScrWidth = 450;//size of the overlay for max
    
    $( document ).ready(function() {   	
    	
	    jQuery('#formTaskDueDateViewId').datetimepicker({
			  format:'Y-m-d H:i',
			  onChangeDateTime:function(dp,$input){
				$("#formTaskDueDateId").val($input.val());
				$("#formTaskDueDateViewId").html("<i class=\"fa fa-clock-o\" Title=\"Due Date\"></i> Due " + $input.val());
			  }
			});
	    
	  });
    
    //load task into the overlay    
    $(".tasksItem").click(function(){
    	
    	var taskId =  $(this).attr("taskIdInfo");
    	
    	$("#taskLoadId").show();
    	
    	setOverlaySize();
    	
    	$("#mySidenav").addClass("showing");
    	
    	loadTaskById(taskId);   
    	
    	
    	    	
    });      
  
    
    //clicked on the checkbox
    var nbrCplTsk = ( $("#nbrTaskCompleteId").text().length > 0 ? parseInt($("#nbrTaskCompleteId").text()) : 0);
    $(".taskItemChkBox").click(function(){

    	var nbr = $(this).attr("taskIdInfo");
    	//groupItemId //is the task item id.
    	
    	//add to active task list    	
    	if ($("#groupItemId"+nbr).hasClass("list-group-item-success")){	//item is no more completed
    		//verify if we have any non completed task before adding.
    		if ($("#noTaskId").text().length > 0){
    			if ($("#noTaskId").text().trim() === '-No Tasks'){//remove the text
    				$("#noTaskId").text(" ");
	   			}    			
    			$("#groupItemId"+nbr).appendTo("#noTaskId");
        	}else{
        		$("#groupItemId"+nbr).appendTo("#taskGroupGroupId");
        	}
    		
    		$("#groupItemId"+nbr).removeClass("list-group-item-success");
    		
        	$(this).next().html($(this).next().text()); //update span with text
        	updateTask(nbr, false);
        	nbrCplTsk --;
        	$("#nbrTaskCompleteId").text(nbrCplTsk); //update the number of task completed number
        	
    	}else{
    		//item is completed. add to completed items
    		if (!$("#completedId").is(":visible")){ //check if the complete section is visible
    			$("#completedId").show(200);
    		}
    		$("#groupItemId"+nbr).appendTo("#completedGroupId"); //add task to the completed group    		
    		$("#groupItemId"+nbr).addClass("list-group-item-success"); // add green sucess color
        	$(this).next().html("<s>" + $(this).next().text() +  "</s>"); //add strikethrough
        	updateTask(nbr, true);
        	nbrCplTsk ++;
        	$("#nbrTaskCompleteId").text(nbrCplTsk);
    	}    	    	
    });
    

  
    //resize overlay based on the window sizew
	var minSet = false;
	$(window).resize(function() {
		var viewportWidth = $(window).width();

		if ($("#mySidenav").hasClass("showing")){
		
			if (viewportWidth < 450 && !minSet){
				setMinViewPort();
				minSet = true;
			}else if(minSet && viewportWidth > 449){
				setMaxViewPort();
				minSet = false;
			};
		}
	});
	
	//load task by ID in the overlay
	function loadTaskById(id){
		
		$.getJSON("loadAjaxTaskById",{taskId : id}, function(data){
			//do something here
			if (data != null && data.id > 0){
				$("#taskLoadId").hide();
				$("#taskInformationId").show();
				
				$("#formTaskId").val(data.id);
				$("#formTaskNameId").val(data.task);
				$("#formTaskCommentId").val(data.taskComment);
				$("#formTaskCategoryId").val(data.category);
				$("#formTaskDueDateId").val(data.dueDateString);
				
				if (data.dueDateString == null){
					$("#formTaskDueDateViewId").html("<i class=\"fa fa-clock-o\" Title=\"Due Date\"></i> No due date");
				}else{
					$('#formTaskDueDateId').datetimepicker('destroy');
					$('#formTaskDueDateId').datetimepicker();
					var d = $('#formTaskDueDateId').datetimepicker('getValue');
					$("#formTaskDueDateViewId").html("<i class=\"fa fa-clock-o\" Title=\"Due Date\"></i> Due " +formatDate(d));
				}	
						
				//load the sub tasks				
				if (data.subTasks.length > 0){						
					
					//1st empty the sub task if any					
					$("#subTaskBodyId").empty();
						for(var i = 0 ; i < data.subTasks.length ; i++){
							
							var color = "";
							var readOnly = "";
							var chk = "";
							if (data.subTasks[i].complete != null && data.subTasks[i].complete){
								color = "background-color:lightgreen";
								readOnly = "readonly=''";
								chk = "checked";
							}							
							
							var subTask = "<div class='input-group' id='subTskGroupId"+ i +"'>  <input type='hidden' name='subTskChk["+i+"]' value='"+ data.subTasks[i].id +"'  id='subTskHiddenId"+i+"'/> " +	
							  "<span class='input-group-btn' style='border: 1px solid lightgray;"+color+"' id='subTskchkboxId" + i+"'>" +
							 "<input type='checkbox' class='subTskCmplCheck' style='margin-left: 8px;margin-right: 8px;' listIdx='"+i+"' name='test2' "+chk+" /> "+
							  "</span>"+
							 "<input type='text' name='subTaskName["+i+"]' class='form-control list-group-item' value='"+data.subTasks[i].subTask+"' id='subTskInpId" +i + "' "+readOnly+"/>" +											   	
							  "</div>" ;
							  
							$("#subTaskBodyId").append(subTask);
						}
				}else{
					var subTask2 = "<div class='input-group' id='subTskGroupId"+ i +"'>  <input type='hidden' name='subTskChk[0]' value='' /> " +	
					  "<span class='input-group-btn' style='border: 1px solid lightgray;' listIdx='0'>" +
					 "<input type='checkbox' style='margin-left: 8px;margin-right: 8px;' disabled name='test2'/> "+
					  "</span>"+
					 "<input type='text' name='subTaskName[0]' class='form-control list-group-item' value=''/>" +											   	
					  "</div>" ;
		
					$("#subTaskBodyId").append(subTask2);
				}				
				
			}else{
				$("#taskLoadId").hide();
				$("#taskInformationId").hide();
			}
			
		});
	}	
	
	function formatDate(d){
		
		var dateFormatted = "";
		if (d != null){
			
			//year
			dateFormatted += d.getFullYear() + "-";
			
			//month
			var month = (d.getMonth() + 1);			
			if (month < 10){
				month = "0" + month;
			}			

			//day
			var day = d.getDate();
			if (day < 10){
				day = "0" + day;
			}
			
			//hour
			var hour = d.getHours();
			if (hour < 10){
				hour = "0" + hour;
			}
	
			//minutes
			var min = d.getMinutes();
			if (min < 10){
				min = "0" + min;
			}		
			
			dateFormatted += month + "-" + day + " " + hour + ":" + min;			
		}
		
		return dateFormatted;		
	}
	
	function updateTask(id, complete){
		
		$.getJSON("toggleUpdateCompleted",{taskId : id, completed: complete,categoryName: '<s:property value="categoryName"/>' }, function(data){

			var cat = '<s:property value="categoryName"/>';
			
			var taskNbr    = parseInt($("#taskBadgeId" + cat).text());
			var allTaskNbr = parseInt($("#allTasksId").text());
			
			if (complete){
				taskNbr -= 1;
				allTaskNbr -= 1;
			}else{
				taskNbr += 1;
				allTaskNbr += 1;
			}
							
			$("#taskBadgeId" + cat).text(taskNbr); //update side menu badge
			$("#allTasksId").text(allTaskNbr); //update all task side menu badge
			
		});
	}
	
	  //because class is added dynamically, this needs to tbe done.
    $(document).on('click', ".subTskCmplCheck", function() {
		var idx = $(this).attr("listIdx");    
		var subTskid = $("#subTskHiddenId" + idx).val();
		
		var complete = false;
		
		if ($("#subTskInpId"+idx).prop("readonly")){
			complete = false;
			
		}else{
			complete = true;
		}	
		$("body").css("cursor", "progress");
		
		$.getJSON("toggleUpdComplForSubTask",{taskId : subTskid, completed: complete }, function(data){			
			
			
			if ($("#subTskInpId"+idx).prop("readonly")){		
				$("#subTskInpId"+idx).prop("readonly", false);
				$("#subTskchkboxId" + idx).css("background-color", "#fcfcfc");				
			}else{			
				$("#subTskInpId"+idx).prop("readonly", true);
				$("#subTskchkboxId" + idx).css("background-color", "lightgreen");		
			}		
			$("body").css("cursor", "default");
		});				
    });   
	
	function updateSubTask(id, complete, groupId){
		
		
	}
		
	$("#deleteCatId").click(function(){
		$("#deleteCategoryModale").modal();
	});
	
	$("#addSubTskId").click(function(){
		
		var occ = $('input[name*="subTaskName"]').length;
		
		if (occ === 'undefined'){
			occ = 0;
		}
		
		//load the sub tasks
		var subTask = "<div class='input-group' id='subTskGroupId"+ occ +"'>  <input type='hidden' name='subTskChk["+occ+"]' value='' /> " +	
					  "<span class='input-group-btn' style='border: 1px solid lightgray;'>" +
					 "<input type='checkbox' style='margin-left: 8px;margin-right: 8px;' disabled/> "+
					  "</span>"+
					 "<input type='text' name='subTaskName["+occ+"]' class='form-control list-group-item' value=''/>" +											   	
					  "</div>" ;
		 $("#subTaskBodyId").append(subTask);
	});
		
	//viewport functions
	    // close overlay
	function closeNav() {
		$("#mySidenav").css("margin-right","0px").css("width","0px");
		$("#mySidenav").removeClass("showing")
		$("#taskLoadId").hide();
		$("#taskInformationId").hide();
	}
	

	function setOverlaySize(visible){
		
		var width = $(window).width();

		if (width < bigScrWidth){
			setMinViewPort();
			minSet = true;
		}else if(width > (bigScrWidth - 1)){
			setMaxViewPort();
			minSet = false;
		}
	}
	
	function setMinViewPort(){
		$("#mySidenav").css("margin-right","20px").css("width","250px");
	}
	
	function setMaxViewPort(){
		$("#mySidenav").css("margin-right","20px").css("width",bigScrWidth+"px");
	}
	
	</script>
	
	<jsp:include page="/jsp/footer.jsp" />
