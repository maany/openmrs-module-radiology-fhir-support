package org.openmrs.module.radiologyfhirsupport.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by devmaany on 22/6/16.
 */
@Controller
public class MRRTCreationController {
    @RequestMapping(value = "/module/radiologyfhirsupport/template/new.form", method = RequestMethod.GET)
    public String getForm(){
        System.out.println("This is the template creation controller");
        return "module/radiologyfhirsupport/create";
    }
}
