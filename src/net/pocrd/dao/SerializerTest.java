package net.pocrd.dao;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;

import net.pocrd.api.resp.ApiNotification.Api_Notification;
import net.pocrd.api.resp.ApiResponse.Api_Response;
import net.pocrd.define.Serializer;
import net.pocrd.entity.ReturnCode;
import net.pocrd.entity.ReturnCodeException;
import net.pocrd.util.SerializerProvider;

import com.google.protobuf.GeneratedMessage;

public class SerializerTest implements Serializer<Api_Response> {
    Method m;

    @Override
    public void toXml(Api_Response instance, OutputStream out, boolean isRoot) {
        try {
            int size = instance.getNotificationCount();
            if (size > 0) {
                Serializer<Api_Notification> s = SerializerProvider.getSerializer(Api_Notification.class);
                if (s != null) {
                    out.write(1);
                    for (int i = 0; i < size; i++) {
                        Api_Notification n = instance.getNotification(i);
                        if (n != Api_Notification.getDefaultInstance()) {
                            s.toXml(n, out, false);
                        }
                    }
                }
            }

//            if (isRoot) {
//                out.write("</Api_Response>".getBytes(ConstField.UTF8));
//            }
        } catch (IOException e) {
            throw new ReturnCodeException(ReturnCode.UNKNOWN_ERROR, e);
        }
    try {
        out.write(1);
    } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
    }

    @Override
    public void toJson(Api_Response instance, OutputStream out, boolean isRoot) {

    }

    @Override
    public void toProtobuf(GeneratedMessage instance, OutputStream out) {

    }

}
