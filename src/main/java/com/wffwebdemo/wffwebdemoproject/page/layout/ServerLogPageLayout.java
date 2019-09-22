package com.wffwebdemo.wffwebdemoproject.page.layout;

import java.util.Date;
import java.util.List;

import com.webfirmframework.wffweb.tag.html.AbstractHtml;
import com.webfirmframework.wffweb.tag.html.Body;
import com.webfirmframework.wffweb.tag.html.Html;
import com.webfirmframework.wffweb.tag.html.TitleTag;
import com.webfirmframework.wffweb.tag.html.attribute.global.Style;
import com.webfirmframework.wffweb.tag.html.metainfo.Head;
import com.webfirmframework.wffweb.tag.html.stylesandsemantics.Div;
import com.webfirmframework.wffweb.tag.htmlwff.NoTag;

public class ServerLogPageLayout extends Html {

    private static final long serialVersionUID = 1L;
    
    private Body body;

    public ServerLogPageLayout() {
        super(null);
        super.setPrependDocType(true);
        develop();
    }

    private void develop() {

        new Head(this).give(head -> {
            new TitleTag(head).give(title -> new NoTag(title, "Server Log"));
        });

        body = new Body(this, new Style("background:white"));

    }

    public void log(final String msg) {
        Div logDiv = new Div(null)
                .give(div -> new NoTag(div, "[" + new Date() + "]$ " + msg));

        List<AbstractHtml> children = body.getChildren();
        if (children.size() > 50) {
            body.removeChild(children.get(0));
            body.appendChild(logDiv);
        } else {
            body.appendChild(logDiv);
        }
    }

}
