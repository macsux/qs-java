package com.docusign.controller.examples;

import com.docusign.esign.client.ApiException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Controller
public abstract class EGController {
    private String message;

    @Autowired
    protected HttpSession session;

    // Base method for the example GET requests which show the example's
    // form page
    @RequestMapping(method = RequestMethod.GET)
    public String get(ModelMap model, HttpSession session, HttpServletRequest request) {
        model.addAttribute("csrfToken", "");
        model.addAttribute("title", getTitle());
        addSpecialAttributes(model);

        return "pages/examples/" + getEgName();
    }

    protected abstract void addSpecialAttributes(ModelMap model);

    // Base method for POST requests to run an example
    @RequestMapping(method = RequestMethod.POST)
    public Object create(WorkArguments args,
                         ModelMap model,
                         HttpSession session,
                         @RequestBody MultiValueMap<String, String> formParams,
                         HttpServletResponse response) throws IOException, ApiException {

        try {
            Object result = doWork(args, model);
            String redirectUrl = args.getRedirectUrl();
            Boolean externalRedirect = redirectUrl != null && redirectUrl.indexOf("redirect:") == 0;
            if (externalRedirect) {
                String url = redirectUrl.substring(9); // strip 'redirect:'
                RedirectView redirect = new RedirectView(url);
                redirect.setExposeModelAttributes(false);
                return redirect;
            } else if (redirectUrl != null) {
                // show a generic template
                postWork(result, model);
                return redirectUrl;
            } else {
                // download logic
                JSONObject r = (JSONObject) result;

                byte[] buffer = (byte[]) r.get("fileBytes");

                response.setContentType(r.getString("mimetype"));
                response.setContentLength(buffer.length);
                response.setHeader("Content-disposition", "inline;filename=" + r.getString("docName"));

                response.getOutputStream().write(buffer);
                response.flushBuffer();
                return null;
            }

        } catch (Exception e) {
            populateErrorModel(model, e);
            throw new RuntimeException(e);
        }
    }


    protected void postWork(Object result, ModelMap model) {

        String title = getResponseTitle();
        String message = getMessage();
        model.addAttribute("title", title);
        model.addAttribute("h1", title);
        model.addAttribute("message", message);
        model.addAttribute("json",
                (result != null) ? new JSONObject(result).toString(4) : null);
    }


    protected void populateErrorModel(ModelMap model, Exception e) {
        model.addAttribute("err", e);
        model.addAttribute("errorCode", e.getCause());
        model.addAttribute("errorMessage", e.getMessage());
    }

    protected abstract String getEgName();

    protected abstract String getTitle();

    protected abstract String getResponseTitle();

    protected byte[] readFile(String path) throws IOException {
        InputStream is = EGController.class.getResourceAsStream("/" + path);

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        int nRead;

        byte[] data = new byte[1024];

        while ((nRead = is.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }

        buffer.flush();
        return buffer.toByteArray();
    }

    protected abstract Object doWork(WorkArguments args, ModelMap model) throws ApiException, IOException;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
