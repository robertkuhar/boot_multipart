package hello;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
  public ResponseEntity<String> thirdTry_image(
          @RequestHeader HttpHeaders headers,
          @PathVariable String userId,
          @RequestParam(value = "image", required = false) MultipartFile image,
          @RequestParam(value = "scouting_activity", required = true) Object scouting_activity_json) {
    LOG.info("thirdTry_image: headers.getContentType(): {}", headers.getContentType());
    LOG.info("thirdTry_image: userId: {}", userId);
    LOG.info("thirdTry_image: image: {}", image);
    LOG.info("thirdTry_image: scouting_activity_json.getType().getName(): {}, scouting_activity: {}",
            scouting_activity_json.getClass().getName(), scouting_activity_json);
    return new ResponseEntity<String>("thirdTry_image\n", HttpStatus.OK);
  }

  @RequestMapping(value = "/thirdTry/{userId}/scouting_activities", method = RequestMethod.POST,
          headers = "content-type=application/json")
  public ResponseEntity<String> thirdTry_no_image(
          @RequestHeader HttpHeaders headers,
          @PathVariable String userId,
          @RequestBody String scouting_activity_json) {
    LOG.info("thirdTry_no_image: headers.getContentType(): {}", headers.getContentType());
    LOG.info("thirdTry_no_image: userId: {}", userId);
    LOG.info("thirdTry_no_image: scouting_activity_json.getType().getName(): {}, scouting_activity: {}",
            scouting_activity_json.getClass().getName(), scouting_activity_json);
    return new ResponseEntity<String>("thirdTry_no_image\n", HttpStatus.OK);
  }

  @RequestMapping(value = "/user/{userId}/scouting_activities", method = RequestMethod.POST,
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

  @RequestMapping(value = "/user/{userId}/scouting_activities", method = RequestMethod.POST,
          headers = {"content-type=multipart/mixed","content-type=multipart/form-data"})
  public ResponseEntity<String> POST_v1_scouting_activities(
          @RequestHeader HttpHeaders headers,
          @PathVariable String userId,
          @RequestPart(value = "image", required = false) MultipartFile image,
          @RequestPart(value = "scouting_activity", required = true) String scouting_activity_json) {
    LOG.info("POST_v1_scouting_activities: headers.getContentType(): {}", headers.getContentType());
    LOG.info("POST_v1_scouting_activities: userId: {}", userId);
    LOG.info("POST_v1_scouting_activities: image.originalFilename: {}, image: {}",
            (image!=null) ? image.getOriginalFilename() : null, image);
    LOG.info("POST_v1_scouting_activities: scouting_activity_json.getType().getName(): {}, scouting_activity: {}",
            scouting_activity_json.getClass().getName(), scouting_activity_json);
    return new ResponseEntity<String>("POST_v1_scouting_activities\n", HttpStatus.OK);
  }

  /*
   * This guy is cool because it can handle both multipart/mixed and multpart/file but there is no way I can find
   * to make it deal with multiple unique copies of the same param name.  Under the covers it is definitely capable
   * of doing this as the rich mappings handle this.  Maybe it doesn't matter for my purposes.
   */
  @RequestMapping(value = "/forthTry", method = RequestMethod.POST)
  public ResponseEntity<String> POST_blah_blah(MultipartHttpServletRequest request) {

    LOG.info("forthTry:  request.getContentType(): {}", request.getContentType());

    LOG.info("forthTry:  ----- for each request.getParameterNames() begin -----");
    Enumeration<String> params = request.getParameterNames();
    while (params.hasMoreElements()) {
      String paramName = params.nextElement();
      LOG.info("forthTry:  paramName: {}, paramValues: {}", paramName, request.getParameterValues(paramName));
    }
    LOG.info("----- forthTry:  for each request.getParameterNames() end   -----");

    LOG.info("----- forthTry:  for each request.getFileMap() begin -----");
    Map<String, MultipartFile> multipartFileMap = request.getFileMap();
    for (String paramName : multipartFileMap.keySet()) {
      LOG.info("forthTry:  paramName: {}, paramValue: {}", paramName, multipartFileMap.get(paramName));
    }
    LOG.info("----- forthTry:  for each request.getFileMap() begin -----");

    LOG.info("----- forthTry:  for each request.getFileMap() begin -----");
    for (MultipartFile mulitpartFile : multipartFileMap.values()) {
      LOG.info("forthTry:  name: {}, originalName: {}, contentType: {}",
              mulitpartFile.getName(),
              mulitpartFile.getOriginalFilename(),
              mulitpartFile.getContentType());
    }
    LOG.info("----- forthTry:  for each request.getFileMap() end   -----");

    return new ResponseEntity<String>("Hello from forthTry\n", HttpStatus.OK);
  }

}
