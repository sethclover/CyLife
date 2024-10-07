package onetomany.Laptops;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 
 * @author Vivek Bengre
 * 
 */ 

public interface LaptopRepository extends JpaRepository<Laptop, Long> {
    Laptop findById(int id);
    void deleteById(int id);
}
