package by.lobacevich.config;

import by.lobacevich.mapper.PaymentCardMapper;
import by.lobacevich.mapper.PaymentCardMapperImpl;
import by.lobacevich.mapper.UserMapper;
import by.lobacevich.mapper.UserMapperImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringConfig {

    @Bean
    UserMapper userMapper() {
        return new UserMapperImpl();
    }

    @Bean
    PaymentCardMapper paymentCardMapper() {
        return new PaymentCardMapperImpl();
    }
}
