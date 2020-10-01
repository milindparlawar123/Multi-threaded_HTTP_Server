# cs457-557-fall2020-pa1-milindparlawar123
cs457-557-fall2020-pa1-milindparlawar123 created by GitHub Classroom

Steps to execute Multi-threaded HTTP Server: 
1. enter into src directory of project
2. make 
3. java Server
4. Server starts listening, you can run wget commands to request objects which are present in www directory
5. Ctrl + c to terminate program


Brief Description :
1. place www directory of objects and this project in same directory
2. enter make on terminal to compile the project 
3. enter java Server to run the project
4. Server.java class creates socket and listen on port 8080
5. server creates new thread to serve http request
6. after serving the requests, it prints number of time server serves the same request details on terminal


Input - Output:

Server side : 

Milinds-MacBook-Pro:src milindparlawar$ make
javac *.java
Milinds-MacBook-Pro:src milindparlawar$ java Server
Server listening : localhost
PORT NUMBER : 8080
/sample.html|0:0:0:0:0:0:0:1|51940|1

Client side:

Milinds-MacBook-Pro:objects milindparlawar$ wget -O sample.html http://localhost:8080/sample.html
--2020-10-01 15:28:10--  http://localhost:8080/sample.html
Resolving localhost (localhost)... ::1, 127.0.0.1
Connecting to localhost (localhost)|::1|:8080... connected.
HTTP request sent, awaiting response... 200 OK
Length: 107 [text/html]
Saving to: ‘sample.html’

sample.html         100%[===================>]     107  --.-KB/s    in 0s      

2020-10-01 15:28:10 (10.2 MB/s) - ‘sample.html’ saved [107/107]

invalid filename :
Milinds-MacBook-Pro:objects milindparlawar$ wget -O sample.html http://localhost:8080/poiu.html
--2020-10-01 15:29:31--  http://localhost:8080/poiu.html
Resolving localhost (localhost)... ::1, 127.0.0.1
Connecting to localhost (localhost)|::1|:8080... connected.
HTTP request sent, awaiting response... 404 Not Found
2020-10-01 15:29:31 ERROR 404: Not Found.

Note: In place of localhost, provide remote machine name
