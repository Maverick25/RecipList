/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.reciplist.controller;

import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.QueueingConsumer;
import dk.reciplist.dto.ComplexMessageDTO;
import dk.reciplist.dto.LoanRequestDTO;
import dk.reciplist.messaging.Receive;
import dk.reciplist.messaging.Send;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *  
 * @author marekrigan
 */
public class ContactSelectedBanks 
{
    private static Gson gson;
    
    public static void receiveMessages() throws IOException,InterruptedException
    {
        gson = new Gson();
        
        HashMap<String,Object> objects = Receive.setUpReceiver();
        
        QueueingConsumer consumer = (QueueingConsumer) objects.get("consumer");
        Channel channel = (Channel) objects.get("channel");
        
        
        ComplexMessageDTO messageDTO;
        LoanRequestDTO loanRequestDTO;
        List<String> selectedBanks;
        
        while (true) 
        {
          QueueingConsumer.Delivery delivery = consumer.nextDelivery();
          String message = new String(delivery.getBody());
          
          messageDTO = gson.fromJson(message, ComplexMessageDTO.class);
          
          loanRequestDTO = messageDTO.getDto();
          selectedBanks = messageDTO.getBanks();
          
          sendMessage(loanRequestDTO,selectedBanks);

          channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
        }
        
    }
    
    public static void sendMessage(LoanRequestDTO dto,List<String> banks) throws IOException
    {
        String message = gson.toJson(dto);
        
        Send.sendMessage(message,banks);
    }   
}
