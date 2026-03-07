package ysharp.evaluator.Native.Util;

import ysharp.YsharpError;
import ysharp.evaluator.*;
import ysharp.parser.TypeTag;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HexFormat;
import java.util.List;

public class Y_Crypto {

    static {}

    public static class Y_CryptoInstance extends Y_Class.ClassObjectInstance {

        public Y_CryptoInstance() {}

        @Override
        public boolean isTruthy() {
            return true;
        }

        @Override
        public String getType() {
            return "Crypto";
        }

        @Override
        public String toString() {
            return "<instance:Crypto>";
        }
    }


    public static class Y_CryptoClass extends Y_Class.SealedClassObject {

        Y_CryptoClass() {
            this.prototype = Y_Class.ClassPrototype;

            // add static methods here


            // Crypto.md5(input: string) -> string  (hex)
            class Md5Fn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 1;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {

                    requireArity(arguments, 1, getClassName());

                    String input = requireString(arguments.getFirst(), getClassName(), 1);

                    try {
                        MessageDigest md = MessageDigest.getInstance("MD5");
                        byte[] digest = md.digest(input.getBytes(StandardCharsets.UTF_8));
                        String response = HexFormat.of().formatHex(digest);
                        return new Variable.Variant(new Y_String.Y_StringInstance(response));
                    } catch (NoSuchAlgorithmException e) {
                        throw new YsharpError(YsharpError.YsharpErrorType.PROCESS,
                                -1 ,
                                "Crypto.md5: algorithm not available");
                    }
                }

                @Override
                public String getFnName() {
                    return "md5";
                }
            }

            Md5Fn md5 = new Md5Fn();
            Variable md5Var = new Variable(
                    new Variable.Variant(md5),
                    true,
                    TypeTag.OBJECT);
            this.set(md5.getFnName(), md5Var);


            // Crypto.sha1(input: string) -> string  (hex)
            class Sha1Fn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 1;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {

                    requireArity(arguments, 1, getClassName());

                    String input = requireString(arguments.getFirst(), getClassName(), 1);

                    try {
                        MessageDigest md = MessageDigest.getInstance("SHA-1");
                        byte[] digest = md.digest(input.getBytes(StandardCharsets.UTF_8));
                        String response = HexFormat.of().formatHex(digest);
                        return new Variable.Variant(new Y_String.Y_StringInstance(response));
                    } catch (NoSuchAlgorithmException e) {
                        throw new YsharpError(YsharpError.YsharpErrorType.PROCESS ,
                                -1,
                                "Crypto.sha1: algorithm not available");
                    }
                }

                @Override
                public String getFnName() {
                    return "sha1";
                }
            }

            Sha1Fn sha1 = new Sha1Fn();
            Variable sha1Var = new Variable(
                    new Variable.Variant(sha1),
                    true,
                    TypeTag.OBJECT);
            this.set(sha1.getFnName(), sha1Var);


            // Crypto.sha256(input: string) -> string  (hex)
            class Sha256Fn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 1;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {

                    requireArity(arguments, 1, getClassName());

                    String input = requireString(arguments.getFirst(), getClassName(), 1);

                    try {
                        MessageDigest md = MessageDigest.getInstance("SHA-256");
                        byte[] digest = md.digest(input.getBytes(StandardCharsets.UTF_8));
                        String response = HexFormat.of().formatHex(digest);
                        return new Variable.Variant(new Y_String.Y_StringInstance(response));
                    } catch (NoSuchAlgorithmException e) {
                        throw new YsharpError(YsharpError.YsharpErrorType.PROCESS,
                                -1,
                                "Crypto.sha256: algorithm not available");
                    }
                }

