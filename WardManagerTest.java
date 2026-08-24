package com.mycompany.prog6112_assignment1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class WardManagerTest {
    private WardManager manager;

    @BeforeEach
    public void setUp() {
        manager = new WardManager();
    }

    @Test
    public void testRegisterPatient() {
        Patient p = new Patient("P001", "John", "Doe", 30, "Male", "Flu", PatientCategory.OUTPATIENT);
        assertTrue(manager.registerPatient(p));
        assertEquals(1, manager.getPatients().size());
    }

    @Test
    public void testPreventDuplicatePatientID() {
        Patient p1 = new Patient("P001", "John", "Doe", 30, "Male", "Flu", PatientCategory.OUTPATIENT);
        Patient p2 = new Patient("P001", "Jane", "Smith", 25, "Female", "Fever", PatientCategory.INPATIENT);
        manager.registerPatient(p1);
        assertFalse(manager.registerPatient(p2));
    }

    @Test
    public void testSearchPatient() {
        Patient p = new Patient("P002", "Alice", "Brown", 45, "Female", "Asthma", PatientCategory.EMERGENCY);
        manager.registerPatient(p);
        assertNotNull(manager.searchPatientById("P002"));
    }

    @Test
    public void testUpdatePatient() {
        Patient p = new Patient("P003", "Bob", "Green", 50, "Male", "Diabetes", PatientCategory.OUTPATIENT);
        manager.registerPatient(p);
        assertTrue(manager.updatePatient("P003", "Robert", "Green", 51, "Male", "Diabetes"));
        assertEquals("Robert", manager.searchPatientById("P003").getFirstName());
    }

    @Test
    public void testDeletePatient() {
        Patient p = new Patient("P004", "Charlie", "White", 60, "Male", "Hypertension", PatientCategory.OUTPATIENT);
        manager.registerPatient(p);
        assertTrue(manager.deletePatient("P004"));
        assertNull(manager.searchPatientById("P004"));
    }

    @Test
    public void testAllocateBed() {
        Patient p = new Patient("P005", "David", "Black", 35, "Male", "Fracture", PatientCategory.INPATIENT);
        manager.registerPatient(p);
        assertTrue(manager.allocateBed("P005", "0,0"));
        assertFalse(manager.isBedAvailable("0,0"));
    }

    @Test
    public void testPreventAllocatingOccupiedBed() {
        Patient p1 = new Patient("P006", "Eva", "Blue", 28, "Female", "Observation", PatientCategory.INPATIENT);
        Patient p2 = new Patient("P007", "Frank", "Gray", 40, "Male", "Surgery", PatientCategory.INPATIENT);
        manager.registerPatient(p1);
        manager.registerPatient(p2);
        manager.allocateBed("P006", "0,0");
        assertFalse(manager.allocateBed("P007", "0,0"));
    }

    @Test
    public void testReleaseBed() {
        Patient p = new Patient("P008", "Grace", "Pink", 22, "Female", "Migraine", PatientCategory.INPATIENT);
        manager.registerPatient(p);
        manager.allocateBed("P008", "0,1");
        assertTrue(manager.releaseBed("0,1"));
        assertTrue(manager.isBedAvailable("0,1"));
    }

    @Test
    public void testSortPatients() {
        Patient p1 = new Patient("P009", "Zack", "Zulu", 30, "Male", "Cold", PatientCategory.OUTPATIENT);
        Patient p2 = new Patient("P010", "Adam", "Alpha", 25, "Male", "Flu", PatientCategory.OUTPATIENT);
        manager.registerPatient(p1);
        manager.registerPatient(p2);
        manager.sortPatientsById();
        assertEquals("P009", manager.getPatients().get(0).getPatientId());
    }

    @Test
    public void testPreventBedAllocationWhenFull() {
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 5; col++) {
                String bedNum = row + "," + col;
                String pId = "P1" + row + col;
                Patient p = new Patient(pId, "Test", "User", 30, "Male", "Checkup", PatientCategory.INPATIENT);
                manager.registerPatient(p);
                manager.allocateBed(pId, bedNum);
            }
        }
        Patient extra = new Patient("P999", "Extra", "Patient", 40, "Female", "Emergency", PatientCategory.INPATIENT);
        manager.registerPatient(extra);
        assertFalse(manager.allocateBed("P999", "0,0"));
    }
}