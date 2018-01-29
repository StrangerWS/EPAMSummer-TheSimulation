package boot.controller;

import org.springframework.web.bind.annotation.*;

/**
 * Created by DobryninAM on 22.09.2017.
 */
@RestController
public class LevelingController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    private String leveling(){
        return "ok";
    }
}
