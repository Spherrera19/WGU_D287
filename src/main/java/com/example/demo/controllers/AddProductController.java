package com.example.demo.controllers;

import com.example.demo.domain.Part;
import com.example.demo.domain.Product;
import com.example.demo.domain.InhousePart;
import com.example.demo.domain.OutsourcedPart;
import com.example.demo.repositories.PartRepository;
import com.example.demo.repositories.ProductRepository;
import com.example.demo.service.PartService;
import com.example.demo.service.PartServiceImpl;
import com.example.demo.service.ProductService;
import com.example.demo.service.ProductServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller responsible for managing Product interactions
 */
@Controller
public class AddProductController {
    @Autowired
    private ApplicationContext context;
    @Autowired
    private PartRepository partRepository;
    @Autowired
    private ProductRepository productRepository;

    private PartService partService;
    private List<Part> theParts;
    private static Product product1;
    private Product product;

    public AddProductController(PartService partService) {
        this.partService = partService;
    }

    @GetMapping("/showFormAddProduct")
    public String showFormAddPart(Model theModel) {
        theModel.addAttribute("parts", partService.findAll());
        product = new Product();
        product1=product;
        theModel.addAttribute("product", product);

        List<Part>availParts=new ArrayList<>();
        for(Part p: partService.findAll()){
            if(!product.getParts().contains(p))availParts.add(p);
        }
        theModel.addAttribute("availparts",availParts);
        theModel.addAttribute("assparts",product.getParts());
        return "productForm";
    }

    @PostMapping("/showFormAddProduct")
    public String submitForm(@Valid @ModelAttribute("product") Product product, BindingResult bindingResult, Model theModel) {
        theModel.addAttribute("product", product);

        if(bindingResult.hasErrors()){
            ProductService productService = context.getBean(ProductServiceImpl.class);
            Product product2 = new Product();
            try {
                product2 = productService.findById((int) product.getId());
            } catch (Exception e) {
                System.out.println("Error Message " + e.getMessage());
            }
            theModel.addAttribute("parts", partService.findAll());
            List<Part>availParts=new ArrayList<>();
            for(Part p: partService.findAll()){
                if(!product2.getParts().contains(p))availParts.add(p);
            }
            theModel.addAttribute("availparts",availParts);
            theModel.addAttribute("assparts",product2.getParts());
            return "productForm";
        }
        //       theModel.addAttribute("assparts", assparts);
        //       this.product=product;
        //        product.getParts().addAll(assparts);
        else {
            ProductService repo = context.getBean(ProductServiceImpl.class);
            if(product.getId()!=0) {
                Product product2 = repo.findById((int) product.getId());
                PartService partService1 = context.getBean(PartServiceImpl.class);
                if(product.getInv()- product2.getInv()>0) {
                    for (Part p : product2.getParts()) {
                        int inv = p.getInv();
                        p.setInv(inv - (product.getInv() - product2.getInv()));
                        partService1.save(p);
                    }
                }
            }
            else{
                product.setInv(0);
            }
            repo.save(product);
            return "confirmationaddproduct";
        }
    }

    @GetMapping("/showProductFormForUpdate")
    public String showProductFormForUpdate(@RequestParam("productID") int theId, Model theModel) {
        theModel.addAttribute("parts", partService.findAll());
        ProductService repo = context.getBean(ProductServiceImpl.class);
        Product theProduct = repo.findById(theId);
        product1=theProduct;
        //    this.product=product;
        //set the employ as a model attibute to prepopulate the form
        theModel.addAttribute("product", theProduct);
        theModel.addAttribute("assparts",theProduct.getParts());
        List<Part>availParts=new ArrayList<>();
        for(Part p: partService.findAll()){
            if(!theProduct.getParts().contains(p))availParts.add(p);
        }
        theModel.addAttribute("availparts",availParts);
        //send over to our form
        return "productForm";
    }

    @GetMapping("/deleteproduct")
    public String deleteProduct(@RequestParam("productID") int theId, Model theModel) {
        ProductService productService = context.getBean(ProductServiceImpl.class);
        Product product2=productService.findById(theId);
        for(Part part:product2.getParts()){
            part.getProducts().remove(product2);
            partService.save(part);
        }
        product2.getParts().removeAll(product2.getParts());
        productService.save(product2);
        productService.deleteById(theId);

        return "confirmationdeleteproduct";
    }

    // make the add and remove buttons work

