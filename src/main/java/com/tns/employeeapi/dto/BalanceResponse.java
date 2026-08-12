package com.tns.employeeapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Schema(description = "Account Balance Response DTO")
public class BalanceResponse {

    @Schema(example = "1")
    private Integer accountId;

    @Schema(example = "ACC1001")
    private String accountNumber;

    @Schema(example = "Satya Samantray")
    private String accountHolderName;

    @Schema(example = "25000.00")
    private Double balance;
}