package com.looseboxes;

import com.looseboxes.pu.References;
import com.looseboxes.pu.entities.Product;
import com.looseboxes.pu.entities.Product_;
import com.looseboxes.web.WebApp;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import com.bc.jpa.context.JpaContext;
import com.looseboxes.pu.entities.Availability;
import com.looseboxes.pu.entities.Availability_;
import java.math.RoundingMode;
import java.util.Objects;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

/**
 * @author Josh
 */
public class EditPricesTest {
    
    private final boolean updateDatabase = false;

    private final BigDecimal factor;

    private final MathContext mathContext;
    
    public EditPricesTest() {
        mathContext = WebApp.getInstance().getDefaults().getDefaultMathContext();
        factor = new BigDecimal(370).divide(new BigDecimal(520), mathContext);
System.out.println(this.getClass().getSimpleName()+". Factor: " + factor);        
    }
    
    @BeforeClass
    public static void setUpClass() throws Exception {
        TestWebApp.init();
    }
    
    @AfterClass
    public static void tearDownClass() throws Exception {
        TestWebApp.destroy();
    }
    
    @Test
    public void testAll() throws Exception {
        this.test(References.availability.InStock, 100, Integer.MAX_VALUE);
        this.test(References.availability.LimitedAvailability, 100, Integer.MAX_VALUE);
    }

    private void test(References.availability availabilityRef, int batchSize, int max) throws Exception {
        
        final JpaContext jpa = WebApp.getInstance().getJpaContext();

        final Availability availability = jpa.getDaoForSelect(Availability.class)
                .from(Availability.class)
                .where(Availability_.availability.getName(), availabilityRef.name())
                .getSingleResultAndClose();
        
        int batch = 0;
        
        int total = 0;
        
        while(total < max) {
            
            final List<Integer> productIds = jpa.getDaoForSelect(Product.class, Integer.class)
                    .from(Product.class)
                    .select(Product_.productid.getName())
                    .where(Product_.availabilityid.getName(), availability)
                    .createQuery().setFirstResult(total).setMaxResults(batchSize).getResultList();
            
System.out.println("Availability: "+availability.getAvailability()+", batch: "+batch+", batch size: "+(productIds==null?null:productIds.size()));
            
            if(productIds == null || productIds.isEmpty()) {
                break;
            }
            
            total += productIds.size();
            
            final EntityManager em = jpa.getEntityManager(Product.class);
            
            try{
                
                final EntityTransaction t = em.getTransaction();
                
                try{
                    
                    t.begin();
//////////////////////////////////////////////////////////                    
                    for(Integer productId : productIds) {

                        final Product product = em.find(Product.class, productId);

                        final BigDecimal price = product.getPrice();
                        Objects.requireNonNull(price, "Price cannot be null");
                        BigDecimal price_update = price.multiply(factor, mathContext);

                        final BigDecimal discount = product.getDiscount();
                        BigDecimal discount_update = discount == null ? null : discount.multiply(factor, mathContext);

                        String prefix;
                        try{

                            price_update = this.format(price_update);

                            product.setPrice(price_update);

                            if(discount_update != null) {

                                discount_update = this.format(discount_update);

                                product.setDiscount(discount_update);
                            }

                            em.merge(product);

                            prefix = "SUCCESS";

                        }catch(Exception e) {

                            prefix = "  ERROR";    

                            e.printStackTrace();

                            product.setPrice(price);
                            product.setDiscount(discount);
                        }
        System.out.println(prefix + " Product ID: "+productId+".\tUpdate of price from: "+price+
            " to "+price_update+", discount from: "+discount+" to "+discount_update);                    
                    }
/////////////////////////////////////////////////////////
                    if(updateDatabase) {
                        t.commit();
                    }
                    
                }finally{
                    if(t.isActive()) {
                        t.rollback();
                    }
                }
                
            }finally{
                em.close();
            }
            
System.out.println("Committed batch: "+batch+", total committed: "+total);                

            ++batch;
        }
    }
    
    public BigDecimal format(BigDecimal bd) {
        return this.replaceLastIntegersBeforeDecimalPointWithZero(bd, 1);
    }
    
    /**
     * If <code>n == 1</code>, BigDecimal argument of <code>1_234.5678</code> becomes <code>1_230</code>
     * @param bigDecimal
     * @param n
     * @return 
     */
    private BigDecimal replaceLastIntegersBeforeDecimalPointWithZero(BigDecimal bigDecimal, int n) {
        //  If n == 1, 1_234.5678 becomes 1_230
        return bigDecimal.movePointLeft(n).setScale(0, RoundingMode.HALF_UP).multiply(new BigDecimal(n * 10));
    }
}
