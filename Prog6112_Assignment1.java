/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.prog6112_assignment1;

/**
 *
 * @author Student
 */
import java.util.Scanner;

public class Prog6112_Assignment1 {
    private static WardManager wardManager = new WardManager();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        boolean exit = false;
        while (!exit) {
            System.out.println("\n===== MEDICARE HOSPITAL SYSTEM =====");
            System.out.println("1. Patient Management");
            System.out.println("2. Bed Management");
            System.out.println("3. Ward Reports");
            System.out.println("4. Exit");
            System.out.print("Select an option: ");

            int choice = getIntInput();
            switch (choice) {
                case 1: patientMenu(); break;
                case 2: bedMenu(); break;
                case 3: reportMenu(); break;
                case 4: 
                    exit = true;
                    System.out.println("Exiting system...");
                    break;
                default: System.out.println("Invalid option.");
            }
        }
    }

    private static void patientMenu() {
        System.out.println("\n--- Patient Management ---");
        System.out.println("1. Register Patient");
        System.out.println("2. Search Patient");
        System.out.println("3. Update Patient Details");
        System.out.println("4. Delete Patient");
        System.out.println("5. Display All Patients");
        System.out.print("Select: ");

        int option = getIntInput();
        switch (option) {
            case 1:
                System.out.print("Enter ID: "); String id = scanner.nextLine();
                System.out.print("First Name: "); String fn = scanner.nextLine();
                System.out.print("Last Name: "); String ln = scanner.nextLine();
                System.out.print("Age: "); int age = getIntInput();
                System.out.print("Gender: "); String gender = scanner.nextLine();
                System.out.print("Medical Condition: "); String cond = scanner.nextLine();
                System.out.println("Category (1-INPATIENT, 2-OUTPATIENT, 3-EMERGENCY): ");
                int catChoice = getIntInput();
                PatientCategory cat = (catChoice == 1) ? PatientCategory.INPATIENT :
                                      (catChoice == 2) ? PatientCategory.OUTPATIENT : PatientCategory.EMERGENCY;

                Patient patient = (cat == PatientCategory.INPATIENT) ?
                        new Inpatient(id, fn, ln, age, gender, cond, "Ward 1", "Unassigned") :
                        new Patient(id, fn, ln, age, gender, cond, cat);

                if (wardManager.registerPatient(patient)) {
                    System.out.println("Patient registered.");
                } else {
                    System.out.println("Error: Duplicate ID!");
                }
                break;
            case 2:
                System.out.print("Enter Patient ID: ");
                Patient found = wardManager.searchPatientById(scanner.nextLine());
                if (found != null) found.displayDetails();
                else System.out.println("Patient not found.");
                break;
            case 3:
                System.out.print("Enter Patient ID to update: "); String uId = scanner.nextLine();
                System.out.print("New First Name: "); String uFn = scanner.nextLine();
                System.out.print("New Last Name: "); String uLn = scanner.nextLine();
                System.out.print("New Age: "); int uAge = getIntInput();
                System.out.print("New Gender: "); String uGen = scanner.nextLine();
                System.out.print("New Medical Condition: "); String uCond = scanner.nextLine();
                if (wardManager.updatePatient(uId, uFn, uLn, uAge, uGen, uCond)) {
                    System.out.println("Patient updated.");
                } else {
                    System.out.println("Patient not found.");
                }
                break;
            case 4:
                System.out.print("Enter Patient ID to delete: ");
                if (wardManager.deletePatient(scanner.nextLine())) {
                    System.out.println("Patient deleted.");
                } else {
                    System.out.println("Patient not found.");
                }
                break;
            case 5:
                System.out.println("Sort by: 1. ID | 2. Last Name");
                if (getIntInput() == 2) wardManager.sortPatientsByLastName();
                else wardManager.sortPatientsById();
                for (Patient p : wardManager.getPatients()) {
                    p.displayDetails();
                }
                break;
        }
    }

    private static void bedMenu() {
        System.out.println("\n--- Bed Management ---");
        System.out.println("1. Display Ward Layout");
        System.out.println("2. Allocate Bed");
        System.out.println("3. Release Bed");
        System.out.println("4. Display Available Beds");
        System.out.println("5. Display Occupied Beds");
        System.out.print("Select: ");

        int option = getIntInput();
        switch (option) {
            case 1: wardManager.displayWardLayout(); break;
            case 2:
                System.out.print("Enter Inpatient ID: "); String id = scanner.nextLine();
                System.out.print("Enter Bed Number (e.g., B01): "); String bed = scanner.nextLine();
                if (wardManager.allocateBed(id, bed)) {
                    System.out.println("Bed allocated.");
                } else {
                    System.out.println("Failed: Invalid patient, bed occupied, or invalid bed ID.");
                }
                break;
            case 3:
                System.out.print("Enter Bed Number to Release: ");
                if (wardManager.releaseBed(scanner.nextLine())) {
                    System.out.println("Bed released.");
                } else {
                    System.out.println("Release failed.");
                }
                break;
            case 4: System.out.println("Available: " + wardManager.getAvailableBeds()); break;
            case 5: System.out.println("Occupied: " + wardManager.getOccupiedBeds()); break;
        }
    }

    private static void reportMenu() {
        wardManager.generateReports();
    }

    private static int getIntInput() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}