package in._10h.java.swaggerspringboot;

import reactor.core.publisher.Mono;

public interface UserRepository {
    Mono<User> getUserById(final Integer id);
    Mono<User> save(final User update);
}
