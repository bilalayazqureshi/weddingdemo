package com.example.demo.model;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class WeddingEvent {//NOSONAR

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)

	private Long id;
	private String name;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate date;
	private String location;

	@OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
	private List<Guest> guest;

	public WeddingEvent() {

	}

	public WeddingEvent(Long id, String name, LocalDate date, String location) {
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

	public LocalDate getDate() {
		return date;
	}

	public String getLocation() {
		return location;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public void setLocation(String location) {
		this.location = location;
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

	public List<Guest> getGuest() {
		return guest;
	}

	public void setGuest(List<Guest> list) {
		this.guest = list;
	}

}