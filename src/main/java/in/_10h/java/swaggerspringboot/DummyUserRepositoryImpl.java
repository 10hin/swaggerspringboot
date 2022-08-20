package in._10h.java.swaggerspringboot;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
@ConditionalOnProperty(
        name = "repository.type",
        havingValue = "dummy"
)
public class DummyUserRepositoryImpl implements UserRepository {
    private static final Map<Integer, User> repository = Map.of(
            1, new User()
                    .id(1)
                    .firstName("John")
                    .lastName("Smith")
                    .email("john.smith@dummy.example.com"),
            2, new User()
                    .id(2)
                    .firstName("Johanna")
                    .lastName("Smith")
                    .email("johanna.smith@dummy.example.com")
    );
    @Override
    public Mono<User> getUserById(Integer id) {
        return Mono.justOrEmpty(DummyUserRepositoryImpl.repository.get(id))
                .map(
                        original -> new User()
                                .id(original.getId())
                                .firstName(original.getFirstName())
                                .lastName(original.getLastName())
                                .email(original.getEmail())
                );
    }

    @Override
    public Mono<User> save(User update) {
        if (DummyUserRepositoryImpl.repository.containsKey(update.getId())) {
            return Mono.just(DummyUserRepositoryImpl.repository.get(update.getId()));
        }
        return Mono.just(DummyUserRepositoryImpl.repository.get(1));
    }

}
