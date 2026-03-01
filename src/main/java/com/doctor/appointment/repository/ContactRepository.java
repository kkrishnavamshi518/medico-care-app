package com.doctor.appointment.repository;
import com.doctor.appointment.entities.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ContactRepository extends JpaRepository<Contact, Long> {
}