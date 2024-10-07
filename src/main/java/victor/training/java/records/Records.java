package victor.training.java.records;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@SpringBootApplication
public class Records {
  public static void main(String[] args) {
    SpringApplication.run(Records.class, args);
  }
}

@RestController // Spring managed bean
@RequiredArgsConstructor // tells to create a constructor with all final fields
//record BookApi(BookRepo bookRepo) { // 🛑DON'T: proxies don't work on final classes => eg @Secured/@Transactional.. won't work
class BookApi {
  private final BookRepo bookRepo;

  // DTO - Data Transfer Object (no logic, just data) moving between layers across network
  public record CreateBookRequest(
          @NotBlank String title,
          @NotEmpty List<String> authors,
          Optional<String> teaserVideoUrl // may be absent
  ) {
  }

  @PostMapping("books")
  @Transactional
  //@Validated calls a proxy that validates the request parameters
  public void createBook(@RequestBody @Validated CreateBookRequest request) {
    System.out.println("pretend save title:" + request.title() + " and url:" + request.teaserVideoUrl());
    System.out.println("pretend save authors: " + request.authors());
  }

  // ----

  public record SearchBookResult(
      long id,
      String name
  ) {
  }

  @GetMapping("books")
  public List<SearchBookResult> search(@RequestParam String name) {
    return bookRepo.search(name);
  }

}

@Entity
@Data // avoid @Data on @Entity
//  record Book { //JPA mindset requires mutable entities (setters). so, don't use records for JPA entities
class Book {
  @Id
  @GeneratedValue
  private Long id;
  private String title;

//  private String authorFirstName;
//  private String authorLastName;
  @Embedded
    private Author author; // group related fields together into a single object
//  !!!!!! WITHOUT changing the DB schema, we can change the Java code to use a record instead of a class
//  (NO ALTER TABLE NEEDED)
}
@Embeddable // in recent Hibernate versions, @Embeddable can be records
record Author(String firstName, String lastName) {
}


