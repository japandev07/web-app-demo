package com.webfirmframework.ui.page.component;

import com.webfirmframework.ui.page.common.GlobalSTC;
import com.webfirmframework.ui.page.common.NavigationURI;
import com.webfirmframework.ui.page.css.Bootstrap5CssClass;
import com.webfirmframework.ui.page.model.DocumentModel;
import com.webfirmframework.wffweb.server.page.BrowserPage;
import com.webfirmframework.wffweb.server.page.BrowserPageContext;
import com.webfirmframework.wffweb.tag.html.attribute.AttributeNameConstants;
import com.webfirmframework.wffweb.tag.html.attribute.Name;
import com.webfirmframework.wffweb.tag.html.attribute.Type;
import com.webfirmframework.wffweb.tag.html.attribute.Value;
import com.webfirmframework.wffweb.tag.html.attribute.event.form.OnSubmit;
import com.webfirmframework.wffweb.tag.html.formsandinputs.Button;
import com.webfirmframework.wffweb.tag.html.formsandinputs.Form;
import com.webfirmframework.wffweb.tag.html.formsandinputs.Input;
import com.webfirmframework.wffweb.tag.html.formsandinputs.Label;
import com.webfirmframework.wffweb.tag.html.html5.attribute.Placeholder;
import com.webfirmframework.wffweb.tag.html.stylesandsemantics.Div;
import com.webfirmframework.wffweb.tag.htmlwff.TagContent;
import com.webfirmframework.wffweb.wffbm.data.WffBMByteArray;
import com.webfirmframework.wffwebcommon.TokenUtil;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

public class LoginComponent extends Div {

    private final DocumentModel documentModel;

    public LoginComponent(DocumentModel documentModel) {
        super(null);
        this.documentModel = documentModel;
        GlobalSTC.LOGGER_STC.setContent(
                ZonedDateTime.now(Clock.systemUTC()).format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")) +
                        ":~$ created new LoginComponent");
        develop();
    }

    private void develop() {
        Div msgDiv = new Div(null);

        // using this wff utf-8 encoder is cross browser
        // Eg: var utf8Bytes = wffGlobal.encoder.encode("こんにちは webfirmframework");

        new Form(this, new OnSubmit(true, event -> {

            msgDiv.removeAllChildren();
            msgDiv.removeAttributes(AttributeNameConstants.CLASS);

            String username = (String) event.data().getValue("username");

            //to receive password as byte array instead of string for extra level of security
            WffBMByteArray password = (WffBMByteArray) event.data().getValue("password");

            //to convert UTF-8 byte array to char array
            char[] passwordChars = StandardCharsets.UTF_8.decode(ByteBuffer.wrap(password.toByteArray())).array();

            if ("test".equals(username) && Arrays.equals("test".toCharArray(), passwordChars)) {

                Map<String, Object> payload = Map.of("userId", 5, "username", "test", "role", "user");
                documentModel.session().localStorage().setToken("jwtToken", TokenUtil.createJWT(payload));
                //navigate to user account page on all other opened tabs
                //This works well on multi node mode
                documentModel.browserPage().getTagRepository()
                        .executeJsInOtherBrowserPages(
                                "wffAsync.setURI('%s');".formatted(NavigationURI.USER.getUri(documentModel)));
                //navigate to user account page
                documentModel.browserPage().setURI(NavigationURI.USER.getUri(documentModel));
                return null;
            }

            msgDiv.addAttributes(Bootstrap5CssClass.ALERT_DANGER.getAttribute());
            msgDiv.give(TagContent::text, "Incorrect username or password!");

            return null;
        }, "loadingIcon.hidden = false; return {username: username.value, password: wffGlobal.encoder.encode(password.value)};", "loadingIcon.hidden = true;")).give(form -> {

            new Label(form).give(TagContent::text, "Username: test ");
            new Input(form, new Type(Type.TEXT), new Name("username"), new Placeholder("test"), new Value("test"));

            new Label(form).give(TagContent::text, "Password: test ");
            new Input(form, new Type(Type.PASSWORD), new Name("password"), new Placeholder("test"), new Value("test"));

            new Button(form, new Type(Type.SUBMIT), Bootstrap5CssClass.BTN_SECONDARY.getAttribute()).give(TagContent::text, "Login");


        });

        this.appendChild(msgDiv);

    }


}