package com.docusign.controller;

import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.client.ApiClient;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.*;
import com.docusign.esign.api.EnvelopesApi.ListStatusChangesOptions;
import com.docusign.model.Session;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.joda.time.LocalDate;
import java.io.IOException;


///////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////
//
// Java Quickstart example: List envelopes whose status has changed
//
// Copyright (c) 2018 by DocuSign, Inc.
// License: The MIT License -- https://opensource.org/licenses/MIT
//
///////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////

@Controller
public class QS03ListEnvelopesController {

    @Autowired
    Session session;


    @RequestMapping(path = "/qs03", method = RequestMethod.POST)
    public Object create(ModelMap model) throws ApiException, IOException {
        model.addAttribute("title","Embedded Signing Ceremony");

        // Data for this example
        // Fill in these constants
        //
        // Obtain an OAuth access token from https://developers.hqtest.tst/oauth-token-generator
        String accessToken = "{ACCESS_TOKEN";
        // Obtain your accountId from demo.docusign.com -- the account id is shown in the drop down on the
        // upper right corner of the screen by your picture or the default picture.
        String accountId = "{ACCOUNT_ID}";


        /////////////////////
        accessToken = "eyJ0eXAiOiJNVCIsImFsZyI6IlJTMjU2Iiwia2lkIjoiNjgxODVmZjEtNGU1MS00Y2U5LWFmMWMtNjg5ODEyMjAzMzE3In0.AQkAAAABAAUABwCA8wFiAmDWSAgAgDMlcEVg1kgCAFCYSRTxQ4RBlE9V5f7RiHAVAAEAAAAYAAEAAAAFAAAADQAkAAAAZjBmMjdmMGUtODU3ZC00YTcxLWE0ZGEtMzJjZWNhZTNhOTc4EgACAAAABwAAAG1hbmFnZWQLAAAAZHNfaW50ZXJuYWwwAADA_yYCYNZI.zKcewRlJJGkkimeaiPUMIhsFxzN3DSdJO2Lk1H1GS4JILknNj8vyBEadiM8qvhqV3buO3lZKjJN0pl9dIGtT5hP6wqtK2VBYgBXW1FGVfC4a21oPyABZu6h4UrggXpkuWE6Tv11tG7TiZbRb_CPyeQlG4d8AdXVKW6jcWF9gILqUzgNTivcI31_LKqIceKoB_IKdyhalPR_oDCvl4QOZSBrIHvsW5amgiuZNKwGLRQUa7XaJ9TrYclSBrspA-91wzFlGnOvjFu3fYDQk279ZaPyAVTSsCFUWH25dAmIydBlmKdTjJlgEn8Fk_U0DR7OHgjH8sg-qcMIo9jSQ9M_OKQ";
        accountId = "3964103";
        /////////////////////
        /////////////////////
        /////////////////////



        //
        // The API base path
        String basePath = "https://demo.docusign.net/restapi";

        // Step 1. Call the API
        ApiClient apiClient = new ApiClient(basePath);
        apiClient.addDefaultHeader("Authorization", "Bearer " + accessToken);
        EnvelopesApi envelopesApi = new EnvelopesApi(apiClient);
        // prepare the request body
        ListStatusChangesOptions options = envelopesApi.new ListStatusChangesOptions();
        LocalDate date = LocalDate.now().minusDays(10);
        options.setFromDate(date.toString("yyyy/MM/dd"));
        // call the API
        EnvelopesInformation results = envelopesApi.listStatusChanges(accountId, options);

        // Show results
        String title = "List Updated Envelopes";
        model.addAttribute("title", title);
        model.addAttribute("h1", title);
        model.addAttribute("message", "Envelopes::listStatusChanges results");
        model.addAttribute("json", new JSONObject(results).toString(4));
        return "pages/example_done";
    }


    // Handle get request to show the form
    @RequestMapping(path = "/qs03", method = RequestMethod.GET)
    public String get(ModelMap model) {
        model.addAttribute("title","List Updated Envelopes");
        return "pages/qs03";
    }
}
