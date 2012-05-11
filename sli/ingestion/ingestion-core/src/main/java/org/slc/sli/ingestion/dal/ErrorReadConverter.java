package org.slc.sli.ingestion.dal;

import java.util.Date;

import com.mongodb.DBObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;

import org.slc.sli.dal.encrypt.EntityEncryption;
import org.slc.sli.ingestion.model.Error;

/**
 * Spring converter registered in the Mongo configuration to convert DBObjects
 * into NewBatchJob.
 *
 */
public class ErrorReadConverter implements Converter<DBObject, Error> {
	private static final Logger LOG = LoggerFactory.getLogger(NeutralRecordWriteConverter.class);
	EntityEncryption encryptor;

	public EntityEncryption getEncryptor() {
		return encryptor;
	}

	public void setEncryptor(EntityEncryption encryptor) {
		this.encryptor = encryptor;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Error convert(DBObject dbObj) {

		Error error = new Error();

		Object obj = dbObj.get("batchJobId");

		if (obj instanceof String) {
			error.setBatchJobId((String) obj);
		} else {
			LOG.info("batchJobId is invalid!");
		}

		obj = dbObj.get("stageName");

		if (obj instanceof String) {
			error.setStageName((String) obj);
		} else {
			LOG.info("statgeName is invalid! ");
		}

		obj = dbObj.get("resourceId");
		if (obj instanceof String) {
			error.setResourceId((String) obj);
		} else {
			LOG.info("resourceId is invalid! ");
		}

		obj = dbObj.get("sourceIp");
		if (obj instanceof String) {
			error.setSourceIp((String) obj);
		} else {
			LOG.info("sourceIp is invalid! ");
		}

		obj = dbObj.get("hostname");
		if (obj instanceof String) {
			error.setHostname((String) obj);
		} else {
			LOG.info("hostname is invalid! ");
		}

		obj = dbObj.get("timestamp");
		if (obj instanceof Date) {
			error.setTimestamp((Date) obj);
		} else {
			LOG.info("timestamp is invalid! ");
		}

		obj = dbObj.get("severity");
		if (obj instanceof String) {
			error.setSeverity((String) obj);
		} else {
			LOG.info("severity is invalid! ");
		}

		obj = dbObj.get("errorDetail");
		if (obj instanceof String) {
			error.setErrorDetail((String) encryptor.decryptSingleValue(obj));
		} else {
			LOG.info("errorDetail is invalid! ");
		}

		return error;

	}

}
