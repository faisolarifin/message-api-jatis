package com.messageapi.dc.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.messageapi.dc.controller.MessageApiController;
import com.messageapi.dc.dto.payload.PayloadDTO;
import com.messageapi.dc.dto.response.ResponseContacts;
import com.messageapi.dc.dto.response.ResponseMessages;

import com.messageapi.dc.model.Token;
import com.messageapi.dc.repository.TokenRepository;
import com.messageapi.dc.response.ResponseUtil;
import lombok.extern.slf4j.Slf4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class MessageService {

    Logger logger = LoggerFactory.getLogger(MessageApiController.class);

    @Autowired
    private ProducerService producerService;

    @Autowired
    private TokenRepository repository;

    public ResponseEntity<Object> addMessage(String author, PayloadDTO request){
        HashMap <String, Object> responseFail = new HashMap<>();

        try{

            if (author ==  null){
                responseFail.put("status","Token Null");
                responseFail.put("code","401");
                logger.error("[Message] token null");
                return new ResponseEntity<>(responseFail,HttpStatus.UNAUTHORIZED);
            }

            // check token
            String tokenReal = author.substring(7,author.length());
            Optional<Token> tokenOptional = repository.findByToken(tokenReal);

            if (tokenOptional.isEmpty()){
                responseFail.put("status","Token Not Found");
                responseFail.put("code","401");
                logger.error("[Message] token not found");
                return new ResponseEntity<>(responseFail,HttpStatus.UNAUTHORIZED);
            }

            // check property
            if (request.getMessaging_product() == null || !(request.getMessaging_product().equals("whatsapp"))){
                String message_product_err = "messaging_product null or not whatsapp";
                responseFail.put("status",message_product_err);
                responseFail.put("code","400");
                logger.error("[Message] messaging_product null or not whatsapp");
                return new ResponseEntity<>(responseFail,HttpStatus.BAD_REQUEST);
            }

            if (request.getRecipient_type()== null || !(request.getRecipient_type().equals("individual"))){
                String recipient_err = "recipient_type null or not individual";
                responseFail.put("status",recipient_err);
                responseFail.put("code","400");
                logger.error("[Message] recipient_type null or not individual");
                return new ResponseEntity<>(responseFail,HttpStatus.BAD_REQUEST);
            }

            if (request.getTo() == null || request.getTo().length() <= 11){
                log.info("jumlah nomor : {}",request.getTo().length());
                String noWa = "Number is null or digit number less than equals to 11";
                responseFail.put("status",noWa);
                responseFail.put("code","400");
                logger.error("[Message] Number is null or digit number less than equals to 11");
                return new ResponseEntity<>(responseFail,HttpStatus.BAD_REQUEST);
            }

            if (request.getType() == null || !(request.getType().equals("text")) ){
                String type_err = "Type null or not text";
                responseFail.put("status",type_err);
                responseFail.put("code","400");
                logger.error("[Message] Type null or not text");
                return new ResponseEntity<>(responseFail,HttpStatus.BAD_REQUEST);
            }

            if (request.getText().getPreview_url() == null || !(request.getText().getPreview_url().equals(false))){
                String preview_err = "Preview url is null or not false";
                responseFail.put("status",preview_err);
                responseFail.put("code","400");
                logger.error("[Message] Preview url is null or not false");
                return new ResponseEntity<>(responseFail,HttpStatus.BAD_REQUEST);
            }

            if (request.getText().getBody() == null){
                String body_err = "Body is null";
                responseFail.put("status",body_err);
                responseFail.put("code","400");
                logger.error("[Message] Body is null");
                return new ResponseEntity<>(responseFail,HttpStatus.BAD_REQUEST);
            }

            UUID uuid = UUID.randomUUID();
            String uuIdGenerate = uuid.toString();

            //encrypt message
            char[] chars = request.getText().getBody().toCharArray();

            String strEncrypt = "";
            for (char c: chars) {
                    c += 5;
                    strEncrypt += c;
            }


            //decrypt message
            char[] decrypt = strEncrypt.toCharArray();
            String strDecrypt = "";
            for (char st: decrypt) {
                st -= 5;
                strDecrypt += st;

            }

            Map<String, Object> payload = (new ObjectMapper()).convertValue(request, Map.class);

            producerService.sendMessage(uuIdGenerate, payload);
            logger.info("[Message] Payload : {}, Message_id : {}",payload,uuIdGenerate);
            //Response

            List<ResponseContacts> responseContacts = new ArrayList<>();

            responseContacts.add(ResponseContacts.builder()
                    .input(request.getTo())
                    .wa_id(request.getTo())
                    .build());
            List<ResponseMessages> responseMessages = new ArrayList<>();

            responseMessages.add(ResponseMessages.builder()
                            .id(strEncrypt)
                    .build());
            logger.info("[Message] Response successfuly sended");
            return  ResponseUtil.build("whatsapp",responseContacts,responseMessages,HttpStatus.OK);


        }catch (Exception e){
            responseFail.put("status","ERROR_INTERNAL_SERVER");
            responseFail.put("code","500");
            logger.error("[Message] ERROR_INTERNAL_SERVER, Has Error : {}",e.getMessage());
            return  new ResponseEntity<>(responseFail, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
