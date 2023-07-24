package net.cvergara.order.service.config;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    //Spring bean for queue order queue
    @Value("${rabbitmq.queue.order.name}")
    private String orderQueue;

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.binding.routing.key}")
    private String orderRoutingKey;

   @Bean
   public Queue orderQueue(){
       return new Queue(orderQueue);
    }

    //Spring bean for exchange
    @Bean
    public TopicExchange exchange(){
       return new TopicExchange(exchange);
    }
    //Spring bean for binding between exhange and queue using routing key
    @Bean
    public Binding binding(){
       return BindingBuilder
               .bind(orderQueue())
               .to(exchange())
               .with(orderRoutingKey);
    }

    //Message Converter
     @Bean
     public MessageConverter converter(){
       return new Jackson2JsonMessageConverter();
    }

    //Configure Rabbit Template

    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory){

        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter());
        return rabbitTemplate;
    }


}
