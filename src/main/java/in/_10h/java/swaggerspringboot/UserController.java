package in._10h.java.swaggerspringboot;

import io.swagger.models.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RestController
public class UserController implements UserApi, UsersApi {

    private final UserRepository userRepository;

    public UserController(

            final UserRepository userRepository

    ) {

        this.userRepository = userRepository;

    }

    @Override
    public Mono<ResponseEntity<User>> postUser(
            final Mono<UserDraft> userDraft,
            final ServerWebExchange exchange
    ) {

        return userDraft
                .map(
                        draft -> new User()
                                .firstName(draft.getFirstName())
                                .lastName(draft.getLastName())
                                .email(draft.getEmail())
                )
                .flatMap(this.userRepository::save)
                .map(
                        user -> ResponseEntity
                                .created(
                                        exchange.getRequest()
                                                .getURI()
                                                .resolve("../users/" + user.getId())
                                )
                                .body(user)
                )
                .onErrorReturn(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());

    }

    @Override
    public Mono<ResponseEntity<User>> getUsersUserId(
            final Integer userId,
            final ServerWebExchange exchange
    ) {

        return this.userRepository.getUserById(userId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());

    }

    @Override
    public Mono<ResponseEntity<User>> patchUsersUserId(
            final Integer userId,
            final Mono<UserPatch> userPatch,
            final ServerWebExchange exchange
    ) {

        return this.userRepository.getUserById(userId)
                .zipWith(userPatch)
                .map(tuple -> {
                    final User entity = tuple.getT1();
                    final UserPatch patch = tuple.getT2();
                    if (patch.getFirstName() != null) {
                        entity.setFirstName(patch.getFirstName());
                    }
                    if (patch.getLastName() != null) {
                        entity.setLastName(patch.getLastName());
                    }
                    if (patch.getEmail() != null) {
                        entity.setEmail(patch.getEmail());
                    }
                    return entity;
                })
                .flatMap(this.userRepository::save)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build())
                .onErrorReturn(ResponseEntity.status(HttpStatus.CONFLICT).build());

    }

}
