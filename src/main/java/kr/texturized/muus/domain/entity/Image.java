package kr.texturized.muus.domain.entity;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;
import kr.texturized.muus.domain.entity.fk.ImageFk;
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

    @EmbeddedId
    private ImageFk id;

    @NotBlank
    @Column(length = 250)
    private String path;       // Relative image path


    @Builder
    public Image(
        final ImageFk id,
        final String path
    ) {
        this.id = id;
        this.path = path;
    }
}
