package com.wffwebdemo.wffwebdemoproject.page.template.users;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import com.webfirmframework.wffweb.server.page.BrowserPageContext;
import com.webfirmframework.wffweb.tag.html.AbstractHtml;
import com.webfirmframework.wffweb.tag.html.Br;
import com.webfirmframework.wffweb.tag.html.attribute.Type;
import com.webfirmframework.wffweb.tag.html.attribute.event.ServerAsyncMethod;
import com.webfirmframework.wffweb.tag.html.attribute.event.form.OnChange;
import com.webfirmframework.wffweb.tag.html.attribute.event.mouse.OnClick;
import com.webfirmframework.wffweb.tag.html.attribute.global.Id;
import com.webfirmframework.wffweb.tag.html.attribute.global.Style;
import com.webfirmframework.wffweb.tag.html.formatting.B;
import com.webfirmframework.wffweb.tag.html.formsandinputs.Button;
import com.webfirmframework.wffweb.tag.html.formsandinputs.Input;
import com.webfirmframework.wffweb.tag.html.html5.attribute.Max;
import com.webfirmframework.wffweb.tag.html.programming.Script;
import com.webfirmframework.wffweb.tag.html.stylesandsemantics.Div;
import com.webfirmframework.wffweb.tag.html.stylesandsemantics.Span;
import com.webfirmframework.wffweb.tag.html.stylesandsemantics.StyleTag;
import com.webfirmframework.wffweb.tag.html.tables.TBody;
import com.webfirmframework.wffweb.tag.html.tables.Table;
import com.webfirmframework.wffweb.tag.html.tables.Td;
import com.webfirmframework.wffweb.tag.html.tables.Th;
import com.webfirmframework.wffweb.tag.html.tables.Tr;
import com.webfirmframework.wffweb.tag.htmlwff.NoTag;
import com.webfirmframework.wffweb.wffbm.data.WffBMObject;
import com.wffwebdemo.wffwebdemoproject.page.ServerLogPage;
import com.wffwebdemo.wffwebdemoproject.page.model.DocumentModel;

@SuppressWarnings("serial")
public class ListUsersTempate extends Div implements ServerAsyncMethod {

    private static final Logger LOGGER = Logger
            .getLogger(ListUsersTempate.class.getName());

    private TBody tBody;

    private DocumentModel documentModel;

    private int rowCount = 0;

    private Input noOfRowsInput;

    private Button addNewRowOnTopButton;

    private Button nextRowsButton;

    private Button lazyNextRowsButton;

    private Button markGreenButton;

    private Button markVioletButton;

    private Button removeColoumnStyleButton;

    private Button removeColoumnStyleOneByOneButton;

    private Style countryColumnStyle;
    
    private int noOfRows = 1;
    
    private static final ExecutorService EXECUTOR_SERVICE = Executors.newCachedThreadPool();

    public ListUsersTempate(DocumentModel documentModel) {
        super(null);
        this.documentModel = documentModel;

        countryColumnStyle = new Style("background:yellow;color:orange");

        develop();
    }

