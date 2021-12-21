package com.volk.model;

import lombok.*;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class CustomMessage {
    private String message;
    private Date date;
}
