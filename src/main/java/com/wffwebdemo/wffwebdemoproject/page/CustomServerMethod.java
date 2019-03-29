package com.wffwebdemo.wffwebdemoproject.page;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.logging.Logger;

import javax.servlet.http.HttpSession;

import com.webfirmframework.wffweb.server.page.BrowserPageContext;
import com.webfirmframework.wffweb.tag.html.attribute.event.ServerAsyncMethod;
import com.webfirmframework.wffweb.wffbm.data.BMValueType;
import com.webfirmframework.wffweb.wffbm.data.ValueValueType;
import com.webfirmframework.wffweb.wffbm.data.WffBMArray;
import com.webfirmframework.wffweb.wffbm.data.WffBMByteArray;
import com.webfirmframework.wffweb.wffbm.data.WffBMObject;

public class CustomServerMethod implements ServerAsyncMethod {

    private static final long serialVersionUID = 1L;
    
    private static final Logger LOGGER = Logger.getLogger(CustomServerMethod.class.getName());
    
    private HttpSession httpSession;
    
    public CustomServerMethod(HttpSession httpSession) {
        this.httpSession = httpSession;
    }

    @Override
    public WffBMObject asyncMethod(WffBMObject wffBMObject, Event event) {
        try {
            displayInServerLogPage(
                    "serverAsyncMethod invoked " + event.getServerMethodName()
                            + " wffBMObject " + wffBMObject);
            
            displayInServerLogPage("registered method name "+event.getServerMethodName());

            printBMObject(wffBMObject);

            WffBMObject bmObject = new WffBMObject();
            bmObject.put("serverKey", BMValueType.STRING, "value from server");
            bmObject.put("string", BMValueType.STRING, "sample string");
            //Right-to-left language
            bmObject.put("rtlString", BMValueType.STRING, "£ is pound symbol كيف حالك؟");
            bmObject.put("nul", BMValueType.NULL, null);
            bmObject.put("number", BMValueType.NUMBER, 555);
            bmObject.put("negativeNumber", BMValueType.NUMBER, -555);
            bmObject.put("undef", BMValueType.UNDEFINED, null);
            bmObject.put("reg", BMValueType.REG_EXP, "[w]");
            bmObject.put("bool", BMValueType.BOOLEAN, true);
            bmObject.put("testFun", BMValueType.FUNCTION,
                    "function(arg) {alert(arg);}");

            insertArraysTo(bmObject);

            bmObject.put("anotherObj", BMValueType.BM_OBJECT, bmObject.clone());
            displayInServerLogPage("finished");
            return bmObject;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void printBMObject(WffBMObject wffBMObject) {
        if (wffBMObject != null) {

            for (ValueValueType each : wffBMObject.values()) {

                displayInServerLogPage("key is " + each.getName()
                        + ", value type is " + each.getValueType()
                        + ", value is " + each.getValue());

                if (BMValueType.BM_OBJECT.equals(each.getValueType())) {
                    displayInServerLogPage(
                            "-------------inner bmObject------------------");
                    WffBMObject bmObject = (WffBMObject) each.getValue();
                    for (ValueValueType eachKeyValue : bmObject.values()) {

                        displayInServerLogPage("key is "
                                + eachKeyValue.getName()
                                + ", value type is "
                                + eachKeyValue.getValueType()
                                + ", value is " + eachKeyValue.getValue());
                    }
                    displayInServerLogPage("-------------------------------");
                } else if (BMValueType.BM_ARRAY.equals(each.getValueType())) {
                    WffBMArray wffBMArray = (WffBMArray) each.getValue();
                    displayInServerLogPage("value type in wffBMArray.getValueType "+wffBMArray.getValueType());
                } else if (BMValueType.BM_BYTE_ARRAY.equals(each.getValueType())) {
                    WffBMByteArray bmByteArray = (WffBMByteArray) each.getValue();
                    try {
                        displayInServerLogPage("toString of bytes from bmByteArray "
                                + new String(bmByteArray.toByteArray(),
                                        "UTF-8"));
                        
                    } catch (UnsupportedEncodingException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void insertArraysTo(WffBMObject bmObject)
            throws UnsupportedEncodingException, IOException {
        WffBMArray stringArray = new WffBMArray(BMValueType.STRING);
        stringArray.add("array value 1 こんにちは");
        stringArray.add("array value 2");

        bmObject.put("stringArray", BMValueType.BM_ARRAY, stringArray);

        WffBMArray numberArray = new WffBMArray(BMValueType.NUMBER);
        numberArray.add(555);
        numberArray.add(5);
        numberArray.add(55);

        bmObject.put("numberArray", BMValueType.BM_ARRAY, numberArray);

        WffBMByteArray byteArray = new WffBMByteArray();
        byteArray.write("こんにちは WFFWEB".getBytes("UTF-8"));

        bmObject.put("byteArray", BMValueType.BM_BYTE_ARRAY, byteArray);

        WffBMArray booleanArray = new WffBMArray(BMValueType.BOOLEAN);
        booleanArray.add(true);
        booleanArray.add(false);
        booleanArray.add(true);
        bmObject.put("booleanArray", BMValueType.BM_ARRAY, booleanArray);

        WffBMArray regexArray = new WffBMArray(BMValueType.REG_EXP);
        regexArray.add("[w]");
        regexArray.add("[f]");
        regexArray.add("[f]");
        bmObject.put("regexArray", BMValueType.BM_ARRAY, regexArray);

        WffBMArray funcArray = new WffBMArray(BMValueType.FUNCTION);
        funcArray.add("function(arg) {console.log(arg);}");
        funcArray.add("function(arg1) {console.log(arg1);}");
        funcArray.add("function(arg2) {console.log(arg2);}");
        bmObject.put("funcArray", BMValueType.BM_ARRAY, funcArray);

        WffBMArray nullArray = new WffBMArray(BMValueType.NULL);
        nullArray.add(null);
        nullArray.add(null);
        nullArray.add(null);
        bmObject.put("nullArray", BMValueType.BM_ARRAY, nullArray);

        WffBMArray undefinedArray = new WffBMArray(BMValueType.UNDEFINED);
        undefinedArray.add(null);
        undefinedArray.add(null);
        undefinedArray.add(null);
        bmObject.put("undefinedArray", BMValueType.BM_ARRAY, undefinedArray);

        WffBMArray arrayArray = new WffBMArray(BMValueType.BM_ARRAY);
        arrayArray.add(funcArray);
        arrayArray.add(funcArray);
        arrayArray.add(funcArray);
        bmObject.put("arrayArray", BMValueType.BM_ARRAY, arrayArray);

        WffBMArray objectArray = new WffBMArray(BMValueType.BM_OBJECT);
        objectArray.add(bmObject.clone());
        objectArray.add(bmObject.clone());
        objectArray.add(bmObject.clone());
        bmObject.put("objectArray", BMValueType.BM_ARRAY, objectArray);
    }
    
    private void displayInServerLogPage(String msg) {
        Object serverLogPageInstanceId = httpSession
                .getAttribute("serverLogPageInstanceId");
        if (serverLogPageInstanceId != null) {
            ServerLogPage serverLogPage = (ServerLogPage) BrowserPageContext.INSTANCE
                    .getBrowserPage(serverLogPageInstanceId.toString());
            if (serverLogPage != null) {
                serverLogPage.log(msg);
            }
        }
        
        LOGGER.info(msg);
    }
}