    private void develop() {
        new StyleTag(this) {
            {
                new NoTag(this, "table {\n    font-family: arial, sans-serif;");
                new NoTag(this, "border-collapse: collapse;\n    width: 100%;");
                new NoTag(this, "}\n\ntd, th {");
                new NoTag(this,
                        "border: 1px solid #dddddd;\n    text-align: left;");
                new NoTag(this, "padding: 8px;\n}");
                new NoTag(this,
                        "tr:nth-child(even) {\n    background-color: #dddddd;");
                new NoTag(this, "}");
            }
        };

        // this way also you can execute JavaScript
        // check browser console when clicking on list users button
        new Script(this, new Type(Type.TEXT_JAVASCRIPT)) {
            {
                new NoTag(this, "console.log('list users template is added');");
            }
        };

        new Div(this) {
            {
                new NoTag(null, "Users list");
            }
        };

        new Br(this);
        new Br(this);

        addNewRowOnTopButton = new Button(this, new OnClick(this)) {
            {
                new B(this) {
                    {
                        new NoTag(this, "Add Rows On Top");
                    }
                };
            }
        };

        new Span(this) {{
            new NoTag(this, "    =   > ");
        }};

        noOfRowsInput = new Input(this, new Id("noOfRowsInput"), new Type(Type.NUMBER), new Max("1000"),
                new OnChange("return true;", this, "return {noOfRows:document.getElementById('noOfRowsInput').value};", null));

        new Br(this);
        new Br(this);

        nextRowsButton = new Button(this, new OnClick(this)) {
            {
                new B(this) {
                    {
                        new NoTag(this, "Next 25 rows");
                    }
                };
            }
        };

        new Br(this);
        new Br(this);

        lazyNextRowsButton = new Button(this, new OnClick(this)) {
            {
                new B(this) {
                    {
                        new NoTag(this, "Next 1000 rows as stream");
                    }
                };
            }
        };

        new Br(this);
        new Br(this);

        markGreenButton = new Button(this, new OnClick(this)) {
            {
                new B(this) {
                    {
                        new NoTag(this, "Mark column green");
                    }
                };
            }
        };

        new Br(this);
        new Br(this);

        markVioletButton = new Button(this, new OnClick(this)) {
            {
                new B(this) {
                    {
                        new NoTag(this, "Mark column violet");
                    }
                };
            }
        };

        new Br(this);
        new Br(this);

        removeColoumnStyleButton = new Button(this, new OnClick(this)) {
            {
                new B(this) {
                    {
                        new NoTag(this,
                                "Remove style from column all together");
                    }
                };
            }
        };

        new Br(this);
        new Br(this);

        removeColoumnStyleOneByOneButton = new Button(this, new OnClick(this)) {
            {
                new B(this) {
                    {
                        new NoTag(this,
                                "Remove style attribute from column one by one");
                    }
                };
            }
        };

        new Br(this);
        new Br(this);

        new Table(this) {
            {
                tBody = new TBody(this) {
                    {
                        new Tr(this) {
                            {
                                new Th(this) {
                                    {
                                        new NoTag(this, "Company (会社)");
                                    }
                                };
                                new Th(this) {
                                    {
                                        new NoTag(this, "Contact (接触)");
                                    }
                                };
                                new Th(this) {
                                    {
                                        new NoTag(this, "Country (国)");
                                    }
                                };
                                new Th(this) {
                                    {
                                        new NoTag(this, "Action");
                                    }
                                };
                            }
                        };

                    }
                };
            }
        };
        // initially add rows
        addRows(false, 25);
    }

    private void addRows(final boolean onTop, int howMany) {

        List<AbstractHtml> rows = new LinkedList<AbstractHtml>();

        for (int i = 0; i < howMany; i++) {

            final Tr tr = new Tr(null) {
                {
                    rowCount++;

                    new Td(this) {
                        {
                            new NoTag(this, "Alfreds Futterkiste " + rowCount);
                        }
                    };
                    new Td(this) {
                        {
                            new NoTag(this, "Maria Anders " + rowCount);
                        }
                    };
                    new Td(this, countryColumnStyle) {
                        {
                            new NoTag(this, "Germany " + rowCount);
                        }
                    };
                }
            };

            final int rowCount = this.rowCount;
            // as java 8 syntax, lambda expression way
            final OnClick deleteClick = new OnClick((wffBMObject, event) -> {
                AbstractHtml parentOfTr = tr.getParent();
                parentOfTr.removeChild(tr);
                rows.remove(tr);
                displayInServerLogPage("Removed row no " + rowCount);
                return null;
            });

            // as java 7 syntax
            // final OnClick deleteClick = new OnClick(new ServerAsyncMethod() {
            // @Override
            // public WffBMObject asyncMethod(WffBMObject wffBMObject, Event
            // event) {
            // AbstractHtml parentOfTr = tr.getParent();
            // parentOfTr.removeChild(tr);
            // rows.remove(tr);
            // displayInServerLogPage("Removed row no " + rowCount);
            // return null;
            // }
            // });

            new Td(tr, countryColumnStyle) {
                {
                    new Button(this, deleteClick) {
                        {
                            new NoTag(this, "Delete Row");
                        }
                    };
                }
            };

            rows.add(tr);
        }

//        if (tBody.getChildren().size() > 1 && !onTop) {
//            removePreviousRows();
//        }

        if (onTop && tBody.getChildren().size() > 1) {
            // at zeroth index child tag represents the head of the row
            // so taking the child at 1st index
            AbstractHtml firstChild = tBody.getChildren().get(1);
            firstChild
                    .insertBefore(rows.toArray(new AbstractHtml[rows.size()]));
        } else {
            AbstractHtml firstChild = tBody.getChildren().get(0);
            Collection<AbstractHtml> allChildren = new LinkedList<AbstractHtml>();
            allChildren.add(firstChild);
            allChildren.addAll(rows);
            tBody.addInnerHtmls(allChildren.toArray(new AbstractHtml[allChildren.size()]));
        }

    }

    private void removePreviousRows() {
        List<AbstractHtml> currentChildren = new LinkedList<AbstractHtml>(
                tBody.getChildren());
        currentChildren.remove(0);
        tBody.removeChildren(currentChildren);
    }

