package com.webfirmframework.ui.page.template;

import com.webfirmframework.ui.page.common.GlobalSTC;
import com.webfirmframework.ui.page.css.Bootstrap5CssClass;
import com.webfirmframework.ui.page.model.DocumentModel;
import com.webfirmframework.wffweb.tag.html.AbstractHtml;
import com.webfirmframework.wffweb.tag.html.Br;
import com.webfirmframework.wffweb.tag.html.H3;
import com.webfirmframework.wffweb.tag.html.attribute.For;
import com.webfirmframework.wffweb.tag.html.attribute.Type;
import com.webfirmframework.wffweb.tag.html.attribute.event.ServerMethod;
import com.webfirmframework.wffweb.tag.html.attribute.event.form.OnSubmit;
import com.webfirmframework.wffweb.tag.html.attribute.event.mouse.OnClick;
import com.webfirmframework.wffweb.tag.html.attribute.global.Id;
import com.webfirmframework.wffweb.tag.html.formsandinputs.Button;
import com.webfirmframework.wffweb.tag.html.formsandinputs.Form;
import com.webfirmframework.wffweb.tag.html.formsandinputs.Input;
import com.webfirmframework.wffweb.tag.html.formsandinputs.Label;
import com.webfirmframework.wffweb.tag.html.html5.attribute.Placeholder;
import com.webfirmframework.wffweb.tag.html.stylesandsemantics.Div;
import com.webfirmframework.wffweb.tag.htmlwff.NoTag;
import com.webfirmframework.wffweb.tag.repository.TagRepository;
import com.webfirmframework.wffweb.wffbm.data.WffBMObject;

import java.time.Clock;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class SampleTemplate1 extends Div implements ServerMethod {

    private DocumentModel documentModel;

    public SampleTemplate1(DocumentModel documentModel) {
        super(null, Bootstrap5CssClass.CONTAINER.getAttribute());
        this.documentModel = documentModel;
        GlobalSTC.LOGGER_STC.setContent(
                ZonedDateTime.now(Clock.systemUTC()).format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")) +
                        ":~$ created new SampleTemplate1");
        develop();
    }

 // @formatter:off
    private void develop() {
        
        changeTitle();
        
        
        new H3(this).give(h -> {
            new NoTag(h, "SampleTemplate1.java");
        });
        
        
        new Form(this, new OnSubmit("event.preventDefault(); return true;", this, null, null)).give(form -> {
            new Div(form,
                Bootstrap5CssClass.FORM_GROUP.getAttribute()).give(div -> {
                    new Label(div, new For("inputId")).give(label -> {
                        new NoTag(label, "Email Input");
                    });
                    new Input(div,
                        new Id("inputId"),    
                        new Type(Type.EMAIL),
                        Bootstrap5CssClass.FORM_CONTROL.getAttribute(),
                        new Placeholder("Eg: tech-support@webfirmframework.com"));
            });
        });
        
        
        
        new Br(this);
        
        new Button(this, new OnClick(SampleTemplate1.this), Bootstrap5CssClass.BTN_SECONDARY.getAttribute()).give(btn -> {
            new NoTag(btn, "Click Me to change to SampleTemplate2");
        });
        new Br(this);
        new Br(this);
    }
 // @formatter:on

    private void changeTitle() {
        // getTagRepository() will give object only if the browserPage.render is
        // returned
        TagRepository tagRepository = documentModel.browserPage()
                .getTagRepository();
        if (tagRepository != null) {
            AbstractHtml title = tagRepository.findTagById("windowTitleId");
            if (title != null) {
                title.addInnerHtml(new NoTag(null, "SampleTemplate1"));
            }
        }
    }

    @Override
    public WffBMObject invoke(Event event) {

        this.insertBefore(new SampleTemplate2(documentModel));
        this.getParent().removeChild(this);

        return null;
    }
}
