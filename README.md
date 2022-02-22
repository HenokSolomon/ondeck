## Ondeck vip app

This is solution for assesement [here](https://odteam.notion.site/odteam/No-Code-Infrastructure-Engineer-Take-Home-Test-0987b15357f941ab80ca79c16b23c9cd)

## Tools Used 
- Java 11 
- Spring Boot 2.6.3
- Gradle build tool 
- H2 in memory DB
- Restful API
- java SMTP email client 
- thymeleaf for templating engine 
- docker
- git , github 

## Vip DB Entities 

![vip app db schema](vip-db-imge.png)



## How to run this app on your local environment 


- Pull the latest docker image from Dockerhub [here](https://hub.docker.com/r/hsolomondocker/ondeck_vip/tags)

     Or simply run this command to pull the latest image tag

    `docker pull hsolomondocker/ondeck_vip`

    Run this command to confirm if the image is loaded to your host machine 

    ` docker image ls `

    you should see vip docker image listed `hsolomondocker/ondeck_vip:latest `
  
- Run this command to launch the conainter with ondeck vip app 
   
   ` docker run -it -p9090:8080 hsolomondocker/ondeck_vip:latest `
   
    **Make sure that port 9090 is open in your machine and also not blocked by any firewall setup you may have**
   
- You can Run `docker ps ` to verify that the container is running on your local docker engine
- Open your browser and go to (http://localhost:9090/api-docs.html) you should see swagger api doc page

## How to test the functionalities 

>Note that basic validations are already implemented by all services in this app.
>This include Email format validation , duplicate email prevention or prevention for duplicate vip record and e.t.c 
>The app is built with generic error handler That means if your call to any of vip apis ended with error then you should expect json reponse that looks like below & with different error message

` {
    "error" : "bad request , please fix your request and try again",
    "message" : "vip record doesn't exist ",
    "path" : "/vip/accept/250cb745-82e8-431b-8532-a6e0af9f53d4",
    "status" : 400,
    "timestamp" : "2022-02-21"
}`


1)  POST  **/vip/create**  - is a simple restful endpoint to create a VIP record and send invitation to the given email 

      open your terminal and execute this curl command 

     **you should change the email value in the post data to a valid email that you have access to , this will be good to demonistrate the whole functionality**

      `curl --header "Content-Type: application/json" --request POST --data '{"name":"Henok","email":"solomonmail88@gmail.com"}' http://localhost:9090/vip/create | json_pp`

     If the request is successful you should see a response json with the newly created vip record
      
      sample response 

      `      {
         "createdBy" : "system",
         "createdDate" : "2022-02-21T20:40:28.930234",
         "email" : "solomonmail88@gmail.com",
         "invitationConfirmDate" : null,
         "invitationConfirmRemark" : null,
         "invitationSentDate" : null,
         "invitationStatus" : "PENDING",
         "name" : "Henok",
         "updatedBy" : "system",
         "updatedDate" : "2022-02-21T20:40:28.930239",
         "vipRecordId" : "3bfc5c85-97b5-40bf-bc2f-fdc5041edc41"
      }`

      
      
     

      *Also this endpoint will try to **send an actual invitation email** to the destination vip email address*
        hence please go a head and check the inbox

       Alternatively you can call `GET /vip/findAll` to verify that vip record is created


2)  GET  **vip/findAll** - is a simple restful endpoint to list all registred vip records in the current H2 in memory DB

      open your terminal and execute this curl command 


      `curl --header "Content-Type: application/json" --request GET http://localhost:9090/vip/findAll | json_pp`


      you should see list of VIP records currently registred in your local H2 in memory DB 

      sample response 

      `
            [
         {
            "createdBy" : "system",
            "createdDate" : "2022-02-21T20:40:28.930234",
            "email" : "solomonmail88@gmail.com",
            "invitationConfirmDate" : null,
            "invitationConfirmRemark" : null,
            "invitationSentDate" : "2022-02-21T20:40:35.044186",
            "invitationStatus" : "PENDING_CONFIRM",
            "name" : "Henok",
            "updatedBy" : "system",
            "updatedDate" : "2022-02-21T20:40:28.930239",
            "vipRecordId" : "3bfc5c85-97b5-40bf-bc2f-fdc5041edc41"
         }
      ]
      `

    *we are using H2 in mememory DB that means the DB uses its runtime memory to store the records , this means any records that you created will be lost if you restart your docker conatiner ** Its done this way because the app is intended only for demonstration purpose*

  
3)  GET  **vip/Accept/{vipRecordId}** & **vip/reject/{vipRecordId}** - mark the invitation status of vip record to **ACCEPTTED OR REJECTED**  

      > The invitation email that is sent during the create operation should have two embedded buttons 

      > `Accept Invitation` & `No Thanks `
      
      > You can click either of this buttons to test accept and reject endpoints 
      
      you can also do it using curl command 
      
      > But First call the `/vip/findAll` and copy the vipRecordId value which you want to update 

      then 

      `curl --header "Content-Type: application/json" --request GET http://localhost:9090/vip/accept/250cb745-82e8-431b-8532-a6e0af9f53d4 | json_pp`

       sample response 

        `    {
                 "createdBy" : "system",
                 "createdDate" : "2022-02-21T20:40:28.930234",
                 "email" : "solomonmail88@gmail.com",
                 "invitationConfirmDate" : "2022-02-21T21:17:36.92906",
                 "invitationConfirmRemark" : null,
                 "invitationSentDate" : "2022-02-21T20:40:35.044186",
                 "invitationStatus" : "ACCEPTED",
                 "name" : "Henok",	
                 "updatedBy" : "system",
                 "updatedDate" : "2022-02-21T20:40:28.930239",
                 "vipRecordId" : "3bfc5c85-97b5-40bf-bc2f-fdc5041edc41"
              }`

 4) POST  **/vip/create-no-invitation** This API endpoint is built to simulate failed invitation email integration 
 
	>The solution I came up for this is to implement a scheduled batch job which is configured to run every 10sec *(for demo purpose)*
	>This batch job when triggered it fetchs all vipRecords which **InvitationStatus = 'PENDING'** and it trys to send an invitation email
 	>If the email succeeds this time then the batch updates its  **InvitationStatus = 'PENDING_CONFIRMATION'** where from this point on It follows the normal 			flow Where the vip user gets the email then accepts or reject the invitation. 
	>Incase the email failed again then the batch will try it again the next time it's triggered.

	This process can be sumulated by this endpoint 
	
	open your terminal and run this command - please change the email address value to the one that you own 
	
	`curl --header "Content-Type: application/json" --request POST --data '{"name":"Henok_paga","email":"hsolomon@paga.com"}' http://localhost:9090/vip/create-no-invitation | json_pp` 

	then after that quickly call `/vip/findAll ` endpoint and observe the vipRecords invitationStatus - >  it should be **'PENDING'**
	
	Then wait for a max 10sec and execute `/vip/findAll ` endpoint again , by this time the batch should have run in backend and should have already sent the 		invitation email .





 
