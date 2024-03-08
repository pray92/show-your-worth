package kr.texturized.muus.dao;

import kr.texturized.muus.domain.entity.Busking;
import kr.texturized.muus.domain.entity.Image;
import kr.texturized.muus.domain.entity.Keyword;
import kr.texturized.muus.domain.entity.PostTypeEnum;
import kr.texturized.muus.domain.vo.BuskingCreateModelVo;
import kr.texturized.muus.domain.vo.BuskingUpdateModelVo;
import kr.texturized.muus.infrastructure.repository.BuskingRepository;
import kr.texturized.muus.infrastructure.repository.ImageRepository;
import kr.texturized.muus.infrastructure.repository.KeywordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * DAO for Busking CUD.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class BuskingDao {

    private final BuskingRepository buskingRepository;
    private final KeywordRepository keywordRepository;
    private final ImageRepository imageRepository;

    /**
     * 버스킹을 생성해요.
     *
     * @param vo 버스킹 생성에 필요한 데이터 집합을 추상화한 ModelVo
     * @return 버스킹 ID
     */
    public Long create(final BuskingCreateModelVo vo) {

        saveBusking(vo.busking());
        saveKeywords(vo.keywords(), vo.busking().getId(), vo.busking().getTitle());
        saveImages(vo.imagePaths(), vo.busking().getId(), vo.busking().getTitle());

        return vo.busking().getId();
    }


    /**
     * 버스킹 엔티티를 저장해요. 키워드나 이미지는 따로 저장해야 해요.
     *
     * @param busking 버스킹 엔티티
     */
    private void saveBusking(final Busking busking) {
        buskingRepository.save(busking);

        if (0 >= busking.getId()) {
            log.info("Busking is created({}): {}", busking.getId(), busking);
        } else {
            log.info("Busking is updated({}): {}", busking.getId(), busking);
        }
    }

    /**
     *
     * 키워드 엔티티를 저장해요. 게시물 정보는 키워드 엔티티에 있어요.
     *
     * @param keywords 키워드 엔티티 목록
     * @param postId 키워드가 속한 게시물 ID
     * @param title 게시물 제목
     */
    private void saveKeywords(
        final List<String> keywords,
        final Long postId,
        final String title
    ) {
        keywords.forEach(keyword -> {
            keywordRepository.save(Keyword.builder()
                    .postId(postId)
                    .postType(PostTypeEnum.BUSKING)
                    .keyword(keyword)
                .build());
            log.info("Keyword: {} for {} {} is added", keyword,  PostTypeEnum.BUSKING, title);
        });
    }

    /**
     *
     * 이미지 엔티티를 저장해요. 게시물 정보는 이미지 엔티티에 있어요.
     *
     * @param imagePaths 이미지 엔티티 목록
     * @param postId 이미지가 속한 게시물 ID
     * @param title 게시물 제목
     */
    private void saveImages(
            final List<String> imagePaths,
            final Long postId,
            final String title
    ) {
        for (int order = 0; order < imagePaths.size(); ++order) {
            final String imagePath = imagePaths.get(order);
            imageRepository.save(Image.builder()
                    .postId(postId)
                    .postType(PostTypeEnum.BUSKING)
                    .uploadOrder(order)
                    .path(imagePath)
                .build());

            log.info("Image No. {} for {} {} is added named by [{}]", order, PostTypeEnum.BUSKING, title, imagePath);
        }
    }

    /**
     * 버스킹 정보를 업데이트해요.
     *
     * @param vo 버스킹+변경할 버스킹 정보를 담은 Vo
     * @return 변경된 버스킹 ID
     */
    public Long update(final BuskingUpdateModelVo vo) {
        final Busking busking = buskingRepository.getById(vo.buskingId());
        busking.update(vo.latitude(), vo.longitude(), vo.title(), vo.description(), vo.managedStartTime(), vo.managedEndTime());

        saveBusking(busking);
        deleteAllBuskingKeywords(busking.getId());
        saveKeywords(vo.keywords(), busking.getId(), vo.title());

        return busking.getId();
    }

    /**
     * 해당 버스킹에 대한 정보를 모두 삭제해요.
     *
     * @param buskingId 삭제하려는 버스킹 ID
     */
    public void delete(final Long buskingId) {
        buskingRepository.deleteById(buskingId);
        deleteAllBuskingKeywords(buskingId);
        deleteAllBuskingImages(buskingId);
    }

    /**
     * 해당 버스킹의 키워드를 모두 삭제해요.
     *
     * @param buskingId 키워드를 삭제하려는 버스킹 ID
     */
    private void deleteAllBuskingKeywords(final Long buskingId) {
        keywordRepository.deleteAllInBatchByPostIdAndPostType(buskingId, PostTypeEnum.BUSKING);
    }

    /**
     * DB에 있는 해당 버스킹의 이미지 '정보'를 모두 삭제해요.<br>
     * <br>
     * 이미지의 경우 테이블에 존재하는 이미지에 대한 정보만 존재해요.
     * 실제 이미지를 없애려면 따로 처리가 필요해요.
     *
     * '24.03.08 기준으로 이에 대한 로직은 준비되어 있지 않아요.
     * 실제 이미지 삭제는 서버에서 처리하지 않고 이후 스케듈러를 통해 일정 주기로 DB에 등록되지 않은 이미지는 스토리지에 삭제하도록 구현할 예정이에요.
     *
     * @param buskingId 이미지를 삭제하려는 버스킹 ID
     */
    private void deleteAllBuskingImages(final Long buskingId) {
        imageRepository.deleteAllInBatchByPostIdAndPostType(buskingId, PostTypeEnum.BUSKING);
    }
}

