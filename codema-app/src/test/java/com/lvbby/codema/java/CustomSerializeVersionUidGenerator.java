package com.lvbby.codema.java;

import com.lvbby.codema.java.entity.JavaClass;
import com.lvbby.codema.java.entity.JavaField;
import com.lvbby.codema.java.entity.JavaMethod;
import com.lvbby.codema.java.entity.JavaType;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Created by lipeng on 17/1/22.
 */
public class CustomSerializeVersionUidGenerator {

    public static long computeDefaultSUID(JavaClass cl) {
        //        if (!Serializable.class.isAssignableFrom(cl) || Proxy.isProxyClass(cl)) {
        //            return 0L;
        //        }

        try {
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            DataOutputStream dout = new DataOutputStream(bout);

            dout.writeUTF(cl.classFullName());

            int classMods = Modifier.PUBLIC;

            /*
             * compensate for javac bug in which ABSTRACT bit was set for an
             * interface only if the interface declared methods
             */
            List<JavaMethod> methods = cl.getMethods();
            if ((classMods & Modifier.INTERFACE) != 0) {
                classMods = (methods.size() > 0) ?
                        (classMods | Modifier.ABSTRACT) :
                        (classMods & ~Modifier.ABSTRACT);
            }
            dout.writeInt(classMods);

                /*
                 * compensate for change in 1.2FCS in which
                 * Class.getInterfaces() was modified to return Cloneable and
                 * Serializable for array classes.
                 */
            //            Class<?>[] interfaces = cl.getInterfaces();
            //            String[] ifaceNames = new String[interfaces.length];
            //            for (int i = 0; i < interfaces.length; i++) {
            //                ifaceNames[i] = interfaces[i].getName();
            //            }
            //            Arrays.sort(ifaceNames);
            //            for (int i = 0; i < ifaceNames.length; i++) {
            //                dout.writeUTF(ifaceNames[i]);
            //            }

            List<JavaField> fields = cl.getFields();
            MemberSignature[] fieldSigs = new MemberSignature[fields.size()];
            for (int i = 0; i < fields.size(); i++) {
                fieldSigs[i] = new MemberSignature(fields.get(i));
            }
            Arrays.sort(fieldSigs, new Comparator<MemberSignature>() {
                public int compare(MemberSignature ms1, MemberSignature ms2) {
                    return ms1.name.compareTo(ms2.name);
                }
            });
            for (int i = 0; i < fieldSigs.length; i++) {
                MemberSignature sig = fieldSigs[i];
                int mods = Modifier.PRIVATE;//TODO
                if (((mods & Modifier.PRIVATE) == 0) ||
                        ((mods & (Modifier.STATIC | Modifier.TRANSIENT)) == 0)) {
                    dout.writeUTF(sig.name);
                    dout.writeInt(mods);
                    dout.writeUTF(sig.signature);
                }
            }

            //            if (hasStaticInitializer(cl)) {
            //                dout.writeUTF("<clinit>");
            //                dout.writeInt(Modifier.STATIC);
            //                dout.writeUTF("()V");
            //            }

            if (1 == 1) {//TODO
//                MemberSignature sig = new MemberSignature()//Constructor
//                int mods = Modifier.PUBLIC;
//                if ((mods & Modifier.PRIVATE) == 0) {
//                    dout.writeUTF("<init>");
//                    dout.writeInt(mods);
//                    dout.writeUTF(sig.signature.replace('/', '.'));
//                }
            }

            MemberSignature[] methSigs = new MemberSignature[methods.size()];
            for (int i = 0; i < methods.size(); i++) {
                methSigs[i] = new MemberSignature(methods.get(i));
            }
            Arrays.sort(methSigs, new Comparator<MemberSignature>() {
                public int compare(MemberSignature ms1, MemberSignature ms2) {
                    int comp = ms1.name.compareTo(ms2.name);
                    if (comp == 0) {
                        comp = ms1.signature.compareTo(ms2.signature);
                    }
                    return comp;
                }
            });
            for (int i = 0; i < methSigs.length; i++) {
                MemberSignature sig = methSigs[i];
                int mods = Modifier.PUBLIC;//TODO
                if ((mods & Modifier.PRIVATE) == 0) {
                    dout.writeUTF(sig.name);
                    dout.writeInt(mods);
                    dout.writeUTF(sig.signature.replace('/', '.'));
                }
            }

            dout.flush();

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

    private static class MemberSignature {

        public final String name;
        public final String signature;

        public MemberSignature(JavaField field) {
            name = field.getName();
            signature = getClassSignature(field.getType());
        }

//        public MemberSignature(Constructor<?> cons) {
//            name = cons.getName();
//            signature = getMethodSignature(cons.getParameterTypes(), Void.TYPE);
//        }

        public MemberSignature(JavaMethod meth) {
            name = meth.getName();
            signature = getMethodSignature(meth);
        }
    }

    private static String getMethodSignature(JavaMethod method) {
        StringBuilder sbuf = new StringBuilder();
        sbuf.append('(');
        for (int i = 0; i < method.getArgs().size(); i++) {
            sbuf.append(getClassSignature(method.getArgs().get(i).getType()));
        }
        sbuf.append(')');
        sbuf.append(getClassSignature(method.getReturnType()));
        return sbuf.toString();
    }

    private static String getClassSignature(JavaType javaType) {
        StringBuilder sbuf = new StringBuilder();
        //        while (cl.isArray()) {
        //            sbuf.append('[');
        //            cl = cl.getComponentType();
        //        }
        Class cl = javaType.getJavaType();
        if (cl.isPrimitive()) {
            if (cl == Integer.TYPE) {
                sbuf.append('I');
            } else if (cl == Byte.TYPE) {
                sbuf.append('B');
            } else if (cl == Long.TYPE) {
                sbuf.append('J');
            } else if (cl == Float.TYPE) {
                sbuf.append('F');
            } else if (cl == Double.TYPE) {
                sbuf.append('D');
            } else if (cl == Short.TYPE) {
                sbuf.append('S');
            } else if (cl == Character.TYPE) {
                sbuf.append('C');
            } else if (cl == Boolean.TYPE) {
                sbuf.append('Z');
            } else if (cl == Void.TYPE) {
                sbuf.append('V');
            } else {
                throw new InternalError();
            }
        } else {
            sbuf.append('L' + cl.getName().replace('.', '/') + ';');
        }
        return sbuf.toString();
    }
}
