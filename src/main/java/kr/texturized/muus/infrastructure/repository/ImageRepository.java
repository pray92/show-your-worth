package kr.texturized.muus.infrastructure.repository;

import kr.texturized.muus.domain.entity.Image;
import kr.texturized.muus.domain.entity.PostTypeEnum;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * JPA Repository for Image.
 */
public interface ImageRepository extends JpaRepository<Image, Long> {

    void deleteAllInBatchByPostIdAndPostType(Long postId, PostTypeEnum postType);
}
