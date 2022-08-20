package in._10h.java.swaggerspringboot;

import in._10h.java.swaggerspringboot.server.model.User;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@ConditionalOnProperty(
        name = "repository.type",
        havingValue = "mem",
        matchIfMissing = true
)
public class MemoryUserRepositoryImpl implements UserRepository {
    private static final ConcurrentMap<Integer, User> repository = new ConcurrentHashMap<>();
    private static final AtomicInteger serial = new AtomicInteger(1);
    @Override
    public Mono<User> getUserById(Integer id) {
        return Mono.justOrEmpty(MemoryUserRepositoryImpl.repository.get(id))
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
        if (update.getId() == null) {
            return this.create(update);
        }
        return this.update(update);
    }

    private Mono<User> create(final User draft) {
        final Integer newId = MemoryUserRepositoryImpl.serial.getAndIncrement();
        final User entity = new User()
                .id(newId)
                .firstName(draft.getFirstName())
                .lastName(draft.getLastName())
                .email(draft.getEmail());
        MemoryUserRepositoryImpl.repository.put(newId, entity);
        return Mono.just(entity);
    }

    private Mono<User> update(final User update) {
        return Mono.justOrEmpty(
                MemoryUserRepositoryImpl.repository.computeIfPresent(
                        update.getId(),
                        (key, entity) -> new User()
                                    .id(entity.getId())
                                    .firstName(update.getFirstName())
                                    .lastName(update.getLastName())
                                    .email(update.getLastName())
                )
        );
    }
}
