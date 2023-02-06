package victor.training.java.records;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
class MyController {
  private final MyService service; // Spring has to injected a Proxy, to intercept @Transactional method calls
}

@Service
/*final*/
record MyService(MyRepo myRepo) { // CGLIB cannot generate a proxy (dynamic subclass) of a final class
  @Transactional
  public void method() {

  }
}

interface MyRepo {
}
