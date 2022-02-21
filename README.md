## Ondeck VIP management

This is solution for assesement [here](https://odteam.notion.site/odteam/No-Code-Infrastructure-Engineer-Take-Home-Test-0987b15357f941ab80ca79c16b23c9cd)

## Tools Used 
- Java 11 
- Spring Boot 2.6.3
- Gradle build tool 
- H2 in memory DB
- java SMTP email client 
- thymeleaf for templating engin 
- docker
- git , github 

## How to run this app on your local environment 

- clone the docker image from Docker hub 
  ` docker pull hsolomondocker/ondeck_vip `
  
- Run below command to confirm the image 
  ` docker image ls `
  you should see vip docker image listed `hsolomondocker/ondeck_vip:latest `
  
- Run below command to launch the conainter with ondeck vip app 
   ` docker run -it -p9090:8080 hsolomondocker/ondeck_vip:latest `
   NB- Make sure that port 9090 is open in your machine and also not blocked by any firewall setup you may have  
   
- You can Run `docker ps ` to verify that the container is running on your local docker engine
- Open your browser and go to `(http://localhost:9090/api-docs.html)` you should see wagger api doc UI

## How to test the functionalities 

1)  POST  **/vip/create**  - used to create a VIP record and send invitation to the given email 
e.g open your terminal execute the below curl command 
    you can change the email value in the post data to a valid email that you have access too , this will be good to demonistrate the whole functionality 
 
  `curl --header "Content-Type: application/json" --request POST --data '{"name":"Henok","email":"solomonmail88@gmail.com"}' http://localhost:9090/vip/create | json_pp`
  
  after running this command and if its success you should get a response json with the created vip record on your terminal 
  **Also it will send a simple invitation email** to the email address assoicated with the vip post data
  hence the next step after running this command will be to check the mail inbox 

2)  GET  **vip/findAll** - lists all registred vip records in the current H2 in memory DB
e.g open your terminal execute the following curl command 
`curl --header "Content-Type: application/json" --request GET http://localhost:9090/vip/findAll | json_pp`

you should see list of VIP records currently found in your local H2 in memory DB 
NB - we are using H2 in mememory DB that means the DB uses its runtime memory to store the records , this means that the data will be lost after stoing and starting the conatiner , I thought this should be enough for as its for demonistration purpose

3)  GET  **vip/Accept/{{vipRecordId}** - mark the invitation status of vip record with vipRecordId to **ACCEPTTED**  


 
    
