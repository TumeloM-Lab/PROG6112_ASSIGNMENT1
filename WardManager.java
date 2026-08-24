/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.prog6112_assignment1;

/**
 *
 * @author Tumelo Mahape st10464247
 */
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class WardManager {
    private List<Patient> patients = new ArrayList<>();
    private String[][] beds = new String[4][5];
    private static final String WARD_NAME = "Ward 1";

    public WardManager() {
        int bedCount = 1;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 5; j++) {
                beds[i][j] = String.format("B%02d", bedCount++);
            }
        }
    }

    public boolean registerPatient(Patient patient) {
        if (searchPatientById(patient.getPatientId()) != null) {
            return false;
        }
        patients.add(patient);
        return true;
    }

    public Patient searchPatientById(String patientId) {
        for (Patient p : patients) {
            if (p.getPatientId().equalsIgnoreCase(patientId)) {
                return p;
            }
        }
        return null;
    }

    public boolean updatePatient(String patientId, String firstName, String lastName, int age, String gender, String medicalCondition) {
        Patient p = searchPatientById(patientId);
        if (p != null) {
            p.setFirstName(firstName);
            p.setLastName(lastName);
            p.setAge(age);
            p.setGender(gender);
            p.setMedicalCondition(medicalCondition);
            return true;
        }
        return false;
    }

    public boolean deletePatient(String patientId) {
        Patient p = searchPatientById(patientId);
        if (p != null) {
            if (p instanceof Inpatient) {
                releaseBed(((Inpatient) p).getBedNumber());
            }
            patients.remove(p);
            return true;
        }
        return false;
    }

    public boolean allocateBed(String patientId, String bedNumber) {
        Patient p = searchPatientById(patientId);
        if (p == null || p.getCategory() != PatientCategory.INPATIENT) {
            return false;
        }

        if (!isBedAvailable(bedNumber)) {
            return false;
        }

        Inpatient inpatient = new Inpatient(p.getPatientId(), p.getFirstName(), p.getLastName(),
                p.getAge(), p.getGender(), p.getMedicalCondition(), WARD_NAME, bedNumber);

        int index = patients.indexOf(p);
        patients.set(index, inpatient);
        return true;
    }

    public boolean releaseBed(String bedNumber) {
        for (Patient p : patients) {
            if (p instanceof Inpatient) {
                Inpatient inp = (Inpatient) p;
                if (inp.getBedNumber().equalsIgnoreCase(bedNumber)) {
                    inp.setBedNumber("Unassigned");
                    inp.setWardNumber("Unassigned");
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isBedAvailable(String bedNumber) {
        if (!isValidBed(bedNumber)) return false;
        for (Patient p : patients) {
            if (p instanceof Inpatient) {
                Inpatient inp = (Inpatient) p;
                if (bedNumber.equalsIgnoreCase(inp.getBedNumber())) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isValidBed(String bedNumber) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 5; j++) {
                if (beds[i][j].equalsIgnoreCase(bedNumber)) return true;
            }
        }
        return false;
    }

    public void displayWardLayout() {
        System.out.println("\n--- Ward Bed Layout (4x5) ---");
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 5; j++) {
                String bed = beds[i][j];
                String status = isBedAvailable(bed) ? "[ " + bed + " ]" : "[ OCC ]";
                System.out.print(status + "\t");
            }
            System.out.println();
        }
    }

    public List<String> getAvailableBeds() {
        List<String> available = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 5; j++) {
                if (isBedAvailable(beds[i][j])) {
                    available.add(beds[i][j]);
                }
            }
        }
        return available;
    }

    public List<String> getOccupiedBeds() {
        List<String> occupied = new ArrayList<>();
        for (Patient p : patients) {
            if (p instanceof Inpatient) {
                Inpatient inp = (Inpatient) p;
                if (!"Unassigned".equalsIgnoreCase(inp.getBedNumber())) {
                    occupied.add(inp.getBedNumber());
                }
            }
        }
        return occupied;
    }

    public void sortPatientsByLastName() {
        patients.sort(Comparator.comparing(Patient::getLastName, String.CASE_INSENSITIVE_ORDER));
    }

    public void sortPatientsById() {
        patients.sort(Comparator.comparing(Patient::getPatientId, String.CASE_INSENSITIVE_ORDER));
    }

    public void generateReports() {
        System.out.println("\n===== WARD SUMMARY REPORT =====");
        System.out.println("Total Registered Patients: " + patients.size());
        System.out.println("Total Occupied Beds    : " + getOccupiedBeds().size());
        System.out.println("Total Available Beds   : " + getAvailableBeds().size());
        double occupancyRate = (getOccupiedBeds().size() / 20.0) * 100;
        System.out.printf("Ward Occupancy Rate    : %.2f%%\n", occupancyRate);
        System.out.println("=================================");
    }

    public List<Patient> getPatients() { return patients; }
}