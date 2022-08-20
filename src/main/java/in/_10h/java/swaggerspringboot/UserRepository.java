package in._10h.java.swaggerspringboot;

import in._10h.java.swaggerspringboot.server.model.User;
import reactor.core.publisher.Mono;

public interface UserRepository {
    Mono<User> getUserById(final Integer id);
    Mono<User> save(final User update);
}
