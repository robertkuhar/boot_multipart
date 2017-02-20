package hello;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.xml.ws.Response;
import java.io.File;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/robert")
public class RobertController {
  private static final Logger LOG = LoggerFactory.getLogger(RobertController.class);

  @RequestMapping(value = "/hello", method = RequestMethod.GET)
  public ResponseEntity<String> hello() {
    return new ResponseEntity<String>("Hello\n", HttpStatus.OK);
  }

  @RequestMapping(value = "/firstTry", method = RequestMethod.POST)
  public ResponseEntity<String> firstTry(
          @RequestHeader HttpHeaders headers,
          @RequestParam(value = "theFile", required = false) List<MultipartFile> theFiles) {
    LOG.info("firstTry(headers: {}, theFiles: {})", headers, theFiles);
    LOG.info("headers.getContentType(): {}", headers.getContentType());

    int i = 0;
    // Note that if "theFile" is not required and absent in the request this kid is null, not empty...
    if (theFiles != null) {
      for (MultipartFile file : theFiles) {
        LOG.info("firstTry theFile: {}, getName: {}, getOriginalFilename: {}, getContentType: {}",
                i, file.getName(), file.getOriginalFilename(), file.getContentType());
        i++;
      }
    } else {
      LOG.info("theFile is null if none present in the request.");
    }
    return new ResponseEntity<String>("Hello from firstTry\n", HttpStatus.OK);
  }

  // This kind-of works but the allRequestParams has no knowledge of "theFile" stuff.  Hmm.
  @RequestMapping(value = "/secondTry", method = RequestMethod.POST, headers = {"content-type=multipart/mixed"})
  public ResponseEntity<String> secondTry(
          @RequestHeader HttpHeaders headers,
          @RequestParam Map<String, Object> allRequestParams) {
    LOG.info("secondTry: headers.getContentType(): {}", headers.getContentType());
    if (allRequestParams != null) {
      for (String paramName : allRequestParams.keySet()) {
        Object paramValue = allRequestParams.get(paramName);
        LOG.info("secondTry: paramName: {}, paramValue.getClass().getName(): {}, paramValue: {}",
                paramName, paramValue.getClass().getName(), paramValue);
      }
    } else {
      LOG.info("allRequestParams is null if none present in the request");
    }
    return new ResponseEntity<String>("Hello from secondTry", HttpStatus.OK);
  }

  @RequestMapping(value = "/thirdTry/{userId}/scouting_activities", method = RequestMethod.POST,
          headers = "content-type=multipart/mixed")
  public ResponseEntity<String> POST_v1_scouting_activities_image(
          @RequestHeader HttpHeaders headers,
          @PathVariable String userId,
          @RequestParam(value = "image", required = false) MultipartFile image,
          @RequestParam(value = "scouting_activity", required = true) Object scouting_activity_json) {
    LOG.info("POST_v1_scouting_activities_image: headers.getContentType(): {}", headers.getContentType());
    LOG.info("POST_v1_scouting_activities_image: userId: {}", userId);
    LOG.info("POST_v1_scouting_activities_image: image: {}", image);
    LOG.info("POST_v1_scouting_activities_image: scouting_activity_json.getType().getName(): {}, scouting_activity: {}",
            scouting_activity_json.getClass().getName(), scouting_activity_json);
    return new ResponseEntity<String>("POST_v1_scouting_activities_image\n", HttpStatus.OK);
  }

  @RequestMapping(value = "/thirdTry/{userId}/scouting_activities", method = RequestMethod.POST,
          headers = "content-type=application/json")
  public ResponseEntity<String> POST_v1_scouting_activities_no_image(
          @RequestHeader HttpHeaders headers,
          @PathVariable String userId,
          @RequestBody String scouting_activity_json) {
    LOG.info("POST_v1_scouting_activities_no_image: headers.getContentType(): {}", headers.getContentType());
    LOG.info("POST_v1_scouting_activities_no_image: userId: {}", userId);
    LOG.info("POST_v1_scouting_activities_no_image: scouting_activity_json.getType().getName(): {}, scouting_activity: {}",
            scouting_activity_json.getClass().getName(), scouting_activity_json);
    return new ResponseEntity<String>("POST_v1_scouting_activities_no_image\n", HttpStatus.OK);
  }

  // FIXME this never worked!!
  @RequestMapping(value = "/forthTry", method = RequestMethod.POST)
  public ResponseEntity<String> POST_blah_blah(
          @RequestHeader HttpHeaders headers,
          MultipartHttpServletRequest request) {

    LOG.info("----- for each request.getParameterNames() -----");
    Enumeration<String> params = request.getParameterNames();
    while(params.hasMoreElements()) {
      String paramName = params.nextElement();
      LOG.info("paramName: {}, paramValues: {}", paramName, request.getParameterValues(paramName));
    }
    return new ResponseEntity<String>("Hello from thirdTry", HttpStatus.OK);
  }

}
