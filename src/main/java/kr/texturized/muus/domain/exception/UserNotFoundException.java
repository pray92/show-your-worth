package kr.texturized.muus.domain.exception;

import javax.persistence.EntityNotFoundException;

public class UserNotFoundException extends EntityNotFoundException {
    public UserNotFoundException(Long target) {
        super("User with table ID [" + target + "] is not found");
    }

}
