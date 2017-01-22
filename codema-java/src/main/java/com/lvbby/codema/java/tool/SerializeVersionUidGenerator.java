package com.lvbby.codema.java.tool;

import com.lvbby.codema.java.entity.JavaClass;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by lipeng on 17/1/22.
 */
public class SerializeVersionUidGenerator {

    /***
     * stupid for now , only ussing the class name
     * @param cl
     * @return
     */
    public static long computeDefaultSUID(JavaClass cl) {
        try {
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            DataOutputStream dout = new DataOutputStream(bout);

            dout.writeUTF(cl.classFullName());

            int classMods = Modifier.PUBLIC;

            dout.writeInt(classMods);

            MessageDigest md = MessageDigest.getInstance("SHA");
            byte[] hashBytes = md.digest(bout.toByteArray());
            long hash = 0;
            for (int i = Math.min(hashBytes.length, 8) - 1; i >= 0; i--) {
                hash = (hash << 8) | (hashBytes[i] & 0xFF);
            }
            return hash;
        } catch (IOException ex) {
            throw new InternalError(ex);
        } catch (NoSuchAlgorithmException ex) {
            throw new SecurityException(ex.getMessage());
        }
    }
}
