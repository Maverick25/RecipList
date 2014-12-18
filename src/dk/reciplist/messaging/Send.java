/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.reciplist.messaging;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author marekrigan
 */
public class Send 
{
    // changes to come!!!!!!!!!!!!!!!!!!!
    
//    private static final String TASK_QUEUE_NAME = "queue_recipList";
    
    private static final String EXCHANGE_NAME = "direct_logs";
    
    public static void sendMessage(String message, List<String> banks) throws IOException 
    {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("datdb.cphbusiness.dk");
	factory.setUsername("student");
	factory.setPassword("cph");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        
        channel.exchangeDeclare(EXCHANGE_NAME, "direct");
        
        String type;
        
        switch(banks.size())
        {
            case 4:
                type = "high";
                break;
            case 3:
                type = "medium";
                break;
            case 2:
                type = "low";
                break;
            case 1:
                type = "miserable";
                break;
            case 0:
                type = "none";
                break;
            default:
                type = "error";
        }
        
        channel.basicPublish(EXCHANGE_NAME, type , null, message.getBytes());
        
        System.out.println(" [x] Sent '" + type + "':'" + message + "'");
        
//        channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);
//        
//        channel.basicPublish( "", TASK_QUEUE_NAME, 
//                MessageProperties.PERSISTENT_TEXT_PLAIN,
//                message.getBytes());
        
//        channel.close();
//        connection.close();
    }
}
