package kr.texturized.muus.domain.entity;

import java.util.Arrays;
import kr.texturized.muus.domain.exception.PostCategoryNotFoundException;
import lombok.Getter;

/**
 * PostCategory.
 */
@Getter
public enum PostTypeEnum {

    BUSKING(1),
    FEED(2),
    NOTICE(3)

    ;

    private final int value;

    PostTypeEnum(final int value) {
        this.value = value;
    }

    public static PostTypeEnum fromKey(String key) {
        int keyValue = Integer.parseInt(key);
        return fromKey(keyValue);
    }

    /**
     * Integer value to enum instance.
     *
     * @param key index in database
     * @return enum class mapping with key
     */
    public static PostTypeEnum fromKey(int key) {
        return Arrays.stream(values())
            .filter(type -> type.value == key)
            .findAny()
            .orElseThrow(() -> new PostCategoryNotFoundException(key));
    }

}