                @Override
                public String getFnName() {
                    return "sha256";
                }
            }

            Sha256Fn sha256 = new Sha256Fn();
            Variable sha256Var = new Variable(
                    new Variable.Variant(sha256),
                    true,
                    TypeTag.OBJECT);
            this.set(sha256.getFnName(), sha256Var);


            // Crypto.sha512(input: string) -> string  (hex)
            class Sha512Fn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 1;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {

                    requireArity(arguments, 1, getClassName());

                    String input = requireString(arguments.getFirst(), getClassName(), 1);

                    try {
                        MessageDigest md = MessageDigest.getInstance("SHA-512");
                        byte[] digest = md.digest(input.getBytes(StandardCharsets.UTF_8));
                        String response = HexFormat.of().formatHex(digest);
                        return new Variable.Variant(new Y_String.Y_StringInstance(response));
                    } catch (NoSuchAlgorithmException e) {
                        throw new YsharpError(YsharpError.YsharpErrorType.PROCESS,
                                -1,
                                "Crypto.sha512: algorithm not available");
                    }
                }

                @Override
                public String getFnName() {
                    return "sha512";
                }
            }

            Sha512Fn sha512 = new Sha512Fn();
            Variable sha512Var = new Variable(
                    new Variable.Variant(sha512),
                    true,
                    TypeTag.OBJECT);
            this.set(sha512.getFnName(), sha512Var);


            // Crypto.hmacSha256(key: string, data: string) -> string  (hex)
            class HmacSha256Fn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 2;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {

                    requireArity(arguments, 2, getClassName());

                    String key  = requireString(arguments.get(0), getClassName(), 1);
                    String data = requireString(arguments.get(1), getClassName(), 2);

                    try {
                        Mac mac = Mac.getInstance("HmacSHA256");
                        SecretKeySpec keySpec = new SecretKeySpec(
                                key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
                        mac.init(keySpec);
                        byte[] digest = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
                        String response = HexFormat.of().formatHex(digest);
                        return new Variable.Variant(new Y_String.Y_StringInstance(response));
                    } catch (Exception e) {
                        throw new YsharpError(YsharpError.YsharpErrorType.PROCESS,
                                -1,
                                "Crypto.hmacSha256: " + e.getMessage());
                    }
                }

                @Override
                public String getFnName() {
                    return "hmacSha256";
                }
            }

            HmacSha256Fn hmacSha256 = new HmacSha256Fn();
            Variable hmacSha256Var = new Variable(
                    new Variable.Variant(hmacSha256),
                    true,
                    TypeTag.OBJECT);
            this.set(hmacSha256.getFnName(), hmacSha256Var);


            // Crypto.hmacSha512(key: string, data: string) -> string  (hex)
            class HmacSha512Fn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 2;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {

                    requireArity(arguments, 2, getClassName());

                    String key  = requireString(arguments.get(0), getClassName(), 1);
                    String data = requireString(arguments.get(1), getClassName(), 2);

                    try {
                        Mac mac = Mac.getInstance("HmacSHA512");
                        SecretKeySpec keySpec = new SecretKeySpec(
                                key.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
                        mac.init(keySpec);
                        byte[] digest = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
                        String response = HexFormat.of().formatHex(digest);
                        return new Variable.Variant(new Y_String.Y_StringInstance(response));
                    } catch (Exception e) {
                        throw new YsharpError(YsharpError.YsharpErrorType.PROCESS ,
                                -1,
                                "Crypto.hmacSha512: " + e.getMessage());
                    }
                }

                @Override
                public String getFnName() {
                    return "hmacSha512";
                }
            }

            HmacSha512Fn hmacSha512 = new HmacSha512Fn();
            Variable hmacSha512Var = new Variable(
                    new Variable.Variant(hmacSha512),
                    true,
                    TypeTag.OBJECT);
            this.set(hmacSha512.getFnName(), hmacSha512Var);



            // Crypto.base64Encode(input: string) -> string
            class Base64EncodeFn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 1;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {

                    requireArity(arguments, 1, getClassName());

                    String input = requireString(arguments.getFirst(), getClassName(), 1);
                    String response = Base64.getEncoder().encodeToString(
                            input.getBytes(StandardCharsets.UTF_8));

                    return new Variable.Variant(new Y_String.Y_StringInstance(response));
                }

                @Override
                public String getFnName() {
                    return "base64Encode";
                }
            }

            Base64EncodeFn base64Encode = new Base64EncodeFn();
            Variable base64EncodeVar = new Variable(
                    new Variable.Variant(base64Encode),
                    true,
                    TypeTag.OBJECT);
            this.set(base64Encode.getFnName(), base64EncodeVar);


            // Crypto.base64Decode(input: string) -> string
            class Base64DecodeFn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 1;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {

                    requireArity(arguments, 1, getClassName());

                    String input = requireString(arguments.getFirst(), getClassName(), 1);

                    try {
                        byte[] decoded = Base64.getDecoder().decode(input);
                        String response = new String(decoded, StandardCharsets.UTF_8);
                        return new Variable.Variant(new Y_String.Y_StringInstance(response));
                    } catch (IllegalArgumentException e) {
                        throw new YsharpError(YsharpError.YsharpErrorType.PROCESS,
                                -1,
                                "Crypto.base64Decode: invalid base64 string");
                    }
                }

                @Override
                public String getFnName() {
                    return "base64Decode";
                }
            }

            Base64DecodeFn base64Decode = new Base64DecodeFn();
            Variable base64DecodeVar = new Variable(
                    new Variable.Variant(base64Decode),
                    true,
                    TypeTag.OBJECT);
            this.set(base64Decode.getFnName(), base64DecodeVar);



            // Crypto.toHex(input: string) -> string
            class ToHexFn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 1;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {

                    requireArity(arguments, 1, getClassName());

                    String input = requireString(arguments.getFirst(), getClassName(), 1);
                    String response = HexFormat.of().formatHex(
                            input.getBytes(StandardCharsets.UTF_8));

                    return new Variable.Variant(new Y_String.Y_StringInstance(response));
                }

                @Override
                public String getFnName() {
                    return "toHex";
                }
            }

            ToHexFn toHex = new ToHexFn();
            Variable toHexVar = new Variable(
                    new Variable.Variant(toHex),
                    true,
                    TypeTag.OBJECT);
            this.set(toHex.getFnName(), toHexVar);


            // Crypto.fromHex(input: string) -> string
            class FromHexFn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 1;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {

                    requireArity(arguments, 1, getClassName());

                    String input = requireString(arguments.getFirst(), getClassName(), 1);

                    try {
                        byte[] bytes = HexFormat.of().parseHex(input);
                        String response = new String(bytes, StandardCharsets.UTF_8);
                        return new Variable.Variant(new Y_String.Y_StringInstance(response));
                    } catch (IllegalArgumentException e) {
                        throw new YsharpError( YsharpError.YsharpErrorType.PROCESS,
                                -1,
                                "Crypto.fromHex: invalid hex string");
                    }
                }

                @Override
                public String getFnName() {
                    return "fromHex";
                }
            }

            FromHexFn fromHex = new FromHexFn();
            Variable fromHexVar = new Variable(
                    new Variable.Variant(fromHex),
                    true,
                    TypeTag.OBJECT);
            this.set(fromHex.getFnName(), fromHexVar);



            // Crypto.randomBytes(length: int) -> string  (hex)
            class RandomBytesFn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 1;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {

                    requireArity(arguments, 1, getClassName());

                    int length = (int) requireInt(arguments.getFirst(), getClassName(), 1);

                    byte[] bytes = new byte[length];
                    new SecureRandom().nextBytes(bytes);
                    String response = HexFormat.of().formatHex(bytes);

                    return new Variable.Variant(new Y_String.Y_StringInstance(response));
                }

                @Override
                public String getFnName() {
                    return "randomBytes";
                }
            }

            RandomBytesFn randomBytes = new RandomBytesFn();
            Variable randomBytesVar = new Variable(
                    new Variable.Variant(randomBytes),
                    true,
                    TypeTag.OBJECT);
            this.set(randomBytes.getFnName(), randomBytesVar);


            // Crypto.uuid() -> string  (UUID v4)
            class UuidFn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 0;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {

                    String response = java.util.UUID.randomUUID().toString();

                    return new Variable.Variant(new Y_String.Y_StringInstance(response));
                }

                @Override
                public String getFnName() {
                    return "uuid";
                }
            }

            UuidFn uuid = new UuidFn();
            Variable uuidVar = new Variable(
                    new Variable.Variant(uuid),
                    true,
                    TypeTag.OBJECT);
            this.set(uuid.getFnName(), uuidVar);


            // Crypto.aesGenerateKey() -> string  (base64 encoded 256-bit key)
            class AesGenerateKeyFn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 0;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {

                    try {
                        KeyGenerator kg = KeyGenerator.getInstance("AES");
                        kg.init(256, new SecureRandom());
                        SecretKey key = kg.generateKey();
                        String response = Base64.getEncoder().encodeToString(key.getEncoded());
                        return new Variable.Variant(new Y_String.Y_StringInstance(response));
                    } catch (NoSuchAlgorithmException e) {
                        throw new YsharpError(YsharpError.YsharpErrorType.PROCESS,
                                -1,
                                "Crypto.aesGenerateKey: " + e.getMessage());
                    }
                }

                @Override
                public String getFnName() {
                    return "aesGenerateKey";
                }
            }

            AesGenerateKeyFn aesGenerateKey = new AesGenerateKeyFn();
            Variable aesGenerateKeyVar = new Variable(
                    new Variable.Variant(aesGenerateKey),
                    true,
                    TypeTag.OBJECT);
            this.set(aesGenerateKey.getFnName(), aesGenerateKeyVar);


            // Crypto.aesEncrypt(plaintext: string, base64Key: string) -> string  (base64: iv + ciphertext)
            class AesEncryptFn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 2;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {

                    requireArity(arguments, 2, getClassName());

                    String plaintext  = requireString(arguments.get(0), getClassName(), 1);
                    String base64Key  = requireString(arguments.get(1), getClassName(), 2);

                    try {
                        byte[] keyBytes = Base64.getDecoder().decode(base64Key);
                        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");

                        byte[] iv = new byte[16];
                        new SecureRandom().nextBytes(iv);
                        IvParameterSpec ivSpec = new IvParameterSpec(iv);

                        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
                        byte[] encrypted = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));

                        byte[] combined = new byte[iv.length + encrypted.length];
                        System.arraycopy(iv, 0, combined, 0, iv.length);
                        System.arraycopy(encrypted, 0, combined, iv.length, encrypted.length);

                        String response = Base64.getEncoder().encodeToString(combined);
                        return new Variable.Variant(new Y_String.Y_StringInstance(response));
                    } catch (Exception e) {
                        throw new YsharpError(YsharpError.YsharpErrorType.PROCESS,
                                -1,
                                "Crypto.aesEncrypt: " + e.getMessage());
                    }
                }

                @Override
                public String getFnName() {
                    return "aesEncrypt";
                }
            }

            AesEncryptFn aesEncrypt = new AesEncryptFn();
            Variable aesEncryptVar = new Variable(
                    new Variable.Variant(aesEncrypt),
                    true,
                    TypeTag.OBJECT);
            this.set(aesEncrypt.getFnName(), aesEncryptVar);


            // Crypto.aesDecrypt(base64Ciphertext: string, base64Key: string) -> string
            class AesDecryptFn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 2;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {

                    requireArity(arguments, 2, getClassName());

                    String base64Cipher = requireString(arguments.get(0), getClassName(), 1);
                    String base64Key    = requireString(arguments.get(1), getClassName(), 2);

                    try {
                        byte[] combined = Base64.getDecoder().decode(base64Cipher);
                        byte[] keyBytes = Base64.getDecoder().decode(base64Key);

                        byte[] iv         = new byte[16];
                        byte[] ciphertext = new byte[combined.length - 16];
                        System.arraycopy(combined, 0, iv, 0, 16);
                        System.arraycopy(combined, 16, ciphertext, 0, ciphertext.length);

                        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
                        IvParameterSpec ivSpec = new IvParameterSpec(iv);

                        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
                        byte[] decrypted = cipher.doFinal(ciphertext);

                        String response = new String(decrypted, StandardCharsets.UTF_8);
                        return new Variable.Variant(new Y_String.Y_StringInstance(response));
                    } catch (Exception e) {
                        throw new YsharpError(YsharpError.YsharpErrorType.PROCESS,
                                -1,
                                "Crypto.aesDecrypt: " + e.getMessage());
                    }
                }

                @Override
                public String getFnName() {
                    return "aesDecrypt";
                }
            }

            AesDecryptFn aesDecrypt = new AesDecryptFn();
            Variable aesDecryptVar = new Variable(
                    new Variable.Variant(aesDecrypt),
                    true,
                    TypeTag.OBJECT);
            this.set(aesDecrypt.getFnName(), aesDecryptVar);

            // Crypto.aesGcmEncrypt(plaintext: string, base64Key: string) -> string  (base64: iv + tag + ciphertext)
            class AesGcmEncryptFn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 2;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {

                    requireArity(arguments, 2, getClassName());

                    String plaintext = requireString(arguments.get(0), getClassName(), 1);
                    String base64Key = requireString(arguments.get(1), getClassName(), 2);

                    try {
                        byte[] keyBytes = Base64.getDecoder().decode(base64Key);
                        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");

                        byte[] iv = new byte[12];
                        new SecureRandom().nextBytes(iv);
                        GCMParameterSpec gcmSpec = new GCMParameterSpec(128, iv);

                        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
                        cipher.init(Cipher.ENCRYPT_MODE, keySpec, gcmSpec);
                        byte[] encrypted = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));

                        byte[] combined = new byte[iv.length + encrypted.length];
                        System.arraycopy(iv, 0, combined, 0, iv.length);
                        System.arraycopy(encrypted, 0, combined, iv.length, encrypted.length);

                        String response = Base64.getEncoder().encodeToString(combined);
                        return new Variable.Variant(new Y_String.Y_StringInstance(response));
                    } catch (Exception e) {
                        throw new YsharpError(YsharpError.YsharpErrorType.PROCESS,
                                -1,
                                "Crypto.aesGcmEncrypt: " + e.getMessage());
                    }
                }

                @Override
                public String getFnName() {
                    return "aesGcmEncrypt";
                }
            }

            AesGcmEncryptFn aesGcmEncrypt = new AesGcmEncryptFn();
            Variable aesGcmEncryptVar = new Variable(
                    new Variable.Variant(aesGcmEncrypt),
                    true,
                    TypeTag.OBJECT);
            this.set(aesGcmEncrypt.getFnName(), aesGcmEncryptVar);


            // Crypto.aesGcmDecrypt(base64Ciphertext: string, base64Key: string) -> string
            class AesGcmDecryptFn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 2;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {

                    requireArity(arguments, 2, getClassName());

                    String base64Cipher = requireString(arguments.get(0), getClassName(), 1);
                    String base64Key    = requireString(arguments.get(1), getClassName(), 2);

                    try {
                        byte[] combined = Base64.getDecoder().decode(base64Cipher);
                        byte[] keyBytes = Base64.getDecoder().decode(base64Key);

                        byte[] iv         = new byte[12];
                        byte[] ciphertext = new byte[combined.length - 12];
                        System.arraycopy(combined, 0, iv, 0, 12);
                        System.arraycopy(combined, 12, ciphertext, 0, ciphertext.length);

                        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
                        GCMParameterSpec gcmSpec = new GCMParameterSpec(128, iv);

                        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
                        cipher.init(Cipher.DECRYPT_MODE, keySpec, gcmSpec);
                        byte[] decrypted = cipher.doFinal(ciphertext);

                        String response = new String(decrypted, StandardCharsets.UTF_8);
                        return new Variable.Variant(new Y_String.Y_StringInstance(response));
                    } catch (Exception e) {
                        throw new YsharpError(YsharpError.YsharpErrorType.PROCESS,
                                -1,
                                "Crypto.aesGcmDecrypt: " + e.getMessage());
                    }
                }

                @Override
                public String getFnName() {
                    return "aesGcmDecrypt";
                }
            }

            AesGcmDecryptFn aesGcmDecrypt = new AesGcmDecryptFn();
            Variable aesGcmDecryptVar = new Variable(
                    new Variable.Variant(aesGcmDecrypt),
                    true,
                    TypeTag.OBJECT);
            this.set(aesGcmDecrypt.getFnName(), aesGcmDecryptVar);


            // Crypto.rsaGenerateKeyPair() -> string  (JSON: { publicKey, privateKey } both base64)
            class RsaGenerateKeyPairFn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 0;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {

                    try {
                        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
                        kpg.initialize(2048, new SecureRandom());
                        KeyPair kp = kpg.generateKeyPair();

                        String pub  = Base64.getEncoder().encodeToString(kp.getPublic().getEncoded());
                        String priv = Base64.getEncoder().encodeToString(kp.getPrivate().getEncoded());

                        String response = "{\"publicKey\":\"" + pub + "\",\"privateKey\":\"" + priv + "\"}";
                        return new Variable.Variant(new Y_String.Y_StringInstance(response));
                    } catch (NoSuchAlgorithmException e) {
                        throw new YsharpError(YsharpError.YsharpErrorType.PROCESS,
                                -1 ,
                                "Crypto.rsaGenerateKeyPair: " + e.getMessage());
                    }
                }

                @Override
                public String getFnName() {
                    return "rsaGenerateKeyPair";
                }
            }

            RsaGenerateKeyPairFn rsaGenerateKeyPair = new RsaGenerateKeyPairFn();
            Variable rsaGenerateKeyPairVar = new Variable(
                    new Variable.Variant(rsaGenerateKeyPair),
                    true,
                    TypeTag.OBJECT);
            this.set(rsaGenerateKeyPair.getFnName(), rsaGenerateKeyPairVar);


            // Crypto.rsaEncrypt(plaintext: string, base64PublicKey: string) -> string  (base64)
            class RsaEncryptFn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 2;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {

                    requireArity(arguments, 2, getClassName());

                    String plaintext      = requireString(arguments.get(0), getClassName(), 1);
                    String base64PubKey   = requireString(arguments.get(1), getClassName(), 2);

                    try {
                        byte[] keyBytes = Base64.getDecoder().decode(base64PubKey);
                        PublicKey publicKey = KeyFactory.getInstance("RSA")
                                .generatePublic(new X509EncodedKeySpec(keyBytes));

                        Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
                        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
                        byte[] encrypted = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));

                        String response = Base64.getEncoder().encodeToString(encrypted);
                        return new Variable.Variant(new Y_String.Y_StringInstance(response));
                    } catch (Exception e) {
                        throw new YsharpError(YsharpError.YsharpErrorType.PROCESS,
                                -1,
                                "Crypto.rsaEncrypt: " + e.getMessage());
                    }
                }

                @Override
                public String getFnName() {
                    return "rsaEncrypt";
                }
            }

            RsaEncryptFn rsaEncrypt = new RsaEncryptFn();
            Variable rsaEncryptVar = new Variable(
                    new Variable.Variant(rsaEncrypt),
                    true,
                    TypeTag.OBJECT);
            this.set(rsaEncrypt.getFnName(), rsaEncryptVar);


            // Crypto.rsaDecrypt(base64Ciphertext: string, base64PrivateKey: string) -> string
            class RsaDecryptFn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 2;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {

                    requireArity(arguments, 2, getClassName());

                    String base64Cipher   = requireString(arguments.get(0), getClassName(), 1);
                    String base64PrivKey  = requireString(arguments.get(1), getClassName(), 2);

                    try {
                        byte[] keyBytes = Base64.getDecoder().decode(base64PrivKey);
                        PrivateKey privateKey = KeyFactory.getInstance("RSA")
                                .generatePrivate(new PKCS8EncodedKeySpec(keyBytes));

                        Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
                        cipher.init(Cipher.DECRYPT_MODE, privateKey);
                        byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(base64Cipher));

                        String response = new String(decrypted, StandardCharsets.UTF_8);
                        return new Variable.Variant(new Y_String.Y_StringInstance(response));
                    } catch (Exception e) {
                        throw new YsharpError(YsharpError.YsharpErrorType.PROCESS,
                                -1,
                                "Crypto.rsaDecrypt: " + e.getMessage());
                    }
                }

                @Override
                public String getFnName() {
                    return "rsaDecrypt";
                }
            }

            RsaDecryptFn rsaDecrypt = new RsaDecryptFn();
            Variable rsaDecryptVar = new Variable(
                    new Variable.Variant(rsaDecrypt),
                    true,
                    TypeTag.OBJECT);
            this.set(rsaDecrypt.getFnName(), rsaDecryptVar);


            // Crypto.rsaSign(data: string, base64PrivateKey: string) -> string  (base64 signature)
            class RsaSignFn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 2;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {

                    requireArity(arguments, 2, getClassName());

                    String data          = requireString(arguments.get(0), getClassName(), 1);
                    String base64PrivKey = requireString(arguments.get(1), getClassName(), 2);

                    try {
                        byte[] keyBytes = Base64.getDecoder().decode(base64PrivKey);
                        PrivateKey privateKey = KeyFactory.getInstance("RSA")
                                .generatePrivate(new PKCS8EncodedKeySpec(keyBytes));

                        Signature sig = Signature.getInstance("SHA256withRSA");
                        sig.initSign(privateKey);
                        sig.update(data.getBytes(StandardCharsets.UTF_8));
                        byte[] signature = sig.sign();

                        String response = Base64.getEncoder().encodeToString(signature);
                        return new Variable.Variant(new Y_String.Y_StringInstance(response));
                    } catch (Exception e) {
                        throw new YsharpError(YsharpError.YsharpErrorType.PROCESS,
                                -1,
                                "Crypto.rsaSign: " + e.getMessage());
                    }
                }

                @Override
                public String getFnName() {
                    return "rsaSign";
                }
            }

            RsaSignFn rsaSign = new RsaSignFn();
            Variable rsaSignVar = new Variable(
                    new Variable.Variant(rsaSign),
                    true,
                    TypeTag.OBJECT);
            this.set(rsaSign.getFnName(), rsaSignVar);


            // Crypto.rsaVerify(data: string, base64Signature: string, base64PublicKey: string) -> bool
            class RsaVerifyFn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 3;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {

                    requireArity(arguments, 3, getClassName());

                    String data           = requireString(arguments.get(0), getClassName(), 1);
                    String base64Sig      = requireString(arguments.get(1), getClassName(), 2);
                    String base64PubKey   = requireString(arguments.get(2), getClassName(), 3);

                    try {
                        byte[] keyBytes = Base64.getDecoder().decode(base64PubKey);
                        PublicKey publicKey = KeyFactory.getInstance("RSA")
                                .generatePublic(new X509EncodedKeySpec(keyBytes));

                        Signature sig = Signature.getInstance("SHA256withRSA");
                        sig.initVerify(publicKey);
                        sig.update(data.getBytes(StandardCharsets.UTF_8));
                        boolean response = sig.verify(Base64.getDecoder().decode(base64Sig));

                        return new Variable.Variant(response);
                    } catch (Exception e) {
                        throw new YsharpError(YsharpError.YsharpErrorType.PROCESS,
                                -1,
                                "Crypto.rsaVerify: " + e.getMessage());
                    }
                }

                @Override
                public String getFnName() {
                    return "rsaVerify";
                }
            }

            RsaVerifyFn rsaVerify = new RsaVerifyFn();
            Variable rsaVerifyVar = new Variable(
                    new Variable.Variant(rsaVerify),
                    true,
                    TypeTag.OBJECT);
            this.set(rsaVerify.getFnName(), rsaVerifyVar);


            // Crypto.constantTimeEquals(a: string, b: string) -> bool  (timing-safe compare)
            class ConstantTimeEqualsFn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 2;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {

                    requireArity(arguments, 2, getClassName());

                    String a = requireString(arguments.get(0), getClassName(), 1);
                    String b = requireString(arguments.get(1), getClassName(), 2);

                    byte[] aBytes = a.getBytes(StandardCharsets.UTF_8);
                    byte[] bBytes = b.getBytes(StandardCharsets.UTF_8);

                    if (aBytes.length != bBytes.length) {
                        return new Variable.Variant(false);
                    }

                    int diff = 0;
                    for (int i = 0; i < aBytes.length; i++) {
                        diff |= aBytes[i] ^ bBytes[i];
                    }

                    boolean response = (diff == 0);
                    return new Variable.Variant(response);
                }

                @Override
                public String getFnName() {
                    return "constantTimeEquals";
                }
            }

            ConstantTimeEqualsFn constantTimeEquals = new ConstantTimeEqualsFn();
            Variable constantTimeEqualsVar = new Variable(
                    new Variable.Variant(constantTimeEquals),
                    true,
                    TypeTag.OBJECT);
            this.set(constantTimeEquals.getFnName(), constantTimeEqualsVar);


            // Crypto.pbkdf2(password: string, salt: string, iterations: int) -> string
            class Pbkdf2Fn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 3;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {

                    requireArity(arguments, 3, getClassName());

                    String password = requireString(arguments.get(0), getClassName(), 1);
                    String salt = requireString(arguments.get(1), getClassName(), 2);
                    int iterations = (int) requireInt(arguments.get(2), getClassName(), 3);

                    try {

                        javax.crypto.SecretKeyFactory skf =
                                javax.crypto.SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");

                        javax.crypto.spec.PBEKeySpec spec =
                                new javax.crypto.spec.PBEKeySpec(
                                        password.toCharArray(),
                                        salt.getBytes(StandardCharsets.UTF_8),
                                        iterations,
                                        256
                                );

                        byte[] key = skf.generateSecret(spec).getEncoded();
                        String response = HexFormat.of().formatHex(key);

                        return new Variable.Variant(new Y_String.Y_StringInstance(response));

                    } catch (Exception e) {

                        throw new YsharpError(
                                YsharpError.YsharpErrorType.PROCESS,
                                -1,
                                "Crypto.pbkdf2: " + e.getMessage()
                        );
                    }
                }

                @Override
                public String getFnName() {
                    return "pbkdf2";
                }
            }

            Pbkdf2Fn pbkdf2 = new Pbkdf2Fn();
            Variable pbkdf2Var = new Variable(
                    new Variable.Variant(pbkdf2),
                    true,
                    TypeTag.OBJECT);
            this.set(pbkdf2.getFnName(), pbkdf2Var);


            // Crypto.scrypt(password: string, salt: string) -> string
            class ScryptFn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 2;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {

                    requireArity(arguments, 2, getClassName());

                    String password = requireString(arguments.get(0), getClassName(), 1);
                    String salt = requireString(arguments.get(1), getClassName(), 2);

                    try {

                        javax.crypto.SecretKeyFactory skf =
                                javax.crypto.SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");

                        javax.crypto.spec.PBEKeySpec spec =
                                new javax.crypto.spec.PBEKeySpec(
                                        password.toCharArray(),
                                        salt.getBytes(StandardCharsets.UTF_8),
                                        65536,
                                        256
                                );

                        byte[] key = skf.generateSecret(spec).getEncoded();

                        String response = HexFormat.of().formatHex(key);

                        return new Variable.Variant(new Y_String.Y_StringInstance(response));

                    } catch (Exception e) {

                        throw new YsharpError(
                                YsharpError.YsharpErrorType.PROCESS,
                                -1,
                                "Crypto.scrypt: " + e.getMessage()
                        );
                    }
                }

                @Override
                public String getFnName() {
                    return "scrypt";
                }
            }

            ScryptFn scrypt = new ScryptFn();
            Variable scryptVar = new Variable(
                    new Variable.Variant(scrypt),
                    true,
                    TypeTag.OBJECT);
            this.set(scrypt.getFnName(), scryptVar);


            // Crypto.randomString(length: int) -> string
            class RandomStringFn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 1;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {

                    requireArity(arguments, 1, getClassName());

                    int length = (int) requireInt(arguments.get(0), getClassName(), 1);

                    final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

                    SecureRandom random = new SecureRandom();

                    StringBuilder sb = new StringBuilder();

                    for(int i = 0; i < length; i++) {
                        int index = random.nextInt(chars.length());
                        sb.append(chars.charAt(index));
                    }

                    return new Variable.Variant(new Y_String.Y_StringInstance(sb.toString()));
                }

                @Override
                public String getFnName() {
                    return "randomString";
                }
            }

            RandomStringFn randomString = new RandomStringFn();
            Variable randomStringVar = new Variable(
                    new Variable.Variant(randomString),
                    true,
                    TypeTag.OBJECT);
            this.set(randomString.getFnName(), randomStringVar);


            // Crypto.deriveKey(password: string, salt: string) -> string
            class DeriveKeyFn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 2;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {

                    requireArity(arguments, 2, getClassName());

                    String password = requireString(arguments.get(0), getClassName(), 1);
                    String salt = requireString(arguments.get(1), getClassName(), 2);

                    try {

                        javax.crypto.SecretKeyFactory skf =
                                javax.crypto.SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");

                        javax.crypto.spec.PBEKeySpec spec =
                                new javax.crypto.spec.PBEKeySpec(
                                        password.toCharArray(),
                                        salt.getBytes(StandardCharsets.UTF_8),
                                        100000,
                                        256
                                );

                        byte[] key = skf.generateSecret(spec).getEncoded();

                        String response = Base64.getEncoder().encodeToString(key);

                        return new Variable.Variant(new Y_String.Y_StringInstance(response));

                    } catch (Exception e) {

                        throw new YsharpError(
                                YsharpError.YsharpErrorType.PROCESS,
                                -1,
                                "Crypto.deriveKey: " + e.getMessage()
                        );
                    }
                }

                @Override
                public String getFnName() {
                    return "deriveKey";
                }
            }

            DeriveKeyFn deriveKey = new DeriveKeyFn();
            Variable deriveKeyVar = new Variable(
                    new Variable.Variant(deriveKey),
                    true,
                    TypeTag.OBJECT);
            this.set(deriveKey.getFnName(), deriveKeyVar);

        }


        @Override
        public int arity() {
            return 0;
        }

        @Override
        public Variable.Variant call(Interpreter interpreter,
                                     List<Variable.Variant> arguments)
                throws YsharpError {

            requireArity(arguments, 0, getClassName());

            Y_CryptoInstance instance = new Y_CryptoInstance();
            return new Variable.Variant(instance);
        }

        @Override
        public String getClassName() {
            return "Crypto";
        }

        @Override
        public String getType() {
            return "Crypto";
        }
    }


    public static void Register(Interpreter interpreter) throws Exception {

        Y_CryptoClass ctor = new Y_CryptoClass();

        Variable.Variant variant = new Variable.Variant(ctor);
        Variable var = new Variable(variant,
                true,
                TypeTag.OBJECT);

        interpreter.defineGlobal(ctor.getClassName(), var);
    }

}