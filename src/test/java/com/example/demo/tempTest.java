package com.example.demo;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import com.example.demo.model.WeddingEvent;

class tempTest {

	@Test
 	public void test() {
 		assertNotNull(new WeddingEvent(1L, "Bilal's event","1-1-25", "florence"));
 	}
}
