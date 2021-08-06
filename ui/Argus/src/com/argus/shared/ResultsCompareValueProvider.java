package com.argus.shared;

import java.lang.String;

import com.sencha.gxt.core.client.ValueProvider;

public class ResultsCompareValueProvider implements ValueProvider<ResultsCompare, String> {
	public final Integer indexKey;

	public ResultsCompareValueProvider(Integer specifiedKey) {
		this.indexKey = specifiedKey;
	}

	@Override
	public String getValue(ResultsCompare resultCompare) {
		if (null != resultCompare.getResultList()) {
			if (indexKey >= resultCompare.getResultList().size()) {
				return "NULLi";
			}
			return resultCompare.getResultList().get(indexKey);
		} else {
			return "NAV";
		}
	}

	@Override
	public String getPath() {
		return indexKey.toString();
	}

	@Override
	public void setValue(ResultsCompare object, String value) {
		// TODO Auto-generated method stub
		
	}
} 