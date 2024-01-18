package com.xkyss.security;

import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.security.PrivateKey;
import java.security.PublicKey;

public class RsaUtil {

    public static void writePem(PrivateKey privateKey, String fileName) throws IOException {
        PemWriter pemWriter = new PemWriter(new FileWriter(fileName));
        try {
            pemWriter.writeObject(new PemObject("PRIVATE KEY", privateKey.getEncoded()));
        } finally {
            pemWriter.close();
        }
    }

    public static void writePem(PublicKey publicKey, String fileName) throws IOException {
        FileWriter writer = new FileWriter(fileName);
        JcaPEMWriter pemWriter = new JcaPEMWriter(writer);

        SubjectPublicKeyInfo subjectPublicKeyInfo = SubjectPublicKeyInfo.getInstance(publicKey.getEncoded());
        pemWriter.writeObject(subjectPublicKeyInfo);
        pemWriter.flush();
        pemWriter.close();
        writer.close();
    }
}
