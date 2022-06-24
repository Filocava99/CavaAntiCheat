package it.filippocavallari.cavaanticheat.common;

import java.io.*;

public class SerializationUtils {

    public static byte[] serialize(Object object) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(bos);
        out.writeObject(object);
        out.flush();
        byte[] packetToBytes = bos.toByteArray();
        out.close();
        bos.close();
        return packetToBytes;
    }

    public static Object deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        ObjectInputStream objectInputStream = new ObjectInputStream(bis);
        Object object = objectInputStream.readObject();
        objectInputStream.close();
        bis.close();
        return object;
    }

}
