credentials for DB are in model/DBConnection.java

My app will add test user with password test if no users are in DB, it will also add Countries and associated cities if there are none already in the DB..

A.   Create a log-in form that can determine the user’s location and translate log-in and
error control messages (e.g., “The username and password did not match.”) into two languages.

--- My program will translate the login page to spanish if that is the region selected, as well as
the login error messages
i used español españa to test

C. 
-- appointments can be added once you add customers

F. 
--exceptions: DAOImplementations/AppointmentDAO.java line 129-176 .
	      modifyAppointmentController.java line 76.

G.  --
-- lambda at model/AlertBox.java line 25 -- this lambda translates any given comma separated string
into spanish, it's an efficient way to translate any phrases. Could be easily modified to translate
other languages too.
Lambda at AppointmentDAO.java line 32 is an easy way to check appointments before adding or
updating to make sure that they are ready for DB entry.


H.   Write code to provide an alert if there is an appointment within 15 minutes of the user’s
log-in. 
-- doesn't provide an alert if the appointment has already passed, but if it is within
the next 15 minutes it will.


I.   Provide the ability to generate each  of the following reports:

•   number of appointment types by month 
-- reports page

•   the schedule for each consultant 
-- Each user's schedule can be filtered at appointment menu top right

•   one additional report of your choice 
-- reports page, i found the busiest user and listed their # of appointments scheduled


J.   Provide the ability to track user activity by recording timestamps for user log-ins
in a .txt file. Each new record should be appended to the log file, if the file already exists.

 -- text file should be in the AppointmentApp folder named UserLog (that's where it is for me)



