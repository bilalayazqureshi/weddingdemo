package com.example.demo.model;

import java.util.Objects;
import java.util.List;

public class WeddingEvent {

	private Long id;
	private String name;
	private String date;
	private String location;
	private List<Guest> guests;

	public WeddingEvent(Long id, String name, String date, String location) {
		this.id = id;
		this.name = name;
		this.date = date;
		this.location = location;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getDate() {
		return date;
	}

	public String getLocation() {
		return location;
	}

	public List<Guest> getGuests() {
		return guests;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public void setGuests(List<Guest> guests) {
		this.guests = guests;
	}

	@Override
	public String toString() {
		return "WeddingEvent [id=" + id + ", name=" + name + ", date=" + date + ", location=" + location + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name, date, location);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WeddingEvent other = (WeddingEvent) obj;
		return Objects.equals(id, other.id) && Objects.equals(name, other.name) && Objects.equals(date, other.date)
				&& Objects.equals(location, other.location);
	}
}