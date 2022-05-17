package com.webfirmframework.ui.page.layout;

import com.webfirmframework.ui.page.common.NavigationURI;
import com.webfirmframework.ui.page.component.LoginComponent;
import com.webfirmframework.ui.page.component.RealtimeServerLogComponent;
import com.webfirmframework.ui.page.component.UserAccountComponent;
import com.webfirmframework.ui.page.model.DocumentModel;
import com.webfirmframework.wffweb.server.page.BrowserPage;
import com.webfirmframework.wffweb.server.page.BrowserPageSession;
import com.webfirmframework.wffweb.server.page.LocalStorage;
import com.webfirmframework.wffweb.tag.html.*;
import com.webfirmframework.wffweb.tag.html.attribute.*;
import com.webfirmframework.wffweb.tag.html.attribute.global.ClassAttribute;
import com.webfirmframework.wffweb.tag.html.attribute.global.Id;
import com.webfirmframework.wffweb.tag.html.attributewff.CustomAttribute;
import com.webfirmframework.wffweb.tag.html.html5.attribute.Content;
import com.webfirmframework.wffweb.tag.html.html5.attribute.global.Hidden;
import com.webfirmframework.wffweb.tag.html.links.Link;
import com.webfirmframework.wffweb.tag.html.metainfo.Head;
import com.webfirmframework.wffweb.tag.html.metainfo.Meta;
import com.webfirmframework.wffweb.tag.html.programming.Script;
import com.webfirmframework.wffweb.tag.html.stylesandsemantics.Div;
import com.webfirmframework.wffweb.tag.html.stylesandsemantics.Span;
import com.webfirmframework.wffweb.tag.htmlwff.NoTag;
import com.webfirmframework.wffweb.tag.htmlwff.TagContent;
import com.webfirmframework.wffwebcommon.TokenUtil;

import java.util.logging.Logger;

public class IndexPageLayout extends Html {

    private static final Logger LOGGER = Logger
            .getLogger(IndexPageLayout.class.getName());

    private final DocumentModel documentModel;

    private final String contextPath;

    private Div mainDiv;

    // no need use volatile modifier, the framework internally handles it unless
    // the value is assigned via a custom thread.
    private AbstractHtml componentDivCurrentChild;

    public IndexPageLayout(BrowserPage browserPage, BrowserPageSession session, String contextPath) {
        super(null);
        super.setPrependDocType(true);
        this.documentModel = new DocumentModel(session, browserPage, contextPath);
        super.setSharedData(documentModel);
        this.contextPath = contextPath;
        develop();
    }

    // @formatter:off
    private void develop() {


        new Head(this).give(head -> {
            new TitleTag(head).give(TagContent::text, "wffweb with bootstrap 5 css example");
            new Meta(head,
                    new Charset("utf-8"));
            new Meta(head,
                    new Name("viewport"),
                    new Content("width=device-width, initial-scale=1"));

            new Link(head,
                    new Href("https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css"),
                    new Rel("stylesheet"),
                    new CustomAttribute("integrity", "sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3"),
                    new CustomAttribute("crossorigin", "anonymous"));
            new Link(head,
                    new Rel(Rel.STYLESHEET),
                    new Href(contextPath + "/assets/css/app.css"));


            new Script(head,
                    new Defer(),
                    new Src(contextPath + "https://www.gstatic.com/charts/loader.js"));

            new Script(head,
                    new Defer(),
                    new Src(contextPath + "/assets/js/app.js"));

        });

        new Body(this).give(body -> {

            mainDiv = new Div(body, new Id("mainDivId")).give(div -> {
                new NoTag(div, "Loading...");
            });

            new Script(body,
                    new Src("https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"),
                    new CustomAttribute("integrity", "sha384-ka7Sk0Gln4gmtz2MlQnikT1wXgYsOg+OMhuP+IlRH9sENBO0LRn5q+8nbTov4+1p"),
                    new CustomAttribute("crossorigin", "anonymous"));

        });

    }

    public void buildMainDivTags() {

        mainDiv.removeAllChildren();
        //common progress icon
        new Div(mainDiv, new Hidden(), new Id("loadingIcon"), new ClassAttribute("spinner-border text-primary"), new Role(Role.STATUS)).give(tag -> {
            new Span(tag, new ClassAttribute("visually-hidden")).give(TagContent::text, "Loading...");
        });

        URIStateSwitch componentDiv = new Div(mainDiv);

        componentDiv.whenURI(NavigationURI.LOGIN.getPredicate(documentModel),
                () -> {
                    if (!(componentDivCurrentChild instanceof LoginComponent)) {
                        componentDivCurrentChild = new LoginComponent(documentModel);
                    }
                    documentModel.browserPage().getTagRepository().findTitleTag().give(
                            TagContent::text, "Login | wffweb demo");
                    return new AbstractHtml[]{componentDivCurrentChild};
                });

        componentDiv.whenURI(NavigationURI.REALTIME_SERVER_LOG.getPredicate(documentModel),
                () -> {
                    documentModel.browserPage().getTagRepository().findTitleTag().give(
                            TagContent::text, "Server Log | User Account | wffweb demo");
                    if (!(componentDivCurrentChild instanceof RealtimeServerLogComponent)) {
                        componentDivCurrentChild = new RealtimeServerLogComponent();
                    }
                    return new AbstractHtml[]{componentDivCurrentChild};
                });

        componentDiv.whenURI(NavigationURI.USER.getPredicate(documentModel),
                () -> {
                    documentModel.browserPage().getTagRepository().findTitleTag().give(
                            TagContent::text, "User Account | wffweb demo");
                    if (!(componentDivCurrentChild instanceof UserAccountComponent)) {
                        componentDivCurrentChild = new UserAccountComponent(documentModel);
                    }
                    return new AbstractHtml[]{componentDivCurrentChild};
                },
                event -> {

                    LocalStorage.Item token = documentModel.session().localStorage().getToken("jwtToken");
                    //if already logged in then navigate to user account page otherwise navigate to login page
                    if (TokenUtil.isValidJWT(token)) {
                        documentModel.browserPage().setURI(NavigationURI.USER.getUri(documentModel));
                    } else {
                        documentModel.browserPage().setURI(NavigationURI.LOGIN.getUri(documentModel));
                    }

                });
    }

    // @formatter:on

}