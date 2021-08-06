package com.argus.client.util.valueproviders;
import com.argus.shared.DbSoftwareBuildsInterface;
import com.sencha.gxt.core.client.ValueProvider;

public class HashMapValueProvider implements ValueProvider<DbSoftwareBuildsInterface, String> {
    private String field;

    public HashMapValueProvider(String field) {
        this.field = field;
    }

    @Override
    public String getPath() {
        return field;
    }

    @Override
    public String getValue(DbSoftwareBuildsInterface object) {
        return object.getSoftware_builds().get(field);
    }

    @Override
    public void setValue(DbSoftwareBuildsInterface object, String value) {
        object.getSoftware_builds().put(field, value);
    }
}