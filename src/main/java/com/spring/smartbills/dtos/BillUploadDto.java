package com.spring.smartbills.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;


@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class BillUploadDto {
    public String title;
    public String category;

    @JsonFormat()
    public LocalDate duedate;
}