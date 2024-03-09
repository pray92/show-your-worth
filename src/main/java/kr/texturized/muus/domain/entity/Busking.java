package kr.texturized.muus.domain.entity;

import java.time.LocalDateTime;
import javax.persistence.*;
import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.PastOrPresent;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

/**
 * Entity for Busking.
 */
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Busking {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "host_id", nullable = false, updatable = false)
    private User host;

    private Double latitude;
    private Double longitude;

    @Column(nullable = false, length = 20)
    private String title;

    @Lob
    @Column(nullable = false)
    private String description;

    @FutureOrPresent
    @Column(nullable = false)
    private LocalDateTime managedStartTime;

    @Future
    @Column(nullable = false)
    private LocalDateTime managedEndTime;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createTime;

    @PastOrPresent
    private LocalDateTime endTime;

    /**
     * Constructor.
     */
    @Builder
    public Busking(
        final User host,
        final Double latitude,
        final Double longitude,
        final String title,
        final String description,
        final LocalDateTime managedStartTime,
        final LocalDateTime managedEndTime,
        final LocalDateTime endTime
    ) {
        this.host = host;
        this.latitude = latitude;
        this.longitude = longitude;
        this.title = title;
        this.description = description;
        this.managedStartTime = managedStartTime;
        this.managedEndTime = managedEndTime;
        this.endTime = endTime;
    }

    public void update(
        final Double latitude,
        final Double longitude,
        final String title,
        final String description,
        final LocalDateTime managedStartTime,
        final LocalDateTime managedEndTime
    ) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.title = title;
        this.description = description;
        this.managedStartTime = managedStartTime;
        this.managedEndTime = managedEndTime;
    }

    public void startNow() {
        // now() 사용 시 초기화 하는 과저에서 시간이 지나 '과거'가 됨
        // 플러시 되는 텀을 고려해 1초 추가
        this.managedStartTime = LocalDateTime.now().plusSeconds(1L);
    }

    public void endNow() {
        this.endTime = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return String.format("Busking(host=%s, title=%s, createTime=%s",
            host,
            title,
            createTime
        );
    }
}
