package com.webfirmframework.ui.page.component;

import com.webfirmframework.ui.page.common.GlobalSTC;
import com.webfirmframework.ui.page.css.Bootstrap5CssClass;
import com.webfirmframework.wffweb.tag.html.H1;
import com.webfirmframework.wffweb.tag.html.attribute.event.mouse.OnClick;
import com.webfirmframework.wffweb.tag.html.attribute.global.ClassAttribute;
import com.webfirmframework.wffweb.tag.html.formsandinputs.Button;
import com.webfirmframework.wffweb.tag.html.stylesandsemantics.Div;
import com.webfirmframework.wffweb.tag.html.tables.*;
import com.webfirmframework.wffweb.tag.htmlwff.TagContent;

import java.time.Clock;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class ViewItemsComponent extends Div {

    public ViewItemsComponent() {
        super(null);
        GlobalSTC.LOGGER_STC.setContent(
                ZonedDateTime.now(Clock.systemUTC()).format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")) +
                        ":~$ created new ViewItemsComponent");
        develop();
    }

    private void develop() {
        new H1(this).give(TagContent::text, "All Items");

        ClassAttribute infoResponsiveTableCssClass = Bootstrap5CssClass.TABLE.merge(
                Bootstrap5CssClass.TABLE_RESPONSIVE,
                Bootstrap5CssClass.TABLE_BORDERED,
                Bootstrap5CssClass.TABLE_INFO);
        new Table(this, infoResponsiveTableCssClass).give(table -> {
            new THead(table).give(head -> {
                new Tr(head).give(tr -> {
                    new Th(tr).give(TagContent::text, "Item Name");
                    new Th(tr).give(TagContent::text, "Item Code");
                    new Th(tr).give(TagContent::text, "Edited time");
                    new Th(tr);
                });
            });
            new TBody(table).give(tbody -> {
                for (int i = 1; i < 11; i++) {
                    var itemCode = i;
                    new Tr(tbody).give(tr -> {
                        new Td(tr).give(TagContent::text, "Item " + itemCode);
                        new Td(tr).give(TagContent::text, String.valueOf(itemCode));
                        new Td(tr).give(TagContent::text, ZonedDateTime.now(ZoneId.of("Japan")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")));
                        new Td(tr).give(td -> {
                            new Button(td, Bootstrap5CssClass.BTN_DANGER_SM.getAttribute(),
                                    new OnClick(event -> {
                                        tbody.removeChild(tr);
                                        return null;
                                    })).give(TagContent::text, "Delete");
                        });
                    });
                }
            });
        });
    }
}
