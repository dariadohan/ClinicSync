package controller;

import Data.MedicalService;
import services.MedicalServiceService;

import java.util.List;
import java.util.Optional;

public class MedicalServiceController {

    private static MedicalServiceService serviceService;

    public MedicalServiceController() {
        serviceService = new MedicalServiceService();
    }

    // Create a new medical service
    public boolean createService(String name, float price, int duration) {
        if (name == null || name.isBlank()) {
            System.out.println("\033[31mService name is required.\033[0m");
            return false;
        }

        if (price <= 0 || duration <= 0) {
            System.out.println("\033[31mPrice and duration must be positive values.\033[0m");
            return false;
        }

        return serviceService.createMedicalService(name, price, duration);
    }

    // Get all services
    public List<MedicalService> getAllServices() {
        System.out.println("Fetching all medical services...");
        return serviceService.getAllServices();
    }

    // Get a service by ID
    public Optional<MedicalService> getServiceById(int id) {
        System.out.println("Fetching medical service with ID: " + id);
        return serviceService.getServiceById(id);
    }

    // Update a service by ID
    public boolean updateService(int id, String newName, float newPrice, int newDuration) {
        System.out.println("Updating service with ID: " + id);
        return serviceService.updateServiceById(id, newName, newPrice, newDuration);
    }

    // Remove a service by ID
    public boolean deleteService(int id) {
        System.out.println("Deleting service with ID: " + id);
        return serviceService.deleteServiceById(id);
    }
}
