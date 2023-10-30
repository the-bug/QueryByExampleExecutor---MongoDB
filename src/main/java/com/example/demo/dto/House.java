package com.example.demo.dto;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document
@Builder
@Jacksonized
public class House {

	private  List<Room> rooms;
	private String name;

}
