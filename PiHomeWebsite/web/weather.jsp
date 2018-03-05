<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<%-- start header  --%>
	<jsp:include page="/jsp/header.jsp" />
	
	<!-- Morris Charts CSS -->
    <link href="css/morris.css" rel="stylesheet">

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
	
        <s:if test="#session.user != null">
        	<div id="page-wrapper">
		</s:if>
		<s:else>
			<div id="page-wrapper-noMenu">
		</s:else>
    
		<%-- MAIN CONTENT START HERE --%>	
		
		<s:if test="actionErrors.size > 0" >
			<div class="alert alert-danger">
		  		<p>
		        	<s:actionerror escape="false"/>
		        </p>
			</div>
		</s:if>    

	      <div id="currWeatherWait" style="display:none">	      	
	      	<i class="fa fa-spinner fa-pulse fa-3x fa-fw"></i>
			<span class="sr-only">Loading...</span>
	      </div>
	      
	      	<div id="currWeather" style="display:none">
 			</div>           
            
            <div class="row"> 
            	<div class="col-sm-2">
	                	<s:form  theme="simple" action="displayCurrentWeather" cssClass="submit-once form-inline" id="intvFormId">
	                			<label>Interval:</label>
		                       	<s:select name="interval"	
		                       		 id="intervalId"							
									 cssClass="form-control"
									 list="@net.project.enums.Interval@values()"
									 listValue="getIntvName()"
									 listKey="getIntvTime()"
									 onchange=""
									 />
									<i class='fa fa-circle-o-notch fa-spin fa-fw' style="display:none" id="loadingSpinnerId"></i>
						</s:form>
	            </div>
            </div> 
            
            <div class="row">       	
            	
                <div class="col-lg-12">
	                
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            Current temperature
                        </div>
                        <!-- /.panel-heading -->
                        <div class="panel-body">
                        
                            <div id="morris-area-chart"></div>	
                            						
                        </div>
						
						 <div class="panel-footer text-center"><div id="legend"></div></div>
                        <!-- /.panel-body -->
                    </div>
                    <!-- /.panel -->
                </div>          
                <!-- /.col-lg-6 -->
            </div>
            <!-- /.row -->    
        </div>          
        <%-- MAIN CONTENT END HERE --%>
     </div>       

   <!-- /#wrapper -->

    <!-- jQuery -->
    <jsp:include page="/jsp/jquery.jsp" /> 
    <script src="js/raphael.min.js"></script>
    <script src="js/morris.min.js"></script>
    <%--<script src="js/morris-data1.js"></script> --%>
    <script>
	    $( document ).ready(function() {
	    	
	    	loadCurrWeather();
	    });
    
    	<s:property value="chartData" escapeHtml="false"/>    	
    	
    	$("#intervalId").change(function(){    		
    		
    		var intvValue = $("#intervalId").val();
    		console.log(intvValue);
    		
    		$("#intervalId").prop("disabled",true);
    		
    		var input = $("<input>")
            .attr("type", "hidden")
            .attr("name", "interval").val(intvValue);
    		
			$('#intvFormId').append($(input));    		
    		
    		$("#loadingSpinnerId").show(200);
    		
    		$("#intvFormId").submit();
    		 
    	});
        	
    	function loadCurrWeather(){
    		
    		$("#currWeatherWait").show();
    		
    		$.getJSON("loadAjaxCurrWeather", function(data){

    			if (data != null && data.length > 0){
    				$("#currWeather").show();
    				$("#currWeather").html(data);
    				$("#currWeatherWait").hide();
    			}else{
    				$("#currWeather").show();
    				$("#currWeather").html("Error loading current weather data");
    				$("#currWeatherWait").hide();
    			}    			
    			
    		});
    	}
    	    	
    </script>
    

	<jsp:include page="/jsp/footer.jsp" />

