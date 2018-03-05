 <%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
 
 	<!-- Add new categrory task modale -->
 	<div class="container">  	
		<!-- Modal -->
		<div class="modal fade" id="myModal" role="dialog">
			<div class="modal-dialog">			    
			<!-- Modal content-->
				<div class="modal-content">
					<div class="modal-header">
				    	<button type="button" class="close" data-dismiss="modal">&times;</button>
				        	<h4><span class="fa fa-plus"></span> Add new category</h4>
				    </div>
				    <div class="modal-body" style="padding:40px 50px;">
				    	<form role="form" action="tasks" class="submit-once">
				        	<div class="form-group">
				            	<label for="usrname"><span class="glyphicon glyphicon-user"></span> Category Name</label>
				              	<input type="text" class="form-control" id="catgNameId" name="newCatgName" placeholder="Enter Category name">
				            </div>				           
				              <button type="submit" class="btn btn-success pull-left" name="addNewCategoryBtn"
				               id="addNewCatBtnId" onclick="return validateNewCat();"><span class="glyphicon glyphicon-off"></span>
				                Add
				                </button>
				        </form>
				    </div>
				    <div class="modal-footer">
				    	<button type="submit" class="btn btn-danger btn-default pull-left" data-dismiss="modal"><span class="glyphicon glyphicon-remove"></span> Cancel</button>
				    </div>
				</div>				      
			</div>
		</div>				      	
	</div> 
	
	  <!-- Add new inventory Modal -->
	  <div class="modal fade" id="newInvModal" role="dialog">
		    <div class="modal-dialog">
		    
		      <!-- Modal content-->
		      <div class="modal-content">
		        <div class="modal-header">
		          <button type="button" class="close" data-dismiss="modal">&times;</button>
		          <h4 class="modal-title">Add new group</h4>
		        </div>
		        <div class="modal-body">
		          <p>Group name.</p>
		          <input type="text" id="newGroupTextId" class="form-control"/>
		        </div>
		        <div class="modal-footer">
		          <button type="button" class="btn btn-default" data-dismiss="modal" >Close</button>
		           <button type="button" class="btn btn-primary"  id="addGroupIdBtn" >Add</button>
		        </div>
		      </div>
		      
		    </div>
		  </div>
	
 	
 	
    
   <!-- Bootstrap Core JavaScript -->
    <script src="js/bootstrap.min.js"></script>

    <!-- Metis Menu Plugin JavaScript -->
    <script src="js/metisMenu.min.js"></script>

    <!-- Custom Theme JavaScript -->
    <script src="js/sb-admin-2.js"></script>
    
    <script>
	    //common to all pages
	    $( document ).ready(function() {
	
	    	//keep the menu expanded when we are doing folder stuff.
	    	<s:if test="folderExpanded != null && folderExpanded">
	    		$("#folderDropId").addClass("in");
	    		$("#folderDropId").prop('aria-expanded', 'true');
	    	</s:if>
	    	
	    	//keep the menu expanded when we are doing folder stuff.
	    	<s:if test="taskExpanded != null && taskExpanded">
	    		$("#taskDropId").addClass("in");
	    		$("#taskDropId").prop('aria-expanded', 'true');
	    	</s:if>
	    	
	    	//add new group of inventory: side menu
			  $("#addGroupIdBtn").click(function(){    
					
					var addText = $("#newGroupTextId").val();
			    	
			    	if (addText.length != 0){				
			    		
			    		$(document.body).css({'cursor' : 'wait'});
			    		
			    		//fire json to add the new group		    		
			    		$.getJSON("addGroupToSideMenu", {newGroupName : addText}, function(data){	  
			    			console.log(data);
			    			
			    			if(data.groupId != null && data.groupId > 0 ){	
			    				
			    				var full = location.protocol+'//'+location.hostname+(location.port ? ':'+location.port: '');
			    				
			    				full = full + "/web/loadUserGroup.action?groupId="+data.groupId; 		

			    				$("#addGroupLiId").before("<li><a href='" + full + "'> <i class='fa fa-archive fa-fw' ></i>" + addText + "</a></li>");
								$("#newGroupNameErrorId").remove();
								$("#newGroupJsonErrorId").remove();
								$("#newGroupTextId").val("");
								$('#newInvModal').modal('hide');
								
			    			}else if(data.error != null && data.error.length > 0 ){

								console.log($("#newGroupNameErrorId").text().length);
								if($("#newGroupNameErrorId").text().length > 0){
									$("#newGroupNameErrorId").remove();
								}

								$("#newGroupJsonErrorId").remove(); 											

			    				$("#newGroupTextId").before('<p class="alert alert-danger" id="newGroupJsonErrorId">Error proccessing group. Error: ' +data.error+ '</p>');
								
			    			}
			    			$(document.body).css({'cursor' : 'default'});
			    		});
						
			    	}else if($("#newGroupNameErrorId").text().length == 0){
			    		
						if($("#newGroupJsonErrorId").text().length > 0){
								$("#newGroupJsonErrorId").remove();
						}

			    		$("#newGroupTextId").before('<p class="alert alert-danger" id="newGroupNameErrorId">Please enter a new Group Name.</p>');		    		
			    	} 
			    });
	    	
	    	
	    });
    
    
    	function btnLoad(id){
    		var spin = "<i class='fa fa-circle-o-notch fa-spin fa-fw'></i>";
    		
    		$("#"+id).html(spin);   		
    		
    	}
    	//all - Only submit the form once if the form has the submit-once class.
    	$('form.submit-once').submit(function(e){
    	  if( $(this).hasClass('form-submitted')  ){
    	    e.preventDefault();
    	    return;
    	  }
    	  
    	  if ($(this).valid()){ //form valid and ready to be submitted.
    	  	$(this).addClass('form-submitted');
    	  }
    	});
    	
    	
    	function validateNewCat(){
    		
    		if($("#catgNameId").val().length ==0){
    			
    			if($("#errorCatNameId").text().length == 0){
    				$("#catgNameId").before('<p class="alert alert-danger" id="errorCatNameId">Please enter a category name</p>');
    			}
    			return false;
    		}
    	
    		return true;
    		
    	}
    	
    </script>
    

</body>

</html>
   