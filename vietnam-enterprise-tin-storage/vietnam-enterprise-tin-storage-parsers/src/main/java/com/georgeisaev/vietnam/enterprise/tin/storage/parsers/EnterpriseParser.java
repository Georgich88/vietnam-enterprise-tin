package com.georgeisaev.vietnam.enterprise.tin.storage.parsers;

import com.georgeisaev.vietnam.enterprise.tin.storage.domain.enterprise.Enterprise;

import java.util.List;

/**
 * @author Georgy Isaev
 */
public interface EnterpriseParser {

	List<Enterprise> parse() throws Exception;

}
