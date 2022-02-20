package com.ondeck.crm.vip.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateVipRequest {
    private String name;
    private String email;
}
