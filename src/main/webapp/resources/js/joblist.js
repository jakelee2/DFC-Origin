var JOB_MANAGEMENT = (function(){
	
	function levelDropDownAction(jobId, jobOwner, selectedLevel, selectedLevelName){
		$("#level .dropdown-menu li[id='"+jobId+"']").removeClass('selected');									// remove all 'selected' first
		$("#level .dropdown-menu li[id='"+jobId+"'] a[id='"+selectedLevel+"']").parent().addClass('selected');	// then put a new 'selected' in the <li> tag
		$("#level .dropdown-menu li[id='"+jobId+"'] a").children("i").remove();								// remove all 'check-mark'
		$("#level .dropdown-menu li[id='"+jobId+"'] a[id='"+selectedLevel+"']").append("<i class=\"fa fa-check check-mark\"></i>");	// then append a new 'check-mark'
		$("#level .selectLevel_"+jobId).text(selectedLevelName);// display the current level name in the drop down 

		if($("#"+selectedLevel).attr('id') != "defaultLevelDisplay"){	// if the user select "-- -- Select Level -- --", do nothing. 
			changeJobLevel(jobId, jobOwner, selectedLevel, selectedLevelName);
		}
	}
	
	function changeJobLevel(jobId, jobOwner, selectedLevel, selectedLevelName){
		var dataToBeSent = {
			jobId : jobId,
			selectedLevel : selectedLevel,
			selectedLevelName: selectedLevelName
		}
		
		$.ajax({
			url: '/jobs/changeJobLevel',
			data: dataToBeSent,
			type: 'POST',
			success: function(change_result, xhr){
				updateUsersForJob(jobId, jobOwner, selectedLevel);
			},
			error: function (change_result, xhr) {
			}
		});
	}
	
	function updateUsersForJob(jobId, jobOwner, selectedLevel){
		
		var dataToBeSent = {
				jobId : jobId,
				selectedLevel : selectedLevel
		}

		$.ajax({
			url: '/jobs/findUsersForJob',
			data: dataToBeSent,
			type: 'POST',
			success: function(result, xhr){
				$("#transfer ul[id='"+jobId+"'] li[id='"+jobId+"']").remove();

		            var html = 	"<li id=\""+jobId+"\">" +
			           				"<a href=\"#\" id=\"defaultUserDisplay\" class=\""+jobOwner+"\">" +
			           					"<span class=\"text\">-- -- Select User -- --</span>" +
			           				"</a>" +
			           			"</li>"; 
        		for(var i = 0; i < result.length; i++){
                	if(jobOwner != result[i].username){
                		html += "<li id=\""+jobId+"\">" +
                					"<a href=\"#\" id=\""+result[i].id+"\" class=\""+jobOwner+"\">" +
                						"<span class=\"text\">"+result[i].username+"</span>" +
                					"</a>" +
                				"</li> " ;
                	}
                }
                $("#transfer ul[id='"+jobId+"']").html(html);
			},
			error: function (result, xhr) {}
		});
	}

	
	// Job transfer
	function transferDropDownAction(jobId, userJobId, jobOwner, selectedUser, selectedUserName){
		$("#transfer .dropdown-menu li[id='"+userJobId+"']").removeClass('selected');								// remove all 'selected' first
		$("#transfer .dropdown-menu li[id='"+userJobId+"'] a[id='"+selectedUser+"']").parent().addClass('selected');// then put a new 'selected' in the <li> tag
		$("#transfer .dropdown-menu li[id='"+userJobId+"'] a").children("i").remove();								// remove all 'check-mark'
		$("#transfer .dropdown-menu li[id='"+userJobId+"'] a[id='"+selectedUser+"']").append("<i class=\"fa fa-check check-mark\"></i>");	// then append a new 'check-mark'
		$("#transfer #selectUser_"+userJobId).text(selectedUserName);															// display the current user name in the drop down 

		if($("#"+selectedUser).attr('id') != "defaultUserDisplay"){	// if the user select "-- -- Select User -- --", do nothing. 
			transferJob(jobId, userJobId, selectedUser);
		}
		else {
			dectivateTransfer(jobId, userJobId, jobOwner);
		}
	}
	
	function dectivateTransfer(jobId, userJobId, jobOwner){
		var dataToBeSent = {
				jobId : jobId,
				userJobId : userJobId,
				jobOwner: jobOwner
		}
		$.ajax({
			url: '/jobs/dectivateTransfer',
			data: dataToBeSent,
			type: 'POST',
			success: function(transfer_result, xhr){
			}
		});
	}

	function transferJob(jobId, userJobId, selectedUser){
		var dataToBeSent = {
			jobId : jobId,
			userJobId : userJobId,
			transfereeId : selectedUser
		}
		
		$.ajax({
			url: '/jobs/transfer',
			data: dataToBeSent,
			type: 'POST',
			success: function(transfer_result, xhr){
			},
			error: function (transfer_result, xhr) {
			}
		});
	}
	
	// functions in 'return' block are called from outside or triggered by itself
	return {

		getSelectedLevel: (function(){
			$("#level .dropdown-menu li a").click(function(){
				var jobId = $(this).parent().attr('id');					// Get the job id from drop down list [li]
				var jobOwner = $(this).attr('class');						// Get the job owner id from drop down list [a]
				var selectedLevel = $(this).attr('id');						// Get the job security level from drop down list [a]
				var selectedLevelName = $(this).text();						// inside [span]
				levelDropDownAction(jobId, jobOwner, selectedLevel, selectedLevelName);
			});
		})(),
		
		
		/* 
		 * The click() binding you're using is called a "direct" binding which will only attach the handler to elements that already exist. 
		 * It won't get bound to elements created in the future. To do that, you'll have to create a "delegated" binding by using on().
		 * http://learn.jquery.com/events/event-delegation/
		 */
		getSelectedUser: (function(){
			$("#transfer .dropdown-menu").on('click', "li a", function(e){
				var jobId = $(this).parent().attr('class');					// Get the job id from drop down list [li]
				var userJobId = $(this).parent().attr('id');				// Get the users_jobs id from drop down list [li]
				var jobOwner = $(this).attr('class');						// Get the job owner id from drop down list [a]
				var selectedUser = $(this).attr('id');						// Get the user id from drop down list [a]
				var selectedUserName = $(this).text();						// Get the user name from drop down list inside [span]
				transferDropDownAction(jobId, userJobId, jobOwner, selectedUser, selectedUserName);
			});
		})()
	};
})();