package com.ondeck.crm.vip.domain.repository;

import com.ondeck.crm.vip.domain.entity.VipRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface VipRecordRepository extends JpaRepository<VipRecord, UUID> {

    VipRecord findVipRecordByEmail(String email);

    VipRecord findVipRecordByVipRecordId(UUID vipRecordId);

}
