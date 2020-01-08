package com.docusign.controller;

import com.auth0.jwt.algorithms.Algorithm;
import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.client.ApiClient;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.*;
import com.docusign.esign.model.Signer;
import com.sun.jersey.core.util.Base64;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
//import org.bouncycastle.util.io.pem.PemReader;
//import org.bouncycastle.util.io.pem.PemReader;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.springframework.http.HttpMethod;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.view.RedirectView;

import java.io.*;
import java.math.BigInteger;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.Instant;
import java.time.temporal.IsoFields;
import java.time.temporal.TemporalUnit;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.regex.Pattern;

///////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////
//
// Java Quickstart example: Create an envelope and sign it with an embedded
// Signing Ceremony
//
// Copyright (c) 2018 by DocuSign, Inc.
// License: The MIT License -- https://opensource.org/licenses/MIT
//
///////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////

@Controller
public class QS01EmbedSigningCeremonyController {


    @RequestMapping(path = "/qs01", method = RequestMethod.GET)


//    public Object create(@RegisteredOAuth2AuthorizedClient("docusign-client") OAuth2AuthorizedClient docusignAuthClient, ModelMap model) throws ApiException, IOException {
    public Object create(ModelMap model) throws ApiException, IOException, NoSuchAlgorithmException, InvalidKeySpecException {

        Security.addProvider(new BouncyCastleProvider());

        URL url = getClass().getResource("/private.txt");
        InputStream in = url.openStream();
        var pemParser = new PEMParser(new InputStreamReader(in));
        var pemKeypair = PEMKeyPair.class.cast(pemParser.readObject());
        var converter = new JcaPEMKeyConverter().setProvider("BC");
        var keyPair = converter.getKeyPair(pemKeypair);

        var jwt = Jwts.builder()
                .setIssuer("469774cd-29e0-44b6-8897-7245d88386ea")
                .setSubject("288d2ef2-8c7e-421a-ba63-34d316a05484")
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plusSeconds(60 * 60)))
                .setAudience("account-d.docusign.com")
                .claim("scope", "signature impersonation")
                .setHeaderParam("typ", Header.JWT_TYPE)
                .setHeaderParam("alg", "RSA256")
                .signWith(SignatureAlgorithm.RS256, keyPair.getPrivate())
                .compact();

        var rest = new RestTemplate();
        var accessToken = rest.exchange("https://account-d.docusign.com/oauth/token?grant_type={grant_type}&assertion={assertion}",
                HttpMethod.POST,
                null,
                AccessTokenResponse.class,
                Map.of("grant_type","urn:ietf:params:oauth:grant-type:jwt-bearer",
                        "assertion",jwt)).getBody().access_token;

        model.addAttribute("title","Embedded Signing Ceremony");

        // Data for this example
        // Fill in these constants
        //
        // Obtain an OAuth access token from https://developers.docusign.com/oauth-token-generator
