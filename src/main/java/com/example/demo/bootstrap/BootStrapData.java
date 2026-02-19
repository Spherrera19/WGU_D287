package com.example.demo.bootstrap;

import com.example.demo.domain.InhousePart;
import com.example.demo.domain.OutsourcedPart;
import com.example.demo.domain.Part;
import com.example.demo.domain.Product;
import com.example.demo.repositories.OutsourcedPartRepository;
import com.example.demo.repositories.PartRepository;
import com.example.demo.repositories.ProductRepository;
import com.example.demo.service.OutsourcedPartService;
import com.example.demo.service.OutsourcedPartServiceImpl;
import com.example.demo.service.ProductService;
import com.example.demo.service.ProductServiceImpl;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 *
 *
 *
 *
 */
@Component
public class BootStrapData implements CommandLineRunner {

    private final PartRepository partRepository;
    private final ProductRepository productRepository;

    private final OutsourcedPartRepository outsourcedPartRepository;

    public BootStrapData(PartRepository partRepository, ProductRepository productRepository, OutsourcedPartRepository outsourcedPartRepository) {
        this.partRepository = partRepository;
        this.productRepository = productRepository;
        this.outsourcedPartRepository = outsourcedPartRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        // 1. Only run if the database is empty
        if (partRepository.count() == 0 && productRepository.count() == 0) {

            // 2. CAMERA PARTS
            InhousePart sensor = new InhousePart();
            sensor.setName("Full Frame CMOS Sensor");
            sensor.setPrice(450.0);
            sensor.setInv(10);
            partRepository.save(sensor);

            OutsourcedPart lens = new OutsourcedPart();
            lens.setName("24-70mm f/2.8 Pro Lens");
            lens.setPrice(1200.0);
            lens.setInv(5);
            lens.setCompanyName("Canon"); // Required for Outsourced parts
            partRepository.save(lens);

            InhousePart battery = new InhousePart();
            battery.setName("LP-E6NH Battery Pack");
            battery.setPrice(79.0);
            battery.setInv(25);
            partRepository.save(battery);

            OutsourcedPart card = new OutsourcedPart();
            card.setName("128GB SDXC V90 Memory Card");
            card.setPrice(115.0);
            card.setInv(40);
            card.setCompanyName("SanDisk");
            partRepository.save(card);

            InhousePart shutter = new InhousePart();
            shutter.setName("Mechanical Shutter Assembly");
            shutter.setPrice(210.0);
            shutter.setInv(15);
            partRepository.save(shutter);

            // PHOTOGRAPHY KITS (PRODUCTS)
            productRepository.save(new Product("Wedding Photography Kit", 5500.0, 3));
            productRepository.save(new Product("Content Creator Bundle", 1250.0, 8));
            productRepository.save(new Product("Street Photo Setup", 2900.0, 5));
            productRepository.save(new Product("Studio Lighting Suite", 950.0, 2));
            productRepository.save(new Product("Vlogging Starter Pack", 899.0, 12));
        }

        System.out.println("Started in Bootstrap");
        System.out.println("Number of Products "+productRepository.count());
        System.out.println(productRepository.findAll());
        System.out.println("Number of Parts "+partRepository.count());
        System.out.println(partRepository.findAll());
    }
}
