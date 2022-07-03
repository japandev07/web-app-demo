package com.webfirmframework.ui.page.component;

import com.webfirmframework.ui.page.common.GlobalSTC;
import com.webfirmframework.ui.page.css.Bootstrap5CssClass;
import com.webfirmframework.ui.page.model.DocumentModel;
import com.webfirmframework.wffweb.tag.html.Br;
import com.webfirmframework.wffweb.tag.html.attribute.Name;
import com.webfirmframework.wffweb.tag.html.attribute.Type;
import com.webfirmframework.wffweb.tag.html.attribute.event.form.OnSubmit;
import com.webfirmframework.wffweb.tag.html.attribute.global.Id;
import com.webfirmframework.wffweb.tag.html.formatting.Pre;
import com.webfirmframework.wffweb.tag.html.formsandinputs.Button;
import com.webfirmframework.wffweb.tag.html.formsandinputs.Form;
import com.webfirmframework.wffweb.tag.html.formsandinputs.Input;
import com.webfirmframework.wffweb.tag.html.formsandinputs.Label;
import com.webfirmframework.wffweb.tag.html.stylesandsemantics.Div;
import com.webfirmframework.wffweb.tag.htmlwff.TagContent;
import com.webfirmframework.wffwebcommon.FileUtil;
import com.webfirmframework.wffwebcommon.UploadedFilesData;
import com.webfirmframework.wffwebconfig.server.constants.ServerConstants;
import jakarta.servlet.http.Part;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

public class SampleFilesUploadComponent extends Div {

    public static final String FILE_UPLOAD_SERVER_METHOD = "fileUploadServerMethod";

    private final DocumentModel documentModel;

    public SampleFilesUploadComponent(DocumentModel documentModel) {
        super(null, Bootstrap5CssClass.CONTAINER.getAttribute());
        this.documentModel = documentModel;
        GlobalSTC.LOGGER_STC.setContent(
                ZonedDateTime.now(Clock.systemUTC()).format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")) +
                        ":~$ created new " + getClass().getSimpleName());

        develop();
    }

    private void develop() {

        final String fileSecretKey = documentModel.session().userProperties().computeIfAbsent("fileSecretKey", k -> UUID.randomUUID().toString()).toString();

        new Br(this);
        new Br(this);
        Div displayMsgDiv = new Div(null);



        documentModel.browserPage().addServerMethod(FILE_UPLOAD_SERVER_METHOD, event -> {

            displayMsgDiv.removeAllChildren();

            UploadedFilesData uploadedFilesData = (UploadedFilesData) event.recordData();

            System.out.println("recordData = " + uploadedFilesData);

            String[] fullNames = uploadedFilesData.requestParamMap().get("fullName");
            String fullName = fullNames != null && fullNames.length > 0 ? fullNames[0] : null;
            System.out.println("fullName = " + fullName);

            displayMsgDiv.appendChild(new Pre(null).give(TagContent::text, "Full Name: " + fullName));

            List<Part> parts = uploadedFilesData.parts();
            for (Part part : parts) {
                try (InputStream fileContent = part.getInputStream();) {

                    long fileSize = part.getSize();

                    String fileName = FileUtil.getFileName(part);


                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    int read = 0;
                    final byte[] bytes = new byte[1024];
                    long totalLength = 0;
                    while ((read = fileContent.read(bytes)) != -1) {
                        outputStream.write(bytes, 0, read);
                        totalLength += read;

                        long percent = (totalLength * 100 / fileSize);
//                            progressBarHolder.get().addAttributes(new Style("width: " + percent + "%"),
//                                    new CustomAttribute("aria-valuenow", "0"));

                    }


                    try {
                        // remove this line if the content is binary
                        String fileContentString = outputStream.toString(StandardCharsets.UTF_8);
                        System.out.println(fileName + " contains: " + fileContentString);

                        displayMsgDiv.appendChild(new Pre(null).give(TagContent::text, fileName + " contains:\n" + fileContentString));
                    } catch (Exception e) {
                        e.printStackTrace();
                        displayMsgDiv.appendChild(new Pre(null).give(TagContent::text, fileName + " is a binary file"));
                    }


                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }


            return null;
        });

        final String fileUploadURL = ServerConstants.DOMAIN_URL + documentModel.contextPath() + ServerConstants.FILE_UPLOAD_URI;
        new Form(this, new Id("fileUploadForm"), new OnSubmit(true, """
                loadingIcon.hidden = false;
                fileUploadSubmitBtn.setAttribute('disabled', 'disabled');
                var formData = new FormData();
                formData.append('inputFile1', inputFile1.files[0]);
                formData.append('inputFile2', inputFile2.files[0]);
                formData.append('fullName', fullName.value);
                formData.append('sessionId', '%s');
                formData.append('instanceId', '%s');
                formData.append('serverMethod', '%s');
                formData.append('fileSecretKey', '%s');
                $.ajax({
                  url: "%s",
                  type: 'post',
                  data: formData,
                  contentType: false,
                  processData: false,
                  success: function( result ) {
                    console.log('upload result', result);                       
                    if (result.status === 'success') {
                        console.log('upload success');
                        action.perform();                            
                    } else {
                        fileUploadSubmitBtn.removeAttribute('disabled');
                    }
                  }
                });
                return false;
                """.formatted(documentModel.session().id(),
                documentModel.browserPage().getInstanceId(),
                FILE_UPLOAD_SERVER_METHOD,
                fileSecretKey,
                fileUploadURL).stripIndent(),
                event -> {


                    System.out.println("event.data().getValue(fullName) = " + event.data().getValue("fullName"));


                    return null;
                }, "return {fullName: fullName.value};",
                "fileUploadForm.reset(); fileUploadSubmitBtn.removeAttribute('disabled'); loadingIcon.hidden = true;")).give(form -> {

            new Div(form, Bootstrap5CssClass.FORM_GROUP.getAttribute()).give(dv -> {
                new Label(dv).give(TagContent::text, "Full Name");
                new Input(dv, new Type(Type.TEXT), new Name("fullName"), Bootstrap5CssClass.FORM_CONTROL.getAttribute());
            });

            new Div(form, Bootstrap5CssClass.FORM_GROUP.getAttribute()).give(dv -> {
                new Label(dv).give(TagContent::text, "Input File 1");
                new Input(dv, new Type(Type.FILE), new Name("inputFile1"), Bootstrap5CssClass.FORM_CONTROL.getAttribute());
            });

            new Div(form, Bootstrap5CssClass.FORM_GROUP.getAttribute()).give(dv -> {
                new Label(dv).give(TagContent::text, "Input File 2");
                new Input(dv, new Type(Type.FILE), new Name("inputFile2"), Bootstrap5CssClass.FORM_CONTROL.getAttribute());
            });

            new Button(form, new Type(Type.SUBMIT), new Id("fileUploadSubmitBtn"), Bootstrap5CssClass.BTN_PRIMARY.getAttribute()).give(TagContent::text, "Upload file");
        });


        this.appendChild(displayMsgDiv);

    }
}
