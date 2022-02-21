package com.ondeck.crm.vip.domain.repository;

import com.ondeck.crm.vip.domain.entity.InvitationStatus;
import com.ondeck.crm.vip.domain.entity.VipRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface VipRecordRepository extends JpaRepository<VipRecord, UUID> {

    VipRecord findVipRecordByEmail(String email);

    VipRecord findVipRecordByVipRecordId(UUID vipRecordId);

    List<VipRecord> findAllByInvitationStatus(InvitationStatus invitationStatus);

}
