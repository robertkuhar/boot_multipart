package hello;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class HealthzController {
  @RequestMapping(value = "/healthz", method = RequestMethod.GET)
  public ResponseEntity<String> hello() {
    return new ResponseEntity<String>("OK\n", HttpStatus.OK);
  }

}
