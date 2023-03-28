package com.jacknie.reservation.facade

import java.time.Duration

interface ContractFacade {

    fun getUsageLimit(contractId: Long): Duration
}