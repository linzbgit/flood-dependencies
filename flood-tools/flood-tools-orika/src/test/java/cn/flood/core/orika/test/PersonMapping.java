package cn.flood.core.orika.test;

import cn.flood.core.orika.OrikaMapperFactoryConfigurer;
import ma.glasnost.orika.MapperFactory;
import org.springframework.stereotype.Component;

/**
 * The configuration of mapping between {@link PersonSource} and {@link PersonDestination}.
 */
@Component
public class PersonMapping implements OrikaMapperFactoryConfigurer {

    /** {@inheritDoc} */
    @Override
    public void configure(MapperFactory orikaMapperFactory) {
        orikaMapperFactory.classMap(PersonSource.class, PersonDestination.class)
                .field("firstName", "givenName")
                .field("lastName", "sirName")
                .byDefault()
                .register();
    }

}
