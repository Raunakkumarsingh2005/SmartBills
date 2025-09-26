package com.spring.smartbills.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;


@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class BillUploadDto {
    @NotBlank(message = "title is mandatory")
    public String title;

    @NotBlank(message = "category is mandatory")
    public String category;

    @JsonFormat()
    public LocalDate duedate;
}