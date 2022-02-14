package com.webfirmframework.ui.page.component;

import com.webfirmframework.ui.page.common.GlobalSTC;
import com.webfirmframework.ui.page.model.DocumentModel;
import com.webfirmframework.wffweb.tag.html.H1;
import com.webfirmframework.wffweb.tag.html.H2;
import com.webfirmframework.wffweb.tag.html.SharedTagContent;
import com.webfirmframework.wffweb.tag.html.stylesandsemantics.Div;
import com.webfirmframework.wffweb.tag.htmlwff.TagContent;

import java.time.Clock;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class RealtimeClock extends Div {

    private final DocumentModel documentModel;

    public RealtimeClock(DocumentModel documentModel) {
        super(null);
        this.documentModel = documentModel;
        GlobalSTC.LOGGER_STC.setContent(
                ZonedDateTime.now(Clock.systemUTC()).format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")) +
                        ":~$ created new RealtimeClock");
        develop();
    }

    private void develop() {
        new H1(this).give(TagContent::text, "Server time in realtime: ");
        new H2(this).subscribeTo(GlobalSTC.CLOCK,
                content -> new SharedTagContent.Content<>(content.content().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")), content.contentTypeHtml()));
    }
}
