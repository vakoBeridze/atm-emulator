package ge.atm.bankservice.config;

import ge.atm.bankservice.domain.dao.AuthMethod;
import ge.atm.bankservice.domain.dto.AuthenticationMethod;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    public static ModelMapper createInstance() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.typeMap(AuthMethod.class, AuthenticationMethod.class)
                .setConverter(mappingContext -> {
                    final AuthMethod source = mappingContext.getSource();
                    if (source == null)
                        return null;
                    return AuthenticationMethod.valueOf(source.getId());
                });

        return modelMapper;
    }

    @Bean
    public ModelMapper modelMapper() {
        return createInstance();
    }
}
