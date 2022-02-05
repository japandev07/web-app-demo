package com.webfirmframework.ui.page.css;

import com.webfirmframework.wffweb.tag.html.attribute.global.ClassAttribute;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.webfirmframework.wffweb.util.StringUtil.splitBySpace;

/**
 * The custom classes you created it.
 * 
 * You can also merge from custom classes, Eg:
 * 
 * <pre>
 * <code>
 * ClassAttribute classAttributeFromCssClasses = CustomCssClass.CUSTOM_CLASS.merge(Bootstrap4CssClass.BTN_DANGER);
 * 
 * ClassAttribute classAttributeFromCssClasses2 = CustomCssClass.CUSTOM_CLASS.merge(Bootstrap4CssClass.BTN_DANGER, Bootstrap4CssClass.BTN_GROUP, CustomCssClass.CUSTOM_CLASS2);
 * </code>
 * </pre>
 *
 */
public enum CustomCssClass implements CssClass {

    CUSTOM_CLASS("custom-class"),

    CUSTOM_CLASS2("custom-class2"),

    ;

    private final String[] classNames;

    CustomCssClass(String classNames) {
        this.classNames = splitBySpace(classNames);
    }

    @Override
    public String[] getClassNames() {
        return classNames;
    }

    public ClassAttribute getClassAttribute() {
        return new ClassAttribute(classNames);
    }

    public ClassAttribute getAttribute() {
        return getClassAttribute();
    }

    public ClassAttribute merge(String... cssClasses) {

        List<String> list = new ArrayList<>();
        Collections.addAll(list, classNames);
        list.addAll(Arrays.asList(cssClasses));

        return new ClassAttribute(list.toArray(new String[list.size()]));
    }

    public ClassAttribute merge(CssClass... cssClasses) {

        List<String> list = new ArrayList<>();
        Collections.addAll(list, classNames);
        for (CssClass cssClass : cssClasses) {
            Collections.addAll(list, cssClass.getClassNames());
        }

        return new ClassAttribute(list.toArray(new String[list.size()]));
    }

    public ClassAttribute merge(CssClass[]... twoDArray) {

        List<String> list = new ArrayList<>();
        Collections.addAll(list, classNames);
        for (CssClass[] cssClasses : twoDArray) {
            for (CssClass cssClass : cssClasses) {
                Collections.addAll(list, cssClass.getClassNames());
            }
        }

        return new ClassAttribute(list.toArray(new String[list.size()]));
    }

}
