package com.georgeisaev.vietnam.enterprise.tin.storage.domain.enterprise;

import java.io.Serializable;

/**
 * Contains available resources for loading enterprise data from.
 * <p>
 * {@code BKAV} - e-invoices service
 * <br>
 * {@code NATIONAL_BUSINESS_REGISTRATION_PORTAL} - https://dichvuthongtin.dkkd.gov.vn
 *
 * @author Georgy Isaev
 */
public enum InformationSource implements Serializable {

	BKAV, NATIONAL_BUSINESS_REGISTRATION_PORTAL

}
