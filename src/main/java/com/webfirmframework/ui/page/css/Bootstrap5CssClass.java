package com.webfirmframework.ui.page.css;

import com.webfirmframework.wffweb.tag.html.attribute.global.ClassAttribute;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.webfirmframework.wffweb.util.StringUtil.splitBySpace;

/**
 * 
 * Keeping an enum for css classes will prevent developer mistakes declaring
 * class names. It will also help us to know what are the classes available in
 * the whole project. <br>
 * <br>
 * It contains bootstrap5 classes but this is not complete. It may also contain
 * bootstrap3/bootstrap4 classes so use it carefully. NB: update it with
 * bootstrap5 doc. You can also merge from custom classes, Eg:
 * 
 * <pre>
 * <code>
 * ClassAttribute classAttributeFromCssClasses = Bootstrap4CssClass.BTN_DANGER.merge(CustomCssClass.CUSTOM_CLASS);
 * </code>
 * </pre>
 *
 */
public enum Bootstrap5CssClass implements CssClass {

    FORM_CONTROL("form-control"),

    TEXT_LEFT("text-left"),

    TEXT_CENTER("text-center"),

    TEXT_RIGHT("text-right"),
    
    BTN_INFO("btn btn-info"),

    BTN_INFO_SM("btn btn-info btn-sm"),

    BTN_INFO_LG("btn btn-info btn-lg"),

    BTN_PRIMARY("btn btn-primary"),

    BTN_PRIMARY_SM("btn btn-primary btn-sm"),

    BTN_PRIMARY_LG("btn btn-primary btn-lg"),

    BTN_SUCCESS("btn btn-success"),

    BTN_SUCCESS_SM("btn btn-success btn-sm"),

    BTN_SUCCESS_LG("btn btn-success btn-lg"),

    BTN_DANGER("btn btn-danger"),

    BTN_DANGER_SM("btn btn-danger btn-sm"),

    BTN_DANGER_LG("btn btn-danger btn-lg"),

    BTN_GROUP("btn-group"),

    BTN_GROUP_SM("btn-group btn-group-sm"),

    BTN_GROUP_LG("btn-group btn-group-lg"),

    TABLE("table"),

    TABLE_RESPONSIVE("table-responsive"),

    TABLE_BORDERED("table-bordered"),

    TABLE_PRIMARY("table-primary"),

    TABLE_SECONDARY("table-secondary"),

    TABLE_SUCCESS("table-success"),

    TABLE_DANGER("table-danger"),

    TABLE_WARNING("table-warning"),

    TABLE_INFO("table-info"),

    TABLE_LIGHT("table-light"),

    TABLE_DARK("table-dark"),

    FORM_GROUP("form-group"),

    FORM_INLINE("form-inline"),

    LABEL_INFO("label label-info"),

    LABEL_WARNING("label label-warning"),

    LABEL_DANGER("label label-danger"),

    TEXT_MUTED("text-muted"),

    TEXT_PRIMARY("text-primary"),

    TEXT_SUCCESS("text-success"),

    TEXT_INFO("text-info"),

    TEXT_WARNING("text-warning"),

    TEXT_DANGER("text-danger"),

    ALERT_PRIMARY("alert alert-primary"),

    ALERT_SECONDARY("alert alert-secondary"),

    ALERT_SUCCESS("alert alert-success"),

    ALERT_INFO("alert alert-info"),

    ALERT_WARNING("alert alert-warning"),

    ALERT_DANGER("alert alert-danger"),

    ALERT_LIGHT("alert alert-light"),

    ALERT_DARK("alert alert-dark"),

    BTN_LINK("btn btn-link"),

    BTN_SECONDARY("btn btn-secondary"),

    CONTAINER("container"),

    LINK_PRIMARY("link-primary")

    ;

    private final String[] classNames;

    Bootstrap5CssClass(String classNames) {
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
