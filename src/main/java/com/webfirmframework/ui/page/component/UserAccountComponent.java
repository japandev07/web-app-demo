package com.webfirmframework.ui.page.component;

import com.webfirmframework.ui.page.common.GlobalSTC;
import com.webfirmframework.ui.page.common.NavigationURI;
import com.webfirmframework.ui.page.css.Bootstrap5CssClass;
import com.webfirmframework.ui.page.model.DocumentModel;
import com.webfirmframework.ui.page.template.SampleTemplate1;
import com.webfirmframework.ui.page.template.SampleTemplate2;
import com.webfirmframework.wffweb.tag.html.*;
import com.webfirmframework.wffweb.tag.html.attribute.Href;
import com.webfirmframework.wffweb.tag.html.attribute.Target;
import com.webfirmframework.wffweb.tag.html.attribute.event.mouse.OnClick;
import com.webfirmframework.wffweb.tag.html.formsandinputs.Button;
import com.webfirmframework.wffweb.tag.html.links.A;
import com.webfirmframework.wffweb.tag.html.stylesandsemantics.Div;
import com.webfirmframework.wffweb.tag.htmlwff.NoTag;
import com.webfirmframework.wffweb.tag.htmlwff.TagContent;
import com.webfirmframework.wffweb.util.URIUtil;
import com.webfirmframework.wffwebcommon.TokenUtil;

import org.json.JSONObject;

