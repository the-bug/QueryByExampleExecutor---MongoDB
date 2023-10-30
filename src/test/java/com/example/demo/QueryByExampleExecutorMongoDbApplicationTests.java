package com.example.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.demo.dto.Chair;
import com.example.demo.dto.House;
import com.example.demo.dto.Room;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.List;

@SpringBootTest
@RequiredArgsConstructor
@Testcontainers
@ActiveProfiles(profiles = "local")
class QueryByExampleExecutorMongoDbApplicationTests {

	@Container
	@ServiceConnection
	static MongoDBContainer mongoDbContainer = getMongoDBContainer();

	@NotNull
	private static MongoDBContainer getMongoDBContainer() {
		final MongoDBContainer mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo:5.0.20"));
		mongoDBContainer.setPortBindings(List.of("27017:27017"));
		return mongoDBContainer;
	}

	@Autowired
	private MyRepository repository;

	private static House getHouse1() {
		final List<Chair> hr1c = List.of( //
				Chair.builder().color("Green") //
						.countOfLegs(4L) //
						.build(), //
				Chair.builder().color("Blue") //
						.countOfLegs(4L) //
						.build() //
		);


		final List<Chair> hr2c = List.of( //
				Chair.builder().color("Green") //
						.countOfLegs(4L) //
						.build(), //
				Chair.builder().color("Blue") //
						.countOfLegs(3L) //
						.build(), //
				Chair.builder().color("Blue") //
						.countOfLegs(4L) //
						.build() //
		);
		return House.builder() //
				.name("house1").rooms( //
						List.of( //
								Room.builder() //
										.chairs(hr1c) //
										.build(), // //
								Room.builder() //
										.chairs(hr2c) //
										.build() //
								//
						)).build();
	}

	private static House getHouse2() {
		final List<Chair> hr1c = List.of( //
				Chair.builder().color("Green") //
						.countOfLegs(4L) //
						.build(), //
				Chair.builder().color("Purple") //
						.countOfLegs(4L) //
						.build() //
		);


		final List<Chair> hr2c = List.of( //
				Chair.builder().color("Yellow") //
						.countOfLegs(4L) //
						.build(), //
				Chair.builder().color("Blue") //
						.countOfLegs(5L) //
						.build(), //
				Chair.builder().color("Blue") //
						.countOfLegs(5L) //
						.build() //
		);
		return House.builder()//
				.name("house2").rooms( //
						List.of( //
								Room.builder() //
										.chairs(hr1c) //
										.build(), // //
								Room.builder() //
										.chairs(hr2c) //
										.build() //
								//
						)).build();
	}

	@Test
	@SneakyThrows
	void contextLoads() {

		final House house1hasChairWith3Legs = getHouse1();
		final House house2hasChairWith5Legs = getHouse2();
		final List<House> testData = List.of(house1hasChairWith3Legs, house2hasChairWith4Legs);
		repository.insert(testData);

		final ExampleMatcher matcher = ExampleMatcher.matchingAll().withIgnoreNullValues();

		final House probe = House.builder()
				.rooms(List.of(Room.builder().chairs(List.of(Chair.builder().countOfLegs(3L).build())).build()))
				.build();
		final Example<House> example = Example.of(probe, matcher);

		final List<House> result = repository.findAll(example); // empty result
		assertEquals(1, result.size());
	}

}
