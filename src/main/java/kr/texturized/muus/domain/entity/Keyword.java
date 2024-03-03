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
 * Entity class for keyword.
 */
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Keyword {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "post_id")
    private Long postId;

    @NotNull
    @Convert(converter = PostCategoryConverter.class)
    @Column(nullable = false, updatable = false)
    private PostCategoryEnum postType;

    @NotBlank
    @Column(name = "keyword", length = 15)
    private String keyword;

    @Builder
    public Keyword(
        final Long postId,
        final PostCategoryEnum postType,
        final String keyword) {
        this.postId = postId;
        this.postType = postType;
        this.keyword = keyword;
    }
}
