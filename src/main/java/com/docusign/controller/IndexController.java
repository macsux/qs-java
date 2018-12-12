package com.docusign.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class IndexController {
    @RequestMapping(path = "/",method = RequestMethod.GET)
    public String index(ModelMap model) {
        model.addAttribute("title","Home");
        return "pages/index";
    }

    @RequestMapping(path = "/ds-return",method = RequestMethod.GET)
    public String returnController(@RequestParam(value="state", required = false) String state,
                                   @RequestParam(value="event", required = false) String event,
                                   @RequestParam(value="envelopeId", required = false) String envelopeId,
                                   ModelMap model) {
        model.addAttribute("title" , "Return from DocuSign");
        model.addAttribute("event", event);
        model.addAttribute("state", state);
        model.addAttribute("qpEnvelopeId", envelopeId);
        return "pages/ds_return";
    }
}
