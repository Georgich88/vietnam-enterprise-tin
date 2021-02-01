package com.georgeisaev.vietnam.enterprise.tin.storage.configurations.datasource;

import com.georgeisaev.vietnam.enterprise.tin.storage.domain.enterprise.InformationSource;
import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.core.convert.CassandraCustomConversions;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;

import java.util.List;

public class CassandraConfig extends AbstractCassandraConfiguration {

	@Override
	public String getKeyspaceName() {
		return "vietnam_enterprise_tin";
	}

	@Bean
	CassandraCustomConversions getConversions() {
		return new CassandraCustomConversions(List.of(EnumWriteConverter.INSTANCE,
				EnumReadConverter.INSTANCE));
	}

	@WritingConverter
	enum EnumWriteConverter implements Converter<Enum<InformationSource>, String> {
		INSTANCE;

		@Override
		public String convert(Enum<InformationSource> source) {
			return source.name();
		}
	}

	@ReadingConverter
	enum EnumReadConverter implements Converter<String, Enum<InformationSource>> {
		INSTANCE;

		@Override
		public Enum<InformationSource> convert(String source) {
			return InformationSource.valueOf(source);
		}
	}

}
