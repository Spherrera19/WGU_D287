<strong>** DO NOT DISTRIBUTE OR PUBLICLY POST SOLUTIONS TO THESE LABS. MAKE ALL FORKS OF THIS REPOSITORY WITH SOLUTION CODE PRIVATE. PLEASE REFER TO THE STUDENT CODE OF CONDUCT AND ETHICAL EXPECTATIONS FOR COLLEGE OF INFORMATION TECHNOLOGY STUDENTS FOR SPECIFICS. ** </strong>

# WESTERN GOVERNORS UNIVERSITY 
## D287 – JAVA FRAMEWORKS

### Student Information
Steven Herrera
ID: 010825670

## Requirements

### C.  Customize the HTML user interface for your customer’s application. The user interface should include the shop name, the product names, and the names of the parts.

File: src/main/resources/templates/mainscreen.html

Line 15-19: Implemented a professional "Hero" section with the shop title "Herrera Lens & Light".

Line 36: Updated the Part section header to "Camera Components & Accessories".

Line 84: Updated the Product section header to "Professional Photography Kits".

Note: Utilized Bootstrap card and hero-section classes to improve visual hierarchy while maintaining all required functional elements.

Note: Do not remove any elements that were included in the screen. You may add any additional elements you would like or any images, colors, and styles, although it is not required.


### D.  Add an “About” page to the application to describe your chosen customer’s company to web viewers and include navigation to and from the “About” page and the main screen.

File: src/main/java/com/example/demo/controllers/AboutController.java

Lines 1-13: Created the controller class and used @GetMapping("/about") to map the web endpoint to the about view.

File: src/main/resources/templates/about.html

Lines 10-35: Refined the page with a layered UI design, using a negative margin to overlap the content area onto a photography-themed hero header.

Lines 45-60: Created a brand story detailing the Herrera Lens & Light legacy in Downtown Miami and included two-column feature cards for "Pro Gear" and "Miami Local".

Line 52: Implemented a "Return to Store" button using th:href="@{/mainscreen}" to fulfill the bi-directional navigation requirement.

File: src/main/resources/templates/mainscreen.html

Line 26: Added a navigation link to the "About" page within the hero section using th:href="@{/about}".

### E.  Add a sample inventory appropriate for your chosen store to the application. You should have five parts and five products in your sample inventory and should not overwrite existing data in the database.

Note: Make sure the sample inventory is added only when both the part and product lists are empty. When adding the sample inventory appropriate for the store, the inventory is stored in a set so duplicate items cannot be added to your products. When duplicate items are added, make a “multi-pack” part.

File modified: src/main/java/com/example/demo/bootstrap/BootStrapData.java

Logic Detail: Used partRepository.count() == 0 and productRepository.count() == 0 within an if block to ensure data is only seeded when the H2 database is empty.

Sample Parts Added:

Full Frame CMOS Sensor (Inhouse)

24-70mm f/2.8 Pro Lens (Outsourced - Canon)

LP-E6NH Battery Pack (Inhouse)

128GB SDXC V90 Memory Card (Outsourced - SanDisk)

Mechanical Shutter Assembly (Inhouse)

Sample Products Added:

Wedding Photography Kit

Content Creator Bundle

Street Photo Setup

Studio Lighting Suite

Vlogging Starter Pack

### F.  Add a “Buy Now” button to your product list. Your “Buy Now” button must meet each of the following parameters:
•  The “Buy Now” button must be next to the buttons that update and delete products.
• The button should decrement the inventory of that product by one. It should not affect the inventory of any of the associated parts.
•  Display a message that indicates the success or failure of a purchase.

File modified: src/main/resources/templates/mainscreen.html

Lines 120: Added a "Buy Now" button to the product table. The button uses a dynamic link (@{/buyProduct(productID=${tempProduct.id})}) to pass the specific product ID to the controller.

File modified: src/main/java/com/example/demo/controllers/AddProductController.java

Lines 187 - 201: Implemented the buyProduct method.

Logic Detail:

Retrieves the product using productService.findById(theId).

Checks if inventory > 0.

If yes: Decrements inventory by 1, saves the product, and returns "purchase-success".

If no: Returns "purchase-error".

New Files:

src/main/resources/templates/purchase-success.html: Displays a confirmation message for successful purchases.

src/main/resources/templates/purchase-error.html: Displays an error message when attempting to buy an out-of-stock item.


### G.  Modify the parts to track maximum and minimum inventory by doing the following:
•  Add additional fields to the part entity for maximum and minimum inventory.
•  Modify the sample inventory to include the maximum and minimum fields.
•  Add to the InhousePartForm and OutsourcedPartForm forms additional text inputs for the inventory so the user can set the maximum and minimum values.
•  Rename the file the persistent storage is saved to.
•  Modify the code to enforce that the inventory is between or at the minimum and maximum value.

src/main/java/com/example/demo/domain/Part.java: Added min and max integer fields with validation annotations. Added corresponding getters and setters.

src/main/java/com/example/demo/bootstrap/BootStrapData.java: Updated the sample inventory creation script to include min and max values for all 5 default parts to prevent database constraint errors.

src/main/resources/templates/InhousePartForm.html: Added labeled text input fields for Minimum and Maximum inventory. Added Thymeleaf spans to display validation errors.

src/main/resources/templates/OutsourcedPartForm.html: Added labeled text input fields for Minimum and Maximum inventory. Added Thymeleaf spans to display validation errors.


### H.  Add validation for between or at the maximum and minimum fields. The validation must include the following:
•  Display error messages for low inventory when adding and updating parts if the inventory is less than the minimum number of parts.
•  Display error messages for low inventory when adding and updating products lowers the part inventory below the minimum.
•  Display error messages when adding and updating parts if the inventory is greater than the maximum.


### I.  Add at least two unit tests for the maximum and minimum fields to the PartTest class in the test package.


### J.  Remove the class files for any unused validators in order to clean your code.
