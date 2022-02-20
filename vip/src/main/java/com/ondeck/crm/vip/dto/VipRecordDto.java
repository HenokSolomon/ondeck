package com.ondeck.crm.vip.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VipRecordDto {

    private String name;
    private String email;
    private String createdBy;
    private LocalDateTime createdDate;

}