    @GetMapping("/associatepart")
    public String associatePart(@Valid @RequestParam("partID") int theID, Model theModel){
        //    theModel.addAttribute("product", product);
        //    Product product1=new Product();
        if (product1.getName()==null) {
            return "saveproductscreen";
        }
        else{
            product1.getParts().add(partService.findById(theID));
            partService.findById(theID).getProducts().add(product1);
            ProductService productService = context.getBean(ProductServiceImpl.class);
            productService.save(product1);
            partService.save(partService.findById(theID));
            theModel.addAttribute("product", product1);
            theModel.addAttribute("assparts",product1.getParts());
            List<Part>availParts=new ArrayList<>();
            for(Part p: partService.findAll()){
                if(!product1.getParts().contains(p))availParts.add(p);
            }
            theModel.addAttribute("availparts",availParts);
            return "productForm";}
        //        return "confirmationassocpart";
    }

    @GetMapping("/removepart")
    public String removePart(@RequestParam("partID") int theID, Model theModel){
        theModel.addAttribute("product", product);
        //  Product product1=new Product();
        product1.getParts().remove(partService.findById(theID));
        partService.findById(theID).getProducts().remove(product1);
        ProductService productService = context.getBean(ProductServiceImpl.class);
        productService.save(product1);
        partService.save(partService.findById(theID));
        theModel.addAttribute("product", product1);
        theModel.addAttribute("assparts",product1.getParts());
        List<Part>availParts=new ArrayList<>();
        for(Part p: partService.findAll()){
            if(!product1.getParts().contains(p))availParts.add(p);
        }
        theModel.addAttribute("availparts",availParts);
        return "productForm";
    }

    // PART F: BUY NOW LOGIC
    // =========================================================
    @GetMapping("/buyProduct")
    public String buyProduct(@RequestParam("productID") int theId, Model theModel) {
        ProductService productService = context.getBean(ProductServiceImpl.class);
        Product productToBuy = productService.findById(theId);

        // Check if product exists and inventory is sufficient
        if(productToBuy.getInv() > 0) {
            productToBuy.setInv(productToBuy.getInv() - 1);
            productService.save(productToBuy);
            return "purchase-success";
        }
        else {
            return "purchase-error";
        }
    }

    // RESET DATABASE LOGIC (Cleaned up)
    // =========================================================
    @GetMapping("/resetDatabase")
    public String resetDatabase() {
        // 1. CLEAR EXISTING DATA
        // delete products first to avoid foreign key constraints
        List<Product> products = (List<Product>) productRepository.findAll();
        for (Product p : products) {
            p.getParts().clear(); // Remove relationships
            productRepository.save(p);
        }
        productRepository.deleteAll(); // Delete products
        partRepository.deleteAll();    // Delete parts

        // 2. RE-POPULATE 5 PARTS
        InhousePart sensor = new InhousePart();
        sensor.setName("Full Frame CMOS Sensor");
        sensor.setPrice(450.0);
        sensor.setInv(10);
        sensor.setMin(2);
        sensor.setMax(50);
        partRepository.save(sensor);

        OutsourcedPart lens = new OutsourcedPart();
        lens.setName("24-70mm f/2.8 Pro Lens");
        lens.setPrice(1200.0);
        lens.setInv(5);
        lens.setMin(1);
        lens.setMax(20);
        lens.setCompanyName("Canon");
        partRepository.save(lens);

        InhousePart battery = new InhousePart();
        battery.setName("LP-E6NH Battery Pack");
        battery.setPrice(79.0);
        battery.setInv(25);
        battery.setMin(5);
        battery.setMax(100);
        partRepository.save(battery);

        OutsourcedPart card = new OutsourcedPart();
        card.setName("128GB SDXC V90 Memory Card");
        card.setPrice(115.0);
        card.setInv(40);
        card.setMin(10);
        card.setMax(200);
        card.setCompanyName("SanDisk");
        partRepository.save(card);

        InhousePart shutter = new InhousePart();
        shutter.setName("Mechanical Shutter Assembly");
        shutter.setPrice(210.0);
        shutter.setInv(15);
        shutter.setMin(2);
        shutter.setMax(30);
        partRepository.save(shutter);

        // 3. RE-POPULATE 5 PRODUCTS
        productRepository.save(new Product("Wedding Photography Kit", 5500.0, 3));
        productRepository.save(new Product("Content Creator Bundle", 1250.0, 8));
        productRepository.save(new Product("Street Photo Setup", 2900.0, 5));
        productRepository.save(new Product("Studio Lighting Suite", 950.0, 2));
        productRepository.save(new Product("Vlogging Starter Pack", 899.0, 12));

        // 4. Return to main screen
        return "redirect:/mainscreen";
    }
}
