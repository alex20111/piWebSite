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
	      
	      		<div id="currWeather" style="display:none">    </div>     
	      		
	      		
         
        	       
	
  	
        </div>
       <!--  main content ENNDDDD  -->

    </div>
    <!-- /#wrapper -->

    <!-- jQuery -->
    <jsp:include page="/jsp/jquery.jsp" /> 
    <script>
	    $( document ).ready(function() {
	    	
	    	loadCurrWeather();
	    });
    
	    function loadCurrWeather(){
	    		
	    		$("#currWeatherWait").show();
	    		
	    		$.getJSON("loadAjaxMainPageCurrWeather", function(data){
	
  			
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
