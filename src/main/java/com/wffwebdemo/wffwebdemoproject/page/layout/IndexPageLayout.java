package com.wffwebdemo.wffwebdemoproject.page.layout;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import com.webfirmframework.wffweb.tag.html.Body;
import com.webfirmframework.wffweb.tag.html.Br;
import com.webfirmframework.wffweb.tag.html.Html;
import com.webfirmframework.wffweb.tag.html.TitleTag;
import com.webfirmframework.wffweb.tag.html.attribute.Href;
import com.webfirmframework.wffweb.tag.html.attribute.Target;
import com.webfirmframework.wffweb.tag.html.attribute.global.Style;
import com.webfirmframework.wffweb.tag.html.links.A;
import com.webfirmframework.wffweb.tag.html.metainfo.Head;
import com.webfirmframework.wffweb.tag.html.stylesandsemantics.Div;
import com.webfirmframework.wffweb.tag.htmlwff.NoTag;
import com.wffwebdemo.wffwebdemoproject.page.model.DocumentModel;
import com.wffwebdemo.wffwebdemoproject.page.template.LoginTemplate;

public class IndexPageLayout extends Html {

    private static final long serialVersionUID = 1L;

    private TitleTag pageTitle;

    private HttpSession httpSession;
    
    private List<Thread> allThreads;

    public IndexPageLayout(HttpSession httpSession) {
        super(null);
        this.httpSession = httpSession;
        super.setPrependDocType(true);
        
        allThreads = new ArrayList<Thread>();
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
            }
        };

        new Body(this, new Style("background:lightgray")) {

            {
                new Br(this);
                new Br(this);
                new A(this, new Href("https://github.com/webfirmframework/wffweb-demo-deployment"), new Target(Target.BLANK)) {
                    {
                        new NoTag(this, "Find its code in github");
                    }
                };
                new Br(this);
                new Br(this);
                
                new A(this, new Href("server-log"), new Target(Target.BLANK)) {
                    {
                        new NoTag(this, "view server log");
                    }
                };
                
                
                new Br(this);
                new Br(this);
                

                final Div timeDiv = new Div(this);
                
                Thread thread = new Thread(new Runnable() {
                    
                    @Override
                    public void run() {
                        while (!Thread.interrupted()) {
                            try {
                                timeDiv.addInnerHtml(new NoTag(null, new Date().toString()));
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                break;
                            }
                        }
                        
                    }
                });
                
                thread.setDaemon(true);
                
                allThreads.add(thread);
                thread.start();
                
                
                
                new Br(this);
                new Br(this);
                
                new NoTag(this, "Username : demo");
                new Br(this);
                new NoTag(this, "Password : demo");
                
                new Br(this);
                new Br(this);
                
                DocumentModel documentModel = new DocumentModel();
                
                Div bodyDiv = new Div(this);
                documentModel.setBodyDiv(bodyDiv);
                
                documentModel.setPageTitle(pageTitle);
                documentModel.setHttpSession(httpSession);
                
                
                bodyDiv.appendChild(new LoginTemplate(documentModel));
                
            }

        };

    }
    
    public List<Thread> getAllThreads() {
        return allThreads;
    }

}
