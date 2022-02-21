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
- 
  ` docker image ls `
  
  you should see vip docker image listed `hsolomondocker/ondeck_vip:latest `
  
- Run below command to launch the conainter with ondeck vip app 
   
   ` docker run -it -p9090:8080 hsolomondocker/ondeck_vip:latest `
   
    **Make sure that port 9090 is open in your machine and also not blocked by any firewall setup you may have**
   
- You can Run `docker ps ` to verify that the container is running on your local docker engine
- Open your browser and go to `(http://localhost:9090/api-docs.html)` you should see swagger api doc page

## How to test the functionalities 

1)  POST  **/vip/create**  - is a simple restful endpoint to create a VIP record and send invitation to the given email 

      open your terminal and execute this curl command 

     **you should change the email value in the post data to a valid email that you have access to , this will be good to demonistrate the whole functionality**

      `curl --header "Content-Type: application/json" --request POST --data '{"name":"Henok","email":"solomonmail88@gmail.com"}' http://localhost:9090/vip/create | json_pp`

      after running this command and if its successfull you should get a response json with the created vip record on your terminal 
      
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

      
      
     

      **Also this endpoint will try to send an actual invitation email to the destination vip email address**
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

    **Note that - we are using H2 in mememory DB that means the DB uses its runtime memory to store the records , this means that any records that you created will be lost after restarting conatiner, I thought this should be enough as its for demonistration purpose**


  
3)  GET  **vip/Accept/{{vipRecordId}** - mark the invitation status of vip record with vipRecordId to **ACCEPTTED**  


 
    
