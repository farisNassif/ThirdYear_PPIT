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