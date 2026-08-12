package com.tns.employeeapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Schema(description = "Employee Response DTO")
public class EmployeeResponse {

    @Schema(example = "1")
    private Integer id;

    @Schema(example = "Satya Samantray")
    private String name;

    @Schema(example = "satya@gmail.com")
    private String email;

    @Schema(example = "IT")
    private String department;

    @Schema(example = "55000")
    private Double salary;
}