package com.jacknie.reservation.facade.support

import com.jacknie.reservation.facade.ContractFacade
import org.springframework.stereotype.Component
import java.time.Duration

@Component
class ContractFacadeImpl: ContractFacade {

    override fun getUsageLimit(contractId: Long): Duration {
        return Duration.ofHours(120)
    }
}