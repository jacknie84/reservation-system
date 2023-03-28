package com.jacknie.reservation.jpa.inprogress

import com.jacknie.reservation.jpa.Reservation
import jakarta.persistence.*
import org.hibernate.Hibernate
import org.springframework.boot.context.properties.bind.DefaultValue
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.Instant

@Entity
@EntityListeners(value = [AuditingEntityListener::class])
data class ReservationInProgress(

    @Id
    var facilityId: Long,

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "ReservationInProgressGroup",
        joinColumns = [JoinColumn(name = "facilityId", referencedColumnName = "facilityId", nullable = false)],
        inverseJoinColumns = [JoinColumn(name = "reservationId", referencedColumnName = "id", nullable = false)],
        uniqueConstraints = [UniqueConstraint(columnNames = ["facilityId", "reservationId"])]
    )
    var reservations: MutableList<Reservation>,

    @CreatedDate
    @DefaultValue("current_timestamp")
    @Column(nullable = false, updatable = false)
    var createdDate: Instant = Instant.now(),

) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as ReservationInProgress

        return facilityId != null && facilityId == other.facilityId
    }

    override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(facilityId = $facilityId )"
    }
}
