package com.webfirmframework.ui.page.template;

import com.webfirmframework.ui.page.common.GlobalSTC;
import com.webfirmframework.ui.page.css.Bootstrap5CssClass;
import com.webfirmframework.ui.page.model.DocumentModel;
import com.webfirmframework.wffweb.tag.html.AbstractHtml;
import com.webfirmframework.wffweb.tag.html.H3;
import com.webfirmframework.wffweb.tag.html.attribute.For;
import com.webfirmframework.wffweb.tag.html.attribute.Name;
import com.webfirmframework.wffweb.tag.html.attribute.Type;
import com.webfirmframework.wffweb.tag.html.attribute.event.ServerMethod;
import com.webfirmframework.wffweb.tag.html.attribute.event.form.OnSubmit;
import com.webfirmframework.wffweb.tag.html.attribute.global.Id;
import com.webfirmframework.wffweb.tag.html.formsandinputs.Button;
import com.webfirmframework.wffweb.tag.html.formsandinputs.Form;
import com.webfirmframework.wffweb.tag.html.formsandinputs.Input;
import com.webfirmframework.wffweb.tag.html.formsandinputs.Label;
import com.webfirmframework.wffweb.tag.html.html5.attribute.Placeholder;
import com.webfirmframework.wffweb.tag.html.html5.attribute.Required;
import com.webfirmframework.wffweb.tag.html.stylesandsemantics.Div;
import com.webfirmframework.wffweb.tag.htmlwff.NoTag;
import com.webfirmframework.wffweb.tag.repository.TagRepository;
import com.webfirmframework.wffweb.wffbm.data.BMValueType;
import com.webfirmframework.wffweb.wffbm.data.WffBMObject;

import java.time.Clock;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class SampleTemplate2 extends Div implements ServerMethod {

    private DocumentModel documentModel;

    public SampleTemplate2(DocumentModel documentModel) {
        super(null, Bootstrap5CssClass.CONTAINER.getAttribute());
        this.documentModel = documentModel;
        GlobalSTC.LOGGER_STC.setContent(
                ZonedDateTime.now(Clock.systemUTC()).format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")) +
                        ":~$ created new SampleTemplate2");
        develop();
    }

 // @formatter:off
    private void develop() {
        changeTitle();
        
        new H3(this) .give(h -> {
            new NoTag(h, "SampleTemplate2.java");
        });
        new Form(this,
            new OnSubmit("event.preventDefault(); return true;", 
                this, 
                "return {name:fullname.value};", 
                "console.log(jsObject.msg); alert('The full name is printed in server console');")) .give(form -> {
            
            new Div(form,
                    Bootstrap5CssClass.FORM_GROUP.getAttribute()) .give(div -> {
                    
                    new Label(div, new For("fullnameId")).give(label -> {
                        new NoTag(label, "Full Name");
                    });
                    new Input(div,
                            Bootstrap5CssClass.FORM_CONTROL.getAttribute(),
                        new Id("fullnameId"),
                        new Name("fullname"),
                        new Type(Type.TEXT),
                        new Required(),
                        new Placeholder("Your Full Name"));
            });
            
            new Button(form,
                new Type(Type.SUBMIT),
                Bootstrap5CssClass.BTN_PRIMARY.getAttribute()) .give(btn -> {
                new NoTag(btn, "Submit");
            });
        });
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
                title.addInnerHtml(new NoTag(null, "SampleTemplate2"));
            }
        }

    }

    @Override
    public WffBMObject invoke(Event event) {

        WffBMObject data = event.data();
        System.out.println("full name: " + data.getValue("name"));

        this.insertBefore(new SampleTemplate1(documentModel));
        this.getParent().removeChild(this);

        WffBMObject result = new WffBMObject();
        result.put("msg", BMValueType.STRING,
                "This msg will be printed in the browser console.");

        return result;
    }
}
