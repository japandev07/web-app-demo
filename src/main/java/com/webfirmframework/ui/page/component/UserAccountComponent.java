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
                                    "window.setURI('%s');".formatted(NavigationURI.LOGIN.getUri(documentModel)));

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
                new OnClick("event.preventDefault(); window.setURI('" + itemsURI + "');"))
                .give(TagContent::text, "View Items");

        new Br(this);
        new Br(this);

        new A(this,
                Bootstrap5CssClass.BTN_PRIMARY.getAttribute(),
                new Href(itemsURI),
                new OnClick("event.preventDefault(); window.setURI('" + itemsURI + "', null, null, true);"))
                .give(TagContent::text, "View Items with replace true");

        new Br(this);
        new Br(this);

        final String viewItemURI = NavigationURI.VIEW_ITEM.getUri(documentModel) + "?itemId=55555";
        //navigation using server side setURI method
        new A(this,
                Bootstrap5CssClass.BTN_PRIMARY.getAttribute(),
                new Href(itemsURI),
                new OnClick("event.preventDefault(); window.setURI('" + viewItemURI + "');"))
                .give(TagContent::text, "View Item 55555 by query string param");

        new Br(this);
        new Br(this);

        final String viewItemURI2 = NavigationURI.VIEW_ITEM.getUri(documentModel) + "?itemId=14";
        //navigation using server side setURI method
        new A(this,
                Bootstrap5CssClass.BTN_PRIMARY.getAttribute(),
                new Href(itemsURI),
                new OnClick("event.preventDefault(); window.setURI('" + viewItemURI2 + "');"))
                .give(TagContent::text, "View Item 14 by query string param");

        new Br(this);
        new Br(this);


        final String viewItemURI3 = NavigationURI.VIEW_ITEM.getUri(documentModel) + "?itemId=14#hash1";
        //navigation using server side setURI method
        new A(this,
                Bootstrap5CssClass.BTN_PRIMARY.getAttribute(),
                new Href(viewItemURI3),
                new OnClick("event.preventDefault(); window.setURI('" + viewItemURI3 + "');"))
                .give(TagContent::text, "View Item 14 by query string param hash1");

        new Br(this);
        new Br(this);

        final String viewItemURI4 = NavigationURI.VIEW_ITEM.getUri(documentModel) + "?itemId=14#hash2";
        //navigation using server side setURI method
        new A(this,
                Bootstrap5CssClass.BTN_PRIMARY.getAttribute(),
                new Href(viewItemURI4),
                new OnClick("event.preventDefault(); window.setURI('" + viewItemURI4 + "');"))
                .give(TagContent::text, "View Item 14 by query string param hash2");

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

        final String priceHistoryURI2 = NavigationURI.ITEM_PRICE_HISTORY_CHART.getUri(documentModel).replace("{itemId}", "5");
        //navigation using server side setURI method
        new A(this,
                Bootstrap5CssClass.BTN_PRIMARY.getAttribute(),
                new Href(priceHistoryURI2),
                new OnClick("""
                        event.preventDefault();
                        loadingIcon.hidden = false;
                        return true;""", event -> {
                    documentModel.browserPage().setURI(priceHistoryURI2);
                    return null;
                }, null, "loadingIcon.hidden = true;"))
                .give(TagContent::text, "Item 5 Price History");

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

        final String sampleFilesUploadUri = NavigationURI.SAMPLE_FILES_UPLOAD.getUri(documentModel);
        //navigation using server side setURI method
        new A(this,
                Bootstrap5CssClass.BTN_PRIMARY.getAttribute(),
                new Href(sampleFilesUploadUri),
                new OnClick("""
                        event.preventDefault();
                        loadingIcon.hidden = false;
                        return true;""", event -> {
                    documentModel.browserPage().setURI(sampleFilesUploadUri);
                    return null;
                }, null, "loadingIcon.hidden = true;"))
                .give(TagContent::text, "Sample Files Upload");

        new Br(this);
        new Br(this);

        //calling custom Server Method
        new A(this,
                Bootstrap5CssClass.BTN_PRIMARY.getAttribute(),
                new OnClick("event.preventDefault(); invokeServerMethod();"))
                .give(TagContent::text, "Call custom ServerMethod");

        new Br(this);
        new Br(this);


        URIStateSwitch widgetDiv = new Div(this);

        widgetDiv.whenURI(NavigationURI.VIEW_ITEMS.getPredicate(documentModel, widgetDiv),
                () -> {
                    documentModel.browserPage().getTagRepository().findTitleTag().give(
                            TagContent::text, "View Items | User Account | wffweb demo");
                    return new AbstractHtml[]{new ViewItemsComponent()};
                });

        widgetDiv.whenURI(NavigationURI.ADD_ITEM.getPredicate(documentModel, widgetDiv),
                () -> {
                    documentModel.browserPage().getTagRepository().findTitleTag().give(
                            TagContent::text, "Add Item | User Account | wffweb demo");
                    return new AbstractHtml[]{new AddItemComponent()};
                });

        widgetDiv.whenURI(NavigationURI.ITEM_PRICE_HISTORY_CHART.getPredicate(documentModel, widgetDiv),
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
                    return new AbstractHtml[]{new ItemPriceHistoryChartComponent(documentModel, itemId)};
                });

        widgetDiv.whenURI(NavigationURI.VIEW_ITEM.getPredicate(documentModel, widgetDiv),
                () -> {
                    documentModel.browserPage().getTagRepository().findTitleTag().give(
                            TagContent::text, "View Item | User Account | wffweb demo");
                    return new AbstractHtml[]{new ViewItem(documentModel)};
                });


        widgetDiv.whenURI(NavigationURI.SAMPLE_TEMPLATE1.getPredicate(documentModel, widgetDiv),
                () -> {
                    documentModel.browserPage().getTagRepository().findTitleTag().give(
                            TagContent::text, "SampleTemplate1 | User Account | wffweb demo");
                    return new AbstractHtml[]{new SampleTemplate1(documentModel)};
                });

        widgetDiv.whenURI(NavigationURI.SAMPLE_TEMPLATE2.getPredicate(documentModel, widgetDiv),
                () -> {
                    documentModel.browserPage().getTagRepository().findTitleTag().give(
                            TagContent::text, "SampleTemplate2 | User Account | wffweb demo");
                    return new AbstractHtml[]{new SampleTemplate2(documentModel)};
                });

        widgetDiv.whenURI(NavigationURI.REALTIME_CLOCK.getPredicate(documentModel, widgetDiv),
                () -> {
                    documentModel.browserPage().getTagRepository().findTitleTag().give(
                            TagContent::text, "RealtimeClock | User Account | wffweb demo");
                    return new AbstractHtml[]{new RealtimeClock(documentModel)};
                });

        widgetDiv.whenURI(NavigationURI.SAMPLE_FILES_UPLOAD.getPredicate(documentModel, widgetDiv),
                () -> {
                    documentModel.browserPage().getTagRepository().findTitleTag().give(
                            TagContent::text, "SampleFilesUploadComponent | User Account | wffweb demo");
                    return new AbstractHtml[]{new SampleFilesUploadComponent(documentModel)};
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
                new OnClick("event.preventDefault(); window.setURI('" + sampleTemplate1URI + "', function(){loadingIcon.hidden = false;});"),
                Bootstrap5CssClass.BTN_INFO_SM.getAttribute()).give(TagContent::text, "SampleTemplate1");


        //just for space
        new NoTag(this, " ");


        new A(this,
                new Href(sampleTemplate2URI),
                new OnClick("event.preventDefault(); window.setURI('" + sampleTemplate2URI + "', function(){loadingIcon.hidden = false;});"),
                Bootstrap5CssClass.BTN_INFO_SM.getAttribute()).give(TagContent::text, "SampleTemplate2");

    }
}
