package kr.texturized.muus.presentation.api;

import java.util.List;
import javax.validation.Valid;

import kr.texturized.muus.application.UserSignFacade;
import kr.texturized.muus.application.service.BuskingService;
import kr.texturized.muus.common.coordinate.RangeChecker;
import kr.texturized.muus.common.util.SignInCheck;
import kr.texturized.muus.domain.entity.UserTypeEnum;
import kr.texturized.muus.domain.vo.*;
import kr.texturized.muus.presentation.api.request.BuskingCreateRequest;
import kr.texturized.muus.presentation.api.request.BuskingSearchRequest;
import kr.texturized.muus.presentation.api.request.BuskingUpdateRequest;
import kr.texturized.muus.presentation.api.response.BuskingProfileResponse;
import kr.texturized.muus.presentation.api.response.BuskingSearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/buskings")
@RequiredArgsConstructor
public class BuskingController {

    private final RangeChecker rangeChecker;
    private final BuskingService buskingService;

    private final UserSignFacade userSignFacade;

    /**
     * Create Busking Post.<br>
     * <br>
     * There are several reasons why imageFiles are seperated with dto.<br>
     * 1. Normally, client has the responsibility for uploading image to storage using ajax.
     * In this case, I need experience for uploading images in server. So I made logics for it.
     * It may be deprecated when some webpages are made. So I split for this changes.<br>
     * <br>
     * 2. There is an another way to upload, base64 encoding. But it's ineffective way because
     * encoded images strings are too long, so it's hard to request.<br>
     * <br>
     * Reference: <a href="https://www.inflearn.com/questions/307133/image-%EC%A0%84%EC%86%A1%EA%B3%BC-%ED%95%A8%EA%BB%98-%EB%8D%B0%EC%9D%B4%ED%84%B0%EB%8A%94-json%EC%9C%BC%EB%A1%9C-%EB%B3%B4%EB%82%B4%EA%B3%A0-%EC%8B%B6%EC%9D%80-%EA%B2%BD%EC%9A%B0">image 전송과 함께 데이터는 json으로 보내고 싶은 경우</a>
     *
     * @param request DTO for busking post information
     * @param imageFiles images for post
     * @return Created busking ID in DB
     */
    @PostMapping()
    @SignInCheck(userType = {UserTypeEnum.USER, UserTypeEnum.ADMIN})
    public ResponseEntity<Long> create(
        @Valid @RequestPart final BuskingCreateRequest request,
        @RequestPart("images") final MultipartFile[] imageFiles
    ) {
        final String accountId = userSignFacade.getCurrentAccountId();

        rangeChecker.validateRange(request.latitude(), request.longitude(), 0.0, 0.0);

        final BuskingCreateVo vo = BuskingCreateVo.of(accountId, request, imageFiles);
        final Long buskingId = buskingService.create(vo);

        return ResponseEntity.status(HttpStatus.CREATED).body(buskingId);
    }

    /**
     * 지도에서 특정 범위의 활동 예정 및 진행 중인 버스킹을 조회.
     *
     * @param request 특정 위치 및 범위에 대한 요청
     * @return 활동 예정 및 진행 중인 버스킹 목록
     */
    @GetMapping
    public ResponseEntity<BuskingSearchResponse> search(@RequestBody final BuskingSearchRequest request) {
        rangeChecker.validateRange(request.latitude(), request.longitude(), request.widthMeter(), request.heightMeter());

        final BuskingSearchVo vo = BuskingSearchVo.of(request);
        final List<BuskingSearchResultVo> resultVo = buskingService.search(vo);

        return ResponseEntity.status(HttpStatus.OK).body(BuskingSearchResponse.of(resultVo));
    }

    /**
     * 버스킹 프로필 조회
     *
     * @param buskingId 프로필 조회할 버스킹 ID
     * @return 버스킹 프로필 Vo
     */
    @GetMapping("/{buskingId}")
    public ResponseEntity<BuskingProfileResponse> profile(@PathVariable final Long buskingId) {
        final BuskingProfileResultVo resultVo = buskingService.profile(buskingId);

        return ResponseEntity.status(HttpStatus.OK).body(BuskingProfileResponse.of(resultVo));
    }

    /**
     * 버스킹 업데이트
     *
     * @param buskingId 업데이트할 버스킹 ID
     * @param request 버스킹 업데이트 요청 파라미터
     * @return 업데이트된 버스킹 ID
     */
    @PutMapping("/{buskingId}")
    @SignInCheck(userType = {UserTypeEnum.USER, UserTypeEnum.ADMIN})
    public ResponseEntity<Long> update(
            @PathVariable final Long buskingId,
            @RequestBody final BuskingUpdateRequest request
    ) {
        final String accountId = userSignFacade.getCurrentAccountId();

        rangeChecker.validateRange(request.latitude(), request.longitude(), 0.0, 0.0);
        buskingService.validateBuskingMadeByUser(buskingId, accountId);

        final Long updatedBuskingId = buskingService.update(BuskingUpdateVo.of(buskingId, request));

        return ResponseEntity.status(HttpStatus.OK).body(updatedBuskingId);
    }
}
