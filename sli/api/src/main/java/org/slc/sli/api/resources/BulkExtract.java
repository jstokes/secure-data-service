/*
 * Copyright 2012 Shared Learning Collaborative, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.slc.sli.api.resources;

import org.apache.commons.lang3.tuple.Pair;
import org.slc.sli.api.security.RightsAllowed;
import org.slc.sli.api.security.SLIPrincipal;
import org.slc.sli.domain.Entity;
import org.slc.sli.domain.NeutralCriteria;
import org.slc.sli.domain.NeutralQuery;
import org.slc.sli.domain.Repository;
import org.slc.sli.domain.enums.Right;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.StreamingOutput;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.KeySpec;
import java.util.Date;

/**
 * 
 * @author dkornishev
 * 
 */
@Component
@Path("/bulk")
@Produces({ "application/x-tar" })
public class BulkExtract {

    private static final Logger LOG = LoggerFactory.getLogger(BulkExtract.class);

    private static final String SAMPLED_FILE_NAME = "NY-WALTON-2013-03-19T13-02-02.tar";

    public static final String BULK_EXTRACT_FILES = "bulkExtractFiles";
    public static final String BULK_EXTRACT_FILE_PATH = "path";
    public static final String BULK_EXTRACT_DATE = "date";

    @Autowired
    private Repository<Entity> mongoEntityRepository;

    private SLIPrincipal principal;

    private void initializePrincipal() {
        this.principal = (SLIPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    /**
     * Creates a streaming response for a sample data file
     * 
     * @return
     * @throws FileNotFoundException
     */
    @GET
    @Path("extract")
    @RightsAllowed({ Right.BULK_EXTRACT })
    public Response get() throws Exception {
        LOG.info("Received request to stream bulk extract...");

        final Pair<Cipher, AESDecryptKeys> cipherAESDecryptKeysPair = getCiphers();

        String fileName = SAMPLED_FILE_NAME;
        File bulkExtractFile = null;
        Entity bulkExtractFileEntity = bulkExtractFileEntity();
        String lastModified = "Not Specified";
        if (bulkExtractFileEntity != null) {
            bulkExtractFile = getbulkExtractFile(bulkExtractFileEntity);
            lastModified = ((Date) bulkExtractFileEntity.getBody().get(BULK_EXTRACT_DATE)).toString();
            fileName = bulkExtractFile.getName();
            LOG.info("Requested stream bulk extract file: {}", bulkExtractFile);
        }

        final InputStream is = bulkExtractFile == null || !bulkExtractFile.exists() ? this.getClass().getResourceAsStream("/bulkExtractSampleData/" + SAMPLED_FILE_NAME) : new FileInputStream(bulkExtractFile);

        StreamingOutput out = new StreamingOutput() {
            @Override
            public void write(OutputStream output) throws IOException, WebApplicationException {
                int n;
                byte[] buffer = new byte[1024];

                byte[] ivBytes = cipherAESDecryptKeysPair.getRight().getIvString().getBytes();
                byte[] secretBytes = cipherAESDecryptKeysPair.getRight().getSecretKey().getEncoded();
                PublicKey publicKey = null; //TODO
                byte[] encryptedIV = encryptDataWithRSAPublicKey(ivBytes, publicKey);
                byte[] encryptedSecret = encryptDataWithRSAPublicKey(secretBytes, publicKey);

                output.write(encryptedSecret.length);
                output.write(encryptedSecret);
                output.write(encryptedIV.length);
                output.write(encryptedIV);

                while ((n = is.read(buffer)) > -1) {
                    output.write(buffer, 0, n);
                }
            }
        };

        ResponseBuilder builder = Response.ok(out);
        builder.header("content-disposition", "attachment; filename = " + fileName);
        builder.header("last-modified", lastModified);
        return builder.build();
    }

    private File getbulkExtractFile(Entity bulkExtractFileEntity) {
        String fileName = (String) bulkExtractFileEntity.getBody().get(BULK_EXTRACT_FILE_PATH);
        File bulkExtractFile = new File(fileName);
        return bulkExtractFile;
    }

    private Entity bulkExtractFileEntity() {
        initializePrincipal();
        NeutralQuery tenantQuery = new NeutralQuery(new NeutralCriteria("tenantId", NeutralCriteria.OPERATOR_EQUAL, principal.getTenantId()));
        return mongoEntityRepository.findOne(BULK_EXTRACT_FILES, tenantQuery);
    }

    private byte[] encryptDataWithRSAPublicKey(byte[] rawData, PublicKey publicKey) {
        byte[] encryptedData = null;

        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            encryptedData = cipher.doFinal(rawData);
        } catch (NoSuchAlgorithmException e) {
            LOG.error("Exception: NoSuchAlgorithmException {}", e);
        } catch (NoSuchPaddingException e) {
            LOG.error("Exception: NoSuchPaddingException {}", e);
        } catch (InvalidKeyException e) {
            LOG.error("Exception: InvalidKeyException {}", e);
        } catch (BadPaddingException e) {
            LOG.error("Exception: BadPaddingException {}", e);
        } catch (IllegalBlockSizeException e) {
            LOG.error("Exception: IllegalBlockSizeException {}", e);
        }

        return encryptedData;
    }

    /**
     * @return the mongoEntityRepository
     */
    public Repository<Entity> getMongoEntityRepository() {
        return mongoEntityRepository;
    }

    /**
     * @param mongoEntityRepository the mongoEntityRepository to set
     */
    public void setMongoEntityRepository(Repository<Entity> mongoEntityRepository) {
        this.mongoEntityRepository = mongoEntityRepository;
    }

    private Pair<Cipher, AESDecryptKeys> getCiphers() throws Exception {

        char[] password = String.valueOf(System.currentTimeMillis()).toCharArray();
        byte[] salt = String.valueOf(System.currentTimeMillis()).getBytes();
        KeySpec spec = new PBEKeySpec(password, salt, 65536, 128);

        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        SecretKey tmp = factory.generateSecret(spec);
        SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");

        String ivString="1c345678a0123456";
        IvParameterSpec iv = new IvParameterSpec(ivString.getBytes());

        Cipher encrypt = Cipher.getInstance("AES/CBC/PKCS5Padding");
        encrypt.init(Cipher.ENCRYPT_MODE, secret, iv);

        AESDecryptKeys decryptKeys = new AESDecryptKeys();
        decryptKeys.setIvString(ivString);
        decryptKeys.setSecretKey(secret);

        return Pair.of(encrypt, decryptKeys);
    }

    class AESDecryptKeys {
        String ivString;
        SecretKey secretKey;

        public String getIvString() {
            return ivString;
        }

        public void setIvString(String ivString) {
            this.ivString = ivString;
        }

        public SecretKey getSecretKey() {
            return secretKey;
        }

        public void setSecretKey(SecretKey secretKey) {
            this.secretKey = secretKey;
        }
    }

}
