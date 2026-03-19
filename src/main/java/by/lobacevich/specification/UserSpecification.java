package by.lobacevich.specification;

import by.lobacevich.entity.User;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecification {

    public static final String NAME = "name";
    public static final String SURNAME = "surname";

    private UserSpecification() {
    }

    private static Specification<User> hasFirstName(String firstName) {
        return (root, query, cb) -> {
            if (firstName == null || firstName.isEmpty()) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get(NAME)),
                    "%" + firstName.toLowerCase() + "%");
        };
    }

    private static Specification<User> hasSurname(String surname) {
        return (root, query, cb) -> {
            if (surname == null || surname.isEmpty()) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get(SURNAME)),
                    "%" + surname.toLowerCase() + "%");
        };
    }

    public static Specification<User> filterBy(String firstName, String surname) {
        return hasFirstName(firstName)
                .and(hasSurname(surname));
    }

}
