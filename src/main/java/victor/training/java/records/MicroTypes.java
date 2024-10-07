package victor.training.java.records;

import org.jooq.lambda.tuple.Tuple;
import org.jooq.lambda.tuple.Tuple2;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.joining;
import static org.assertj.core.api.Assertions.assertThat;

public class MicroTypes {
//primitive obsession - bad habit of using primitives instead of domain types, like String, int, boolean
//   instead use domain types like Email, Age, Money, etc. with proper validation and behavior
   public Map<CustomerId, Set<ProductCount>> extremeFP() {
      Long customerId = 1L;
      Integer product1Count = 2;
      Integer product2Count = 4;
      return Map.of(new CustomerId(customerId), Set.of(
         new ProductCount(
                 "Table", product1Count),
          new ProductCount(
                  "Chair", product2Count)
      ));
   }
   record ProductCount(String name, int count) {}
   //microtypes - domain types with a single field
   record CustomerId(long id) {}
   record IBAN(String value) {}
   record Email(String value) {}
   record SSN(String value) {}


   void lackOfAbstractions() {
      Map<CustomerId, Set<ProductCount>> map = extremeFP();
      // 🚫Don't use 'var' above

      for (CustomerId cid : map.keySet()) {
         String pl = map.get(cid).stream()
             .map(t -> t.count()+ " of " + t.name())
             .collect(joining(", "));
         System.out.println("cid=" + cid + " got " + pl);
      }
   }

   public void varUsecase() {
      ResponseEntity<List<String>> response = new RestTemplate().exchange(new RequestEntity<>(HttpMethod.POST, URI.create("http://some-url")), new ParameterizedTypeReference<List<String>>() {
      });
   }

}
