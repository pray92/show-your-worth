package kr.texturized.muus.infrastructure.mapper;

import java.util.List;
import java.util.Optional;

import kr.texturized.muus.domain.vo.BuskingProfileResultVo;
import kr.texturized.muus.domain.vo.BuskingSearchResultVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * DAO for Busking Selection.
 */
@Mapper
public interface BuskingMapper {

    /**
     * Get Active buskings for map.
     * Since this method is basic showing in map, it should be ready to start or already started
     *
     * @param latitude Offset latitude
     * @param longitude Offset longitude
     * @param width Range of width to show, it should be converted to latitude
     * @param height Range of height to show, it should be converted to longitude
     * @return List of Busking, just id and coordinate
     */
    List<BuskingSearchResultVo> search(
            @Param("latitude") Double latitude,
            @Param("longitude") Double longitude,
            @Param("width") Double width,
            @Param("height") Double height
    );

    /**
     * 버스킹 프로필을 조회해요.
     *
     * @param buskingId 버스킹 ID
     * @return 호스팅 유저의 정보와 버스킹 프로필 정보 Vo
     */
    Optional<BuskingProfileResultVo> profile(Long buskingId);

    /**
     * 해당 유저가 해당 버스킹을 생성했는지 확인해요.
     *
     * @param buskingId 버스킹 ID
     * @param accountId 유저 계정
     * @return 해당 유저가 버스킹을 생성했는지 여부
     */
    boolean isBuskingMadeByUser(@Param("buskingId") Long buskingId, @Param("accountId") String accountId);

    /**
     * 해당 계정이 현재 버스킹을 만들 수 있는지 확인해요.
     *
     * 조건:
     * 1. 이미 생성해서 시작 대기 상태인 버스킹이 없어야 함
     * 2. 이미 생성해서 시작 중인 버스킹이 없어야 함
     *
     * @param accountId 유저 계정
     * @return 해당 계정 버스킹 생성 가능 여부
     */
    boolean isUserEnableToMakeBusking(String accountId);
}
