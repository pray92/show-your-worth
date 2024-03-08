package kr.texturized.muus.infrastructure.repository.converter.type;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import kr.texturized.muus.domain.entity.PostTypeEnum;

/**
 * JPA Converter for PostCategory.
 */
@Converter
public class PostCategoryConverter implements AttributeConverter<PostTypeEnum, Integer> {

    @Override
    public Integer convertToDatabaseColumn(PostTypeEnum attribute) {
        return attribute.getValue();
    }

    @Override
    public PostTypeEnum convertToEntityAttribute(Integer dbData) {
        return PostTypeEnum.fromKey(dbData);
    }
}
