package com.wffwebdemo.wffwebdemoproject.page.layout;

import java.util.Date;
import java.util.Locale;
import java.util.logging.Logger;

import javax.servlet.http.HttpSession;

import com.webfirmframework.wffweb.server.page.BrowserPage;
import com.webfirmframework.wffweb.tag.html.Body;
import com.webfirmframework.wffweb.tag.html.Br;
import com.webfirmframework.wffweb.tag.html.H4;
import com.webfirmframework.wffweb.tag.html.Html;
import com.webfirmframework.wffweb.tag.html.SharedTagContent.Content;
import com.webfirmframework.wffweb.tag.html.TitleTag;
import com.webfirmframework.wffweb.tag.html.attribute.For;
import com.webfirmframework.wffweb.tag.html.attribute.Href;
import com.webfirmframework.wffweb.tag.html.attribute.Rel;
import com.webfirmframework.wffweb.tag.html.attribute.Src;
import com.webfirmframework.wffweb.tag.html.attribute.Target;
import com.webfirmframework.wffweb.tag.html.attribute.Type;
import com.webfirmframework.wffweb.tag.html.attribute.Value;
import com.webfirmframework.wffweb.tag.html.attribute.event.mouse.OnClick;
import com.webfirmframework.wffweb.tag.html.attribute.global.Dir;
import com.webfirmframework.wffweb.tag.html.attribute.global.Id;
import com.webfirmframework.wffweb.tag.html.attribute.global.Lang;
import com.webfirmframework.wffweb.tag.html.attribute.global.Style;
import com.webfirmframework.wffweb.tag.html.formsandinputs.Button;
import com.webfirmframework.wffweb.tag.html.formsandinputs.Input;
import com.webfirmframework.wffweb.tag.html.formsandinputs.Label;
import com.webfirmframework.wffweb.tag.html.links.A;
import com.webfirmframework.wffweb.tag.html.metainfo.Head;
import com.webfirmframework.wffweb.tag.html.programming.Script;
import com.webfirmframework.wffweb.tag.html.stylesandsemantics.Div;
import com.webfirmframework.wffweb.tag.html.stylesandsemantics.Span;
import com.webfirmframework.wffweb.tag.htmlwff.NoTag;
import com.wffwebdemo.wffwebdemoproject.page.IndexPage;
import com.wffwebdemo.wffwebdemoproject.page.model.DocumentModel;
import com.wffwebdemo.wffwebdemoproject.page.template.LoginTemplate;
import com.wffwebdemo.wffwebdemoproject.page.template.components.SuggestionSearchInput;

public class IndexPageLayout extends Html {
    
    private static final Logger LOGGER = Logger.getLogger(IndexPageLayout.class.getName());

    private static final long serialVersionUID = 1L;

    private TitleTag pageTitle;

    private HttpSession httpSession;
    
//    private List<Runnable> timePrinters;

    private Locale locale;

    private BrowserPage browserPage;    
    
    public IndexPageLayout(HttpSession httpSession, Locale locale, BrowserPage browserPage) {
        super(null);
        this.httpSession = httpSession;
        this.locale = locale;
        this.browserPage = browserPage;
        super.setPrependDocType(true);
        
//        timePrinters = new ArrayList<Runnable>();
        develop();
    }

    @SuppressWarnings("serial")
    private void develop() {

        new Head(this) {
            {
                pageTitle = new TitleTag(this) {
                    {
                        new NoTag(this);
                    }
                };
                new Script(this, new Type(Type.TEXT_JAVASCRIPT), new Src("js/util.js"));
            }
        };

        new Body(this, new Style("background:lightgray")) {

            {
                
                new SuggestionSearchInput(this);
                
                new Br(this);
                new Br(this);
                
                //invokeServerMethod() is defined in js/util.js
                
                new Button(this, new OnClick("invokeServerMethod()")) {
                    {
                        new NoTag(this, "Custom Server Method Invocation");
                    }
                };
                
                new Br(this);
                new Br(this);
                new A(this, new Href("https://github.com/webfirmframework/wffweb-demo-deployment"), new Target(Target.BLANK)) {
                    {
                        new NoTag(this, "Find its code in github");
                    }
                };
                new Br(this);
                new Br(this);
                
                new A(this, new Href("server-log"), new Target(Target.BLANK), new Rel("noopener")) {
                    {
                        new NoTag(this, "view server log");
                    }
                };
                
                new Br(this);
                new Br(this);
                
                new H4(this) {{
                    new NoTag(this, "webfirmframework supports RTL (Right-to-left language), eg Arabic: كيف حالك؟");
                }};
                
                new Div(this) {{
                    new Label(this,
                        new For("rtlTextField")) {{
                        new NoTag(this, "Arabic ");
                    }};
                    new Input(this,
                        new Type(Type.TEXT),
			new Dir(Dir.RTL),
			new Lang("ar"),
                        new Id("rtlTextField"),
                        new Value("كيف حالك؟"));
		}};                
                
                new Br(this);
                
                //to print server time
                new H4(this) {
                    {
                        new NoTag(this, "Server time : ");

                        final Span timeSpan = new Span(this);
                        
                        timeSpan.subscribeTo(IndexPage.CURRENT_DATE_TIME_STC, (content) -> {

                            if (browserPage.getTagRepository().exists(timeSpan)) {
                                LOGGER.info("Server Time " + new Date()
                                      + ", locale " + locale);
                                
                            }
                            return new Content<String>(content.getContent().toString(), false);
                            
                        });

//                        Runnable timePrinter = new Runnable() {
//
//                            @Override
//                            public void run() {
//                                if (browserPage.getTagRepository().exists(timeSpan)) {
//                                    try {
//                                        timeSpan.addInnerHtml(new NoTag(null,
//                                                new Date().toString()));
//                                        LOGGER.info("Server Time " + new Date()
//                                                + ", locale " + locale);
//                                        Thread.sleep(1000);
//                                    } catch (InterruptedException e) {
//                                        e.printStackTrace();
//                                    }
//                                } else {
//                                    try {
//                                        ScheduledThreadPool.NEW_SINGLE_THREAD_SCHEDULED_EXECUTOR.cancel(this, false);
//                                    } catch (IllegalAccessException e) {
//                                        e.printStackTrace();
//                                    }
//                                }
//
//                                
//                            }
//                        };

                        
//                        timePrinters.add(timePrinter);
                    }
                };
                
                
                
                
                new Br(this);
                new Br(this);
                
                new NoTag(this, "Username : demo");
                new Br(this);
                new NoTag(this, "Password : demo");
                
                new Br(this);
                new Br(this);
                
                DocumentModel documentModel = new DocumentModel(browserPage);
                
                Div bodyDiv = new Div(this);
                documentModel.setBodyDiv(bodyDiv);
                
                documentModel.setPageTitle(pageTitle);
                documentModel.setHttpSession(httpSession);
                
                
                bodyDiv.appendChild(new LoginTemplate(documentModel));
                
            }

        };

    }
    
//    public List<Runnable> getTimers() {
//        return timePrinters;
//    }

}
