package net.pocrd.dao;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;

import net.pocrd.api.resp.ApiResponse.Api_Response;
import net.pocrd.api.resp.ApiString.Api_String;
import net.pocrd.define.ConstField;
import net.pocrd.define.Serializer;
import net.pocrd.entity.ReturnCode;
import net.pocrd.entity.ReturnCodeException;
import net.pocrd.util.SerializerProvider;

import com.google.protobuf.GeneratedMessage;

public class SerializerTest implements Serializer<Api_Response> {
    Method m;

    @Override
    public void toXml(Api_Response instance, OutputStream out, boolean isRoot) {
        if (instance == null) return;
        try {
            if (isRoot) {
                out.write("<Api_Response>".getBytes(ConstField.UTF8));
            }
            SerializerProvider.writeString(out, "12345");
            // int size = instance.getNotificationCount();
            // if (size > 0) {
            // Serializer<Api_Notification> s = SerializerProvider.getSerializer(Api_Notification.class);
            // if (s != null) {
            // out.write("<NotificationList>".getBytes(ConstField.UTF8));
            // for (; size > 0; size--) {
            // out.write("<Notification>".getBytes(ConstField.UTF8));
            // s.toXml(instance.getNotification(size - 1), out, false);
            // out.write("</Notification>".getBytes(ConstField.UTF8));
            // }
            // out.write("</NotificationList>".getBytes(ConstField.UTF8));
            // }
            // }
            //
            if (isRoot) {
                out.write("</Api_Response>".getBytes(ConstField.UTF8));
            }
        } catch (IOException e) {
            throw new ReturnCodeException(ReturnCode.UNKNOWN_ERROR, e);
        }
    }

    @Override
    public void toJson(Api_Response instance, OutputStream out) {

    }

    @Override
    public void toProtobuf(GeneratedMessage instance, OutputStream out) {

    }

}