//        String accessToken = "";
//        String accessToken = "";
        // Obtain your accountId from demo.docusign.com -- the account id is shown in the drop down on the
        // upper right corner of the screen by your picture or the default picture.
        String accountId = "9654515";
        // Recipient Information
        String signerName = "Andrew";
        String signerEmail = "myemail@pivotal.io";

        // The url for this web application
        String baseUrl = "http://localhost:8080";
        String clientUserId = "123"; // Used to indicate that the signer will use an embedded
        // Signing Ceremony. Represents the signer's userId within
        // your application.
        String authenticationMethod = "None"; // How is this application authenticating
        // the signer? See the `authenticationMethod' definition
        //  https://developers.docusign.com/esign-rest-api/reference/Envelopes/EnvelopeViews/createRecipient
        //
        // The API base path
        String basePath = "https://demo.docusign.net/restapi";
        // The document to be signed. See /qs-java/src/main/resources/World_Wide_Corp_lorem.pdf
        String docPdf = "World_Wide_Corp_lorem.pdf";

        // Step 1. Create the envelope definition
        // One "sign here" tab will be added to the document.

        byte[] buffer = readFile(docPdf);
        String docBase64 = new String(Base64.encode(buffer));

        // Create the DocuSign document object
        Document document = new Document();
        document.setDocumentBase64(docBase64);
        document.setName("Example document"); // can be different from actual file name
        document.setFileExtension("pdf"); // many different document types are accepted
        document.setDocumentId("1"); // a label used to reference the doc

        // The signer object
        // Create a signer recipient to sign the document, identified by name and email
        // We set the clientUserId to enable embedded signing for the recipient
        Signer signer = new Signer();
        signer.setEmail(signerEmail);
        signer.setName(signerName);
        signer.clientUserId(clientUserId);
        signer.recipientId("1");

        // Create a signHere tabs (also known as a field) on the document,
        // We're using x/y positioning. Anchor string positioning can also be used
        SignHere signHere = new SignHere();
        signHere.setDocumentId("1");
        signHere.setPageNumber("1");
        signHere.setRecipientId("1");
        signHere.setTabLabel("SignHereTab");
        signHere.setXPosition("195");
        signHere.setYPosition("147");

        // Add the tabs to the signer object
        // The Tabs object wants arrays of the different field/tab types
        Tabs signerTabs = new Tabs();
        signerTabs.setSignHereTabs(Arrays.asList(signHere));
        signer.setTabs(signerTabs);

        // Next, create the top level envelope definition and populate it.
        EnvelopeDefinition envelopeDefinition = new EnvelopeDefinition();
        envelopeDefinition.setEmailSubject("Please sign this document");
        envelopeDefinition.setDocuments(Arrays.asList(document));
        // Add the recipient to the envelope object
        Recipients recipients = new Recipients();
        recipients.setSigners(Arrays.asList(signer));
        envelopeDefinition.setRecipients(recipients);
        envelopeDefinition.setStatus("sent"); // requests that the envelope be created and sent.

        // Step 2. Call DocuSign to create and send the envelope
        ApiClient apiClient = new ApiClient(basePath);
        apiClient.addDefaultHeader("Authorization", "Bearer " + accessToken);
        EnvelopesApi envelopesApi = new EnvelopesApi(apiClient);
        EnvelopeSummary results = envelopesApi.createEnvelope(accountId, envelopeDefinition);
        String envelopeId = results.getEnvelopeId();

        // Step 3. The envelope has been created.
        //         Request a Recipient View URL (the Signing Ceremony URL)
        RecipientViewRequest viewRequest = new RecipientViewRequest();
        // Set the url where you want the recipient to go once they are done signing
        // should typically be a callback route somewhere in your app.
        viewRequest.setReturnUrl(baseUrl + "/ds-return");
        viewRequest.setAuthenticationMethod(authenticationMethod);
        viewRequest.setEmail(signerEmail);
        viewRequest.setUserName(signerName);
        viewRequest.setClientUserId(clientUserId);
        // call the CreateRecipientView API
        ViewUrl results1 = envelopesApi.createRecipientView(accountId, envelopeId, viewRequest);

        // Step 4. The Recipient View URL (the Signing Ceremony URL) has been received.
        //         The user's browser will be redirected to it.
        String redirectUrl = results1.getUrl();
        RedirectView redirect = new RedirectView(redirectUrl);
        redirect.setExposeModelAttributes(false);
        return redirect;
    }

    private byte[] loadPEM(String resource) throws IOException {


        URL url = getClass().getResource(resource);
        InputStream in = url.openStream();

        String pem = new String(in.readAllBytes(), StandardCharsets.ISO_8859_1);
        Pattern parse = Pattern.compile("(?m)(?s)^---*BEGIN.*---*$(.*)^---*END.*---*$.*");
        String encoded = parse.matcher(pem).replaceFirst("$1").trim();
        return Base64.decode(encoded);
    }



    // Read a file
    private byte[] readFile(String path) throws IOException {
        InputStream is = QS01EmbedSigningCeremonyController.class.getResourceAsStream("/" + path);
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int nRead;
        byte[] data = new byte[1024];
        while ((nRead = is.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }
        buffer.flush();
        return buffer.toByteArray();
    }


}
