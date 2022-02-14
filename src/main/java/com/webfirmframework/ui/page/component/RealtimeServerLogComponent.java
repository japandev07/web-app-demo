package com.webfirmframework.ui.page.component;

import com.webfirmframework.ui.page.common.GlobalSTC;
import com.webfirmframework.wffweb.tag.html.AbstractHtml;
import com.webfirmframework.wffweb.tag.html.H1;
import com.webfirmframework.wffweb.tag.html.html5.attribute.global.Hidden;
import com.webfirmframework.wffweb.tag.html.stylesandsemantics.Div;
import com.webfirmframework.wffweb.tag.html.stylesandsemantics.Span;
import com.webfirmframework.wffweb.tag.htmlwff.TagContent;

import java.time.Clock;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class RealtimeServerLogComponent extends Div {

    public RealtimeServerLogComponent() {
        super(null);
        GlobalSTC.LOGGER_STC.setContent(
                ZonedDateTime.now(Clock.systemUTC()).format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")) +
                        ":~$ created new RealtimeServerLogComponent");
        develop();
    }

    private void develop() {

        new H1(this).give(TagContent::text, "Server Log (50 only)");
        Div logDiv = new Div(this);

        //hidden div just to listen to logs
        new Span(this, new Hidden()).subscribeTo(GlobalSTC.LOGGER_STC, content -> {

            AbstractHtml log = new Div(null).give(TagContent::text, content.content());
            logDiv.appendChild(log);

            //to show only last 50 logs
            if (logDiv.getChildrenSize() > 50) {
                logDiv.removeChild(logDiv.getFirstChild());
            }

            //we don't need to write anything to this span so returning null
            return null;
        });

    }
}
