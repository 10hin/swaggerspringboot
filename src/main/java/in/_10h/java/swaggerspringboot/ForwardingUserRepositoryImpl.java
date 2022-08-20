package in._10h.java.swaggerspringboot;

import in._10h.java.swaggerspringboot.client.api.DefaultApi;
import in._10h.java.swaggerspringboot.server.model.User;
import in._10h.java.swaggerspringboot.server.model.UserDraft;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@ConditionalOnProperty(
        name = "repository.type",
        havingValue = "forward"
)
public class ForwardingUserRepositoryImpl implements UserRepository {

    private final DefaultApi defaultApi;

    public ForwardingUserRepositoryImpl(
            @Autowired
            final DefaultApi defaultApi
    ) {
        this.defaultApi = defaultApi;
    }

    @Override
    public Mono<User> getUserById(final Integer id) {
        return this.defaultApi.getUsersUserId(id)
                .map(ForwardingUserRepositoryImpl::clientModelToServerModel);
    }

    @Override
    public Mono<User> save(final User update) {
        if (update.getId() == null) {
            return create(update);
        }
        return update(update);
    }

    private Mono<User> create(final User draft) {

        final in._10h.java.swaggerspringboot.client.model.UserDraft draftRequest = new in._10h.java.swaggerspringboot.client.model.UserDraft()
                .firstName(draft.getFirstName())
                .lastName(draft.getLastName())
                .email((draft.getEmail()));

        return this.defaultApi.postUser(draftRequest)
                .map(ForwardingUserRepositoryImpl::clientModelToServerModel);

    }

    private Mono<User> update(final User update) {

        final in._10h.java.swaggerspringboot.client.model.UserPatch patchRequest = new in._10h.java.swaggerspringboot.client.model.UserPatch()
                .firstName(update.getFirstName())
                .lastName(update.getLastName())
                .email(update.getEmail());

        return this.defaultApi.patchUsersUserId(update.getId(), patchRequest)
                .map(ForwardingUserRepositoryImpl::clientModelToServerModel);

    }

    private static User clientModelToServerModel(final in._10h.java.swaggerspringboot.client.model.User user) {

        return new User()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail());

    }

}
