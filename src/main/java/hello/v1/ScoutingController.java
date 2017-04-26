package hello.v1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping(value = "/v1")
public class ScoutingController {
  private static final Logger LOG = LoggerFactory.getLogger(ScoutingController.class);

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

}