    private void addRowsAsStream() {

        List<AbstractHtml> rows = new LinkedList<AbstractHtml>();
        if (tBody.getChildren().size() > 1) {
            removePreviousRows();
        }

        for (int i = 0; i < 1000; i++) {

            final Tr tr = new Tr(tBody) {
                {
                    rowCount++;

                    new Td(this) {
                        {
                            new NoTag(this, "Alfreds Futterkiste " + rowCount);
                        }
                    };
                    new Td(this) {
                        {
                            new NoTag(this, "Maria Anders " + rowCount);
                        }
                    };
                    new Td(this, countryColumnStyle) {
                        {
                            new NoTag(this, "Germany " + rowCount);
                        }
                    };
                }
            };

            final int rowCount = this.rowCount;
            // as java 8 syntax, lambda expression way
            final OnClick deleteClick = new OnClick((wffBMObject, event) -> {
                AbstractHtml parentOfTr = tr.getParent();
                parentOfTr.removeChild(tr);
                rows.remove(tr);
                displayInServerLogPage("Removed row no " + rowCount);
                return null;
            });

            // as java 7 syntax
            // final OnClick deleteClick = new OnClick(new ServerAsyncMethod() {
            // @Override
            // public WffBMObject asyncMethod(WffBMObject wffBMObject, Event
            // event) {
            // AbstractHtml parentOfTr = tr.getParent();
            // parentOfTr.removeChild(tr);
            // rows.remove(tr);
            // displayInServerLogPage("Removed row no " + rowCount);
            // return null;
            // }
            // });

            new Td(tr, countryColumnStyle) {
                {
                    new Button(this, deleteClick) {
                        {
                            new NoTag(this, "Delete Row");
                        }
                    };
                }
            };

            rows.add(tr);
        }

    }

    @Override
    public WffBMObject asyncMethod(WffBMObject wffBMObject, Event event) {

        if (nextRowsButton.equals(event.getSourceTag())) {
            addRows(false, 25);
            displayInServerLogPage("nextRowsButton");
        } else if (markGreenButton.equals(event.getSourceTag())) {
            LOGGER.info("Mark column green");
            countryColumnStyle.addCssProperties("background:green");
            displayInServerLogPage("Mark column green");
        } else if (markVioletButton.equals(event.getSourceTag())) {
            LOGGER.info("Mark column violet");
            countryColumnStyle.addCssProperties("background:violet");
            displayInServerLogPage("Mark column violet");
        } else if (removeColoumnStyleButton.equals(event.getSourceTag())) {
            LOGGER.info("remove style all together");
            //is not supported since 3.0.1
            //countryColumnStyle.getCssProperties().clear();
            countryColumnStyle.removeAllCssProperties();
            displayInServerLogPage("remove style all together ");
        } else if (removeColoumnStyleOneByOneButton
                .equals(event.getSourceTag())) {
            LOGGER.info("remove style attribyte one by one");

            AbstractHtml[] ownerTags = countryColumnStyle.getOwnerTags();
            
            try {
                documentModel.getBrowserPage().holdPush();
                for (AbstractHtml ownerTag : ownerTags) {
                    ownerTag.removeAttributes(
                            countryColumnStyle.getAttributeName());
                }
            } finally {
                documentModel.getBrowserPage().unholdPush();
            }
            
            displayInServerLogPage("remove style atribyte one by one");
        } else if (lazyNextRowsButton.equals(event.getSourceTag())) {

            //since 3.0.1, it perfectly supports multi-threading
            //if addRowsAsStream is called inside a thread, 
            //we can execute other operations at the same time.
            //eg: if we click on logout while addRowsAsStream is in a thread execution
            // the logout will work otherwise the logout will wait to 
            //finish the addRowsAsStream if it is not in a thread.
            //if 3.0.1 is not released yet you can try wffweb-3.0.1-SNAPSHOT
            EXECUTOR_SERVICE.execute(() -> {
                addRowsAsStream();
            });

            LOGGER.info("lazyNextRowsButton");
            displayInServerLogPage("lazyNextRowsButton");
        } else if (addNewRowOnTopButton.equals(event.getSourceTag())) {
            addRows(true, noOfRows);
            displayInServerLogPage("addNewRowOnTopButton clicked");
        } else if (noOfRowsInput.equals(event.getSourceTag())) {
            
            try {
                noOfRows = Integer.parseInt(wffBMObject.getValue("noOfRows").toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            displayInServerLogPage("ValueChanged");
        }

        return null;
    }

    private void displayInServerLogPage(String msg) {
        Object serverLogPageInstanceId = documentModel.getHttpSession()
                .getAttribute("serverLogPageInstanceId");
        if (serverLogPageInstanceId != null) {
            ServerLogPage serverLogPage = (ServerLogPage) BrowserPageContext.INSTANCE
                    .getBrowserPage(serverLogPageInstanceId.toString());
            if (serverLogPage != null) {
                serverLogPage.log(msg);
            }
        }
    }

}
