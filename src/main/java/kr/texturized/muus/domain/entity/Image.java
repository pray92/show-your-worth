package kr.texturized.muus.domain.entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import kr.texturized.muus.infrastructure.repository.converter.type.PostCategoryConverter;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Entity for image.
 */
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long postId;

    @NotNull
    @Convert(converter = PostCategoryConverter.class)
    @Column(nullable = false, updatable = false)
    private PostTypeEnum postType;

    @NotNull
    private Integer uploadOrder;

    @NotBlank
    @Column(length = 250)
    private String path;       // Relative image path

    @Builder
    public Image(
        final Long postId,
        final PostTypeEnum postType,
        final Integer uploadOrder,
        final String path
    ) {
        this.postId = postId;
        this.postType = postType;
        this.uploadOrder = uploadOrder;
        this.path = path;
    }
}
