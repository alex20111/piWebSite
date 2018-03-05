<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>


<%-- start header  --%>
	<jsp:include page="/jsp/header.jsp" />

		<link href="css/uploadfile.css" rel="stylesheet">

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
		
			<!--  .fa-download  .fa-share-square-o  .  .fa-upload -->
		
		    <div class="container-fluid">
		      <div class="row">        
				
		        <div class="col-xs-18 col-md-12 main">
			        <h2>Upload file(s) to <s:property value="viewFolder.folderName"/></h2>
					<div id="fileuploader">Browse</div>
					<button id="extrabutton" class="btn btn-primary">Start Upload</button>
					<button id="resetId" class="btn btn-primary">Reset</button>
					<div id="eventsmessage"></div>
			        
		       </div>        
		      </div>
		    </div> 
		
        </div>
       <!--  main content ENNDDDD  -->

    </div>
    <!-- /#wrapper -->

    <!-- jQuery -->
    <jsp:include page="/jsp/jquery.jsp" /> 

	 <script src="js/jquery.uploadfile.js"></script>
     <!-- Menu Toggle Script -->
     
   <script>
    $( document ).ready(function() {

    	<s:if test="#session.user == null">
    		$("#wrapper").addClass("toggled");
    	</s:if>	
  

    	var nbrFileSuccess = 0;
    	var fileUpload = $("#fileuploader").uploadFile({
    		url:"uploadFiles.action?folderId=<s:property value="folderId"/>",
    		fileName:"file",
    			multiple:true,
    			dragDrop:true,
    			autoSubmit:false,
    			uploadStr:"Browse",
    			showFileSize:true,
    			returnType:"json",
    			allowDuplicates: false,
    			sequential:true,
    			onSuccess:function(files,data,xhr,pd)
    			{
    				//$("#eventsmessage").html($("#eventsmessage").html()+"<br/>Success for: "+JSON.stringify(data)); 
    				nbrFileSuccess ++;
    			},
    			afterUploadAll:function(obj)
    			{
    				if (nbrFileSuccess > 0){
    					//update  the file count through ajax
    					$.getJSON("updateFolder", {folderId : <s:property value="folderId"/>}, function(data){    		
		    			   		
		    			}); 
    					
    					var number = parseInt($("#flNbr<s:property value="folderId"/>").text());
    					number = number + nbrFileSuccess;
    					$("#flNbr<s:property value="folderId"/>").html(number);
    				}
    				
    				$("#eventsmessage").html($("#eventsmessage").html() + "<br/>" + nbrFileSuccess + " files uploaded ");   				

    			},
    		//	onError: function(files,status,errMsg,pd)
    		//	{
    		//		$("#eventsmessage").html($("#eventsmessage").html()+"<br/><span class='fa fa-times-circle ' style='font-weight: bold;color:red'>Error for: "+JSON.stringify(files) + " " +files + " errMsg: " + errMsg + " </span>");
    		//	},
    		//	onSubmit:function(files)
    		//	{
    				
    		//		$("#eventsmessage").html($("#eventsmessage").html()+"<br/>Submitting:"+JSON.stringify(files) + " " +files);    		
    
   // 				console.log("cont");
   // 			},
    			onSelect: function (files) {
    		//		$("#eventsmessage").html($("#eventsmessage").html()+"<br/>on select: " + files[0].name);
    		//		console.log(files);
    				for (i = 0; i < files.length; i++) { 
    					checkFile(files[i].name);
    				 }    				
                    return true;
                }
    		});
    	
    	$("#extrabutton").click(function()
    	{
    		fileUpload.startUpload();
    	}); 
    	
    	$("#resetId").click(function()
    	{
    		nbrFileSuccess = 0;
    	  	fileUpload.cancelAll();
    	  	$("#eventsmessage").html("");
    	});
    	  	
    	
    	 function checkFile(file){
    	    	
    	    	var stringFile = String(file);
    	  
    		    $.getJSON("checkFileExist", {fileToCheck : stringFile, folderId : <s:property value="folderId"/>}, function(data){
    		
    				if(data != null ){				
    					console.log("data: " + data);
    					if (data == 'file Exist on disk'){    				
    						 $("<div class='ajax-file-upload-error'><b>" + stringFile + "</b> File exist in directory. File will not be uploaded. </div>").appendTo(fileUpload.errorLog);
    						 fileUpload.cancel(stringFile);
    					}
    					else if (data == 'ERROR not logged in'){
    						 $("<div class='ajax-file-upload-error'><b> You are not logged in, cannot upload </div>").appendTo(fileUpload.errorLog);
    						 fileUpload.stopUpload();
    					}
    				}    		
    			});    		 
    	    }     	
    });
   
    
    </script>    

	<jsp:include page="/jsp/footer.jsp" />
