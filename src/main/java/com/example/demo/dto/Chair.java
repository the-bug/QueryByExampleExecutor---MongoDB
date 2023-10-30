package com.example.demo.dto;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public class Chair {

	private String color;

	private Long countOfLegs;

}
