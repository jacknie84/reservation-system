package com.jacknie.reservation.jpa

import jakarta.persistence.*
import org.hibernate.Hibernate
import org.springframework.boot.context.properties.bind.DefaultValue
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.Instant

@Entity
@EntityListeners(value = [AuditingEntityListener::class])
data class Reservation(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    var summary: String? = null,

    @Column(name = "startDate", nullable = false)
    var start: Instant,

    @Column(name = "endDate", nullable = false)
    var end: Instant,

    @Column(nullable = false)
    var contractId: Long,

    @Column(nullable = false)
    val facilityId: Long,

    @Embedded
    @AttributeOverrides(value = [
        AttributeOverride(name = "code", column = Column(name = "status", nullable = false)),
        AttributeOverride(name = "date", column = Column(name = "statusDate", nullable = false))
    ])
    var status: Status,

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
        name = "ReservationIncompleteReason",
        joinColumns = [JoinColumn(name = "reservationId", referencedColumnName = "id", nullable = false)]
    )
    @Enumerated(EnumType.STRING)
    var incompleteReasons: MutableSet<IncompleteReason> = mutableSetOf(),

    @CreatedDate
    @DefaultValue("current_timestamp")
    @Column(nullable = false, updatable = false)
    var createdDate: Instant = Instant.now()
) {

    @Embeddable
    data class Status(

        @Enumerated(EnumType.STRING)
        var code: Code,

        var date: Instant = Instant.now()

    ) {
        enum class Code {
            /**
             * 예약이 처음 등록 되어 접수 된 상태
             */
            ACCEPTED,

            /**
             * 접수 등록 된 예약을 승인 하기 위한 작업들을 처리 중인 상태
             */
            IN_PROGRESS,

            /**
             * 모든 사전 작업을 완료하고 유효한 예약으로 승인 된 상태
             */
            RESERVED,

            /**
             * 예약이 유효하지 않은 상태
             */
            INCOMPLETE,

            /**
             * 예약을 취소 한 상태
             */
            CANCELLED,

            /**
             * 승인 된 예약을 수정/취소 할 수 없는 상태
             */
            CONFIRMED,

            /**
             * 예정된 예약 시간을 사용 완료 한 상태
             */
            COMPLETE
        }
    }

    enum class IncompleteReason {

        /**
         * 요청 한 시간에 이미 예약이 존재
         */
        ALREADY_EXISTS,

        /**
         * 계약에서 정한 사용 가능 시간을 초과
         */
        EXCEEDED_USAGE_LIMIT
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as Reservation

        return id != null && id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id )"
    }
}
