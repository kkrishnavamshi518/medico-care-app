package com.doctor.appointment.controller;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.doctor.appointment.entities.Appointment;
import com.doctor.appointment.entities.Doctor;
import com.doctor.appointment.entities.Patient;
import com.doctor.appointment.service.AppointmentService;
import com.doctor.appointment.service.DoctorService;
import com.doctor.appointment.service.PatientService;
@Controller
@RequiredArgsConstructor
public class PatientController {
    private final DoctorService doctorService;
    private final PatientService patientService;
    private final AppointmentService appointmentService;
    // ---------------- LOGIN PAGE ----------------
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }
    // ---------------- REGISTER PAGE ----------------
    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }
    // ---------------- REGISTER SAVE ----------------
    @PostMapping("/register")
    public String register(@ModelAttribute Patient patient) {
        if (patientService.existsByEmail(patient.getEmail())) {
            return "registration-error";
        }
        patientService.registerPatient(patient);
        return "registration-success";
    }
    // ---------------- PATIENT DASHBOARD ----------------
    @GetMapping("/patient/home")
    public String dashboard(Model model) {
        List<Doctor> doctors = doctorService.getAllDoctors();
        model.addAttribute("doctors", doctors);
        return "patient-dashboard";
    }
    // ---------------- BOOK APPOINTMENT PAGE ----------------
    @GetMapping("/patient/book")
    public String bookAppointment(@RequestParam String name, @RequestParam String specialization, @RequestParam String email, Model model) {
        model.addAttribute("doctorName", name);
        model.addAttribute("doctorSpecialization", specialization);
        model.addAttribute("doctorEmail", email);
        return "book-appointment";
    }
    // ---------------- SAVE APPOINTMENT ----------------
    @PostMapping("/patient/book")
    public String saveAppointment(@RequestParam String doctorName, @RequestParam String doctorSpecialization,
            @RequestParam String doctorEmail, @RequestParam String date,
            @RequestParam String time, @RequestParam String problem, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String patientEmail = auth.getName();
        String validationResult = appointmentService.validateAppointment(doctorEmail, date, time);
        if (!validationResult.equals("VALID")) {
            model.addAttribute("doctorName", doctorName);
            model.addAttribute("doctorSpecialization", doctorSpecialization);
            model.addAttribute("doctorEmail", doctorEmail);
            switch (validationResult) {
                case "PAST_DATE":
                    model.addAttribute("error", "You cannot book appointments for past dates.");
                    return "book-appointment";
                case "PAST_TIME_TODAY":
                    model.addAttribute("error", "You cannot book past time slots for today.");
                    return "book-appointment";
                case "BOOKING_CLOSED_TODAY":
                    model.addAttribute("error", "Booking for today is closed (After 4 PM).");
                    return "book-appointment";
                case "INVALID_TIME":
                    model.addAttribute("error", "Appointments allowed between 10 AM and 4 PM only.");
                    return "book-appointment";
                case "SLOT_NOT_AVAILABLE":
                    model.addAttribute("doctorName", doctorName);
                    model.addAttribute("date", date);
                    model.addAttribute("time", time);
                    return "appointment_not_available";
            }
        }
        Appointment appt = new Appointment();
        appt.setDoctorName(doctorName);
        appt.setDoctorSpecialization(doctorSpecialization);
        appt.setDoctorEmail(doctorEmail);
        appt.setPatientEmail(patientEmail);
        appt.setDate(date);
        appt.setTime(time);
        appt.setProblem(problem);
        appt.setStatus("Slot Booked Successfully");
        appointmentService.save(appt);
        model.addAttribute("appointment", appt);
        return "appointment_success";
    }
    // ---------------- APPOINTMENT HISTORY ----------------
    @GetMapping("/patient/history")
    public String history(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        model.addAttribute("appointments", appointmentService.getHistory(email));
        return "booking-history";
    }
    // ---------------- CANCEL APPOINTMENT ----------------
    @PostMapping("/patient/cancel")
    public String cancel(@RequestParam Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        appointmentService.cancelByPatient(id, email);
        return "redirect:/patient/history";
    }
    @GetMapping("/logout-success")
    public String logoutSuccess() {
        return "logout-success";
    }
}