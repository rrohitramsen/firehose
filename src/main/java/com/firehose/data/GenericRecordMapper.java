package com.firehose.data;

import com.twitter.bijection.Injection;
import com.twitter.bijection.avro.GenericAvroCodecs;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.reflect.ReflectData;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.util.Assert;

/**
 * @implNote  This class is only for mapping Java Object to Avro GenericData Record and vice versa.
 * Created by rohitkumar on 04/08/17.
 */
public class GenericRecordMapper {

    /**
     *
     * @param object
     * @return GenericData.Record
     */
    public static GenericData.Record mapObjectToRecord(StockPrice object) {

        Assert.notNull(object, "object must not be null");
        final Schema schema = ReflectData.get().getSchema(object.getClass());
        final GenericData.Record record = new GenericData.Record(schema);
        schema.getFields().forEach(
                field -> record.put(field.name(),
                        field.name().equals("date") ? object.getDate():
                        PropertyAccessorFactory.forDirectFieldAccess(object).getPropertyValue(field.name()))
        );
        return record;

    }

    /**
     *
     * @param classType
     * @param <T>
     * @return Injection<GenericRecord, byte[]>
     */
    public static <T> Injection<GenericRecord, byte[]> mapClassToRecordInjection(Class<T> classType) {

        Schema schema = ReflectData.get().getSchema(classType);
        Injection<GenericRecord, byte[]> recordInjection = GenericAvroCodecs.toBinary(schema);
        return recordInjection;

    }

    /**
     *
     * @param record
     * @param object
     * @param <T>
     * @return object of type T
     */
    public static <T> T mapRecordToObject(GenericRecord record, T object) {

        Assert.notNull(record, "record must not be null");
        Assert.notNull(object, "object must not be null");
        final Schema schema = ReflectData.get().getSchema(object.getClass());
        Assert.isTrue(schema.getFields().equals(record.getSchema().getFields()), "Schema fields didn't match");
        record.getSchema().getFields().forEach(
                d -> PropertyAccessorFactory.forDirectFieldAccess(object).setPropertyValue(d.name(),
                        record.get(d.name()) == null ? record.get(d.name()) : record.get(d.name()))
        );
        return object;

    }
}
