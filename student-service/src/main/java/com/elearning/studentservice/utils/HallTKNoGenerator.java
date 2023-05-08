package com.elearning.studentservice.utils;

import com.elearning.studentservice.model.Student;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.enhanced.SequenceStyleGenerator;
import org.hibernate.internal.util.config.ConfigurationHelper;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.LongType;
import org.hibernate.type.Type;
import org.springframework.data.mapping.MappingException;

import java.io.Serializable;
import java.time.Year;
import java.util.Properties;

public class HallTKNoGenerator  extends SequenceStyleGenerator {

    public static final String DATE_FORMAT_PARAMETER = "dateFormat";
    public static final String DATE_FORMAT_DEFAULT = "%ty";
    private String dateFormat;


    public static final String VALUE_PREFIX_PARAMETER = "valuePrefix";
    public static final String VALUE_PREFIX_DEFAULT = "";
    private String valuePrefix;


    public static final String NUMBER_FORMAT_PARAMETER = "numberFormat";
    public static final String NUMBER_FORMAT_DEFAULT = "%03d";

    private String numberFormat;



    @Override
    public Serializable generate(SharedSessionContractImplementor session,
                                 Object object) throws HibernateException {
        String f = String.valueOf(((Student)object).getFname().charAt(0));
        String l = String.valueOf(((Student)object).getLname().charAt(0));
        return String.format(dateFormat, Year.now(), super.generate(session, object))+valuePrefix +
                String.format(numberFormat, super.generate(session, object))+f+l;
    }

    @Override
    public void configure(Type type, Properties params,
                          ServiceRegistry serviceRegistry) throws MappingException {
        super.configure(LongType.INSTANCE, params, serviceRegistry);
        dateFormat = ConfigurationHelper.getString(DATE_FORMAT_PARAMETER, params, DATE_FORMAT_DEFAULT);
        valuePrefix = ConfigurationHelper.getString(VALUE_PREFIX_PARAMETER,params, VALUE_PREFIX_DEFAULT);
        numberFormat = ConfigurationHelper.getString(NUMBER_FORMAT_PARAMETER, params, NUMBER_FORMAT_DEFAULT);

    }
}

