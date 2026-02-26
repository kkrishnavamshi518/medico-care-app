package com.doctor.appointment.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.doctor.appointment.entities.Patient;
import com.doctor.appointment.repository.PatientRepository;
@Service
public class PatientService {
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    public void registerPatient(Patient patient) {
        patient.setPassword(passwordEncoder.encode(patient.getPassword()));
        patientRepository.save(patient);
    }
    public boolean existsByEmail(String email) {
        return patientRepository.existsById(email);
    }
    public boolean validateLogin(String email, String password) {
        Patient patient = patientRepository.findById(email).orElse(null);
        return patient != null && patient.getPassword().equals(password);
    }
}