# Java Server/Client Card Library Project

## Overveiw
This is a project for our 3rd Year Software Development module Professional Practice in IT, using the Java Programming Language to construct a Server/Client based card game Library that will allow Clients to connect to the Server, Register/Login to a Server side Database, play a selection of card games then save/load any previous gamestates from the database and continue off where they left.

## Goals 
Our goals for this Project were to create a fully Server side program that allows multiple clients to connect at a time, Login/Register, play a selection of card games and have the ability to save/load any gamestate they wish with everything being stored Server side on SQL.

## Running the Program
After cloning the Project, there must be a SQL connection available for the Program, so WampServer must be running. Two command prompts must be opened for running the program for yourself, one for hosting the server the other being the client. In both prompts navigate to the directory where you have the .jar files and run the following in each

`java -jar serverRunner.jar`\
`java -jar clientRunner.jar`

Assuming the WampServer is running the database and it's tables should be automatically created allowing for population immediately.
The client window should then prompt for input, allowing you to navigate the Program.













Cormac Final Notes 
----------
Overall I would designate this project a success as we achieved the goal of having card 
games hosted on a server and saving them to a database. With that being said ofcourse we 
have had our fair share of issues. A major issue I ran into came with making multiple 
clients be able to play the same game with another client. I tried majorly with the game 
lives to achieve this goal but I could not get each client to play cooperatively as they 
would all need different ip addresses and ofcourse with the server being hosted locally 
all the ip addresses are the same. I then tried distinguishing different users by 
assigning each player a different client id but that also came with it's own issues so 
I then realised that I could not achieve this objective. The other objective that I 
struggled with was to have the game snap working with a set timer. I had it working as 
a normal class by itself but getting it working with a client proved to difficult 
unfortunately. The working version should be in a recent commit on github titled 
"adding lives auto flip". I definitely have learnt a lot from this project such as 
how important time management and consistency are. Communication with your group 
partner is definitely key as a lot of their code may affect yours and you will need 
to work together to merge it.
