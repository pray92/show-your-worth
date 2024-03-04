package kr.texturized.muus.domain.exception;

import javax.persistence.EntityNotFoundException;

public class BuskingProfileNotFoundException extends EntityNotFoundException {
    public BuskingProfileNotFoundException(Long target) {
        super("Busking profile with table ID [" + target + "] is not found");
    }
}
