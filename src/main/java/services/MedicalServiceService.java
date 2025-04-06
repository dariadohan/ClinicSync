package services;

import Data.MedicalService;
import dao.MedicalServiceDAO;

import java.util.List;
import java.util.Optional;

public class MedicalServiceService {

    private static final MedicalServiceDAO medicalServiceDAO = new MedicalServiceDAO();

    public boolean createMedicalService(String name, float price, int duration) {
        if (name == null || name.isBlank()) {
            System.out.println("\033[31mService name cannot be empty.\033[0m");
            return false;
        }

        if (price <= 0) {
            System.out.println("\033[31mPrice must be greater than zero.\033[0m");
            return false;
        }

        if (duration <= 0) {
            System.out.println("\033[31mDuration must be greater than zero.\033[0m");
            return false;
        }

        if (!isServiceNameUnique(name)) {
            System.out.println("\033[31mService name already exists.\033[0m");
            return false;
        }

        MedicalService service = new MedicalService(name, price, duration);
        return medicalServiceDAO.addMedicalService(service);
    }

    public boolean isServiceNameUnique(String name) {
        return medicalServiceDAO.findByName(name).isEmpty();
    }

    public boolean deleteServiceById(int id) {
        return medicalServiceDAO.deleteById(id);
    }

    public boolean updateServiceById(int id, String newName, float newPrice, int newDuration) {
        Optional<MedicalService> serviceOpt = medicalServiceDAO.findById(id);

        if (serviceOpt.isEmpty()) {
            System.out.println("\033[31mMedical service not found with ID: " + id + "\033[0m");
            return false;
        }

        MedicalService service = serviceOpt.get();
        service.setName(newName);
        service.setPrice(newPrice);
        service.setDuration(newDuration);

        return medicalServiceDAO.updateServiceById(id, service);
    }

    public List<MedicalService> getAllServices() {
        return medicalServiceDAO.getAll();
    }

    public Optional<MedicalService> getServiceById(int id) {
        return medicalServiceDAO.findById(id);
    }
}
