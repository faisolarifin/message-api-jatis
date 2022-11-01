# WhatsApp Cloud Api
Final Task Intership Jatis Mobile as IT Dev (Core)

## Tech Stacks Used
[![](https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white)]() [![](https://img.shields.io/badge/-ActiveMQ-red)]() [![](https://img.shields.io/badge/MySQL-005C84?style=for-the-badge&logo=mysql&logoColor=white)]() [![](https://img.shields.io/badge/Postman-FF6C37?style=for-the-badge&logo=postman&logoColor=white)]()

## How To Use
-> You must install Install JDK 11,Text Editor(Intellij, VSC, Eclipse, NeatBeans), ActiveMQ, XAMPP or MySQL

-> Run your ActiveMQ using command prompt

-> Build Database database with name : `messageapi` in MySQL or XAMPP

-> Change database password in text editor :
- Search the directory `src/main/resources/application.properties`then you find `spring.datasource.password=password`
- if you use `XAMPP`, you should delete valiue password
- if you use `MySQL Workbench`, you should change the password if this password is different from yours
<br/>

-> Run Spring Boot using Eclipse, Inteliij IDEA, Visual Studio Code, etc.<br/>

-> Run endpoint with url : `http://localhost:8080/message` in postman <br/>

-> Add Token <br/>
Example : <br/>
![](https://github.com/hafidzencis/jatis-message-api/blob/master/imggithub/add-token-postman.jpg)
-> Add Request/Payload <br/>
Example : <br/>
![](https://github.com/hafidzencis/jatis-message-api/blob/master/imggithub/post-body.jpg)<br/>
Copy this text in to your postman : <br/>
```
{
  "messaging_product": "whatsapp",
  "recipient_type": "individual",
  "to": "62812345678910",
  "type": "text",
  "text": {
    "preview_url": false,
    "body": "Sugeng Enjing, Good Morning, Selamat Pagi,Ohayuokkk"
  }
}
```




