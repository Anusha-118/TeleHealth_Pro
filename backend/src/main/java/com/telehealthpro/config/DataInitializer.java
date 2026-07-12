package com.telehealthpro.config;

import com.telehealthpro.entity.DoctorCategory;
import com.telehealthpro.entity.User;
import com.telehealthpro.repository.DoctorCategoryRepository;
import com.telehealthpro.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Runs once at startup:
 *  - Creates the default admin account if it doesn't exist (admin@telehealthpro.com / Admin@123)
 *  - Seeds the ten doctor categories if the table is empty
 *
 * Note: sample doctor and appointment data is provided separately in sql/data.sql
 * for anyone who prefers loading via MySQL directly. This runner keeps the app
 * usable even on a completely empty database.
 */
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final DoctorCategoryRepository categoryRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        seedAdmin();
        seedCategories();
    }

    private void seedAdmin() {
        if (!userRepository.existsByEmail("admin@telehealthpro.com")) {
            User admin = new User();
            admin.setFullName("System Administrator");
            admin.setEmail("admin@telehealthpro.com");
            admin.setMobile("9999999999");
            admin.setGender("Other");
            admin.setPassword(passwordEncoder.encode("Admin@123"));
            admin.setRole("ROLE_ADMIN");
            admin.setEnabled(true);
            userRepository.save(admin);
            System.out.println(">>> Default admin created: admin@telehealthpro.com / Admin@123");
        }
    }

    private void seedCategories() {
        if (categoryRepository.count() == 0) {
            List<DoctorCategory> categories = List.of(
                    category("General Physician", "bi-heart-pulse"),
                    category("Cardiologist", "bi-heart"),
                    category("Dermatologist", "bi-bandaid"),
                    category("Orthopedic", "bi-person-wheelchair"),
                    category("Neurologist", "bi-cpu"),
                    category("ENT Specialist", "bi-ear"),
                    category("Dentist", "bi-emoji-smile"),
                    category("Gynecologist", "bi-gender-female"),
                    category("Pediatrician", "bi-emoji-laughing"),
                    category("Psychiatrist", "bi-brain")
            );
            categoryRepository.saveAll(categories);
            System.out.println(">>> Seeded 10 default doctor categories");
        }
    }

    private DoctorCategory category(String name, String icon) {
        DoctorCategory c = new DoctorCategory();
        c.setCategoryName(name);
        c.setIconClass(icon);
        return c;
    }
}
