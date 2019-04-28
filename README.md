# Java Server/Client Card-Game-Library Project

## Overview
This is a project for our 3rd Year Software Development module Professional Practice in IT, using the Java Programming Language to construct a Server/Client based card game Library that will allow Clients to connect to the Server, Register/Login to a Server side Database, play a selection of card games then save/load any previous gamestates from the database and continue off where they left.

## Goals 
Our goals for this Project were to create a fully Server side program that allows multiple clients to connect at a time, Login/Register, play a selection of card games and have the ability to save/load any gamestate they wish with everything being stored Server side on SQL.

## Running the Program
After cloning the Project, there must be a SQL connection available for the Program, so WampServer must be running. Two command prompts must be opened for running the program for yourself, one for hosting the server the other being the client. In both prompts navigate to the directory where you have the .jar files and run the following in each

`java -jar serverRunner.jar`\
`java -jar clientRunner.jar`

Assuming the WampServer is running the database and it's tables should be automatically created allowing for population immediately.
The client window should then prompt for input, allowing you to traverse the Program.














