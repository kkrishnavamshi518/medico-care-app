package com.doctor.appointment.service;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import com.doctor.appointment.entities.Patient;
import com.doctor.appointment.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final PatientRepository patientRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Patient patient = patientRepository.findById(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return org.springframework.security.core.userdetails.User
                .withUsername(patient.getEmail())
                .password(patient.getPassword())
                .roles("PATIENT")
                .build();
    }
}