import java.time.Clock;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class UserAccountComponent extends Div {

    private final DocumentModel documentModel;

    // no need use volatile modifier, the framework internally handles it unless
    // the value is assigned via a custom thread.
    private AbstractHtml widgetDivCurrentChild;

    public UserAccountComponent(DocumentModel documentModel) {
        super(null);
        this.documentModel = documentModel;
        GlobalSTC.LOGGER_STC.setContent(
                ZonedDateTime.now(Clock.systemUTC()).format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")) +
                        ":~$ created new UserAccountComponent");
        develop();
    }

    private void develop() {
        JSONObject user = TokenUtil.getPayloadFromJWT(documentModel.session().localStorage().getToken("jwtToken"));
        new H1(this).give(TagContent::text, "Welcome " + user.get("username"));
        new Hr(this);
        new Button(this,
                Bootstrap5CssClass.BTN_PRIMARY.getAttribute(),
                new OnClick(event -> {
//                    documentModel.session().localStorage().removeToken("jwtToken");
                    //on logout all localStorage items and tokens should be cleared not just jwtToken so calling clear() method
                    documentModel.session().localStorage().clear();

                    //navigate to login page on all other opened tabs
                    //This works well on multi node mode
                    documentModel.browserPage().getTagRepository()
                            .executeJsInOtherBrowserPages(
                                    "wffAsync.setURI('%s');".formatted(NavigationURI.LOGIN.getUri(documentModel)));

                    //navigate to login page
                    documentModel.browserPage().setURI(NavigationURI.LOGIN.getUri(documentModel));
                    return null;
                }))
                .give(TagContent::text, "Logout");
        

        new Br(this);
        new Br(this);


        final String realtimeLogURI = NavigationURI.REALTIME_SERVER_LOG.getUri(documentModel);
        //navigation using server side setURI method
        new A(this,
                Bootstrap5CssClass.LINK_PRIMARY.getAttribute(),
                new Href(realtimeLogURI),
                new Target(Target.BLANK))
                .give(TagContent::text, "Realtime Server Log");

        new Br(this);
        new Br(this);

        //navigation using client side setURI method
        final String itemsURI = NavigationURI.VIEW_ITEMS.getUri(documentModel);
        new A(this,
                Bootstrap5CssClass.BTN_PRIMARY.getAttribute(),
                new Href(itemsURI),
                new OnClick("event.preventDefault(); wffAsync.setURI('" + itemsURI + "');"))
                .give(TagContent::text, "View Items");

        new Br(this);
        new Br(this);

        new A(this,
                Bootstrap5CssClass.BTN_PRIMARY.getAttribute(),
                new Href(itemsURI),
                new OnClick("event.preventDefault(); wffAsync.setURI('" + itemsURI + "', null, null, true);"))
                .give(TagContent::text, "View Items with replace true");

        new Br(this);
        new Br(this);

        final String addItemURI = NavigationURI.ADD_ITEM.getUri(documentModel);
        //navigation using server side setURI method
        new A(this,
                Bootstrap5CssClass.BTN_PRIMARY.getAttribute(),
                new Href(addItemURI),
                new OnClick("""
                        event.preventDefault();
                        loadingIcon.hidden = false;
                        return true;""", event -> {
                    documentModel.browserPage().setURI(NavigationURI.ADD_ITEM.getUri(documentModel));
                    return null;
                }, null, "loadingIcon.hidden = true;"))
                .give(TagContent::text, "Add Item");


        new Br(this);
        new Br(this);

        new A(this,
                Bootstrap5CssClass.BTN_PRIMARY.getAttribute(),
                new Href(addItemURI),
                new OnClick("""
                        event.preventDefault();
                        loadingIcon.hidden = false;
                        return true;""", event -> {
                    documentModel.browserPage().setURI(NavigationURI.ADD_ITEM.getUri(documentModel), true);
                    return null;
                }, null, "loadingIcon.hidden = true;"))
                .give(TagContent::text, "Add Item with replace true");


        new Br(this);
        new Br(this);

        final String priceHistoryURI = NavigationURI.ITEM_PRICE_HISTORY_CHART.getUri(documentModel).replace("{itemId}", "2");
        //navigation using server side setURI method
        new A(this,
                Bootstrap5CssClass.BTN_PRIMARY.getAttribute(),
                new Href(priceHistoryURI),
                new OnClick("""
                        event.preventDefault();
                        loadingIcon.hidden = false;
                        return true;""", event -> {
                    documentModel.browserPage().setURI(priceHistoryURI);
                    return null;
                }, null, "loadingIcon.hidden = true;"))
                .give(TagContent::text, "Item 2 Price History");

        new Br(this);
        new Br(this);

        final String realtimeClockURI = NavigationURI.REALTIME_CLOCK.getUri(documentModel);
        //navigation using server side setURI method
        new A(this,
                Bootstrap5CssClass.BTN_PRIMARY.getAttribute(),
                new Href(realtimeClockURI),
                new OnClick("""
                        event.preventDefault();
                        loadingIcon.hidden = false;
                        return true;""", event -> {
                    documentModel.browserPage().setURI(realtimeClockURI);
                    return null;
                }, null, "loadingIcon.hidden = true;"))
                .give(TagContent::text, "Realtime Server Clock");

        new Br(this);
        new Br(this);


        URIStateSwitch widgetDiv = new Div(this);

        widgetDiv.whenURI(NavigationURI.VIEW_ITEMS.getPredicate(documentModel),
                () -> {
                    documentModel.browserPage().getTagRepository().findTitleTag().give(
                            TagContent::text, "View Items | User Account | wffweb demo");
                    if (!(widgetDivCurrentChild instanceof ViewItemsComponent)) {
                        widgetDivCurrentChild = new ViewItemsComponent();
                    }
                    return new AbstractHtml[]{widgetDivCurrentChild};
                });

        widgetDiv.whenURI(NavigationURI.ADD_ITEM.getPredicate(documentModel),
                () -> {
                    documentModel.browserPage().getTagRepository().findTitleTag().give(
                            TagContent::text, "Add Item | User Account | wffweb demo");
                    if (!(widgetDivCurrentChild instanceof AddItemComponent)) {
                        widgetDivCurrentChild = new AddItemComponent();
                    }
                    return new AbstractHtml[]{widgetDivCurrentChild};
                });

        widgetDiv.whenURI(NavigationURI.ITEM_PRICE_HISTORY_CHART.getPredicate(documentModel),
                () -> {
                    documentModel.browserPage().getTagRepository().findTitleTag().give(
                            TagContent::text, "Item Price History | User Account | wffweb demo");
                    //Note: URIUtil class will be available since 12.0.0-beta.2
                    Map<String, String> pathParams = URIUtil.parseValues(NavigationURI.ITEM_PRICE_HISTORY_CHART.getUri(documentModel), documentModel.browserPage().getURI());
                    long itemId = 0;
                    try {
                        itemId = Long.parseLong(pathParams.get("itemId"));
                    } catch (NumberFormatException e) {
                        return new AbstractHtml[]{new H6(null).give(TagContent::text, "Invalid Item Id")};
                    }
                    if (widgetDivCurrentChild instanceof ItemPriceHistoryChartComponent component) {
                        if (component.getItemId() != itemId) {
                            widgetDivCurrentChild = new ItemPriceHistoryChartComponent(documentModel, itemId);
                        }
                    } else {
                        widgetDivCurrentChild = new ItemPriceHistoryChartComponent(documentModel, itemId);
                    }
                    return new AbstractHtml[]{widgetDivCurrentChild};
                });

        widgetDiv.whenURI(NavigationURI.SAMPLE_TEMPLATE1.getPredicate(documentModel),
                () -> {
                    documentModel.browserPage().getTagRepository().findTitleTag().give(
                            TagContent::text, "SampleTemplate1 | User Account | wffweb demo");
                    if (!(widgetDivCurrentChild instanceof SampleTemplate1)) {
                        widgetDivCurrentChild = new SampleTemplate1(documentModel);
                    }
                    return new AbstractHtml[]{widgetDivCurrentChild};
                });

        widgetDiv.whenURI(NavigationURI.SAMPLE_TEMPLATE2.getPredicate(documentModel),
                () -> {
                    documentModel.browserPage().getTagRepository().findTitleTag().give(
                            TagContent::text, "SampleTemplate2 | User Account | wffweb demo");
                    if (!(widgetDivCurrentChild instanceof SampleTemplate2)) {
                        widgetDivCurrentChild = new SampleTemplate2(documentModel);
                    }
                    return new AbstractHtml[]{widgetDivCurrentChild};
                });

        widgetDiv.whenURI(NavigationURI.REALTIME_CLOCK.getPredicate(documentModel),
                () -> {
                    documentModel.browserPage().getTagRepository().findTitleTag().give(
                            TagContent::text, "RealtimeClock | User Account | wffweb demo");
                    if (!(widgetDivCurrentChild instanceof RealtimeClock)) {
                        widgetDivCurrentChild = new RealtimeClock(documentModel);
                    }
                    return new AbstractHtml[]{widgetDivCurrentChild};
                });


        sampleTemplateButtons();
    }

    private void sampleTemplateButtons() {

        String sampleTemplate1URI = NavigationURI.SAMPLE_TEMPLATE1.getUri(documentModel);
        String sampleTemplate2URI = NavigationURI.SAMPLE_TEMPLATE2.getUri(documentModel);

        new Br(this);
        new Br(this);
        new A(this,
                new Href(sampleTemplate1URI),
                new OnClick("event.preventDefault(); wffAsync.setURI('" + sampleTemplate1URI + "', function(){loadingIcon.hidden = false;});"),
                Bootstrap5CssClass.BTN_INFO_SM.getAttribute()).give(TagContent::text, "SampleTemplate1");


        //just for space
        new NoTag(this, " ");


        new A(this,
                new Href(sampleTemplate2URI),
                new OnClick("event.preventDefault(); wffAsync.setURI('" + sampleTemplate2URI + "', function(){loadingIcon.hidden = false;});"),
                Bootstrap5CssClass.BTN_INFO_SM.getAttribute()).give(TagContent::text, "SampleTemplate2");

    }
}
